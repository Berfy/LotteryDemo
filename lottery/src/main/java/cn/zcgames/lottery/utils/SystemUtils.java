package cn.zcgames.lottery.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;

import cn.zcgames.lottery.app.MyApplication;

/**
 * Created by admin on 2017/5/5.
 */

public class SystemUtils {

    private static final String TAG = "SystemUtils";

    //屏幕密度的值
    public static int DENSITY_LOW = 120;
    public static int DENSITY_MEDIUM = 160;  //默认值
    public static int DENSITY_TV = 213;      //TV专用
    public static int DENSITY_HIGH = 240;
    public static int DENSITY_XHIGH = 320;
    public static int DENSITY_356 = 356;
    public static int DENSITY_400 = 400;
    public static int DENSITY_XXHIGH = 480;
    public static int DENSITY_XXXHIGH = 640;

    /**
     * 获取系统的密度
     */
    public static DisplayMetrics getDensity() {
        DisplayMetrics displayMetrics = MyApplication.getAppContext().getResources().getDisplayMetrics();
        Log.d(TAG, "Density is " + displayMetrics.density +
                " densityDpi is " + displayMetrics.densityDpi +
                " height: " + displayMetrics.heightPixels +
                " width: " + displayMetrics.widthPixels);
        return displayMetrics;
    }

    /**
     * 获取app的版本号
     *
     * @return
     */
    public static String getAppVersionName() {
        String pkName = MyApplication.getAppContext().getPackageName();
        try {
            return MyApplication.getAppContext().getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取app的版本
     *
     * @return
     */
    public static int getAppVersionCode() {
        String pkName = MyApplication.getAppContext().getPackageName();
        try {
            return MyApplication.getAppContext().getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取屏幕状态栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
