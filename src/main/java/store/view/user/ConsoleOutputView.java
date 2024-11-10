package store.view.user;

import store.domain.order.OrderSheet;
import store.domain.order.OrderSheets;
import store.domain.product.Price;
import store.view.dto.StockView;

import java.util.List;

public class ConsoleOutputView implements OutputView {

    private static final String ERROR_PREFIX = "[ERROR]";
    private static final String RETRY_MESSAGE = "다시 입력해 주세요.";

    @Override
    public void printWelcome() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    @Override
    public void printProductStocks(List<StockView> stockViews) {
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        for (StockView stockView : stockViews) {
            System.out.println("- " + stockView);
        }
        System.out.println();
    }

    @Override
    public void printOrderSheet(OrderSheets orderSheets, Price membershipDiscount) {
        System.out.println("===========W 편의점=============");
        printPurchaseList(orderSheets);
        System.out.println("===========증\t정=============");
        printGetFreeList(orderSheets);
        System.out.println("==============================");
        printAmountDetails(orderSheets, membershipDiscount);
        System.out.println();
    }

    @Override
    public void printPurchaseList(OrderSheets orderSheets) {
        System.out.println("상품명\t\t수량\t금액");
        List<OrderSheet> results = orderSheets.getOrderSheets();
        results.forEach(purchaseResult -> {
            System.out.printf("%s\t\t%d \t%s",
                    purchaseResult.productName(),
                    purchaseResult.totalQuantity(),
                    purchaseResult.totalAmount());
            System.out.println();
        });
    }

    private void printGetFreeList(OrderSheets orderSheets) {
        List<OrderSheet> results = orderSheets.onlyAppliedPromotion();
        results.forEach(purchaseResult -> {
            System.out.printf("%s\t\t%d", purchaseResult.productName(), purchaseResult.getFreeQuantity());
            System.out.println();
        });
    }

    private void printAmountDetails(OrderSheets orderSheets, Price membershipDiscount) {
        Price totalAmount = orderSheets.totalAmount();
        Price promotionAmount = orderSheets.promotionAmount();
        Price subtractPromotion = totalAmount.subtract(promotionAmount);
        Price payAmount = subtractPromotion.subtract(membershipDiscount);

        printTotal(orderSheets.totalQuantity(), totalAmount);
        printPromotionDiscount(promotionAmount);
        printMembershipDiscount(membershipDiscount);
        printPayAmount(payAmount);
    }

    private void printTotal(int totalQuantity, Price totalAmount) {
        System.out.printf("총구매액\t\t%d\t%s", totalQuantity, totalAmount);
        System.out.println();
    }

    private void printPromotionDiscount(Price amount) {
        System.out.printf("행사할인\t\t\t-%s", amount);
        System.out.println();
    }

    private void printMembershipDiscount(Price amount) {
        System.out.printf("멤버십할인\t\t\t-%s", amount);
        System.out.println();
    }

    private void printPayAmount(Price amount) {
        System.out.printf("내실돈\t\t\t %s", amount);
        System.out.println();
    }

    @Override
    public void printInputErrorMessage(String message) {
        System.out.printf("%s %s %s", ERROR_PREFIX, message, RETRY_MESSAGE);
        System.out.println();
        System.out.println();
    }

}
