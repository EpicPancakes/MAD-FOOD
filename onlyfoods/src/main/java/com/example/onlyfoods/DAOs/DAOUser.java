package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.Review;
import com.example.onlyfoods.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

}
