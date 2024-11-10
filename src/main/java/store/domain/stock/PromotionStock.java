package store.domain.stock;

import store.domain.product.Product;
import store.domain.promotion.BenefitResult;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionResult;
import store.view.dto.PromotionStockView;
import store.view.dto.StockView;

import java.time.LocalDateTime;

public class PromotionStock extends ProductStock {

    private final Promotion promotion;

    public PromotionStock(Stock stock, Promotion promotion) {
        super(stock);
        this.promotion = promotion;
    }

    public boolean inPromotion(LocalDateTime dateTime) {
        return promotion.onGoing(dateTime);
    }

    public PromotionResult check(int quantity) {
        BenefitResult benefitResult = checkPromotion(quantity);
        int generalQuantity = quantity - benefitResult.getQuantity();

        if (stock.hasMore(quantity) && promotion.canGetOneMore(generalQuantity)) {
            return PromotionResult.morePromotion(benefitResult, generalQuantity);
        }

        return PromotionResult.withPromotion(benefitResult, generalQuantity);
    }

    private BenefitResult checkPromotion(int quantity) {
        int availableQuantity = stock.available(quantity);
        return promotion.getBenefitResult(availableQuantity);
    }

    public StockView getView(Product product) {
        return new PromotionStockView(product.name(), product.price(),
                stock.getStock(), promotion.name());
    }

}
