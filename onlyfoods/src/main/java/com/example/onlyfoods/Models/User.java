package com.example.onlyfoods.Models;

import java.util.Map;

public class User {

    private String username;
    private String password;
    private Map<String, Boolean> recentPlaces; // <recentPlaceKey, true>
    private Map<String, Boolean> reviews; // <recentPlaceKey, true>
    private Map<String, Boolean> recommendations; // <recentPlaceKey, true>
    private Map<String, Boolean> followers; // <recentPlaceKey, true>
    private Map<String, Boolean> following; // <recentPlaceKey, true>


    public User() { }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
