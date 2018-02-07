package com.example.cpu11341_local.lyrickaraoketalk.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.cpu11341_local.lyrickaraoketalk.R;
import com.example.cpu11341_local.lyrickaraoketalk.utils.LyricUtils;
import com.example.cpu11341_local.lyrickaraoketalk.model.Lyric;
import com.example.cpu11341_local.lyrickaraoketalk.model.Sentence;

import java.util.ArrayList;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class LyricView extends AppCompatTextView implements Runnable {
    Lyric lyric;

    private static final int DY = 50;
    private int scrollDistance = 50;

    private Paint mCurrentPaint;
    private Paint mPaint;
    private int mLyricIndex = 0;
    private int mLyricSentenceLength;
    private float mMiddleX;
    private float mMiddleY;
    private int mHeight;
    private int iLoop = 0;
    private long delay;
    private boolean isLooping = false;

    private float afterPos;

    private int highlightColor = Color.YELLOW;
    private int normalColor = Color.WHITE;
    private float lyricTextSize = 36;
    public void setLyricLength(long time){
        lyric.setLength(time);
    }


    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LyricView, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        // Non-highlight part
        mPaint = new Paint();
        mPaint.setTextSize(lyricTextSize);
        mPaint.setColor(normalColor);
        mPaint.setTypeface(Typeface.SERIF);

        // highlight part, current lyric
        mCurrentPaint = new Paint();
        mCurrentPaint.setColor(highlightColor);
        mCurrentPaint.setTextSize(lyricTextSize);
        mCurrentPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));

        mPaint.setTextAlign(Paint.Align.CENTER);
        mCurrentPaint.setTextAlign(Paint.Align.CENTER);

        setMinHeight(100);
    }

    protected void initByAttributes(TypedArray attributes) {
        highlightColor = attributes.getColor(R.styleable.LyricView_highlightColor, Color.YELLOW);
        normalColor = attributes.getColor(R.styleable.LyricView_normalColor, Color.WHITE);

        lyricTextSize = attributes.getDimension(R.styleable.LyricView_lyricTextSize, 36);
    }

    private int drawText(Canvas canvas, Paint paint, String text, float startY) {
        int line = 0;
        float textWidth = mCurrentPaint.measureText(text);
        final int width = getWidth() - 85;

        while (textWidth != 0){
            int length = text.length();
            int startIndex = 0;
            int endIndex;
            if (textWidth < width){
                endIndex = length;
            } else {
                endIndex = text.lastIndexOf(" ", (int) ((float) length * (width / textWidth)));
            }

            String newText = (text.substring(startIndex, endIndex));

            text = text.substring(endIndex);
            textWidth = mCurrentPaint.measureText(text);

            ++line;
            canvas.drawText(newText, mMiddleX, startY + (line - 1) * DY, paint);
        }
        return line;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int totalLine = 0;

        if (iLoop < scrollDistance && System.currentTimeMillis() > delay && !mStop) {
            iLoop++;
        }
        if (lyric == null) {
            return;
        }

        ArrayList<Sentence> arrSentences = lyric.getArrSentences();
        if (arrSentences == null || arrSentences.isEmpty() || mLyricIndex == -2) {
            return;
        }

        // Draw sentences before current one
        int i = mLyricIndex - 1;
        if (i >= 0) {
            int line = drawText(canvas, mCurrentPaint, arrSentences.get(i).getContent(), mMiddleY - iLoop);
            scrollDistance = line * DY;
        }

        if (mLyricIndex > -1) {
            // Current line with highlighted color
//            mMovingPaint.setTextSize(movingLyricTextSize);
            int line = drawText(canvas, mCurrentPaint, arrSentences.get(mLyricIndex).getContent(), mMiddleY + scrollDistance - iLoop);
            afterPos = mMiddleY + DY * line;
            totalLine += line;
        }

        // Draw sentences afterwards
        i = mLyricIndex + 1;
        if (i > 0 && i < mLyricSentenceLength && iLoop == scrollDistance) {
            int line = drawText(canvas, mPaint, arrSentences.get(i).getContent(), afterPos);
            totalLine += line;
            int newHeight = totalLine*DY;
            if (getHeight() > newHeight){
                if (iLoop == scrollDistance){
                    setHeight(newHeight);
                }
            } else {
                setHeight(newHeight);
            }
        }


        invalidate();
    }

    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mMiddleX = w * 0.5f; // remember the center of the screen
        mHeight = h;
        mMiddleY = 40;
    }

    public long updateIndex(long time) {
        // Current index is last sentence
        if (mLyricIndex >= mLyricSentenceLength - 1) {
            mLyricIndex = mLyricSentenceLength - 1;
            return -1;
        }

        // Get index of sentence whose timestamp is between its startTime and currentTime.
        mLyricIndex = LyricUtils.getSentenceIndex(lyric, time, mLyricIndex);

        // New current index is last sentence
        if (mLyricIndex >= mLyricSentenceLength - 1) {
            mLyricIndex = mLyricSentenceLength - 1;
            return -1;
        }

        return lyric.getArrSentences().get(mLyricIndex + 1).getFromTime();
    }

    public synchronized void setLyric(Lyric lyric, boolean resetIndex) {
        this.lyric = lyric;
        mLyricSentenceLength = this.lyric.getArrSentences().size();
        if (resetIndex) {
            mLyricIndex = 0;
        }
    }

    public synchronized void setLyric(Lyric lyric) {
        setLyric(lyric, true);
    }

    public void setLyricIndex(int index) {
        mLyricIndex = index;
    }

    public String getCurrentSentence() {
        if (mLyricIndex >= 0 && mLyricIndex < mLyricSentenceLength) {
            return lyric.getArrSentences().get(mLyricIndex).getContent();
        }
        return null;
    }

    public void play() {
        mStop = false;
        mStartTime = -1;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        mStop = true;
        mLyricIndex = 0;
        mStartTime = -2;
        mNextSentenceTime = -1;
    }

    public void repeat(){
        mLyricIndex = 0;
        mStartTime = System.currentTimeMillis();
    }

    private long mStartTime = -1;
    private boolean mStop = true;
    private long mNextSentenceTime = -1;

    @Override
    public void run() {
        if (mStartTime == -1) {
            mStartTime = System.currentTimeMillis();
        }
        while (mLyricIndex != -2) {
            if (mStop) {
                return;
            }

            long ts = System.currentTimeMillis() - mStartTime;
            if (ts >= mNextSentenceTime) {
                delay = System.currentTimeMillis() + 200;
                iLoop = 0;
                mNextSentenceTime = updateIndex(ts);
            }
            if (mNextSentenceTime == -1 && ts > lyric.getLength()) {
                if (isLooping){
                    repeat();
                }else {
                    mStop = true;
                }
            }
        }
    }

    float dX, dY;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //getX getY your coordinate
                dX = this.getX() - event.getRawX();
                dY = this.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();

                break;
            default:
                return false;
        }
        return true;
    }
}
