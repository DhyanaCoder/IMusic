package com.example.thinkpad.imusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;


import java.util.ArrayList;

/**
 * Created by thinkpad on 2018/9/8.
 */

public class ScanMusicUtil {
    public static ArrayList<Song> scanMusic(Context context){
       ArrayList<Song> musicList= new ArrayList<Song>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if(cursor!=null){
            while(cursor.moveToNext()){
                Song music=new Song();
                music.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
               // music.setCoverId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                musicList.add(music);
                Log.d("testest",music.getName()+"   "+music.getPath());
            }
        }
        cursor.close();
        return musicList;
    }
}
