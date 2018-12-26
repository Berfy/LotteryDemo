package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.Arrange5Order;

/**
 * Created by admin on 2017/6/22.
 */

public class BillDetailArrange5 extends BillDetail {

    private Arrange5Direct bets;
    String sequence;

    public Arrange5Direct getBets() {
        return bets;
    }

    public void setBets(Arrange5Direct bets) {
        this.bets = bets;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public class Arrange5Direct {

        List<Arrange5Order.Arrange5Bet> direct;

        public List<Arrange5Order.Arrange5Bet> getDirect() {
            return direct;
        }

        public void setDirect(List<Arrange5Order.Arrange5Bet> direct) {
            this.direct = direct;
        }

    }
}
