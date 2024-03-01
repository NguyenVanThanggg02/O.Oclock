package com.example.ooclock.broadcastreceiver;

import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.FRIDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.MONDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.RECURRING;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SATURDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SUNDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.THURSDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TUESDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.WEDNESDAY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.ooclock.service.AlarmNotificationService;

import java.util.Calendar;

public class NotiBroadcastReceiver extends BroadcastReceiver {
    public static final int NOTITIME = 15;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getBooleanExtra(RECURRING, false)) {
            startAlarmNotiService(context, intent);
        } else if (alarmIsToday(intent)) {
            startAlarmNotiService(context, intent);
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra(MONDAY, false))
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra(TUESDAY, false))
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra(WEDNESDAY, false))
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra(THURSDAY, false))
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra(FRIDAY, false))
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra(SATURDAY, false))
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra(SUNDAY, false))
                    return true;
                return false;
        }
        return false;
    }

    private void startAlarmNotiService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmNotificationService.class);
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
