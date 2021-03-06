package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.RestaurantImage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAORestaurantImage {

    private DatabaseReference databaseReference;
    public DAORestaurantImage()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(RestaurantImage.class.getSimpleName());
    }

    public Task<Void> add(RestaurantImage restaurantImage)
    {
        return databaseReference.push().setValue(restaurantImage);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

}
