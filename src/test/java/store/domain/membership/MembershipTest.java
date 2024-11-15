package store.domain.membership;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.product.Price;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MembershipTest {

    @DisplayName("할인율과 일일 할인 가능 금액으로 멤버십을 생성할 수 있다.")
    @Test
    void createMembership() {
        int percentage = 30;
        long oneDayLimit = 8000;

        Membership membership = new Membership(percentage, oneDayLimit);

        assertThat(membership).extracting("percentage", "oneDayLimit")
                .containsExactly(0.3, Price.valueOf(8000));
    }

    @DisplayName("할인율이 0과 100 범위를 넘어가면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void discountPercentageShouldBe0To100(int percentage) {
        long oneDayLimit = 8000;

        assertThatThrownBy(() -> new Membership(percentage, oneDayLimit))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("멤버십 할인율이 유효하지 않습니다.");
    }

    @DisplayName("멤버십 할인은 금액의 30프로이다.")
    @Test
    void calculateMembershipDiscount() {
        Membership membership = new Membership(30, 8000);
        Price price = Price.valueOf(5000);

        Price discountPrice = membership.calculateDiscount(price);

        assertThat(discountPrice).isEqualTo(Price.valueOf(1500));
    }

    @DisplayName("멤버십 할인은 일일 할인 가능 금액내에서 가능하다.")
    @Test
    void discountCannotBeOverOneDayLimit() {
        Membership membership = new Membership(30, 1000);
        Price price = Price.valueOf(5000);

        Price discountPrice = membership.calculateDiscount(price);

        assertThat(discountPrice).isEqualTo(Price.valueOf(1000));
    }

}
