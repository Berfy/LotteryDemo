package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.bean.LotteryResultHistory;

/**
 * Created by admin on 2017/5/10.
 * "status": "true",
 * "error": "",
 * "msg": "",
 * "data":
 */

public class LotteryWinningResponseBean extends ResponseBaseBean {

    LotteryWinningDataBean payload;

    public LotteryWinningDataBean getPayload() {
        return payload;
    }

    public void setPayload(LotteryWinningDataBean payload) {
        this.payload = payload;
    }
}
