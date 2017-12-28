package com.example.cpu11341_local.lyrickaraoketalk.model;

/**
 * Created by CPU11341-local on 28-Dec-17.
 */

public class Sentence {
    String content;
    long fromTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public Sentence(String content, long fromTime) {
        this.content = content;
        this.fromTime = fromTime;
    }
}
