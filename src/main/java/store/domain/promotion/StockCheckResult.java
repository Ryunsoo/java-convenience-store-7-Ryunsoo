package store.domain.promotion;

import store.domain.order.OrderSheet;
import store.domain.common.OrderStatus;
import store.domain.product.Product;

import static store.domain.common.OrderStatus.*;

public class StockCheckResult {

    private final OrderStatus status;
    private final BenefitResult benefitResult;
    private final int generalQuantity;

    private StockCheckResult(OrderStatus status, BenefitResult benefitResult, int generalQuantity) {
        this.status = status;
        this.benefitResult = benefitResult;
        this.generalQuantity = generalQuantity;
    }

    public static StockCheckResult withoutPromotion(int quantity) {
        return new StockCheckResult(DONE, new BenefitResult(0, 0), quantity);
    }

    public static StockCheckResult morePromotion(BenefitResult benefitResult, int generalQuantity) {
        return new StockCheckResult(ADD, benefitResult, generalQuantity);
    }

    public static StockCheckResult withPromotion(BenefitResult benefitResult, int generalQuantity) {
        if (generalQuantity > 0) {
            return new StockCheckResult(REMOVE, benefitResult, generalQuantity);
        }
        return new StockCheckResult(DONE, benefitResult, generalQuantity);
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
        return new OrderSheet(product, benefitResult.getApplyQuantity() + generalQuantity,
                benefitResult.getFreeQuantity() + 1, 0);
    }

    public OrderSheet getOrderSheetOnlyBenefit(Product product) {
        return new OrderSheet(product, benefitResult.getApplyQuantity(),
                benefitResult.getFreeQuantity(), 0);
    }

    public int getGeneralQuantity() {
        return generalQuantity;
    }

}
