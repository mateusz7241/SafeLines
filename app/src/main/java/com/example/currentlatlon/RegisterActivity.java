package com.example.currentlatlon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton2;
    EditText registerLogin,registerPassword,registerConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerLogin = findViewById(R.id.registerLogin);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        registerButton2 = findViewById(R.id.registerButton2);

        registerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerLogin.getText().toString().isEmpty() || registerPassword.getText().toString().isEmpty() || registerConfirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }else if(registerPassword.getText().toString() == registerConfirmPassword.getText().toString() && !(registerLogin.getText().toString().isEmpty())){

                    String login = registerLogin.getText().toString();
                    String pass = registerPassword.getText().toString();

                    // mozna wyslac ten login i haslo do bazy danych lokalnej lub firebase
                }
            }
        });
    }
}