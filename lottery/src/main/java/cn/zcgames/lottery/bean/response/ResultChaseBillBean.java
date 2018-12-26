package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.bean.ChaseBillBean;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseBillBean extends ResponseBaseBean {
    List<ChaseBillBean> payload;

    public List<ChaseBillBean> getData() {
        return payload;
    }

    public void setData(List<ChaseBillBean> data) {
        this.payload = data;
    }
}
