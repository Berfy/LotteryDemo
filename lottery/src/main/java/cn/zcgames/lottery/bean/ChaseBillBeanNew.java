package cn.zcgames.lottery.bean;

import java.util.List;

/**
 * @author Berfy
 * 追期管理列表
 * 2018.9.11
 */
public class ChaseBillBeanNew {

    private int page;
    private int page_size;
    private int total;
    private long ts;
    private int status;
    private List<ChaseBillList> list;

    public static class ChaseBillList {

        private String lottery_name;
        private String order_id;//订单id
        private String cost;//订单金额
        private String period_start;//开始期次
        private String period_end;//结束期次
        private String chase;//总追期
        private String chased;//已追期
        private List<String> periods;//期次

        public String getName() {
            return lottery_name;
        }

        public void setLottery_name(String lottery_name) {
            this.lottery_name = lottery_name;
        }

        public String getPeriod_start() {
            return period_start;
        }

        public void setPeriod_start(String period_start) {
            this.period_start = period_start;
        }

        public String getPeriod_end() {
            return period_end;
        }

        public void setPeriod_end(String period_end) {
            this.period_end = period_end;
        }

        public String getChase() {
            return chase;
        }

        public void setChase(String chase) {
            this.chase = chase;
        }

        public String getChased() {
            return chased;
        }

        public void setChased(String chased) {
            this.chased = chased;
        }

        public List<String> getPeriods() {
            return periods;
        }

        public void setPeriods(List<String> periods) {
            this.periods = periods;
        }

        public String getOrderId() {
            return order_id == null ? "" : order_id;
        }

        public String getCost() {
            return cost == null ? "" : cost;
        }
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ChaseBillList> getList() {
        return list;
    }

    public void setList(List<ChaseBillList> list) {
        this.list = list;
    }
}
