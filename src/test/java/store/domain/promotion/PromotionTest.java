package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.common.Name;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {

    @DisplayName("프로모션은 이름, 혜택, 적용 기간을 갖는다.")
    @Test
    void createPromotionWithNameBenefitAndPeriod() {
        Name name = new Name("반짝할인");
        Benefit benefit = new Benefit(1);
        Period period = Period.between(LocalDate.now(), LocalDate.now().plusDays(10));

        Promotion promotion = new Promotion(name, benefit, period);

        assertThat(promotion).extracting("name", "benefit", "period")
                .containsExactly(name, benefit, period);
    }

}
