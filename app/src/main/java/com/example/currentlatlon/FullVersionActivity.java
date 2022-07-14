package com.example.currentlatlon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class FullVersionActivity extends AppCompatActivity {

    private Button onSecureButton,pointButton,logoutTV;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_version);

        logoutTV = findViewById(R.id.logoutTV);
        onSecureButton = findViewById(R.id.onSecureButton);
        pointButton = findViewById(R.id.pointButton);

        final TextView greetingTV = (TextView) findViewById(R.id.greetingTV);

        onSecureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // dystans + komunikaty
            }
        });

        pointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullVersionActivity.this,ListPointsActivity.class);
                startActivity(intent);
            }
        });


        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullVersionActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        //łączenie z bazda danych
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //pobieranie danych o uzytkowniku
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;

                    greetingTV.setText("Witaj " + fullName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FullVersionActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            }
        });



    }
}