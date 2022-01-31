package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.Review;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOReview {

    private DatabaseReference databaseReference;
    public DAOReview()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(Review.class.getSimpleName());
    }

    public Task<Void> add(Review review)
    {
        return databaseReference.push().setValue(review);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

}
