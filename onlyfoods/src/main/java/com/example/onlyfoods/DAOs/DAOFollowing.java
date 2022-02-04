package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.Following;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOFollowing {

    private DatabaseReference databaseReference;
    public DAOFollowing()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(Following.class.getSimpleName());
    }

    public Task<Void> add(Following following)
    {
        return databaseReference.push().setValue(following);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

}
