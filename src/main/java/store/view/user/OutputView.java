package store.view.user;

import store.domain.order.OrderSheets;
import store.domain.product.Price;

public interface OutputView {
    void printInputErrorMessage(String message);
    void printOrderSheet(OrderSheets orderSheets, Price membershipDiscount);
    void printPurchaseList(OrderSheets orderSheets);
}
