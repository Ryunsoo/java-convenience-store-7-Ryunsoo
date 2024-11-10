package store.view.dto;

import store.domain.common.Name;

public class PurchaseInfo {

    private final String productName;
    private final int quantity;

    public PurchaseInfo(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public Name getProductName() {
        return new Name(productName);
    }

    public int getQuantity() {
        return quantity;
    }

}
