package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * author: Berfy
 * date: 2018/10/17
 * 计算当前选择号码
 * 选号详情信息（奖金 盈利 注数 等等）
 */
public class ChooseNumberInfoBean {

    private long count;
    private List<String> price;//如果有范围就是包含2位  {"2","3"}
    private List<String> winPrice;
    private List<LotteryBall> ge;//个位 胆码 同号   1星
    private List<LotteryBall> shi;//十位 拖码 不同号 2星
    private List<LotteryBall> bai;//百位
    private List<LotteryBall> qian;//千位
    private List<LotteryBall> wan;//万位 任选

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getWinPrice() {
        return winPrice;
    }

    public void setWinPrice(List<String> winPrice) {
        this.winPrice = winPrice;
    }

    public List<LotteryBall> getGe() {
        return ge;
    }

    public void setGe(List<LotteryBall> ge) {
        this.ge = ge;
    }

    public List<LotteryBall> getShi() {
        return shi;
    }

    public void setShi(List<LotteryBall> shi) {
        this.shi = shi;
    }

    public List<LotteryBall> getBai() {
        return bai;
    }

    public void setBai(List<LotteryBall> bai) {
        this.bai = bai;
    }

    public List<LotteryBall> getQian() {
        return qian;
    }

    public void setQian(List<LotteryBall> qian) {
        this.qian = qian;
    }

    public List<LotteryBall> getWan() {
        return wan;
    }

    public void setWan(List<LotteryBall> wan) {
        this.wan = wan;
    }
}
