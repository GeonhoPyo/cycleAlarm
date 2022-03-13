package kr.co.pgbdev.android.cyclealarm.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.pgbdev.android.cyclealarm.R;
import kr.co.pgbdev.android.cyclealarm.Tool.AlarmState;
import kr.co.pgbdev.android.cyclealarm.Tool.ContackShared;

public class SettingActivity extends AppCompatActivity {

    EditText et_number;

    //EditText et_test;
    //Button bt_send;

    Button btn_test;

    TextView tv_gps;

    Switch sw_version;


    private String[] distance = {"50m","100m","150m","200m","250m","300m","500m"};
    private AlertDialog alertDialog = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView(){
        try{

            btn_test = findViewById(R.id.btn_test);
            btn_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlarmState().alarmStart(getBaseContext());
                }
            });

            tv_gps = findViewById(R.id.tv_gps);
            String selectDistance = ContackShared.getDistance(getBaseContext());
            tv_gps.setText(selectDistance);
            tv_gps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        alertDialog.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            et_number = findViewById(R.id.et_number);
            et_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            String number = ContackShared.getNumber(getBaseContext());
            if(number != null){
                et_number.setText(number);
                et_number.setTextColor(getBaseContext().getResources().getColor(R.color.key_color_1,null));
            }
            et_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    String number = et_number.getText().toString();
                    ContackShared.setNumber(getBaseContext(),number);
                    et_number.setTextColor(getBaseContext().getResources().getColor(R.color.key_color_1,null));
                    et_number.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_number.getWindowToken(),0);
                    return true;
                }
            });

            sw_version = findViewById(R.id.sw_version);
            sw_version.setChecked(ContackShared.getBLEVersion(getBaseContext()));
            sw_version.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try{
                        ContackShared.setBLEVersion(getBaseContext(),b);
                        startActivity(new Intent(getApplication(),SplashActivity.class));
                        if(MainActivity.viewHandler != null){
                            MainActivity.viewHandler.obtainMessage(6).sendToTarget();
                        }
                        SettingActivity.this.finish();


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            alertDialog = new AlertDialog.Builder(SettingActivity.this)
                    .setItems(distance, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try{
                                String selectDistance = distance[i];
                                tv_gps.setText(selectDistance);
                                ContackShared.setDistance(getBaseContext(),selectDistance);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    })
                    .setTitle("오차 범위")
                    .setPositiveButton("취소",null)
                    .create();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
