package com.example.ooclock.alarmlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ooclock.MainActivity;
import com.example.ooclock.R;
import com.example.ooclock.model.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<com.example.ooclock.alarmlist.AlarmViewHolder> {
    private List<Alarm> alarms;
    private MainActivity listener;
    public AlarmRecyclerViewAdapter(MainActivity listener) {
        this.alarms = new ArrayList<Alarm>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public com.example.ooclock.alarmlist.AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new com.example.ooclock.alarmlist.AlarmViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.ooclock.alarmlist.AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.bind(alarm);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull com.example.ooclock.alarmlist.AlarmViewHolder holder) {
        super.onViewRecycled(holder);
        holder.alarmStarted.setOnCheckedChangeListener(null);
    }

    public String getWillRing(){
        String text="";
        Date currentTime = Calendar.getInstance().getTime();
        Alarm currentalarm = new Alarm(currentTime.getHours(), currentTime.getMinutes());
        boolean willRing24h = false;
        for (Alarm alarm : alarms) {
            if (alarm.compareTo(currentalarm) >= 0 && alarm.willRingToday()) {
                Alarm minusalarm = alarm.minus(currentalarm);
                text="Chuông báo thức sau \n"+minusalarm.getHour()+" giờ "+minusalarm.getMinute()+" phút";
                willRing24h = true;
                break;
            }
        }
        if (!willRing24h)
            for (Alarm alarm : alarms) {
                if (alarm.compareTo(currentalarm) < 0 && alarm.willRingTomorow()) {
                    Alarm minusalarm = alarm.minus(currentalarm);
                    text="Chuông báo thức sau \n"+minusalarm.getHour()+" giờ "+minusalarm.getMinute()+" phút";
                    willRing24h = true;
                    break;
                }
            }
        if (!willRing24h) {
            text="Không có báo thức nào được bật";
        }
        return text;
    }
}

