package com.example.onlyfoods.Models;

import java.util.Date;

public class Review {

    private String userKey;
    private String restaurantKey;
    private Date date;
    private String reviewMsg;

    public Review() { }

    public Review(String userKey, String restaurantKey, Date date, String reviewMsg) {
        this.userKey = userKey;
        this.restaurantKey = restaurantKey;
        this.date = date;
        this.reviewMsg = reviewMsg;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReviewMsg() {
        return reviewMsg;
    }

    public void setReviewMsg(String reviewMsg) {
        this.reviewMsg = reviewMsg;
    }
}
