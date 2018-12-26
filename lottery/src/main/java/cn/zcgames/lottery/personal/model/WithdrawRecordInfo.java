package cn.zcgames.lottery.personal.model;

import java.util.List;

import cn.zcgames.lottery.bean.response.ResponseBaseBean;

/**
 * 提现记录
 *
 * @author NorthStar
 * @date 2018/9/11 14:23
 */
public class WithdrawRecordInfo extends ResponseBaseBean {

    /**
     * payload : {"page":1,"page_size":10,"total":0,"ts":0,"withdraw_data":[{"amount":3200,"order_id":259069386747285504,"pay_type":300,"status":2}]}
     */
    private withdrawBean payload;

    public withdrawBean getData() {
        return payload;
    }

    public static class withdrawBean {
        /**
         * page : 1
         * page_size : 10
         * total : 0
         * ts : 0
         * withdraw_data : [{"amount":3200,"order_id":259069386747285504,"pay_type":300,"status":2}]
         */

        private int page;
        private int page_size;
        private int total;
        private long ts;
        private List<WithdrawRecord> withdraw_data;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public List<WithdrawRecord> getWithdrawData() {
            return withdraw_data;
        }

        public void setWithdraw_data(List<WithdrawRecord> withdraw_data) {
            this.withdraw_data = withdraw_data;
        }
    }
}
