package cn.zcgames.lottery.home.listener;

import java.util.List;

import cn.zcgames.lottery.home.bean.LotteryBall;

/**
 * 双色球普通投注方式时，点击数字事件的监听器
 * Created by admin on 2017/4/6.
 */

public interface DoubleColorNormalSelectBallListener {
    void onSelectRedBallChanged(List<LotteryBall> numberList);

    void onSelectBlueBallChanged(List<LotteryBall> numberList);
}
