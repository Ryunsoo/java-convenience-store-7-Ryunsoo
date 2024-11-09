package store.domain.promotion;

public class PromotionResult {

    private final String status;
    private final BenefitResult benefitResult;

    private PromotionResult(String status, BenefitResult benefitResult) {
        this.status = status;
        this.benefitResult = benefitResult;
    }

    public static PromotionResult complete(BenefitResult benefitResult) {
        return new PromotionResult("적용 완료", benefitResult);
    }

    public static PromotionResult canAddMore(BenefitResult benefitResult) {
        return new PromotionResult("추가 증정", benefitResult);
    }

    public boolean canGetFree() {
        return this.status.equals("추가 증정");
    }

    public BenefitResult getBenefitResult() {
        return benefitResult;
    }
}
