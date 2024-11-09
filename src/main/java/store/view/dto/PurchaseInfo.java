package store.view.dto;

import store.domain.common.Name;
import store.domain.product.Product;

import java.util.Collection;

public class PurchaseInfo {

    private final String productName;
    private final int quantity;

    public PurchaseInfo(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public Product chooseProduct(Collection<Product> products) {
        Name name = new Name(this.productName);

        return products.stream()
                .filter(product -> product.is(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public int getQuantity() {
        return quantity;
    }
}
