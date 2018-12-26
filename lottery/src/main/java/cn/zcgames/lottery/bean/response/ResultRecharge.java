package cn.zcgames.lottery.bean.response;

/**
 * Created by admin on 2017/6/13.
 */

public class ResultRecharge extends ResponseBaseBean {

    private RechargeBean payload;

    public RechargeBean getData() {
        return payload;
    }

    public void setData(RechargeBean data) {
        this.payload = data;
    }

    public class RechargeBean {

        /**
         * goods_name : 1元
         * amount : 10
         * paytype : 100
         * descp :
         * qr_url : http://test.522zf.com/wap/pay/cd2b1020d0cbbb5fca3ed0d6519fe5b5/e9c836366feb656aa253f96b064116e78de83181399dd6961ff15a5234a94d24
         * url : HTTPS://QR.ALIPAY.COM/FKX03098MCFO1K15O4XO9E
         * created : 0
         * order_id : 254746678362378240
         * status : 1
         */

        private String goods_name;
        private int amount;
        private int paytype;
        private String descp;
        private String qr_url;
        private String url;
        private long created;
        private String order_id;
        private int status;

        public String getGoods_name() {
            return goods_name == null ? "" : goods_name;
        }

        public int getAmount() {
            return amount;
        }

        public int getPaytype() {
            return paytype;
        }

        public String getDescp() {
            return descp == null ? "" : descp;
        }

        public String getQr_url() {
            return qr_url == null ? "" : qr_url;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public long getCreated() {
            return created;
        }

        public String getOrder_id() {
            return order_id == null ? "" : order_id;
        }

        public int getStatus() {
            return status;
        }
    }

}
