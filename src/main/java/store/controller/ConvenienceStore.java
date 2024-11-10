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

}
