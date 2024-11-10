package store.domain.stock;

import store.domain.product.Product;
import store.view.dto.StockView;

public class BasicStock extends ProductStock {

    public BasicStock(Stock stock) {
        super(stock);
    }

    public StockView getView(Product product) {
        return new StockView(product.name(), product.price(), stock.getStock());
    }

}
