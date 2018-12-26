package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.DoubleColorOrderBet;

/**
 * Created by admin on 2017/7/14.
 * 双色球追期详情  接口返回Data bean
 * Berfy修改2018.8.31
 */

public class ChaseDetail_DC_Bean extends ChaseDetailBean {

    List<DoubleColorOrderBet> bets;

    public List<DoubleColorOrderBet> getBets() {
        return bets;
    }

    public void setBets(List<DoubleColorOrderBet> bets) {
        this.bets = bets;
    }
}
