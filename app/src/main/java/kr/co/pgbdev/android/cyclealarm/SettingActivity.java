package kr.co.pgbdev.android.cyclealarm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.pgbdev.android.cyclealarm.Phone.ContackShared;
import kr.co.pgbdev.android.cyclealarm.Tool.AlarmState;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;

public class SettingActivity extends AppCompatActivity {

    EditText et_number;

    //EditText et_test;
    //Button bt_send;

    Button btn_test;

    TextView tv_gps;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView(){
        try{

            //TEST
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
                                int integerDistance = Integer.parseInt(selectDistance.replaceAll("m",""));
                                tv_gps.setText(selectDistance);
                                ContackShared.setDistance(getBaseContext(),selectDistance);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    })
                    .setTitle("?????? ??????")
                    .setPositiveButton("??????",null)
                    .create();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String[] distance = {"50m","100m","150m","200m","250m","300m","500m"};
    private AlertDialog alertDialog = null;
}
