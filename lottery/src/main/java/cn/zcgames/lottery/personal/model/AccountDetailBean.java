package cn.zcgames.lottery.personal.model;

/**
 * 账户明细数据
 */
public class AccountDetailBean {

    private String datetime;
    private long amount;
    private String type;
    private String descp;
    private long cur_remain;

    public String getTime() {
        return datetime;
    }

    public long getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getInfo() {
        return descp;
    }

    public long getRemain() {
        return cur_remain;
    }
}
