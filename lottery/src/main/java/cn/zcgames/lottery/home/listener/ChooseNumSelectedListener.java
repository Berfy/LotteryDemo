package cn.zcgames.lottery.home.listener;

import java.util.List;

import cn.zcgames.lottery.home.bean.LotteryBall;

/**
 * @author Berfy
 * 选号接口
 */
public interface ChooseNumSelectedListener {
    void onSelectBall(int type, List<LotteryBall> balls);
}
