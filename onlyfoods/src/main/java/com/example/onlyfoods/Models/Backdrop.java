package com.example.onlyfoods.Models;

import com.google.firebase.database.Exclude;

public class Backdrop {
    private String userKey;
    private String imageName;
    private String backdropUrl;
    private String backdropKey;

    public Backdrop(){
        // empty constructor needed for firebase
    }

    public Backdrop(String userKey, String imageName, String backdropUrl) {
        if (imageName.trim().equals("")) {
            imageName = "No Name";
        }
        this.userKey = userKey;
        this.imageName = imageName;
        this.backdropUrl = backdropUrl;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    @Exclude
    public String getBackdropKey() {
        return backdropKey;
    }

    @Exclude
    public void setBackdropKey(String backdropKey) {
        this.backdropKey = backdropKey;
    }
}
