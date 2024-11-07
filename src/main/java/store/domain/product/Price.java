package store.domain.product;

import java.math.BigDecimal;

public class Price {

    private final BigDecimal price;

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price valueOf(long value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        return new Price(decimal);
    }

}
