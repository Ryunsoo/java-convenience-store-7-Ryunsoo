package store.view.dto;

import store.domain.common.Name;
import store.domain.product.Price;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.Promotions;
import store.domain.stock.BasicStock;
import store.domain.stock.ProductStock;
import store.domain.stock.PromotionStock;
import store.domain.stock.Stock;
import store.domain.store.StockInfo;

import java.util.Optional;

public class ProductData {

    private final String name;
    private final long price;
    private final int quantity;
    private final Optional<String> promotionName;

    public ProductData(String name, long price, int quantity, Optional<String> promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    public StockInfo toStockInfo(Promotions promotions) {
        Product product = getProduct();
        ProductStock productStock = getProductStock(promotions);
        return new StockInfo(product, productStock);
    }

    public Product getProduct() {
        Name name = new Name(this.name);
        Price price = Price.valueOf(this.price);
        return new Product(name, price);
    }

    private ProductStock getProductStock(Promotions promotions) {
        Stock stock = new Stock(this.quantity);

        if (promotionName.isEmpty()) {
            return new BasicStock(stock);
        }

        Promotion promotion = promotions.pick(promotionName.get());
        return new PromotionStock(stock, promotion);
    }

}
