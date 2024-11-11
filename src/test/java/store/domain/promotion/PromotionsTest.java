package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.common.Name;
import store.exception.StoreSetUpFailException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PromotionsTest {

    @DisplayName("프로모션 목록에 존재하는 프로모션 이름이면 프로모션을 반환한다.")
    @Test
    void successToPickPromotionByName() {
        Promotions promotions = new Promotions(createPromotionsWithNames("프로모션1", "프로모션2"));

        Promotion picked = promotions.pick("프로모션1");

        assertThat(picked.is(new Name("프로모션1"))).isTrue();
    }

    @DisplayName("프로모션 목록에 존재하지 않는 프로모션 이름이면 예외를 던진다.")
    @Test
    void failToPickPromotionByName() {
        Promotions promotions = new Promotions(createPromotionsWithNames("프로모션1", "프로모션2"));

        assertThatThrownBy(() -> promotions.pick("혜택"))
                .isInstanceOf(StoreSetUpFailException.class)
                .hasMessage("존재하지 않는 프로모션입니다.");
    }

    private List<Promotion> createPromotionsWithNames(String...names) {
        return Arrays.stream(names)
                .map((name) -> createPromotionWithName(new Name(name)))
                .toList();
    }

    private Promotion createPromotionWithName(Name name) {
        Period period = Period.between(LocalDate.now(), LocalDate.now().plusDays(5));
        return new Promotion(name, new Benefit(2), period);
    }

}
