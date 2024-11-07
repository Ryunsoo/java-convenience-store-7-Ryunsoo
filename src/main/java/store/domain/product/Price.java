package store.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(price);
    }
}
