package com.example.ooclock;

import static com.example.ooclock.broadcastreceiver.AlarmBroadcastReceiver.MODE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WayTurnOffActivity extends AppCompatActivity {

    ConstraintLayout math_option_layout;
    RadioButton radio_bell;
    RadioButton radio_math;
//    SeekBar seekBar3;
    EditText editText_amount;
    TextView txt_level;
    TextView txt_example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_turn_off);

        bindingView();

        radio_bell.setChecked(true);
//        txt_level.setText(R.string.math_0_txt);
//        txt_example.setText(R.string.math_0_exa);
////        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//////                switch (seekBar.getProgress()){
//////                    case 0:
////
//////                        break;
//////                    case 1:
//////                        txt_level.setText(R.string.math_1_txt);
//////                        txt_example.setText(R.string.math_1_exa);
//////                        break;
//////                    case 2:
//////                        txt_level.setText(R.string.math_2_txt);
//////                        txt_example.setText(R.string.math_2_exa);
//////                        break;
//////                    case 3:
//////                        txt_level.setText(R.string.math_3_txt);
//////                        txt_example.setText(R.string.math_3_exa);
//////                        break;
//////                    case 4:
//////                        txt_level.setText(R.string.math_4_txt);
//////                        txt_example.setText(R.string.math_4_exa);
//////                        break;
//////                    case 5:
//////                        txt_level.setText(R.string.math_5_txt);
//////                        txt_example.setText(R.string.math_5_exa);
//////                        break;
//////                }
////            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        Intent intent = getIntent();
        String mode = intent.getStringExtra(MODE);
        if(!mode.trim().equals("")&&mode!=null){
            if(mode.startsWith("0")){
                radio_bell.setChecked(true);
                radio_math.setChecked(false);
            }
            else if(mode.startsWith("1")){
                radio_bell.setChecked(false);
                radio_math.setChecked(true);
                math_option_layout.setVisibility(View.VISIBLE);
//                seekBar3.setProgress(Integer.parseInt(mode.split("-")[1]));
                editText_amount.setText(mode.split("-")[2]);
            }
        }
    }

    private void bindingView() {
        math_option_layout = findViewById(R.id.math_option);
        math_option_layout.setVisibility(View.INVISIBLE);
        radio_bell = findViewById(R.id.radio_bell);
        radio_math = findViewById(R.id.radio_math);
//        seekBar3 = findViewById(R.id.seekBar3);
        editText_amount = findViewById(R.id.editText_amount);
//        txt_level = findViewById(R.id.txt_level);
//        txt_example = findViewById(R.id.txt_example);
    }

    public void optionCheck(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radio_bell:
                if (checked){
                    math_option_layout.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.radio_math:
                if (checked){
                    math_option_layout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void back(View view) {
        finish();
    }

    public void chooseOK(View view) {
        String mode="";
        if(radio_bell.isChecked()) mode="0";
        else if(radio_math.isChecked()){
            mode="1";
            mode+="-"+0;
//            if(!editText_amount.getText().toString().trim().equals(""))
//                mode+="-"+editText_amount.getText();
//            else
                mode+="-"+1;
        }
        Log.d("An_Test",mode);
        Intent replyIntent = new Intent();
        replyIntent.putExtra(MODE, mode);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}