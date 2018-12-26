package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.ChaseDetail_FT_Bean;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseDetail_FT_Bean extends ResponseBaseBean {
    ChaseDetail_FT_Bean payload;

    public ChaseDetail_FT_Bean getData() {
        return payload;
    }

    public void setData(ChaseDetail_FT_Bean data) {
        this.payload = data;
    }
}
