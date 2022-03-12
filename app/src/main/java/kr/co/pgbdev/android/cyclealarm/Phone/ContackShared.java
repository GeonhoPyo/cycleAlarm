package kr.co.pgbdev.android.cyclealarm.Phone;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedHashMap;
import java.util.Map;

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

    //Paired Bluetooth List
    private final String PairedBluetoothMap = "PairedBluetoothMap";
    public void setPairedBluetoothMap(Context context, String macAddress, String strDate){
        Map<String,String> pairedBluetoothMap = getPairedBluetoothList(context);
        if(pairedBluetoothMap==null){
            pairedBluetoothMap = new LinkedHashMap<>();
        }
        pairedBluetoothMap.put(macAddress,strDate);

        Gson gson = new Gson();
        String strPairedBluetoothArrayList = gson.toJson(pairedBluetoothMap);
        getEditor(context).putString(PairedBluetoothMap,strPairedBluetoothArrayList).apply();
    }

    public Map<String,String> getPairedBluetoothList(Context context){
        String strGson = getInstance(context).getString(PairedBluetoothMap,null);
        if(strGson != null){
            java.lang.reflect.Type type = new TypeToken<Map<String,String>>(){}.getType();
            Gson gson = new Gson();
            Map<String,String> pairedBluetoothMap = gson.fromJson(strGson,type);
            if(pairedBluetoothMap != null){
                return pairedBluetoothMap;
            }else {
                return new LinkedHashMap<>();
            }
        }
        return null;
    }


    //BLE VERSION
    public static void setBLEVersion(Context context, boolean bleVersion){
        getEditor(context).putBoolean("BLEVERSION",bleVersion).apply();
    }
    public static boolean getBLEVersion(Context context){
        return getInstance(context).getBoolean("BLEVERSION",true);
    }
}
