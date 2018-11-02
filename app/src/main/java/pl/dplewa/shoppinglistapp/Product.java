package pl.dplewa.shoppinglistapp;

import java.math.BigDecimal;

public class Product {
    private String name;
    private BigDecimal price;
    private boolean isPurchased;

    public Product(String name, BigDecimal price, boolean isPurchased) {
        this.name = name;
        this.price = price;
        this.isPurchased = isPurchased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }
}
