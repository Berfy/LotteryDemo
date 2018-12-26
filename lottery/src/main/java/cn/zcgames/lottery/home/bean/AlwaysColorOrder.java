package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/6/29.
 * 时时彩下单参数
 * Berfy修改 2018.8.31
 */
public class AlwaysColorOrder {
    String period;
    int times;
    int chase;
    List<AlwaysColorOrder.AlwaysColorBet> stakes;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getChase() {
        return chase;
    }

    public void setChase(int chase) {
        this.chase = chase;
    }

    public List<AlwaysColorBet> getStakes() {
        return stakes;
    }

    public void setStakes(List<AlwaysColorBet> stakes) {
        this.stakes = stakes;
    }

    public static class AlwaysColorBet {

        //numbers_a到numbers_e依次为万位到个位，投注可以按照网易彩票的手端来，网易手端有位标记的这里也按位投放数据没有位标记的都放在numbers_e中
        String gameplay;
        List<String> numbers_a;//万
        List<String> numbers_b;//千
        List<String> numbers_c;//百
        List<String> numbers_d;//十
        List<String> numbers_e;//个

        public String getGameplay() {
            return gameplay;
        }

        public void setGameplay(String gameplay) {
            this.gameplay = gameplay;
        }

        public List<String> getNumbers_a() {
            return numbers_a;
        }

        public void setNumbers_a(List<String> numbers_a) {
            this.numbers_a = numbers_a;
        }

        public List<String> getNumbers_b() {
            return numbers_b;
        }

        public void setNumbers_b(List<String> numbers_b) {
            this.numbers_b = numbers_b;
        }

        public List<String> getNumbers_c() {
            return numbers_c;
        }

        public void setNumbers_c(List<String> numbers_c) {
            this.numbers_c = numbers_c;
        }

        public List<String> getNumbers_d() {
            return numbers_d;
        }

        public void setNumbers_d(List<String> numbers_d) {
            this.numbers_d = numbers_d;
        }

        public List<String> getNumbers_e() {
            return numbers_e;
        }

        public void setNumbers_e(List<String> numbers_e) {
            this.numbers_e = numbers_e;
        }
    }
}
