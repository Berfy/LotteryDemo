package cn.zcgames.lottery.home.bean;

import java.io.Serializable;

public class FormTrendBean implements Serializable {

    private String period;
    private String numbers;
    private String times;


    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
