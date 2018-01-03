package com.example.cpu11341_local.lyrickaraoketalk.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.cpu11341_local.lyrickaraoketalk.LyricUtils;
import com.example.cpu11341_local.lyrickaraoketalk.R;
import com.example.cpu11341_local.lyrickaraoketalk.model.Lyric;
import com.example.cpu11341_local.lyrickaraoketalk.model.Sentence;

import java.util.ArrayList;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class LyricView extends AppCompatTextView implements Runnable {
    Lyric lyric;

    private static final int DY = 50;

    private Paint mCurrentPaint;
    private Paint mPaint;
    private int mLyricIndex = 0;
    private int mLyricSentenceLength;
    private float mMiddleX;
    private float mMiddleY;
    private int mHeight;
    private int iLoop = 0;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        int highlightColor = Color.YELLOW;
        int normalColor = Color.GRAY;

        setMinHeight(110);

        // Non-highlight part
        mPaint = new Paint();
        mPaint.setTextSize(36);
        mPaint.setColor(normalColor);
        mPaint.setTypeface(Typeface.SERIF);

        // highlight part, current lyric
        mCurrentPaint = new Paint();
        mCurrentPaint.setColor(highlightColor);
        mCurrentPaint.setTextSize(36);
        mCurrentPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));

        mPaint.setTextAlign(Paint.Align.CENTER);
        mCurrentPaint.setTextAlign(Paint.Align.CENTER);

    }

    private int drawText(Canvas canvas, Paint paint, String text, float startY) {
        int line = 0;
        Log.d(String.valueOf(mLyricIndex), text);
        float textWidth = paint.measureText(text);
        final int width = getWidth() - 85;
        if (textWidth > width) {
            int length = text.length();
            int startIndex = 0;
            int endIndex = text.indexOf(" ", (int) (text.length()/3*2));
            if (endIndex == -1){
                endIndex = Math.min((int) ((float) length * (width / textWidth)), length - 1);
            }
            int perLineLength = endIndex - startIndex;

            ArrayList<String> lines = new ArrayList<>();
            lines.add(text.substring(startIndex, endIndex));
            while (endIndex < length - 1) {
                startIndex = endIndex;
                endIndex = Math.min(startIndex + perLineLength, length);
                lines.add(text.substring(startIndex, endIndex));
            }
            int linesLength = lines.size();
            for (String str : lines) {
                ++line;
                if (startY < mMiddleY)
                    canvas.drawText(str, mMiddleX, startY - (linesLength - line) * DY, paint);
                else
                    canvas.drawText(str, mMiddleX, startY + (line - 1) * DY, paint);
            }
        } else {
            ++line;
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, mMiddleX, startY, paint);
        }
        Log.d("Line", String.valueOf(line));
        return line;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (iLoop < DY) {
            iLoop++;
        }
        if (lyric == null) {
            return;
        }

        ArrayList<Sentence> arrSentences = lyric.getArrSentences();
        if (arrSentences == null || arrSentences.isEmpty() || mLyricIndex == -2) {
            return;
        }

        float currY;

        if (mLyricIndex > -1) {
            // Current line with highlighted color
            currY = mMiddleY + DY * drawText(
                    canvas, mCurrentPaint, arrSentences.get(mLyricIndex).getContent(), mMiddleY + DY - iLoop);
        } else {
            // First line is not from timestamp 0
            currY = mMiddleY + iLoop;
        }

        // Draw sentences afterwards
        int i = mLyricIndex + 1;
        if (i < mLyricSentenceLength) {
            currY += DY * drawText(canvas, mPaint, arrSentences.get(i).getContent(), currY + DY - iLoop);
        }

        currY = mMiddleY - DY;
        // Draw sentences before current one
        i = mLyricIndex - 1;
        if (i >= 0) {
            currY -= DY * drawText(canvas, mPaint, arrSentences.get(i).getContent(), currY + DY - iLoop);
        }
        invalidate();

//        int size = arrSentences.size();
//        for (int i = mLyricIndex + 1; i < size; i++) {
//            if (currY > mHeight) {
//                break;
//            }
//            // Draw and Move down
//            currY += DY * drawText(canvas, mPaint, arrSentences.get(i).getContent(), currY);
//        }

//        currY = mMiddleY - DY;
//
//        // Draw sentences before current one
//        for (int i = mLyricIndex - 1; i >= 0; i--) {
//            if (currY < 0) {
//                break;
//            }
//            // Draw and move upwards
//            currY -= DY * drawText(canvas, mPaint, arrSentences.get(i).getContent(), currY);
//            // canvas.translate(0, DY);
//        }
    }

    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mMiddleX = w * 0.5f; // remember the center of the screen
        mHeight = h;
        mMiddleY = h * 0.35f;
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
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        mStop = true;
    }

    private long mStartTime = -1;
    private boolean mStop = true;
    private boolean mIsForeground = true;
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
                iLoop = 0;
                mNextSentenceTime = updateIndex(ts);
            }
            if (mNextSentenceTime == -1) {
                mStop = true;
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
