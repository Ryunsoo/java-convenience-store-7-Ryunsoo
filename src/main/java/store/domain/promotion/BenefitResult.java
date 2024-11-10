package store.domain.promotion;

public class BenefitResult {

    private int applyQuantity;
    private int freeQuantity;

    public BenefitResult(int applyQuantity, int freeQuantity) {
        this.applyQuantity = applyQuantity;
        this.freeQuantity = freeQuantity;
    }

    public int getApplyQuantity() {
        return applyQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getQuantity() {
        return this.applyQuantity + this.freeQuantity;
    }

}
