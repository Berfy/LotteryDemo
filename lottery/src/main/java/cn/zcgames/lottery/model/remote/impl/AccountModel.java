package cn.zcgames.lottery.model.remote.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;

import static cn.zcgames.lottery.app.AppConstants.*;

import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.bean.response.LoginRegistBean;
import cn.zcgames.lottery.bean.response.ResponseBillDetailFastThree;
import cn.zcgames.lottery.bean.response.ResponseMessageBen;
import cn.zcgames.lottery.bean.response.ResultChaseBillBeanNew;
import cn.zcgames.lottery.personal.model.KefuInfo;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UrlBean;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.bean.response.ResponseBaseBean;
import cn.zcgames.lottery.bean.response.ResponseBillBean;
import cn.zcgames.lottery.bean.response.ResponseUserData;
import cn.zcgames.lottery.bean.response.ResponseNormalBean;
import cn.zcgames.lottery.bean.response.ResultChaseBillBean;
import cn.zcgames.lottery.event.LoginEvent;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.utils.okhttp.OkHttpUtils;
import cn.zcgames.lottery.utils.okhttp.callback.GenericsCallback;
import cn.zcgames.lottery.utils.okhttp.callback.JsonGenericsSerializer;
import cn.zcgames.lottery.utils.okhttp.callback.StringCallback;
import okhttp3.Call;


/**
 * Created by admin on 2017/5/16.
 */

public class AccountModel implements IAccountModel {

    private static final String TAG = "AccountModel";

