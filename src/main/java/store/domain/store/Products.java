package store.domain.store;

import store.domain.common.Name;
import store.domain.product.Product;
import store.domain.stock.ProductStocks;
import store.view.dto.StockView;

import java.util.List;
import java.util.Map;

public class Products {

    private final Map<Product, ProductStocks> productStocks;

    public Products(Map<Product, ProductStocks> productStocks) {
        this.productStocks = productStocks;
    }

    public Product select(Name productName, int quantity) {
        Product product = find(productName);
        if (!hasStocks(product, quantity)) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다.");
        }
        return product;
    }

    private Product find(Name productName) {
        return productStocks.keySet().stream()
                .filter(product -> product.is(productName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public boolean hasStocks(Product product, int quantity) {
        ProductStocks stocks = productStocks.get(product);
        return stocks.hasEnough(quantity);
    }

    public ProductStocks getStocks(Product product) {
        return productStocks.get(product);
    }

    public List<StockView> getStockViews() {
        return productStocks.entrySet().stream()
                .map((entry) -> entry.getValue().getStockViews(entry.getKey()))
                .flatMap(List::stream)
                .toList();
    }

}
