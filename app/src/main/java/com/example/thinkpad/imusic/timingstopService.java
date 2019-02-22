package com.example.thinkpad.imusic;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

public class timingstopService extends Service {
    public timingstopService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        MusicPlayService.mediaPlayer.pause();
        MainActivity.state=0;
        MainActivity.play.setBackgroundResource(R.drawable.music_play);
        return super.onStartCommand(intent, flags, startId);
    }
}
