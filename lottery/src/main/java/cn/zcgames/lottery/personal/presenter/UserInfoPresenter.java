package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.personal.view.iview.IUploadView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 用户信息的presenter
 *
 * @author NorthStar
 * @date 2018/8/20 17:47
 */
public class UserInfoPresenter {

    private static final String TAG = "UserInfoPresenter";

    private Activity mContext;
    private IAccountModel mModel;
    private IBaseView mActivity;

    public UserInfoPresenter(Activity activity, IBaseView iActivity) {
        mContext = activity;
        mActivity = iActivity;
        mModel = new AccountModel();
    }

    /**
     * 获取用户身份信息
     */
    public void requestUserInfo() {
        mActivity.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        mModel.requestIdentityInfo(new NormalCallback() {
            @Override
            public void responseOk(Object bean) {
                mActivity.requestResult(true, bean);
                mActivity.hideTipDialog();
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mActivity.hideTipDialog();
                mActivity.requestResult(false, errorStr);
            }
        });
    }
}
