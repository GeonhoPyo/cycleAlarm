package kr.co.pgbdev.android.cyclealarm.Tool;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import kr.co.pgbdev.android.cyclealarm.Activity.MainActivity;


/**
 * Created by user on 2017-06-08.
 */

public class GPS_Protocol {

    private static LocationRequest request ;
    private static LocationCallback callback ;
    private static FusedLocationProviderClient client;
    private static Context context;
    private static int accuracy;

    public void googleGpsListener(Context context) {
        //Dlog.e("googleGpsListener");
        GPS_Protocol.context = context;
        String distance = ContackShared.getDistance(context);
        setAccuracy(distance);



        createLocationRequest();
        createLocationCallback();

        client = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        client.requestLocationUpdates(request, callback, Looper.myLooper());
    }

    public static void setAccuracy(String distance){
        try{
            GPS_Protocol.accuracy = Integer.parseInt(distance.replaceAll("m",""));
        }catch (Exception e){
            e.printStackTrace();
            GPS_Protocol.accuracy = 100;
        }

    }


    private void createLocationRequest(){
        //Dlog.e("createLocationRequest");
        request = LocationRequest.create();
        request.setInterval(0);
        request.setMaxWaitTime(0);
        request.setFastestInterval(0);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback(){
        //Dlog.e("createLocationCallback");
        if (callback != null){
            client.removeLocationUpdates(callback);
        }
        callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                try{
                    Location location = locationResult.getLastLocation();

                    if(location.getAccuracy() <= GPS_Protocol.accuracy){
                        Handler viewHandler = MainActivity.viewHandler;
                        if(viewHandler != null){
                            viewHandler.obtainMessage(5).sendToTarget();
                        }
                        Dlog.e("longitude : " + location.getLongitude() + " , getLatitude : " + location.getLatitude());
                        StateData.setLongitude(location.getLongitude());
                        StateData.setLatitude(location.getLatitude());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
    }
}
