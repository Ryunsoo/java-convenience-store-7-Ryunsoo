package store.domain.store;

import store.domain.product.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart {

    private final Map<Product, Integer> cart;

    public ShoppingCart() {
        this.cart = new HashMap<>();
    }

    public void put(Product product, int quantity) {
        cart.put(product, quantity);
    }

    public Set<Product> products() {
        return cart.keySet();
    }

    public int getQuantity(Product product) {
        return cart.get(product);
    }

}
