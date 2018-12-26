package cn.zcgames.lottery.home.listener;

import java.util.List;

import cn.zcgames.lottery.home.bean.LotteryBall;

/**
 * Created by admin on 2017/6/19.
 */

public interface FastThreeSelectedListener {
    void onSelectBall(int playType, int type, List<LotteryBall> balls);
}
