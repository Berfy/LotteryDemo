package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.SevenHappyOrder;

/**
 * Created by admin on 2017/6/22.
 */

public class BillDetailSevenHappy extends BillDetail {

    SevenHappyBillDetail bets;

    public SevenHappyBillDetail getBets() {
        return bets;
    }

    public void setBets(SevenHappyBillDetail bets) {
        this.bets = bets;
    }

    public class SevenHappyBillDetail {
        String sequence;
        String cost;
        int mutiple;
        int chase;
        String created_at;
        List<String> rewards;
        List<SevenHappyOrder.SevenHappyOrderPlain> plain;
        List<SevenHappyOrder.SevenHappyOrderDan> dantuo;

        public List<String> getRewards() {
            return rewards;
        }

        public void setRewards(List<String> rewards) {
            this.rewards = rewards;
        }

        public List<SevenHappyOrder.SevenHappyOrderPlain> getPlain() {
            return plain;
        }

        public void setPlain(List<SevenHappyOrder.SevenHappyOrderPlain> plain) {
            this.plain = plain;
        }

        public List<SevenHappyOrder.SevenHappyOrderDan> getDantuo() {
            return dantuo;
        }

        public void setDantuo(List<SevenHappyOrder.SevenHappyOrderDan> dantuo) {
            this.dantuo = dantuo;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public int getMutiple() {
            return mutiple;
        }

        public void setMutiple(int mutiple) {
            this.mutiple = mutiple;
        }

        public int getChase() {
            return chase;
        }

        public void setChase(int chase) {
            this.chase = chase;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }
    }
}
