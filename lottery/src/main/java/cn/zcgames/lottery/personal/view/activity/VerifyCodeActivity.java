package cn.zcgames.lottery.personal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.country.view.CountryActivity;
import cn.zcgames.lottery.event.LoginEvent;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.VerifyCodePresenter;

import static cn.zcgames.lottery.app.ActivityConstants.*;

import cn.zcgames.lottery.personal.view.iview.IVerifyCodeActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 快速注册
 * 找回登录密码
 * 修改支付密码
 *
 * @author NorthStar
 * @date 2018/11/12 12:27
 */
public class VerifyCodeActivity extends BaseActivity implements IVerifyCodeActivity {

    @BindView(R.id.ll_tab_regsiter)
    LinearLayout tabLL;

    @BindView(R.id.title_tv)
    TextView titleTV;

    @BindView(R.id.tv_register_phone)
    TextView phoneTV;

    @BindView(R.id.tv_register_email)
    TextView emailTV;

    @BindView(R.id.et_phone)
    EditText mPhoneEt;

    @BindView(R.id.et_email)
    EditText mEmailEt;

    @BindView(R.id.et_verify_code)
    EditText mVerifyCodeEt;

    @BindView(R.id.get_verify_code)
    TextView mGetVerifyCodeTv;

    @BindView(R.id.cb_service)
    CheckBox mServiceCb;

    @BindView(R.id.tv_next)
    TextView mNextBtn;

    @BindView(R.id.tv_zone_code)
    TextView countryCode;

    @BindView(R.id.ll_service)
    View serviceView;

    @BindView(R.id.ll_register_phone)
    LinearLayout phoneLL;

    @BindView(R.id.ll_register_email)
    LinearLayout emailLL;

    private int mPageType;
    private int requestCode;
    private boolean mIsEmailOk;
    private boolean mIsCaptchaOk;
    private VerifyCodePresenter mPresenter;
    private static final String TAG = "VerifyCodeActivity";

