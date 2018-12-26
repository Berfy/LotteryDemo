package cn.zcgames.lottery.bean;

import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class ChaseDetail_TD_Bean extends ChaseDetailBean {

    BillDetailThreeD.ThreeDBill bets;

    public BillDetailThreeD.ThreeDBill getBets() {
        return bets;
    }

    public void setBets(BillDetailThreeD.ThreeDBill bets) {
        this.bets = bets;
    }
}
