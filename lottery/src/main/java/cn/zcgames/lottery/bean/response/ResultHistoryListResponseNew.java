package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.home.bean.ResultHistoryListData;

/**
 * @author Berfy
 * 快三历史开奖
 * 2018.9.10
 */
public class ResultHistoryListResponseNew extends ResponseBaseBean {

    ResultHistoryListData payload;

    public ResultHistoryListData getData() {
        return payload;
    }

    public void setData(ResultHistoryListData payload) {
        this.payload = payload;
    }
}