    //倒计时器
    private MyCountDownTimer myCountDownTimer;
    private boolean isFindPassWord = false;
    private int registerType = 0;//注册类型 0:手机号注册,1:邮箱注册
    private String phoneCode = "";
    private String emailCode = "";
    private boolean issetPayPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_verify_code);
        initData();
        initView();
    }

    private void initData() {
        setButterKnife(this);//绑定注解
        EventBus.getDefault().register(this);
        mPresenter = new VerifyCodePresenter(this, this);
        myCountDownTimer = new MyCountDownTimer(AppConstants.COUNT_DOWN_MAX_TIME * 1000, 1000);
        mPageType = getIntent().getIntExtra(PARAM_KEY_VERIFIY_CAPTCHA_TYPE, PARAM_VALUE_REGISTER);
        requestCode = getIntent().getIntExtra(PARAM_KEY_PAY_PASSWORD_REQUESTCODE, -1);
        isFindPassWord = mPageType != PARAM_VALUE_REGISTER;
        int loginWay = (int) SharedPreferenceUtil.getPublic(MyApplication.getAppContext(), AppConstants.SPDATA_KEY_LOGIN_WAY, 0);
        registerType = isFindPassWord ? loginWay : 0;
    }

    private void initView() {
        setTitleBar();
        setTab();
    }

    private void setTab() {
        LogF.d(TAG, "初始化registerType==>" + registerType);
        UIHelper.showWidget(serviceView, !isFindPassWord);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        setTabState(registerType);
    }

    private void setTitleBar() {
        //判断是是注册还是找回密码
        String titleStr = "";
        switch (mPageType) {
            case PARAM_VALUE_REGISTER:
                titleStr = getString(R.string.mine_register);
                break;
            case PARAM_VALUE_FIND_PASSWORD:
                titleStr = getString(R.string.mine_find_psw);
                break;

            case PARAM_VALUE_SET_PAY_PASSWORD:
                titleStr = getString(R.string.verify_mobile);
                setPayPhone();
                registerType = 0;//支付密码的设置和修改都是手机号验证
                break;
            case PARAM_VALUE_UPDATE_PAY_PASSWORD:
                titleStr = getString(R.string.set_pay_password);
                setPayPhone();
                registerType = 0;//支付密码的设置和修改都是手机号验证
                break;
        }
        titleTV.setText(titleStr);
        tabLL.setVisibility(isFindPassWord ? View.GONE : View.VISIBLE);
    }

    private void setPayPhone() {
        UserBean user = MyApplication.getCurrLoginUser();
        if (user != null && user.getMobile() != null) {
            mPhoneEt.setText(user.getMobile().getNumbers());
            mPhoneEt.setFocusable(false);
            countryCode.setText(user.getMobile().getCode());
            countryCode.setClickable(false);
            issetPayPwd = true;
            mIsEmailOk = true;
        }
    }

    private void setListener() {
        boolean isPhone = registerType == 0;
        if(!issetPayPwd) setChangeListener(isPhone ? mPhoneEt : mEmailEt, 0);
        String verifyCode = isPhone ? phoneCode : emailCode;
        mVerifyCodeEt.setText(verifyCode);
        setChangeListener(mVerifyCodeEt, 1);
    }

    private void setChangeListener(EditText et, int type) {
        if (et.isFocused()) {
            et.setSelection(et.getText().toString().length());
        }
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (type) {
                    case 0:
                        mIsEmailOk = notEmpty(et);
                        break;
                    case 1:
                        mIsCaptchaOk = notEmpty(et);
                        if (registerType == 0) {
                            phoneCode = mIsCaptchaOk ? getElement(et) : "";
                        } else {
                            emailCode = mIsCaptchaOk ? getElement(et) : "";
                        }
                        break;
                }
                mNextBtn.setEnabled(mIsCaptchaOk && mIsEmailOk);
            }
        });
    }

    @OnClick({R.id.tv_next, R.id.title_back, R.id.get_verify_code, R.id.tv_service_page,
            R.id.tv_register_phone, R.id.tv_register_email, R.id.tv_zone_code})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(VerifyCodeActivity.this);
                break;

            case R.id.tv_register_phone:
                setTabState(0);
                break;

            case R.id.tv_register_email:
                setTabState(1);
                break;

            case R.id.tv_zone_code:
                CountryActivity.launch(VerifyCodeActivity.this);
                break;

            case R.id.tv_next://下一步
                if (!AppUtils.checkJump()) return;
                switch (mPageType) {
                    case PARAM_VALUE_SET_PAY_PASSWORD://设置
                        Intent data = new Intent();
                        data.putExtra(HttpHelper.PARAMS_CAPTCHA, phoneCode);
                        setResult(RESULT_OK, data);
                        goBack(mContext);
                        break;

                    case PARAM_VALUE_UPDATE_PAY_PASSWORD://更改
                        PayPasswordActivity.intoThisActivity(mContext, getCountryCode(),
                                getElement(mPhoneEt), phoneCode, requestCode);
                        break;

                    case PARAM_VALUE_FIND_PASSWORD:
                        verifyPwdCaptcha();
                        break;

                    default:
                        goRegister();
                }
                break;

            case R.id.get_verify_code://获取验证码
                if (!AppUtils.checkJump()) return;
                myCountDownTimer.start();//开启倒计时
                mVerifyCodeEt.requestFocus();
                //mPageType: 1.注册  2.找回登录密码  3.找回支付密码
                UserBean user = getUser();
                mPresenter.requestVerifyCode(mPageType, user);//获取验证码
                break;

            case R.id.tv_service_page://查看用户协议
                if (!AppUtils.checkJump()) return;
                UIHelper.gotoActivity(VerifyCodeActivity.this, ServicePageActivity.class, false);
                break;
        }
    }

    //去注册
    private void goRegister() {
        if (mServiceCb.isChecked()) {
            UserBean user = getUser();
            mPresenter.verifyCode(registerType == 0 ? phoneCode : emailCode, user);//获取验证码
        } else {
            ToastUtil.getInstances().showShort(CommonUtil.getString(this, R.string.tips_please_read_service));
        }
    }


    //跳向重置密码页面
    private void verifyPwdCaptcha() {
        UserBean user = getUser();
        mPresenter.pwdVerifyCode(registerType == 0 ? phoneCode : emailCode, user);
    }

    //设置密码
    private void startSetPassWordActivity(int pageType) {
        SharedPreferenceUtil.putPublic(MyApplication.getAppContext(), AppConstants.SPDATA_KEY_LOGIN_WAY, registerType);
        Intent i = new Intent(VerifyCodeActivity.this, SetPasswordActivity.class);
        i.putExtra(PARAM_KEY_VERIFIY_CAPTCHA_TYPE, pageType);
        i.putExtra(PARAM_KEY_CAPTCHA, registerType == 0 ? phoneCode : emailCode);
        if (registerType == 0) {//手机号
            i.putExtra(PARAM_KEY_MOBILE, getElement(mPhoneEt));
            i.putExtra(PARAM_KEY_COUNTRYCODE, getCountryCode());
        } else {
            i.putExtra(PARAM_KEY_EMAIL, getElement(mEmailEt));
        }
        startActivity(i);
    }


    private UserBean getUser() {
        UserBean user = new UserBean();
        user.setLoginWay(registerType);
        if (registerType == 0) {//0:手机号注册,1:邮箱注册
            user.setMobile(new Mobile(getCountryCode(), getElement(mPhoneEt)));
        } else {
            user.setEmail(getElement(mEmailEt));
        }
        return user;
    }

    //获取所需参数
    private String getElement(EditText et) {
        return et.getText().toString();
    }

    private String getCountryCode() {
        String codeStr = countryCode.getText().toString();
        return codeStr.substring(codeStr.indexOf("+") + 1, codeStr.length());
    }

    @Override
    public void requestVerifyResult(final boolean isOk, final Object msgStr) {
        runOnUiThread(() -> {
            UIHelper.showToast((String) msgStr);
            VerifyCodeActivity.this.hideWaitingDialog();
            if (!isOk) {
                setButtonReset();
            }
        });
    }

    @Override //跳转设置的登录密码界面
    public void verifyCodeResult(final boolean isOk, final Object msgStr) {
        runOnUiThread(() -> {
            UIHelper.showToast((String) msgStr);
            hideWaitingDialog();
            if (isOk) {
                startSetPassWordActivity(PARAM_VALUE_REGISTER);
            }
        });
    }

    @Override
    public void showWaitingMsg(final String msgStr) {
        runOnUiThread(() -> showWaitingDialog(VerifyCodeActivity.this, msgStr, false));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        goBack(VerifyCodeActivity.this);
    }

    //倒计时的定时器
    private int time = 0;

    private class MyCountDownTimer extends CountDownTimer {

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mGetVerifyCodeTv.setEnabled(false);
            String str = String.format(Locale.CHINA, getResources().getString(R.string.btn_get_code_with_time),
                    String.valueOf(AppConstants.COUNT_DOWN_MAX_TIME - (++time)));
            mGetVerifyCodeTv.setText(str.toLowerCase(Locale.getDefault()));
        }

        @Override
        public void onFinish() {
            setButtonReset();
        }
    }

    private void setButtonReset() {
        if (VerifyCodeActivity.this.isFinishing()) return;
        mGetVerifyCodeTv.setEnabled(true);
        mGetVerifyCodeTv.setText(getResources().getString(R.string.get_verify_code));
        myCountDownTimer.cancel();
        time = 0;
    }

    //切换title状态
    private void setTabState(int position) {
        if (!isFindPassWord) {
            if (position != registerType) {
                initWay(position);
                phoneTV.setTextColor(StaticResourceUtils.getColorResourceById(position == 0 ?
                        R.color.color_FFFFFF : R.color.color_999999));

                phoneTV.setBackgroundResource(position == 0 ? R.drawable.shape_btn_round_corner_red_left
                        : R.drawable.shape_btn_round_corner_white_left);

                emailTV.setTextColor(StaticResourceUtils.getColorResourceById(position == 1 ?
                        R.color.color_FFFFFF : R.color.color_999999));

                emailTV.setBackgroundResource(position == 1 ? R.drawable.shape_btn_round_corner_red_right
                        : R.drawable.shape_btn_round_corner_white_right);
                //                setButtonReset();
            }
        } else {
            initWay(position);
        }
        setListener();
    }

    private void initWay(int position) {
        registerType = position;
        phoneLL.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
        emailLL.setVisibility(position == 1 ? View.VISIBLE : View.INVISIBLE);
        mNextBtn.setEnabled(notEmpty(registerType == 0 ? mPhoneEt : mEmailEt) && notEmpty(mVerifyCodeEt));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isBackground(mContext)) {//后台
            myCountDownTimer.cancel();
            myCountDownTimer.onFinish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 用于设置和修改和验证密码回调
     *
     * @param context     前一个activity
     * @param actionType  1: 注册验证码  2: 重置登录密码验证码  3: 重置支付密码验证码
     *                    4: 邮箱设置验证吗 5: 手机号设置验证码 6.设置支付密码
     * @param RequestCode 请求码 0:设置支付密码,1:修改支付密码,2:输入支付密码并验证,不需要的传-1;
     */
    public static void intoThisActivity(Activity context, int actionType, int RequestCode) {
        Intent intent = new Intent(context, VerifyCodeActivity.class);
        intent.putExtra(PARAM_KEY_VERIFIY_CAPTCHA_TYPE, actionType);
        intent.putExtra(PARAM_KEY_PAY_PASSWORD_REQUESTCODE, RequestCode);
        context.startActivityForResult(intent, RequestCode);
    }

    public static void intoThisActivity(Fragment fromFragment, int actionType, int RequestCode) {
        Intent intent = new Intent(fromFragment.getActivity(), VerifyCodeActivity.class);
        intent.putExtra(PARAM_KEY_VERIFIY_CAPTCHA_TYPE, actionType);
        intent.putExtra(PARAM_KEY_PAY_PASSWORD_REQUESTCODE, RequestCode);
        fromFragment.startActivityForResult(intent, RequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.REQUEST_COUNTRY_CODE:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String countryNumber = bundle.getString("countryNumber");
                        countryCode.setText(countryNumber);
                    }
                    break;
                case REQUEST_CODE_RESET_PAYPASSWORD://修改
                    setResult(RESULT_OK, new Intent());
                    goBack(VerifyCodeActivity.this);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean notEmpty(EditText et) {
        return !TextUtils.isEmpty(getElement(et));
    }
}
