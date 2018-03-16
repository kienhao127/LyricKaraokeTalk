package com.example.cpu11341_local.lyrickaraoketalk.model;

/**
 * Created by CPU11341-local on 29-Dec-17.
 */

public class Song {
    int id;
    String name;
    String artist;
    String mp3Path;
    String lyricPath;

    public Song(int id, String name, String artist, String mp3Path, String lyricPath) {
        this.name = name;
        this.artist = artist;
        this.mp3Path = mp3Path;
        this.lyricPath = lyricPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    public String getLyricPath() {
        return lyricPath;
    }

    public void setLyricPath(String lyricPath) {
        this.lyricPath = lyricPath;
    }
}
