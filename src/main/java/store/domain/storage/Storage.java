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

    public static Storage onlyPromotion(Promotion promotion, Stock promotionStock) {
        return new Storage(promotion, new Stock(0), promotionStock);
    }

    public static Storage onlyBasic(Stock basicStock) {
        return new Storage(null, basicStock, new Stock(0));
    }

}
