package com.example.cpu11341_local.lyrickaraoketalk;

import android.text.TextUtils;
import android.util.Log;

import com.example.cpu11341_local.lyrickaraoketalk.model.Lyric;
import com.example.cpu11341_local.lyrickaraoketalk.model.Sentence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class LyricUtils {

    public static Lyric parseLyric(InputStream inputStream, String Encoding) {
        Lyric lyric = new Lyric();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Encoding));
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line, lyric);
            }
            Collections.sort(lyric.getArrSentences(), new Lyric.SentenceComparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyric;
    }

    public static Lyric parseLyric(File file, String Encoding) {
        Lyric lyric = new Lyric();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Encoding));
            Log.i(TAG, String.format("parseLyric(%s, %s)", file.getPath(), Encoding));
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line, lyric);
            }
            Collections.sort(lyric.getArrSentences(), new Lyric.SentenceComparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyric;
    }

    public static boolean parseLine(String line, Lyric lyric){
        int lineLength = line.length();
        line = line.trim();
        int openBracketIndex, closedBracketIndex;
        openBracketIndex = line.indexOf('[', 0);

        while (openBracketIndex != -1) {
            closedBracketIndex = line.indexOf(']', openBracketIndex);
            if (closedBracketIndex < 1)
                return false;

            String strTime = line.substring(openBracketIndex + 1, closedBracketIndex);

            ArrayList<Long> timestampList = new ArrayList<>();
            long time = parseTime(strTime);
            if (time != -1) {
                timestampList.add(time);
            }

            // We may have line like [01:38.33][01:44.01][03:22.05]Test Test
            while ((lineLength > closedBracketIndex + 2)
                    && (line.charAt(closedBracketIndex + 1) == '[')) {

                int nextOpenBracketIndex = closedBracketIndex + 1;
                int nextClosedBracketIndex = line.indexOf(']', nextOpenBracketIndex + 1);
                time = parseTime(line.substring(nextOpenBracketIndex + 1, nextClosedBracketIndex));
                if (time != -1) {
                    timestampList.add(time);
                }
                closedBracketIndex = nextClosedBracketIndex;
            }

            String content = line.substring(closedBracketIndex + 1, line.length());
            for (long timestamp : timestampList) {
                lyric.addSentence(content, timestamp);
            }
            openBracketIndex = line.indexOf('[', closedBracketIndex + 1);
        }
        return true;
    }

    public static long parseTime(String time){
        String[] ss = time.split("\\:|\\.");
        if (ss.length < 2) {
            return -1;
        }

        if (ss.length == 2){
            int min = Integer.parseInt(ss[0]);
            int sec = Integer.parseInt(ss[1]);
            if (min < 0 || sec < 0 || sec >= 60) {
                throw new RuntimeException("Chữ số không hợp lệ!");
            }
            // System.out.println("time" + (min * 60 + sec) * 1000L);
            return (min * 60 + sec) * 1000L;
        }

        if (ss.length == 3){
            int min = Integer.parseInt(ss[0]);
            int sec = Integer.parseInt(ss[1]);
            int mm = Integer.parseInt(ss[2]);
            if (min < 0 || sec < 0 || sec >= 60 || mm < 0 || mm > 99) {
                throw new RuntimeException("Chữ số không hợp lệ!");
            }
            // System.out.println("time" + (min * 60 + sec) * 1000L + mm * 10);
            return (min * 60 + sec) * 1000L + mm * 10;
        }

        return -1;
    }

    public static int getSentenceIndex(Lyric lyric, long ts, int index) {
        if (lyric == null || ts < 0 || index < -1)
            return -1;
        ArrayList<Sentence> list = lyric.getArrSentences();

        if (index >= list.size())
            index = list.size() - 1;
        if (index == -1)
            index = 0;

        int found = -2;

        if (list.get(index).getFromTime() > ts) {
            for (int i = index; i > -1; --i) {
                if (list.get(i).getFromTime() <= ts) {
                    found = i;
                    break;
                }
            }
            // First line of lyric is bigger than starting time.
            if (found == -2)
                found = -1;
        } else {
            for (int i = index; i < list.size() - 1; ++i) {
                //Log.d(TAG, String.format("ts: %d, offset: %d, curr_ts: %d, next_ts: %d", ts, offset, list.get(i).getFromTime(), list.get(i + 1).getFromTime()));
                if (list.get(i + 1).getFromTime() > ts) {
                    found = i;
                    break;
                }
            }
            // If not found, return last mLyricIndex
            if (found == -2) {
                found = list.size() - 1;
            }
        }

        return found;
    }
}
