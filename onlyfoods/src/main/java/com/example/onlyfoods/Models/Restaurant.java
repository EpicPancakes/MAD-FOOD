package com.example.onlyfoods.Models;

import java.util.Date;

public class Restaurant {

    private String restaurantName;
    private String category;
    private String location;

    public Restaurant() { }

    public Restaurant(String restaurantName, String category, String location) {
        this.restaurantName = restaurantName;
        this.category = category;
        this.location = location;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
