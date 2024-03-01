package com.example.ooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MenuStopWatch extends AppCompatActivity {
    private long milisecs = 0;
    long start;
    long minus_time_start;
    boolean runed;
    private boolean running;
    private boolean wasRunning;
    Button btn_ss;
    Button btn_rr;
    TextView txt_time;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "myPrefs";
    BottomNavigationView bottomNavigationView;

    void bindingView(){
        btn_rr = findViewById(R.id.btn_reset);
        btn_ss = findViewById(R.id.button2);
        txt_time = findViewById(R.id.textView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
    
    void bindingAction(){
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_alarm:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;

            case R.id.menu_clock:
                startActivity(new Intent(getApplicationContext(), MenuClock.class));
                overridePendingTransition(0,0);
                finish();
                return true;

            case R.id.menu_focus:
                startActivity(new Intent(getApplicationContext(), MenuFocus.class));
                overridePendingTransition(0,0);
                finish();
                return true;

            case R.id.menu_more:
                startActivity(new Intent(getApplicationContext(), MenuSettings.class));
                overridePendingTransition(0,0);
                finish();
                return true;

            case R.id.menu_stopwatch:
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_stop_watch);
        runed=false;
        bindingView();
        bottomNavigationView.setSelectedItemId(R.id.menu_stopwatch);
        bindingAction();

        minus_time_start=0;
        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        milisecs
                = mPreferences
                .getLong("milisecs",0L);
        running
                = mPreferences
                .getBoolean("running",false);
        wasRunning
                = mPreferences
                .getBoolean("wasRunning",false);
        start=mPreferences
                .getLong("start", 0L);
        minus_time_start=mPreferences
                .getLong("minus_time_start", 0L);
        runed=mPreferences
                .getBoolean("runed", false);

        txt_time.setText(mPreferences.getString("txt_time_string","00:00:00.00"));
        if(!runed){
            btn_rr.setVisibility(View.VISIBLE);
            btn_rr.setText(R.string.btn_reset);
            btn_ss.setText(R.string.btn_start);
        }
        else {
            btn_rr.setVisibility(View.INVISIBLE);
            btn_ss.setText(R.string.stop);
        }

        runTimer();
    }




    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor
                .putLong("milisecs", milisecs);
        preferencesEditor
                .putBoolean("running", running);
        preferencesEditor
                .putBoolean("wasRunning", wasRunning);
        preferencesEditor
                .putLong("start", start);
        preferencesEditor
                .putLong("minus_time_start", minus_time_start);
        preferencesEditor
                .putBoolean("runed", runed);
        preferencesEditor.putString("txt_time_string",txt_time.getText().toString());
        preferencesEditor.commit();
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }


    public void onClickStart_Stop(View view)
    {
        if(runed){
            minus_time_start=System.currentTimeMillis();
            runed=false;
            btn_rr.setVisibility(View.VISIBLE);
            btn_rr.setText(R.string.btn_reset);
            btn_ss.setText(R.string.btn_start);
        }
        else {
            start +=System.currentTimeMillis()-minus_time_start;
            runed=true;
            btn_rr.setVisibility(View.INVISIBLE);
            btn_ss.setText(R.string.stop);
        }
    }


    public void onClickReset_Record(View view)
    {
            milisecs=0;
            start=0;
            minus_time_start=0;
            txt_time.setText(R.string.txt_timer);

    }

    private void runTimer()
    {

        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run()
            {
                String time = format_time(milisecs,true);
                if (runed) {
                    txt_time.setText(time);
                    milisecs=System.currentTimeMillis()-start;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1);
            }
        });
    }

    public String format_time(long mils,boolean zero){
        String time;
        int hours = (int) (mils/1000) / 3600;
        int minutes = (int) ((mils/1000) % 3600) / 60;
        int secs = (int) (mils/1000) % 60;
        int mili = (int) (mils % 1000)/10;

        // Format the seconds into hours, minutes,
        // and seconds.
        if(zero) {
            time = String.format(Locale.getDefault(),
                    "%02d:%02d:%02d.%02d", hours,
                    minutes, secs, mili);
        }
        else {
            if(hours==0){
                if(minutes==0){
                    time = String.format(Locale.getDefault(),
                            "%02d.%02d", secs, mili);
                }
                else{
                    time = String.format(Locale.getDefault(),
                            "%02d:%02d.%02d",minutes, secs, mili);
                }
            }
            else{
                time = String.format(Locale.getDefault(),
                        "%02d:%02d:%02d.%02d", hours,
                        minutes, secs, mili);
            }
        }
        return time;
    }

}
