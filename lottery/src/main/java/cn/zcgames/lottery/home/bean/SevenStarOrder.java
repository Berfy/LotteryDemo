package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/6/29.
 */

public class SevenStarOrder {
    String start_sequence;
    int multiple;
    int chase;
    List<SevenStarOrder.SevenStarBet> direct;

    public String getStart_sequence() {
        return start_sequence;
    }

    public void setStart_sequence(String start_sequence) {
        this.start_sequence = start_sequence;
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

    public List<SevenStarBet> getDirect() {
        return direct;
    }

    public void setDirect(List<SevenStarBet> direct) {
        this.direct = direct;
    }

    public static class SevenStarBet {
        List<String> seven, six, five, four, three, two, one;

        public List<String> getSeven() {
            return seven;
        }

        public void setSeven(List<String> seven) {
            this.seven = seven;
        }

        public List<String> getSix() {
            return six;
        }

        public void setSix(List<String> six) {
            this.six = six;
        }

        public List<String> getFive() {
            return five;
        }

        public void setFive(List<String> five) {
            this.five = five;
        }

        public List<String> getFour() {
            return four;
        }

        public void setFour(List<String> four) {
            this.four = four;
        }

        public List<String> getThree() {
            return three;
        }

        public void setThree(List<String> three) {
            this.three = three;
        }

        public List<String> getTwo() {
            return two;
        }

        public void setTwo(List<String> two) {
            this.two = two;
        }

        public List<String> getOne() {
            return one;
        }

        public void setOne(List<String> one) {
            this.one = one;
        }
    }
}
