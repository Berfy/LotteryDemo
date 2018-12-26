package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.ChaseDetail_A5_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_AC_Bean;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseDetail_A5_Bean extends ResponseBaseBean {
    ChaseDetail_A5_Bean payload;

    public ChaseDetail_A5_Bean getData() {
        return payload;
    }

    public void setData(ChaseDetail_A5_Bean data) {
        this.payload = data;
    }
}
