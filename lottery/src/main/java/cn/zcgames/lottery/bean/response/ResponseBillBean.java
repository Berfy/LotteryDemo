package cn.zcgames.lottery.bean.response;

/**
 * Created by admin on 2017/5/19.
 * 投注记录接口返回Bean
 * Berfy修改 2018.8.31
 */
public class ResponseBillBean extends ResponseBaseBean {

    //    List<BuyHistoryBean> payload;
    //
    //    public List<BuyHistoryBean> getData() {
    //        return payload;
    //    }
    //
    //    public void setData(List<BuyHistoryBean> data) {
    //        this.payload = data;
    //    }

    private OrderRecordInfo payload;

    public OrderRecordInfo getData() {
        return payload;
    }
}
