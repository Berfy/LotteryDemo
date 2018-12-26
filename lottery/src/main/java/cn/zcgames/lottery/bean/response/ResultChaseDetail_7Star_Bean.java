package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.ChaseDetail_7Star_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_A5_Bean;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseDetail_7Star_Bean extends ResponseBaseBean {
    ChaseDetail_7Star_Bean payload;

    public ChaseDetail_7Star_Bean getData() {
        return payload;
    }

    public void setData(ChaseDetail_7Star_Bean data) {
        this.payload = data;
    }
}
