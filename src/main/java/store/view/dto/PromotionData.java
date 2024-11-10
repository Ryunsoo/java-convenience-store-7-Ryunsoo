package store.view.dto;

import store.domain.common.Name;
import store.domain.promotion.Benefit;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;

import java.time.LocalDate;

public class PromotionData {

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public PromotionData(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Promotion toPromotion() {
        Name name = new Name(this.name);
        Benefit benefit = new Benefit(this.buy);
        Period period = Period.between(this.startDate, this.endDate);

        return new Promotion(name, benefit, period);
    }

}
