package com.example.onlyfoods.Models;

public class RestaurantImage {

    private String restaurantKey;
    private String filePath;

    public RestaurantImage() { }

    public RestaurantImage(String restaurantKey, String filePath) {
        this.restaurantKey = restaurantKey;
        this.filePath = filePath;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
