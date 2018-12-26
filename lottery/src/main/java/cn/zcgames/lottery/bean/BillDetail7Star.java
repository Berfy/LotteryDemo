package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.SevenStarOrder;

/**
 * Created by admin on 2017/6/22.
 */

public class BillDetail7Star extends BillDetail {

    private SevenStarDirect bets;
    String sequence;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public SevenStarDirect getBets() {
        return bets;
    }

    public void setBets(SevenStarDirect bets) {
        this.bets = bets;
    }

    public class SevenStarDirect {

        List<SevenStarOrder.SevenStarBet> direct;

        public List<SevenStarOrder.SevenStarBet> getDirect() {
            return direct;
        }

        public void setDirect(List<SevenStarOrder.SevenStarBet> direct) {
            this.direct = direct;
        }
    }
}
