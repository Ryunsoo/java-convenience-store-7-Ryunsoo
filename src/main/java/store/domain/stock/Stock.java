package store.domain.stock;

public class Stock {

    private static final int MIN_QUANTITY = 0;

    private final int quantity;

    public Stock(int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
        }
    }

}
