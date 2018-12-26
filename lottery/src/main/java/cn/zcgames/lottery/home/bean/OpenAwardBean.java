package cn.zcgames.lottery.home.bean;

import java.io.Serializable;

public class OpenAwardBean implements Serializable {
    private String awardNums;
    private String key3;
    private String key4;
    private String key5;
    private int sum;
    private String numSize;
    private String sumSize;
    private String period;
    private String numState;

    public String getAwardNums() {
        return awardNums;
    }

    public void setAwardNums(String awardNums) {
        this.awardNums = awardNums;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getKey4() {
        return key4;
    }

    public void setKey4(String key4) {
        this.key4 = key4;
    }

    public String getKey5() {
        return key5;
    }

    public void setKey5(String key5) {
        this.key5 = key5;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNumState() {
        return numState;
    }

    public void setNumState(String numState) {
        this.numState = numState;
    }
}
