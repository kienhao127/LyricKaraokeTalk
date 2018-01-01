package com.example.cpu11341_local.lyrickaraoketalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cpu11341_local.lyrickaraoketalk.adapter.SongListAdapter;
import com.example.cpu11341_local.lyrickaraoketalk.model.Song;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    RecyclerView songListRecyclerView;
    SongListAdapter songListAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Song> songs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        songs = new ArrayList<>();
        songs.add(new Song("Năm ấy", "Đức Phúc", R.raw.namay_ducphuc, R.raw.namay));
        songs.add(new Song("All Falls Down", "Alan Walker", R.raw.allfallsdown_alanwalker, R.raw.allfallsdown));
        songs.add(new Song("無條件", "陳奕迅", R.raw.vodieukien_trandichtan, R.raw.vodieukien));

        songListRecyclerView = (RecyclerView) findViewById(R.id.songList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        songListRecyclerView.setLayoutManager(layoutManager);
        songListAdapter = new SongListAdapter(songs);
        songListRecyclerView.setAdapter(songListAdapter);

        songListAdapter.SetOnItemClickListener(new SongListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mp3ID", songs.get(position - 1).getMp3ID());
                intent.putExtra("lyricID", songs.get(position - 1).getLyricID());
                startActivity(intent);
            }
        });
    }
}
