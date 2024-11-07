package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("상품명 또는 가격이 null 이면 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("provideInvalidNameOrPrice")
    void productShouldHaveNameAndPrice(ProductName name, Price price) {
        assertThatThrownBy(() -> new Product(name, price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 생성에 실패했습니다.");
    }

    private static Stream<Arguments> provideInvalidNameOrPrice() {
        return Stream.of(
                Arguments.of(null, Price.valueOf(1000)),
                Arguments.of(new ProductName("이름"), null),
                Arguments.of(null, null)
        );
    }

    @DisplayName("상품명과 가격이 동일하면 같은 상품이다.")
    @Test
    void equalsByNameAndPrice() {
        Product product1 = new Product(new ProductName("이름"), Price.valueOf(1000));
        Product product2 = new Product(new ProductName("이름"), Price.valueOf(1000));

        assertThat(product1).isEqualTo(product2);
    }

    @DisplayName("상품명과 가격이 다르면 다른 상품이다.")
    @ParameterizedTest
    @MethodSource("provideDifferentProducts")
    void differentByNameAndPrice(Product product1, Product product2) {
        assertThat(product1).isNotEqualTo(product2);
    }

    private static Stream<Arguments> provideDifferentProducts() {
        return Stream.of(
                Arguments.of(
                        new Product(new ProductName("상품1"), Price.valueOf(1000)),
                        new Product(new ProductName("상품2"), Price.valueOf(1000))),
                Arguments.of(
                        new Product(new ProductName("이름"), Price.valueOf(1000)),
                        new Product(new ProductName("이름"), Price.valueOf(2000))),
                Arguments.of(
                        new Product(new ProductName("상품1"), Price.valueOf(2000)),
                        new Product(new ProductName("상품2"), Price.valueOf(1000)))
        );
    }

}
