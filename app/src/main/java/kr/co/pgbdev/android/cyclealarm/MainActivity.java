package kr.co.pgbdev.android.cyclealarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLeScanTool.BluetoothLEAutoScanTool;
import kr.co.pgbdev.android.cyclealarm.Fragment.Beacon.ConnectionBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.Fragment.Bluetooth.ConnectionBottomSheetBluetoothFragment;
import kr.co.pgbdev.android.cyclealarm.Fragment.ConfirmBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.Tool.GPS_Protocol;
import kr.co.pgbdev.android.cyclealarm.Tool.Utils;

public class MainActivity extends AppCompatActivity {

    TextView tv_bluetoothName;
    ImageView iv_gps;
    ImageView iv_bluetooth;
    ImageView iv_setting;

    TextView tv_beacon_test;

    TextView tv_battery_state_title;
    TextView tv_battery_state;
    TextView tv_battery_use_title;
    TextView tv_battery_use;
    TextView tv_motor_state_title;
    TextView tv_motor_state;
    TextView tv_motor_request_title;
    TextView tv_motor_request;

    RelativeLayout rl_data_1;
    RelativeLayout rl_data_2;
    RelativeLayout rl_data_3;
    RelativeLayout rl_data_4;



    public static Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //Utils 초기화
        Utils.init(getBaseContext());


        //GPS 수신
        new GPS_Protocol().googleGpsListener(this);

