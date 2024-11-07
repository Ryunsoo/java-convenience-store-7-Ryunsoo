package store.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}
