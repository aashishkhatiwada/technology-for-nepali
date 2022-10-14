package com.vitechtoday.technologyfornepali;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.vitechtoday.technologyfornepali.model.FMMediaPlayer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service {
    public  class  MusicBinder  extends Binder {
        public  MusicService getMusicService() {
            return  MusicService.this;
        }
    }
    private final IBinder musicBinder = new MusicBinder();
    private MediaPlayer mediaPlayer;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  musicBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = FMMediaPlayer.getMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    public  void  createAudioTrack(String path) {
        try {


mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public  boolean isPlaying() {
        return  mediaPlayer.isPlaying();
    }
    public  void  playAudio() {
        if ((mediaPlayer !=null)&&(!mediaPlayer.isPlaying()))
            mediaPlayer.start();
        else  mediaPlayer.pause();
    }
    public  void  setPlaybackPosition(int position) {
mediaPlayer.seekTo(position);
    }
    public  int getCurrentPlaybackPosition(){


        return  mediaPlayer.getCurrentPosition();
    }
    public  String getCurrentPlaybackPositionAsFormatted() {
        long millis = getCurrentPlaybackPosition();
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return  hms;
    }
    public  int getDuration() {
        return  mediaPlayer.getDuration();
    }

}