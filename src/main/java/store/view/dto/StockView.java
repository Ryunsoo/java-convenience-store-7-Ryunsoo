package store.view.dto;

import store.domain.common.Name;
import store.domain.product.Price;
import store.domain.stock.Stock;

public class StockView {

    private final Name productName;
    private final Price productPrice;
    private final Stock productStock;

    public StockView(Name productName, Price productPrice, Stock productStock) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    @Override
    public String toString() {
        return String.format("%s %s원 %S", productName, productPrice, productStock);
    }
}
