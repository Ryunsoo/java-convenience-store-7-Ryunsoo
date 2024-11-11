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

class ProductStocksTest {

    private Promotion defaultPromotion;

    @BeforeEach
    void setup() {
        Name name = new Name("프로모션");
        Benefit benefit = new Benefit(2);
        Period period = Period.between(LocalDate.now(), LocalDate.now().plusDays(5));
        this.defaultPromotion = new Promotion(name, benefit, period);
    }

    @DisplayName("일반 재고와 프로모션 재고가 충분한지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"5, true", "8, false"})
    void returnWhetherHasEnoughStocks(int quantity, boolean expected) {
        BasicStock basicStock = new BasicStock(new Stock(2));
        PromotionStock promotionStock = new PromotionStock(new Stock(3), defaultPromotion);
        ProductStocks productStocks = new ProductStocks(basicStock, promotionStock);

        assertThat(productStocks.hasEnough(quantity)).isEqualTo(expected);
    }

}
