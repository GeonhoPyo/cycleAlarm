package kr.co.pgbdev.android.cyclealarm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.pgbdev.android.cyclealarm.Phone.ContackShared;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;

public class SettingActivity extends AppCompatActivity {

    EditText et_number;

    EditText et_test;
    Button bt_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView(){
        try{

            et_number = findViewById(R.id.et_number);
            et_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            et_number.setText(ContackShared.getNumber(getBaseContext()));

            et_test = findViewById(R.id.et_test);
            bt_send = findViewById(R.id.bt_send);
            bt_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        String number = et_number.getText().toString();
                        String message = et_test.getText().toString();
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, message+"\n"+"https://www.google.com/maps/search/37.5840135,127.0247839/data=!4m2!2m1!4b1?hl=ko&nogmmr=1", null,
                                null);
                        Toast.makeText(SettingActivity.this, "number : " + number + " , message : " + message, Toast.LENGTH_SHORT).show();

                        ContackShared.setNumber(getBaseContext(),number);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
