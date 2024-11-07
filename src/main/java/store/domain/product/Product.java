package store.domain.product;

public class Product {

    private final ProductName name;
    private final Price price;

    public Product(ProductName name, Price price) {
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

}
