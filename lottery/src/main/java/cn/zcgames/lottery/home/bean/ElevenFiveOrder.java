package cn.zcgames.lottery.home.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Berfy
 * 11选5下注参数
 * 2018.9.26
 */
public class ElevenFiveOrder {

    String period;//购买期数
    int times;//倍数
    int chase;//追期
    List<ElvenFiveBet> stakes;

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

    public List<ElvenFiveBet> getStakes() {
        return stakes;
    }

    public void setStakes(List<ElvenFiveBet> stakes) {
        this.stakes = stakes;
    }

    public static class ElvenFiveBet {

        //不涉及直选且不涉及胆拖时数据放到numbers_c中，涉及直选时numbers_a为万位，numbers_b为千位，numbers_c为百位；涉及胆拖时numbers_a为胆数据，numbers_b为拖数据
        String gameplay;

        int is_dan_tuo;//1普通 2胆拖

        List<String> numbers_a;
        List<String> numbers_b;
        List<String> numbers_c;

        public String getGameplay() {
            return gameplay;
        }

        public void setGameplay(String gameplay) {
            this.gameplay = gameplay;
        }

        public int getIs_dan_tuo() {
            return is_dan_tuo;
        }

        public void setIs_dan_tuo(int is_dan_tuo) {
            this.is_dan_tuo = is_dan_tuo;
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
    }
}
