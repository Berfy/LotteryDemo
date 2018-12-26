package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.BillDetailAlwaysColor;
import cn.zcgames.lottery.bean.BillDetailFastThree;

/**
 * Created by admin on 2017/6/5.
 */

public class ResponseBillDetailAlwaysColor extends ResponseBaseBean {

    private BillDetailAlwaysColor payload;

    public BillDetailAlwaysColor getData() {
        return payload;
    }

    public void setData(BillDetailAlwaysColor data) {
        this.payload = data;
    }
}
