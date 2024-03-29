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

import Model.PointMap;
import Model.User;

public class FullVersionActivity extends AppCompatActivity {

    private Button onSecureButton,pointButton,logoutTV,infoButton,removePointButton,removeUserButton;
    private TextView latitude2,longitude2,distTV;
    private GpsTracker gpsTracker;

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
        infoButton = findViewById(R.id.infoButton);
        removePointButton = findViewById(R.id.removePointButton);
        removeUserButton = findViewById(R.id.removeUserButton);


        removePointButton.setVisibility(View.INVISIBLE);
        removeUserButton.setVisibility(View.INVISIBLE);

        gpsTracker = new GpsTracker(this);
        latitude2 = findViewById(R.id.latitude2);
        longitude2 = findViewById(R.id.longitude2);
        distTV = findViewById(R.id.distTV);

        latitude2.setTextIsSelectable(true);
        longitude2.setTextIsSelectable(true);



        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("role");

        userRoleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = snapshot.getValue(String.class);
                if(role.equals("u")){
                    removePointButton.setVisibility(View.GONE);
                    removeUserButton.setVisibility(View.GONE);
                }else if(role.equals("a")){
                    removePointButton.setVisibility(View.VISIBLE);
                    removeUserButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error download role",error.toString());
            }
        });


        final TextView greetingTV = findViewById(R.id.greetingTV);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        removePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullVersionActivity.this,ListPointsRemove.class);
                startActivity(intent);
            }
        });

        removeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullVersionActivity.this,UserRemoveActivity.class);
                startActivity(intent);
            }
        });

        onSecureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getLocation();
                        handler.postDelayed(this,3000);
                    }
                });
            }});

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                compareLatLng();
            }
        };
        runOnUiThread(runnable);


        pointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullVersionActivity.this,ListPointsActivity.class);
                startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullVersionActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });


        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FullVersionActivity.this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(FullVersionActivity.this, getString(R.string.logutsuccesful), Toast.LENGTH_SHORT).show();
            }
        });

        onSecureButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onSecureButton.isLongClickable() == true){
                    stopSound();
                    stopGpsService();
                    return true;
                }
                return false;
            }
        });

        //łączenie z baza danych aktualnego uzytkownika
        user = FirebaseAuth.getInstance().getCurrentUser();
        //tworzenie referencji do sciezki Users
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //pobiernaie Uid, czyli ID aktualnego usera
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    protected void onPause(){
        super.onPause();
    }
    protected void onResume(){
        super.onResume();
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
        double latitudeFV = gpsTracker.getLatitude();
        double longitudeFV = gpsTracker.getLongitude();

        reference = FirebaseDatabase.getInstance().getReference("Points");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                List<PointMap> pointMapList = jsonToMapPointsList(snapshot);
                for (int i = 0; i < pointMapList.size(); i++) {
                    PointMap currentPoint = pointMapList.get(i);



                    if (distance(latitudeFV, longitudeFV, currentPoint.latitude, currentPoint.longitude)  > 0.004 && distance(latitudeFV, longitudeFV, currentPoint.latitude, currentPoint.longitude) <= 0.010) {
                        Toast.makeText(FullVersionActivity.this, getString(R.string.fivestepsprev), Toast.LENGTH_LONG).show();
                        playBackgroundSound();
                    }
                    else if(distance(latitudeFV,longitudeFV,currentPoint.latitude,currentPoint.longitude) > 0.010 && (distance(latitudeFV,longitudeFV,currentPoint.latitude,currentPoint.longitude) <= 0.020)){
                        vibrateMessages();
                    }
                    else{
                        stopSound();
                    }

                    double dist = distance(latitudeFV, longitudeFV, currentPoint.latitude, currentPoint.longitude);
                    distTV.setText(String.valueOf(dist));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DatabaseError",error.getMessage());
            }
        });


    }

    public void stopSound(){
       Intent intent = new Intent(this,BackgroundSoundService.class);
       stopService(intent);
    }

    public void stopGpsService(){
        Intent intent = new Intent(this,GpsTracker.class);
        stopService(intent);
    }

    public void playBackgroundSound(){
        Intent intent = new Intent(FullVersionActivity.this,BackgroundSoundService.class);
        startService(intent);
    }
    public void vibrateMessages(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //vibrate to 1000 milisecond

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            v.vibrate(VibrationEffect.createOneShot(2000,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 30
            v.vibrate(1000);
        }
    }
    public void vibrateCancel(){
        Vibrator v1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v1.cancel();
    }
    private double distance(double lat1,double lon1,double lat2,double lon2){
        double earthRadius = 6371; //promien ziemi w kilometrach
        double dLat = Math.toRadians(lat2-lat1); //roznica szerokosci w radianach
        double dLon = Math.toRadians(lon2-lon1); //roznica dlugosci w radianach

        double sindLat = Math.sin(dLat/2); // sin roznicy szerokosci
        double sindLon = Math.sin(dLon/2); // sin roznicy dlugosci

        double a = Math.pow(sindLat,2) + Math.pow(sindLon,2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)); // podniesienie do kwadratu sinusów szerokosci i dlugosci oraz mnozenie przez iloczyc cosinusów szerokości punktów
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a)); // 2-krotny arcus tangens z ilorazem pierwiastkow kwadratowych a i 1-a
        double dist = earthRadius * c;
        return dist;
    }

    private List<PointMap> jsonToMapPointsList(DataSnapshot snapshot) {
        List<PointMap> mapPointList = new ArrayList<>();
        for (DataSnapshot point : snapshot.getChildren()){
            mapPointList.add(new PointMap(Double.parseDouble(point.child("latitude").getValue().toString()),
                    Double.parseDouble(point.child("longitude").getValue().toString())));

        }
        return mapPointList;
    }
}