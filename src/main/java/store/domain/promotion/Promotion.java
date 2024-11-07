package store.domain.promotion;

import store.domain.common.Name;

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

}
