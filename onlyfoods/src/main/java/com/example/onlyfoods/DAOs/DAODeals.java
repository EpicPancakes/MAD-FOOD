package com.example.onlyfoods.DAOs;

import com.example.onlyfoods.Models.Deal;
import com.example.onlyfoods.Models.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAODeals {

    private DatabaseReference databaseReference;
    private String dealKey;

    public DAODeals(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(Deal.class.getSimpleName());
    }

    public Task<Void> add(Deal deal)
    {
        return databaseReference.push().setValue(deal);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

    //Get All
    public Task<DataSnapshot> get(){
        return databaseReference.get();
    }

    //Get One Instance
    public Task<DataSnapshot> getDealByKey(String dealKey){
        return databaseReference.child(dealKey).get();
    }

    public String getDealKey() {
        return dealKey;
    }

}
