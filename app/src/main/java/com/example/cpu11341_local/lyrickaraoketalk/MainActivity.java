package com.example.cpu11341_local.lyrickaraoketalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cpu11341_local.lyrickaraoketalk.myview.DonutProgress;
import com.example.cpu11341_local.lyrickaraoketalk.myview.LyricView;
import com.example.cpu11341_local.lyrickaraoketalk.utils.LyricUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private LyricView lyricView;
    ImageView karaBtn;
    MediaPlayer mp;
    CountDownTimer countDownTimer;
    DonutProgress donutProgress;
    TextView lyricTimer;

    void init(){
        lyricView = (LyricView) findViewById(R.id.lyricView);
        karaBtn = (ImageView) findViewById(R.id.karaBtn);
        lyricTimer = (TextView) findViewById(R.id.lyricTimer);
        donutProgress = (DonutProgress) findViewById(R.id.donutProgress);
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
                    onMusicPlaying(lyricID, mp3ID, getApplicationContext());
                }catch (Resources.NotFoundException e){

                }

            }
        }
    }

    private void onMusicPlaying(int lyricID, int mp3ID, Context context){
        lyricView.setLyric(LyricUtils.parseLyric(
                getResources().openRawResource(lyricID), "UTF-8"));
        lyricView.setLyricIndex(0);
        lyricView.setLooping(true);
        lyricView.play();

        lyricTimer.setVisibility(View.VISIBLE);

        mp = MediaPlayer.create(getApplicationContext(), mp3ID);// the song is a filename which i have pasted inside a folder **raw** created under the **res** folder.//
        mp.setLooping(true);
        lyricView.setLyricLength(mp.getDuration());
        mp.start();

        countDownTimer = new CountDownTimer(mp.getDuration(), 1000) {

            public void onTick(long millisUntilFinished) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                lyricTimer.setText(simpleDateFormat.format(millisUntilFinished));
            }

            public void onFinish() {
                lyricTimer.setText("00:00");
            }
        }.start();

        donutProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (donutProgress.getBackground().getConstantState() == getResources().getDrawable(R.drawable.play).getConstantState()){
                    lyricView.play();
                    mp.start();
                    donutProgress.start();
                    donutProgress.setBackgroundResource(R.drawable.stop);
                    countDownTimer.start();
                } else {
                    lyricView.stop();
                    mp.seekTo(0);
                    mp.pause();
                    donutProgress.stop();
                    donutProgress.setBackgroundResource(R.drawable.play);
                    countDownTimer.cancel();
                }
            }
        });

        donutProgress.setVisibility(View.VISIBLE);
        donutProgress.setDuration(mp.getDuration());
        donutProgress.setLooping(true);
        donutProgress.start();
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
