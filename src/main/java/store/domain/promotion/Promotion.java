package store.domain.promotion;

import store.domain.common.Name;

public class Promotion {

    private final Name name;
    private final Benefit benefit;
    private final Period period;

    public Promotion(Name name, Benefit benefit, Period period) {
        this.name = name;
        this.benefit = benefit;
        this.period = period;
    }

}
