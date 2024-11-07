package store.domain.promotion;

public class Benefit {

    private static final int MIN_APPLY_QUANTITY = 1;

    private final int applyQuantity;

    public Benefit(int applyQuantity) {
        validate(applyQuantity);
        this.applyQuantity = applyQuantity;
    }

    private void validate(int applyQuantity) {
        if (applyQuantity < MIN_APPLY_QUANTITY) {
            throw new IllegalArgumentException("혜택 적용 수량이 올바르지 않습니다.");
        }
    }

}
