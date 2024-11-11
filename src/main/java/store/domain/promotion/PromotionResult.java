package store.domain.promotion;

import store.domain.order.OrderSheet;
import store.domain.common.OrderStatus;
import store.domain.product.Product;

import static store.domain.common.OrderStatus.*;

public class PromotionResult {

    private final OrderStatus status;
    private final BenefitResult benefitResult;
    private final int generalQuantity;

    private PromotionResult(OrderStatus status, BenefitResult benefitResult, int generalQuantity) {
        this.status = status;
        this.benefitResult = benefitResult;
        this.generalQuantity = generalQuantity;
    }

    public static PromotionResult withoutPromotion(int generalQuantity) {
        return new PromotionResult(DONE, new BenefitResult(0, 0), generalQuantity);
    }

    public static PromotionResult morePromotion(BenefitResult benefitResult, int generalQuantity) {
        return new PromotionResult(ADD, benefitResult, generalQuantity);
    }

    public static PromotionResult withPromotion(BenefitResult benefitResult, int generalQuantity) {
        if (generalQuantity > 0) {
            return new PromotionResult(REMOVE, benefitResult, generalQuantity);
        }
        return new PromotionResult(DONE, benefitResult, generalQuantity);
    }

    public boolean canAddMore() {
        return status == ADD;
    }

    public boolean shouldCheckRemove() {
        return status == REMOVE;
    }

    public OrderSheet getOrderSheet(Product product) {
        return new OrderSheet(product, benefitResult.getApplyQuantity(),
                benefitResult.getFreeQuantity(), generalQuantity);
    }

    public OrderSheet getOrderSheetWithOneMore(Product product) {
        if (!canAddMore()) {
            throw new UnsupportedOperationException("증정 수량을 추가할 수 없습니다.");
        }
        return new OrderSheet(product, benefitResult.getApplyQuantity() + generalQuantity,
                benefitResult.getFreeQuantity() + 1, 0);
    }

    public OrderSheet getOrderSheetOnlyBenefit(Product product) {
        if (!shouldCheckRemove()) {
            throw new UnsupportedOperationException("일반 결제 수량을 제외할 수 없습니다.");
        }
        return new OrderSheet(product, benefitResult.getApplyQuantity(),
                benefitResult.getFreeQuantity(), 0);
    }

    public int getGeneralQuantity() {
        return generalQuantity;
    }

}
