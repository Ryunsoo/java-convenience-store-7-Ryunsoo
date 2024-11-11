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

    @DisplayName("혜택 정보를 통해 혜택 적용 수량과 무료 증정 수량을 반환한다.")
    @Test
    void returnBenefitResultAfterCompareWithQuantity() {
        Benefit benefit = new Benefit(2);
        int quantity = 7;

        BenefitResult expected = new BenefitResult(4, 2);

        BenefitResult result = benefit.compare(quantity);
        assertThat(result.getApplyQuantity()).isEqualTo(expected.getApplyQuantity());
        assertThat(result.getFreeQuantity()).isEqualTo(expected.getFreeQuantity());
    }

}
