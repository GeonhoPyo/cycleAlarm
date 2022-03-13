package kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

import com.polidea.rxandroidble2.RxBleConnection;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import kr.co.pgbdev.android.cyclealarm.Connection.ResponseProtocol;
import kr.co.pgbdev.android.cyclealarm.Tool.Dlog;

public class BluetoothLEIOController {
    /**
     * -----------------------------------------------------------
     * connectionObservable 관리
     * */
    private static Observable<RxBleConnection> connectionObservable;
    public static void setConnectionObservable(Observable<RxBleConnection> connectionObservable) {
        BluetoothLEIOController.connectionObservable = connectionObservable;
    }
    /**
     * -----------------------------------------------------------
     * Notification 관리
     * */
    private static Disposable notificationData = null;
    public void getData() {
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
        notificationData = connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(BluetoothLEConnection.getNotifyCharacteristic().getUuid()))
                .flatMap(notificationObservable -> notificationObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNotificationReceived,
                        this::onNotificationSetupFailure);
    }
    public void notificationDispose(){
        try{
            if(notificationData != null ){
                Dlog.e("notificationData.dispose()");
                notificationData.dispose();
                notificationData = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onNotificationReceived(byte[] bytes) {
        //Dlog.e("Response : " + new ProtocolTool().byteArrayToHex(bytes));
        //Dlog.e("onNotificationReceived : " + Arrays.toString(bytes));
        new ResponseProtocol().bleTestDataRead(bytes);
    }
    public void onNotificationSetupFailure(Throwable throwable) {
        Dlog.e("onNotificationSetupFailure !!! throwable : " +throwable);
        ConnectState.setConnecting(false);
    }
    /**
     * -----------------------------------------------------------
     * Write 관리
     * */
    public void dataWrite(byte[] data){
        if(ConnectState.isConnectSuccess()){
            setWrite(BluetoothLEConnection.getWriteCharacteristic(),data);

        }else{
            Dlog.e("Not Connect");
        }
    }
    public void strDataWrite(String data){
        if(ConnectState.isConnectSuccess()){
            setWrite(BluetoothLEConnection.getWriteCharacteristic(),(data+"\r\n").getBytes());
        }else{
            Dlog.e("Not Connect");
        }
    }
    private static Disposable writeData = null;
    public void setWrite(BluetoothGattCharacteristic characteristic, byte[] write) {
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
        writeData = connectionObservable
                .firstOrError()
                .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(characteristic.getUuid(),write))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bytes -> onWriteSuccess(),
                        this::onWriteFailure
                );
    }
    public void writeDispose(){
        try{
            if(writeData != null ){
                Dlog.e("writeData.dispose()");
                writeData.dispose();
                writeData= null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onWriteSuccess() {
    }
    private void onWriteFailure(Throwable throwable) {

    }
}
