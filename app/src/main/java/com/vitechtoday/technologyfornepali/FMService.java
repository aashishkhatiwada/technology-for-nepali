package com.vitechtoday.technologyfornepali;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.vitechtoday.technologyfornepali.model.FMMediaPlayer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public  class FMService extends Service implements AudioManager.OnAudioFocusChangeListener, Runnable  {

    public  static  final  String ACTION_FM_PLAYED = "com.vitechtoday.technologyfornepali.fm_played";
    Intent i;

    public class FmHandler extends Binder {
        public FMService getFmService() {
            return FMService.this;
        }
    }
private  boolean isPrepared;
    private Looper looper;
    private MediaPlayer mediaPlayer;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Handler handler;
    private AudioManager audioManager;
    private Notification notification;
    private final IBinder fmHandler = new FmHandler();
    private static  boolean running;
    public void initialize() {
        mediaPlayer = FMMediaPlayer.getMediaPlayer();
    }

    public static boolean isRunning() {
        return running;
    }

    public void playFM(String url) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = null;
                looper = Looper.myLooper();
                handler = new Handler(looper);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mediaPlayer.reset();

                            mediaPlayer.setDataSource(url);
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();
isPrepared = true;
if (isPrepared) {
    i = new Intent(getApplicationContext(), EventReceiver.class);
    i.setAction(ACTION_FM_PLAYED);
    sendBroadcast(i);
}

    }
});


                            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED).setLegacyStreamType(AudioManager.STREAM_MUSIC).build());
                            mediaPlayer.prepareAsync();
                        } catch (IOException|  IllegalStateException exception) {
                            exception.printStackTrace();
                            playFM(url);
                        }

                    }
                });
                Looper.loop();
            }
        });
        if (mediaPlayer.isPlaying())
            mediaPlayer.start();
        else mediaPlayer.reset();
    }


    public String getUrl() {
        return "";
    }

    public void setVolume(float left, float right) {
        mediaPlayer.setVolume(left, right);
    }

    public void startFm() {
        mediaPlayer.start();
    }

    public void stopFM() {


            if (isPrepared) {
                mediaPlayer.stop();


            }

    }

    public boolean isPlaying() {
        if (isPrepared)
            return  mediaPlayer.isPlaying();
        return  false;
    }


    public void run() {
            initialize();
    }


    @Override
    public void onCreate() {

        registerReceiver(new EventReceiver(), new IntentFilter(ACTION_FM_PLAYED));

        handler = new Handler();
initialize();
        super.onCreate();
        showNotification("FM service has started");
        startForeground(1, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        if (!requestAudioFocus()) {
            stopSelf();
        }


        showNotification("FM service is now running");

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return fmHandler;
    }

    @Override
    public void onDestroy() {
        Log.d("FMService", "onDestroy called, releasing resources");

        handler.removeCallbacks(this);
            mediaPlayer.reset();
            //executorService.shutdown();
        running = false;
        super.onDestroy();

    }


    public void showNotification(String message) {

        Intent notificationIntent = new Intent(this, AudioAndFmActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra(AudioAndFmActivity.LAUNCHED_FROM_NOTIFICATION_KEY, true);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                54321,


                notificationIntent, PendingIntent.FLAG_IMMUTABLE); // when the user presses the notification, he/she directly move into the FMFragment

        notification = new NotificationCompat.Builder(this, FMNotification.CHANNEL_ID)
                .setContentTitle("TechnologyForNepali")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(notificationPendingIntent)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)

                .setGroup("scilent group")
                .setGroupSummary(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSound(null)
                .build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }


    @Override
    public void onAudioFocusChange(int focusState) {
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN: {
                if (!isPlaying())
                    startFm();
                setVolume(1.0f, 1.0f);
            }
            break;
            case AudioManager.AUDIOFOCUS_LOSS: {
                stopFM();
            }
            break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                if (isPlaying())
                    stopFM();
            }
            break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                if (isPlaying())
                    setVolume(0.1f, 0.1f);
            }
            break;
        }

    }


    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

    }


}