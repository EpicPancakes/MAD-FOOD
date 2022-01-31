package com.example.onlyfoods.Models;

import java.util.Date;

public class RecentPlace {

    private String restaurantKey;
    private String userKey;
    private Date date;

    public RecentPlace() { }

    public RecentPlace(String restaurantKey, String userKey, Date date) {
        this.restaurantKey = restaurantKey;
        this.userKey = userKey;
        this.date = date;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
