package com.example.cpu11341_local.lyrickaraoketalk;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.cpu11341_local.lyrickaraoketalk.myview.LyricView;

import java.io.IOException;

public class MainActivity extends Activity {
    private LyricView mLyricView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        mLyricView = (LyricView) findViewById(R.id.lyricView);
        mLyricView.setLyric(LyricUtils.parseLyric(
                getResources().openRawResource(R.raw.namay), "UTF-8"));
        mLyricView.setLyricIndex(0);
        mLyricView.play();

        MediaPlayer mp = new MediaPlayer();
        mp=MediaPlayer.create(getApplicationContext(),R.raw.namay_ducphuc);// the song is a filename which i have pasted inside a folder **raw** created under the **res** folder.//
        mp.start();
    }
}
