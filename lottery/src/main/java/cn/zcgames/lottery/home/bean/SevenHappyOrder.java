package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/7/7.
 * <p>
 * {
 * "sequence":"2016011",
 * "plain":[{"number":["1","2","3","4","5","6","7"]}],
 * "dantuo":[{"dan":["1","2","3","4","5"], "tuo":["6","7","8"]}],
 * "chase":1,
 * "multiple":1
 * }
 */

public class SevenHappyOrder {

    String sequence;
    int chase;
    int multiple;
    List<SevenHappyOrderPlain> plain;
    List<SevenHappyOrderDan> dantuo;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getChase() {
        return chase;
    }

    public void setChase(int chase) {
        this.chase = chase;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public List<SevenHappyOrderPlain> getPlain() {
        return plain;
    }

    public void setPlain(List<SevenHappyOrderPlain> plain) {
        this.plain = plain;
    }

    public List<SevenHappyOrderDan> getDantuo() {
        return dantuo;
    }

    public void setDantuo(List<SevenHappyOrderDan> dantuo) {
        this.dantuo = dantuo;
    }

    public static class SevenHappyOrderPlain{
        List<String> number;

        public List<String> getNumber() {
            return number;
        }

        public void setNumber(List<String> number) {
            this.number = number;
        }
    }

    public static class SevenHappyOrderDan {
        List<String> dan;
        List<String> tuo;

        public List<String> getDan() {
            return dan;
        }

        public void setDan(List<String> dan) {
            this.dan = dan;
        }

        public List<String> getTuo() {
            return tuo;
        }

        public void setTuo(List<String> tuo) {
            this.tuo = tuo;
        }
    }
}
