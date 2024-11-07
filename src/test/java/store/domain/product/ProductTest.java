package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @DisplayName("상품명과 가격으로 상품을 생성할 수 있다.")
    @Test
    void createProductWithNameAndPrice() {
        ProductName name = new ProductName("상품명");
        Price price = Price.valueOf(1000);

        Product product = new Product(name, price);
        assertThat(product).extracting("name").isEqualTo(name);
        assertThat(product).extracting("price").isEqualTo(price);
    }

}
