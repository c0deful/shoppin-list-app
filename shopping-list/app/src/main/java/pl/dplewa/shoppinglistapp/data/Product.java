package pl.dplewa.shoppinglistapp.data;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private final int count;
    private boolean isPurchased;

    public Product(int id, String name, BigDecimal price, int count, boolean isPurchased) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.isPurchased = isPurchased;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public boolean isPurchased() {
        return isPurchased;
    }
}
