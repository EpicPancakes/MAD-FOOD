package com.example.onlyfoods.Models;

public class Following {

    private String userKey;
    private String followedUserKey;

    public Following() { }

    public Following(String userKey, String followedUserKey) {
        this.userKey = userKey;
        this.followedUserKey = followedUserKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getFollowedUserKey() {
        return followedUserKey;
    }

    public void setFollowedUserKey(String followedUserKey) {
        this.followedUserKey = followedUserKey;
    }
}
