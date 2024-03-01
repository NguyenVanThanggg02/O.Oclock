package com.example.ooclock.utils;

import android.os.Build;
import android.widget.TextView;
import android.widget.TimePicker;

public final class TimePickerUtil {
    public static int getTimePickerHour(TextView tp) {
        String time = tp.getText().toString();
        int hour = Integer.parseInt(time.split(":")[0]);
        if(hour==12) hour=0;
        String am = time.split(" ")[1];
        if(am.equals("PM")||am.equals("CH")){
            hour+=12;
        }
        if(hour>=24) hour-=24;
        return hour;
    }

    public static int getTimePickerMinute(TextView tp) {
        String time = tp.getText().toString();
        int minute = Integer.parseInt(time.split(":")[1].split(" ")[0]);
        return minute;
    }

    public static String tof12H(int hour,int minute) {
        String m,h;
        String aa="AM";
        if(hour>12){
            hour-=12;
            aa = "PM";
        }
        if(hour==12){
            aa = "PM";
        }
        if(hour==0){
            hour = 12;
            aa = "AM";
        };
        if(minute<10) m="0"+minute;
        else m=""+minute;
        if(hour<10) h="0"+hour;
        else h=""+hour;
        return h+":"+m+" "+aa;
    }
}
