package cn.zcgames.lottery.bean;

import java.io.Serializable;

//基本走势收据实体类
public class BasicTrendBean implements Serializable {
    public String periods;
    public String numbers; //中奖号
    public int sum;
    public int differ;
    public String singleNumbers; //遗漏次数
    private boolean isWaitAward = true;//是否等待开奖

    public boolean isWaitAward() {
        return isWaitAward;
    }

    public void setWaitAward(boolean waitAward) {
        isWaitAward = waitAward;
    }

    public String getSingleNumbers() {
        return singleNumbers == null ? "" : singleNumbers;
    }

    public void setSingleNumbers(String singleNumbers) {
        this.singleNumbers = singleNumbers;
    }
}
