package com.example.onlyfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class haikalLogin extends AppCompatActivity {

    TextView TVRegister;
    EditText LoginEmail, LoginPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haikal_login);

        TVRegister = (TextView)findViewById(R.id.TVRegister);

        TVRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(haikalLogin.this, haikalRegistration.class);
                startActivity(intent);

            }
        });

        LoginEmail = findViewById(R.id.ETLoginEmail);
        LoginPassword = findViewById(R.id.ETLoginPassword);
        btnLogin = findViewById(R.id.BtnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
    }


    private void checkCredentials() {
        String email = LoginEmail.getText().toString();
        String password = LoginPassword.getText().toString();

        if (email.isEmpty() || !email.contains("@")){
            showError(LoginEmail, "Invalid email");
        }
        else if (password.isEmpty() || password.length()<8){
            showError(LoginPassword, "Password must be 8 characters or more");
        }
        else{
            Toast.makeText(this, "Call Login Method", Toast.LENGTH_SHORT).show();
        }

    }

    private void showError(EditText ET, String s) {
        ET.setError(s);
        ET.requestFocus();
    }
}