package cn.zcgames.lottery.home.lottery.alwaycolor.presenter;

import android.app.Activity;

import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.IAlwaysColorActivity;
import cn.zcgames.lottery.base.IBaseView;

/**
 * Created by admin on 2017/6/28.
 */

public class AlwaysColorPresenter {

    private static final String TAG = "AlwaysColorPresenter";

    private Activity mContext;
    private IAlwaysColorActivity iAlwaysColor;
    private IBaseView iBaseView;
    private ILotteryModel iLotteryModel;

    public AlwaysColorPresenter(Activity activity, IBaseView iView) {
        mContext = activity;
        this.iBaseView = iView;
        iLotteryModel = new LotteryModel();
    }

    public AlwaysColorPresenter(IAlwaysColorActivity iView) {
        this.iAlwaysColor = iView;
        iLotteryModel = new LotteryModel();
    }


    /**
     * 获取当前期号
     */
    public void requestCurrentSequence(String lotteryType) {
        iLotteryModel.requestCurrentSequence(lotteryType, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iAlwaysColor.requestCurrentSequenceResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iAlwaysColor.requestCurrentSequenceResult(false, errorStr);
            }
        });
    }

    /**
     * 查询前十条开奖记录
     */
    public void requestTopTenHistory() {
        iLotteryModel.requestAlwaysColorHistory(10, 0, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iAlwaysColor.requestResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iAlwaysColor.requestResult(false, errorStr);
            }
        });
    }
}
