package com.example.cpu11341_local.lyrickaraoketalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
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

import java.io.File;
import java.io.IOException;
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
    String mp3Path;
    String lyricPath;

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

        mp3Path = "";
        lyricPath = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mp3Path = bundle.getString("mp3Path");
            lyricPath = bundle.getString("lyricPath");

            if (mp3Path != null && lyricPath != null) {
                try {
                    onMusicPlaying(lyricPath, mp3Path, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onMusicPlaying(String lyricPath, String mp3Path, Context context) throws IOException {
        lyricView.setLyric(LyricUtils.parseLyric(new File(lyricPath), "UTF-8"));
        lyricView.setLyricIndex(0);
        lyricView.setLooping(true);
        lyricView.play();

        lyricTimer.setVisibility(View.VISIBLE);

        mp = new MediaPlayer();
        mp.setDataSource(mp3Path);
        mp.setLooping(true);
        mp.prepare();
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
        if (mp3Path != null && lyricPath != null){
            File mp3File = new File(mp3Path);
            File lyricFile = new File(lyricPath);
            mp3File.delete();
            lyricFile.delete();
        }
        super.onDestroy();
    }
}
