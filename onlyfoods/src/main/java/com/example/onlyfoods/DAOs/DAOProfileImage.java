package com.example.onlyfoods.DAOs;


import com.example.onlyfoods.Models.ProfileImage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOProfileImage {

    private DatabaseReference databaseReference;
    public DAOProfileImage()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://onlyfoods-e16b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = db.getReference(ProfileImage.class.getSimpleName());
    }

    public Task<Void> add(ProfileImage bd)
    {
        return databaseReference.push().setValue(bd);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key){
        return databaseReference.child(key).removeValue();
    }

    public Query getByUserKey(String userKey){
        return databaseReference.orderByChild("userKey").startAt(userKey).endAt(userKey).limitToFirst(1);
    }

    public void removeListener(ValueEventListener listener){
        databaseReference.removeEventListener(listener);
    }


}
