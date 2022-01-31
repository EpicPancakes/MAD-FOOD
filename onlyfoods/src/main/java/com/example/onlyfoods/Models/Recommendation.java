package com.example.onlyfoods.Models;

import java.util.Date;

public class Recommendation {

    private String userKey;
    private String restaurantKey;
    private String recommendedUserKey;
    private Date date;
    private String recommendationMsg;

    public Recommendation() {
    }

    public Recommendation(String userKey, String restaurantKey, String recommendedUserKey, Date date, String recommendationMsg) {
        this.userKey = userKey;
        this.restaurantKey = restaurantKey;
        this.recommendedUserKey = recommendedUserKey;
        this.date = date;
        this.recommendationMsg = recommendationMsg;
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

    public String getRecommendedUserKey() {
        return recommendedUserKey;
    }

    public void setRecommendedUserKey(String recommendedUserKey) {
        this.recommendedUserKey = recommendedUserKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecommendationMsg() {
        return recommendationMsg;
    }

    public void setRecommendationMsg(String recommendationMsg) {
        this.recommendationMsg = recommendationMsg;
    }
}
