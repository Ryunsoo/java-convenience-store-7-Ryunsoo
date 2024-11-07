package store.domain.product;

import java.math.BigDecimal;

public class Price {

    private static final long MIN_VALUE = 0;

    private final BigDecimal price;

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price valueOf(long value) {
        validate(value);
        BigDecimal decimal = BigDecimal.valueOf(value);
        return new Price(decimal);
    }

    private static void validate(long value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("가격이 올바르지 않습니다.");
        }
    }

}
