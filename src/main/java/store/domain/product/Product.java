package store.domain.product;

public class Product {

    private final ProductName name;
    private final Price price;

    public Product(ProductName name, Price price) {
        this.name = name;
        this.price = price;
    }

}
