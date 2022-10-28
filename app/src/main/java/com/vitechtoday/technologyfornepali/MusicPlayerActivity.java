package com.vitechtoday.technologyfornepali;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.google.android.material.button.MaterialButton;
import com.vitechtoday.technologyfornepali.model.AudioTrack;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {
private AppCompatSeekBar rewind_fastForward;
private TextView currently_playing_info_textView;
private MaterialButton play_pause_button;
private MaterialButton previous_button;
private  MaterialButton next_button;
private  MaterialButton shuffle_button;
private  MusicService musicService;
private  MusicService.MusicBinder musicBinder;
private ArrayList<AudioTrack> fileList;
private  Intent intent;
public  static  final String FILE_PATH_KEY = "filePath";
private Handler handler;
private  final ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        musicBinder = (MusicService.MusicBinder)iBinder;
        musicService = musicBinder.getMusicService();
        musicService.createAudioTrack(getIntent().getStringExtra(MusicFragment.PATH_KEY));
        musicService.playAudio();
        rewind_fastForward.setMax(musicService.getDuration());
        rewind_fastForward.setContentDescription("current playback position: "+musicService.getCurrentPlaybackPositionAsFormatted());

        isBound =true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
isBound = false;
    }
};
private  boolean isBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        rewind_fastForward = findViewById(R.id.rewind_fastForward);
        currently_playing_info_textView = findViewById(R.id.currently_played_music_info);
        play_pause_button = findViewById(R.id.play_pause_button);
        Bundle injecter = getIntent().getBundleExtra(MusicFragment.INJECTER_KEY);
        fileList = (ArrayList<AudioTrack>) injecter.getSerializable(MusicFragment.EXTRA_FILELIST_KEY);
        String trackInfo = getIntent().getStringExtra(MusicFragment.CURRENTLY_PLAYING_AUDIO_INFO_KEY);
        handler = new Handler();
        currently_playing_info_textView.setText(trackInfo);
        play_pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.playAudio();
            }
        });
        intent = new Intent(MusicPlayerActivity.this, MusicService.class);
startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
rewind_fastForward.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            musicService.setPlaybackPosition(i);
        }
        rewind_fastForward.setContentDescription("current playback position: "+musicService.getCurrentPlaybackPositionAsFormatted());

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
});

    }
}