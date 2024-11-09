package store.domain;

import store.domain.product.Price;

import java.util.List;

public class PurchaseResults {

    private final List<PurchaseResult> purchaseResults;

    public PurchaseResults(List<PurchaseResult> purchaseResults) {
        this.purchaseResults = purchaseResults;
    }

    public Price nonPromotionAmount() {
        return purchaseResults.stream()
                .map(PurchaseResult::nonPromotionPrice)
                .reduce(Price::add)
                .orElse(Price.valueOf(0));
    }

    public List<PurchaseResult> getPurchaseResults() {
        return purchaseResults;
    }

    public List<PurchaseResult> onlyAppliedPromotion() {
        return purchaseResults.stream()
                .filter(PurchaseResult::appliedPromotion)
                .toList();
    }

    public int totalQuantity() {
        return purchaseResults.stream()
                .map(PurchaseResult::totalQuantity)
                .reduce(0, Integer::sum);
    }

    public Price totalAmount() {
        return purchaseResults.stream()
                .map(PurchaseResult::totalAmount)
                .reduce(Price::add)
                .orElse(Price.valueOf(0));
    }

    public Price promotionAmount() {
        return purchaseResults.stream()
                .map(PurchaseResult::promotionPrice)
                .reduce(Price::add)
                .orElse(Price.valueOf(0));
    }
}
