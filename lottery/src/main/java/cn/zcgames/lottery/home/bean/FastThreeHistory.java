package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/6/20.
 */

public class FastThreeHistory {

    String sequence;
    String datetime;
    String detail;
    Dice dice;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public static class Dice {
        String sequence;
        List<String> number;
        String sum;
        String size;
        String single_or_double;
        String form;

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public List<String> getNumber() {
            return number;
        }

        public void setNumber(List<String> number) {
            this.number = number;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getSingle_or_double() {
            return single_or_double;
        }

        public void setSingle_or_double(String single_or_double) {
            this.single_or_double = single_or_double;
        }
    }
}
