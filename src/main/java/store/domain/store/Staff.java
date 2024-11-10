package store.domain.store;

import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.Promotions;
import store.domain.stock.ProductStocks;
import store.view.setup.StoreDataProvider;
import store.view.dto.ProductData;
import store.view.dto.PromotionData;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Staff {

    private final StoreDataProvider storeDataProvider;

    public Staff(StoreDataProvider storeDataProvider) {
        this.storeDataProvider = storeDataProvider;
    }

    public Products prepareProducts() {
        StockInfos stockInfos = getStockInfos();
        Set<Product> allProducts = getAllProducts();

        Map<Product, ProductStocks> products = allProducts.stream()
                .collect(Collectors.toMap(Function.identity(), stockInfos::findStocks));
        return new Products(products);
    }

    private Set<Product> getAllProducts() {
        return storeDataProvider.provideProductData()
                .stream()
                .map(ProductData::getProduct)
                .collect(Collectors.toSet());
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
