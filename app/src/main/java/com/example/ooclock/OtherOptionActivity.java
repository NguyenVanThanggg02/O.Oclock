package com.example.ooclock;

import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.REMINDER;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.SNOOZE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.URI;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VIBRATE;
import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.VOLUME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class OtherOptionActivity extends AppCompatActivity {

    CheckBox reminderCheckBox;
    CheckBox snoozeCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_option);
        reminderCheckBox = findViewById(R.id.reminder);
        snoozeCheckBox = findViewById(R.id.snooze);
        Intent intent = getIntent();
        if(intent!=null){
            reminderCheckBox.setChecked(intent.getBooleanExtra(REMINDER,false));
            snoozeCheckBox.setChecked(intent.getBooleanExtra(SNOOZE,false));
        }
    }

    public void back(View view) {
        finish();
    }

    public void chooseOK(View view) {
        Log.d("An_Test", "rm: "+reminderCheckBox.isChecked());
        Log.d("An_Test", "sn: "+snoozeCheckBox.isChecked());
        Intent replyIntent = new Intent();
        replyIntent.putExtra(REMINDER, reminderCheckBox.isChecked());
        replyIntent.putExtra(SNOOZE, snoozeCheckBox.isChecked());
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}