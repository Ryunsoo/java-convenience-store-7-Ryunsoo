package store.domain.store;

import store.domain.common.Name;
import store.domain.product.Product;
import store.domain.stock.ProductStocks;

import java.util.Map;

public class Products {

    private final Map<Product, ProductStocks> productStocks;

    public Products(Map<Product, ProductStocks> productStocks) {
        this.productStocks = productStocks;
    }

    public Product find(Name productName) {
        return productStocks.keySet().stream()
                .filter(product -> product.is(productName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public ProductStocks getStocks(Product product) {
        return productStocks.get(product);
    }

}
