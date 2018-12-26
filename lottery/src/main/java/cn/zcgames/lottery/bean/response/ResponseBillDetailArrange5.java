package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.BillDetailAlwaysColor;
import cn.zcgames.lottery.bean.BillDetailArrange5;

/**
 * Created by admin on 2017/6/5.
 */

public class ResponseBillDetailArrange5 extends ResponseBaseBean {

    private BillDetailArrange5 payload;

    public BillDetailArrange5 getData() {
        return payload;
    }

    public void setData(BillDetailArrange5 data) {
        this.payload = data;
    }
}
