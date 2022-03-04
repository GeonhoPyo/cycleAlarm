package kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLeScanTool;

import android.Manifest;
import android.content.Context;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanSettings;

import io.reactivex.plugins.RxJavaPlugins;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLEController;
import kr.co.pgbdev.android.cyclealarm.Phone.ContackShared;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;
import kr.co.pgbdev.android.cyclealarm.Tool.Utils;

public class BluetoothLEAutoScanTool {

    public void startAutoConnect(Context context){
        try{
            String connectBluetoothMacAddress = ContackShared.getConnectBluetoothMacAddress(context);
            Dlog.e("connectBluetoothMacAddress : " + connectBluetoothMacAddress);
            if(connectBluetoothMacAddress != null){
                if ((Utils.isPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        Utils.isPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        Utils.isPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION))){
                    if(BluetoothLEScanTool.scanDisposable != null){
                        scanStop();
                    }

                    RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
                    RxBleClient rxBleClient = BluetoothLEController.getRxBleClient(context);
                    BluetoothLEScanTool.scanDisposable = rxBleClient.scanBleDevices(
                            new ScanSettings.Builder()
                                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                    .build()
                    ).subscribe(
                            scanResult -> {
                                RxBleDevice device = scanResult.getBleDevice();
                                if(device != null && device.getName() != null && device.getMacAddress() != null){
                                    BluetoothInfo bluetoothInfo = new BluetoothInfo(device.getName(), device.getMacAddress(),"NONE",null);
                                    if(connectBluetoothMacAddress.equals(bluetoothInfo.bluetoothMacAddress)){
                                        scanStop();
                                        new BluetoothLEController().connect(bluetoothInfo);
                                    }
                                }

                            },
                            throwable -> {
                                scanStop();
                            }
                    );
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void scanStop(){
        try{
            if(BluetoothLEScanTool.scanDisposable != null){
                BluetoothLEScanTool.scanDisposable.dispose();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
