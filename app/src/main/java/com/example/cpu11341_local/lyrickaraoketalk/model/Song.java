package com.example.cpu11341_local.lyrickaraoketalk.model;

/**
 * Created by CPU11341-local on 29-Dec-17.
 */

public class Song {
    String name;
    String artist;
    int mp3ID;
    int lyricID;

    public Song(String name, String artist, int mp3ID, int lyricID) {
        this.name = name;
        this.artist = artist;
        this.mp3ID = mp3ID;
        this.lyricID = lyricID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getMp3ID() {
        return mp3ID;
    }

    public void setMp3ID(int mp3ID) {
        this.mp3ID = mp3ID;
    }

    public int getLyricID() {
        return lyricID;
    }

    public void setLyricID(int lyricID) {
        this.lyricID = lyricID;
    }
}
