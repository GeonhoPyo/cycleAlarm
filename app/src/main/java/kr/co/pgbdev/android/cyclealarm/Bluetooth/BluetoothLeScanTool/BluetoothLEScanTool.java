package kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLeScanTool;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLEController;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.ConnectState;
import kr.co.pgbdev.android.cyclealarm.Fragment.Beacon.ConnectionBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.Fragment.Bluetooth.ConnectionBottomSheetBluetoothFragment;
import kr.co.pgbdev.android.cyclealarm.Fragment.Bluetooth.ScanBluetoothAdapter;
import kr.co.pgbdev.android.cyclealarm.Phone.ContackShared;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;

public class BluetoothLEScanTool {


    public void refreshScanBluetooth(Context context){
        try{
            ConnectionBottomSheetBluetoothFragment.refreshScanRecyclerView(new ArrayList<>());
            BluetoothLEController.initRxBleClient();
            scanBluetooth(context);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static Disposable scanDisposable = null;
    public void scanBluetooth(Context context){
        try{
            if(scanDisposable != null){
                scanStop();
            }

            new ConnectionBottomSheetBluetoothFragment().startScanAnimation();

            ArrayList<BluetoothInfo> scanBluetoothArrayList = new ArrayList<>();

            if(ConnectState.connectSuccessBluetoothInfo != null){
                BluetoothInfo connectedBluetoothInfo = ConnectState.connectSuccessBluetoothInfo;
                connectedBluetoothInfo.connectState = "PAIRED";
                Map<String,String> pairedMap = new ContackShared().getPairedBluetoothList(context);
                if(pairedMap.containsKey(connectedBluetoothInfo.bluetoothMacAddress)){
                    connectedBluetoothInfo.pairedDate = pairedMap.get(connectedBluetoothInfo.bluetoothMacAddress);
                }
                scanBluetoothArrayList.add(ConnectState.connectSuccessBluetoothInfo);
                ConnectionBottomSheetBluetoothFragment.refreshScanRecyclerView(scanBluetoothArrayList);
            }


            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
                        RxBleClient rxBleClient = BluetoothLEController.getRxBleClient(context);
                        scanDisposable = rxBleClient.scanBleDevices(
                                new ScanSettings.Builder()
                                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                        .build()
                        ).subscribe(
                                scanResult -> {
                                    RxBleDevice device = scanResult.getBleDevice();

                                    if(device != null && device.getName() != null && device.getMacAddress() != null){
                                        BluetoothInfo bluetoothInfo = new BluetoothInfo(device.getName(), device.getMacAddress(),"NONE",null);
                                        Dlog.e("BluetoothInfo : " + bluetoothInfo);
                                        ArrayList<BluetoothInfo> scanBluetoothArrayList = ScanBluetoothAdapter.getScanBluetoothInfoArrayList();
                                        if(!ScanBluetoothAdapter.getScanBluetoothInfoArrayList().contains(bluetoothInfo)){
                                            Map<String,String> pairedMap = new ContackShared().getPairedBluetoothList(context);
                                            if(pairedMap ==null){
                                                pairedMap = new LinkedHashMap<>();
                                            }
                                            if(pairedMap.containsKey(bluetoothInfo.bluetoothMacAddress)){
                                                bluetoothInfo.pairedDate = pairedMap.get(bluetoothInfo.bluetoothMacAddress);
                                            }

                                            scanBluetoothArrayList.add(bluetoothInfo);
                                            ConnectionBottomSheetBluetoothFragment.refreshScanRecyclerView(scanBluetoothArrayList);
                                        }
                                    }

                                },
                                throwable -> {
                                    scanStop();
                                }
                        );
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },300);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void scanStop(){
        try{
            if(scanDisposable != null){
                scanDisposable.dispose();
                scanDisposable = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(ConnectionBottomSheetFragment.viewHandler != null){
            ConnectionBottomSheetFragment.viewHandler.obtainMessage(4).sendToTarget();
        }

    }

}
