package cn.zcgames.lottery.home.bean;

import java.util.List;

public class DoubleColorOrder {
    int chase;//追期
    int multiple;//倍数
    List<DoubleColorOrderBet> bets;

    public int getChase() {
        return chase;
    }

    public void setChase(int chase) {
        this.chase = chase;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public List<DoubleColorOrderBet> getBets() {
        return bets;
    }

    public void setBets(List<DoubleColorOrderBet> bets) {
        this.bets = bets;
    }

}
