package com.example.ooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.FocusFinder;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;

public class MenuClock extends AppCompatActivity {
    TextView txtHours;
    TextView txtDays;
    Handler handler;
    Runnable clockRunable;
    BottomNavigationView bottomNavigationView;

    void bindingView() {
        txtHours = findViewById(R.id.txt_hours);
        txtDays = findViewById(R.id.txt_days);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        handler = new Handler();
    }

    void bindingAction() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_alarm:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

            case R.id.menu_clock:
                return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_clock);
        bindingView();
        bottomNavigationView.setSelectedItemId(R.id.menu_clock);
        bindingAction();
        currentTime();
    }

    private void currentTime() {
        handler.post(clockRunable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                int h = calendar.getTime().getHours();
                int m = calendar.getTime().getMinutes();
                String dw = getDayofWeek(calendar);
                int dm = calendar.get(Calendar.DAY_OF_MONTH);
                int mo = calendar.get(Calendar.MONTH) + 1;
                int y = calendar.get(Calendar.YEAR);
                txtHours.setText(String.format("%02d:%02d", h, m));
                txtDays.setText(String.format("%s, %02d/%02d/%04d", dw, dm, mo, y));
                handler.postDelayed(clockRunable, 1000);
            }
        });
    }

    public String getDayofWeek(Calendar calendar) {
        String dom = "";
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
            dom = "CN";
        else
            dom = "Thứ " + calendar.get(Calendar.DAY_OF_WEEK);
        return dom;
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(clockRunable);
        super.onStop();
    }
}