package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.RecentPlace;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAORecentPlace {

    private DatabaseReference databaseReference;
    private String recentPlaceKey;


    public DAORecentPlace()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(RecentPlace.class.getSimpleName());
    }

    public Task<Void> add(RecentPlace rp)
    {
        recentPlaceKey = databaseReference.push().getKey();
        return databaseReference.child(recentPlaceKey).setValue(rp);
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

    public String getRecentPlaceKey() {
        return recentPlaceKey;
    }

}
