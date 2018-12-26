package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.personal.view.iview.ISetPasswordActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 设置登录密码的presenter
 *
 * @author NorthStar
 * @date 2018/8/20 17:46
 */
public class SetPasswordPresenter {

    private Activity mContext;
    private ISetPasswordActivity iSetPasswordActivity;
    private IAccountModel iSetPasswordModel;

    public SetPasswordPresenter(Activity activity, ISetPasswordActivity iActivity) {
        mContext = activity;
        this.iSetPasswordActivity = iActivity;
        iSetPasswordModel = new AccountModel();

    }


    /**
     * 获取验证码
     *
     * @param type 1: 注册验证码
     *             2: 重置登录密码验证码
     *             3: 重置支付密码验证码
     *             4: 邮箱设置验证码
     *             5: 手机号设置验证码
     *             6: 设置支付密码
     *             7: 修改支付密码接口
     */
    public void requestVerifyCode(int type, UserBean user) {
        iSetPasswordActivity.setPasswordDoing(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_requesting));
        iSetPasswordModel.requestVerifyCode(type, user, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                iSetPasswordActivity.requestVerifyResult(true, msg);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iSetPasswordActivity.requestVerifyResult(false, errorMsg);
            }
        });
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(UserBean user, String password) {
        iSetPasswordActivity.setPasswordDoing(StaticResourceUtils.getStringResourceById(R.string.mine_password_set_doing));
        iSetPasswordModel.setPassword(user, password, new NormalCallback() {
            @Override
            public void responseOk(Object user) {
                iSetPasswordActivity.setPasswordSuccess(ActivityConstants.PARAM_VALUE_REGISTER, "设置成功");
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iSetPasswordActivity.setPasswordFail(errorMsg);
            }
        });
    }

    /**
     * 找回密码
     *
     * @param captcha     验证码
     * @param newPassword 新密码
     */
    public void resetPassword(UserBean user, String captcha, String newPassword) {
        iSetPasswordActivity.setPasswordDoing(StaticResourceUtils.getStringResourceById(R.string.mine_password_set_doing));
        iSetPasswordModel.resetPassword(user, captcha, newPassword, new NormalCallback() {
            @Override
            public void responseOk(Object user) {
                iSetPasswordActivity.setPasswordSuccess(ActivityConstants.PARAM_VALUE_FIND_PASSWORD, "设置成功");
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iSetPasswordActivity.setPasswordFail(errorStr);
            }
        });
    }


    /**
     * 设置支付密码
     *
     * @param password 支付密码
     */
    public void setPayPassword(String captcha, String password) {
        iSetPasswordActivity.setPasswordDoing(StaticResourceUtils.getStringResourceById(R.string.mine_password_set_doing));
        iSetPasswordModel.setPayPassword(captcha, password, new NormalCallback() {
            @Override
            public void responseOk(Object user) {
                iSetPasswordActivity.setPasswordSuccess(ActivityConstants.PARAM_VALUE_SET_PAY_PASSWORD, "设置成功");
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iSetPasswordActivity.setPasswordFail(errorMsg);
            }
        });
    }

    /**
     * 修改支付密码
     *
     * @param mobile      手机号
     * @param captcha     验证码
     * @param newPassword 新密码
     */
    public void updatePayPassword(Mobile mobile, String captcha, String newPassword) {
        iSetPasswordActivity.setPasswordDoing(StaticResourceUtils.getStringResourceById(R.string.mine_password_set_doing));
        iSetPasswordModel.updatePayPassword(mobile, captcha, newPassword, new NormalCallback() {
            @Override
            public void responseOk(Object user) {
                iSetPasswordActivity.setPasswordSuccess(ActivityConstants.PARAM_VALUE_UPDATE_PAY_PASSWORD, "修改成功");
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iSetPasswordActivity.setPasswordFail(errorStr);
            }
        });
    }
}
