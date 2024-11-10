package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.order.OrderSheet;
import store.domain.order.OrderSheets;
import store.domain.membership.Membership;
import store.domain.product.Price;
import store.domain.product.Product;
import store.domain.promotion.*;
import store.domain.stock.ProductStocks;
import store.domain.store.Products;
import store.domain.order.ShoppingCart;
import store.domain.store.Staff;
import store.view.user.InputView;
import store.view.user.OutputView;
import store.view.dto.PurchaseInfo;
import store.domain.common.YN;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConvenienceStore {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    private final Products products;

    public ConvenienceStore(Staff staff) {
        this.products = staff.prepareProducts();
    }

    public void open() {
        LocalDateTime now = DateTimes.now();
        Membership membership = new Membership(30, 8000);

        // TODO 상품 목록 출력

        while (true) {
            // 재고 확인
            ShoppingCart shoppingCart = fillShoppingCart();

            // 프로모션 적용 확인
            OrderSheets orderSheets = checkPromotions(shoppingCart, now);

            // 재고 차감
            updateStocks(orderSheets, now);

            Price membershipDiscount = tryMembershipDiscount(membership, orderSheets);
            // 영수증 출력
            outputView.printReceipt(orderSheets, membershipDiscount);

            YN answer = inputView.askStopPurchase();
            if (answer.no()) {
                break;
            }
        }
    }

    private void updateStocks(OrderSheets orderSheets, LocalDateTime dateTime) {
        Map<Product, Integer> finalQuantities = orderSheets.getFinalQuantities();

        for (Map.Entry<Product, Integer> entry : finalQuantities.entrySet()) {
            ProductStocks productsStocks = products.getStocks(entry.getKey());
            productsStocks.deduct(dateTime, entry.getValue());
        }
    }

    private OrderSheets checkPromotions(ShoppingCart shoppingCart, LocalDateTime dateTime) {
        List<OrderSheet> orderSheets = new ArrayList<>();
        Set<Product> products = shoppingCart.products();

        for (Product product : products) {
            int quantity = shoppingCart.getQuantity(product);
            OrderSheet orderSheet = confirmOrder(product, quantity, dateTime);
            orderSheets.add(orderSheet);
        }
        return new OrderSheets(orderSheets);
    }

    private OrderSheet confirmOrder(Product product, int orderQuantity, LocalDateTime dateTime) {
        ProductStocks productStocks = products.getStocks(product);
        StockCheckResult checkResult = productStocks.checkPromotion(dateTime, orderQuantity);

        if (checkResult.canAddMore()) {
            return askAddOneMore(product, checkResult);
        }
        if (checkResult.shouldCheckRemove()) {
            return askBuyOnlyPromotion(product, checkResult);
        }
        return checkResult.getOrderSheet(product);
    }

    private OrderSheet askAddOneMore(Product product, StockCheckResult checkResult) {
        YN answer = inputView.recheckAddOneMore(product);
        if (answer.yes()) {
            return checkResult.getOrderSheetWithOneMore(product);
        }
        return checkResult.getOrderSheet(product);
    }

    private OrderSheet askBuyOnlyPromotion(Product product, StockCheckResult checkResult) {
        YN answer = inputView.recheckContinueWithoutDiscount(product, checkResult.getGeneralQuantity());
        if (answer.yes()) {
            return checkResult.getOrderSheet(product);
        }
        return checkResult.getOrderSheetOnlyBenefit(product);
    }

    private ShoppingCart fillShoppingCart() {
        while (true) {
            try {
                return chooseProducts();
            } catch (IllegalArgumentException ex) {
                outputView.printInputErrorMessage(ex.getMessage());
            }
        }
    }

    private ShoppingCart chooseProducts() {
        List<PurchaseInfo> purchaseInfos = inputView.readPurchaseInfo();
        ShoppingCart shoppingCart = new ShoppingCart();

        for (PurchaseInfo purchaseInfo : purchaseInfos) {
            Product product = chooseProduct(purchaseInfo);
            shoppingCart.put(product, purchaseInfo.getQuantity());
        }
        return shoppingCart;
    }

    private Product chooseProduct(PurchaseInfo purchaseInfo) {
        Product product = products.find(purchaseInfo.getProductName());

        ProductStocks productsStocks = products.getStocks(product);
        boolean hasEnough = productsStocks.hasEnough(purchaseInfo.getQuantity());

        if (!hasEnough) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다.");
        }
        return product;
    }

    private Price tryMembershipDiscount(Membership membership, OrderSheets orderSheets) {
        YN answer = inputView.checkMembershipDiscount();
        if (answer.yes()) {
            Price nonPromotionAmount = orderSheets.nonPromotionAmount();
            return membership.calculateDiscount(nonPromotionAmount);
        }
        return Price.valueOf(0);
    }

//    private PurchaseResults order(Map<Product, ProductStocks> stockInfos, Map<Product, Integer> orders) {
//        List<PurchaseResult> purchaseResults = orders.entrySet().stream()
//                .map((entry) -> orderOneByOne(stockInfos, entry.getKey(), entry.getValue()))
//                .toList();
//        return new PurchaseResults(purchaseResults);
//    }

//    private PurchaseResult orderOneByOne(Map<Product, ProductStocks> stockInfos, Product product, int quantity) {
//        ProductStocks stockInfo = stockInfos.get(product);
//        PromotionStock promotionStock = stockInfo.getPromotionStock();
//        BasicStock basicStock = stockInfo.getBasicStock();
//
//        if (stockInfo.hasPromotion()) {
//            // 프로모션이 적용된 상품이다.
//            return purchase(product, quantity, promotionStock, basicStock);
//        } else {
//            //프로모션이 없는 상품이다.
//            return purchase(product, quantity, basicStock);
//        }
//    }

