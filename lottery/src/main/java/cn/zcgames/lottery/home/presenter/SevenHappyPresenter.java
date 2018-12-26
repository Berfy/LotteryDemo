package cn.zcgames.lottery.home.presenter;

import android.app.Activity;

import java.util.List;

import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.ISevenActivity;

import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_HAPPY;
import static cn.zcgames.lottery.app.AppConstants.SEVEN_HAPPY_PLAY_STYLE_NORMAL;

/**
 * Created by admin on 2017/7/4.
 */

public class SevenHappyPresenter {

    private Activity mContext;
    private ISevenActivity iSevenActivity;
    private ILotteryModel iLotteryModel;

    public SevenHappyPresenter(Activity activity, ISevenActivity iView) {
        mContext = activity;
        this.iSevenActivity = iView;
        this.iLotteryModel = new LotteryModel();
    }

    public void requestTopTenHistory() {
        iLotteryModel.requestSevenHappyHistory(10, 0, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iSevenActivity.requestResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iSevenActivity.requestResult(false, errorStr);
            }
        });
    }

    public void calculateOrderCount(int size) {
        iSevenActivity.onSelectedOrderCount(CalculateCountUtils.calculateSevenCount(size));
    }

    public void requestCurrentSequence() {
        iLotteryModel.requestCurrentSequence(LOTTERY_TYPE_7_HAPPY, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iSevenActivity.onResultCurrentSequence(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iSevenActivity.onResultCurrentSequence(false, errorStr);
            }
        });
    }

    public void createLocalOrder(List<LotteryBall> mSelectedBall, int mCount) {
        if (mCount <= 0) {
            iSevenActivity.onResultCreateLocalOrder(false, "请选择投注");
            return;
        }
        boolean isOk = LotteryOrderDbUtils.addLotteryOrder(mSelectedBall, mCount,
                SEVEN_HAPPY_PLAY_STYLE_NORMAL, LOTTERY_TYPE_7_HAPPY);
        String msgStr = "";
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iSevenActivity.onResultCreateLocalOrder(isOk, msgStr);
    }
}
