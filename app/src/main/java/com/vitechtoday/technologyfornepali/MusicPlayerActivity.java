package com.vitechtoday.technologyfornepali;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.google.android.material.button.MaterialButton;
import com.vitechtoday.technologyfornepali.model.AudioTrack;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity implements  MusicService.OnTrackCompleteListener{
private AppCompatSeekBar rewind_fastForward;


    private TextView currently_playing_info_textView;
private MaterialButton play_pause_button;
private MaterialButton previous_button;
private  MaterialButton next_button;
private CheckBox shuffle_checkbox;
private  TextView elapsed_time_textView;
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
        musicService.setTracks(fileList);
        musicService.setPlaybackId(getIntent().getIntExtra(MusicFragment.ITEM_INDEX_CLICKED_KEY, 0));
        musicService.setOnTrackCompleteListener(MusicPlayerActivity.this);

        rewind_fastForward.setMax(musicService.getDuration());
        rewind_fastForward.setContentDescription("current playback position: "+musicService.getCurrentPlaybackPositionAsFormatted(musicService.getCurrentPlaybackPosition()));

        isBound =true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
isBound = false;
    }
};
private  boolean isBound;
private  String trackInfo;
    @Override
    public void onTrackComplete() {
trackInfo =   musicService.getCurrentTrack(true);
        currently_playing_info_textView.setText(getResources().getString(R.string.dummy_text_string) + " " + trackInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        rewind_fastForward = findViewById(R.id.rewind_fastForward);
        currently_playing_info_textView = findViewById(R.id.currently_played_music_info);
        elapsed_time_textView = findViewById(R.id.elapsed_time_textView);
        play_pause_button = findViewById(R.id.play_pause_button);
        next_button = findViewById(R.id.next_button);
        previous_button = findViewById(R.id.previous_button);
        shuffle_checkbox = findViewById(R.id.shuffle_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.getPlaybackId() == fileList.size())
                    return;
                trackInfo = musicService.nextTrack();
                currently_playing_info_textView.setText(getResources().getString(R.string.dummy_text_string) + " " + trackInfo);
            }
        });
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.getPlaybackId()== 0)
                    return;
                trackInfo = musicService.previousTrack();
                currently_playing_info_textView.setText(getResources().getString(R.string.dummy_text_string) + " " + trackInfo);

            }
        });
        shuffle_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    musicService.shuffleTracks(true);
                else musicService.shuffleTracks(false);
            }
        });
        Bundle injecter = getIntent().getBundleExtra(MusicFragment.INJECTER_KEY);
        fileList = (ArrayList<AudioTrack>) injecter.getSerializable(MusicFragment.EXTRA_FILELIST_KEY);
         trackInfo = getIntent().getStringExtra(MusicFragment.CURRENTLY_PLAYING_AUDIO_INFO_KEY);
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
        if (musicService!=null) {
            musicService.setPlaybackId(getIntent().getIntExtra(MusicFragment.ITEM_INDEX_CLICKED_KEY, 0));

        }
rewind_fastForward.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            musicService.setPlaybackPosition(i);
        }
        rewind_fastForward.setContentDescription("current playback position: "+musicService.getCurrentPlaybackPositionAsFormatted(musicService.getCurrentPlaybackPosition()));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
});
handler.post(new Runnable() {
    @Override
    public void run() {
        if (musicService!=null) {
            elapsed_time_textView.setText(getString(R.string.elapsed_time) + musicService.getCurrentPlaybackPositionAsFormatted(musicService.getCurrentPlaybackPosition()) + "/" + musicService.getCurrentPlaybackPositionAsFormatted(musicService.getDuration()));
            rewind_fastForward.setProgress(musicService.getCurrentPlaybackPosition());

        }
        handler.postDelayed(this, 1000);
    }
});
    }
}