package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.DoubleColorOrderBet;

/**
 * Created by admin on 2017/6/22.
 */

public class BillDetailDC extends BillDetail {
    List<DoubleColorOrderBet> bets;

    public List<DoubleColorOrderBet> getBets() {
        return bets;
    }

    public void setBets(List<DoubleColorOrderBet> bets) {
        this.bets = bets;
    }
}
