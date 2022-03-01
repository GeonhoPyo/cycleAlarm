package kr.co.pgbdev.android.cyclealarm.Tool;

public class StateData {

    public static double longitude;
    public static double latitude;

    public static void setLongitude(double longitude){
        StateData.longitude = longitude;
    }
    public static double getLongitude(){
        return StateData.longitude;
    }
    public static void setLatitude(double latitude){
        StateData.latitude = latitude;
    }
    public static double getLatitude(){
        return StateData.latitude;
    }
}