    /**
     * @param type     1: 注册验证码  2: 重置登录密码验证码  3: 重置支付密码验证码 4: 邮箱设置验证吗 5: 手机号设置验证码
     * @param user
     * @param callback
     */
    @Override//获取验证码  type
    public void requestVerifyCode(int type, final UserBean user, NormalCallback callback) {
        if (type == 0) {
            LogF.d(TAG, "没有传入用于区分注册还是找回密码的type ");
            return;
        }
        int loginType = user.getLoginWay();
        if (VerifyLoginWay(user, callback, loginType)) return;
        Map<String, Object> params = new HashMap<>();
        setLoginWayParams(user, params, loginType);
        params.put("type", type);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_REQUEST_CODE)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    //登录
    @Override
    public void login(final UserBean user, final NormalCallback callback) {
        int loginType = user.getLoginWay();
        user.setLoginType(loginType);
        if (TextUtils.isEmpty(user.getPassword())) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_null));
            return;
        }
        if (VerifyLoginWay(user, callback, loginType)) return;
        Map<String, Object> params = new HashMap<>();
        params.put(HttpHelper.PARAMS_PASSWORD, user.getPassword());
        setLoginWayParams(user, params, loginType);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);

        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_LOGIN)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<LoginRegistBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "onError: login error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LoginRegistBean response, int id) {
                        Log.d(TAG, "onResponse: msg is " + response.toString());
                        if (response.isOk() && null != response.getPayload()) {
                            String token = response.getPayload().getToken();
                            user.setTokenId(token);

                            String userId = response.getPayload().getPlayer_id();
                            user.setPlayer_id(userId);
                            LogF.d("SharedPreferencesUtils", "登录后的userId==>" + userId);

                            String email = response.getPayload().getEmail();
                            if (!TextUtils.isEmpty(email)) user.setEmail(email);
                            Mobile mobile = response.getPayload().getMobile();
                            if (mobile != null && !TextUtils.isEmpty(mobile.getNumbers())) {
                                user.setIs_mobile_set("2");
                            }
                            user.setIsPayPwd(response.getPayload().getIs_pay_pswd_set());
                            MyApplication.updateCurrLoginUser(user);//用户登录成功之后，将信息保存在本地，用于自动登录
                            setMQTTUser();
                            EventBus.getDefault().post(new LoginEvent(user));
                            LogF.d(TAG, "loginUser==>" + MyApplication.getCurrLoginUser().toString());
                            callback.responseOk(user);
                        } else {
                            LogF.d("loginError", "onResponse--loginError==>" + response.getMsg());
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    private void setMQTTUser() {
        if (null != MyApplication.getCurrLoginUser()) {
            UserBean userBean = MyApplication.getCurrLoginUser();
            LogF.d(TAG, "登陆成功" + GsonUtil.getInstance().toJson(userBean));
            //开启订阅话题推送
            if (!TextUtils.isEmpty(userBean.getPlayerId()) && MyApplication.isPhoneState) {
                MyApplication.getInstance().initMQTT(userBean.getPlayerId());
            }
        }
    }

    /**
     * 授权。如果授权成功，则获取用户信息
     *
     * @param platform
     */
    public void loginThirdPlatform(final SHARE_MEDIA platform, final Activity activity, final NormalCallback callback) {
        UMShareAPI.get(activity).doOauthVerify(activity, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                boolean loginOk = false;
                if (platform == SHARE_MEDIA.QQ) {//QQ平台
                    if (!TextUtils.isEmpty(data.get(QQ_UUID))) {
                        loginOk = true;
                    }
                } else if (platform == SHARE_MEDIA.WEIXIN) {
                    if (!TextUtils.isEmpty(data.get(WEIXIN_UUID))) {
                        loginOk = true;
                    }
                }

                if (loginOk) {
                    getThirdPlatformUserInfo(platform, activity, callback);
                } else {
                    callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_login_auth_fail));
                }

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_login_auth_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_login_cancel));
            }
        });

    }

    /**
     * 获取用户信息
     *
     * @param platform
     */
    private void getThirdPlatformUserInfo(SHARE_MEDIA platform,
                                          Activity activity,
                                          final NormalCallback callback) {
        UMShareAPI.get(MyApplication.getAppContext())
                .getPlatformInfo(activity, platform, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        UserBean user = new UserBean();
                        if (share_media == SHARE_MEDIA.QQ) {//QQ平台
                            user.setNickname(map.get(QQ_NAME));
                            user.setTokenId(map.get(QQ_TOKEN_ID));
                            user.setAvatar(map.get(QQ_ICON_URL));
                            //                    user.setUuid(map.get(QQ_UUID));
                        } else if (share_media == SHARE_MEDIA.WEIXIN) {//微信平台
                            user.setNickname(map.get(WEIXIN_NAME));
                            user.setTokenId(map.get(WEIXIN_TOKEN_ID));
                            user.setAvatar(map.get(WEIXIN_ICON_URL));
                            //                    user.setUuid(map.get(WEIXIN_UUID));
                        }
                        user.setLoginType(USER_LOGIN_TYPE_THIRD_PLATFORM);
                        callback.responseOk(user);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
    }

    @Override
    public void requestMinePageData(final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }

        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_MINE_FRAGMENT)
                .content("")
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .build()
                .execute(new GenericsCallback<ResponseUserData>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: login error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseUserData response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getUser());
                        } else if (response.isStolen()) {
                            String stolen = StaticResourceUtils.getStringResourceById(R.string.tips_request_stolen);
                            callback.responseFail(true, stolen);
                        } else {
                            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.tips_request_fail));
                        }
                    }
                });

    }


    @Override//校验注册验证码
    public void verifyVerificationCode(String captcha, final UserBean user, final NormalCallback callback) {
        int loginType = user.getLoginWay();
        if (VerifyLoginWay(user, callback, loginType)) return;
        Map<String, Object> params = new HashMap<>();
        setLoginWayParams(user, params, loginType);
        params.put(HttpHelper.PARAMS_CAPTCHA, captcha);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);

        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_REGISTER)
                .content(jsonParam)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: response is " + response);
                        ResponseBaseBean bean = GsonUtils.formatLoginResponseResult(response);
                        if (bean.isOk()) {
                            //                            MyApplication.setTokenId(bean.getToken());
                            callback.responseOk(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_verify_ok));
                        } else {
                            callback.responseFail(false, bean.getMsg());
                        }
                    }
                });

    }

    @Override//校验修改密码验证码
    public void verifyVerificationCaptcha(String captcha, UserBean user, NormalCallback callback) {
        Map<String, Object> params = new HashMap<>();
        int loginType = user.getLoginWay();
        if (VerifyLoginWay(user, callback, loginType)) return;
        setLoginWayParams(user, params, loginType);
        params.put(HttpHelper.PARAMS_CAPTCHA, captcha);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_FORGIT_PWD)
                .content(jsonParam)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: response is " + response);
                        ResponseBaseBean bean = GsonUtils.formatLoginResponseResult(response);
                        if (bean.isOk()) {
                            //                            MyApplication.setTokenId(bean.getToken());
                            callback.responseOk(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_verify_ok));
                        } else {
                            callback.responseFail(false, bean.getMsg());
                        }
                    }
                });

    }

    @Override
    public void bindEmail(String code, String email, final NormalCallback callback) {
        if (TextUtils.isEmpty(email)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_email_null));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_null));
            return;
        }

        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(HttpHelper.PARAMS_EMAIL, email);
        params.put(HttpHelper.PARAMS_CAPTCHA, code);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_SET_EMAIL)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<LoginRegistBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "bindEmail onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LoginRegistBean response, int id) {
                        if (response.isOk()) {
                            UserBean user = MyApplication.getCurrLoginUser();
                            String emailStr = response.getPayload().getEmail();
                            user.setEmail(emailStr);
                            LogF.d("BindingPhoneActivity", "email==>" + emailStr);
                            MyApplication.updateCurrLoginUser(user);
                            callback.responseOk(StaticResourceUtils.getStringResourceById(R.string.mine_band_ok));
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void bindPhone(String captcha, Mobile mobile, final NormalCallback callback) {
        if (TextUtils.isEmpty(mobile.getNumbers())) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_phone_null));
            return;
        }

        if (TextUtils.isEmpty(captcha)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_null));
            return;
        }

        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, Object> params = new HashMap<>();

        params.put(HttpHelper.PARAMS_MOBILE, mobile);
        params.put(HttpHelper.PARAMS_CAPTCHA, captcha);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_SET_MOBILE)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<LoginRegistBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "bindEmail onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LoginRegistBean response, int id) {
                        if (response.isOk()) {
                            UserBean user = MyApplication.getCurrLoginUser();
                            user.setMobile(mobile);
                            user.setIs_mobile_set("2");
                            MyApplication.updateCurrLoginUser(user);
                            LogF.d(TAG, "userBean==>" + MyApplication.getCurrLoginUser().toString());
                            callback.responseOk(StaticResourceUtils.getStringResourceById(R.string.mine_band_ok));
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }


    //修改登录密码
    @Override
    public void updateLoginPassword(String oldPsw, String newPsw, String confirmPsw, final NormalCallback callback) {
        if (TextUtils.isEmpty(oldPsw)) {
            callback.responseFail(false, "原密码不能为空");
            return;
        }
        if (USER_PASSWORD_MIN_LENGTH > newPsw.length() || newPsw.length() > USER_PASSWORD_MAX_LENGTH) {
            callback.responseFail(false, "密码必须在6至16个字符之间");
            return;
        }
        if (TextUtils.isEmpty(confirmPsw)) {
            callback.responseFail(false, "确认新密码不能为空");
            return;
        }
        if (!newPsw.equals(confirmPsw)) {
            callback.responseFail(false, "请检查重复密码是否相同");
            return;
        }
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(HttpHelper.PARAMS_OLD_PASSWORD, oldPsw);
        params.put(HttpHelper.PARAMS_NEW_PASSWORD, newPsw);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_CHANGE_PASSWORD)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getMsg());
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void setLoginPassword(String psw, String confirmPsw, final NormalCallback callback) {

        //        if (TextUtils.isEmpty(psw)) {
        //            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_null));
        //            return;
        //        }
        //        if (USER_PASSWORD_MIN_LENGTH > psw.length() || psw.length() > USER_PASSWORD_MAX_LENGTH) {
        //            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_length_wrong));
        //            return;
        //        }
        //
        //        if (!psw.equals(confirmPsw)) {
        //            callback.responseFail(false, "两次输入的新密码不一致");
        //            return;
        //        }
        //
        //        String phone = MyApplication.getCurrLoginUser().getUserEmail();
        //        if (TextUtils.isEmpty(phone)) {
        //            callback.responseFail(false, "请先绑定手机号");
        //            return;
        //        }
        //
        //        setPassword(ActivityConstants.PARAM_VALUE_FIND_PASSWORD, phone, psw, new NormalCallback() {
        //            @Override
        //            public void responseOk(Object user) {
        //                callback.responseOk(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_verify_ok));
        //            }
        //
        //            @Override
        //            public void responseFail(boolean isNeedLogin, String errorStr) {
        //                callback.responseFail(false, errorStr);
        //            }
        //        });
    }

    //注册后设置新密码
    @Override
    public void setPassword(UserBean user, String password, final NormalCallback callback) {
        if (TextUtils.isEmpty(password)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_null));
            return;
        }
        if (USER_PASSWORD_MIN_LENGTH > password.length() || password.length() > USER_PASSWORD_MAX_LENGTH) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_length_wrong));
            return;
        }
        int loginType = user.getLoginWay();
        user.setLoginType(loginType);
        if (VerifyLoginWay(user, callback, loginType)) return;
        Map<String, Object> params = new HashMap<>();
        params.put(HttpHelper.PARAMS_PASSWORD, password);
        params.put(HttpHelper.PARAMS_SERVANT_ID, MyApplication.getInstance().getApkKeFuInfo());
        setLoginWayParams(user, params, loginType);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);

        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_SET_PASSWORD)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<LoginRegistBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(LoginRegistBean response, int id) {
                        LoginRegistBean.UserBean payload = response.getPayload();
                        if (response.isOk() && null != payload) {
                            setUserInfo(payload, user.getMobile(), loginType);
                            callback.responseOk(StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_verify_ok));
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    //设置用户数据
    private void setUserInfo(LoginRegistBean.UserBean payload, Mobile mobile, int loginType) {
        UserBean userBean = new UserBean();
        userBean.setLoginType(USER_LOGIN_TYPE_EMAIL);
        if (loginType == 0) {
            userBean.setMobile(mobile);
            userBean.setIs_mobile_set("2");
        } else {
            userBean.setEmail(payload.getEmail());
        }
        userBean.setTokenId(payload.getToken());
        userBean.setPlayer_id(payload.getPlayer_id());
        MyApplication.updateCurrLoginUser(userBean);//用户登录成功之后，将信息保存在本地，用于自动登录
        EventBus.getDefault().post(new LoginEvent(userBean));
        setMQTTUser(); //注册设置密码后登录mqtt
    }


    @Override//找回登录密码
    public void resetPassword(final UserBean user, String captcha, String psw, final NormalCallback callback) {
        if (TextUtils.isEmpty(psw)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_null));
            return;
        }
        if (USER_PASSWORD_MIN_LENGTH > psw.length() || psw.length() > USER_PASSWORD_MAX_LENGTH) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_length_wrong));
            return;
        }

        int loginWay = user.getLoginWay();
        Map<String, Object> params = new HashMap<>();
        setLoginWayParams(user, params, loginWay);
        params.put(HttpHelper.PARAMS_CAPTCHA, captcha);
        params.put(HttpHelper.PARAMS_NEW_PASSWORD, psw);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_RESET_PASSWORD)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }


    //设置登录方式
    private void setLoginWayParams(UserBean user, Map<String, Object> params, int loginType) {
        String wayKey = loginType == 0 ? HttpHelper.PARAMS_MOBILE : HttpHelper.PARAMS_EMAIL;
        Object wayValue = loginType == 0 ? user.getMobile() : user.getEmail();
        params.put(wayKey, wayValue);
    }

    //校验登录参数正确性
    private boolean VerifyLoginWay(UserBean user, NormalCallback callback, int logintype) {
        if (logintype == 1 && TextUtils.isEmpty(user.getEmail())) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_email_null));
            return true;
        }

        if (logintype == 1 && !StringUtils.isEmail(user.getEmail())) {//验证是否是邮箱
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_email_wrong));
            return true;
        }

        if (logintype == 0 && TextUtils.isEmpty(user.getMobile().getNumbers())) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_phone_null));
            return true;
        }
        return false;
    }


    //设置支付密码
    @Override
    public void setPayPassword(String captcha, String password, NormalCallback callback) {
        if (TextUtils.isEmpty(password)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_null));
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(HttpHelper.PARAMS_PAY_PSWD, password);
        params.put(HttpHelper.PARAMS_CAPTCHA, captcha);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_SET_PAY_PASSWORD)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            UserBean user = MyApplication.getCurrLoginUser();
                            user.setIsPayPwd("2");
                            MyApplication.updateCurrLoginUser(user);
                            callback.responseOk(response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }


    //修改支付密码
    @Override
    public void updatePayPassword(Mobile mobile, String captcha, String newPassword, NormalCallback callback) {
        if (TextUtils.isEmpty(newPassword)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_password_null));
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put(HttpHelper.PARAMS_MOBILE, mobile);
        params.put(HttpHelper.PARAMS_CAPTCHA, captcha);
        params.put(HttpHelper.PARAMS_NEW_PASSWORD, newPassword);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_RESET_PAY_PASSWORD)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestIdentityInfo(final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_GET_USER_INFO)
                .content("")
                .build()
                .execute(new GenericsCallback<ResponseUserData>(new JsonGenericsSerializer()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseUserData response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getUser());
                        } else if (response.isStolen()) {
                            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void setIdentityInfo(String name, String id, final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(name)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.tips_realname_null));
            return;
        }
        if (TextUtils.isEmpty(id)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.tips_id_null));
            return;
        }
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put(HttpHelper.PARAMS_REAL_NAME, name);
        param.put(HttpHelper.PARAMS_ID, id);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);

        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_SET_IDENTIFY_INFO)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getMsg());
                        } else if (response.isStolen()) {
                            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestOrderRecord(int startPageIdx, long ts, int status, final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("page", startPageIdx);
        if (startPageIdx == 1) param.put(HttpHelper.PARAMS_TS, 0);
        param.put(HttpHelper.PARAMS_PAGE_SIZE, 20);
        param.put(HttpHelper.PARAMS_STATUS, status);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);

        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_PERSONAL_BILLS)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<ResponseBillBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseBillBean response, int id) {
                        if (response.isOk()) {
                            if (response.getData() == null) {
                                callback.responseFail(false, response.getMsg());
                            } else if (response.isStolen()) {
                                setUnLogin(callback, response.getMsg());
                            } else {
                                callback.responseOk(response.getData());
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestDefaultMobileCode(final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_DEFAULT_CODE)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content("")
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void verifyPhone(String mOldMobile, String s, final NormalCallback callback) {
        if (TextUtils.isEmpty(s)) {
            callback.responseFail(false, StaticResourceUtils.getStringResourceById(R.string.mine_verify_code_null));
            return;
        }

        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(HttpHelper.PARAMS_MOBILE, mOldMobile);
        params.put(HttpHelper.PARAMS_CAPTCHA, s);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_VERIFY_PHONE)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "verifyPhone onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk("验证码通过");
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestChaseBill(String status, int startPageIndex, int pageMax, final NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(HttpHelper.PARAMS_START_PAGE, startPageIndex);
        param.put(HttpHelper.PARAMS_PAGE_SIZE, pageMax);
        param.put(HttpHelper.PARAMS_STATUS, status);
        String jsonParam = GsonUtils.getGsonInstance().toJson(param);

        Log.e(TAG, "requestChaseBill: jsonParam is " + jsonParam);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_CHASE_BILL)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResultChaseBillBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "verifyPhone onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultChaseBillBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getData());
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestChaseBillNew(int page, int pageSize, int status, long ts, NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("page", page);
        param.put(HttpHelper.PARAMS_PAGE_SIZE, pageSize);
        if (page == 1) param.put(HttpHelper.PARAMS_TS, 0);
        param.put(HttpHelper.PARAMS_STATUS, status);
        String jsonParam = GsonUtils.getGsonInstance().toJson(param);

        Log.e(TAG, "requestChaseBill: jsonParam is " + jsonParam);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_CHASE_BILL_NEW)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResultChaseBillBeanNew>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "verifyPhone onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResultChaseBillBeanNew response, int id) {
                        if (response.isOk()) {
                            callback.responseOk(response.getPayload());
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestChaseDetail(String lotteryName, String orderId, NormalCallback callback) {

        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        String url = "";
        switch (lotteryName) {
            case LOTTERY_TYPE_FAST_3:
            case LOTTERY_TYPE_FAST_3_JS:
            case LOTTERY_TYPE_FAST_3_HB:
            case LOTTERY_TYPE_FAST_3_NEW:
            case LOTTERY_TYPE_11_5:
            case LOTTERY_TYPE_11_5_OLD:
            case LOTTERY_TYPE_11_5_YUE:
            case LOTTERY_TYPE_11_5_LUCKY:
            case LOTTERY_TYPE_11_5_YILE:
            case LOTTERY_TYPE_ALWAYS_COLOR://重庆时时彩
            case LOTTERY_TYPE_ALWAYS_COLOR_NEW://新时时彩
                url = HttpHelper.PRE_ORDER_DETAIL;
                break;
        }

        Map<String, Object> param = new HashMap<>();
        param.put(HttpHelper.PARAMS_ORDER_ID, orderId);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);
        Log.e(TAG, "requestOrderDetail: jsonStr is " + paramStr);
        Log.e(TAG, "requestOrderDetail: url is " + url);
        Log.e(TAG, "lotteryType: is " + lotteryName);
        OkHttpUtils.postString()
                .url(url)
                .content(paramStr)
                .addHeader("LotteryName", lotteryName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: response is " + response);
                        ResponseBillDetailFastThree bean = GsonUtils.getGsonInstance().fromJson(response, new TypeToken<ResponseBillDetailFastThree>() {
                        }.getType());
                        if (bean.isOk()) {
                            callback.responseOk(bean.getData());
                        } else if (bean.getStatus().equals(HttpHelper.RESPONSE_STATUS_UN_LOGIN)) {
                            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
                        } else {
                            callback.responseFail(false, bean.getMsg());
                        }
                    }
                });
    }

    @Override
    public void uploadFile(String name, String filePath, NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_request_token_lost));
            return;
        }
        File file = new File(filePath);
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHelper.HEADER_PARAMS_CONTENT_TYPE, "multipart/form-data");
        LogF.d(TAG, "name==>" + name + " ,file.getName()" + file.getName() + " ,file" + file);
        OkHttpUtils.post()
                .url(HttpHelper.ACCOUNT_UPLOAD_FILE)
                .addFile(name, file.getName(), file)
                .headers(headers)
                .build()
                .execute(new GenericsCallback<UrlBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "verifyPhone onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(UrlBean response, int id) {
                        if (response.isOk()) {
                            String url = "";
                            UrlBean.UrlData payload = response.getPayload();
                            if (payload != null) url = payload.getUrl();
                            if (!TextUtils.isEmpty(url)) callback.responseOk(url);
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void sendFile(String type, List<String> urls, NormalCallback callback) {
        Map<String, Object> param = new HashMap<>();
        param.put(HttpHelper.PARAMS_FILE_TAG, type);
        param.put(HttpHelper.PARAMS_URLS, urls);
        String jsonParam = GsonUtils.getGsonInstance().toJson(param);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_SEND_FILE)
                //                .addHeader(HEADER_PARAMS_AUTHORIZATION, "Bearer " + tokenStr)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<UrlBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "verifyPhone onError: error : " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(UrlBean response, int id) {
                        if (response.isOk()) {
                            callback.responseOk("上传成功");
                        } else if (response.isStolen()) {
                            setUnLogin(callback, response.getMsg());
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestMessage(int startPageIdx, long ts, NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("page", startPageIdx);
        if (startPageIdx == 1) param.put(HttpHelper.PARAMS_TS, 0);
        param.put(HttpHelper.PARAMS_PAGE_SIZE, 20);
        String paramStr = GsonUtils.getGsonInstance().toJson(param);
        OkHttpUtils.postString()
                .url(HttpHelper.RESULT_SYSTEM_MSG_URL)
                .content(paramStr)
                .build()
                .execute(new GenericsCallback<ResponseMessageBen>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(ResponseMessageBen response, int id) {
                        if (response.isOk()) {
                            if (response.getData() == null) {
                                callback.responseFail(false, response.getMsg());
                            } else if (response.isStolen()) {
                                setUnLogin(callback, response.getMsg());
                            } else {
                                callback.responseOk(response.getData());
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void requestKefuInfo(NormalCallback callback) {
        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            callback.responseFail(true, StaticResourceUtils.getStringResourceById(R.string.tips_no_login));
            return;
        }
        OkHttpUtils.get()
                .url(HttpHelper.ACCOUNT_KEFU_INFO)
                .build()
                .execute(new GenericsCallback<KefuInfo>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: error is " + e.getMessage());
                        callback.responseFail(false, HttpHelper.getErrorTipsByResponseCode(e.getMessage()));
                    }

                    @Override
                    public void onResponse(KefuInfo response, int id) {
                        if (response.isOk()) {
                            if (response.getPayload() == null) {
                                callback.responseFail(false, response.getMsg());
                            } else if (response.isStolen()) {
                                setUnLogin(callback, response.getMsg());
                            } else {
                                callback.responseOk(response.getPayload());
                            }
                        } else {
                            callback.responseFail(false, response.getMsg());
                        }
                    }
                });
    }

    //设置未登录事件
    private void setUnLogin(NormalCallback callback, String msg) {
        MyApplication.getInstance().unSubscribeAllTopic();
        callback.responseFail(true, msg);
    }
}
