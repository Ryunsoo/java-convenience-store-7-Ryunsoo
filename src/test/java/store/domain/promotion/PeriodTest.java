package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.domain.common.Name;
import store.domain.product.Price;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PeriodTest {

    @DisplayName("기간은 시작 날짜와 종료 날짜를 갖는다.")
    @Test
    void createPeriodWithStartDateAndEndDate() {
        LocalDate start = LocalDate.of(2024, 11, 1);
        LocalDate end = LocalDate.of(2024, 11, 30);

        Period period = Period.between(start, end);

        assertThat(period).extracting("start", "end")
                .isEqualTo(List.of(start, end));
    }

    @DisplayName("종료 날짜가 시작 날짜보다 이전이면 예외를 던진다.")
    @Test
    void endDateCannotBeEarlierThanStartDate() {
        LocalDate start = LocalDate.of(2024, 11, 2);
        LocalDate end = LocalDate.of(2024, 11, 1);

        assertThatThrownBy(() -> Period.between(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기간 날짜 범위가 올바르지 않습니다.");
    }

    @DisplayName("시작 날짜 또는 종료 날짜가 null 이면 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("provideInvalidStartOrEnd")
    void periodShouldHaveStartDateAndEndDate(LocalDate start, LocalDate end) {
        assertThatThrownBy(() -> Period.between(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기간 날짜 정보가 올바르지 않습니다.");
    }

    private static Stream<Arguments> provideInvalidStartOrEnd() {
        return Stream.of(
                Arguments.of(null, LocalDate.of(2024, 11, 30)),
                Arguments.of(LocalDate.of(2024, 11, 1), null),
                Arguments.of(null, null)
        );
    }

}
