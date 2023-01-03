package com.example.currentlatlon;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start();
    }
    public int onStartCommand(Intent intent,int flags,int startId){
        return START_STICKY;
    }
    public void onStart(Intent intent, int startId){}
    public void onDestroy(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    public void onLowMemory(){
    }

}
