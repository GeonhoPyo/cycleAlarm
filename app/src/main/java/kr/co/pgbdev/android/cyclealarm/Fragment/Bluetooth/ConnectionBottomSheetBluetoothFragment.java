package kr.co.pgbdev.android.cyclealarm.Fragment.Bluetooth;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLeScanTool.BluetoothLEScanTool;
import kr.co.pgbdev.android.cyclealarm.Fragment.Beacon.ScanBeaconAdapter;
import kr.co.pgbdev.android.cyclealarm.Fragment.ConfirmBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.R;

public class ConnectionBottomSheetBluetoothFragment extends BottomSheetDialogFragment {


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
        scanBluetooth();

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


    private static Handler viewHandler ;
    ConfirmBottomSheetFragment confirmBottomSheetFragment;
    private void initHandler(){
        try{
            viewHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    try{
                        switch (msg.what){
                            case 1 :
                                ArrayList<BluetoothInfo> scanBluetoothArrayList = (ArrayList<BluetoothInfo>)msg.obj;
                                if(scanBluetoothArrayList != null){
                                    Collections.sort(scanBluetoothArrayList, new Comparator<BluetoothInfo>() {
                                        @Override
                                        public int compare(BluetoothInfo o1, BluetoothInfo o2) {
                                            if(o1.pairedDate != null && o2.pairedDate != null){
                                                return o1.pairedDate.compareToIgnoreCase(o2.pairedDate);
                                            }
                                            return 1;

                                        }
                                    });
                                    scanBluetoothAdapter.setScanBluetoothInfoArrayList(scanBluetoothArrayList);
                                    scanBluetoothAdapter.notifyDataSetChanged();
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
                                BluetoothInfo bluetoothInfoSuccess = (BluetoothInfo)msg.obj;

                                connectStateChange(bluetoothInfoSuccess);
                                break;
                            case 6 :
                                BluetoothInfo bluetoothInfoFail = (BluetoothInfo)msg.obj;
                                connectStateChange(bluetoothInfoFail);

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

    private void scanBluetooth(){

        try{
            new BluetoothLEScanTool().scanBluetooth(getContext());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void refreshScanBluetooth(){
        try{
            new BluetoothLEScanTool().refreshScanBluetooth(getContext());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ScanBluetoothAdapter scanBluetoothAdapter;
    private void connectStateChange(BluetoothInfo bluetoothInfo){
        try{
            if(bluetoothInfo != null){
                if(bluetoothInfo.connectState.equals("PAIRED")){
                    scanBluetoothAdapter.setConnectBluetoothArray(ScanBluetoothAdapter.getScanBluetoothInfoArrayList(),bluetoothInfo);
                    rv_scan_bluetooth_list.setAdapter(scanBluetoothAdapter);
                    rv_scan_bluetooth_list.invalidate();
                }else if(bluetoothInfo.connectState.equals("FAIL")){
                    scanBluetoothAdapter.setConnectBluetoothArray(ScanBluetoothAdapter.getScanBluetoothInfoArrayList(),bluetoothInfo);
                    scanBluetoothAdapter.notifyDataSetChanged();
                    rv_scan_bluetooth_list.invalidate();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void refreshScanRecyclerView(ArrayList<BluetoothInfo> bluetoothInfoArrayList){
        if(viewHandler != null){
            viewHandler.obtainMessage(1,bluetoothInfoArrayList).sendToTarget();
        }
    }

    public void startScanAnimation(){
        if(viewHandler != null){
            viewHandler.obtainMessage(3).sendToTarget();
        }
    }

    public void stopScanAnimation(){
        if(viewHandler != null){
            viewHandler.obtainMessage(4).sendToTarget();
        }
    }

    public static void connectSuccess(BluetoothInfo bluetoothInfo){
        if(viewHandler != null){
            bluetoothInfo.connectState = "PAIRED";
            viewHandler.obtainMessage(5,bluetoothInfo).sendToTarget();
        }
    }

    public static void connectFail(BluetoothInfo bluetoothInfo){
        if(viewHandler != null){
            bluetoothInfo.connectState = "FAIL";
            viewHandler.obtainMessage(6,bluetoothInfo).sendToTarget();
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        new BluetoothLEScanTool().scanStop();
    }






}
