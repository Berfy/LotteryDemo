package cn.zcgames.lottery.home.bean;

import cn.zcgames.lottery.bean.response.ResponseBaseBean;

/**
 * author: Berfy
 * date: 2018/10/13
 * 首页返回json
 */
public class LotteryPageDataResponseBean extends ResponseBaseBean {

    private LotteryPageDataBean payload;

    public LotteryPageDataBean getPayload() {
        return payload;
    }

    public void setPayload(LotteryPageDataBean payload) {
        this.payload = payload;
    }
}
