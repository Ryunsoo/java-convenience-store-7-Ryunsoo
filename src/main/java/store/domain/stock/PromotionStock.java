package store.domain.stock;

import store.domain.promotion.Promotion;

import java.time.LocalDateTime;

public class PromotionStock {

    private final Promotion promotion;
    private final Stock stock;

    public PromotionStock(Promotion promotion, Stock stock) {
        this.promotion = promotion;
        this.stock = stock;
    }

    public static PromotionStock convert(ProductStock productStock) {
        return new PromotionStock(productStock.getPromotion(), productStock.getStock());
    }

    public boolean inPromotion(LocalDateTime dateTime) {
        return promotion.onGoing(dateTime);
    }

    public Stock getStock() {
        return stock;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int availableQuantity(int quantity) {
        return stock.available(quantity);
    }

    public void deduct(int quantity) {
        if (stock.available(quantity) != quantity) {
            throw new RuntimeException("차감할 수 있는 재고 수량을 초과했습니다.");
        }
        stock.deduct(quantity);
    }

}
