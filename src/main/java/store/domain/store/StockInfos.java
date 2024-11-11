package store.domain.store;

import store.domain.product.Product;
import store.domain.stock.*;
import store.exception.StoreSetUpFailException;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StockInfos {

    private final Map<Product, BasicStock> basicStocks;
    private final Map<Product, PromotionStock> promotionStocks;

    public StockInfos(List<StockInfo> stockInfos) {
        this.basicStocks = filterOnlyBasicStocks(stockInfos);
        this.promotionStocks = filterOnlyPromotionStocks(stockInfos);
    }

    private Map<Product, BasicStock> filterOnlyBasicStocks(List<StockInfo> stockInfos) {
        return stockInfos.stream()
                .filter(Predicate.not(StockInfo::hasPromotion))
                .collect(Collectors.toMap(
                        StockInfo::getProduct,
                        stockInfo -> (BasicStock) stockInfo.getProductStock())
                );
    }

    private Map<Product, PromotionStock> filterOnlyPromotionStocks(List<StockInfo> stockInfos) {
        return stockInfos.stream()
                .filter(StockInfo::hasPromotion)
                .collect(Collectors.toMap(
                                StockInfo::getProduct,
                                stockInfo -> (PromotionStock) stockInfo.getProductStock(),
                                (stock1, stock2) -> {
                                    throw new StoreSetUpFailException("상품은 두 개 이상의 프로모션이 적용될 수 없습니다.");
                                }
                        )
                );
    }

    public ProductStocks findStocks(Product product) {
        return new ProductStocks(findBasicStock(product), findPromotionStock(product));
    }

    private BasicStock findBasicStock(Product product) {
        return basicStocks.getOrDefault(product, new BasicStock(Stock.empty()));
    }

    private PromotionStock findPromotionStock(Product product) {
        return promotionStocks.getOrDefault(product, new EmptyPromotionStock());
    }

}
