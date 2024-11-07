package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @DisplayName("값이 값으면 같은 가격, 다르면 다른 가격이다.")
    @ParameterizedTest
    @CsvSource(value = {"1000, 1000, true", "1000, 100, false"})
    void sameOrDifferentByValue(long value1, long value2, boolean expected) {
        Price price1 = Price.valueOf(value1);
        Price price2 = Price.valueOf(value2);

        assertThat(price1.equals(price2)).isEqualTo(expected);
    }

}
