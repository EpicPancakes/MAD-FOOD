package com.example.onlyfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class haikalRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haikal_registration);

        TextView TVLogin = (TextView)findViewById(R.id.TVLogin);

        TVLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(haikalRegistration.this, haikalLogin.class);
                startActivity(intent);

            }
        });
    }
}