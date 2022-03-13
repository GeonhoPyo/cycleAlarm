package kr.co.pgbdev.android.cyclealarm.Tool;

import android.content.Context;
import android.content.SharedPreferences;

public class ContackShared {

    static final String Name = "Name";
    static final String Number = "Number";
    static final String beaconAddress = "MacAddress";
    static final String BluetoothMacAddress = "BluetoothMacAddress";

    public static SharedPreferences getInstance(Context context){

        return context.getApplicationContext().getSharedPreferences("Setting", Context.MODE_PRIVATE);
    }
    private static SharedPreferences.Editor getEditor(Context context){

        return getInstance(context.getApplicationContext()).edit();
    }

    //name
    public static void setName(Context context, String name){
        getEditor(context).putString(Name, name).apply();
    }
    public static String getName(Context context){
        return getInstance(context).getString(Name, null);
    }

    //number
    public static void setNumber(Context context, String name){
        getEditor(context).putString(Number, name).apply();
    }
    public static String getNumber(Context context){
        return getInstance(context).getString(Number, null);
    }

    public static void setConnectBeaconAddress(Context context, String mac){
        getEditor(context).putString(beaconAddress,mac).apply();
    }
    public static String getConnectBeaconAddress(Context context){
        return getInstance(context).getString(beaconAddress, null);
    }

    public static void setConnectBluetoothAddress(Context context, String macAddress){
        getEditor(context).putString(BluetoothMacAddress,macAddress).apply();
    }

    public static String getConnectBluetoothAddress(Context context){
        return getInstance(context).getString(BluetoothMacAddress,null);
    }

    public static void setMajor (Context context, int major){
        getEditor(context).putInt("Major",major).apply();
    }
    public static int getMajor (Context context){
        return getInstance(context).getInt("Major",-1);
    }

    public static void setDistance(Context context, String distance){
        getEditor(context).putString("Distance",distance).apply();
    }
    public static String getDistance(Context context){
        return getInstance(context).getString("Distance","100m");
    }


    //BLE VERSION
    public static void setBLEVersion(Context context, boolean bleVersion){
        getEditor(context).putBoolean("BLEVERSION",bleVersion).apply();
    }
    public static boolean getBLEVersion(Context context){
        return getInstance(context).getBoolean("BLEVERSION",true);
    }
}
