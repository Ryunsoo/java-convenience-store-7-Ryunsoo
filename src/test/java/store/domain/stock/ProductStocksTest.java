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
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductStocksTest {

    private Name defaultPromotionName;
    private Benefit defaultBenefit;
    private Promotion defaultPromotion;

    @BeforeEach
    void setup() {
        this.defaultPromotionName = new Name("프로모션");
        this.defaultBenefit = new Benefit(2);
        Period period = Period.between(LocalDate.now(), LocalDate.now().plusDays(5));
        this.defaultPromotion = new Promotion(defaultPromotionName, defaultBenefit, period);
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

    @DisplayName("프로모션 기간이면 프로모션 재고를 먼저 차감하고, 아니면 일반 재고를 먼저 차감한다.")
    @ParameterizedTest
    @CsvSource(value = {"2024-11-01, 4, 0", "2024-12-01, 0, 4"})
    void deductStocksInPromotionPeriod(String purchaseDate, int remainBasicStock, int remainPromotionStock) {
        Period period = Period.between(LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 11, 30));

        BasicStock basicStock = new BasicStock(new Stock(5));
        PromotionStock promotionStock = new PromotionStock(new Stock(5), createPromotion(period));
        ProductStocks productStocks = new ProductStocks(basicStock, promotionStock);

        LocalDateTime dateTime = LocalDate.parse(purchaseDate).atStartOfDay();
        productStocks.deduct(dateTime, 6);

        assertThat(basicStock).extracting("stock").isEqualTo(new Stock(remainBasicStock));
        assertThat(promotionStock).extracting("stock").isEqualTo(new Stock(remainPromotionStock));
    }

    private Promotion createPromotion(Period period) {
        return new Promotion(defaultPromotionName, defaultBenefit, period);
    }

}
