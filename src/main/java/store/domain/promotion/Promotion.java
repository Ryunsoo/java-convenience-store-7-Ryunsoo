package store.domain.promotion;

import store.domain.common.Name;

import java.time.LocalDateTime;

public class Promotion {

    private final Name name;
    private final Benefit benefit;
    private final Period period;

    public Promotion(Name name, Benefit benefit, Period period) {
        validate(name, benefit, period);
        this.name = name;
        this.benefit = benefit;
        this.period = period;
    }

    private void validate(Name name, Benefit benefit, Period period) {
        validate(name);
        validate(benefit);
        validate(period);
    }

    private void validate(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("프로모션 생성에 실패했습니다.");
        }
    }

    public boolean is(Name name) {
        return this.name.equals(name);
    }

    public boolean onGoing(LocalDateTime dateTime) {
        return period.within(dateTime);
    }

    public PromotionResult checkDiscount(int quantity) {
        BenefitResult benefitResult = benefit.compare(quantity);
        int unapplyQuantity = benefitResult.getUnapplyQuantity();

        if (benefit.isApplyQuantity(unapplyQuantity)) {
            return PromotionResult.canAddMore(benefitResult);
        }

        return PromotionResult.complete(benefitResult);
    }

}
