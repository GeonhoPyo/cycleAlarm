package kr.co.pgbdev.android.cyclealarm.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;

import com.jakewharton.rx.ReplayingShare;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import kr.co.pgbdev.android.cyclealarm.MainActivity;
import kr.co.pgbdev.android.cyclealarm.Phone.ContackShared;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;

public class BluetoothLEController {

    //client
    private static RxBleClient rxBleClient = null;
    public static RxBleClient getRxBleClient(Context context){
        try{
            if(rxBleClient == null){
                rxBleClient = RxBleClient.create(context);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return rxBleClient;
    }

    //init Client
    public static void initRxBleClient(){
        rxBleClient = null;
    }

    //connect

    private static Disposable connectDisposable = null;
    private static RxBleDevice rxBleDevice = null;
    public void connect(BluetoothInfo bluetoothInfo){
        try{
            Dlog.e("Connect BluetoothInfo : " + bluetoothInfo);
            if(!ConnectState.isConnecting()){
                ConnectState.setConnecting(true);
                if(connectDisposable != null){
                    connectDisposable.dispose();
                    connectDisposable = null;
                }
                rxBleDevice = rxBleClient.getBleDevice(bluetoothInfo.bluetoothMacAddress);
                BluetoothLEIOController.setConnectionObservable(prepareConnectionObservable());

                ConnectState.setConnectTryBluetoothInfo(bluetoothInfo);
                connectDisposable = rxBleDevice.establishConnection(false) // <-- autoConnect flag
                        .flatMapSingle(RxBleConnection::discoverServices)
                        .take(1)
                        .timeout(2000, TimeUnit.MILLISECONDS)
                        .takeUntil(disconnectTriggerSubject)
                        .compose(ReplayingShare.instance())
                        .retryWhen(new RetryWithDelay(200, 100))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                BluetoothLEController::swapScanResult,
                                BluetoothLEController::onConnectionFailure
                        );
            }else{
                //연결중..
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Bluetooth connect success
    private static BluetoothGattCharacteristic notifyCharacteristic = null;
    private static BluetoothGattCharacteristic writeCharacteristic = null;
    public static void swapScanResult(RxBleDeviceServices services) {
        Dlog.e("swapScanResult");
        ConnectState.setConnecting(false);
        StringBuilder strUUID = new StringBuilder();
        ArrayList<BluetoothGattCharacteristic> notifyArray = new ArrayList<BluetoothGattCharacteristic>();
        ArrayList<BluetoothGattCharacteristic> writeArray = new ArrayList<BluetoothGattCharacteristic>();
        for (BluetoothGattService service : services.getBluetoothGattServices()) {
            Dlog.e("service -------------------------------------");
            strUUID.append("service -------------------------------------\n");
            final List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                String describeProperties = describeProperties(characteristic);
                Dlog.e("describeProperties : " +  describeProperties+" , getUuid : "+ characteristic.getUuid());
                strUUID.append("describeProperties : ").append(describeProperties).append(" , getUuid : ").append(characteristic.getUuid()).append("\n");
                if(describeProperties.contains("Write")){
                    writeArray.add(characteristic);
                    writeCharacteristic = characteristic;
                }
                if(describeProperties.contains("Notify")){
                    notifyArray.add(characteristic);
                    notifyCharacteristic = characteristic;
                }
            }
        }

        BluetoothLEController.bluetoothConnectSuccess();

    }


    private static void bluetoothConnectSuccess(){
        try{
            Dlog.e("bluetoothConnectSuccess");

            BluetoothInfo connectTryBluetoothInfo = ConnectState.getConnectTryBluetoothInfo();
            connectTryBluetoothInfo.connectState = "PAIRED";
            ConnectState.connectSuccessBluetoothInfo = connectTryBluetoothInfo;
            ConnectState.setConnectSuccess(true);
            new BluetoothLEIOController().getData();

            if(MainActivity.mainContext != null){
                ContackShared.setConnectBluetoothAddress(MainActivity.mainContext,connectTryBluetoothInfo.bluetoothMacAddress);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Bluetooth connect fail
    public static int failCount = 1;
    public static boolean onConnectionFailure(Throwable throwable) {
        try{

            Dlog.e("throwable : " + throwable);

            ConnectState.connectSuccessBluetoothInfo = null;
            ConnectState.setConnecting(false);
            if(failCount >= 200){
                //ConnectionBottomSheetFragment.connectFail(ConnectState.getConnectTryBluetoothInfo());
                return false;
            }
            Dlog.e("failCount : " + failCount);
            failCount ++;
            BluetoothInfo connectTryBluetoothInfo = ConnectState.getConnectTryBluetoothInfo();
            if(connectTryBluetoothInfo.bluetoothMacAddress != null && !ConnectState.isConnectSuccess() && ConnectState.isConnecting()){ //연결 안됫을때.
                return true;
            }else {
                ConnectState.setConnectSuccess(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //ConnectionBottomSheetFragment.connectFail(ConnectState.getConnectTryBluetoothInfo());


        return false;
    }




    //Bluetooth Object
    public static BluetoothGattCharacteristic getNotifyCharacteristic (){
        return notifyCharacteristic;
    }
    public static BluetoothGattCharacteristic getWriteCharacteristic(){
        return writeCharacteristic;
    }

    //Bluetooth Connection Finish
    public void connectFinish(){
        try{
            ConnectState.connectSuccessBluetoothInfo= null;
            new BluetoothLEIOController().notificationDispose();
            new BluetoothLEIOController().writeDispose();
            if(connectDisposable != null){
                connectDisposable.dispose();
                connectDisposable = null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private final PublishSubject<Boolean> disconnectTriggerSubject = PublishSubject.create();
    private Observable<RxBleConnection> prepareConnectionObservable() {
        return rxBleDevice
                .establishConnection(true)
                .takeUntil(disconnectTriggerSubject)
                .compose(ReplayingShare.instance());
    }


    public static class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
        private final int maxRetries;
        private final int retryDelayMillis;
        private int retryCount;

        public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
            this.maxRetries = maxRetries;
            this.retryDelayMillis = retryDelayMillis;
            this.retryCount = 0;
        }

        @Override
        public Observable<?> apply(final Observable<? extends Throwable> attempts) {
            return attempts
                    .flatMap(new Function<Throwable, Observable<?>>() {
                        @Override
                        public Observable<?> apply(final Throwable throwable) {
                            if (++retryCount < maxRetries && ConnectState.isConnecting()) {
                                return Observable.timer(retryDelayMillis,
                                        TimeUnit.MILLISECONDS);
                            }
                            // Max retries hit. Just pass the error along.
                            return Observable.error(throwable);
                        }
                    });
        }
    }

    /**
     * -----------------------------------------------------------
     * Characteristic 관리
     * */
    private static String describeProperties(BluetoothGattCharacteristic characteristic) {
        List<String> properties = new ArrayList<>();
        if (isCharacteristicReadable(characteristic)) properties.add("Read");
        if (isCharacteristicWriteable(characteristic)) properties.add("Write");
        if (isCharacteristicNotifiable(characteristic)) properties.add("Notify");
        return TextUtils.join(" ", properties);
    }

    private static boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }

    private static boolean isCharacteristicReadable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    private static boolean isCharacteristicWriteable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE
                | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }
}
