package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.home.bean.AlwaysColorHistory;

/**
 * Created by admin on 2017/6/20.
 */

public class ResultAlwaysColorHistory extends ResponseBaseBean {

    List<AlwaysColorHistory> data;

    public List<AlwaysColorHistory> getData() {
        return data;
    }

    public void setData(List<AlwaysColorHistory> data) {
        this.data = data;
    }
}
