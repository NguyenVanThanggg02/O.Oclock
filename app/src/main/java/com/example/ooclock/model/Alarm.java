package com.example.ooclock.model;

import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.FRIDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.MODE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.MONDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.RECURRING;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SATURDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SNOOZE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SUNDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.THURSDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TUESDAY;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.URI;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VIBRATE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VOLUME;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.WEDNESDAY;
import static com.example.ooclock.broadcastreceiver.NotiBroadcastReceiver.NOTITIME;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver;
import com.example.ooclock.broadcastreceiver.NotiBroadcastReceiver;
import com.example.ooclock.service.AlarmNotificationService;
import com.example.ooclock.utils.DayUtil;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "alarm_table")
public class Alarm implements Comparable<Alarm> {
    @PrimaryKey
    @NonNull
    private int alarmId;
    private int hour, minute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String title;
    private String mode;
    private String uri;
    private boolean vibrate;
    private float volume;
    private boolean reminder;
    private boolean snooze;

    private long created;

    @Ignore
    public Alarm(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public Alarm(int alarmId, int hour, int minute, String title, long created, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, String mode, String uri, boolean vibrate, float volume, boolean reminder, boolean snooze) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.started = started;

        this.recurring = recurring;

        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;

        this.title = title;

        this.created = created;

        this.mode = mode;

        this.uri = uri;
        this.volume = volume;
        this.vibrate = vibrate;

        this.reminder = reminder;
        this.snooze = snooze;
    }

    public boolean isReminder() {
        return reminder;
    }

    public boolean isSnooze() {
        return snooze;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isStarted() {
        return started;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }


    public String getMode() {
        return mode;
    }

    public String getUri() {
        return uri;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public float getVolume() {
        return volume;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void schedule(Context context) {
        Log.d("An_Test",context.toString());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        recurring = monday|tuesday|wednesday|thursday|friday|saturday|sunday;
        intent.putExtra("ID", alarmId);
        intent.putExtra(RECURRING, recurring);
        intent.putExtra(MONDAY, monday);
        intent.putExtra(TUESDAY, tuesday);
        intent.putExtra(WEDNESDAY, wednesday);
        intent.putExtra(THURSDAY, thursday);
        intent.putExtra(FRIDAY, friday);
        intent.putExtra(SATURDAY, saturday);
        intent.putExtra(SUNDAY, sunday);
        intent.putExtra(MODE, mode);
        intent.putExtra(URI, uri);
        intent.putExtra(VOLUME, volume);
        intent.putExtra(VIBRATE, vibrate);
        intent.putExtra(SNOOZE, snooze);
        intent.putExtra(TITLE, title+" "+hour+":"+minute);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();

        Date now = Calendar.getInstance().getTime();
        if(now.getHours()==this.getHour() && now.getMinutes()==this.getMinute())
            if(!recurring)
                context.sendBroadcast(intent);
            else if(alarmIsToday(intent))
                context.sendBroadcast(intent);

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("One Time Alarm %s scheduled for %s at %02d:%02d", title, DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)), hour, minute);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        alarmPendingIntent
                );
            }
        } else {
            String toastText = String.format("Recurring Alarm %s scheduled for %s at %02d:%02d", title, getRecurringDaysText(), hour, minute, alarmId);
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            final long RUN_DAILY = 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    RUN_DAILY,
                    alarmPendingIntent
            );
        }

        if(reminder) {
            PendingIntent alarmNotiPendingIntent = PendingIntent.getBroadcast(context, alarmId, new Intent(context, NotiBroadcastReceiver.class).putExtra(TITLE, title + " " + hour + ":" + minute), 0);
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Log.d("An_test", "Exact: " + calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes());
            calendar.add(Calendar.MINUTE, -NOTITIME);
            Log.d("An_test", "Noti: " + calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes());
            if (calendar.getTimeInMillis() <= (System.currentTimeMillis() - NOTITIME * 60 * 1000)) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            }
            if (!recurring) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            alarmNotiPendingIntent
                    );
                }
            } else if (alarmIsToday(intent)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            alarmNotiPendingIntent
                    );
                }
            }
        }

        this.started = true;
    }

    public void cancelAlarm(Context context) {
        Intent intentService = new Intent(context, AlarmNotificationService.class);
        context.stopService(intentService);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(alarmPendingIntent);
        this.started = false;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, NotiBroadcastReceiver.class);
        alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(alarmPendingIntent);

        String toastText = String.format("Alarm cancelled for %02d:%02d", hour, minute);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }

    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (monday) {
            days += "Mo ";
        }
        if (tuesday) {
            days += "Tu ";
        }
        if (wednesday) {
            days += "We ";
        }
        if (thursday) {
            days += "Th ";
        }
        if (friday) {
            days += "Fr ";
        }
        if (saturday) {
            days += "Sa ";
        }
        if (sunday) {
            days += "Su ";
        }

        return days;
    }

    public boolean isEveryday(){
        return monday&tuesday&wednesday&thursday&friday&saturday&sunday;
    }

    public String getTitle() {
        return title;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
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

    private boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (monday)
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (tuesday)
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (wednesday)
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (thursday)
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (friday)
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (saturday)
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (sunday)
                    return true;
                return false;
        }
        return false;
    }

    private boolean isTomorow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int tomorow = calendar.get(Calendar.DAY_OF_WEEK);

        switch(tomorow) {
            case Calendar.MONDAY:
                if (tuesday)
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (wednesday)
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (thursday)
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (friday)
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (saturday)
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (sunday)
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (monday)
                    return true;
                return false;
        }
        return false;
    }

    public boolean willRingTomorow(){
        return isStarted()&&((!recurring)||(recurring&&isTomorow()));
    }

    public boolean willRingToday(){
        return isStarted()&&((!recurring)||(recurring&&isToday()));
    }

    @Override
    public int compareTo(Alarm alarm) {
        if(hour==alarm.hour & minute==alarm.minute) return 0;
        else if(hour>alarm.hour | (hour==alarm.hour & minute>alarm.minute)) return 1;
        else return -1;
    }

    public Alarm minus(Alarm alarm) {
        int h;
        int m;
        Alarm result;
        if(this.compareTo(alarm)>=0){
            if(hour==alarm.hour){
                h=0;
                m=minute-alarm.minute;
            }
            else {
                if(minute>=alarm.minute){
                    h=hour-alarm.hour;
                    m=minute-alarm.minute;
                }
                else {
                    h=hour-alarm.hour-1;
                    m=60+minute-alarm.minute;
                }
            }
            result= new Alarm(h,m);
        }
        else {
            Alarm thisalarm = new Alarm(hour+24,minute);
            result = thisalarm.minus(alarm);
        }
        return result;
    }
}
