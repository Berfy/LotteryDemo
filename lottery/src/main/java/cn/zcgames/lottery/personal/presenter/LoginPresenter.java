package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.personal.view.iview.ILoginActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.personal.view.iview.ILoginActivity.LOGIN_OK_TYPE_OUR_SERVER;
import static cn.zcgames.lottery.personal.view.iview.ILoginActivity.LOGIN_OK_TYPE_THIRDPLAT;

/**
 * 用于登录逻辑的Presenter
 *
 * @author NorthStar
 * @date 2018/8/20 15:58
 */
public class LoginPresenter {

    private Activity mContext;
    private ILoginActivity mLoginActivity;
    private IAccountModel mLoginModel;

    public LoginPresenter(Activity activity, ILoginActivity iView) {
        mContext = activity;
        mLoginActivity = iView;
        mLoginModel = new AccountModel();
    }

    /**
     * 登录l
     *
     * @param user
     */
    public void login(UserBean user) {
        mLoginActivity.loginLoading(StaticResourceUtils.getStringResourceById(R.string.mine_login_doing));

        mLoginModel.login(user, new NormalCallback() {
            @Override
            public void responseOk(Object user) {
                mLoginActivity.hideDialog();
                mLoginActivity.loginSuccess((UserBean) user, LOGIN_OK_TYPE_OUR_SERVER);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mLoginActivity.hideDialog();
                mLoginActivity.loginFail(errorStr);
            }
        });
    }

    /**
     * 第三方平台登录
     *
     * @param platform
     */
    public void loginThirdPlatform(SHARE_MEDIA platform, Activity activity) {
        mLoginActivity.loginLoading(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        mLoginModel.loginThirdPlatform(platform, activity, new NormalCallback() {
            @Override
            public void responseOk(Object user) {
                mLoginActivity.hideDialog();
                mLoginActivity.loginSuccess((UserBean) user, LOGIN_OK_TYPE_THIRDPLAT);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mLoginActivity.hideDialog();
                mLoginActivity.loginFail(errorStr);
            }
        });
    }
}
