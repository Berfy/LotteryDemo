package cn.zcgames.lottery.personal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.BindContactsPresenter;
import cn.zcgames.lottery.personal.view.iview.IBindingPhoneActivity;

/**
 * 绑定邮箱界面
 *
 * @author NorthStar
 * @date 2018/8/20 16:21
 */
public class BindingEmailActivity extends BaseActivity implements IBindingPhoneActivity {

    private static final String TAG = "BindingPhoneActivity";

    @BindView(R.id.et_email)
    EditText mEmailEt;
    @BindView(R.id.binding_et_verifyCode)
    EditText mVerifyCodeEt;
    @BindView(R.id.binding_btn_getVerifyCode)
    TextView mGetVerifyCodeTv;
    @BindView(R.id.tv_bind_finish)
    TextView mNextBtn;
    @BindView(R.id.title_tv)
    TextView titleTV;

    //倒计时器
    private MyCountDownTimer myCountDownTimer;
    private boolean mIsEmailOk;
    private boolean mIsCaptchaOk;
    BindContactsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_email);
        initData();
        initView();
    }

    private void initData() {
        setButterKnife(this);//绑定注解
        mPresenter = new BindContactsPresenter(this, this);
        myCountDownTimer = new MyCountDownTimer(AppConstants.COUNT_DOWN_MAX_TIME * 1000, 1000);
    }


    private void initView() {
        titleTV.setText(R.string.bind_email);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        setListener();
    }

    private void setListener() {
        setChangeListener(mEmailEt, 0);
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
                        break;
                }
                mNextBtn.setEnabled(mIsCaptchaOk && mIsEmailOk);
            }
        });
    }

    @OnClick({R.id.title_back, R.id.tv_bind_finish, R.id.binding_btn_getVerifyCode})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(BindingEmailActivity.this);
                break;

            case R.id.binding_btn_getVerifyCode://获取验证码
                if (!AppUtils.checkJump()) return;
                myCountDownTimer.start();//开启倒计时
                mVerifyCodeEt.requestFocus();
                UserBean userBean = new UserBean();
                userBean.setLoginWay(1);
                userBean.setEmail(getElement(mEmailEt));
                mPresenter.requestVerifyCode(ActivityConstants.PARAM_VALUE_BIND_EMAIL, userBean);//获取验证码
                break;

            case R.id.tv_bind_finish://绑定完成
                if (!AppUtils.checkJump()) return;
                mPresenter.bindEmail(getElement(mVerifyCodeEt), getElement(mEmailEt));
                break;
        }
    }

    //获取所需参数
    private String getElement(EditText et) {
        return et.getText().toString();
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

    @Override
    public void showWaitingMsg(String msg) {
        runOnUiThread(() -> showWaitingDialog(BindingEmailActivity.this, msg, false));
    }

    @Override
    public void requestVerifyResult(final boolean isOk, final Object msgStr) {
        UIHelper.showToast((String) msgStr);
        BindingEmailActivity.this.hideWaitingDialog();
        if (!isOk) setButtonReset();
    }

    @Override
    public void bindResult(boolean isOk, Object msg) {
        runOnUiThread(() -> {
            hideWaitingDialog();
            UIHelper.showToast((String) msg);
            if (isOk) {
                setBindEmail();
            }
        });
    }

    private void setBindEmail() {
        UserBean user = MyApplication.getCurrLoginUser();
        EventBus.getDefault().post(new UserInfoUpdateEvent(user));
        goBack(BindingEmailActivity.this);
    }

    private void setButtonReset() {
        if (BindingEmailActivity.this.isFinishing()) return;
        mGetVerifyCodeTv.setEnabled(true);
        mGetVerifyCodeTv.setText(getResources().getString(R.string.get_verify_code));
        myCountDownTimer.cancel();
        time = 0;
    }


    @Override
    protected void onStop() {
        super.onStop();
        myCountDownTimer.cancel();
        myCountDownTimer.onFinish();
    }

    public boolean notEmpty(EditText et) {
        return !TextUtils.isEmpty(getElement(et));
    }

    public static void bindingEmailLauncher(Activity activity) {
        Intent i = new Intent(activity, BindingEmailActivity.class);
        activity.startActivity(i);
    }
}