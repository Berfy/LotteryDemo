package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 追期管理presenter
 *
 * @author NorthStar
 * @date 2018/8/20 17:23
 */
public class AfterPhasePresenter {

    private Activity mContext;
    IBaseView mBaseView;
    IAccountModel iAccountModel;
    public static final String TAG = "AfterPhasePresenter";

    public AfterPhasePresenter(IBaseView baseView) {
        mBaseView = baseView;
        iAccountModel = new AccountModel();
    }

    /**
     * 加载追期管理列表接口
     *
     * @param status
     * @param ts
     * @param startPageIndex
     * @param pageMax
     */
    public void requestPhaseBillNew(int status, long ts, int startPageIndex, int pageMax) {
//        mBaseView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        iAccountModel.requestChaseBillNew(startPageIndex, pageMax, status, ts, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
//                mBaseView.hideTipDialog();
                mBaseView.requestResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
//                mBaseView.hideTipDialog();
                mBaseView.requestResult(false, errorStr);
            }
        });
    }

    /**
     * 追期管理详情页数据接口
     *
     * @param lotteryName
     * @param orderId
     */
    public void requestPhaseBillDetail(String lotteryName, String orderId) {
        mBaseView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        iAccountModel.requestChaseDetail(lotteryName, orderId, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mBaseView.requestResult(true, msgStr);
                mBaseView.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mBaseView.hideTipDialog();
                mBaseView.requestResult(false, errorStr);
            }
        });
    }

}
