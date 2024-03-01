package com.example.ooclock;

import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.MODE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.REMINDER;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SNOOZE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SNOOZE_TIME;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.URI;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VIBRATE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VOLUME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ooclock.model.Alarm;
import com.example.ooclock.service.AlarmService;
import com.example.ooclock.utils.TimePickerUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;

public class TurnOffAlarm extends AppCompatActivity {
    TextView txt_thucday;
    TextView noi_dung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_off_alarm);
        txt_thucday = findViewById(R.id.txt_gioThucDay);
        Date currentTime = Calendar.getInstance().getTime();
        txt_thucday.setText(TimePickerUtil.tof12H(currentTime.getHours(),currentTime.getMinutes()));
        noi_dung = findViewById(R.id.noi_dung);
        noi_dung.setText(getIntent().getStringExtra(TITLE));
    }

    public void turnOffAlarm(View view) {
        if(getIntent().getStringExtra(MODE)!=null) {
            if (getIntent().getStringExtra(MODE).startsWith("1")) {
                Intent intent = new Intent(this, TurnOffAlarmMath.class);

                intent.putExtra(MODE, getIntent().getStringExtra(MODE));
                intent.putExtra(URI, getIntent().getStringExtra(URI));
                intent.putExtra(VOLUME, getIntent().getFloatExtra(VOLUME,1.0f));
                intent.putExtra(VIBRATE, getIntent().getBooleanExtra(VIBRATE,false));
                intent.putExtra(SNOOZE, getIntent().getBooleanExtra(SNOOZE,false));
                intent.putExtra(TITLE, getIntent().getStringExtra(TITLE));

                startActivity(intent);
                finish();
            } else turnOff();
        }
        else turnOff();
    }

    public void turnOff(){
        if(getIntent().getBooleanExtra(SNOOZE,false)){
            startSnooze();
        }
        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }
    public void startSnooze(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, SNOOZE_TIME);

        Alarm alarm = new Alarm(
                new Random().nextInt(Integer.MAX_VALUE),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                getIntent().getStringExtra(TITLE),
                System.currentTimeMillis(),
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                getIntent().getStringExtra(MODE),
                getIntent().getStringExtra(URI),
                getIntent().getBooleanExtra(VIBRATE,false),
                getIntent().getFloatExtra(VOLUME,1.0f),
                false,
                false
        );

        alarm.schedule(getApplicationContext());

        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }
}