package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/6/28.
 * 时时彩历史开奖记录
 * Berfy修改 2018.8.31
 */
public class AlwaysColorHistory {
    String sequence;
    String date;
    String award_time;
    List<String> number;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAward_time() {
        return award_time;
    }

    public void setAward_time(String award_time) {
        this.award_time = award_time;
    }

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }
}
