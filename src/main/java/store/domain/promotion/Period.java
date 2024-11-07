package store.domain.promotion;

import java.time.LocalDate;

public class Period {

    private final LocalDate start;
    private final LocalDate end;

    private Period(LocalDate start, LocalDate end) {
        validate(start, end);
        this.start = start;
        this.end = end;
    }

    private void validate(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("기간 날짜 범위가 올바르지 않습니다.");
        }
    }

    public static Period between(LocalDate start, LocalDate end) {
        return new Period(start, end);
    }

}
