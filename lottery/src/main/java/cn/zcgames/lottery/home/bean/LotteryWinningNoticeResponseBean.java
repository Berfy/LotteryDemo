package cn.zcgames.lottery.home.bean;

import java.util.List;

import cn.zcgames.lottery.bean.response.ResponseBaseBean;

/**
 * @author Berfy
 * 首页公告请求返回
 */
public class LotteryWinningNoticeResponseBean extends ResponseBaseBean {

    private LotteryWinningNoticeData payload;

    public LotteryWinningNoticeData getPayload() {
        return payload;
    }

    public void setPayload(LotteryWinningNoticeData payload) {
        this.payload = payload;
    }
}
