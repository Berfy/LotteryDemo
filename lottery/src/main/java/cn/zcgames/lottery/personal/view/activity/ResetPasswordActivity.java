package cn.zcgames.lottery.personal.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.country.view.CountryActivity;
import cn.zcgames.lottery.event.LoginEvent;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.SetPasswordPresenter;
import cn.zcgames.lottery.personal.view.iview.ISetPasswordActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 重置账户密码界面
 *
 * @author NorthStar
 * @date 2018/12/3 17:31
 */
public class ResetPasswordActivity extends BaseActivity implements ISetPasswordActivity {

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

    @BindView(R.id.setPsw_et_password)
    EditText mPswEt;

    @BindView(R.id.setPsw_et_confirmPassword)
    EditText mConfirmPswEt;

    @BindView(R.id.tv_next)
    TextView mNextBtn;

    @BindView(R.id.tv_zone_code)
    TextView countryCode;


    @BindView(R.id.ll_register_phone)
    LinearLayout phoneLL;

    @BindView(R.id.ll_register_email)
    LinearLayout emailLL;

    private boolean mIsEmailOk;
    private boolean mIsCaptchaOk;
    private boolean mIsPsw;
    private boolean mIsConfirmPsw;
    private SetPasswordPresenter mPresenter;
    private static final String TAG = "ResetPasswordActivity";

    //倒计时器
    private MyCountDownTimer myCountDownTimer;
    private int registerType = 0;//注册类型 0:手机号注册,1:邮箱注册
    private String phoneCode = "";
    private String emailCode = "";
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initData();
        setTab();
    }

    private void initData() {
        setButterKnife(this);//绑定注解
        mPresenter = new SetPasswordPresenter(this, this);
        myCountDownTimer = new MyCountDownTimer(AppConstants.COUNT_DOWN_MAX_TIME * 1000, 1000);
        registerType = (int) SharedPreferenceUtil.getPublic(MyApplication.getAppContext(), AppConstants.SPDATA_KEY_LOGIN_WAY, 0);

    }


    private void setTab() {
        titleTV.setText(getString(R.string.mine_find_psw));
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        LogF.d(TAG, "current registerType==>" + registerType);
        setTabState(registerType);
        isFirst = false;
    }

    private void setListener() {
        boolean isPhone = registerType == 0;
        setChangeListener(isPhone ? mPhoneEt : mEmailEt, 0);
        String verifyCode = isPhone ? phoneCode : emailCode;
        mVerifyCodeEt.setText(verifyCode);
        setChangeListener(mVerifyCodeEt, 1);
        setChangeListener(mPswEt, 2);
        setChangeListener(mConfirmPswEt, 3);
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
                    case 2:
                        mIsPsw = notEmpty(et);
                        break;
                    case 3:
                        mIsConfirmPsw = notEmpty(et);
                        break;
                }
                mNextBtn.setEnabled(mIsCaptchaOk && mIsEmailOk && mIsPsw && mIsConfirmPsw);
            }
        });
    }

    @OnClick({R.id.tv_next, R.id.title_back, R.id.get_verify_code,
            R.id.tv_register_phone, R.id.tv_register_email, R.id.tv_zone_code})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(ResetPasswordActivity.this);
                break;

            case R.id.tv_register_phone:
                setTabState(0);
                break;

            case R.id.tv_register_email:
                setTabState(1);
                break;

            case R.id.tv_zone_code:
                CountryActivity.launch(ResetPasswordActivity.this);
                break;

            case R.id.get_verify_code://获取验证码
                if (!AppUtils.checkJump()) return;
                myCountDownTimer.start();//开启倒计时
                mVerifyCodeEt.requestFocus();
                //mPageType: 1.注册  2.找回登录密码  3.找回支付密码
                mPresenter.requestVerifyCode(2, getUser());//获取验证码
                break;

            case R.id.tv_next://下一步
                if (!AppUtils.checkJump()) return;
                if (!getElement(mPswEt).equals(getElement(mConfirmPswEt))) {
                    showMessageDialog(getString(R.string.password_not_match), null);
                    return;
                }
                UserBean user = getUser();
                String password = getElement(mPswEt);
                String captcha = getElement(mVerifyCodeEt);
                mPresenter.resetPassword(user, captcha, password);
                break;
        }
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
    public void setPasswordSuccess(int type, final String msg) {
        runOnUiThread(() -> {
            hideWaitingDialog();
            ToastUtil.getInstances().showShort(msg);
            //发送登录成功的事件，销毁不需要的activity
            EventBus.getDefault().post(new LoginEvent(new UserBean()));
            finish();
            LoginActivity.intoThisActivity(this, true);

        });
    }

    @Override
    public void setPasswordFail(final String errorMsg) {
        runOnUiThread(() -> {
            hideWaitingDialog();
            UIHelper.showToast(errorMsg);
        });
    }

    @Override
    public void setPasswordDoing(String msg) {
        showWaitingDialog(ResetPasswordActivity.this, msg, false);
    }

    @Override
    public void requestVerifyResult(boolean isOk, Object msg) {
        UIHelper.showToast((String) msg);
        ResetPasswordActivity.this.hideWaitingDialog();
        if (!isOk) setButtonReset();
        /*LogF.d(TAG, "currentThread==>" + Thread.currentThread().getName());*/
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
        if (ResetPasswordActivity.this.isFinishing()) return;
        mGetVerifyCodeTv.setEnabled(true);
        mGetVerifyCodeTv.setText(getResources().getString(R.string.get_verify_code));
        myCountDownTimer.cancel();
        time = 0;
    }

    //切换title状态
    private void setTabState(int position) {
        if (isFirst || position != registerType) {
            initWay(position);
            phoneTV.setTextColor(StaticResourceUtils.getColorResourceById(position == 0 ?
                    R.color.color_FFFFFF : R.color.color_999999));

            phoneTV.setBackgroundResource(position == 0 ? R.drawable.shape_btn_round_corner_red_left
                    : R.drawable.shape_btn_round_corner_white_left);

            emailTV.setTextColor(StaticResourceUtils.getColorResourceById(position == 1 ?
                    R.color.color_FFFFFF : R.color.color_999999));

            emailTV.setBackgroundResource(position == 1 ? R.drawable.shape_btn_round_corner_red_right
                    : R.drawable.shape_btn_round_corner_white_right);
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
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean notEmpty(EditText et) {
        return !TextUtils.isEmpty(getElement(et));
    }
}
