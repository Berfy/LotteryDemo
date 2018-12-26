package cn.zcgames.lottery.home.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.zcgames.lottery.bean.response.ResponseBaseBean;

/**
 * author: Berfy
 * date: 2018/10/15
 * 走势图接口
 */
public class TrendDataResponse extends ResponseBaseBean implements Serializable {

    private PayLoad payload;

    public static class PayLoad {

        private String lottery_name;
        private String trend;

        public String getLottery_name() {
            return lottery_name;
        }

        public void setLottery_name(String lottery_name) {
            this.lottery_name = lottery_name;
        }

        public TrendResponseData getTrend() {
            if (!TextUtils.isEmpty(trend)) {
                return GsonUtil.getInstance().toClass(trend, TrendResponseData.class);
            }
            return new TrendResponseData();
        }

        public void setTrend(String trend) {
            this.trend = trend;
        }
    }

    public PayLoad getPayLoad() {
        return payload;
    }

    public void setPayLoad(PayLoad payLoad) {
        this.payload = payLoad;
    }
}
