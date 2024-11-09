package store.domain;

import store.domain.common.Name;
import store.domain.product.Price;
import store.domain.product.Product;

public class PurchaseResult {

    private final Product product;
    private final int applyQuantity;
    private final int freeQuantity;
    private final int unapplyQuantity;

    public PurchaseResult(Product product, int applyQuantity, int freeQuantity, int unapplyQuantity) {
        this.product = product;
        this.applyQuantity = applyQuantity;
        this.freeQuantity = freeQuantity;
        this.unapplyQuantity = unapplyQuantity;
    }

    public static PurchaseResult onlyBasic(Product product, int quantity) {
        return new PurchaseResult(product, 0, 0, quantity);
    }

    public static PurchaseResult cancel(Product product) {
        return new PurchaseResult(product, 0, 0, 0);
    }

    public Price nonPromotionPrice() {
        return product.calculatePrice(unapplyQuantity);
    }

    public Name productName() {
        return product.name();
    }

    public int totalQuantity() {
        return applyQuantity + freeQuantity + unapplyQuantity;
    }

    public Price totalAmount() {
        int totalQuantity = totalQuantity();
        return product.calculatePrice(totalQuantity);
    }

    public boolean appliedPromotion() {
        return applyQuantity > 0 && freeQuantity > 0;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public Price promotionPrice() {
        return product.calculatePrice(freeQuantity);
    }

}
