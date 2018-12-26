package cn.zcgames.lottery.personal.presenter;


import android.app.Activity;

import org.greenrobot.eventbus.EventBus;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.personal.view.iview.IBindingPhoneActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 绑定手机号的presenter
 *
 * @author NorthStar
 * @date 2018/8/20 17:16
 */
public class BindContactsPresenter {

    private static final String TAG = "BindingMobilePresenter";

    private Activity mContext;
    private IBindingPhoneActivity mActivity;
    private IAccountModel iModel;


    public BindContactsPresenter(Activity activity, IBindingPhoneActivity iactivity) {
        mContext = activity;
        mActivity = iactivity;
        iModel = new AccountModel();
    }


    /**
     * 获取验证码
     * type==4:绑定邮箱或手机号
     */
    public void requestVerifyCode(int type,UserBean user) {
        mActivity.showWaitingMsg(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_requesting));
        iModel.requestVerifyCode(type, user, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mActivity.requestVerifyResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mActivity.requestVerifyResult(false, errorStr);
            }
        });
    }

    /**
     * 校验验证码并绑定手机号
     *
     * @param captcha     验证码
     * @param phoneNumber 手机号
     * @param countryCode 国家码
     */
    public void bindPhone(String captcha, String phoneNumber, String countryCode) {
        mActivity.showWaitingMsg(StaticResourceUtils.getStringResourceById(R.string.mine_banding));
        iModel.bindPhone(captcha, new Mobile(countryCode, phoneNumber), new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mActivity.bindResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mActivity.bindResult(false, errorStr);
            }
        });
    }

    /**
     * 校验验证码并绑定邮箱
     *
     * @param captcha  验证码
     * @param email 邮箱
     */
    public void bindEmail(String captcha, String email) {
        mActivity.showWaitingMsg(StaticResourceUtils.getStringResourceById(R.string.mine_banding));
        iModel.bindEmail(captcha, email, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mActivity.bindResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mActivity.bindResult(false, errorStr);
            }
        });
    }
}
