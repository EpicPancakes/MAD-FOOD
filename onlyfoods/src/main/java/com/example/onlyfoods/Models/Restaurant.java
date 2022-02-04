package com.example.onlyfoods.Models;

import com.google.firebase.database.Exclude;

public class Restaurant {

    private String restaurantName;
    private String category;
    private String location;
    private String restaurantKey;
    private String restaurantImageUrl;

    public Restaurant() { }

    public Restaurant(String restaurantName, String category, String location, String restaurantImageUrl) {
        this.restaurantName = restaurantName;
        this.category = category;
        this.location = location;
        this.restaurantImageUrl = restaurantImageUrl;
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

    public String getRestaurantImageUrl() {
        return restaurantImageUrl;
    }

    public void setRestaurantImageUrl(String restaurantImageUrl) {
        this.restaurantImageUrl = restaurantImageUrl;
    }

    @Exclude
    public String getRestaurantKey() {
        return restaurantKey;
    }

    @Exclude
    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }
}
