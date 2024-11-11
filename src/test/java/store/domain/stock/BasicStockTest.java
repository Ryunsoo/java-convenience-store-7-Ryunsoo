package store.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class BasicStockTest {

    @DisplayName("판매 가능한 재고 수량을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"10, 5, 5", "5, 5, 5", "5, 10, 5", "0, 5, 0"})
    void returnAvailableQuantity(int stockQuantity, int purchaseQuantity, int availableQuantity) {
        Stock stock = new Stock(stockQuantity);
        BasicStock basicStock = new BasicStock(stock);

        assertThat(basicStock.availableQuantity(purchaseQuantity)).isEqualTo(availableQuantity);
    }

}
