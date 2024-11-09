package store.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public class Price implements Comparable<Price> {

    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###");
    private static final long MIN_VALUE = 0;

    private final BigDecimal price;

    public Price(BigDecimal price) {
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

    public Price multiple(int number) {
        return multiply(BigDecimal.valueOf(number));
    }

    public Price multiply(BigDecimal decimal) {
        BigDecimal multiply = price.multiply(decimal);
        BigDecimal rounded = multiply.setScale(0, RoundingMode.HALF_UP);
        return new Price(rounded);
    }

    public Price add(Price price) {
        BigDecimal added = this.price.add(price.price);
        return new Price(added);
    }

    public Price subtract(Price amount) {
        BigDecimal subtract = this.price.subtract(amount.price);
        return new Price(subtract);
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

    @Override
    public int compareTo(Price o) {
        return this.price.compareTo(o.price);
    }

    @Override
    public String toString() {
        return FORMATTER.format(price);
    }
}
