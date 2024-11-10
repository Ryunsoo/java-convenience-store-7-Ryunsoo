package store.domain.stock;

import store.domain.promotion.PromotionResult;

import java.time.LocalDateTime;

public class ProductStocks {

    private final BasicStock basicStock;
    private final PromotionStock promotionStock;

    public ProductStocks(BasicStock basicStock, PromotionStock promotionStock) {
        this.basicStock = basicStock;
        this.promotionStock = promotionStock;
    }

    public boolean hasEnough(int quantity) {
        int availableBasic = basicStock.availableQuantity(quantity);
        int availablePromotion = promotionStock.availableQuantity(quantity);
        return availableBasic + availablePromotion >= quantity;
    }

    public PromotionResult checkPromotion(LocalDateTime dateTime, int quantity) {
        if (promotionStock.inPromotion(dateTime)) {
            return promotionStock.check(quantity);
        }
        return PromotionResult.withoutPromotion(quantity);
    }

    public void deduct(LocalDateTime dateTime, int quantity) {
        if (promotionStock.inPromotion(dateTime)) {
            int deducted = promotionStock.deductMaximum(quantity);
            basicStock.deductMaximum(quantity - deducted);
            return;
        }
        int deducted = basicStock.deductMaximum(quantity);
        promotionStock.deductMaximum(quantity - deducted);
    }

}
