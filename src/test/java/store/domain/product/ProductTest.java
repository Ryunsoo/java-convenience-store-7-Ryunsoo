package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import store.domain.common.Name;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품명과 가격으로 상품을 생성할 수 있다.")
    @Test
    void createProductWithNameAndPrice() {
        Name name = new Name("상품명");
        Price price = Price.valueOf(1000);

        Product product = new Product(name, price);
        assertThat(product).extracting("name").isEqualTo(name);
        assertThat(product).extracting("price").isEqualTo(price);
    }

    @DisplayName("상품명 또는 가격이 null 이면 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("provideInvalidNameOrPrice")
    void productShouldHaveNameAndPrice(Name name, Price price) {
        assertThatThrownBy(() -> new Product(name, price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 생성에 실패했습니다.");
    }

    private static Stream<Arguments> provideInvalidNameOrPrice() {
        return Stream.of(
                Arguments.of(null, Price.valueOf(1000)),
                Arguments.of(new Name("이름"), null),
                Arguments.of(null, null)
        );
    }

    @DisplayName("상품명과 가격이 동일하면 같은 상품이다.")
    @Test
    void equalsByNameAndPrice() {
        Product product1 = new Product(new Name("이름"), Price.valueOf(1000));
        Product product2 = new Product(new Name("이름"), Price.valueOf(1000));

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
                        new Product(new Name("상품1"), Price.valueOf(1000)),
                        new Product(new Name("상품2"), Price.valueOf(1000))),
                Arguments.of(
                        new Product(new Name("이름"), Price.valueOf(1000)),
                        new Product(new Name("이름"), Price.valueOf(2000))),
                Arguments.of(
                        new Product(new Name("상품1"), Price.valueOf(2000)),
                        new Product(new Name("상품2"), Price.valueOf(1000)))
        );
    }

    @DisplayName("해당 이름의 상품이면 true, 아니면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"상품, true", "이름, false"})
    void compareProductName(String name, boolean expected) {
        Name productName = new Name("상품");
        Price productPrice = Price.valueOf(1000);
        Product product = new Product(productName, productPrice);

        assertThat(product.is(new Name(name))).isEqualTo(expected);
    }

    @DisplayName("특정 수량만큼의 상품 가격을 반환한다.")
    @Test
    void calculateProductPriceWithQuantity() {
        Name productName = new Name("상품");
        Price productPrice = Price.valueOf(1000);
        Product product = new Product(productName, productPrice);

        int quantity = 5;

        Price expected = Price.valueOf(5000);
        assertThat(product.calculatePrice(quantity)).isEqualTo(expected);
    }

    @DisplayName("상품 가격 계산 시 수량이 0보다 작으면 예외를 던진다.")
    @Test
    void cannotCalculatePriceWithQuantityBelowZero() {
        Name productName = new Name("상품");
        Price productPrice = Price.valueOf(1000);
        Product product = new Product(productName, productPrice);

        int invalidQuantity = -1;

        assertThatThrownBy(() -> product.calculatePrice(invalidQuantity))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("수량이 올바르지 않습니다.");
    }

}
