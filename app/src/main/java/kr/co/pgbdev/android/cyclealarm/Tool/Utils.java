package kr.co.pgbdev.android.cyclealarm.Tool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.core.app.ActivityCompat;

public abstract class Utils {
    private static DisplayMetrics mMetrics;
    private static Context context;

    /**
     * initialize method, called inside the Chart.init() method.
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static void init(Context context) {

        try{
            if(context != null){
                Utils.context = context;
                Resources res = context.getResources();
                Utils.mMetrics = res.getDisplayMetrics();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * initialize method, called inside the Chart.init() method. backwards
     * compatibility - to not break existing code
     *
     * @param res
     */
    @Deprecated
    public static void init(Resources res) {

        mMetrics = res.getDisplayMetrics();

    }

    public static float convertDpToPixel(float dp) {

        if (mMetrics == null) {

            return dp;
        }

        return dp * mMetrics.density;
    }

    public static float convertPixelsToDp(float px) {

        if (mMetrics == null) {

            return px;
        }

        return px / mMetrics.density;
    }

    public static boolean isPermission(String strPermission){
        try{
            if(context != null){
                return ActivityCompat.checkSelfPermission(context, strPermission) == PackageManager.PERMISSION_GRANTED;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
