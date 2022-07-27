package com.example.currentlatlon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class FullVersionActivity extends AppCompatActivity {

    private Button onSecureButton,pointButton,logoutTV;
    private TextView latitude2,longitude2;
    private GpsTracker gpsTracker;
    BackgroundSoundService backgroundSoundService2;

    private Button testButton;

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


        gpsTracker = new GpsTracker(this);
        latitude2 = findViewById(R.id.latitude2);
        longitude2 = findViewById(R.id.longitude2);


        testButton = findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compareLatLng();
            }
        });
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


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLocation();
                        compareLatLng();
                        handler.postDelayed(this,5000);

                    }
                },5000);

            }});

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
                Toast.makeText(FullVersionActivity.this, "Pomyślnie wylogowano", Toast.LENGTH_SHORT).show();
            }
        });

        //łączenie z baza danych
        user = FirebaseAuth.getInstance().getCurrentUser();
        //tworzenie referencji do sciezki Users
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //pobiernaie Uid tzw ID aktualnego usera
        userID = user.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //pobieranie danych o uzytkowniku
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;
                    greetingTV.setText(getString(R.string.greetingMessage) + " " + fullName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FullVersionActivity.this, getString(R.string.somethingWasWrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(this);
        if(gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            latitude2.setText(String.valueOf(latitude));
            longitude2.setText(String.valueOf(longitude));
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void compareLatLng(){
        double latitutdeFV = gpsTracker.getLatitude();
        double longitudeFV = gpsTracker.getLongitude();

        reference = FirebaseDatabase.getInstance().getReference("Points");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                List<PointMap> pointMapList = jsonToMapPointsList(snapshot);
                for (int i = 0; i < pointMapList.size(); i++) {

                    PointMap currentPoint = pointMapList.get(i);
                    System.out.println(currentPoint);

                    if(distance(latitutdeFV,longitudeFV,currentPoint.latitude,currentPoint.longitude) < 0.0008){
                        Toast.makeText(FullVersionActivity.this,"Jestes kilka metrów przed przejsciem",Toast.LENGTH_LONG).show();
                        playBackgroundSound();
                        vibrateMessages();
                    }

                }


//                for (DataSnapshot snapshot1 : snapshot.getChildren()){
//                    String lat2a = snapshot1.child("latitude").getValue().toString();
//                    String lon2a = snapshot1.child("longitude").getValue().toString();
//                    //String name = snapshot1.child("name").getValue().toString();
//                    lat2 = Double.parseDouble(lat2a);
//                    lon2  = Double.parseDouble(lon2a);
//
//

//
//                    //System.out.println("SZEROKOSC " +lat2 + "\n" ); //wypisuje szerokosci punktow
//                   // System.out.println("DLUGOSC : " + lon2 + "\n"); //wypisuje dlugosci punktow
//                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DatabaseError",error.getMessage().toString());
            }
        });

    }

    public void playBackgroundSound(){
        Intent intent = new Intent(FullVersionActivity.this,BackgroundSoundService.class);
        startService(intent);
    }
    public void stopSound(){
        backgroundSoundService2.onDestroy();
    }
    public void vibrateMessages(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //vibrate to 1000 milisecond

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            v.vibrate(VibrationEffect.createOneShot(1500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 30
            v.vibrate(1500);
        }
    }
    private double distance(double lat1,double lon1,double lat2,double lon2){
        double earthRadius = 6371; //promien ziemi w kilometrach
        double dLat = Math.toRadians(lat2-lat1); //roznica szerokosci
        double dLon = Math.toRadians(lon2-lon1); //roznica dlugosci

        double sindLat = Math.sin(dLat/2); // sin roznicy szerokosci
        double sindLon = Math.sin(dLon/2); // sin roznicy dlugosci

        double a = Math.pow(sindLat,2) + Math.pow(sindLon,2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double dist = earthRadius * c;
        return dist;
    }

    private PointMap jsonToMapPoint(DataSnapshot snaposhot) {
        return new PointMap(Double.parseDouble(snaposhot.child("latitude").getValue().toString()),
                Double.parseDouble(snaposhot.child("longitude").getValue().toString()));
    }

    private List<PointMap> jsonToMapPointsList(DataSnapshot snapshot) {
        List<PointMap> mapPointList = new ArrayList<>();
        for (DataSnapshot snapshot1 : snapshot.getChildren()){
            mapPointList.add(new PointMap(Double.parseDouble(snapshot1.child("latitude").getValue().toString()),
                    Double.parseDouble(snapshot1.child("longitude").getValue().toString())));

        }
        return mapPointList;
    }


}