package com.example.onlyfoods.Models;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class User {

    public String username;
    private Map<String, Boolean> recentPlaces; // <recentPlaceKey, true>
    private Map<String, Boolean> reviews; // <recentPlaceKey, true>
    private Map<String, Boolean> recommendations; // <recentPlaceKey, true>
    private Map<String, Boolean> followers; // <recentPlaceKey, true>
    private Map<String, Boolean> following; // <recentPlaceKey, true>
    private String userKey;


    public User() { }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Boolean> getRecentPlaces() {
        return recentPlaces;
    }

    public void setRecentPlaces(Map<String, Boolean> recentPlaces) {
        this.recentPlaces = recentPlaces;
    }

    public Map<String, Boolean> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.followers = followers;
    }

    public Map<String, Boolean> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, Boolean> following) {
        this.following = following;
    }

    public Map<String, Boolean> getReviews() {
        return reviews;
    }

    public void setReviews(Map<String, Boolean> reviews) {
        this.reviews = reviews;
    }

    public Map<String, Boolean> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Map<String, Boolean> recommendations) {
        this.recommendations = recommendations;
    }

    @Exclude
    public String getUserKey() {
        return userKey;
    }

    @Exclude
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @Exclude
    public int getFollowersCount(){
        if(followers!= null){
            return followers.size();
        }else{
            return 0;
        }
    }

    @Exclude
    public int getFollowingCount(){
        if(following!=null){
            return following.size();
        }else{
            return 0;
        }
    }

    @Exclude
    public int getReviewsCount(){
        if(reviews!=null){
            return reviews.size();
        }else{
            return 0;
        }
    }
}
