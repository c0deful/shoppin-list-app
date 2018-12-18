package pl.dplewa.shoppinglistapp.data;

public class Shop {
    public String name;
    public String description;
    public Double latitude;
    public Double longtitude;
    public Double radius;

    public Shop() {
        // required by firebase
    }

    public Shop(String name, String description, Double latitude, Double longtitude, Double radius) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.radius = radius;
    }
}
