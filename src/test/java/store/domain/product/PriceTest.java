package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    @DisplayName("값을 갖는 가격을 생성할 수 있다.")
    @Test
    void createPriceWithValue() {
        Price price = Price.valueOf(1000);

        BigDecimal expected = BigDecimal.valueOf(1000);
        assertThat(price).extracting("price").isEqualTo(expected);
    }

}
