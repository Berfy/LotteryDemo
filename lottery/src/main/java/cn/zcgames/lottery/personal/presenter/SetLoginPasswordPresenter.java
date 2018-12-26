package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.personal.view.iview.ISetLoginPasswordActivity;

/**
 * 设置登录密码的presenter
 *
 * @author NorthStar
 * @date 2017/5/9 17:18
 */
public class SetLoginPasswordPresenter {

    private Activity mContext;
    private ISetLoginPasswordActivity iActivity;
    private IAccountModel iModel;

    public SetLoginPasswordPresenter(Activity activity, ISetLoginPasswordActivity iActivity) {
        mContext = activity;
        this.iActivity = iActivity;
        iModel = new AccountModel();
    }

    /**
     * 检查登录密码
     */
    public void checkPassword() {
        UserBean userBean = MyApplication.getCurrLoginUser();
        if (userBean != null) {
            iActivity.checkPasswordResult(true);
        } else {
            iActivity.checkPasswordResult(false);
        }

    }
    /**
     * 设置登录密码
     *
     * @param psw
     * @param confirmPsw
     */
    public void setLoginPassword(String psw, String confirmPsw) {
        iModel.setLoginPassword(psw, confirmPsw, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iActivity.setLoginPasswordResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.setLoginPasswordResult(false, errorStr);
            }
        });
    }

    /**
     * 更改登录密码
     *
     * @param oldPsw
     * @param psw
     * @param confirmPsw
     */
    public void updateLoginPassword(String oldPsw, String psw, String confirmPsw) {
        iModel.updateLoginPassword(oldPsw, psw, confirmPsw, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iActivity.updateLoginPasswordResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.updateLoginPasswordResult(false, errorStr);
            }
        });
    }
}
