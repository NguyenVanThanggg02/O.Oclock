package com.example.ooclock.service;

import static com.example.ooclock.application.App.CHANNEL_ID;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.MODE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SNOOZE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.URI;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VIBRATE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VOLUME;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.ooclock.MenuStopWatch;
import com.example.ooclock.R;
import com.example.ooclock.TurnOffAlarm;

import java.io.IOException;


public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    AudioManager mAudioManager;
    int originalVolume;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, TurnOffAlarm.class);

        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE));

        notificationIntent.putExtra(MODE, intent.getStringExtra(MODE));
        notificationIntent.putExtra(URI, intent.getStringExtra(URI));
        notificationIntent.putExtra(VOLUME, intent.getFloatExtra(VOLUME,1.0f));
        notificationIntent.putExtra(VIBRATE, intent.getBooleanExtra(VIBRATE,false));
        notificationIntent.putExtra(SNOOZE, intent.getBooleanExtra(SNOOZE,false));
        notificationIntent.putExtra(TITLE, intent.getStringExtra(TITLE));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Ring Ring .. Ring Ring")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        Intent intentService = new Intent(getApplicationContext(), AlarmNotificationService.class);
        getApplicationContext().stopService(intentService);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int volume = (int) (mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * intent.getFloatExtra(VOLUME, 1.0f));
        Log.d("An_Test","re "+volume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

        mediaPlayer = MediaPlayer.create(this, R.raw.nhac_chuong);
        if (intent.getStringExtra(URI) != null && !intent.getStringExtra(URI).trim().equals("")) {
            Uri alarmUri = Uri.parse(intent.getStringExtra(URI));
            Log.d("An_Test", "re " + alarmUri.toString());
            getApplicationContext().getContentResolver().takePersistableUriPermission(alarmUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getApplicationContext(), alarmUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } catch (IOException e) {
                Log.d("An_Test", "e " + e);
                e.printStackTrace();
            }
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }

        if (intent.getBooleanExtra(VIBRATE, false)) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, 0);
        }

        startForeground(1, notification);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(notificationIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        if (vibrator != null)
            vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
