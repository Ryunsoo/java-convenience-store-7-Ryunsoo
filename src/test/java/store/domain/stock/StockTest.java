package store.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고는 수량을 갖는다.")
    @Test
    void createStockWithQuantity() {
        Stock stock = new Stock(10);

        assertThat(stock).extracting("quantity").isEqualTo(10);
    }

    @DisplayName("수량이 0 미만인 경우 예외를 던진다.")
    @Test
    void stockCannotHaveQuantityBelowZero() {
        assertThatThrownBy(() -> new Stock(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고는 0 이상이어야 합니다.");
    }

    @DisplayName("수량이 동일하면 같은 재고, 다르면 다른 재고이다.")
    @ParameterizedTest
    @CsvSource(value = {"5, 5, true", "5, 3, false"})
    void sameOfDifferentByQuantity(int quantity1, int quantity2, boolean expected) {
        Stock stock1 = new Stock(quantity1);
        Stock stock2 = new Stock(quantity2);

        assertThat(stock1.equals(stock2)).isEqualTo(expected);
    }

}
