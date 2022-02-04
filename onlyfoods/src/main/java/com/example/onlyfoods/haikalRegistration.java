package com.example.onlyfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class haikalRegistration extends AppCompatActivity {

    private EditText RegisterUsername, RegisterEmail, RegisterPassword, ConfirmPassword;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

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
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(haikalRegistration.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {
        String username = RegisterUsername.getText().toString().trim();
        String email = RegisterEmail.getText().toString().trim();
        String password = RegisterPassword.getText().toString().trim();
        String confirmPassword = ConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || username.length()<5){
            showError(RegisterUsername, "Username must be 5 characters or more");
        }
        else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showError(RegisterEmail, "Invalid email");
        }
        else if (password.isEmpty() || password.length()<8){
            showError(RegisterPassword, "Password must be 8 characters or more");
        }
        else if (confirmPassword.isEmpty() || !confirmPassword.equals(password)){
            showError(ConfirmPassword, "Password does not match");
        }
        else{
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(username);
                        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DAOUser daoUser = new DAOUser();
                        daoUser.addWithSpecificId(userUid, user).addOnSuccessListener(suc->{
                            Toast.makeText(haikalRegistration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(er->{
                            Toast.makeText(haikalRegistration.this, "Failed to register. Try again", Toast.LENGTH_SHORT).show();
                        });

                        mLoadingBar.dismiss();
                        Intent intent = new Intent(haikalRegistration.this, haikalLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(haikalRegistration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void showError(EditText ET, String s) {
        ET.setError(s);
        ET.requestFocus();
    }
}