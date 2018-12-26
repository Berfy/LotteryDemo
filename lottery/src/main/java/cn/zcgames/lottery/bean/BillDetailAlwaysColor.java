package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.AlwaysColorOrder;

/**
 * Created by admin on 2017/6/22.
 */

public class BillDetailAlwaysColor extends BillDetail {

//    AlwaysColorBillDetail data;
//
//    public AlwaysColorBillDetail getData() {
//        return data;
//    }
//
//    public void setData(AlwaysColorBillDetail data) {
//        this.data = data;
//    }
//
//    public class AlwaysColorBillDetail {
        List<AlwaysColorOrder.AlwaysColorBet> bets;
        String sequence;
//        String cost;
//        int multiple;
//        int chase;
//        String created_at;

//        public int getMultiple() {
//            return multiple;
//        }
//
//        public void setMultiple(int multiple) {
//            this.multiple = multiple;
//        }

//        public String getCost() {
//            return cost;
//        }
//
//        public void setCost(String cost) {
//            this.cost = cost;
//        }

//        public int getChase() {
//            return chase;
//        }
//
//        public void setChase(int chase) {
//            this.chase = chase;
//        }

//        public String getCreated_at() {
//            return created_at;
//        }
//
//        public void setCreated_at(String created_at) {
//            this.created_at = created_at;
//        }

        public List<AlwaysColorOrder.AlwaysColorBet> getBets() {
            return bets;
        }

        public void setBets(List<AlwaysColorOrder.AlwaysColorBet> bets) {
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
