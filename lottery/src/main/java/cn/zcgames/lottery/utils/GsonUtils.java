package cn.zcgames.lottery.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.zcgames.lottery.bean.BuyHistoryBean;
import cn.zcgames.lottery.home.bean.DoubleColorOrder;
import cn.zcgames.lottery.home.bean.FastThreeHistory;
import cn.zcgames.lottery.home.bean.ResultHistoryListData;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.home.bean.ThreeDOrder;
import cn.zcgames.lottery.bean.response.DoubleColorDetailBean;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.bean.response.LotteryWinningBean;
import cn.zcgames.lottery.bean.response.ResponseBaseBean;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.bean.response.ResultHistoryBean;

/**
 * Created by admin on 2017/3/31.
 */

public class GsonUtils {

    private static final String TAG = "GsonUtils";

    private static Gson mGson;

    public static Gson getGsonInstance() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    /**
     * 将string解析成UserBean
     *
     * @param str
     * @return
     */
    public static UserBean formatStringToUserBean(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getGsonInstance().fromJson(str, new TypeToken<UserBean>() {
        }.getType());
    }

    /**
     * 将string解析成List<String>
     *
     * @param str
     * @return
     */
    public static List<String> formatStringToStringList(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getGsonInstance().fromJson(str, new TypeToken<List<String>>() {
        }.getType());
    }

    /**
     * 将string解析成List<String>
     *
     * @param str
     * @return
     */
    public static List<Integer> formatStringToIntList(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getGsonInstance().fromJson(str, new TypeToken<List<Integer>>() {
        }.getType());
    }

    /**
     * 将string解析成LotteryPageDataBean
     *
     * @param str
     * @return
     */
    public static LotteryPageDataResponseBean fomatStringToLotteryPageData(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getGsonInstance().fromJson(str, new TypeToken<LotteryPageDataResponseBean>() {
        }.getType());
    }

    public static LotteryWinningBean testFormatString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getGsonInstance().fromJson(str, new TypeToken<LotteryWinningBean>() {
        }.getType());
    }

    public static DoubleColorDetailBean testFormatString2(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getGsonInstance().fromJson(str, new TypeToken<DoubleColorDetailBean>() {
        }.getType());
    }

    /**
     * 将string解析成LoginResponseResultBean
     *
     * @param str
     * @return
     */
    public static ResponseBaseBean formatLoginResponseResult(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getGsonInstance().fromJson(str, new TypeToken<ResponseBaseBean>() {
        }.getType());
    }

    /**
     * 将string解析成DoubleColorHistory
     *
     * @param jsonStr
     * @return
     */
    public static LotteryResultHistory formatStringToDoubleColorHistory(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return getGsonInstance().fromJson(jsonStr, new TypeToken<LotteryResultHistory>() {
        }.getType());
    }

    /**
     * 将string解析成DoubleColorHistory
     *
     * @param jsonStr
     * @return
     */
    public static ResultHistoryBean formatStringToResultHistoryBean(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return getGsonInstance().fromJson(jsonStr, new TypeToken<ResultHistoryBean>() {
        }.getType());
    }

    public static BuyHistoryBean formatStringToBuyHistoryBean(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return getGsonInstance().fromJson(jsonStr, new TypeToken<BuyHistoryBean>() {
        }.getType());
    }

    public static ThreeDOrder formatStringToThreeDOrder(String mOrderString) {
        if (TextUtils.isEmpty(mOrderString)) {
            return null;
        }
        return getGsonInstance().fromJson(mOrderString, new TypeToken<ThreeDOrder>() {
        }.getType());
    }

    public static DoubleColorOrder formatStringToDCOrder(String mOrderString) {
        if (TextUtils.isEmpty(mOrderString)) {
            return null;
        }
        return getGsonInstance().fromJson(mOrderString, new TypeToken<DoubleColorOrder>() {
        }.getType());
    }

    public static FastThreeHistory formatStringToFastThreeHistory(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return getGsonInstance().fromJson(jsonStr, new TypeToken<FastThreeHistory>() {
        }.getType());
    }

    public static ResultHistoryListData.HistoryList formatStringToFastThreeHistoryNew(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return getGsonInstance().fromJson(jsonStr, new TypeToken<ResultHistoryListData.HistoryList>() {
        }.getType());
    }
}
