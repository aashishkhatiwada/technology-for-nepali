package com.vitechtoday.technologyfornepali;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class EventReceiver extends BroadcastReceiver implements OnStopCallback {

    String message;

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        if (intent.getAction().equals(FMService.ACTION_FM_PLAYED)) {

 try {
     Toast.makeText(context.getApplicationContext(), "playing selected fm", Toast.LENGTH_LONG).show();
FMFragment.getProgressDialog().dismiss();
    message = "dismished";
//    onFmPlaybackCallback.onCancel(message);

}
 catch (Exception e) {
    e.printStackTrace();
 }

}
else  if (intent.getAction().equals(AudioAndFmActivity.ACTION_STOP_MUSIC)) {
    if (MusicPlayerActivity.getMusicService() != null) {
MusicPlayerActivity.getMusicService().stopAudio();
MusicPlayerActivity.getMusicService().stopSelf();
    }
        }
         else if (intent.getAction().equals(AudioAndFmActivity.ACTION_STOP_FM)) {
            if (FMFragment.getFmService() != null) {
                FMFragment.getFmService().stopFM();
            }
        }

    }

    @Override
    public void onStop(FMFragment fragment) {
        Intent intent = new Intent(fragment.getContext(), FMService.class);
        Objects.requireNonNull(fragment.getActivity()).stopService(intent);
        Log.d(getClass().getSimpleName(), "fm has stopped");

    }


}