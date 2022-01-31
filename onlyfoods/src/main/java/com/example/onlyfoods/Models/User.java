package com.example.onlyfoods.Models;

import java.util.Map;

public class User {

    private String username;
    private String password;
    private Map<String, Boolean> recentPlaces; // <recentPlaceKey, true>

    public User() { }

    public User(String username, String password, Map<String, Boolean> recentPlaces) {
        this.username = username;
        this.password = password;
        this.recentPlaces = recentPlaces;
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
}
