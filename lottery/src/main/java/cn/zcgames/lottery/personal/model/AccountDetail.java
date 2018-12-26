package cn.zcgames.lottery.personal.model;

import java.util.List;

/**
 * 账户明细数据
 *
 * @author NorthStar
 * @date 2018/8/20 17:50
 */
public class AccountDetail {
    private String pay_stakes_total;//购彩总数
    private String charges_total;//充值总数
    private String rewards_total;//派奖总数
    private String withdraw_total;//提现总数
    private List<AccountDetailBean> list;

    public String getBuy() {
        return pay_stakes_total == null ? "" : pay_stakes_total;
    }

    public String getRecharge() {
        return charges_total == null ? "" : charges_total;
    }

    public String getAward() {
        return rewards_total == null ? "" : rewards_total;
    }

    public String getWithdraw() {
        return withdraw_total == null ? "" : withdraw_total;
    }

    public List<AccountDetailBean> getDetails() {
        return list;
    }

    public void setDetails(List<AccountDetailBean> details) {
        this.list = details;
    }


}
