package cn.zcgames.lottery.home.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Berfy
 * date: 2018/10/17
 * 选号缓存
 */
public class ChooseNumberBean {

    private String lotteryType;
    private int playType;
    private int numPos;
    private List<LotteryBall> lotteryBalls;

    public String getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public int getNumPos() {
        return numPos;
    }

    public void setNumPos(int numPos) {
        this.numPos = numPos;
    }

    public List<LotteryBall> getLotteryBalls() {
        return null == lotteryBalls ? new ArrayList<>() : lotteryBalls;
    }

    public void setLotteryBalls(List<LotteryBall> lotteryBalls) {
        this.lotteryBalls = lotteryBalls;
    }
}
