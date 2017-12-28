package com.example.cpu11341_local.lyrickaraoketalk.model;


import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class Lyric {
    String title;
    String artist;
    long length;
    ArrayList<Sentence> arrSentences = new ArrayList<>();

    public void addSentence(String content, long time) {
        arrSentences.add(new Sentence(content, time));
    }

    public Lyric() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public ArrayList<Sentence> getArrSentences() {
        return arrSentences;
    }

    public void setArrSentences(ArrayList<Sentence> arrSentences) {
        this.arrSentences = arrSentences;
    }

    public static class SentenceComparator implements Comparator<Sentence> {
        @Override
        public int compare(Sentence sent1, Sentence sent2) {
            return (int) (sent1.fromTime - sent2.fromTime);
        }
    }
}
