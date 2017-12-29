package com.example.cpu11341_local.lyrickaraoketalk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.cpu11341_local.lyrickaraoketalk.myview.LyricView;

import java.io.IOException;

public class MainActivity extends Activity {
    private LyricView mLyricView;
    Button karaBtn;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        karaBtn = (Button) findViewById(R.id.karaBtn);
        karaBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SongListActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        Integer mp3ID = null, lyricID = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mp3ID = bundle.getInt("mp3ID");
            lyricID = bundle.getInt("lyricID");

            if (mp3ID != null && lyricID != null) {
                try {
                    mLyricView = (LyricView) findViewById(R.id.lyricView);
                    mLyricView.setLyric(LyricUtils.parseLyric(
                            getResources().openRawResource(lyricID), "UTF-8"));
                    mLyricView.setLyricIndex(0);
                    mLyricView.play();

                    mp = MediaPlayer.create(getApplicationContext(), mp3ID);// the song is a filename which i have pasted inside a folder **raw** created under the **res** folder.//
                    mp.start();
                }catch (Resources.NotFoundException e){

                }

            }
        }
    }

    @Override
    public void onDestroy() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        super.onDestroy();

    }
}
