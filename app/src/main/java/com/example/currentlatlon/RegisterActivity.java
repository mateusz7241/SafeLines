package com.example.currentlatlon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity{

    private Button registerButton2;
    private EditText registerFullName,registerEmail,registerPassword;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFullName = findViewById(R.id.registerFullName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerButton2 = findViewById(R.id.registerButton2);

        //instancja bazy danych
        mAuth = FirebaseAuth.getInstance();



        registerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }


    private void registerUser(){
        String fullName = registerFullName.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String pass = registerPassword.getText().toString().trim();

        if(fullName.isEmpty()){
            registerFullName.setError(getString(R.string.typeFullName));
            registerFullName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            registerEmail.setError(getString(R.string.typeEmail));
            registerEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmail.setError(getString(R.string.typeEmailConfirm));
            registerEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            registerPassword.setError(getString(R.string.typePassword));
            registerPassword.requestFocus();
            return;
        }
        if(pass.length() < 6){
            registerPassword.setError(getString(R.string.typePassword6));
            registerPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(fullName,email);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              Toast.makeText(RegisterActivity.this, getString(R.string.registerIsComplete), Toast.LENGTH_LONG).show();
                              Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                              startActivity(intent);
                          }else{
                              Toast.makeText(RegisterActivity.this, getString(R.string.somethingWasWrong), Toast.LENGTH_LONG).show();
                          }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this, getString(R.string.somethingWasWrong), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}