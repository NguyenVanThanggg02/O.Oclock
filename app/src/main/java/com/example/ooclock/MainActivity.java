package com.example.ooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
//import android.widget.Toolbar;


import com.example.ooclock.alarmlist.AlarmRecyclerViewAdapter;
import com.example.ooclock.data.AlarmModel;
import com.example.ooclock.model.listeners.OnToggleAlarmListener;
import com.example.ooclock.model.Alarm;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnToggleAlarmListener {
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmModel alarmsListViewModel;
    private RecyclerView alarmsRecyclerView;
    TextView txtMessage;
    BottomNavigationView bottomNavigationView;
    Handler handler;
    Runnable clockRunable;

    void bindingView(){
        txtMessage = findViewById(R.id.txt_message);
        alarmsRecyclerView = findViewById(R.id.fragment_listalarms_recylerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    void bindingAction() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_clock:
                startActivity(new Intent(getApplicationContext(), MenuClock.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_focus:
                startActivity(new Intent(getApplicationContext(), MenuFocus.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_stopwatch:
                startActivity(new Intent(getApplicationContext(), MenuStopWatch.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_more:
                startActivity(new Intent(getApplicationContext(), MenuSettings.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_alarm:
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       bindingView();

        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (alarms != null) {
                    Collections.sort(alarms);
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                    txtMessage.setText(alarmRecyclerViewAdapter.getWillRing());
                }
            }
        });

        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);
        bottomNavigationView.setSelectedItemId(R.id.menu_alarm);
        bindingAction();
        reTime();
    }

    private void reTime()
    {
        // Get the text view.
        // Creates a new Handler
        handler
                = new Handler();
        handler.post(clockRunable = new Runnable() {
            @Override

            public void run()
            {
                txtMessage.setText(alarmRecyclerViewAdapter.getWillRing());
                handler.postDelayed(clockRunable, 1000);
            }
        });
    }

    public void addNewAlarm(View view) {
        Intent intent = new Intent(this, CreateAlarm.class);
        startActivity(intent);
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(this);
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(this);
            alarmsListViewModel.update(alarm);
        }
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(clockRunable);
        super.onStop();
    }

    public void onHoldDelete(Alarm alarm) {
        alarmsListViewModel.deleteAlarm(alarm);
    }
}