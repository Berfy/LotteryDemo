package cn.zcgames.lottery.personal.model;

/**
 * 追期详情追期订单状态详情数据
 *
 * @author NorthStar
 * @date 2018/9/12 13:59
 */
public class ChaseDetailStatus {
    String sequence;
    String winNumber;//开奖号码
    String billStatus;
    String rewardStatus;

    public String getSequence() {
        return sequence == null ? "" : sequence;
    }

    public String getBillStatus() {
        return billStatus == null ? "" : billStatus;
    }

    public String getRewardStatus() {
        return rewardStatus == null ? "" : rewardStatus;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public String getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(String winNumber) {
        this.winNumber = winNumber;
    }
}
