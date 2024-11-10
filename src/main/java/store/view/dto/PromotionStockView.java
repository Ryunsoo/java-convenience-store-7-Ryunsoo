package store.view.dto;

import store.domain.common.Name;
import store.domain.product.Price;
import store.domain.stock.Stock;

public class PromotionStockView extends StockView {

    private final Name promotionName;

    public PromotionStockView(Name productName, Price productPrice, Stock productStock, Name promotionName) {
        super(productName, productPrice, productStock);
        this.promotionName = promotionName;
    }

    @Override
    public String toString() {
        return super.toString() + " " + promotionName;
    }
}
