package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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

    public Query getRestaurantsByQuery(String query){
        return databaseReference.orderByChild("restaurantName").startAt(query).endAt(query+"\uf8ff");
    }

    public Task<DataSnapshot> getRestaurantsByKey(String restaurantKey){
        return databaseReference.child(restaurantKey).get();
    }

    public Query getRestaurantByCategory(String categoryString){
        return databaseReference.orderByChild("category").startAt(categoryString).endAt(categoryString);
    }

    public DatabaseReference getReference(){
        return databaseReference;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }
}
