package pl.dplewa.shoppinglistapp.data;

public class Product {
    public String name;
    public Double price;
    public Long count;
    public Boolean isPurchased;

    public Product() {
        // required by firebase
    }

    public Product(String name, Double price, Long count, Boolean isPurchased) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.isPurchased = isPurchased;
    }
}
