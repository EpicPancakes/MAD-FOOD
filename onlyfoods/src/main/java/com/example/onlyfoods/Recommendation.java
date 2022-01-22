package com.example.onlyfoods;

import java.util.ArrayList;

public class Recommendation {
    private String mName;
    private boolean mOnline;

    public Recommendation(String name, boolean online) {
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

    public static ArrayList<Recommendation> createRecommendationsList(int numRecommendations) {
        ArrayList<Recommendation> contacts = new ArrayList<Recommendation>();

        for (int i = 1; i <= numRecommendations; i++) {
            contacts.add(new Recommendation("Person " + ++lastRecommendationId, i <= numRecommendations / 2));
        }

        return contacts;
    }
}
