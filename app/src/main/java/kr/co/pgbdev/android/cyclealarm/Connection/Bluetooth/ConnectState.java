package kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth;

public class ConnectState {

    public static String BluetoothTAG = ConnectState.BLE;
    public static final String BLUETOOTH = "BLUETOOTH";
    public static final String BLE = "BLE";

    public static BluetoothInfo connectSuccessBluetoothInfo;

    //Bluetooth connect state
    private static BluetoothInfo connectTryBluetoothInfo = null;
    public static void setConnectTryBluetoothInfo(BluetoothInfo connectTryBluetoothInfo){
        ConnectState.connectTryBluetoothInfo = connectTryBluetoothInfo;
    }
    public static BluetoothInfo getConnectTryBluetoothInfo(){
        return ConnectState.connectTryBluetoothInfo;
    }

    /**
     * 연결 완료인지 확인
     * */
    private static boolean connectSuccess = false;
    public static boolean isConnectSuccess(){
        return connectSuccess;
    }
    public static void setConnectSuccess(boolean connectSuccess){
        ConnectState.connectSuccess = connectSuccess;
    }


    /**
     * 연결중 확인
     * */
    private static boolean isConnecting = false;
    public static void setConnecting(boolean connecting){
        ConnectState.isConnecting = connecting;
    }
    public static boolean isConnecting(){
        return ConnectState.isConnecting;
    }
}
