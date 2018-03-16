package com.example.cpu11341_local.lyrickaraoketalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cpu11341_local.lyrickaraoketalk.adapter.SongListAdapter;
import com.example.cpu11341_local.lyrickaraoketalk.model.Song;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SongListActivity extends AppCompatActivity {

    RecyclerView songListRecyclerView;
    SongListAdapter songListAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Song> songs;
    EditText searchBox;
    TextView title;
    Toolbar toolbar;
    TextView mTitle;
    ArrayList<Song> results;

    void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        searchBox = (EditText) findViewById(R.id.searchBox);
        title = (TextView) findViewById(R.id.titleItem);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        init();

        this.setSupportActionBar(toolbar);
        mTitle.setText("Danh sách bài hát");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        songs = new ArrayList<>();
        songs.add(new Song(1, "Năm ấy", "Đức Phúc", "/mnt/sdcard/namay_ducphuc.mp3", "/mnt/sdcard/namay.lrc"));
        songs.add(new Song(2, "All Falls Down", "Alan Walker", "/mnt/sdcard/allfallsdown_alanwalker.mp3", "/mnt/sdcard/allfallsdown.lrc"));
        songs.add(new Song(3, "無條件", "陳奕迅", "/mnt/sdcard/vodieukien_trandichtan.mp3", "/mnt/sdcard/vodieukien.lrc"));

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
                intent.putExtra("mp3Path", songs.get(position).getMp3Path());
                intent.putExtra("lyricPath", songs.get(position).getLyricPath());
                startActivity(intent);
                finish();
            }
        });

        searchBox.clearFocus();
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                results = new ArrayList<>();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (searchBox.getText().toString().trim().length() > 0) {
                    title.setText("Kết quả tìm kiếm");
                } else {
                    title.setText("Bài hát đề cử");
                }

                for (Song s : songs) {
                    if (s.getName() != null && removeAccent(s.getName()).contains(removeAccent(searchBox.getText().toString().toLowerCase()))) {
                        results.add(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                songListAdapter.setSongs(results);
                songListAdapter.notifyDataSetChanged();
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase();
    }
}
