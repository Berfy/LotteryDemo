package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;
import android.text.TextUtils;

import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.personal.view.iview.IVerifyCodeActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 验证邮箱的Presenter
 *
 * @author NorthStar
 * @date 2018/8/20 16:06
 */
public class VerifyCodePresenter {

    private Activity mContext;
    private static final String TAG = "VerifyCodePresenter";
    private IVerifyCodeActivity mVerifyCodeActivity;
    private IAccountModel iVerifyCodeModel;


    public VerifyCodePresenter(Activity activity, IVerifyCodeActivity iActivity) {
        mContext = activity;
        mVerifyCodeActivity = iActivity;
        iVerifyCodeModel = new AccountModel();
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
        mVerifyCodeActivity.showWaitingMsg(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_requesting));
        iVerifyCodeModel.requestVerifyCode(type, user, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                mVerifyCodeActivity.requestVerifyResult(true, msg);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mVerifyCodeActivity.requestVerifyResult(false, errorMsg);
            }
        });
    }

    /**
     * 验证注册的验证码
     *
     * @param code
     */
    public void verifyCode(String code, UserBean user) {
        mVerifyCodeActivity.showWaitingMsg(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_verifying));
        int loginType = user.getLoginWay();
        if (loginType == 1 && TextUtils.isEmpty(user.getEmail())) {
            mVerifyCodeActivity.verifyCodeResult(false, StaticResourceUtils.getStringResourceById(R.string.mine_email_null));
            return;
        }

        if (loginType == 0 && TextUtils.isEmpty(user.getMobile().getNumbers())) {
            mVerifyCodeActivity.verifyCodeResult(false, StaticResourceUtils.getStringResourceById(R.string.mine_phone_null));
            return;
        }

        if (loginType == 1 && !StringUtils.isEmail(user.getEmail())) {//邮箱验证
            mVerifyCodeActivity.verifyCodeResult(false, StaticResourceUtils.getStringResourceById(R.string.mine_email_wrong));
            return;
        }


        if (TextUtils.isEmpty(code)) {
            mVerifyCodeActivity.verifyCodeResult(false, StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_null));
            return;
        }

        iVerifyCodeModel.verifyVerificationCode(code, user, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mVerifyCodeActivity.verifyCodeResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mVerifyCodeActivity.verifyCodeResult(false, errorStr);
            }
        });
    }

    /**
     * 验证忘记密码的验证码
     *
     * @param captcha
     */
    public void pwdVerifyCode(String captcha, UserBean user) {
        mVerifyCodeActivity.showWaitingMsg(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_verifying));
        if (TextUtils.isEmpty(captcha)) {
//            mVerifyCodeActivity.pwdVerifyCodeResult(false, StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_null));
            return;
        }
        iVerifyCodeModel.verifyVerificationCaptcha(captcha, user, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
//                mVerifyCodeActivity.pwdVerifyCodeResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
//                mVerifyCodeActivity.pwdVerifyCodeResult(false, errorStr);
            }
        });
    }
}
