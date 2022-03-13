package kr.co.pgbdev.android.cyclealarm.BottomSheetFragment.Beacon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import kr.co.pgbdev.android.cyclealarm.BottomSheetFragment.Notice.ConfirmBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.R;
import kr.co.pgbdev.android.cyclealarm.Tool.Utils;


public class ConnectionBottomSheetFragment extends BottomSheetDialogFragment implements BeaconConsumer {


    LinearLayout ll_bluetooth_refresh;
    RecyclerView rv_scan_bluetooth_list;

    static ImageView iv_bluetooth_scan_icon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        //View 초기화
        initView(view);
        //Handler 초기화
        initHandler();

        //Scan Bluetooth
        initBeacon();

        return view;
    }

    private ScanBeaconAdapter scanBeaconAdapter;

    private void initView(View view){
        try{

            ll_bluetooth_refresh = view.findViewById(R.id.ll_bluetooth_refresh);
            ll_bluetooth_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshScanBluetooth();
                }
            });
            rv_scan_bluetooth_list = view.findViewById(R.id.rv_scan_bluetooth_list);
            rv_scan_bluetooth_list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    v.onTouchEvent(event);
                    return true;
                }
            });

            iv_bluetooth_scan_icon = view.findViewById(R.id.iv_bluetooth_scan_icon);

            rv_scan_bluetooth_list.setLayoutManager(new LinearLayoutManager(getContext()));
            scanBeaconAdapter = new ScanBeaconAdapter(getContext(),new ArrayList<>());
            rv_scan_bluetooth_list.setAdapter(scanBeaconAdapter);
            rv_scan_bluetooth_list.invalidate();


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Handler viewHandler ;
    ConfirmBottomSheetFragment confirmBottomSheetFragment;
    private void initHandler(){
        try{
            viewHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    try{
                        switch (msg.what){
                            case 1 :
                                ArrayList<Beacon> scanBeaconArrayList = (ArrayList<Beacon>)msg.obj;
                                if(scanBeaconArrayList != null){
                                    scanBeaconAdapter.setBeaconArrayList(scanBeaconArrayList);
                                    scanBeaconAdapter.notifyDataSetChanged();
                                    rv_scan_bluetooth_list.invalidate();
                                }
                                break;

                            case 2 :
                                break;
                            case 3 : //start animation
                                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
                                iv_bluetooth_scan_icon.startAnimation(anim);
                                break;
                            case 4 : //stop animation
                                iv_bluetooth_scan_icon.clearAnimation();
                                break;

                            case 5 :

                                break;
                            case 6 :
                                BluetoothInfo bluetoothInfoFail = (BluetoothInfo)msg.obj;


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
    private void refreshScanBluetooth(){
        try{
            if(beaconManager != null){
                beaconManager.unbindInternal(this);
                beaconManager = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        initBeacon();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            if(beaconManager != null){
                beaconManager.unbindInternal(this);
            }
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
                beaconManager = BeaconManager.getInstanceForApplication(getContext());
                beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
                //beaconManager 설정 bind
                beaconManager.bindInternal(this);

                handler.sendEmptyMessage(0);

                Handler viewHandler = ConnectionBottomSheetFragment.viewHandler;
                if(viewHandler != null){
                    viewHandler.obtainMessage(3).sendToTarget();
                }
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {


            Handler viewHandler = ConnectionBottomSheetFragment.viewHandler;
            if(viewHandler != null){
                viewHandler.obtainMessage(1,beaconList).sendToTarget();
            }

            // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
            for(Beacon beacon : beaconList){
                int txPower = beacon.getTxPower();
                String uuid=beacon.getId1().toString(); //beacon uuid
                int major = beacon.getId2().toInt(); //beacon major
                int minor = beacon.getId3().toInt();// beacon minor
                String address = beacon.getBluetoothAddress();

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

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection connection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection connection, int mode) {
        return false;
    }
}
