package com.example.ooclock.broadcastreceiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.ooclock.data.AlarmModel;
import com.example.ooclock.model.Alarm;
import com.example.ooclock.service.AlarmService;
import com.example.ooclock.service.RescheduleAlarmsService;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    AlarmModel alarmModel;
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String RECURRING = "RECURRING";
    public static final String TITLE = "TITLE";
    public static final String MODE = "MODE";
    public static final String URI = "URI";
    public static final String VOLUME = "VOLUME";
    public static final String VIBRATE = "VIBRATE";
    public static final String REMINDER = "REMINDER";
    public static final String SNOOZE = "SNOOZE";
    public static final int SNOOZE_TIME = 5;


    @Override
    public void onReceive(Context context, Intent intent) {
        alarmModel = new AlarmModel((Application) context.getApplicationContext());
        Log.d("An_Test","AlarmBroadcastReceiver: "+intent.toString());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        }
        else {
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (!intent.getBooleanExtra(RECURRING, false)) {
                startAlarmService(context, intent);
                Alarm alarm = alarmModel.getAlarmbyId(intent.getIntExtra("ID",0));
                alarm.setStarted(false);
                alarmModel.update(alarm);
            }
            else if (alarmIsToday(intent)) {
                startAlarmService(context, intent);
            }
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

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(MODE, intent.getStringExtra(MODE));
        intentService.putExtra(URI, intent.getStringExtra(URI));
        intentService.putExtra(VOLUME, intent.getFloatExtra(VOLUME,1.0f));
        intentService.putExtra(VIBRATE, intent.getBooleanExtra(VIBRATE,false));
        intentService.putExtra(SNOOZE, intent.getBooleanExtra(SNOOZE,false));
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
