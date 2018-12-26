package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.ChaseDetail_DC_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_TD_Bean;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseDetail_TD_Bean extends ResponseBaseBean {
    ChaseDetail_TD_Bean payload;

    public ChaseDetail_TD_Bean getData() {
        return payload;
    }

    public void setData(ChaseDetail_TD_Bean data) {
        this.payload = data;
    }
}
