package store.domain.store;

import store.domain.common.Name;
import store.domain.order.OrderSheets;
import store.domain.product.Product;
import store.domain.promotion.PromotionResult;
import store.domain.stock.ProductStocks;
import store.view.dto.PurchaseInfo;
import store.view.dto.StockView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ConvenienceStore {

    private final Products products;

    public ConvenienceStore(Staff staff) {
        this.products = staff.prepareProducts();
    }

    public Product selectProduct(PurchaseInfo purchaseInfo) {
        Name productName = purchaseInfo.getProductName();
        int quantity = purchaseInfo.getQuantity();
        return products.select(productName, quantity);
    }

    public PromotionResult applyPromotion(Product product, int quantity, LocalDateTime dateTime) {
        ProductStocks productsStocks = products.getStocks(product);
        return productsStocks.checkPromotion(dateTime, quantity);
    }

    public void updateStocks(OrderSheets orderSheets, LocalDateTime dateTime) {
        Map<Product, Integer> finalQuantities = orderSheets.getFinalQuantities();

        for (Map.Entry<Product, Integer> entry : finalQuantities.entrySet()) {
            ProductStocks productsStocks = products.getStocks(entry.getKey());
            productsStocks.deduct(dateTime, entry.getValue());
        }
    }

    public List<StockView> getCurrentStockInfo() {
        return products.getStockViews();
    }

}
