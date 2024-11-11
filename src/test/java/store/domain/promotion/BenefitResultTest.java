package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BenefitResultTest {

    @DisplayName("적용 수량과 증정 수량을 가진 혜택 적용 결과를 생성할 수 있다.")
    @Test
    void createBenefitResult() {
        int applyQuantity = 0;
        int freeQuantity = 0;

        BenefitResult benefitResult = new BenefitResult(applyQuantity, freeQuantity);

        assertThat(benefitResult.getApplyQuantity()).isEqualTo(applyQuantity);
        assertThat(benefitResult.getFreeQuantity()).isEqualTo(freeQuantity);
    }

    @DisplayName("적용 수량 또는 증정 수량이 0보다 작으면 예외를 던진다.")
    @ParameterizedTest
    @CsvSource(value = {"-1, 0", "0, -1", "-1, -1"}, delimiter = ',')
    void anyResultQuantityCannotBeBelowZero(int applyQuantity, int freeQuantity) {
        assertThatThrownBy(() -> new BenefitResult(applyQuantity, freeQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("혜택 적용 결과 수량이 유효하지 않습니다.");
    }

}
