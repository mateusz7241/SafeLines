package com.example.currentlatlon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;
    private Button bt,compareButton,loginButton,registerButton;
    BackgroundSoundService backgroundSoundService;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = (TextView)findViewById(R.id.latitude);
        tvLongitude = (TextView)findViewById(R.id.longitude);
        bt = findViewById(R.id.bt);
        compareButton = findViewById(R.id.compareButton);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getLocation(view);
            }
        });

        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gpsTracker.canGetLocation==false){
                    Toast.makeText(MainActivity.this,getString(R.string.somethingWasWrong),Toast.LENGTH_SHORT).show();
                    gpsTracker.showSettingsAlert();
                }else{
                    compareLatLng(view);
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getLocation(View view){
        gpsTracker = new GpsTracker(MainActivity.this);
            if(gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                tvLatitude.setText(String.valueOf(latitude));
                tvLongitude.setText(String.valueOf(longitude));
            }else{
                gpsTracker.showSettingsAlert();
            }
    }
    public void compareLatLng(View view){
        double latitutde2 = Double.parseDouble(tvLatitude.getText().toString());
        double longitude2 = Double.parseDouble(tvLongitude.getText().toString());


        if(latitutde2 != 50.0068552 && longitude2 != 22.4651861) { // jesli dlugosc i szerokosc jest ta sama co znacznik
            Toast.makeText(MainActivity.this,"DZIALA",Toast.LENGTH_SHORT).show();
            playBackgroundSound(view);
            vibrateMessages(view);
        }
        else{
            //stopSound(view);
        }
    }

    public void playBackgroundSound(View view){
        Intent intent = new Intent(MainActivity.this,BackgroundSoundService.class);
        startService(intent);
    }
    public void stopSound(View view){
        backgroundSoundService.onDestroy();
    }
    public void vibrateMessages(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //vibrate to 1000 milisecond

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            v.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 30
            v.vibrate(1000);
        }
    }
}