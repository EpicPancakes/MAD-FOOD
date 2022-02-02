package com.example.onlyfoods.DAOs;

import com.example.onlyfoods.Models.Recommendation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAORecommendation {

    private DatabaseReference databaseReference;
    public DAORecommendation()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(Recommendation.class.getSimpleName());
    }

    public Task<Void> add(Recommendation rp)
    {
        return databaseReference.push().setValue(rp);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

    public Query get(){
        return databaseReference.orderByKey();
    }

    public Query getByUserKey(String userKey){
        return databaseReference.orderByChild("userKey").startAt(userKey).endAt(userKey);
    }

    public Task<DataSnapshot> getAll(){
        return databaseReference.get();
    }

}
