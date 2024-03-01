package com.example.ooclock;

import static com.example.ooclock.application.App.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.concurrent.TimeUnit;

public class MenuFocusTiming extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    Vibrator vibrator;
    TextView txt_countdown;
    long millis;
    String format_time;
    String countdown_time;
    int time;
    CountDownTimer count;
    boolean touch;
    boolean finish;
    boolean isruned;
    Button btn_setStop;

    void bindingView(){
        txt_countdown = findViewById(R.id.txt_countdown);
        btn_setStop = findViewById(R.id.btn_stop);
    }

    void bindingAction(){
        btn_setStop.setOnClickListener(this::onBtnStop);
    }

    private void onBtnStop(View view) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MenuFocusTiming.this);

        myAlertBuilder.setMessage("Bạn có chắc chắn muốn dừng lại không?");

        myAlertBuilder.setPositiveButton("OK", (dialog, which) ->  {
            startActivity(new Intent(MenuFocusTiming.this, MenuFocusGiveup.class));
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
        setContentView(R.layout.activity_menu_focus_timing);
        bindingView();
        bindingAction();
        isruned=false;
        countdown_time = getIntent().getStringExtra("countdown_time");
        time = Integer.parseInt(countdown_time.split(":")[0]);
        millis= time * 1000L;
        finish=false;
            count = new CountDownTimer(time * 1000L, 1000) {
                @SuppressLint("DefaultLocale")
                public void onTick(long millisUntilFinished) {

                    format_time = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millis) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                            TimeUnit.MILLISECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    txt_countdown.setText(format_time);
                    millis -= 1000;
                }

                public void onFinish() {
                    try {
                        txt_countdown.setText(R.string.end_countdown);
                        mediaPlayer = MediaPlayer.create(MenuFocusTiming.this, R.raw.oosoundtrack);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {0, 100, 1000};
                        vibrator.vibrate(pattern, -1);
                        finish = true;
                        Intent intent = new Intent(MenuFocusTiming.this, MenuFocusBreakTime.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception ex) {
                    }
                }
            }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MenuFocusTiming.this);

        myAlertBuilder.setMessage("Đang tập trung, hãy tiếp tục nào!");

        myAlertBuilder.setPositiveButton("OK", (dialog, which) ->  {
            touch=false;
        });

        AlertDialog alertDialog = myAlertBuilder.create();
        if(!touch){
            alertDialog.show();
            touch=true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onPause() {
        isruned = true;
        super.onPause();
        if(!finish) {
            if (count != null) count.cancel();
            startActivity(new Intent(MenuFocusTiming.this, MenuFocusGiveup.class));
            overridePendingTransition(0, 0);
            finish();
        }
    }


    @Override
    protected void onUserLeaveHint() {
        if(!finish){
            displayNotification();
            if(count!=null)
                count.cancel();
            startActivity(new Intent(MenuFocusTiming.this, MenuFocusGiveup.class));
            overridePendingTransition(0,0);
            finish();
        }
        super.onUserLeaveHint();
    }

    public void displayNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_focus)
                .setContentTitle("Hãy quay lại tập trung")
                .setContentText("Trong lúc tập trung bạn không được cầm điện thoại")
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}