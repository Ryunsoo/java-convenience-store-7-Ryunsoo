package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BenefitTest {

    @DisplayName("혜택은 혜택 적용 구매 수량을 갖는다.")
    @Test
    void createBenefitWithApplyQuantity() {
        Benefit benefit = new Benefit(2);

        assertThat(benefit).extracting("applyQuantity").isEqualTo(2);
    }

}
