package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BenefitTest {

    @DisplayName("혜택은 혜택 적용 구매 수량을 갖는다.")
    @Test
    void createBenefitWithApplyQuantity() {
        Benefit benefit = new Benefit(2);

        assertThat(benefit).extracting("applyQuantity").isEqualTo(2);
    }

    @DisplayName("혜택 적용 구매 수량이 1 미만이면 예외를 던진다.")
    @Test
    void BenefitCannotHaveApplyQuantityBelowOne() {
        assertThatThrownBy(() -> new Benefit(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("혜택 적용 수량이 올바르지 않습니다.");
    }

}
