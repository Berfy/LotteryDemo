package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.ThreeDOrder;

/**
 * Created by admin on 2017/6/22.
 */

public class BillDetailThreeD extends BillDetail {

    ThreeDBill bets;

    public ThreeDBill getBets() {
        return bets;
    }

    public void setBets(ThreeDBill bets) {
        this.bets = bets;
    }

    public class ThreeDBill {
        long chase;
        List<String> rewards;
        String created_at;
        long cost;
        long multiple;
        String sequence;
        List<ThreeDOrder.ThreeDDirect> direct;
        List<ThreeDGroup> group3;
        List<ThreeDGroup> group6;

        public long getChase() {
            return chase;
        }

        public void setChase(long chase) {
            this.chase = chase;
        }

        public List<String> getRewards() {
            return rewards;
        }

        public void setRewards(List<String> rewards) {
            this.rewards = rewards;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public long getCost() {
            return cost;
        }

        public void setCost(long cost) {
            this.cost = cost;
        }

        public long getMultiple() {
            return multiple;
        }

        public void setMultiple(long multiple) {
            this.multiple = multiple;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public List<ThreeDOrder.ThreeDDirect> getDirect() {
            return direct;
        }

        public void setDirect(List<ThreeDOrder.ThreeDDirect> direct) {
            this.direct = direct;
        }

        public List<ThreeDGroup> getGroup3() {
            return group3;
        }

        public void setGroup3(List<ThreeDGroup> group3) {
            this.group3 = group3;
        }

        public List<ThreeDGroup> getGroup6() {
            return group6;
        }

        public void setGroup6(List<ThreeDGroup> group6) {
            this.group6 = group6;
        }
    }

    public static class ThreeDGroup {
        List<String> Digit;

        public List<String> getDigit() {
            return Digit;
        }

        public void setDigit(List<String> digit) {
            this.Digit = digit;
        }
    }
}
