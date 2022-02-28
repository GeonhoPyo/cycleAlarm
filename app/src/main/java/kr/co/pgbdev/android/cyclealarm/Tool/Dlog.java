package kr.co.pgbdev.android.cyclealarm.Tool;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


public class Dlog {
    static final String TAG = "cycleAlarm";


    /** Log Level Error **/
    public static final <T> void e(T message) {
        Log.e(TAG, buildLogMsg(String.valueOf(message)));
    }

    public static String buildLogMsg(String message) {

        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

        StringBuilder sb = new StringBuilder();

        try {
            sb.append("[")
                    .append(ste.getMethodName())
                    .append("()")
                    .append("]")
                    .append(" :: ")
                    .append(message)
                    .append(" (")
                    .append(ste.getFileName())
                    .append(":")
                    .append(ste.getLineNumber())
                    .append(")");
        }catch (Exception e){
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String logToString = new TimeTool().getRealTime()+"/"+writer.toString();

            sb.append("[")
                    .append(ste.getMethodName())
                    .append("()")
                    .append("]")
                    .append(" :: ")
                    .append(logToString)
                    .append(" (")
                    .append(ste.getFileName())
                    .append(":")
                    .append(ste.getLineNumber())
                    .append(")");
        }

        return sb.toString();

    }
}
