package com.vitechtoday.technologyfornepali;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.vitechtoday.technologyfornepali.model.AudioTrack;
import com.vitechtoday.technologyfornepali.model.FMMediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service {
    public  interface  OnTrackCompleteListener {
        void  onTrackComplete();
    }
    public  class  MusicBinder  extends Binder {
        public  MusicService getMusicService() {
            return  MusicService.this;
        }
    }
    private final IBinder musicBinder = new MusicBinder();
    private MediaPlayer mediaPlayer;
private ArrayList<AudioTrack> tracks;
private  int playbackId;
private  OnTrackCompleteListener onTrackCompleteListener;
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
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextTrack();
                    onTrackCompleteListener.onTrackComplete();
                }
            });
            mediaPlayer.prepare();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public  String getCurrentTrack(boolean isComplete) {

        return  tracks.get(playbackId).getArtist() + " " + tracks.get(playbackId).getTitle();
    }
    public  int getPlaybackId() {
        return  playbackId;
    }
    public  void  setPlaybackId(int playbackId) {
        this.playbackId = playbackId;
    }
    public  void  setTracks(ArrayList<AudioTrack> tracks) {
        this.tracks = tracks;
    }
    public  void  setOnTrackCompleteListener(OnTrackCompleteListener onTrackCompleteListener) {
        this.onTrackCompleteListener = onTrackCompleteListener;
    }
    public  boolean isPlaying() {
        if (mediaPlayer != null)
        return  mediaPlayer.isPlaying();
        return  false;
    }
    public  void  playAudio() {
        if ((mediaPlayer !=null)&&(!mediaPlayer.isPlaying()))
            mediaPlayer.start();
        else  mediaPlayer.pause();
    }
    public  void  shuffleTracks(boolean enabled) {
        ArrayList<AudioTrack> shuffledTracks = tracks;
        if (enabled) {
            Collections.shuffle(tracks);
        }
        else  {
            Collections.sort(tracks, new Comparator<AudioTrack>() {
                @Override
                public int compare(AudioTrack audioTrack, AudioTrack t1) {
                    //return (int) ((int) audioTrack.getId() - t1.getId());
                    return audioTrack.getDisplayName().compareTo(t1.getDisplayName());
                }
            });
        }
    }
    public  void  setPlaybackPosition(int position) {
mediaPlayer.seekTo(position);
    }
    public  int getCurrentPlaybackPosition(){


        return  mediaPlayer.getCurrentPosition();
    }
    public  String  nextTrack() {
        if (playbackId<= tracks.size()) {
            playbackId++;
            createAudioTrack(tracks.get(playbackId).getPath());
            playAudio();
        }
        return  tracks.get(playbackId).getArtist() + " " + tracks.get(playbackId).getTitle();

    }
    public  String  previousTrack() {
            if (playbackId<= tracks.size()) {
                playbackId--;
                createAudioTrack(tracks.get(playbackId).getPath());
                playAudio();
            }
            return  tracks.get(playbackId).getArtist() + " " + tracks.get(playbackId).getTitle();
        }
    public  String getCurrentPlaybackPositionAsFormatted(long millis) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return  hms;
    }
    public  int getDuration() {
        return  mediaPlayer.getDuration();
    }

}