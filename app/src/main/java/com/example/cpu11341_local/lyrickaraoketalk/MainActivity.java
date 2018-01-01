package com.example.cpu11341_local.lyrickaraoketalk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cpu11341_local.lyrickaraoketalk.myview.LyricView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private LyricView lyricView;
    ImageView karaBtn;
    MediaPlayer mp;
    ImageView playingMusic;
    LinearLayout playMusicMenu;
    TextView turnOffMusic;
    TextView onOffLyric;

    void init(){
        lyricView = (LyricView) findViewById(R.id.lyricView);
        karaBtn = (ImageView) findViewById(R.id.karaBtn);
        playingMusic = (ImageView) findViewById(R.id.playing_music);
        playMusicMenu = (LinearLayout) findViewById(R.id.playMusicMenu);
        turnOffMusic = (TextView) findViewById(R.id.turnOffMusic);
        onOffLyric = (TextView) findViewById(R.id.onOffLyric);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();

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
                    onMusicPlaying(lyricID, mp3ID);
                }catch (Resources.NotFoundException e){

                }

            }
        }
    }

    private void onMusicPlaying(int lyricID, int mp3ID){
        lyricView.setLyric(LyricUtils.parseLyric(
                getResources().openRawResource(lyricID), "UTF-8"));
        lyricView.setLyricIndex(0);
        lyricView.play();

        mp = MediaPlayer.create(getApplicationContext(), mp3ID);// the song is a filename which i have pasted inside a folder **raw** created under the **res** folder.//
        mp.start();

        Animation playingMusicRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        playingMusic.setVisibility(View.VISIBLE);
        playingMusic.startAnimation(playingMusicRotate);
        playingMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playMusicMenu.getVisibility() == View.VISIBLE){

                } else {
                    Animation showMenu = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
                    playMusicMenu.setVisibility(View.VISIBLE);
                    playMusicMenu.startAnimation(showMenu);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new TimerTask() {
                                @Override
                                public void run() {
                                    playMusicMenu.setVisibility(View.GONE);
                                }
                            });
                        }
                    }, 5000);
                }
            }
        });

        onOffLyric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lyricView.getVisibility() == View.VISIBLE){
                    lyricView.setVisibility(View.GONE);
                    onOffLyric.setText("Bật lời");
                } else {
                    lyricView.setVisibility(View.VISIBLE);
                    onOffLyric.setText("Tắt lời");
                }
            }
        });

        turnOffMusic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mp.stop();
                playMusicMenu.setVisibility(View.GONE);
                playingMusic.clearAnimation();
                playingMusic.setVisibility(View.GONE);
                lyricView.stop();
                lyricView.setVisibility(View.GONE);
            }
        });
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
