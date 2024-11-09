package store.view;

import store.domain.stock.ProductStock;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.stock.Stock;

public class ProductInfo {

    private Product product;
    private Stock stock;
    private Promotion promotion;

    public ProductInfo(Product product, Stock stock, Promotion promotion) {
        this.product = product;
        this.stock = stock;
        this.promotion = promotion;
    }

    public Product getProduct() {
        return this.product;
    }

    public ProductStock getProductStock() {
        return new ProductStock(promotion, stock);
    }

    public boolean hasPromotion() {
        return this.promotion != null;
    }
}
