package store.domain.membership;

import store.domain.product.Price;

import java.math.BigDecimal;

public class Membership {

    private final double percentage;
    private Price oneDayLimit;

    public Membership(int percentage, long oneDayLimit) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("멤버십 할인율이 유효하지 않습니다.");
        }
        this.percentage = ((double) percentage) / 100.0;
        this.oneDayLimit = Price.valueOf(oneDayLimit);
    }

    public Price calculateDiscount(Price amount) {
        Price thirtyPercent = calculatePercentage(amount);
        if (thirtyPercent.compareTo(oneDayLimit) > 0) {
            Price discount = oneDayLimit.getPrice();
            deductOneDayLimit(discount);
            return discount;
        }
        deductOneDayLimit(thirtyPercent);
        return thirtyPercent;
    }

    private Price calculatePercentage(Price amount) {
        return amount.multiply(BigDecimal.valueOf(percentage));
    }

    private void deductOneDayLimit(Price amount) {
        oneDayLimit = oneDayLimit.subtract(amount);
    }

}
