package com.example.ooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuFocusGiveup extends AppCompatActivity {

    Button btn_startAgain;
    BottomNavigationView bottomNavigationView;

    void bindingView(){
        btn_startAgain = findViewById(R.id.btn_start_focus_again);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    void bindingAction(){
        btn_startAgain.setOnClickListener(this::onBtnStartAgain);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
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
        });
    }


    private void onBtnStartAgain(View view) {
        startActivity(new Intent(MenuFocusGiveup.this, MenuFocus.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_focus_giveup);
        bindingView();
        bottomNavigationView.setSelectedItemId(R.id.menu_focus);
        bindingAction();
    }
}