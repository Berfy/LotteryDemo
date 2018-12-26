package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.home.bean.FastThreeHistory;

/**
 * Created by admin on 2017/6/20.
 */

public class ResultFastThreeHistory extends ResponseBaseBean {

    List<FastThreeHistory> payload;

    public List<FastThreeHistory> getData() {
        return payload;
    }

    public void setData(List<FastThreeHistory> data) {
        this.payload = data;
    }
}
