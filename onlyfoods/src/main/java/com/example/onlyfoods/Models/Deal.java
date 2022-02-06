package com.example.onlyfoods.Models;

import com.google.firebase.database.Exclude;

public class Deal {

    private String dealTitle;
    private String price;
    private String dealKey;

    private String restaurantKey;

    public Deal() { }

    public Deal(String dealTitle, String price, String restaurantKey){
        this.dealTitle = dealTitle;
        this.price = price;
        this.restaurantKey = restaurantKey;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }

    @Exclude
    public String getDealKey() { return dealKey; }

    @Exclude
    public void setDealKey(String dealKey) {
        this.dealKey = dealKey;
    }
}
