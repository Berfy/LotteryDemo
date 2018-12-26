package cn.zcgames.lottery.utils;

import cn.zcgames.lottery.app.MyApplication;

/**
 * Created by admin on 2017/4/12.
 */

public class StaticResourceUtils {

    /**
     * 根据id获取string字符串
     * @param strId
     * @return
     */
    public static String getStringResourceById(int strId) {
        return MyApplication.getAppContext().getResources().getString(strId);
    }

    /**
     * 根据colorId获取color
     * @param colorId
     * @return
     */
    public static int getColorResourceById(int colorId){
        return MyApplication.getAppContext().getResources().getColor(colorId);
    }
}
