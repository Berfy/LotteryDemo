package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.bean.LotteryResultHistory;

/**
 * Created by admin on 2017/5/23.
 */

public class ResultHistoryBean extends ResponseBaseBean {

    String type;
    List<LotteryResultHistory> payload;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LotteryResultHistory> getData() {
        return payload;
    }

    public void setData(List<LotteryResultHistory> data) {
        this.payload = data;
    }
}