//    private PurchaseResult purchase(Product product, Integer quantity, BasicStock basicStock) {
//        basicStock.deduct(quantity);
//        return PurchaseResult.onlyBasic(product, quantity);
//    }

//    private PurchaseResult purchase(Product product, Integer quantity,
//                                    PromotionStock promotionStock, BasicStock basicStock) {
//        LocalDateTime now = DateTimes.now();
//
//        // 1. 프로모션 기간이다. (오늘을 기준으로)
//        if (promotionStock.inPromotion(now)) {
//            Promotion promotion = promotionStock.getPromotion();
//            int availableQty = promotionStock.availableQuantity(quantity);
//
//            if (availableQty >= quantity) {
//                // 프로모션 재고가 구매 수량보다 같거나 많다.
//                StockCheckResult promotionResult = promotion.checkDiscount(quantity);
//                BenefitResult benefitResult = promotionResult.getBenefitResult();
//                if (promotionResult.canGetFree()) {
//                    // 적용 수량 만큼 구매하면 증정 수량 추가에 대해 안내한다.
//                    YN answer = inputView.recheckAddOneMore(product);
//                    if (answer.yes()) {
//                        quantity += 1;
//                        // 프로모션 재고 차감 수량 : benefitResult 전부 + 1 (quantity)
//                        // 혜택 적용 수량 : benefitResult.applyQuantity + benefitResult.unapplyQuantity
//                        // 증정 수량 : benefitResult.freeQuantity + 1
//                        // 일반 결제 수량 : 0
//                        promotionStock.deduct(quantity);
//                        return new PurchaseResult(product,
//                                benefitResult.getApplyQuantity() + benefitResult.getUnapplyQuantity(),
//                                benefitResult.getFreeQuantity() + 1,
//                                0);
//                    }
//                    // 프로모션 재고 차감 수량 : benefitResult 전부 (quantity)
//                    // 혜택 적용 수량 : benefitResult.applyQuantity
//                    // 증정 수량 : benefitResult.freeQuantity
//                    // 일반 결제 수량 : benefitResult.unapplyQuantity
//                }
//                // 프로모션 재고 차감 수량 : benefitResult 전부 (quantity)
//                // 혜택 적용 수량 : benefitResult.applyQuantity
//                // 증정 수량 : benefitResult.freeQuantity
//                // 일반 결제 수량 : benefitResult.unapplyQuantity
//                promotionStock.deduct(quantity);
//                return new PurchaseResult(product,
//                        benefitResult.getApplyQuantity(),
//                        benefitResult.getFreeQuantity(),
//                        benefitResult.getUnapplyQuantity());
//            } else if (availableQty == 0) {
//                // 프로모션 재고가 없다.
//                YN answer = inputView.recheckContinueWithoutDiscount(product, quantity);
//                if (answer.yes()) {
//                    // 프로모션 재고 차감 수량 : 0
//                    // 일반 재고 차감 수량 : quantity
//                    // 혜택 적용 수량 : 0
//                    // 증정 수량 : o
//                    // 일반 결제 수량 : quantity
//                    basicStock.deduct(quantity);
//                    return PurchaseResult.onlyBasic(product, quantity);
//                } else {
//                    quantity = 0;
//                    // 혜택 적용 수량 : 0
//                    // 증정 수량 : 0
//                    // 일반 결제 수량 : 0
//                    return PurchaseResult.cancel(product);
//                }
//            } else {
//                // 프로모션 재고(4)가 구매 수량(6)보다 적다. (2+1)
//                int remainQuantity = quantity - availableQty;
//                StockCheckResult promotionResult = promotion.checkDiscount(availableQty);
//                BenefitResult benefitResult = promotionResult.getBenefitResult();
//
//                int noDiscountQuantity = benefitResult.getUnapplyQuantity() + remainQuantity;
//                YN answer = inputView.recheckContinueWithoutDiscount(product, noDiscountQuantity);
//                if (answer.yes()) {
//                    // 프로모션 재고 차감 수량 : availableQty
//                    // 일반 재고 차감 수량 : remainQuantity
//                    // 혜택 적용 수량 : benefitResult.applyQuantity
//                    // 증정 수량 : benefitResult.freeQuantity
//                    // 일반 결제 수량 : noDiscountQuantity
//                    promotionStock.deduct(availableQty);
//                    basicStock.deduct(remainQuantity);
//                    return new PurchaseResult(product, benefitResult.getApplyQuantity(),
//                            benefitResult.getFreeQuantity(), noDiscountQuantity);
//                } else {
//                    quantity -= noDiscountQuantity;
//                    // 프로모션 재고 차감 수량 : availableQty - benefitResult.getUnapplyQuantity
//                    // 일반 재고 차감 수량 : 0
//                    // 혜택 적용 수량 : benefitResult.applyQuantity
//                    // 증정 수량 : benefitResult.freeQuantity
//                    // 일반 결제 수량 : 0
//                    return new PurchaseResult(product, benefitResult.getApplyQuantity(),
//                            benefitResult.getFreeQuantity(), 0);
//                }
//            }
//        } else {
//            // 프로모션 기간이 아니다.
//            int basicQuantity = basicStock.availableQuantity(quantity);
//            basicStock.deduct(quantity);
//            int promotionQuantity = quantity - basicQuantity;
//            promotionStock.deduct(promotionQuantity);
//            return PurchaseResult.onlyBasic(product, quantity);
//        }
//    }



}
