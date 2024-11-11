package store.domain.promotion;

import store.domain.common.Name;
import store.exception.StoreSetUpFailException;

import java.util.List;

public class Promotions {

    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Promotion pick(String name) {
        Name promotionName = new Name(name);

        return promotions.stream()
                .filter(promotion -> promotion.is(promotionName))
                .findAny()
                .orElseThrow(() -> new StoreSetUpFailException("존재하지 않는 프로모션입니다."));
    }

}
