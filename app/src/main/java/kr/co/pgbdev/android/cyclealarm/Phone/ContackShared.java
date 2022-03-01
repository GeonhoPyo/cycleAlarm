package kr.co.pgbdev.android.cyclealarm.Phone;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.jar.Attributes;

public class ContackShared {

    static final String Name = "Name";
    static final String Number = "Number";
    static final String MacAddress = "MacAddress";

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

    public static void setConnectBluetoothMacAddress(Context context, String mac){
        getEditor(context).putString(MacAddress,mac).apply();
    }

    public static String getConnectBluetoothMacAddress(Context context){
        return getInstance(context).getString(MacAddress, null);
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
}
