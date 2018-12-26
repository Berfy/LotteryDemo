package cn.zcgames.lottery.result.presenter;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * Created by admin on 2017/5/18.
 * Berfy修改2018.8.30
 */
public class ResultDetailPresenter {

    private static final String TAG = "ResultDetailPresenter";

    private Activity mContext;
    private IBaseView mBaseView;
    private ILotteryModel mLotteryModel;

    public ResultDetailPresenter(Activity context, IBaseView iActivity) {
        mContext = context;
        mBaseView = iActivity;
        mLotteryModel = new LotteryModel();
    }

    public void requestDetail(String sequence, String mType) {
        mBaseView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        mLotteryModel.requestResultDetail(sequence, mType, new NormalCallback() {
            @Override
            public void responseOk(Object result) {
                mBaseView.requestResult(true, result);
                mBaseView.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mBaseView.requestResult(false, errorMsg);
                mBaseView.hideTipDialog();
            }
        });
    }

    public void showDoubleColorView(LotteryResultHistory mLotteryResultHistory, LinearLayout mBallView, TextView tv_sum, String type) {
        LotteryUtils.showDoubleColorView(mContext, mLotteryResultHistory, mBallView, tv_sum, type);
    }
}
