package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/25.
 */

public class ThreeDOrder {

    String start_sequence;
    int multiple;
    int chase;
    List<ThreeDGroup> group3;
    List<ThreeDGroup> group6;
    List<ThreeDDirect> direct;

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

    public List<ThreeDDirect> getDirect() {
        return direct;
    }

    public void setDirect(List<ThreeDDirect> direct) {
        this.direct = direct;
    }

    public static class ThreeDGroup {
        List<String> digit;

        public List<String> getDigit() {
            return digit;
        }

        public void setDigit(List<String> digit) {
            this.digit = digit;
        }
    }

    public static class ThreeDDirect {
        public List<String> hundred;
        public List<String> ten;
        public List<String> one;

        public List<String> getHundred() {
            return hundred;
        }

        public void setHundred(List<String> hundred) {
            this.hundred = hundred;
        }

        public List<String> getTen() {
            return ten;
        }

        public void setTen(List<String> ten) {
            this.ten = ten;
        }

        public List<String> getOne() {
            return one;
        }

        public void setOne(List<String> one) {
            this.one = one;
        }
    }
}
