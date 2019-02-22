package com.example.thinkpad.imusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayService extends Service {
    private List<Song> mSongList=new ArrayList<>();
    public static  MediaPlayer mediaPlayer;
    public MusicServiceBinder musicServiceBinder=new MusicServiceBinder();
    private MainActivity activity;
    private int SongPointer=-1;
    private int i=0;
    public  MusicPlayService(){

    }
    class MusicServiceBinder extends Binder {
        public void InitMedia(Song music,int pointer){
            // playMusic.setBackground(getResources().getDrawable(R.drawable.music_play));
            try {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }else{
                    mediaPlayer.reset();
                }
                SongPointer=pointer;
             Intent intent=new Intent("com.example.iMusic.UI_UPDATE");
                intent.putExtra("SongName",music.getName());
                sendBroadcast(intent);
                mediaPlayer.setDataSource(music.getPath());
                mediaPlayer.prepare();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void test1(){
            Log.d("good11","wwqqwsqws");
        }

        public void InitBinder(List<Song> mSongList1){
            Init(mSongList1);
        }
        public void start(){mediaPlayer.start();}
        public void pause(){
            mediaPlayer.pause();
        }
        public void stop(){
            mediaPlayer.stop();
        }
        public  void reset(){mediaPlayer.reset();}
        public  void setDataSource(String url){try{mediaPlayer.setDataSource(url);}catch (IOException e){e.printStackTrace();}}
        public void prepare(){
            try{
            mediaPlayer.prepare();}
            catch (IOException e){
                e.printStackTrace();
            }
        }
        public void last(){
            if(SongPointer>0)
                SongPointer--;
            InitMedia(mSongList.get(SongPointer),SongPointer);
            mediaPlayer.start();
        }
        public void next(){
            if(SongPointer<mSongList.size()-1){
                SongPointer++;
                InitMedia(mSongList.get(SongPointer),SongPointer);
                mediaPlayer.start();
            }
        }
        public void play(){
            if(SongPointer==-1){
                InitMedia(mSongList.get(0),SongPointer);
            SongPointer=0;}
            try{
            mediaPlayer.start();}catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void test(){
        i=123;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("xxx","123");
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(SongPointer<mSongList.size()-1){
                    SongPointer++;
                    musicServiceBinder.InitMedia(mSongList.get(SongPointer),SongPointer);
                    mediaPlayer.start();
                    Log.d("Completion","Song"+SongPointer+" "+mSongList.get(SongPointer).getName() );
                }
            }
        });
    }
    public  MusicPlayService(MainActivity activity){
        this.activity=activity;
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    public void Init(List<Song> mSongList1){
        mSongList=mSongList1;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

}

