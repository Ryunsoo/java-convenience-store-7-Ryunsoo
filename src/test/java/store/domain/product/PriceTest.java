package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("값을 갖는 가격을 생성할 수 있다.")
    @Test
    void createPriceWithValue() {
        Price price = Price.valueOf(1000);

        BigDecimal expected = BigDecimal.valueOf(1000);
        assertThat(price).extracting("price").isEqualTo(expected);
    }

    @DisplayName("가격이 0 미만일 경우 예외를 던진다.")
    @Test
    void priceCannotHasValueBelowZero() {
        assertThatThrownBy(() -> Price.valueOf(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 올바르지 않습니다.");
    }

}
