package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static store.domain.common.OrderStatus.DONE;

class PromotionResultTest {

    @DisplayName("프로모션이 미적용 시 상태는 DONE, 혜택 수량은 모두 0이다.")
    @Test
    void createNonPromotionResult() {
        int generalQuantity = 5;

        PromotionResult promotionResult = PromotionResult.withoutPromotion(generalQuantity);

        assertThat(promotionResult).extracting("status").isEqualTo(DONE);
        assertThat(promotionResult).extracting("benefitResult")
                .extracting("applyQuantity").isEqualTo(0);
        assertThat(promotionResult).extracting("benefitResult")
                .extracting("freeQuantity").isEqualTo(0);
        assertThat(promotionResult.getGeneralQuantity()).isEqualTo(generalQuantity);
    }

    @DisplayName("추가 증정이 가능한 프로모션 결과 생성 시 상태는 ADD 이다.")
    @Test
    void createAddMorePromotionResult() {
        BenefitResult benefitResult = new BenefitResult(4, 2);
        int generalQuantity = 2;

        PromotionResult promotionResult = PromotionResult.morePromotion(benefitResult, generalQuantity);

        assertThat(promotionResult.canAddMore()).isTrue();
        assertThat(promotionResult.getGeneralQuantity()).isEqualTo(generalQuantity);
    }

    @DisplayName("프로모션 적용 결과 생성 시 일반결제 수량이 있으면 REMOVE 상태이다.")
    @Test
    void createRemoveCheckPromotionResult() {
        BenefitResult benefitResult = new BenefitResult(4, 2);
        int generalQuantity = 2;

        PromotionResult promotionResult = PromotionResult.withPromotion(benefitResult, generalQuantity);

        assertThat(promotionResult.shouldCheckRemove()).isTrue();
        assertThat(promotionResult.getGeneralQuantity()).isEqualTo(generalQuantity);
    }

    @DisplayName("프로모션 적용 결과 생성 시 일반결제 수량이 없으면 DONE 상태이다.")
    @Test
    void createCompletedPromotionResult() {
        BenefitResult benefitResult = new BenefitResult(4, 2);
        int generalQuantity = 0;

        PromotionResult promotionResult = PromotionResult.withPromotion(benefitResult, generalQuantity);

        assertThat(promotionResult).extracting("status").isEqualTo(DONE);
        assertThat(promotionResult.getGeneralQuantity()).isEqualTo(generalQuantity);
    }

}
