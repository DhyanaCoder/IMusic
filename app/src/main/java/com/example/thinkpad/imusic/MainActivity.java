package com.example.thinkpad.imusic;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //public MediaPlayer mediaPlayer=new MediaPlayer();
    private DrawerLayout mDrawerLayout;
    private    songAdpater recyclerview_adapter;
    private List<Song> mSongList=new ArrayList<>();
    public static ImageButton play;
    public ImageButton last;
    public ImageButton next;
    public static  int state=0;
    public int SongPointer=-1;
    public TextView showSongName;
    public MusicPlayService.MusicServiceBinder musicServiceBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicServiceBinder=(MusicPlayService.MusicServiceBinder)service;
            musicServiceBinder.InitBinder(mSongList);
            musicServiceBinder.test1();
            Log.d("hhhhhh","wqwqwdqwd");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //以下代码隐藏标题栏
        ActionBar actionBar =getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        final Intent intent=new Intent(this,MusicPlayService.class);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.iMusic.UI_UPDATE");
        MyBroadcast my=new MyBroadcast();
        registerReceiver(my,intentFilter);

        if( bindService(intent,connection,BIND_AUTO_CREATE)){
          Log.d("bindservice","success");
      }else{
          Log.d("bindservice","failed");
      }
        startService(intent);
if(musicServiceBinder==null)
    Log.d("testest!","mbinder is  null");


        showSongName=(TextView)findViewById(R.id.show_songname);

        play=(ImageButton) findViewById(R.id.music_play);
        last=(ImageButton) findViewById(R.id.music_left);
        next=(ImageButton) findViewById(R.id.music_right);
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              musicServiceBinder.last();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               musicServiceBinder.next();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(state==1){
                  musicServiceBinder.pause();
                  play.setBackgroundResource(R.drawable.music_play);
                  state=0;
              }else{

                  play.setBackgroundResource(R.drawable.music_pause);
                musicServiceBinder.play();
                  state=1;
              }
            }
        });

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.setting1);
        navigationView.setCheckedItem(R.id.timing);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.setting1:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.timing:
                        Intent timingIntent=new Intent(MainActivity.this,timing.class);
                        startActivity(timingIntent);
                }

                return true;
            }
        });
        ImageButton imageButton=(ImageButton) findViewById(R.id.setting);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        List<String> permissionList=new ArrayList<>();//构建权限申请表，以下就是逐步申请权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            mSongList=ScanMusicUtil.scanMusic(this);


//            recyclerview_adapter.notifyDataSetChanged();
        }
        if(!permissionList.isEmpty() ){
            String [] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
        recyclerview_adapter=new songAdpater(mSongList,this);
        RecyclerView recyclerView_song=(RecyclerView)findViewById(R.id.recyclerview_song);
        recyclerView_song.setAdapter(recyclerview_adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_song.setLayoutManager(layoutManager);
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    mSongList=ScanMusicUtil.scanMusic(this);
                    recyclerview_adapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
          showSongName.setText(mSongList.get(msg.what).getName());
        }
    };
   private   class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            showSongName.setText( intent.getStringExtra("SongName"));
        }
    }

    private void Init_song(){
        for(int i=0;i<10;i++) {
            Song one = new Song();
            one.setName("秋樱");
            one.setAuthor("山口百惠");
            mSongList.add(one);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       Intent i=new Intent(this,MusicPlayService.class);
        stopService(i);
    }


}
