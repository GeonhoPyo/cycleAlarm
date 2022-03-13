package kr.co.pgbdev.android.cyclealarm.Connection.Bluetooth;

import java.util.Objects;

public class BluetoothInfo {
    public String bluetoothName ;
    public String bluetoothMacAddress ;
    public String connectState ;

    public BluetoothInfo(String bluetoothName, String bluetoothMacAddress, String connectState){
        this.bluetoothName = bluetoothName;
        this.bluetoothMacAddress = bluetoothMacAddress;
        this.connectState = connectState;
    }

    @Override
    public String toString() {
        return "BluetoothInfo{" +
                "bluetoothName='" + bluetoothName + '\'' +
                ", bluetoothMacAddress='" + bluetoothMacAddress + '\'' +
                ", connectState='" + connectState + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothInfo bluetoothInfo = (BluetoothInfo) o;
        return Objects.equals(bluetoothName, bluetoothInfo.bluetoothName) &&
                Objects.equals(bluetoothMacAddress, bluetoothInfo.bluetoothMacAddress);
    }
}
