package cn.zcgames.lottery.personal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.util.HostSettings;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.country.view.CountryActivity;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.event.LoginEvent;
import cn.zcgames.lottery.personal.presenter.LoginPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.view.iview.ILoginActivity;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 用户登录界面
 *
 * @author NorthStar
 * @date 2018/8/20 15:56
 */
public class LoginActivity extends BaseActivity implements ILoginActivity {

    @BindView(R.id.title_back)
    ImageButton backIb;

    @BindView(R.id.tv_login_phone)
    TextView phoneTV;
    @BindView(R.id.tv_login_email)
    TextView emailTV;

    @BindView(R.id.et_phone)
    EditText mPhoneEt;

    @BindView(R.id.et_email)
    EditText mEmailEt;

    @BindView(R.id.login_tv_login)
    TextView mNextBtn;

    @BindView(R.id.login_et_password)
    EditText mPswEt;

    @BindView(R.id.ll_login_phone)
    LinearLayout phoneLL;

    @BindView(R.id.ll_login_email)
    LinearLayout emailLL;

    @BindView(R.id.tv_zone_code)
    TextView countryCode;

    private LoginPresenter mPresenter;
    private boolean mIsEmailOk, mIsPasswordOk;
    private static final String TAG = "LoginActivity";
    public static final String KEY_IS_SINGLE = "single";
    private int loginType;//登录类型 0:手机号登录,1:邮箱登录
    private String phonePwd = "";
    private String emailPwd = "";
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        initView();
    }

    public static void intoThisActivity(Activity fromActivity, boolean isSingle) {
        LogF.d(TAG, "isSingle==>" + isSingle);
        Intent intent = new Intent(fromActivity, LoginActivity.class);
        intent.putExtra(KEY_IS_SINGLE, isSingle);
        if (isSingle) intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogF.e("tag", "onNewIntent执行了");
    }

    private void initData() {
        setButterKnife(this);
        mPresenter = new LoginPresenter(this, this);
        EventBus.getDefault().register(this);
        loginType = (int) SharedPreferenceUtil.getPublic(MyApplication.getAppContext(),
                AppConstants.SPDATA_KEY_LOGIN_WAY, 0);
        LogF.d(TAG, "初始化loginType==>" + loginType);
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        UIHelper.showWidget(backIb, true);
        titleTv.setText(R.string.btn_login);
        showHost();//展示当前host
        setTabState(loginType);
        isFirst = false;
    }

    private void setListener() {
        boolean isPhone = loginType == 0;
        setChangeListener(isPhone ? mPhoneEt : mEmailEt, 0);
        String pwd = isPhone ? phonePwd : emailPwd;
        mPswEt.setText(pwd);
        setChangeListener(mPswEt, 1);
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
                        mIsPasswordOk = notEmpty(et);
                        if (loginType == 0) {
                            phonePwd = mIsPasswordOk ? getElement(et) : "";
                        } else {
                            emailPwd = mIsPasswordOk ? getElement(et) : "";
                        }
                        break;
                }
                mNextBtn.setEnabled(mIsPasswordOk && mIsEmailOk);
            }
        });
    }

    //获取所需参数
    private String getElement(EditText et) {
        return et.getText().toString();
    }

    @OnClick({R.id.tv_host_status, R.id.title_back, R.id.login_tv_login, R.id.login_tv_register, R.id.login_tv_findpsw
            , R.id.login_rl_weixinLogin, R.id.login_rl_QQLogin, R.id.tv_login_phone, R.id.tv_login_email, R.id.tv_zone_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                ActivityManager.getInstance().popAllActivityExceptOne(MainActivity.class);
                break;

            case R.id.tv_host_status:
                try {
                    setServerIP();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.login_tv_login:
                UserBean user = new UserBean();
                String codeStr = countryCode.getText().toString();
                String code = codeStr.substring(codeStr.indexOf("+") + 1, codeStr.length());
                user.setPassword(getElement(mPswEt));
                if (loginType == 0) {
                    user.setMobile(new Mobile(code, getElement(mPhoneEt)));
                } else {
                    user.setEmail(getElement(mEmailEt));
                }
                user.setLoginWay(loginType);
                mPresenter.login(user);
                break;

            case R.id.tv_login_phone:
                setTabState(0);
                break;

            case R.id.tv_login_email:
                setTabState(1);
                break;

            case R.id.tv_zone_code:
                CountryActivity.launch(LoginActivity.this);
                break;

            case R.id.login_tv_register:
                UIHelper.gotoActivity(LoginActivity.this, VerifyCodeActivity.class, false);
                break;

            case R.id.login_tv_findpsw://忘记密码
                SharedPreferenceUtil.putPublic(MyApplication.getAppContext(), AppConstants.SPDATA_KEY_LOGIN_WAY, loginType);
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                break;

            case R.id.login_rl_weixinLogin:
                mPresenter.loginThirdPlatform(SHARE_MEDIA.WEIXIN, LoginActivity.this);
                break;

            case R.id.login_rl_QQLogin:
                mPresenter.loginThirdPlatform(SHARE_MEDIA.QQ, LoginActivity.this);
                break;
        }
    }

    @Override
    public void loginSuccess(UserBean user, final int type) {
        if (LOGIN_OK_TYPE_THIRDPLAT == type) {
            // skip to band email
            UIHelper.showToast(R.string.tips_bind_email);
            UIHelper.gotoActivity(LoginActivity.this, BindingPhoneActivity.class, true);
        } else {
            SharedPreferenceUtil.putPublic(MyApplication.getAppContext(), AppConstants.SPDATA_KEY_LOGIN_WAY, loginType);
            goBack(LoginActivity.this);
        }
    }

    @Override
    public void loginFail(final String errorMsg) {
        LogF.d("loginError", "loginError==>" + errorMsg);
        ToastUtil.getInstances().showLong(errorMsg);
    }

    @Override
    public void loginLoading(String msg) {
        showWaitingDialog(LoginActivity.this, msg, false);
    }

    @Override
    public void hideDialog() {
        hideWaitingDialog();
    }

    private void setServerIP() {
        switch (HostSettings.getHostType()) {
            case HostSettings.HOST_TYPE_DEV:
                HostSettings.setHostType(HostSettings.HOST_TYPE_TEST);
                break;
            case HostSettings.HOST_TYPE_TEST:
                HostSettings.setHostType(HostSettings.HOST_TYPE_PREPARED);
                break;
            case HostSettings.HOST_TYPE_PREPARED:
                HostSettings.setHostType(HostSettings.HOST_TYPE_PRO);
                break;
            case HostSettings.HOST_TYPE_PRO:
                HostSettings.setHostType(HostSettings.HOST_TYPE_DEV);
                break;
        }
        HttpHelper.refreshHost(HostSettings.getHost());
        showHost();
    }

    protected void showHost() {
        TextView tv_host_status = findViewById(R.id.tv_host_status);
        if (Constant.DEBUG) {
            //动态显示服务器
            switch (HostSettings.getHostType()) {
                case HostSettings.HOST_TYPE_DEV:
                    tv_host_status.setVisibility(View.VISIBLE);
                    tv_host_status.setText("开发");
                    break;
                case HostSettings.HOST_TYPE_TEST:
                    tv_host_status.setVisibility(View.VISIBLE);
                    tv_host_status.setText("测试");
                    break;
                case HostSettings.HOST_TYPE_PREPARED:
                    tv_host_status.setVisibility(View.VISIBLE);
                    tv_host_status.setText("封测");
                    break;
                case HostSettings.HOST_TYPE_PRO:
                    tv_host_status.setVisibility(View.VISIBLE);
                    tv_host_status.setText("正式");
                    break;
            }
        } else {
            tv_host_status.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstants.REQUEST_COUNTRY_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String countryNumber = bundle.getString("countryNumber");
                        countryCode.setText(countryNumber);
                    }
                }
                break;
        }
        //        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    //切换title状态
    private void setTabState(int position) {
        if (isFirst || position != loginType) {
            loginType = position;
            phoneLL.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
            emailLL.setVisibility(position == 1 ? View.VISIBLE : View.INVISIBLE);
            mNextBtn.setEnabled(notEmpty(loginType == 0 ? mPhoneEt : mEmailEt) && notEmpty(mPswEt));
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

    public boolean notEmpty(EditText et) {
        return !TextUtils.isEmpty(getElement(et));
    }
}
