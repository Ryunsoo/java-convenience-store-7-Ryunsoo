package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductNameTest {

    @DisplayName("문자열로 상품명을 생성할 수 있다.")
    @Test
    void createProductNameWithString() {
        String string = "상품명";

        ProductName name = new ProductName(string);

        assertThat(name).extracting("name").isEqualTo(string);
    }

    @DisplayName("상품명이 null 또는 빈문자열이면 예외를 던진다.")
    @ParameterizedTest
    @NullAndEmptySource
    void productNameCannotHasNullOrEmptyString(String string) {
        assertThatThrownBy(() -> new ProductName(string))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명이 올바르지 않습니다.");
    }

}
