package kr.co.pgbdev.android.cyclealarm.Tool;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import kr.co.pgbdev.android.cyclealarm.GPS.GPSData;

public class AlarmState {

    private static boolean pushMSG = false;

    public void alarmStart(Context context){
        try{
            //phone number
            String number = ContackShared.getNumber(context);

            if(number != null){
                //location
                double longitude = GPSData.getLongitude();
                double latitude = GPSData.getLatitude();
                if(longitude != 0 && latitude != 0){ //위치 정보가 수신된 경우
                    if(!pushMSG){
                        String message = "https://www.google.com/maps/search/"+latitude+","+longitude+"/data=!4m2!2m1!4b1?hl=ko&nogmmr=1";
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null,message , null, null);
                        Toast.makeText(context, "SEND !! number : " + number + " , message : " + message, Toast.LENGTH_SHORT).show();
                        pushMSG = true;
                    }

                }else { //위치 정보가 수신되지 않은 경우
                    //Toast.makeText(context, "위치정보가 수신되지 않았습니다", Toast.LENGTH_SHORT).show();
                }
            }else {
                //Toast.makeText(context, "전화번호가 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
