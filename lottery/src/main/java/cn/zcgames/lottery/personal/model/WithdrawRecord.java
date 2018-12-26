package cn.zcgames.lottery.personal.model;
/**
 * 提现详情接口
 * @author NorthStar
 * @date  2018/9/11 14:23
 */
public class WithdrawRecord {
    /**
     * amount : 3200
     * order_id : 259069386747285504
     * pay_type : 300
     * status : 2
     */

    private long amount;
    private long order_id;
    private int pay_type;
    private int status;//2:已完成 3:申请 4:被拒绝
    private long created;

    public long getAmount() {
        return amount;
    }

    public long getOrder_id() {
        return order_id;
    }

    public int getPay_type() {
        return pay_type;
    }

    public int getStatus() {
        return status;
    }

    public long getCreated() {
        return created;
    }
}
