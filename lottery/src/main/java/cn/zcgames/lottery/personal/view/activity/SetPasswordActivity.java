package cn.zcgames.lottery.personal.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.event.LoginEvent;
import cn.zcgames.lottery.personal.presenter.SetPasswordPresenter;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.personal.view.iview.ISetPasswordActivity;
import cn.zcgames.lottery.base.UIHelper;

/**
 * 设置登录密码界面
 * @author NorthStar
 * @date 2018/8/20 17:26
 */
public class SetPasswordActivity extends BaseActivity implements ISetPasswordActivity {

    private static final String TAG = "SetPasswordActivity";

    @BindView(R.id.setPsw_et_password)
    EditText mPswEt;

    @BindView(R.id.setPsw_et_confirmPassword)
    EditText mConfirmPswEt;

    @BindView(R.id.title_back)
    ImageButton backIb;

    @BindView(R.id.setPsw_ll_confirmPassword)
    View confirmPawView;

    private SetPasswordPresenter mPresenter;
    private String mEmail;
    private String countryCode;
    private String mobile;
    private int loginWay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initData();
        initView();
    }

    private void initData() {
        setButterKnife(this);//绑定注解
        loginWay = (int) SharedPreferenceUtil.getPublic(MyApplication.getAppContext(), AppConstants.SPDATA_KEY_LOGIN_WAY, 0);
        mPresenter = new SetPasswordPresenter(this, this);
        if (loginWay == 0) {
            String codeStr = getIntent().getStringExtra(ActivityConstants.PARAM_KEY_COUNTRYCODE);
            countryCode = codeStr.substring(codeStr.indexOf("+") + 1, codeStr.length());
            mobile = getIntent().getStringExtra(ActivityConstants.PARAM_KEY_MOBILE);
        } else {
            mEmail = getIntent().getStringExtra(ActivityConstants.PARAM_KEY_EMAIL);//邮箱
        }
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText(R.string.mine_register);
        UIHelper.showWidget(backIb, true);
    }

    @OnClick({R.id.register_tv_done, R.id.title_back})
    public void onClick(View view) {
        if (!AppUtils.checkJump()) return;
        switch (view.getId()) {
            case R.id.register_tv_done:
                if (!getPassword(mPswEt).equals(getPassword(mConfirmPswEt))) {
                    showMessageDialog(getString(R.string.password_not_match), null);
                    return;
                }
                countryCode = loginWay == 0 ? countryCode : "";
                mobile = loginWay == 0 ? mobile : "";
                mEmail = loginWay == 0 ? "" : mEmail;
                String password = getPassword(mPswEt);
                UserBean user = getUser();
                mPresenter.setPassword(user, password);
                break;
            case R.id.title_back:
                goBack(SetPasswordActivity.this);
                break;
        }
    }

    @NonNull
    private UserBean getUser() {
        UserBean user = new UserBean();
        user.setLoginWay(loginWay);
        if (loginWay == 0) {//0:手机号注册,1:邮箱注册
            user.setMobile(new Mobile(countryCode, mobile));
        } else {
            user.setEmail(mEmail);
        }
        return user;
    }

    public String getPassword(EditText et) {
        return et.getText().toString();
    }

    @Override
    public void setPasswordSuccess(int type, final String msg) {
        runOnUiThread(() -> {
            hideWaitingDialog();
            ToastUtil.getInstances().showShort(msg);
            //发送登录成功的事件，销毁不需要的activity
            EventBus.getDefault().post(new LoginEvent(new UserBean()));
            UIHelper.gotoActivity(SetPasswordActivity.this, MainActivity.class, true);
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
        showWaitingDialog(SetPasswordActivity.this, msg, false);
    }

    @Override
    public void requestVerifyResult(boolean isOk, Object msg) {}
}
