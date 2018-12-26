package cn.zcgames.lottery.event;

/**
 * Created by admin on 2017/6/2.
 */

public class LotteryBuyOkEvent {
    boolean isOK;

    public LotteryBuyOkEvent(boolean isOk) {
        this.isOK = isOk;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }
}
