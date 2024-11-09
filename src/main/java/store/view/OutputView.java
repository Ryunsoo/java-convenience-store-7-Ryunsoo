package store.view;

import store.domain.PurchaseResult;
import store.domain.PurchaseResults;
import store.domain.product.Price;

import java.util.List;

public class OutputView {

    private static final String ERROR_PREFIX = "[ERROR]";
    private static final String RETRY_MESSAGE = "다시 입력해 주세요.";

    public void printInputErrorMessage(String message) {
        System.out.printf("%s %s %s", ERROR_PREFIX, message, RETRY_MESSAGE);
        System.out.println();
        System.out.println();
    }

    public void printReceipt(PurchaseResults purchaseResults, Price membershipDiscount) {
        System.out.println("===========W 편의점=============");
        printPurchaseList(purchaseResults);
        System.out.println("===========증\t정=============");
        printGetFreeList(purchaseResults);
        System.out.println("==============================");
        printAmountDetails(purchaseResults, membershipDiscount);
        System.out.println();
    }

    private void printPurchaseList(PurchaseResults purchaseResults) {
        System.out.println("상품명\t\t수량\t금액");
        List<PurchaseResult> results = purchaseResults.getPurchaseResults();
        results.forEach(purchaseResult -> {
            System.out.printf("%s\t\t%d \t%s",
                    purchaseResult.productName(),
                    purchaseResult.totalQuantity(),
                    purchaseResult.totalAmount());
            System.out.println();
        });
    }

    private void printGetFreeList(PurchaseResults purchaseResults) {
        List<PurchaseResult> results = purchaseResults.onlyAppliedPromotion();
        results.forEach(purchaseResult -> {
            System.out.printf("%s\t\t%d", purchaseResult.productName(), purchaseResult.getFreeQuantity());
            System.out.println();
        });
    }

    private void printAmountDetails(PurchaseResults purchaseResults, Price membershipDiscount) {
        Price totalAmount = purchaseResults.totalAmount();
        Price promotionAmount = purchaseResults.promotionAmount();
        Price subtractPromotion = totalAmount.subtract(promotionAmount);
        Price payAmount = subtractPromotion.subtract(membershipDiscount);

        System.out.printf("총구매액\t\t%d\t%s", purchaseResults.totalQuantity(), totalAmount);
        System.out.println();
        System.out.printf("행사할인\t\t\t-%s", promotionAmount);
        System.out.println();
        System.out.printf("멤버십할인\t\t\t-%s", membershipDiscount);
        System.out.println();
        System.out.printf("내실돈\t\t\t %s", payAmount);
        System.out.println();
    }

}
