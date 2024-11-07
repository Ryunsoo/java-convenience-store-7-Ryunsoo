package store.domain.product;

import store.domain.common.Name;

import java.util.Objects;

public class Product {

    private final Name name;
    private final Price price;

    public Product(Name name, Price price) {
        validate(name);
        validate(price);
        this.name = name;
        this.price = price;
    }

    private void validate(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("상품 생성에 실패했습니다.");
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
