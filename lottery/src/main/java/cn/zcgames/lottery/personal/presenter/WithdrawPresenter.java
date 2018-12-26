package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.personal.view.iview.IWalletModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.WalletModel;
import cn.zcgames.lottery.personal.view.iview.IWithdrawView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 提现业务的presenter
 *
 * @author NorthStar
 * @date 2018/8/29 12:09
 */
public class WithdrawPresenter {
    private Activity mContext;
    private IWithdrawView iActivity;
    private IWalletModel iWalletModel;
    private IAccountModel mModel;

    public WithdrawPresenter(Activity activity, IWithdrawView iView) {
        mContext = activity;
        this.iActivity = iView;
        this.iWalletModel = new WalletModel();
        mModel = new AccountModel();
    }

    /**
     * 提现
     *
     * @param amount      提现金额  以分为单位
     * @param payType     提现方式  100 支付宝  200 微信
     * @param payPassword 支付码
     */
    public void withdraw(long amount, int payType, String payPassword) {
        iActivity.showTipDialog("正在申请...");
        iWalletModel.withdraw(amount, payType, payPassword, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                iActivity.requestResult(true, msg);
                iActivity.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.requestResult(false, errorMsg);
                iActivity.hideTipDialog();
            }
        });
    }


    /**
     * 获取提现记录
     */
    public void requestWithdrawRecord(int startPageIdx, long ts) {
        iWalletModel.requestWithdrawRecord(startPageIdx, ts, new NormalCallback() {
            @Override
            public void responseOk(Object result) {
                iActivity.requestResult(true, result);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.requestResult(false, errorStr);
            }
        });
    }

    /**
     * 获取用户收款码信息
     */
    public void requestUserInfo() {
        iActivity.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        mModel.requestIdentityInfo(new NormalCallback() {
            @Override
            public void responseOk(Object bean) {
                iActivity.getWithdrawWay(true, bean);
                iActivity.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.hideTipDialog();
                iActivity.getWithdrawWay(false, errorStr);
            }
        });
    }
}
