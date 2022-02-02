package com.example.onlyfoods;

import java.util.ArrayList;

public class RecommendationOld {
    private String mName;
    private boolean mOnline;

    public RecommendationOld(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    private static int lastRecommendationId = 0;

    public static ArrayList<RecommendationOld> createRecommendationsList(int numRecommendations) {
        ArrayList<RecommendationOld> contacts = new ArrayList<RecommendationOld>();

        for (int i = 1; i <= numRecommendations; i++) {
            contacts.add(new RecommendationOld( ""+ ++lastRecommendationId, i <= numRecommendations / 2));
        }

        return contacts;
    }
}
