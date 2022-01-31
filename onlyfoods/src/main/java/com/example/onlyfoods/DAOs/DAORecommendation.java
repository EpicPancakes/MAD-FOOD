package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.Recommendation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAORecommendation {

    private DatabaseReference databaseReference;
    public DAORecommendation()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(Recommendation.class.getSimpleName());
    }

    public Task<Void> add(Recommendation recommendation)
    {
        return databaseReference.push().setValue(recommendation);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

}
