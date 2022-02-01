package com.example.onlyfoods.Models;

public class ProfileImage {

    private String userKey;
    private String filePath;

    public ProfileImage() { }

    public ProfileImage(String userKey, String filePath) {
        this.userKey = userKey;
        this.filePath = filePath;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}