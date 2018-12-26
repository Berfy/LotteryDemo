package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/6/21.
 */

public class FastThreeOrderOld {
    String sequence;
    int multiple;//倍数
    int chase;
    List<FastThreeBet> bet;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public int getChase() {
        return chase;
    }

    public void setChase(int chase) {
        this.chase = chase;
    }

    public void setBet(List<FastThreeBet> bet) {
        this.bet = bet;
    }

    public static class FastThreeBet {
        String mode;
        List<String> number;
        List<String> double_number;
        List<String> triple;

        public List<String> getDouble_number() {
            return double_number;
        }

        public void setDouble_number(List<String> double_number) {
            this.double_number = double_number;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public List<String> getNumber() {
            return number;
        }

        public void setNumber(List<String> number) {
            this.number = number;
        }

        public List<String> getTriple() {
            return triple;
        }

        public void setTriple(List<String> triple) {
            this.triple = triple;
        }
    }
}
