package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

}
