package com.example.onlyfoods;

import java.util.Date;

public class RecentPlace {

    private String restaurant;
    private Date date;

    public RecentPlace() { }

    public RecentPlace(String restaurant, Date date) {
        this.restaurant = restaurant;
        this.date = date;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
