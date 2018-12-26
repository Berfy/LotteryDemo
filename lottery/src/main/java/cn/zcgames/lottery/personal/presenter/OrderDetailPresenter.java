package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;
import android.text.TextUtils;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;

import static cn.zcgames.lottery.app.AppConstants.*;

import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 订单详情列表presenter
 *
 * @author NorthStar
 * @date 2017/6/5 17:01
 */
public class OrderDetailPresenter {

    private Activity mContext;
    private IBaseView iBaseView;
    private ILotteryModel iLotteryModel;
    public static final String TAG = "OrderDetailPresenter";

    public OrderDetailPresenter(Activity activity, IBaseView view) {
        mContext = activity;
        this.iBaseView = view;
        this.iLotteryModel = new LotteryModel();
    }

    /**
     * 请求订单详情
     *
     * @param lotteryName
     * @param orderId
     */
    public void requestOrderDetail(String lotteryName, String orderId) {
        iBaseView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        iLotteryModel.requestOrderDetail(lotteryName, orderId, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iBaseView.requestResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iBaseView.requestResult(false, errorStr);
                iBaseView.hideTipDialog();
            }
        });
    }
}
