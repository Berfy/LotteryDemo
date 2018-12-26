package cn.zcgames.lottery.home.bean;

import java.io.Serializable;

public class TrendData implements Serializable {
    private static final long serialVersionUID = -6330569541989274173L;
    private String balls;
    private String blue;   //有用  列表  遗漏次数
    private String bss;
    private String codes;
    private String form;
    private String mulPos;
    private String winNumbers;//中奖号码
    private String oes;
    private String pid;  //有用  期数
    private String playType;
    private String red;
    private String space;
    private String sum;
    private String times;
    private String type;   //有用  类型

    private boolean isWaitAward = true;//是否等待开奖


    public boolean isWaitAward() {
        return isWaitAward;
    }

    public void setWaitAward(boolean waitAward) {
        isWaitAward = waitAward;
    }

    public String getWinNumbers() {
        return winNumbers;
    }

    public void setWinNumbers(String winNumbers) {
        this.winNumbers = winNumbers;
    }

    public String getCodes() {
        return this.codes;
    }

    public void setCodes(String str) {
        this.codes = str;
    }

    public String getSum() {
        return this.sum;
    }

    public void setSum(String str) {
        this.sum = str;
    }

    public String getSpace() {
        return this.space;
    }

    public void setSpace(String str) {
        this.space = str;
    }

    public String getTimes() {
        return this.times;
    }

    public void setTimes(String str) {
        this.times = str;
    }

    public String getForm() {
        return this.form;
    }

    public void setForm(String str) {
        this.form = str;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String str) {
        this.pid = str;
    }

    public String getRed() {
        return this.red;
    }

    public void setRed(String str) {
        this.red = str;
    }

    public String getBlue() {
        return this.blue;
    }

    public void setBlue(String str) {
        this.blue = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getOes() {
        return this.oes;
    }

    public void setOes(String str) {
        this.oes = str;
    }

    public String getBss() {
        return this.bss;
    }

    public void setBss(String str) {
        this.bss = str;
    }

    public String getBalls() {
        return this.balls;
    }

    public void setBalls(String str) {
        this.balls = str;
    }


    public String toString() {
        return "TrendData [pid=" + this.pid + ", red=" + this.red + ", blue=" + this.blue + ", type=" + this.type + ", balls=" + this.balls + ", oes=" + this.oes + ", bss=" + this.bss + ", playType=" + this.playType + "]";
    }

    public String getPlayType() {
        return this.playType;
    }

    public void setPlayType(String str) {
        this.playType = str;
    }
}
