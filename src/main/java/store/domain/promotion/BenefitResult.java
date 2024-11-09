package store.domain.promotion;

public class BenefitResult {

    private int applyQuantity;
    private int freeQuantity;
    private int unapplyQuantity;

    public BenefitResult(int applyQuantity, int freeQuantity, int unapplyQuantity) {
        this.applyQuantity = applyQuantity;
        this.freeQuantity = freeQuantity;
        this.unapplyQuantity = unapplyQuantity;
    }

    public int getUnapplyQuantity() {
        return this.unapplyQuantity;
    }

    public int getApplyQuantity() {
        return applyQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }
}
