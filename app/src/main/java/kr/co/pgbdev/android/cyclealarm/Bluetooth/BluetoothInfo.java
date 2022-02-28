package kr.co.pgbdev.android.cyclealarm.Bluetooth;

import java.util.Objects;

public class BluetoothInfo {
    public String bluetoothName ;
    public String bluetoothMacAddress ;
    public String connectState ;
    public String pairedDate ;

    public BluetoothInfo(String bluetoothName, String bluetoothMacAddress, String connectState, String pairedDate){
        this.bluetoothName = bluetoothName;
        this.bluetoothMacAddress = bluetoothMacAddress;
        this.connectState = connectState;
        this.pairedDate = pairedDate;
    }

    @Override
    public String toString() {
        return "BluetoothInfo{" +
                "bluetoothName='" + bluetoothName + '\'' +
                ", bluetoothMacAddress='" + bluetoothMacAddress + '\'' +
                ", connectState='" + connectState + '\'' +
                ", pairedDate='" + pairedDate + '\'' +
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
