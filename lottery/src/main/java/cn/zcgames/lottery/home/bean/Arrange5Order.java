package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/6/29.
 * 排列五下单
 * Berfy修改 2018.8.31
 */
public class Arrange5Order {
    String start_sequence;
    int multiple;
    int chase;
    List<Arrange5Order.Arrange5Bet> direct;

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

    public List<Arrange5Bet> getDirect() {
        return direct;
    }

    public void setDirect(List<Arrange5Bet> direct) {
        this.direct = direct;
    }

    public static class Arrange5Bet {
        List<String> tenthousand, thousand, hundred, ten, one;

        public List<String> getTenthousand() {
            return tenthousand;
        }

        public void setTenthousand(List<String> tenthousand) {
            this.tenthousand = tenthousand;
        }

        public List<String> getThousand() {
            return thousand;
        }

        public void setThousand(List<String> thousand) {
            this.thousand = thousand;
        }

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
