package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.FastThreeOrderOld;

/**
 * Created by admin on 2017/6/22.
 */

public class BillDetailFastThree extends BillDetail {

//    FastThreeBillDetail bill;
//
//    public FastThreeBillDetail getBill() {
//        return bill;
//    }
//
//    public void setBill(FastThreeBillDetail bill) {
//        this.bill = bill;
//    }
//
//    public class FastThreeBillDetail {
        List<FastThreeOrderOld.FastThreeBet> bets;
        String sequence;

        public List<FastThreeOrderOld.FastThreeBet> getBets() {
            return bets;
        }

        public void setBets(List<FastThreeOrderOld.FastThreeBet> bets) {
            this.bets = bets;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

//    }

}
