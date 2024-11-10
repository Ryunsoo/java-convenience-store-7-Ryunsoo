package store.controller;

import store.domain.common.Name;
import store.domain.order.OrderSheets;
import store.domain.product.Product;
import store.domain.promotion.PromotionResult;
import store.domain.stock.ProductStocks;
import store.domain.store.Products;
import store.domain.store.Staff;
import store.view.dto.PurchaseInfo;
import store.view.dto.StockView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConvenienceStore {

    private final List<Product> products;
    private final Products stocks;

    public ConvenienceStore(Staff staff) {
        this.products = staff.getAllProducts();
        this.stocks = staff.prepareProducts();
    }

    public Product selectProduct(PurchaseInfo purchaseInfo) {
        Name productName = purchaseInfo.getProductName();
        int quantity = purchaseInfo.getQuantity();
        return stocks.select(productName, quantity);
    }

    public PromotionResult applyPromotion(Product product, int quantity, LocalDateTime dateTime) {
        ProductStocks productsStocks = stocks.getStocks(product);
        return productsStocks.checkPromotion(dateTime, quantity);
    }

    public void updateStocks(OrderSheets orderSheets, LocalDateTime dateTime) {
        Map<Product, Integer> finalQuantities = orderSheets.getFinalQuantities();

        for (Map.Entry<Product, Integer> entry : finalQuantities.entrySet()) {
            ProductStocks productsStocks = stocks.getStocks(entry.getKey());
            productsStocks.deduct(dateTime, entry.getValue());
        }
    }

    public List<StockView> getCurrentStockInfo() {
        List<StockView> stockViews = new ArrayList<>();
        for (Product product : products) {
            ProductStocks productStocks = stocks.getStocks(product);
            List<StockView> productStockView = productStocks.getStockViews(product);
            stockViews.addAll(productStockView);
        }
        return stockViews;
    }

}
