package cn.zcgames.lottery.home.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Berfy
 * 快三下注
 */
public class FastThreeOrder {

    String period;//购买期数
    int times;//倍数
    int chase;//追期
    List<FastThreeBet> stakes;

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

    public List<FastThreeBet> getStakes() {
        return stakes;
    }

    public void setStakes(List<FastThreeBet> stakes) {
        this.stakes = stakes;
    }

    public static class FastThreeBet {

        String gameplay;

        List<String> numbers;

        @SerializedName("double")
        List<String> double_number;

        List<String> triple;

        public String getGameplay() {
            return gameplay;
        }

        public void setGameplay(String gameplay) {
            this.gameplay = gameplay;
        }

        public List<String> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<String> numbers) {
            this.numbers = numbers;
        }

        public List<String> getDouble_number() {
            return double_number;
        }

        public void setDouble_number(List<String> double_number) {
            this.double_number = double_number;
        }

        public List<String> getTriple() {
            return triple;
        }

        public void setTriple(List<String> triple) {
            this.triple = triple;
        }
    }
}
