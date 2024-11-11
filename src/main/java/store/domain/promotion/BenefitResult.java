package store.domain.promotion;

public class BenefitResult {

    private int applyQuantity;
    private int freeQuantity;

    public BenefitResult(int applyQuantity, int freeQuantity) {
        validate(applyQuantity, freeQuantity);
        this.applyQuantity = applyQuantity;
        this.freeQuantity = freeQuantity;
    }

    private void validate(int applyQuantity, int freeQuantity) {
        if (applyQuantity < 0 || freeQuantity < 0) {
            throw new IllegalArgumentException("혜택 적용 결과 수량이 유효하지 않습니다.");
        }
    }

    public int getQuantity() {
        return this.applyQuantity + this.freeQuantity;
    }

    public int getApplyQuantity() {
        return applyQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

}
