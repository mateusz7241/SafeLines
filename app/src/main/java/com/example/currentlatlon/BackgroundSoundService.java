package com.example.currentlatlon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(100,100);
    }
    public int onStartCommand(Intent intent,int flags,int startId){
        mediaPlayer.start();
        Toast.makeText(getApplicationContext(), "Uwaga zbliżasz się do pasów", Toast.LENGTH_SHORT).show();
        return startId;
    }
    public void onStart(Intent intent, int startId){}
    public void onDestroy(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    public void onLowMemory(){
    }

}
