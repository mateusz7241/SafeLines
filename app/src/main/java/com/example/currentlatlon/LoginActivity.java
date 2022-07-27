package com.example.currentlatlon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    private Button signIn;
    private EditText emailEditText,passEditText;
    private TextView forgotPass,registerTV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passEditText = findViewById(R.id.passEditText);
        signIn = findViewById(R.id.signIn);

        forgotPass = findViewById(R.id.forgotPass);
        registerTV = findViewById(R.id.registerTV);
        mAuth = FirebaseAuth.getInstance();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

    }

    private void userLogin(){
        String email = emailEditText.getText().toString().trim();
        String password = passEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError(getString(R.string.typeEmail));
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError(getString(R.string.typeEmailConfirm));
            emailEditText.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passEditText.setError(getString(R.string.typePassword));
            passEditText.requestFocus();
            return;
        }
        if(password.length()<6){
            passEditText.setError(getString(R.string.typePassword6));
            passEditText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        Intent intent = new Intent(LoginActivity.this,FullVersionActivity.class);
                        startActivity(intent);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, getString(R.string.checkMailBox), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.somethingWasWrong), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}