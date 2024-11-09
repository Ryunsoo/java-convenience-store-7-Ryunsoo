package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.*;
import store.domain.common.Name;
import store.view.dto.YN;
import store.domain.membership.Membership;
import store.domain.product.Price;
import store.domain.product.Product;
import store.domain.promotion.*;
import store.domain.stock.BasicStock;
import store.domain.stock.ProductStocks;
import store.domain.stock.PromotionStock;
import store.domain.stock.Stock;
import store.view.FileLineReader;
import store.view.InputView;
import store.view.OutputView;
import store.view.ProductInfo;
import store.view.dto.PurchaseInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvenienceStore {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void open() {
        // 프로모션 로딩
        FileLineReader fileLineReader = new FileLineReader();
        List<String> promotionLines = fileLineReader.readLines("promotions.md");

        List<Promotion> promotions = promotionLines.stream()
                .skip(1)
                .map((line) -> line.split(","))
                .map(Arrays::asList)
                .map(this::toPromotion)
                .toList();

        // 상품 로딩
        List<String> productLines = fileLineReader.readLines("products.md");

        List<ProductInfo> productInfos = productLines.stream()
                .skip(1)
                .map((line) -> line.split(","))
                .map(Arrays::asList)
                .map((strings) -> toProductInfo(strings, promotions))
                .toList();

        Map<Product, List<ProductInfo>> collectMap = productInfos.stream()
                .collect(Collectors.groupingBy(ProductInfo::getProduct));

        Map<Product, ProductStocks> productStocks = collectMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ProductStocks.with(e.getValue())));

        // 편의점 준비 끝

        Membership membership = new Membership(30, 8000);

        // TODO 상품 목록 출력

        while (true) {
            Map<Product, Integer> orders;
            while (true) {
                try {
                    orders = inputOrders(productStocks);
                    break;
                } catch (IllegalArgumentException ex) {
                    outputView.printInputErrorMessage(ex.getMessage());
                }
            }

            PurchaseResults purchaseResults = order(productStocks, orders);
            Price membershipDiscount = tryMembershipDiscount(membership, purchaseResults);
            // 영수증 출력
            outputView.printReceipt(purchaseResults, membershipDiscount);

            YN answer = inputView.askStopPurchase();
            if (answer.no()) {
                break;
            }
        }
    }

    private Price tryMembershipDiscount(Membership membership, PurchaseResults purchaseResults) {
        YN answer = inputView.checkMembershipDiscount();
        if (answer.yes()) {
            Price nonPromotionAmount = purchaseResults.nonPromotionAmount();
            return membership.calculateDiscount(nonPromotionAmount);
        }
        return Price.valueOf(0);
    }

    private PurchaseResults order(Map<Product, ProductStocks> stockInfos, Map<Product, Integer> orders) {
        List<PurchaseResult> purchaseResults = orders.entrySet().stream()
                .map((entry) -> orderOneByOne(stockInfos, entry.getKey(), entry.getValue()))
                .toList();
        return new PurchaseResults(purchaseResults);
    }

    private PurchaseResult orderOneByOne(Map<Product, ProductStocks> stockInfos, Product product, int quantity) {
        ProductStocks stockInfo = stockInfos.get(product);
        PromotionStock promotionStock = stockInfo.getPromotionStock();
        BasicStock basicStock = stockInfo.getBasicStock();

        if (stockInfo.hasPromotion()) {
            // 프로모션이 적용된 상품이다.
            return purchase(product, quantity, promotionStock, basicStock);
        } else {
            //프로모션이 없는 상품이다.
            return purchase(product, quantity, basicStock);
        }
    }

    private PurchaseResult purchase(Product product, Integer quantity, BasicStock basicStock) {
        basicStock.deduct(quantity);
        return PurchaseResult.onlyBasic(product, quantity);
    }

    private PurchaseResult purchase(Product product, Integer quantity,
                                    PromotionStock promotionStock, BasicStock basicStock) {
        LocalDateTime now = DateTimes.now();

        // 1. 프로모션 기간이다. (오늘을 기준으로)
        if (promotionStock.inPromotion(now)) {
            Promotion promotion = promotionStock.getPromotion();
            int availableQty = promotionStock.availableQuantity(quantity);

            if (availableQty >= quantity) {
                // 프로모션 재고가 구매 수량보다 같거나 많다.
                PromotionResult promotionResult = promotion.checkDiscount(quantity);
                BenefitResult benefitResult = promotionResult.getBenefitResult();
                if (promotionResult.canGetFree()) {
                    // 적용 수량 만큼 구매하면 증정 수량 추가에 대해 안내한다.
                    YN answer = inputView.recheckAddOneMore(product);
                    if (answer.yes()) {
                        quantity += 1;
                        // 프로모션 재고 차감 수량 : benefitResult 전부 + 1 (quantity)
                        // 혜택 적용 수량 : benefitResult.applyQuantity + benefitResult.unapplyQuantity
                        // 증정 수량 : benefitResult.freeQuantity + 1
                        // 일반 결제 수량 : 0
                        promotionStock.deduct(quantity);
                        return new PurchaseResult(product,
                                benefitResult.getApplyQuantity() + benefitResult.getUnapplyQuantity(),
                                benefitResult.getFreeQuantity() + 1,
                                0);
                    }
                    // 프로모션 재고 차감 수량 : benefitResult 전부 (quantity)
                    // 혜택 적용 수량 : benefitResult.applyQuantity
                    // 증정 수량 : benefitResult.freeQuantity
                    // 일반 결제 수량 : benefitResult.unapplyQuantity
                }
                // 프로모션 재고 차감 수량 : benefitResult 전부 (quantity)
                // 혜택 적용 수량 : benefitResult.applyQuantity
                // 증정 수량 : benefitResult.freeQuantity
                // 일반 결제 수량 : benefitResult.unapplyQuantity
                promotionStock.deduct(quantity);
                return new PurchaseResult(product,
                        benefitResult.getApplyQuantity(),
                        benefitResult.getFreeQuantity(),
                        benefitResult.getUnapplyQuantity());
            } else if (availableQty == 0) {
                // 프로모션 재고가 없다.
                YN answer = inputView.recheckContinueWithoutDiscount(product, quantity);
                if (answer.yes()) {
                    // 프로모션 재고 차감 수량 : 0
                    // 일반 재고 차감 수량 : quantity
                    // 혜택 적용 수량 : 0
                    // 증정 수량 : o
                    // 일반 결제 수량 : quantity
                    basicStock.deduct(quantity);
                    return PurchaseResult.onlyBasic(product, quantity);
                } else {
                    quantity = 0;
                    // 혜택 적용 수량 : 0
                    // 증정 수량 : 0
                    // 일반 결제 수량 : 0
                    return PurchaseResult.cancel(product);
                }
            } else {
                // 프로모션 재고(4)가 구매 수량(6)보다 적다. (2+1)
                int remainQuantity = quantity - availableQty;
                PromotionResult promotionResult = promotion.checkDiscount(availableQty);
                BenefitResult benefitResult = promotionResult.getBenefitResult();

                int noDiscountQuantity = benefitResult.getUnapplyQuantity() + remainQuantity;
                YN answer = inputView.recheckContinueWithoutDiscount(product, noDiscountQuantity);
                if (answer.yes()) {
                    // 프로모션 재고 차감 수량 : availableQty
                    // 일반 재고 차감 수량 : remainQuantity
                    // 혜택 적용 수량 : benefitResult.applyQuantity
                    // 증정 수량 : benefitResult.freeQuantity
                    // 일반 결제 수량 : noDiscountQuantity
                    promotionStock.deduct(availableQty);
                    basicStock.deduct(remainQuantity);
                    return new PurchaseResult(product, benefitResult.getApplyQuantity(),
                            benefitResult.getFreeQuantity(), noDiscountQuantity);
                } else {
                    quantity -= noDiscountQuantity;
                    // 프로모션 재고 차감 수량 : availableQty - benefitResult.getUnapplyQuantity
                    // 일반 재고 차감 수량 : 0
                    // 혜택 적용 수량 : benefitResult.applyQuantity
                    // 증정 수량 : benefitResult.freeQuantity
                    // 일반 결제 수량 : 0
                    return new PurchaseResult(product, benefitResult.getApplyQuantity(),
                            benefitResult.getFreeQuantity(), 0);
                }
            }
        } else {
            // 프로모션 기간이 아니다.
            int basicQuantity = basicStock.availableQuantity(quantity);
            basicStock.deduct(quantity);
            int promotionQuantity = quantity - basicQuantity;
            promotionStock.deduct(promotionQuantity);
            return PurchaseResult.onlyBasic(product, quantity);
        }
    }

    private Map<Product, Integer> inputOrders(Map<Product, ProductStocks> productStocks) {
        List<PurchaseInfo> purchaseInfos = inputView.readPurchaseInfo();
        Map<Product, Integer> orders = new HashMap<>();
        for (PurchaseInfo purchaseInfo : purchaseInfos) {
            // 구매 가능 여부 확인
            Product product = purchaseInfo.chooseProduct(productStocks.keySet());
            int quantity = purchaseInfo.getQuantity();

            ProductStocks stocks = productStocks.get(product);
            if (!stocks.isAvailable(quantity)) {
                throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다.");
            }
            orders.put(product, quantity);
        }
        return orders;
    }

    private ProductInfo toProductInfo(List<String> strings, List<Promotion> promotions) {
        int NAME_INDEX = 0;
        int PRICE_INDEX = 1;
        int QUANTITY_INDEX = 2;
        int PROMOTION_NAME_INDEX = 3;

        Name productName = new Name(strings.get(NAME_INDEX));
        Price price = Price.valueOf(Long.parseLong(strings.get(PRICE_INDEX)));
        Product product = new Product(productName, price);

        Stock stock = new Stock(Integer.parseInt(strings.get(QUANTITY_INDEX)));

        Name promotionName = new Name(strings.get(PROMOTION_NAME_INDEX));

        Promotion promotion = promotions.stream()
                .filter(pro -> pro.is(promotionName))
                .findAny()
                .orElse(null);

        return new ProductInfo(product, stock, promotion);
    }

    private Promotion toPromotion(List<String> infos) {
        int NAME_INDEX = 0;
        int BUY_INDEX = 1;
        int START_DATE_INDEX = 3;
        int END_DATE_INDEX = 4;

        Name name = new Name(infos.get(NAME_INDEX));
        Benefit benefit = new Benefit(Integer.parseInt(infos.get(BUY_INDEX)));

        LocalDate startDate = LocalDate.parse(infos.get(START_DATE_INDEX), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(infos.get(END_DATE_INDEX), DateTimeFormatter.ISO_LOCAL_DATE);

        Period period = Period.between(startDate, endDate);

        return new Promotion(name, benefit, period);
    }

}
