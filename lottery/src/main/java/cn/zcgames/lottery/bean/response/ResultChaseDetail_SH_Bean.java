package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.ChaseDetail_SH_Bean;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseDetail_SH_Bean extends ResponseBaseBean {
    ChaseDetail_SH_Bean payload;

    public ChaseDetail_SH_Bean getData() {
        return payload;
    }

    public void setData(ChaseDetail_SH_Bean data) {
        this.payload = data;
    }
}
