package cn.zcgames.lottery.home.listener;

import java.util.List;

import cn.zcgames.lottery.home.bean.LotteryBall;

/**
 * 双色球胆拖投注方式时，点击数字事件的监听器
 * <p>
 * Created by admin on 2017/4/6.
 */

public interface DoubleColorDantuoSelectBallListener {
    void onSelectRedDanChanged(List<LotteryBall> numberList);

    void onSelectRedTuoChanged(List<LotteryBall> numberList);

    void onSelectBlueDantuoChanged(List<LotteryBall> numberList);

    void onSetIgnoreNumberListener(LotteryBall ball);
}
