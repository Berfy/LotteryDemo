package cn.zcgames.lottery.bean.response;

/**
 * Created by admin on 2017/6/7.
 */

public class ResultBuyLotteryBeanNew extends ResponseBaseBean {

    BuyLotteryDataBean payload;

    public BuyLotteryDataBean getData() {
        return payload;
    }

    public void setData(BuyLotteryDataBean data) {
        this.payload = data;
    }

    public class BuyLotteryDataBean {
        String order_id;
        String period;
        private int times;//倍数
        private int chase;//期数

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public int getChase() {
            return chase;
        }

        public void setChase(int chase) {
            this.chase = chase;
        }
    }

}
