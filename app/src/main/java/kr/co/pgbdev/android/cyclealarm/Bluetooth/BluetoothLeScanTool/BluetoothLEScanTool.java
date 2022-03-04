package kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLeScanTool;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothInfo;
import kr.co.pgbdev.android.cyclealarm.Bluetooth.BluetoothLEController;
import kr.co.pgbdev.android.cyclealarm.Fragment.Beacon.ConnectionBottomSheetFragment;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;

public class BluetoothLEScanTool {


    public void refreshScanBluetooth(Context context){
        try{
            //ConnectionBottomSheetFragment.refreshScanRecyclerView(new ArrayList<>());
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

            if(ConnectionBottomSheetFragment.viewHandler != null){
                ConnectionBottomSheetFragment.viewHandler.obtainMessage(3).sendToTarget();
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
