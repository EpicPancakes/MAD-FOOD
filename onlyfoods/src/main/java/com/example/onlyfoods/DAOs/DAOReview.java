package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.Review;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOReview {

    private DatabaseReference databaseReference;
    private String reviewKey;


    public DAOReview()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(Review.class.getSimpleName());
    }

    public Task<Void> add(Review rev)
    {
        reviewKey = databaseReference.push().getKey();
        return databaseReference.child(reviewKey).setValue(rev);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> updateByReview(String key, Review review)
    {
        return databaseReference.child(key).setValue(review);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

    public Query get(){
        return databaseReference.orderByKey();
    }

    public Query getByReviewKey(String reviewKey){
        return databaseReference.orderByChild("reviewKey").startAt(reviewKey).endAt(reviewKey);
    }

    public Query getByRestaurantKey(String restaurantKey){
        return databaseReference.orderByChild("restaurantKey").startAt(restaurantKey).endAt(restaurantKey);
    }

    public Query getByUserKey(String userKey){
        return databaseReference.orderByChild("userKey").startAt(userKey).endAt(userKey);
    }

    public Task<DataSnapshot> getAll(){
        return databaseReference.get();
    }

    public String getReviewKey() {
        return reviewKey;
    }

}
