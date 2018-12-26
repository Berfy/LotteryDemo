package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.ChaseDetail_AC_Bean;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseDetail_AC_Bean extends ResponseBaseBean {
    ChaseDetail_AC_Bean payload;

    public ChaseDetail_AC_Bean getData() {
        return payload;
    }

    public void setData(ChaseDetail_AC_Bean data) {
        this.payload = data;
    }
}
