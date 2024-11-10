package store.view.user;

import store.domain.order.OrderSheets;
import store.domain.product.Price;
import store.view.dto.StockView;

import java.util.List;

public interface OutputView {
    void printWelcome();
    void printProductStocks(List<StockView> stockViews);
    void printOrderSheet(OrderSheets orderSheets, Price membershipDiscount);
    void printPurchaseList(OrderSheets orderSheets);
    void printInputErrorMessage(String message);
}
