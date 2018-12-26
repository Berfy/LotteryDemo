package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.model.remote.callback.CommonCallback;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IWalletModel;
import cn.zcgames.lottery.model.remote.impl.WalletModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.base.IBaseView;

/**
 * 充值的presenter
 *
 * @author NorthStar
 * @date 2017/5/18 17:03
 */
public class RechargePresenter {

    private static final String TAG = "RechargePresenter";
    private Activity mContext;
    private IBaseView iActivity;
    private IWalletModel iModel;

    public RechargePresenter(Activity activity, IBaseView iView) {
        mContext = activity;
        this.iActivity = iView;
        iModel = new WalletModel();
    }

    /**
     * 充值
     *
     * @param amount    充值金额  以分为单位
     * @param payType   充值方式  100支付   200 微信
     * @param goodsName 选择的充值金额:10元
     */
    public void recharge(String goodsName, long amount, int payType) {
        iActivity.showTipDialog("正在充值...");
        iModel.recharge(goodsName, amount, payType, new CommonCallback() {
            @Override
            public void responseOk(Object msg) {
                iActivity.requestResult(true, msg);
                iActivity.hideTipDialog();
            }

            @Override
            public void responseFail(int code, String errorMsg) {
                if (code == HttpHelper.RESPONSE_CODE_TOKEN_INVALID
                        || code == HttpHelper.RESPONSE_CODE_STOLEN){
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }else if (code == HttpHelper.RESPONSE_CODE_FORBIDEN_USER
                        || code == HttpHelper.RESPONSE_CODE_FORBIDEN_CHANNEL) {
                    iActivity.requestResult(false, errorMsg);
                } else {
                    iActivity.requestResult(false, "系统繁忙，请稍后再试");
                }
                iActivity.hideTipDialog();
            }
        });
    }

}
