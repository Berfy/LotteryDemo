package cn.zcgames.lottery.result.presenter;

import android.app.Activity;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * Created by admin on 2017/5/18.
 * 开奖历史记录
 * Berfy修改 2018.8.31
 */
public class ResultHistoryPresenter {

    private static final String TAG = "ResultHistoryPresenter";

    private Activity mContext;
    private IBaseView iActivity;
    private ILotteryModel iModel;

    public ResultHistoryPresenter(Activity activity, IBaseView iActivity) {
        mContext = activity;
        this.iActivity = iActivity;
        iModel = new LotteryModel();
    }

    /**
     * 获取快三近期开奖记录
     *
     * @param mStartPage
     * @param mPageSize
     */
    public void requestResultHistory(String lotteryType, int mStartPage, int mPageSize, long ts) {
//        iActivity.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        iModel.requestHistoryNew(lotteryType, mPageSize, mStartPage, ts, new NormalCallback() {
            @Override
            public void responseOk(Object result) {
                iActivity.requestResult(true, result);
//                iActivity.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.requestResult(false, errorStr);
//                iActivity.hideTipDialog();
            }
        });
    }

    public void requestAlwaysColorHistory(int mStartPage, int mPageSize) {
        iActivity.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        iModel.requestAlwaysColorHistory(mPageSize, mStartPage, new NormalCallback() {
            @Override
            public void responseOk(Object result) {
                iActivity.requestResult(true, result);
                iActivity.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.requestResult(false, errorStr);
                iActivity.hideTipDialog();
            }
        });
    }

    //获取彩种历史开奖列表
    public void requestLotteryWinningHistory(String lotteryType, String url, int mStartPage, int mPageSize) {
        iActivity.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        iModel.requestResultHistory(lotteryType, url, mPageSize, mStartPage, new NormalCallback() {
            @Override
            public void responseOk(Object result) {
                iActivity.requestResult(true, result);
                iActivity.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.requestResult(false, errorStr);
                iActivity.hideTipDialog();
            }
        });
    }
}
