package com.vitechtoday.technologyfornepali;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class FMNotification extends Application {

    public static final String CHANNEL_ID = "serviceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

    }

    private void createNotificationChannel() {
/*
We have to create a notification channel for android oreo above
 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Nepali Time Announcer", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }

    }
}
