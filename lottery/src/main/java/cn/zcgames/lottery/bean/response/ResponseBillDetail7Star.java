package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.BillDetail7Star;
import cn.zcgames.lottery.bean.BillDetailArrange5;

/**
 * Created by admin on 2017/6/5.
 */

public class ResponseBillDetail7Star extends ResponseBaseBean {

    private BillDetail7Star payload;

    public BillDetail7Star getData() {
        return payload;
    }

    public void setData(BillDetail7Star data) {
        this.payload = data;
    }
}
