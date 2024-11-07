package store.domain.promotion;

import java.time.LocalDate;

public class Period {

    private final LocalDate start;
    private final LocalDate end;

    private Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public static Period between(LocalDate start, LocalDate end) {
        return new Period(start, end);
    }

}
