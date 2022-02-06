package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;

public class DAOUser {

    private DatabaseReference databaseReference;
    public DAOUser()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public Task<Void> add(User user)
    {
        return databaseReference.push().setValue(user);
    }

    public Task<Void> addWithSpecificId(String specificId, User user)
    {
        return databaseReference.child(specificId).setValue(user);
    }

    public Task<Void> update(String key, Map<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

    public Query getByUserKey(String userKey){
        return databaseReference.child(userKey);
    }

    public Task<DataSnapshot> getByUserKeyOnce(String userKey){
        return databaseReference.child(userKey).get();
    }

    public Query getFollowersByUserKey(String userKey){
        return databaseReference.child(userKey).child("followers");
    }

    public Query getFollowingByUserKey(String userKey){
        return databaseReference.child(userKey).child("following");
    }

    public Query getSavedRestaurantsByUserKey(String userKey){
        return databaseReference.child(userKey).child("savedRestaurants");
    }

    public Query checkIfFollows(String userKey, String followsUserKey){
        return databaseReference.child(userKey).child("following").child(followsUserKey);
    }

    public Query checkIfRestaurantSaved(String userKey, String restaurantKey){
        return databaseReference.child(userKey).child("savedRestaurants").child(restaurantKey);
    }

}
