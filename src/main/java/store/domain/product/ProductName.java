package store.domain.product;

public class ProductName {

    private final String name;

    public ProductName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명이 올바르지 않습니다.");
        }
    }

}
