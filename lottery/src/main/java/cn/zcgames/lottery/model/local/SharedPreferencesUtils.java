package cn.zcgames.lottery.model.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.utils.GsonUtils;

/**
 * 本地文件储存类
 *
 * @author NorthStar
 * @date 2018/9/3 11:30
 */
public class SharedPreferencesUtils {

    private static final String TAG = "SharedPreferencesUtils";

    private static final String SP_DATA_KEY_LOTTERY_DATA_BEAN = "LotteryDataBean";

    /**
     * 保存登录成功的用户信息
     *
     * @param user
     */
    public static void updateLoginUserInfo(UserBean user) {
        SharedPreferences preferences = MyApplication.getAppContext().getSharedPreferences(AppConstants.SPDATA_KEY_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String userStr = GsonUtils.getGsonInstance().toJson(user);
        LogF.d(TAG, "updateLoginUserInfo: userStr is " + userStr);
        editor.putString(AppConstants.SPDATA_KEY_USER, userStr);
        editor.apply();
        if (getLoginUserInfo() != null && !TextUtils.isEmpty(getLoginUserInfo().getPlayerId())) {
            LogF.d(TAG, "登录后更新的userId==>" + getLoginUserInfo().getPlayerId());
        }
    }

    /**
     * 获取保存在本地的用户数据
     *
     * @return
     */
    public static UserBean getLoginUserInfo() {
        SharedPreferences preferences = MyApplication.getAppContext().getSharedPreferences(AppConstants.SPDATA_KEY_USER, Context.MODE_PRIVATE);
        String userStr = preferences.getString(AppConstants.SPDATA_KEY_USER, "");
        return GsonUtils.formatStringToUserBean(userStr);
    }

    /**
     * 清空本地保存的用户信息
     */
    public static void clearLoginUserInfo() {
        updateLoginUserInfo(null);
    }

    public static void saveLotteryPageData(LotteryPageDataResponseBean bean) {
        SharedPreferences preferences = MyApplication.getAppContext().getSharedPreferences(SP_DATA_KEY_LOTTERY_DATA_BEAN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String userStr = GsonUtils.getGsonInstance().toJson(bean);
        editor.putString(SP_DATA_KEY_LOTTERY_DATA_BEAN, userStr);
        editor.apply();
    }

    /**
     * 获取本地保存的购彩首页的数据
     *
     * @return
     */
    public static LotteryPageDataResponseBean getLotteryPageDataInfo() {
        SharedPreferences preferences = MyApplication.getAppContext().getSharedPreferences(SP_DATA_KEY_LOTTERY_DATA_BEAN, Context.MODE_PRIVATE);
        String userStr = preferences.getString(SP_DATA_KEY_LOTTERY_DATA_BEAN, "");
        return GsonUtils.fomatStringToLotteryPageData(userStr);
    }

}
