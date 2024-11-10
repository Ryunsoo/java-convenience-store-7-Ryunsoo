package store.domain.store;

import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.Promotions;
import store.domain.stock.ProductStocks;
import store.view.dto.ProductData;
import store.view.dto.PromotionData;
import store.view.setup.StoreDataProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Staff {

    private final StoreDataProvider storeDataProvider;

    public Staff(StoreDataProvider storeDataProvider) {
        this.storeDataProvider = storeDataProvider;
    }

    public Products prepareProducts() {
        StockInfos stockInfos = getStockInfos();
        Map<Product, ProductStocks> products = getAllProducts().stream()
                .collect(Collectors.toMap(Function.identity(), stockInfos::findStocks));
        return new Products(products);
    }

    public List<Product> getAllProducts() {
        return storeDataProvider.provideProductData()
                .stream()
                .map(ProductData::getProduct)
                .distinct()
                .toList();
    }

    private StockInfos getStockInfos() {
        Promotions promotions = getPromotions();
        List<StockInfo> stockInfos = storeDataProvider.provideProductData()
                .stream()
                .map(productData -> productData.toStockInfo(promotions))
                .toList();
        return new StockInfos(stockInfos);
    }

    private Promotions getPromotions() {
        List<Promotion> promotions = storeDataProvider.providePromotionData()
                .stream()
                .map(PromotionData::toPromotion)
                .toList();
        return new Promotions(promotions);
    }

}
