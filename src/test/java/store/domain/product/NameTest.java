package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NameTest {

    @DisplayName("문자열로 이름을 생성할 수 있다.")
    @Test
    void createNameWithString() {
        String string = "상품명";

        Name name = new Name(string);

        assertThat(name).extracting("name").isEqualTo(string);
    }

}
