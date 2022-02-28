package kr.co.pgbdev.android.cyclealarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import kr.co.pgbdev.android.cyclealarm.Fragment.ConfirmBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.Fragment.ConnectionBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;
import kr.co.pgbdev.android.cyclealarm.Tool.Utils;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    TextView tv_bluetoothName;
    ImageView iv_bluetooth;
    ImageView iv_setting;

    TextView tv_beacon_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //Utils 초기화
        Utils.init(getBaseContext());

        checkPermission();
        initBeacon();
    }

    private void initView(){
        try{
            tv_bluetoothName = findViewById(R.id.tv_bluetoothName);
            iv_bluetooth = findViewById(R.id.iv_bluetooth);
            iv_setting = findViewById(R.id.iv_setting);

            tv_beacon_test = findViewById(R.id.tv_beacon_test);
            iv_bluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        //BottomSheet - Connection

                        //new CheckPermission().checkPermission(getBaseContext(),getSupportFragmentManager());
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    BeaconManager beaconManager;
    private void initBeacon(){
        try{
            if ((Utils.isPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    Utils.isPermission(Manifest.permission.ACCESS_COARSE_LOCATION)&&
                    Utils.isPermission(Manifest.permission.BLUETOOTH_ADMIN) )){
                beaconManager = BeaconManager.getInstanceForApplication(this);
                beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
                //beaconManager 설정 bind
                beaconManager.bindInternal(this);

                handler.sendEmptyMessage(0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList<Beacon> beaconList = new ArrayList<>();
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                }
            }

        });

        try {
            beaconManager.startRangingBeacons(new Region("myRangingUniqueId", null, null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(beaconManager != null){
                beaconManager.unbindInternal(this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    int cnt = 0;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            cnt ++;

            tv_beacon_test.setText("scanning("+cnt+")");


            // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
            for(Beacon beacon : beaconList){
                int txPower = beacon.getTxPower();
                String uuid=beacon.getId1().toString(); //beacon uuid
                int major = beacon.getId2().toInt(); //beacon major
                int minor = beacon.getId3().toInt();// beacon minor
                String address = beacon.getBluetoothAddress();

                tv_beacon_test.append("uuid : " + uuid +" , major : " + major +" , minor : " + minor + " , txPower : "+ txPower + ", address : "+ address);

                /*if(major==40001){
                    //beacon 의 식별을 위하여 major값으로 확인
                    //이곳에 필요한 기능 구현
                    //textView.append("ID 1 : " + beacon.getId2() + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");

                    tv_beacon_test.append("Beacon Bluetooth Id : "+address+"\n");
                    tv_beacon_test.append("Beacon UUID : "+uuid+"\n");

                }else{
                    //나머지 비콘검색
                    tv_beacon_test.append("ID 2: " + beacon.getId2() + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
                }*/

            }

            // 자기 자신을 1초마다 호출
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    ConfirmBottomSheetFragment confirmBottomSheetFragment = null;
    private void checkPermission(){
        try{
            Dlog.e("test 1111");

            Dlog.e("Utils.isPermission(Manifest.permission.ACCESS_FINE_LOCATION) : " + Utils.isPermission(Manifest.permission.ACCESS_FINE_LOCATION));
            Dlog.e("Utils.isPermission(Manifest.permission.ACCESS_COARSE_LOCATION) : " + Utils.isPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
            Dlog.e("Utils.isPermission(Manifest.permission.BLUETOOTH_SCAN) : " + Utils.isPermission(Manifest.permission.BLUETOOTH_SCAN));

            if (!(Utils.isPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    Utils.isPermission(Manifest.permission.ACCESS_COARSE_LOCATION)||
                    Utils.isPermission(Manifest.permission.BLUETOOTH_SCAN))){
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
                                                            Manifest.permission.BLUETOOTH_SCAN},
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
                /*ConnectionBottomSheetFragment connectionBottomSheetFragment = new ConnectionBottomSheetFragment();
                connectionBottomSheetFragment.show(getSupportFragmentManager(), connectionBottomSheetFragment.getTag());*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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
                        initBeacon();
                        /*ConnectionBottomSheetFragment connectionBottomSheetFragment = new ConnectionBottomSheetFragment();
                        connectionBottomSheetFragment.show(getSupportFragmentManager(), connectionBottomSheetFragment.getTag());*/
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

    final String uuid = "01 12 23 34 45 56 67 78 89 9A AB BC CD DE EF F0";
    private void beaconMassage(){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}