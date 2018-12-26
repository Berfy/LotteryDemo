package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.BillDetailAlwaysColor;
import cn.zcgames.lottery.bean.BillDetailSevenHappy;

/**
 * Created by admin on 2017/6/5.
 */

public class ResponseBillDetailSevenHappy extends ResponseBaseBean {

    private BillDetailSevenHappy payload;

    public BillDetailSevenHappy getData() {
        return payload;
    }

    public void setData(BillDetailSevenHappy data) {
        this.payload = data;
    }
}
