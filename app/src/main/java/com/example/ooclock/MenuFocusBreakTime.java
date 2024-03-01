package com.example.ooclock;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MenuFocusBreakTime extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    Vibrator vibrator;
    private ViewFlipper viewFlipper;
    Button btn_break;
    View back_flipper;
    View forward_flipper;
    long millis;
    String format_time;
    TextView txt_countdown;
    String countdown_time;
    int time;
    CountDownTimer count;
    boolean iscount;
    boolean finish;

    void bindingView(){
        viewFlipper = findViewById(R.id.viewflipper);
        btn_break = findViewById(R.id.btn_break);
        back_flipper = findViewById(R.id.back_flipper);
        forward_flipper = findViewById(R.id.forward_flipper);
    }
    
    void bindingAction(){
        btn_break.setOnClickListener(this::onBtnBreakClick);
    }

    private void onBtnBreakClick(View view) {
        back_flipper.setVisibility(View.INVISIBLE);
        forward_flipper.setVisibility(View.INVISIBLE);
        btn_break.setText("Bắt đầu luôn");
        btn_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=null)
                    count.cancel();
                Intent intent = new Intent(MenuFocusBreakTime.this,MenuFocus.class);
                startActivity(intent);
                finish();

            }
        });
        txt_countdown = (TextView) viewFlipper.getCurrentView();
        countdown_time = txt_countdown.getText().toString();
        time = Integer.parseInt(countdown_time.split(":")[0]);
        millis= time * 60000;
        finish=false;
        iscount=true;
        count = new CountDownTimer(millis, 1000) {
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
                    finish=true;
                    txt_countdown.setText(R.string.end_countdown);
                    mediaPlayer = MediaPlayer.create(MenuFocusBreakTime.this, R.raw.restart_sound);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 100, 1000};
                    vibrator.vibrate(pattern, -1);
                    Intent intent = new Intent(MenuFocusBreakTime.this, MenuFocus.class);
                    startActivity(intent);
                    finish();
                } catch (Exception ex) {
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_focus_break_time);
        bindingView();
        iscount=false;
        bindingAction();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(finish) {
            if (count != null)
                count.cancel();
            Intent intent = new Intent(MenuFocusBreakTime.this, MenuFocus.class);
            startActivity(intent);
            finish();
        }
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