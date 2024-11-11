package store.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.common.Name;
import store.domain.product.Price;
import store.domain.product.Product;
import store.domain.promotion.Benefit;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;
import store.domain.stock.*;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductsTest {

    private Promotion defaultPromotion;

    @BeforeEach
    void setup() {
        Name name = new Name("프로모션");
        Benefit benefit = new Benefit(2);
        Period period = Period.between(LocalDate.now(), LocalDate.now().plusDays(5));
        this.defaultPromotion = new Promotion(name, benefit, period);
    }

    @DisplayName("상품 목록에서 상품명으로 상품을 찾을 수 있다.")
    @Test
    void selectProduct() {
        Product product = new Product(new Name("상품"), Price.valueOf(1000));

        BasicStock basicStock = new BasicStock(new Stock(2));
        PromotionStock promotionStock = new PromotionStock(new Stock(3), defaultPromotion);
        ProductStocks productStocks = new ProductStocks(basicStock, promotionStock);

        Products products = new Products(Map.of(product, productStocks));

        Product selected = products.select(new Name("상품"), 3);

        assertThat(selected).isEqualTo(product);
    }

    @DisplayName("상품 목록에 존재하지 않는 상품명이면 예외를 던진다.")
    @Test
    void failToSelectWithNotExistName() {
        Product product = new Product(new Name("상품"), Price.valueOf(1000));
        Products products = new Products(Map.of(product, getProductStocks()));

        assertThatThrownBy(() -> products.select(new Name("상품1"), 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    private ProductStocks getProductStocks() {
        BasicStock basicStock = new BasicStock(new Stock(2));
        PromotionStock promotionStock = new PromotionStock(new Stock(3), defaultPromotion);
        return new ProductStocks(basicStock, promotionStock);
    }

    @DisplayName("재고가 부족한 상품을 선택하면 예외를 던진다.")
    @Test
    void failToSelectWithNotEnoughProduct() {
        Product product = new Product(new Name("상품"), Price.valueOf(1000));

        BasicStock basicStock = new BasicStock(new Stock(2));
        PromotionStock promotionStock = new PromotionStock(new Stock(3), defaultPromotion);
        ProductStocks productStocks = new ProductStocks(basicStock, promotionStock);

        Products products = new Products(Map.of(product, productStocks));

        assertThatThrownBy(() -> products.select(new Name("상품"), 6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고 수량을 초과하여 구매할 수 없습니다.");
    }

}
