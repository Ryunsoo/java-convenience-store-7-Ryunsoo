package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @DisplayName("문자열로 이름을 생성할 수 있다.")
    @Test
    void createNameWithString() {
        String string = "상품명";

        Name name = new Name(string);

        assertThat(name).extracting("name").isEqualTo(string);
    }

    @DisplayName("이름 생성 시 null 또는 빈문자열은 예외를 던진다.")
    @ParameterizedTest
    @NullAndEmptySource
    void nameCannotHasNullOrEmptyString(String string) {
        assertThatThrownBy(() -> new Name(string))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명이 올바르지 않습니다.");
    }

}
