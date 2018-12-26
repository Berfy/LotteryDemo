package cn.zcgames.lottery.home.presenter;

import android.app.Activity;

import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.response.ResponseBaseBean;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IWalletModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.model.remote.impl.WalletModel;
import cn.zcgames.lottery.home.view.iview.IPayActivity;

/**
 * 支付接口
 * Berfy修改2018.8.27
 */
public class PayPresenter {

    private Activity mContext;
    private IPayActivity iPayView;
    private IWalletModel iWalletModel;
    private ILotteryModel iLotteryModel;

    public PayPresenter(Activity activity, IPayActivity iView) {
        mContext = activity;
        this.iPayView = iView;
        this.iWalletModel = new WalletModel();
        iLotteryModel = new LotteryModel();
    }

    /**
     * 查询余额
     */
    public void requestRemain() {
        UserBean userBean = MyApplication.getCurrLoginUser();
        if (!AppConstants.SHOW_LOTTERY_PAY_TEST) {
            if (userBean == null) {
                iPayView.requestResultFailed(true, "请先登录");
                return;
            }
        }
//        ResponseBaseBean responseBaseBean = new ResponseBaseBean();
//        responseBaseBean.setRemain(userBean.getMoney() + "");
//        iPayView.requestResultOk(responseBaseBean);

        iWalletModel.requestRemain(new NormalCallback<ResponseBaseBean>() {
            @Override
            public void responseOk(ResponseBaseBean responseBaseBean) {
                iPayView.requestResultOk(responseBaseBean);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                iPayView.requestResultFailed(isNeedLogin, errorStr);
            }
        });
    }

    /**
     * 购买彩票
     *
     * @param mLotteryType
     * @param mOrderString
     */
    public void buyLottery(String mLotteryType, String mOrderString, String amount, String payPassword) {
        iPayView.showTipDialog("正在购买");
        iLotteryModel.buyLottery(mLotteryType, mOrderString, amount, payPassword, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iPayView.onBuyResult(true, msgStr);
                iPayView.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iPayView.onBuyResult(false, errorStr);
                iPayView.hideTipDialog();
            }
        });
    }

    /**
     * 充值
     *
     * @param mPayMoney
     * @param mRechargeType
     */
//    public void recharge(long mPayMoney, String mRechargeType) {
//        iBaseView.showTipDialog("正在充值...");
//        iWalletModel.recharge(mPayMoney, mRechargeType, new NormalCallback() {
//            @Override
//            public void responseOk(Object msgStr) {
//                iBaseView.onRechargeResult(true, msgStr);
//                iBaseView.hideTipDialog();
//            }
//
//            @Override
//            public void responseFail(String errorStr) {
//                iBaseView.onRechargeResult(false, errorStr);
//                iBaseView.hideTipDialog();
//            }
//        });
//    }
}
