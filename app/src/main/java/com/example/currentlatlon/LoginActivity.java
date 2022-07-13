package com.example.currentlatlon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton2;
    private EditText loginEditText,passEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passEditText = findViewById(R.id.passEditText);
        loginButton2 = findViewById(R.id.loginButton2);

        loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginEditText.getText().toString().isEmpty() || passEditText.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Nie udało się zalogować, spróbuj ponownie", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"Udało sie poprawnie zalogować",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}