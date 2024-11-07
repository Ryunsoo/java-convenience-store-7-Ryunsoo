package store.domain.storage;

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

    @DisplayName("저장소는 프로모션 및 일반/프로모션 재고를 갖는다.")
    @Test
    void createStorageWithPromotionAndStocks() {
        Promotion promotion = new Promotion(new Name("프로모션"), new Benefit(2),
                Period.between(LocalDate.now(), LocalDate.now().plusDays(1)));
        Stock basicStock = new Stock(10);
        Stock promotionStock = new Stock(5);

        Storage storage = Storage.with(promotion, basicStock, promotionStock);

        assertThat(storage).extracting("promotion", "basicStock", "promotionStock")
                .containsExactly(promotion, basicStock, promotionStock);
    }

}
