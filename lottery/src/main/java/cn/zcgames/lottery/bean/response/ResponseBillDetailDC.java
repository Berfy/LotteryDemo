package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.bean.BillDetail;
import cn.zcgames.lottery.bean.BillDetailDC;

/**
 * Created by admin on 2017/6/5.
 */

public class ResponseBillDetailDC extends ResponseBaseBean {

    private BillDetailDC payload;

    public BillDetailDC getData() {
        return payload;
    }

    public void setData(BillDetailDC data) {
        this.payload = data;
    }
}
