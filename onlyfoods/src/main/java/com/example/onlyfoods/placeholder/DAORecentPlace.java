package com.example.onlyfoods.placeholder;

import com.example.onlyfoods.RecentPlace;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAORecentPlace {

    private DatabaseReference databaseReference;
    public DAORecentPlace()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(RecentPlace.class.getSimpleName());
    }

    public Task<Void> add(RecentPlace rp)
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

}
