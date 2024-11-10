package store.domain.stock;

import java.time.LocalDateTime;

public class EmptyPromotionStock extends PromotionStock {

    public EmptyPromotionStock() {
        super(Stock.empty(), null);
    }

    @Override
    public boolean inPromotion(LocalDateTime dateTime) {
        return false;
    }

}
