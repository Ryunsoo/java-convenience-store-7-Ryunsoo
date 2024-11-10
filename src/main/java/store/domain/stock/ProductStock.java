package store.domain.stock;

public abstract class ProductStock {

    protected final Stock stock;

    protected ProductStock(Stock stock) {
        this.stock = stock;
    }

    public int availableQuantity(int quantity) {
        return stock.available(quantity);
    }

    public int deductMaximum(int quantity) {
        int deductQuantity = this.availableQuantity(quantity);
        stock.deduct(deductQuantity);
        return deductQuantity;
    }

}
