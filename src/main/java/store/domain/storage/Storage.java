package store.domain.storage;

import store.domain.promotion.Promotion;
import store.domain.stock.Stock;

public class Storage {

    private Promotion promotion;
    private Stock basicStock;
    private Stock promotionStock;

    private Storage(Promotion promotion, Stock basicStock, Stock promotionStock) {
        this.promotion = promotion;
        this.basicStock = basicStock;
        this.promotionStock = promotionStock;
    }

    public static Storage with(Promotion promotion, Stock basicStock, Stock promotionStock) {
        return new Storage(promotion, basicStock, promotionStock);
    }
}
