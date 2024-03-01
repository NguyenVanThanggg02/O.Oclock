package com.example.ooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuFocus extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    BottomNavigationView bottomNavigationView;
    Button btnTimer;

    void bindingView(){
        viewFlipper = findViewById(R.id.viewflipper);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnTimer = findViewById(R.id.btn_timer);
    }

    void bindingAction(){
        btnTimer.setOnClickListener(this::onBtnTimer);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
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

            case R.id.menu_stopwatch:
                startActivity(new Intent(getApplicationContext(), MenuStopWatch.class));
                overridePendingTransition(0,0);
                finish();
                return true;

            case R.id.menu_more:
                startActivity(new Intent(getApplicationContext(), MenuSettings.class));
                overridePendingTransition(0,0);
                finish();
                return true;

            case R.id.menu_focus:
                return true;
        }
        return false;
    }


    private void onBtnTimer(View view) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MenuFocus.this);

        myAlertBuilder.setMessage("Bạn đã sẵn sàng để tập trung?");

        myAlertBuilder.setPositiveButton("OK", (dialog, which) ->  {
            String countdown_time = ((TextView)viewFlipper.getCurrentView()).getText().toString();
            startActivity(new Intent(MenuFocus.this, MenuFocusTiming.class).putExtra("countdown_time",countdown_time));
            overridePendingTransition(0,0);
            finish();
        });

        myAlertBuilder.setNegativeButton("Hủy", (dialog, which) -> {
        });
        AlertDialog alertDialog = myAlertBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_focus);
        bindingView();
        bottomNavigationView.setSelectedItemId(R.id.menu_focus);
        bindingAction();
    }


    public void flipper(View view) {
        if (view.getId() == R.id.forward_flipper){
            Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            viewFlipper.setOutAnimation(out);
            viewFlipper.setInAnimation(in);
            viewFlipper.showNext();
        } else if (view.getId() == R.id.back_flipper) {
            Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
            viewFlipper.setOutAnimation(out);
            viewFlipper.setInAnimation(in);
            viewFlipper.showPrevious();
        }
    }
}
