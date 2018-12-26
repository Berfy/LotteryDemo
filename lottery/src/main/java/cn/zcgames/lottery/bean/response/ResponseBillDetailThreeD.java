package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.BillDetailDC;
import cn.zcgames.lottery.bean.BillDetailThreeD;

/**
 * Created by admin on 2017/6/5.
 */

public class ResponseBillDetailThreeD extends ResponseBaseBean {

    private BillDetailThreeD payload;

    public BillDetailThreeD getData() {
        return payload;
    }

    public void setData(BillDetailThreeD data) {
        this.payload = data;
    }
}
