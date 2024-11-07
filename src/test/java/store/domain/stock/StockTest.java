package store.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockTest {

    @DisplayName("재고는 수량을 갖는다.")
    @Test
    void createStockWithQuantity() {
        Stock stock = new Stock(10);

        assertThat(stock).extracting("quantity").isEqualTo(10);
    }

}
