package kr.co.pgbdev.android.cyclealarm.Tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.multidex.MultiDexApplication;

public class BaseApplication extends MultiDexApplication {
    public static boolean DEBUG = false;

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private static Context instance = null;


    @Override
    public void onCreate() {
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerApplication());

        super.onCreate();
        if (instance == null){
            instance = getApplicationContext();
        }
        this.DEBUG = isDebuggable(this);
    }

    private boolean isDebuggable(Context context){
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        return debuggable;
    }

    class UncaughtExceptionHandlerApplication implements Thread.UncaughtExceptionHandler{

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            //예외처리를 하지 않고 DefaultUncaughtException으로 넘긴다.
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        }

    }
}
