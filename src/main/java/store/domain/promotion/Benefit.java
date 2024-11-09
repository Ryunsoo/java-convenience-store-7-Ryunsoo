package store.domain.promotion;

public class Benefit {

    private static final int MIN_APPLY_QUANTITY = 1;
    private static final int GET_FREE_QUANTITY = 1;

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

    public BenefitResult compare(int quantity) {
        int benefitUnitQuantity = applyQuantity + GET_FREE_QUANTITY;
        int applyCount = Math.divideExact(quantity, benefitUnitQuantity);
        int remain = quantity - (applyCount * benefitUnitQuantity);
        return new BenefitResult(applyCount * applyQuantity, applyCount * GET_FREE_QUANTITY, remain);
    }

    public boolean isApplyQuantity(int quantity) {
        return quantity == applyQuantity;
    }

}
