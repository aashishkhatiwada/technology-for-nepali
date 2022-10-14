package com.vitechtoday.technologyfornepali.model;

import android.media.MediaPlayer;

public class FMMediaPlayer {

private static MediaPlayer mediaPlayer= null;

    private FMMediaPlayer() {
    }

    public static MediaPlayer getMediaPlayer() {

        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
            return mediaPlayer;

    }

}
