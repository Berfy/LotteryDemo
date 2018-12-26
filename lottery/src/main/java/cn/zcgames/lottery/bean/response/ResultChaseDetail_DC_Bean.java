package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.ChaseDetail_DC_Bean;

/**
 * Created by admin on 2017/7/14.
 * 双色球追期详情  接口返回bean
 * Berfy修改2018.8.31
 */
public class ResultChaseDetail_DC_Bean extends ResponseBaseBean {
    ChaseDetail_DC_Bean payload;

    public ChaseDetail_DC_Bean getData() {
        return payload;
    }

    public void setData(ChaseDetail_DC_Bean data) {
        this.payload = data;
    }
}
