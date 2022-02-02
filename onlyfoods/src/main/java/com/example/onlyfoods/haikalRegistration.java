package com.example.onlyfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class haikalRegistration extends AppCompatActivity {

    private EditText RegisterUsername, RegisterEmail, RegisterPassword, ConfirmPassword;
    Button btnRegister;

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

        RegisterUsername = findViewById(R.id.ETRegisterUsername);
        RegisterEmail = findViewById(R.id.ETRegisterEmail);
        RegisterPassword = findViewById(R.id.ETRegisterPassword);
        ConfirmPassword = findViewById(R.id.ETConfirmPassword);
        btnRegister = findViewById(R.id.BtnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {
        String username = RegisterUsername.getText().toString();
        String email = RegisterEmail.getText().toString();
        String password = RegisterPassword.getText().toString();
        String confirmPassword = ConfirmPassword.getText().toString();

        if (username.isEmpty() || username.length()<5){
            showError(RegisterUsername, "Username must be 5 characters or more");
        }
        else if (email.isEmpty() || !email.contains("@")){
            showError(RegisterEmail, "Invalid email");
        }
        else if (password.isEmpty() || password.length()<8){
            showError(RegisterPassword, "Password must be 8 characters or more");
        }
        else if (confirmPassword.isEmpty() || !confirmPassword.equals(password)){
            showError(ConfirmPassword, "Password does not match");
        }
        else{
            Toast.makeText(this, "Call Registration Method", Toast.LENGTH_SHORT).show();
        }

    }

    private void showError(EditText ET, String s) {
        ET.setError(s);
        ET.requestFocus();
    }
}