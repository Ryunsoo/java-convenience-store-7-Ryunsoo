package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductNameTest {

    @DisplayName("문자열로 상품명을 생성할 수 있다.")
    @Test
    void createProductNameWithString() {
        ProductName name = new ProductName("상품명");

        assertThat(name).extracting("name").isEqualTo("상품명");
    }

    @DisplayName("상품명이 null 또는 빈문자열이면 예외를 던진다.")
    @ParameterizedTest
    @NullAndEmptySource
    void productNameCannotHasNullOrEmptyString(String string) {
        assertThatThrownBy(() -> new ProductName(string))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명이 올바르지 않습니다.");
    }

    @DisplayName("이름이 같으면 두 상품명은 같고, 이름이 다르면 다른 상품명이다.")
    @ParameterizedTest
    @CsvSource(value = {"상품, 상품, true", "상품1, 상품2, false"}, delimiter = ',')
    void sameOrDifferentByName(String name1, String name2, boolean expected) {
        ProductName firstName = new ProductName(name1);
        ProductName secondName = new ProductName(name2);

        assertThat(firstName.equals(secondName)).isEqualTo(expected);
    }

}
