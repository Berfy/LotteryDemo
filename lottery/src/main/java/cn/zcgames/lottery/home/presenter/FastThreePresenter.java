package cn.zcgames.lottery.home.presenter;

import android.app.Activity;

import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.IFastThreeActivity;

/**
 * Created by admin on 2017/6/20.
 */

public class FastThreePresenter {

    private Activity mContext;
    IFastThreeActivity iFastThreeActivity;
    ILotteryModel iLotteryModel;

    public FastThreePresenter(Activity activity, IFastThreeActivity iFastThreeActivity) {
        mContext = activity;
        this.iFastThreeActivity = iFastThreeActivity;
        this.iLotteryModel = new LotteryModel();
    }

    /**
     * 查询当前期号
     */
    public void requestCurrentSequence(String lotteryType) {
        iLotteryModel.requestCurrentSequence(lotteryType, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iFastThreeActivity.requestCurrentSequenceResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iFastThreeActivity.requestCurrentSequenceResult(false, errorStr);
            }
        });
    }
}
