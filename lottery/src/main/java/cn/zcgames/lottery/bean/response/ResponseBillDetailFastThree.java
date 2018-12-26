package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.LotteryOrderDetail;

/**
 * Created by admin on 2017/6/5.
 */

public class ResponseBillDetailFastThree extends ResponseBaseBean {

    private LotteryOrderDetail payload;

    public LotteryOrderDetail getData() {
        return payload;
    }

    public void setData(LotteryOrderDetail data) {
        this.payload = data;
    }

}
