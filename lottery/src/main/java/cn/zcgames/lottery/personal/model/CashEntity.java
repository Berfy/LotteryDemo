package cn.zcgames.lottery.personal.model;

/**
 * 充值金额数据类
 * @author NorthStar
 * @date  2018/8/27 14:01
 */
public class CashEntity {
    private int cash;//充值金额
    public boolean selectedFlag=false;//是否被选中

    public CashEntity(int cash, boolean selectedFlag) {
        this.cash = cash;
        this.selectedFlag = selectedFlag;
    }

    public int getCash() {
        return cash;
    }

    public boolean isSelectedFlag() {
        return selectedFlag;
    }

    public void setSelectedFlag(boolean selectedFlag) {
        this.selectedFlag = selectedFlag;
    }
}
