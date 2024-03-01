package com.example.ooclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuSettings extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    void bindingView(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    void bindingAction(){
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_alarm:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_clock:
                startActivity(new Intent(getApplicationContext(), MenuClock.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_stopwatch:
                startActivity(new Intent(getApplicationContext(), MenuStopWatch.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_focus:
                startActivity(new Intent(getApplicationContext(), MenuFocus.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            case R.id.menu_more:
                return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_settings);
        bindingView();
        bottomNavigationView.setSelectedItemId(R.id.menu_more);
        bindingAction();
    }


    public void onClick(View view) {
        if (view.getId() == R.id.option_infor) {
            Intent intent = new Intent(this, MenuSettingsInfo.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }

}