package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                .containsExactly(start, end);
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

    @DisplayName("기간에 속하는 날짜 시간이면 true, 속하지 않으면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2024-11-01, true", "2024-12-01, false"})
    void compareDateTimeWithPeriod(String dateTimeStr, boolean expected) {
        Period period = Period.between(LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 11, 30));

        LocalDateTime dateTime = LocalDate.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE)
                        .atStartOfDay();

        assertThat(period.within(dateTime)).isEqualTo(expected);
    }

}