        new BluetoothLEAutoScanTool().startAutoConnect(getBaseContext());
        mainContext = getBaseContext();
    }

    boolean click1 = false;
    boolean click2 = false;
    boolean click3 = false;
    boolean click4 = false;


    private void initView(){
        try{
            tv_bluetoothName = findViewById(R.id.tv_bluetoothName);
            iv_bluetooth = findViewById(R.id.iv_bluetooth);
            iv_setting = findViewById(R.id.iv_setting);
            iv_gps = findViewById(R.id.iv_gps);

            tv_beacon_test = findViewById(R.id.tv_beacon_test);
            iv_bluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        checkPermission();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            iv_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        checkSMS();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            tv_battery_state_title = findViewById(R.id.tv_battery_state_title);
            tv_battery_state = findViewById(R.id.tv_battery_state);
            tv_battery_use_title = findViewById(R.id.tv_battery_use_title);
            tv_battery_use = findViewById(R.id.tv_battery_use);
            tv_motor_state_title = findViewById(R.id.tv_motor_state_title);
            tv_motor_state = findViewById(R.id.tv_motor_state);
            tv_motor_request_title = findViewById(R.id.tv_motor_request_title);
            tv_motor_request = findViewById(R.id.tv_motor_request);

            rl_data_1 = findViewById(R.id.rl_data_1);
            rl_data_2 = findViewById(R.id.rl_data_2);
            rl_data_3 = findViewById(R.id.rl_data_3);
            rl_data_4 = findViewById(R.id.rl_data_4);

            rl_data_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(click1){
                            if(dataHandler != null){
                                dataHandler.obtainMessage(1,"Good").sendToTarget();
                            }
                        }else{
                            if(dataHandler != null){
                                dataHandler.obtainMessage(1,"Bad").sendToTarget();
                            }
                        }
                        click1 = !click1;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            rl_data_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(click2){
                            if(dataHandler != null){
                                dataHandler.obtainMessage(2,"Good").sendToTarget();
                            }
                        }else{
                            if(dataHandler != null){
                                dataHandler.obtainMessage(2,"Bad").sendToTarget();
                            }
                        }
                        click2 = !click2;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            rl_data_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(click3){
                            if(dataHandler != null){
                                dataHandler.obtainMessage(3,"Good").sendToTarget();
                            }
                        }else{
                            if(dataHandler != null){
                                dataHandler.obtainMessage(3,"Bad").sendToTarget();
                            }
                        }
                        click3 = !click3;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            rl_data_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if(click4){
                            if(dataHandler != null){
                                dataHandler.obtainMessage(4,"Good").sendToTarget();
                            }
                        }else{
                            if(dataHandler != null){
                                dataHandler.obtainMessage(4,"Bad").sendToTarget();
                            }
                        }
                        click4 = !click4;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            initHandler();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Handler viewHandler = null;
    public static Handler dataHandler = null;
    private void initHandler(){
        try{
            viewHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    try{
                        switch (msg.what){
                            case 1 : // 연결 대기, 권한 없음
                                tv_bluetoothName.setText("연결대기");
                                tv_bluetoothName.setTextColor(getBaseContext().getResources().getColor(R.color.color_9C9C9C,null));
                                break;
                            case 2 : // 연결중
                                int cnt = (int)msg.obj;
                                tv_bluetoothName.setText("연결중("+cnt+")");
                                tv_bluetoothName.setTextColor(getBaseContext().getResources().getColor(R.color.color_9C9C9C,null));
                                break;
                            case 3 : // 연결완료
                                String bluetoothName = (String)msg.obj;
                                tv_bluetoothName.setText(bluetoothName);
                                tv_bluetoothName.setTextColor(getBaseContext().getResources().getColor(R.color.key_color_1,null));
                                break;

                            case 4 : // gps off
                                iv_gps.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.ic_outline_gps_off_24,null));
                                break;

                            case 5 : // gps on
                                iv_gps.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.ic_outline_near_me_24,null));
                                break;

                            case 6 :
                                break;

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            /**
             * tv_battery_state_title
             * tv_battery_state
             * tv_battery_use_title
             * tv_battery_use
             * tv_motor_state_title
             * tv_motor_state
             * tv_motor_request_title
             * tv_motor_request
             * */
            dataHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {

                    try{
                        switch (msg.what){
                            case 1 :
                                //배터리 상태
                                String batteryState = (String)msg.obj;
                                tv_battery_state.setText(batteryState);
                                break;
                            case 2 :
                                //배터리 사용량
                                String batteryUse = (String)msg.obj;
                                tv_battery_use.setText(batteryUse);
                                break;
                            case 3 :
                                //모터 상태
                                String motorState = (String)msg.obj;
                                tv_motor_state.setText(motorState);
                                break;
                            case 4 :
                                //모터 점검 요청
                                String motorRequest = (String)msg.obj;
                                tv_motor_request.setText(motorRequest);
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    ConfirmBottomSheetFragment confirmBottomSheetFragment = null;
    private void checkPermission(){
        try{
            if (!(Utils.isPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    Utils.isPermission(Manifest.permission.ACCESS_COARSE_LOCATION)||
                    Utils.isPermission(Manifest.permission.BLUETOOTH_SCAN)||
                    Utils.isPermission(Manifest.permission.BLUETOOTH_CONNECT))){
                confirmBottomSheetFragment = new ConfirmBottomSheetFragment("알림", "블루투스 주변 검색시, 위치 권한이 필요로 합니다.\n위치 권한을 허용해 주세요.", false,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    confirmBottomSheetFragment.dismiss();
                                    try{
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                                            Manifest.permission.ACCESS_FINE_LOCATION,       // 위치 서비스(정확한 위치 판별 | GPS, Wi-Fi 또는 데이터 사용)
                                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                                            Manifest.permission.BLUETOOTH_SCAN,
                                                            Manifest.permission.BLUETOOTH_CONNECT},
                                                    1000);
                                        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                                            Manifest.permission.ACCESS_FINE_LOCATION,       // 위치 서비스(정확한 위치 판별 | GPS, Wi-Fi 또는 데이터 사용)
                                                            Manifest.permission.ACCESS_COARSE_LOCATION},     // 위치 서비스(대략적인 위치 판별 | Wi-Fi 또는 데이터 사용)
                                                    1000);
                                        }else {
                                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                                            Manifest.permission.ACCESS_FINE_LOCATION,       // 위치 서비스(정확한 위치 판별 | GPS, Wi-Fi 또는 데이터 사용)
                                                            Manifest.permission.ACCESS_COARSE_LOCATION},     // 위치 서비스(대략적인 위치 판별 | Wi-Fi 또는 데이터 사용)
                                                    1000);
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    confirmBottomSheetFragment = null;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                confirmBottomSheetFragment.show(getSupportFragmentManager(), confirmBottomSheetFragment.getTag());
            }else{
                connectionBottomSheetBluetoothFragment = new ConnectionBottomSheetBluetoothFragment();
                connectionBottomSheetBluetoothFragment.show(getSupportFragmentManager(),connectionBottomSheetBluetoothFragment.getTag());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ConnectionBottomSheetBluetoothFragment connectionBottomSheetBluetoothFragment;


    private void checkSMS(){
        try{
            if (!(Utils.isPermission(Manifest.permission.SEND_SMS))){
                confirmBottomSheetFragment = new ConfirmBottomSheetFragment("알림", "문자 보내기 권한이 필요합니다.", false,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    confirmBottomSheetFragment.dismiss();
                                    try{
                                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                                        Manifest.permission.SEND_SMS},
                                                1001);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    confirmBottomSheetFragment = null;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                confirmBottomSheetFragment.show(getSupportFragmentManager(), confirmBottomSheetFragment.getTag());
            }else{
                startActivity(new Intent(getBaseContext(),SettingActivity.class));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static long time =0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            try {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            time = 0;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 50);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            finishAffinity();
                            System.runFinalization();
                            System.exit(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 300);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try{
            if(requestCode == 1000){
                try{
                    if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                        connectionBottomSheetBluetoothFragment = new ConnectionBottomSheetBluetoothFragment();
                        connectionBottomSheetBluetoothFragment.show(getSupportFragmentManager(),connectionBottomSheetBluetoothFragment.getTag());

                        new GPS_Protocol().googleGpsListener(getBaseContext());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if(requestCode == 1001){
                try{
                    if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                        startActivity(new Intent(getBaseContext(),SettingActivity.class));

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}