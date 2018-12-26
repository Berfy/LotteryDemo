package cn.zcgames.lottery.utils.countdowntimer;

/**
 * 倒计时监听
 *
 */
public interface OnCountDownTimerListener{
    void onTick(long millisUntilFinished);

    void onFinish();
}
