package com.example.ooclock;

import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.MODE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.REMINDER;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SNOOZE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.URI;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VIBRATE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VOLUME;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ooclock.data.AlarmModel;
import com.example.ooclock.model.Alarm;
import com.example.ooclock.service.AlarmService;
import com.example.ooclock.utils.TimePickerUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAlarm extends AppCompatActivity {
    @BindView(R.id.textTimePicker) TextView textTimePicker;
    @BindView(R.id.editTextLabel)
    EditText title;
    @BindView(R.id.btn_schedule)
    Button btnScheduleAlarm;
    @BindView(R.id.checkBox)
    CheckBox recurring;
    @BindView(R.id.mon) CheckBox mon;
    @BindView(R.id.tue) CheckBox tue;
    @BindView(R.id.wed) CheckBox wed;
    @BindView(R.id.thu) CheckBox thu;
    @BindView(R.id.fri) CheckBox fri;
    @BindView(R.id.sat) CheckBox sat;
    @BindView(R.id.sun) CheckBox sun;
    @BindView(R.id.recurring_options)
    LinearLayout recurringOptions;
    @BindView(R.id.create_text) TextView txt_create;
    String mode;
    Uri uri;
    boolean vibrate;
    float volume;
    boolean snooze;
    boolean reminder;

    final int WAY = 1;
    final int SOUND = 2;
    final int OTHER = 3;
    private AlarmModel createAlarmViewModel;
//    private Spinner spinner_turnOffAlarm;
    int hour, minute;
    Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        ButterKnife.bind(this);
        createAlarmViewModel = ViewModelProviders.of(this).get(AlarmModel.class);

        txt_create.setText(R.string.lbl_addAlarm);
        int alarmId = getIntent().getIntExtra("alarmId",-1);
        if(alarmId!=-1) alarm = createAlarmViewModel.getAlarmbyId(alarmId);
        mode="0";
        uri=Uri.parse("");
        volume=1.0f;
        vibrate=false;
        reminder=false;
        snooze=false;

        CompoundButton.OnCheckedChangeListener weekdayListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkEveryDay())
                    recurring.setChecked(true);
                else
                    recurring.setChecked(false);
            }
        } ;

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mon.setChecked(true);
                    tue.setChecked(true);
                    wed.setChecked(true);
                    thu.setChecked(true);
                    fri.setChecked(true);
                    sat.setChecked(true);
                    sun.setChecked(true);
                }
                else {
                    if(checkEveryDay()) {
                        mon.setChecked(false);
                        tue.setChecked(false);
                        wed.setChecked(false);
                        thu.setChecked(false);
                        fri.setChecked(false);
                        sat.setChecked(false);
                        sun.setChecked(false);
                    }
                }
            }
        });
        mon.setOnCheckedChangeListener(weekdayListener);
        tue.setOnCheckedChangeListener(weekdayListener);
        wed.setOnCheckedChangeListener(weekdayListener);
        thu.setOnCheckedChangeListener(weekdayListener);
        fri.setOnCheckedChangeListener(weekdayListener);
        sat.setOnCheckedChangeListener(weekdayListener);
        sun.setOnCheckedChangeListener(weekdayListener);

        Date currentTime = Calendar.getInstance().getTime();
        if(alarm!=null) {
            txt_create.setText(R.string.lbl_editAlarm);
            textTimePicker.setText(TimePickerUtil.tof12H(alarm.getHour(), alarm.getMinute()));
            mon.setChecked(alarm.isMonday());
            tue.setChecked(alarm.isTuesday());
            wed.setChecked(alarm.isWednesday());
            thu.setChecked(alarm.isThursday());
            fri.setChecked(alarm.isFriday());
            sat.setChecked(alarm.isSaturday());
            sun.setChecked(alarm.isSunday());
            mode=alarm.getMode();
            uri=Uri.parse(alarm.getUri());
            volume=alarm.getVolume();
            vibrate=alarm.isVibrate();
            reminder = alarm.isReminder();
            snooze = alarm.isSnooze();
        }
        else textTimePicker.setText(TimePickerUtil.tof12H(currentTime.getHours(),currentTime.getMinutes()));
        hour=TimePickerUtil.getTimePickerHour(textTimePicker);
        minute=TimePickerUtil.getTimePickerMinute(textTimePicker);

        textTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateAlarm.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int m) {
                                hour = hourOfDay;
                                minute = m;
                                String time = hour + ":" + minute;
                                //24 hours format
                                SimpleDateFormat f24H = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24H.parse(time);
                                    //12 hours format
                                    SimpleDateFormat f12H = new SimpleDateFormat("hh:mm aa");
                                    textTimePicker.setText(f12H.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },hour,minute, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });
        btnScheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarm==null)
                    scheduleAlarm();
                else{
                    updateAlarm(alarm);
                }
                finish();
            }
        });
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(textTimePicker),
                TimePickerUtil.getTimePickerMinute(textTimePicker),
                title.getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked(),
                mode,
                uri.toString(),
                vibrate,
                volume,
                reminder,
                snooze
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(this);
    }

    private void updateAlarm(Alarm alarm) {
        int alarmId = alarm.getAlarmId();
        alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(textTimePicker),
                TimePickerUtil.getTimePickerMinute(textTimePicker),
                title.getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked(),
                mode,
                uri.toString(),
                vibrate,
                volume,
                reminder,
                snooze
        );

        createAlarmViewModel.update(alarm);
        alarm.schedule(this);
    }

    public void previewAlarm(View view) {
        Intent intent = new Intent(this, TurnOffAlarm.class);
        intent.putExtra(MODE,mode);
        Intent serviceIntent = new Intent(this, AlarmService.class);
        serviceIntent.putExtra(URI,uri.toString());
        serviceIntent.putExtra(VOLUME,volume);
        serviceIntent.putExtra(VIBRATE,vibrate);
        serviceIntent.putExtra(TITLE,title.getText());
        startService(serviceIntent);
        startActivity(intent);
    }

    public boolean checkEveryDay(){
        return mon.isChecked()&tue.isChecked()&wed.isChecked()&thu.isChecked()&fri.isChecked()&sat.isChecked()&sun.isChecked();
    }
    public void chooseWay(View view) {
        Intent intent = new Intent(this, WayTurnOffActivity.class);
        intent.putExtra(MODE,mode);
        startActivityForResult(intent,WAY);
    }
//
//    public void soundOption(View view) {
//        Intent intent = new Intent(this, SoundOptionActivity.class);
//        intent.putExtra(URI,uri.toString());
//        intent.putExtra(VOLUME,volume);
//        intent.putExtra(VIBRATE,vibrate);
//        startActivityForResult(intent,SOUND);
//    }




    public void chooseOtherOption(View view) {
        Intent intent = new Intent(this, OtherOptionActivity.class);
        intent.putExtra(REMINDER,reminder);
        intent.putExtra(SNOOZE,snooze);
        startActivityForResult(intent,OTHER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WAY) {
            if (resultCode == RESULT_OK) {
                mode = data.getStringExtra(MODE);
            }
        }
        if (requestCode == SOUND) {
            if (resultCode == RESULT_OK) {
                uri = Uri.parse(data.getStringExtra(URI));
                volume = data.getFloatExtra(VOLUME,1.0f);
                vibrate = data.getBooleanExtra(VIBRATE,false);
            }
        }
        if (requestCode == OTHER) {
            if (resultCode == RESULT_OK) {
                reminder = data.getBooleanExtra(REMINDER,false);
                snooze = data.getBooleanExtra(SNOOZE,false);
            }
        }
    }

    public void back(View view) {
        finish();
    }
}