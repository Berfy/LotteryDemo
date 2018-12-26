package cn.zcgames.lottery.bean.response;

/**
 * Created by admin on 2017/6/7.
 */

public class ResultBuyLotteryBean extends ResponseBaseBean {

    BuyLotteryDataBean payload;

    public BuyLotteryDataBean getData() {
        return payload;
    }

    public void setData(BuyLotteryDataBean data) {
        this.payload = data;
    }

    public class BuyLotteryDataBean {
        String id;
        String sequence;
        String type;
        String url;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }
    }

}
