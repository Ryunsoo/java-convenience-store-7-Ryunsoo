package store.domain.order;

import store.domain.product.Price;
import store.domain.product.Product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderSheets {

    private final List<OrderSheet> orderSheets;

    public OrderSheets(List<OrderSheet> orderSheets) {
        this.orderSheets = orderSheets;
    }

    public Price nonPromotionAmount() {
        return orderSheets.stream()
                .map(OrderSheet::generalPrice)
                .reduce(Price::add)
                .orElse(Price.valueOf(0));
    }

    public List<OrderSheet> onlyAppliedPromotion() {
        return orderSheets.stream()
                .filter(OrderSheet::appliedPromotion)
                .toList();
    }

    public int totalQuantity() {
        return orderSheets.stream()
                .map(OrderSheet::totalQuantity)
                .reduce(0, Integer::sum);
    }

    public Price totalAmount() {
        return orderSheets.stream()
                .map(OrderSheet::totalAmount)
                .reduce(Price::add)
                .orElse(Price.valueOf(0));
    }

    public Price promotionAmount() {
        return orderSheets.stream()
                .map(OrderSheet::promotionDiscountPrice)
                .reduce(Price::add)
                .orElse(Price.valueOf(0));
    }

    public Map<Product, Integer> getFinalQuantities() {
        return orderSheets.stream()
                .collect(Collectors.toMap(OrderSheet::getProduct, OrderSheet::totalQuantity));
    }

    public List<OrderSheet> getOrderSheets() {
        return orderSheets;
    }

}
