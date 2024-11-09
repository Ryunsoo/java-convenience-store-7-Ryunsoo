package store.domain.stock;

public class BasicStock {

    private final Stock stock;

    public BasicStock(Stock stock) {
        this.stock = stock;
    }

    public static BasicStock convert(ProductStock productStock) {
        return new BasicStock(productStock.getStock());
    }

    public int availableQuantity(int quantity) {
        return stock.available(quantity);
    }

    public void deduct(int quantity) {
        if (stock.available(quantity) != quantity) {
            throw new RuntimeException("차감할 수 있는 재고 수량을 초과했습니다.");
        }
        stock.deduct(quantity);
    }

}
