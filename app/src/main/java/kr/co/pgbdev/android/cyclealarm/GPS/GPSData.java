package kr.co.pgbdev.android.cyclealarm.GPS;

public class GPSData {

    public static double longitude;
    public static double latitude;

    public static void setLongitude(double longitude){
        GPSData.longitude = longitude;
    }
    public static double getLongitude(){
        return GPSData.longitude;
    }
    public static void setLatitude(double latitude){
        GPSData.latitude = latitude;
    }
    public static double getLatitude(){
        return GPSData.latitude;
    }
}
