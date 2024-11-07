package store.domain.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.common.Name;
import store.domain.promotion.Benefit;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;
import store.domain.stock.Stock;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class StorageTest {

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        this.promotion = new Promotion(new Name("프로모션"), new Benefit(2),
                Period.between(LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @DisplayName("저장소는 프로모션 및 일반/프로모션 재고를 갖는다.")
    @Test
    void createStorageWithPromotionAndStocks() {
        Stock basicStock = new Stock(10);
        Stock promotionStock = new Stock(5);

        Storage storage = Storage.with(promotion, basicStock, promotionStock);

        assertThat(storage).extracting("promotion", "basicStock", "promotionStock")
                .containsExactly(promotion, basicStock, promotionStock);
    }

    @DisplayName("프로모션 재고만 갖는 저장소는 일반 재고가 0 이다.")
    @Test
    void createStorageWithOnlyPromotionStock() {
        Storage storage = Storage.onlyPromotion(promotion, new Stock(5));

        assertThat(storage).extracting("basicStock").isEqualTo(new Stock(0));
    }

}
