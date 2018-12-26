package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.AlwaysColorOrder;

/**
 * Created by admin on 2017/7/14.
 */

public class ChaseDetail_AC_Bean extends ChaseDetailBean {

    List<AlwaysColorOrder.AlwaysColorBet> bets;

    public List<AlwaysColorOrder.AlwaysColorBet> getBets() {
        return bets;
    }

    public void setBets(List<AlwaysColorOrder.AlwaysColorBet> bets) {
        this.bets = bets;
    }
}
