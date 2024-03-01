package com.example.ooclock.service;

import static com.example.ooclock.application.App.CHANNEL_ID;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static com.example.ooclock.broadcastreceiver.NotiBroadcastReceiver.NOTITIME;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.ooclock.MainActivity;
import com.example.ooclock.R;

public class AlarmNotificationService extends Service {
    private Vibrator vibrator;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("An_test","RUNNING");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE));
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Chuan bi bao thuc!")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000};
        vibrator.vibrate(pattern, -1);

        startForeground(1, notification);
        new CountDownTimer(NOTITIME * 60 * 1000, 10000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                try {
                    stopSelf();
                } catch (Exception ex) {

                }
            }
        }.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
