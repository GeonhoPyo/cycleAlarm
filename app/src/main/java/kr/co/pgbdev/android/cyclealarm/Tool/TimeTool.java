package kr.co.pgbdev.android.cyclealarm.Tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeTool {

    public String getRealTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public String getPairedDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public int getDiffPairedDate(String pairedDate){
        int days = 0;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateText = simpleDateFormat.format(date);
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        try {
            Date todayDate = dataFormat.parse(todayDateText);
            Date drvDate = dataFormat.parse(pairedDate);
            long duration = todayDate.getTime() - drvDate.getTime();
            days =(int)((duration)/(1000*60*60*24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public String getLogMsecTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return simpleDateFormat.format(date);
    }
    public String getLogsecTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss:SSS");
        return simpleDateFormat.format(date);
    }
}
