package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import store.domain.common.Name;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PromotionTest {

    private static final Name DEFAULT_NAME = new Name("프로모션");
    private static final Benefit DEFAULT_BENEFIT = new Benefit(2);
    private static final Period DEFAULT_PERIOD = Period.between(LocalDate.now(), LocalDate.now().plusDays(10));

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

    @DisplayName("해당 이름의 프로모션이면 true, 아니면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"프로모션, true", "행사, false"})
    void comparePromotionName(String name, boolean expected) {
        Name promotionName = new Name("프로모션");
        Promotion promotion = createPromotion(promotionName);

        assertThat(promotion.is(new Name(name))).isEqualTo(expected);
    }

    @DisplayName("프로모션이 적용되는 날짜/시간이면 true, 아니면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2024-11-01, true", "2024-12-01, false"})
    void compareDateTimeWithPeriod(String dateTimeStr, boolean expected) {
        Period period = Period.between(LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 11, 30));
        Promotion promotion = createPromotion(period);

        LocalDateTime dateTime = LocalDate.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE)
                .atStartOfDay();

        assertThat(promotion.onGoing(dateTime)).isEqualTo(expected);
    }

    @DisplayName("혜택과 수량을 통해 적용 수량과 무료 증정 수량을 반환한다.")
    @Test
    void returnBenefitResultByBenefit() {
        Benefit benefit = new Benefit(2);
        Promotion promotion = createPromotion(benefit);
        int quantity = 7;

        BenefitResult expected = new BenefitResult(4, 2);

        BenefitResult result = promotion.getBenefitResult(quantity);
        assertThat(result.getApplyQuantity()).isEqualTo(expected.getApplyQuantity());
        assertThat(result.getFreeQuantity()).isEqualTo(expected.getFreeQuantity());
    }

    private Promotion createPromotion(Name name) {
        return new Promotion(name, DEFAULT_BENEFIT, DEFAULT_PERIOD);
    }

    private Promotion createPromotion(Period period) {
        return new Promotion(DEFAULT_NAME, DEFAULT_BENEFIT, period);
    }

    private Promotion createPromotion(Benefit benefit) {
        return new Promotion(DEFAULT_NAME, benefit, DEFAULT_PERIOD);
    }

}
