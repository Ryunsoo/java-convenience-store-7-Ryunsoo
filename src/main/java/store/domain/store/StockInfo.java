package store.domain.store;

import store.domain.stock.ProductStock;
import store.domain.product.Product;
import store.domain.stock.PromotionStock;

public class StockInfo {

    private final Product product;
    private final ProductStock productStock;

    public StockInfo(Product product, ProductStock productStock) {
        this.product = product;
        this.productStock = productStock;
    }

    public boolean hasPromotion() {
        return productStock instanceof PromotionStock;
    }

    public Product getProduct() {
        return this.product;
    }

    public ProductStock getProductStock() {
        return this.productStock;
    }

}
