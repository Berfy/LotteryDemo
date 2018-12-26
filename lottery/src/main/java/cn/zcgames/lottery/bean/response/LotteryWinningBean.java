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

public class LotteryWinningBean extends ResponseBaseBean {

    List<LotteryResultHistory> payload;

    public List<LotteryResultHistory> getData() {
        return payload;
    }

    public void setData(List<LotteryResultHistory> data) {
        this.payload = data;
    }
}
