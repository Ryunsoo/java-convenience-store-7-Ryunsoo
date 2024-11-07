package store.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @DisplayName("문자열로 이름을 생성할 수 있다.")
    @Test
    void createNameWithString() {
        Name name = new Name("상품명");

        assertThat(name).extracting("name").isEqualTo("상품명");
    }

    @DisplayName("이름이 null 또는 빈문자열이면 예외를 던진다.")
    @ParameterizedTest
    @NullAndEmptySource
    void nameCannotHasNullOrEmptyString(String string) {
        assertThatThrownBy(() -> new Name(string))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름이 올바르지 않습니다.");
    }

    @DisplayName("값이 같으면 두 이름은 같고, 값이 다르면 다른 이름이다.")
    @ParameterizedTest
    @CsvSource(value = {"상품, 상품, true", "상품1, 상품2, false"}, delimiter = ',')
    void sameOrDifferentByName(String name1, String name2, boolean expected) {
        Name firstName = new Name(name1);
        Name secondName = new Name(name2);

        assertThat(firstName.equals(secondName)).isEqualTo(expected);
    }

}
