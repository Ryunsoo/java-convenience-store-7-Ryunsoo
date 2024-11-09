package store.domain.stock;

import java.util.Objects;

public class Stock {

    private static final int MIN_QUANTITY = 0;

    private int quantity;

    public Stock(int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
        }
    }

    public int available(int quantity) {
        return Math.min(this.quantity, quantity);
    }

    public void deduct(int quantity) {
        this.quantity -= quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Stock stock = (Stock) object;
        return quantity == stock.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(quantity);
    }

}
