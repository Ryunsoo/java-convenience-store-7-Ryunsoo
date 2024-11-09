package store.domain.stock;

import store.domain.promotion.Promotion;

public class ProductStock {

    private final Promotion promotion;
    private final Stock stock;

    public ProductStock(Promotion promotion, Stock stock) {
        this.promotion = promotion;
        this.stock = stock;
    }

    public int availableQuantity(int quantity) {
        return stock.available(quantity);
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public Stock getStock() {
        return stock;
    }

}
