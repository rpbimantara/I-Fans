package com.example.nerita_hendra.i_fans;

public class Live {
    String VideoName;
    String VideoDesc;
    String URL;
    String VideoId;

    public Live(String videoName, String videoDesc, String URL, String videoId) {
        VideoName = videoName;
        VideoDesc = videoDesc;
        this.URL = URL;
        VideoId = videoId;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoDesc() {
        return VideoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        VideoDesc = videoDesc;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }
}
