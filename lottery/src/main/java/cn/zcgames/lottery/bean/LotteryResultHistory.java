package cn.zcgames.lottery.bean;

import java.util.List;

public class LotteryResultHistory {

    String sequence;
    String datetime;
    List<DoubleColorNumber> ball;

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

    public List<DoubleColorNumber> getBall() {
        return ball;
    }

    public void setBall(List<DoubleColorNumber> ball) {
        this.ball = ball;
    }

    public static class DoubleColorNumber {
        private String[] value;
        private String color;
        private String sum;//和值

        public String[] getValue() {
            return value;
        }

        public void setValue(String[] value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }
    }
}
