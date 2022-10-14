package com.vitechtoday.technologyfornepali.model;

import java.io.Serializable;

public class AudioTrack  implements Serializable {
    private  String displayName;
    private  long id;
    private  String artist;
    private  String title;
    private  String path;
    public  AudioTrack() {}
    public AudioTrack(String displayName, long id, String artist, String title, String path) {
        this.displayName = displayName;
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.path = path;
    }
    public  String getArtist() {
        return  artist;
    }
public String getPath() {
        return  path;
}
    public String getTitle() {
        return title;
    }

    public  String getDisplayName() {
        return  displayName;
    }
    public  long getId() {
        return  id;
    }
}
