package com.example.thinkpad.imusic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class timing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
        ActionBar actionBar =getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        final EditText timingText=(EditText) findViewById(R.id.timingText);
        Button  finishButton=(Button)findViewById(R.id.timingFinishButton);
        ImageButton backImgButton=(ImageButton) findViewById(R.id.back);
        backImgButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time=Long.parseLong(timingText.getText().toString());
                long triggerAtTime = SystemClock.elapsedRealtime()+time*1000;
                AlarmManager manager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i=new Intent(timing.this,timingstopService.class);
                PendingIntent pi=PendingIntent.getService(timing.this,0,i,0);
                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
            }
        });
}
}
