package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.bean.ChaseBillBean;
import cn.zcgames.lottery.bean.ChaseBillBeanNew;

/**
 * Created by admin on 2017/7/14.
 */

public class ResultChaseBillBeanNew extends ResponseBaseBean {

    private ChaseBillBeanNew payload;

    public ChaseBillBeanNew getPayload() {
        return payload;
    }

    public void setPayload(ChaseBillBeanNew payload) {
        this.payload = payload;
    }
}
