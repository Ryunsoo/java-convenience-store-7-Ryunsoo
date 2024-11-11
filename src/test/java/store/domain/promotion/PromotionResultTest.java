package store.domain.promotion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.common.Name;
import store.domain.order.OrderSheet;
import store.domain.product.Price;
import store.domain.product.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.domain.common.OrderStatus.DONE;

class PromotionResultTest {

    private Product defaultProduct;

    @BeforeEach
    void setUp() {
        this.defaultProduct = new Product(new Name("상품"), Price.valueOf(1000));
    }

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

    @DisplayName("프로모션 결과를 통해 주문서를 생성, 반환한다.")
    @Test
    void returnOrderSheet() {
        BenefitResult benefitResult = new BenefitResult(4, 2);
        int generalQuantity = 1;

        PromotionResult promotionResult = PromotionResult.withPromotion(benefitResult, generalQuantity);

        OrderSheet orderSheet = promotionResult.getOrderSheet(defaultProduct);
        assertThat(orderSheet.getProduct()).isEqualTo(defaultProduct);
        assertThat(orderSheet).extracting("benefitQuantity").isEqualTo(4);
        assertThat(orderSheet).extracting("freeQuantity").isEqualTo(2);
        assertThat(orderSheet).extracting("generalQuantity").isEqualTo(1);
    }

    @DisplayName("증정 수량을 추가하여 주문서를 생성, 반환한다.")
    @Test
    void returnOrderSheetWithOneMore() {
        BenefitResult benefitResult = new BenefitResult(4, 2);
        int generalQuantity = 2;

        PromotionResult promotionResult = PromotionResult.morePromotion(benefitResult, generalQuantity);

        OrderSheet orderSheet = promotionResult.getOrderSheetWithOneMore(defaultProduct);
        assertThat(orderSheet.getProduct()).isEqualTo(defaultProduct);
        assertThat(orderSheet).extracting("benefitQuantity").isEqualTo(6);
        assertThat(orderSheet).extracting("freeQuantity").isEqualTo(3);
        assertThat(orderSheet).extracting("generalQuantity").isEqualTo(0);
    }

    @DisplayName("증정 수량을 추가 시 상태가 ADD가 아니면 예외를 던진다.")
    @Test
    void CannotReturnOrderSheetWithOneMoreIfStatusNotADD() {
        BenefitResult benefitResult = new BenefitResult(4, 2);
        int generalQuantity = 2;

        PromotionResult promotionResult = PromotionResult.withPromotion(benefitResult, generalQuantity);

        assertThatThrownBy(() -> promotionResult.getOrderSheetWithOneMore(defaultProduct))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("증정 수량을 추가할 수 없습니다.");
    }

    @DisplayName("일반 결제 수량은 제외하여 주문서를 생성, 반환한다.")
    @Test
    void returnOrderSheetWithoutGeneralQuantity() {
        BenefitResult benefitResult = new BenefitResult(4, 2);
        int generalQuantity = 2;

        PromotionResult promotionResult = PromotionResult.withPromotion(benefitResult, generalQuantity);

        OrderSheet orderSheet = promotionResult.getOrderSheetOnlyBenefit(defaultProduct);
        assertThat(orderSheet.getProduct()).isEqualTo(defaultProduct);
        assertThat(orderSheet).extracting("benefitQuantity").isEqualTo(4);
        assertThat(orderSheet).extracting("freeQuantity").isEqualTo(2);
        assertThat(orderSheet).extracting("generalQuantity").isEqualTo(0);
    }

}
