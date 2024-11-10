package store.domain.order;

import store.domain.common.Name;
import store.domain.product.Price;
import store.domain.product.Product;

public class OrderSheet {

    private final Product product;
    private final int benefitQuantity;
    private final int freeQuantity;
    private final int generalQuantity;

    public OrderSheet(Product product, int benefitQuantity, int freeQuantity, int generalQuantity) {
        this.product = product;
        this.benefitQuantity = benefitQuantity;
        this.freeQuantity = freeQuantity;
        this.generalQuantity = generalQuantity;
    }

    public Price generalPrice() {
        return product.calculatePrice(generalQuantity);
    }

    public Price promotionDiscountPrice() {
        return product.calculatePrice(freeQuantity);
    }

    public boolean appliedPromotion() {
        return benefitQuantity > 0 && freeQuantity > 0;
    }

    public Price totalAmount() {
        int totalQuantity = totalQuantity();
        return product.calculatePrice(totalQuantity);
    }

    public int totalQuantity() {
        return benefitQuantity + freeQuantity + generalQuantity;
    }

    public Name productName() {
        return product.name();
    }

    public Product getProduct() {
        return product;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

}
