package com.example.thinkpad.imusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thinkpad on 2018/12/17.
 */

public class songAdpater extends RecyclerView.Adapter<songAdpater.ViewHolder> {
    private List<Song> mSongList;
    private MainActivity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView songAuthor;
        LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            songName = (TextView) v.findViewById(R.id.song_name);
            songAuthor = (TextView) v.findViewById(R.id.song_author);
            linearLayout = (LinearLayout) v.findViewById(R.id.layout);
        }
    }

    public songAdpater(List<Song> mSongList, MainActivity activity) {
        this.mSongList = mSongList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Song song = mSongList.get(position);
                activity.play.setBackgroundResource(R.drawable.music_pause);
                activity.state = 1;
                activity.musicServiceBinder.InitMedia(song, position);
                activity.musicServiceBinder.start();
            }
        });

        return holder;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = mSongList.get(position);
        if (song.getAuthor() != null)
            holder.songAuthor.setText(song.getAuthor());
        holder.songName.setText(song.getName());

    }


    @Override
    public int getItemCount() {
        return mSongList.size();
    }




}
