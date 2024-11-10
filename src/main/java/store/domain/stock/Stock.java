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

    public static Stock empty() {
        return new Stock(MIN_QUANTITY);
    }

    public int available(int quantity) {
        return Math.min(this.quantity, quantity);
    }

    public void deduct(int quantity) {
        if (quantity > this.quantity) {
            throw new RuntimeException("차감할 수 있는 재고 수량을 초과했습니다.");
        }
        this.quantity -= quantity;
    }

    public boolean hasMore(int quantity) {
        return this.quantity > quantity;
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
