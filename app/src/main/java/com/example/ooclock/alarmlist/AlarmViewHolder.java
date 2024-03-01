package com.example.ooclock.alarmlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ooclock.CreateAlarm;
import com.example.ooclock.MainActivity;
import com.example.ooclock.R;
import com.example.ooclock.model.Alarm;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private TextView alarmRecurring;
    private TextView alarmTitle;
    private ConstraintLayout constraintLayout;
    private View container1;

    SwitchCompat alarmStarted;

    private MainActivity listener;

    public AlarmViewHolder(@NonNull View itemView, MainActivity listener) {
        super(itemView);
        bindingView();
        bindingAction();
        this.listener = listener;
    }

    private void bindingAction() {
    }

    private void bindingView() {
        constraintLayout = itemView.findViewById(R.id.alarm_list_item);
        alarmTime = itemView.findViewById(R.id.txt_gio);
        alarmStarted = itemView.findViewById(R.id.switch1);
        alarmRecurring = itemView.findViewById(R.id.txt_lap);
        alarmTitle = itemView.findViewById(R.id.txt_label);
        container1 = itemView.findViewById(R.id.container1);
    }

    public void bind(Alarm alarm) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());
        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if(alarm.isStarted())
            container1.setBackgroundResource(R.drawable.container1);
        else
            container1.setBackgroundResource(R.drawable.container2);

        if (alarm.isRecurring()) {
            if(alarm.isEveryday())
                alarmRecurring.setText("Hàng ngày");
            else
                alarmRecurring.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setText("Một lần");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(alarm.getTitle());
        } else {
            alarmTitle.setText("Alarm");
        }

        constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setTitle("Bạn muốn xoá báo thức ư?");
                alertDialogBuilder.setPositiveButton("OK",(dialog,which)->{
                    alarm.cancelAlarm(view.getContext());
                    listener.onHoldDelete(alarm);
                });
                alertDialogBuilder.show();
                return false;
            }
        });
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarm.cancelAlarm(view.getContext());
                Intent editIntent = new Intent(listener.getBaseContext(), CreateAlarm.class);
                editIntent.putExtra("alarmId",alarm.getAlarmId());
                listener.startActivity(editIntent);
            }
        });

        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    container1.setBackgroundResource(R.drawable.container1);
                else
                    container1.setBackgroundResource(R.drawable.container2);
                listener.onToggle(alarm);
            }
        });
    }
}
