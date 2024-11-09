package store.domain.stock;

import store.view.ProductInfo;

import java.util.List;
import java.util.function.Predicate;

public class ProductStocks {

    private final ProductStock basicStock;
    private final ProductStock promotionStock;

    public ProductStocks(ProductStock basicStock, ProductStock promotionStock) {
        this.basicStock = basicStock;
        this.promotionStock = promotionStock;
    }

    public static ProductStocks with(List<ProductInfo> productInfos) {
        // TODO 2개 이상일 때 동일 프로모션 확인
        if (productInfos.size() > 2) {
            throw new IllegalArgumentException("상품은 두 개 이상의 프로모션이 적용될 수 없습니다.");
        }

        ProductStock basicStock = filterBasicStock(productInfos);
        ProductStock promotionStock = filterPromotionStock(productInfos);

        return new ProductStocks(basicStock, promotionStock);
    }

    private static ProductStock filterBasicStock(List<ProductInfo> productInfos) {
        return productInfos.stream()
                .filter(Predicate.not(ProductInfo::hasPromotion))
                .map(ProductInfo::getProductStock)
                .findAny()
                .orElse(new ProductStock(null, new Stock(0)));
    }

    private static ProductStock filterPromotionStock(List<ProductInfo> productInfos) {
        List<ProductStock> promotionStocks = productInfos.stream()
                .filter(ProductInfo::hasPromotion)
                .map(ProductInfo::getProductStock)
                .toList();

        if (promotionStocks.size() > 1) {
            throw new IllegalArgumentException("상품은 두 개 이상의 프로모션이 적용될 수 없습니다.");
        }

        if (promotionStocks.isEmpty()) {
            return null;
        }

        return promotionStocks.getFirst();
    }

    public boolean isAvailable(int quantity) {
        int availableBasic = basicStock.availableQuantity(quantity);
        if (!hasPromotion()) {
            return availableBasic >= quantity;
        }
        int availablePromotion = promotionStock.availableQuantity(quantity);
        return availableBasic + availablePromotion >= quantity;
    }

    public PromotionStock getPromotionStock() {
        if (promotionStock == null) {
            return null;
        }
        return PromotionStock.convert(promotionStock);
    }

    public BasicStock getBasicStock() {
        return BasicStock.convert(basicStock);
    }

    public boolean hasPromotion() {
        return promotionStock != null;
    }

}
