package store.domain.promotion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.domain.common.Name;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("이름, 혜택 또는 적용 기간이 null 이면 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("provideInvalidElements")
    void promotionCannotHaveAnyNull(Name name, Benefit benefit, Period period) {
        assertThatThrownBy(() -> new Promotion(name, benefit, period))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로모션 생성에 실패했습니다.");
    }

    private static Stream<Arguments> provideInvalidElements() {
        return Stream.of(
                Arguments.of(null, new Benefit(1),
                        Period.between(LocalDate.now(), LocalDate.now().plusDays(10))),
                Arguments.of(new Name("반짝할인"), null,
                        Period.between(LocalDate.now(), LocalDate.now().plusDays(10))),
                Arguments.of(new Name("반짝할인"), new Benefit(1), null)
        );
    }

}
