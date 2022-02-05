package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAORestaurant {

    private DatabaseReference databaseReference;
    private String restaurantKey;

    public DAORestaurant()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(Restaurant.class.getSimpleName());
    }

    public Task<Void> add(Restaurant restaurant)
    {
        restaurantKey = databaseReference.push().getKey();
        return databaseReference.child(restaurantKey).setValue(restaurant);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

    public Task<DataSnapshot> get(){
        return databaseReference.get();
    }

    public Task<DataSnapshot> getRestaurantsByKey(String restaurantKey){
        return databaseReference.child(restaurantKey).get();
    }

    public Task<DataSnapshot> getRestaurantByCategory(String categoryString){
        return databaseReference.orderByChild("userKey").startAt(categoryString).endAt(categoryString).get();
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }
}
