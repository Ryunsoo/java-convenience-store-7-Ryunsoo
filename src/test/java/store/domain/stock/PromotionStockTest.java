package store.domain.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.common.Name;
import store.domain.promotion.Benefit;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionStockTest {

    private Promotion defaultPromotion;

    @BeforeEach
    void setup() {
        Period period = Period.between(LocalDate.now(), LocalDate.now().plusDays(5));
        this.defaultPromotion = new Promotion(new Name("프로모션"), new Benefit(2), period);
    }

    @DisplayName("판매 가능한 재고 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"10, 5, 5", "5, 5, 5", "5, 10, 5", "0, 5, 0"})
    void returnAvailableQuantity(int stockQuantity, int purchaseQuantity, int availableQuantity) {
        Stock stock = new Stock(stockQuantity);
        PromotionStock promotionStock = new PromotionStock(stock, defaultPromotion);

        assertThat(promotionStock.availableQuantity(purchaseQuantity)).isEqualTo(availableQuantity);
    }

    @DisplayName("차감 가능한 최대 수량만큼 차감 후 차감 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"10, 5, 5", "5, 5, 5", "5, 10, 5", "0, 5, 0"})
    void deductAvailableQuantityAndReturn(int stockQuantity, int purchaseQuantity, int deductQuantity) {
        Stock stock = new Stock(stockQuantity);
        PromotionStock promotionStock = new PromotionStock(stock, defaultPromotion);

        int deducted = promotionStock.deductMaximum(purchaseQuantity);
        int remainStockQuantity = stockQuantity - deductQuantity;

        assertThat(deducted).isEqualTo(deductQuantity);
        assertThat(stock.getStock()).isEqualTo(new Stock(remainStockQuantity));
    }

}
