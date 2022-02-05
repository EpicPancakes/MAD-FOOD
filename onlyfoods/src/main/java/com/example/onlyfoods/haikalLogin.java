package com.example.onlyfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class haikalLogin extends AppCompatActivity {

    TextView TVRegister, TVForgotPass;
    EditText LoginEmail, LoginPassword;
    Button btnLogin, LoginGoogle;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haikal_login);

        TVRegister = (TextView)findViewById(R.id.TVRegister);

        //Takes user to Register activity
        TVRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(haikalLogin.this, haikalRegistration.class);
                startActivity(intent);

            }
        });

        LoginEmail = findViewById(R.id.ETLoginEmail);
        LoginPassword = findViewById(R.id.ETLoginPassword);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(haikalLogin.this);

        //attempt login
        btnLogin = findViewById(R.id.BtnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        TVForgotPass = (TextView)findViewById(R.id.TVForgotPass);

        //Takes user to Password reset activity
        TVForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(haikalLogin.this, ForgotPassword.class);
                startActivity(intent);

            }
        });

        LoginGoogle = findViewById(R.id.LoginGoogle);
        LoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(haikalLogin.this, GoogleSignInActivity.class);
                startActivity(intent);
            }
        });


    }

    private void checkCredentials() {
        String email = LoginEmail.getText().toString().trim();
        String password = LoginPassword.getText().toString().trim();

        //check for valid email
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showError(LoginEmail, "Invalid email");
        }
        //check if password is valid
        else if (password.isEmpty() || password.length()<8){
            showError(LoginPassword, "Password must be 8 characters or more");
        }
        else{
            //show loading message
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            //check credentials provided with database
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //redirect to main menu
                        mLoadingBar.dismiss();
                        Intent intent = new Intent(haikalLogin.this, MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                        //show error
                        mLoadingBar.dismiss();
                        Toast.makeText(haikalLogin.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showError(EditText ET, String s) {
        ET.setError(s);
        ET.requestFocus();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(haikalLogin.this, MainMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
        System.exit(0);
    }
}