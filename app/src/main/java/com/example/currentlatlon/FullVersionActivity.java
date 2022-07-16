package com.example.currentlatlon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
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

public class FullVersionActivity extends AppCompatActivity {

    private Button onSecureButton,pointButton,logoutTV;
    private TextView latitude2,longitude2;
    private GpsTracker gpsTracker;
    BackgroundSoundService backgroundSoundService2;


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

        latitude2 = findViewById(R.id.latitude2);
        longitude2 = findViewById(R.id.longitude2);



        final TextView greetingTV = (TextView) findViewById(R.id.greetingTV);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        onSecureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                           // getLocation();
                        } catch( InterruptedException e ) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getLocation();
                            }
                        });
                    }
                }).start();

                //pobieranie lokalizacji zrobić w ASYNC TASKU koniecznie !!!





//                    getLocation(view);
                    //compareLatLng(view); (zrobic porownywanie w wątku thread)
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
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FullVersionActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        //łączenie z baza danych
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

    public void getLocation(){
        gpsTracker = new GpsTracker(FullVersionActivity.this);
        if(gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            latitude2.setText(String.valueOf(latitude));
            longitude2.setText(String.valueOf(longitude));
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void compareLatLng(View view){
        double latitutdeFV = Double.parseDouble(latitude2.getText().toString());
        double longitudeFV = Double.parseDouble(longitude2.getText().toString());


        if(latitutdeFV != 50.0068552 && longitudeFV != 22.4651861) { // jesli dlugosc i szerokosc jest ta sama co znacznik
            Toast.makeText(FullVersionActivity.this,"DZIALA",Toast.LENGTH_SHORT).show();
            playBackgroundSound(view);
        }
        if(latitutdeFV == 50.0078552 && longitudeFV == 22.4751861){
            Toast.makeText(FullVersionActivity.this,"DZIALA",Toast.LENGTH_SHORT).show();
            playBackgroundSound(view);
        }else if(latitutdeFV == 50.0168552 && longitudeFV == 22.4754861){
            Toast.makeText(FullVersionActivity.this,"DZIALA2",Toast.LENGTH_SHORT).show();
            playBackgroundSound(view);
        }else if(latitutdeFV == 50.0178552 && longitudeFV == 22.4756861){
            Toast.makeText(FullVersionActivity.this,"DZIALA3",Toast.LENGTH_SHORT).show();
            playBackgroundSound(view);
        }else{
            //stopSound(view);
        }
    }

    public void playBackgroundSound(View view){
        Intent intent = new Intent(FullVersionActivity.this,BackgroundSoundService.class);
        startService(intent);
    }
    public void stopSound(View view){
        backgroundSoundService2.onDestroy();
    }
}