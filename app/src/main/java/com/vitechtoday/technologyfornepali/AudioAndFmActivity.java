package com.vitechtoday.technologyfornepali;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.tabs.TabLayout;

public class AudioAndFmActivity extends AppCompatActivity  implements View.OnClickListener {
    public  static  final  String ACTION_STOP_FM = "com.vitechtoday.technologyfornepali.stopFm";

    public  static final  String LAUNCHED_FROM_NOTIFICATION_KEY = "launchedFromNotification";
private TabLayout tabLayout;
private Intent fmService;
private Button play_stop_button;
private  TabLayout.Tab music;
private TabLayout.Tab fm;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
          Log.d(getClass().getSimpleName(), "onNewIntent called, setting tab to \"fm\"");
        if (intent.getBooleanExtra(AudioAndFmActivity.LAUNCHED_FROM_NOTIFICATION_KEY, false))
        tabLayout.selectTab(fm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_and_fm);
        registerReceiver(new EventReceiver(), new IntentFilter(AudioAndFmActivity.ACTION_STOP_FM));

ActivityCompat.requestPermissions(this, new  String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        tabLayout = findViewById(R.id.music_and_fm_tabLayout);
        play_stop_button= findViewById(R.id.play_stop_button);
fmService= new Intent(this, FMService.class);
fmService.setAction("com.vitechtoday.technologyfornepali.FMService");
play_stop_button.setOnClickListener(this);
 music = tabLayout.newTab();
         fm = tabLayout.newTab();
        music.setIcon(R.drawable.ic_music);
        music.setText("music");
        fm.setIcon(R.drawable.ic_fm);
        fm.setText("fm");
        tabLayout.addTab(music, true);
        tabLayout.addTab(fm);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_frameLayout, new MusicFragment()).commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("music")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_frameLayout, MusicFragment.newInstance("", "")).commit();
toggleViewState(play_stop_button, false);
                    //play_stop_button.setVisibility(View.GONE);
                }
                else  if (tab.getText().equals("fm")) {
                    //play_stop_button.setVisibility(View.VISIBLE);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_frameLayout, FMFragment.newInstance("", "")).commit();
                        toggleViewState(play_stop_button, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_frameLayout, FMFragment.newInstance("", "")).commit();

            }
        });
        onNewIntent(getIntent());

    }

    @Override
    public void onClick(View view) {
    sendBroadcast(new Intent(AudioAndFmActivity.ACTION_STOP_FM));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ((FMFragment.getFmService().isRunning())&&(!FMFragment.getFmService().isPlaying())) {
            FMFragment.getFmService().stopFM();
            stopService(fmService);

        }
    }
    protected   void toggleViewState(View view, boolean visible) {
        if (visible)
            view.setVisibility(View.VISIBLE);
        else  view.setVisibility(View.INVISIBLE);
    }

    public static class MusicService extends Service {
        public MusicService() {
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
