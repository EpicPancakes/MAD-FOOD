package com.example.onlyfoods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class MainMenu extends AppCompatActivity {

    private FirebaseUser user;
    private DAOUser daoUser;
    private String userID;
    Button BtnRestaurant, BtnSocial, BtnDeals, BtnWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        final TextView TVWelcome = findViewById(R.id.TVWelcome);

        daoUser = new DAOUser();
        daoUser.getByUserKeyOnce(userID).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);
                if(userProfile != null){
                    String username = userProfile.getUsername();
                    TVWelcome.setText("Welcome " + username + "!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainMenu.this, "Oh no i did a woopsie!", Toast.LENGTH_SHORT).show();
            }
        });

        BtnSocial = findViewById(R.id.BtnSocial);
        BtnSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}