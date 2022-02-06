package com.example.onlyfoods.Models;

import com.google.firebase.database.Exclude;

public class ProfileImage {

    private String userKey;
    private String profileImageUrl;
    private String profileImageKey;

    public ProfileImage() { }

    public ProfileImage(String userKey, String profileImageUrl) {
        this.userKey = userKey;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Exclude
    public String getProfileImageKey() {
        return profileImageKey;
    }

    @Exclude
    public void setProfileImageKey(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }
}
