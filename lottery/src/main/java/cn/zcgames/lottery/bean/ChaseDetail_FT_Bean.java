package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.FastThreeOrderOld;

/**
 * Created by admin on 2017/7/14.
 */

public class ChaseDetail_FT_Bean extends ChaseDetailBean {

    List<FastThreeOrderOld.FastThreeBet> bets;

    public List<FastThreeOrderOld.FastThreeBet> getBets() {
        return bets;
    }

    public void setBets(List<FastThreeOrderOld.FastThreeBet> bets) {
        this.bets = bets;
    }
}
