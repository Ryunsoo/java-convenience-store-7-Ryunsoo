package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.common.YN;
import store.domain.membership.Membership;
import store.domain.order.OrderSheet;
import store.domain.order.OrderSheets;
import store.domain.store.ShoppingCart;
import store.domain.product.Price;
import store.domain.product.Product;
import store.domain.promotion.PromotionResult;
import store.domain.store.Staff;
import store.view.dto.PurchaseInfo;
import store.view.dto.StockView;
import store.view.setup.StoreDataProvider;
import store.view.user.InputView;
import store.view.user.OutputView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConvenienceStoreController {

    private static final int MEMBERSHIP_DISCOUNT_PERCENTAGE = 30;
    private static final long MEMBERSHIP_LIMIT_AMOUNT = 8000;

    private final InputView inputView;
    private final OutputView outputView;
    private final ConvenienceStore convenienceStore;

    public ConvenienceStoreController(InputView inputView, OutputView outputView, StoreDataProvider storeDataProvider) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.convenienceStore = createConvenienceStore(storeDataProvider);
    }

    private ConvenienceStore createConvenienceStore(StoreDataProvider storeDataProvider) {
        Staff staff = new Staff(storeDataProvider);
        return new ConvenienceStore(staff);
    }

    public void execute() {
        Membership membership = new Membership(MEMBERSHIP_DISCOUNT_PERCENTAGE, MEMBERSHIP_LIMIT_AMOUNT);
        do {
            LocalDateTime now = DateTimes.now();
            printConvenienceStart();
            OrderSheets orderSheets = order(now);
            convenienceStore.updateStocks(orderSheets, now);
            Price membershipDiscount = tryMembershipDiscount(membership, orderSheets);
            outputView.printOrderSheet(orderSheets, membershipDiscount);
        } while (keepBuying());
    }

    private void printConvenienceStart() {
        outputView.printWelcome();
        List<StockView> currentStockInfos = convenienceStore.getCurrentStockInfo();
        outputView.printProductStocks(currentStockInfos);
    }

    private OrderSheets order(LocalDateTime orderDateTime) {
        ShoppingCart shoppingCart = trySelectProducts();
        return checkPromotions(shoppingCart, orderDateTime);
    }

    private ShoppingCart trySelectProducts() {
        try {
            return selectProducts();
        } catch (IllegalArgumentException ex) {
            outputView.printInputErrorMessage(ex.getMessage());
            return trySelectProducts();
        }
    }

    private ShoppingCart selectProducts() {
        List<PurchaseInfo> purchaseInfos = inputView.readPurchaseInfo();
        ShoppingCart shoppingCart = new ShoppingCart();

        for (PurchaseInfo purchaseInfo : purchaseInfos) {
            Product product = convenienceStore.selectProduct(purchaseInfo);
            shoppingCart.put(product, purchaseInfo.getQuantity());
        }
        return shoppingCart;
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
        PromotionResult checkResult = convenienceStore.applyPromotion(product, orderQuantity, dateTime);

        if (checkResult.canAddMore()) {
            return askAddOneMore( product, checkResult);
        }
        if (checkResult.shouldCheckRemove()) {
            return askBuyOnlyPromotion(product, checkResult);
        }
        return checkResult.getOrderSheet(product);
    }

    private OrderSheet askAddOneMore(Product product, PromotionResult checkResult) {
        YN answer = inputView.recheckAddOneMore(product);
        if (answer.yes()) {
            return checkResult.getOrderSheetWithOneMore(product);
        }
        return checkResult.getOrderSheet(product);
    }

    private OrderSheet askBuyOnlyPromotion(Product product, PromotionResult checkResult) {
        YN answer = inputView.recheckContinueWithoutDiscount(product, checkResult.getGeneralQuantity());
        if (answer.yes()) {
            return checkResult.getOrderSheet(product);
        }
        return checkResult.getOrderSheetOnlyBenefit(product);
    }

    private Price tryMembershipDiscount(Membership membership, OrderSheets orderSheets) {
        YN answer = inputView.checkMembershipDiscount();
        if (answer.yes()) {
            Price nonPromotionAmount = orderSheets.nonPromotionAmount();
            return membership.calculateDiscount(nonPromotionAmount);
        }
        return Price.valueOf(0);
    }

    private boolean keepBuying() {
        YN answer = inputView.askStopPurchase();
        return answer.yes();
    }

}
