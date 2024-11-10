package store.view.setup;

import store.view.dto.ProductData;
import store.view.dto.PromotionData;

import java.util.List;

public interface StoreDataProvider {
    List<PromotionData> providePromotionData();
    List<ProductData> provideProductData();
}
