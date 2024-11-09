package store.domain.promotion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Period {

    private final LocalDate start;
    private final LocalDate end;

    private Period(LocalDate start, LocalDate end) {
        validate(start, end);
        this.start = start;
        this.end = end;
    }

    private void validate(LocalDate start, LocalDate end) {
        validate(start);
        validate(end);

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("기간 날짜 범위가 올바르지 않습니다.");
        }
    }

    private void validate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("기간 날짜 정보가 올바르지 않습니다.");
        }
    }

    public static Period between(LocalDate start, LocalDate end) {
        return new Period(start, end);
    }

    public boolean within(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        return !date.isBefore(start) && !date.isAfter(end);
    }
}
