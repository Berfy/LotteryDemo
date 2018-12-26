package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.model.remote.impl.WalletModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.personal.view.iview.IKefuView;
import cn.zcgames.lottery.personal.view.iview.IWalletModel;
import cn.zcgames.lottery.personal.view.iview.IWithdrawView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 我的客服Presenter
 * @author Berfy
 * @date 2018.12.19
 */
public class KefuPresenter {

    private Activity mContext;
    private IKefuView iActivity;
    private IAccountModel mModel;

    public KefuPresenter(Activity activity, IKefuView iView) {
        mContext = activity;
        this.iActivity = iView;
        mModel = new AccountModel();
    }

    /**
     * 获取提现记录
     */
    public void getKefuInfo() {
        mModel.requestKefuInfo(new NormalCallback() {
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
}
