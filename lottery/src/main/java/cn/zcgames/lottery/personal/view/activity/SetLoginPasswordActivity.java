package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.view.iview.ISetLoginPasswordActivity;
import cn.zcgames.lottery.personal.presenter.SetLoginPasswordPresenter;

/**
 * 设置账户密码页
 *
 * @author NorthStar
 * @date 2018/8/20 17:09
 */
public class SetLoginPasswordActivity extends BaseActivity implements View.OnClickListener, ISetLoginPasswordActivity {

    private static final int MSG_SHOW_VIEW_BY_CHECK_RESULT = 0;
    private static final int MSG_SET_LOGIN_PASSWORD_OK = 1;
    private static final int MSG_UPDATE_LOGIN_PASSWORD_OK = 2;
    private static final int MSG_SHOW_FAIL_TIP = 3;

    private TextView mTitleTv;
    private EditText mOldPasswordEt, mPasswordEt, mConfirmPasswordEt;
    private View mOldView, mOkBtnView;

    private SetLoginPasswordPresenter mPresenter;

    private boolean mIsPswOK;

    private boolean mIsOldPswOk, mIsNewPswOk, mIsConfirmPswOk;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_VIEW_BY_CHECK_RESULT:
                    if (msg.obj instanceof Boolean) {
                        mIsPswOK = (boolean) msg.obj;
                        UIHelper.showWidget(mOldView, mIsPswOK);
                        if (mIsPswOK) {
                            mTitleTv.setText(R.string.mine_login_password_update);
                        } else {
                            mTitleTv.setText(R.string.mine_login_password_set);
                            mOldPasswordEt.setHint(R.string.mine_input_old_password);
                        }
                        mPasswordEt.setHint(R.string.password_hint);
                        mConfirmPasswordEt.setHint(R.string.input_new_password);
                    }
                    break;
                case MSG_SET_LOGIN_PASSWORD_OK:
                    if (msg.obj instanceof String) {
                        String errorStr = msg.obj.toString();
                        UIHelper.showToast(errorStr);
                        UserBean user = MyApplication.getCurrLoginUser();
                        user.setPasswordOk(true);
                        MyApplication.updateCurrLoginUser(user);
                        EventBus.getDefault().post(new UserInfoUpdateEvent(user));
                    } else {
                        UIHelper.showToast(R.string.tips_request_fail);
                    }
                    goBack(SetLoginPasswordActivity.this);
                    break;
                case MSG_UPDATE_LOGIN_PASSWORD_OK:
                    if (msg.obj instanceof String) {
                        String errorStr = msg.obj.toString();
                        showMessageDialog(errorStr, null);
                    } else {
                        UIHelper.showToast(R.string.tips_request_fail);
                    }
                    goBack(SetLoginPasswordActivity.this);
                    break;
                case MSG_SHOW_FAIL_TIP:
                    if (msg.obj instanceof String) {
                        String errorStr = msg.obj.toString();
                        showMessageDialog(errorStr, v -> {

                        });
                    } else {
                        UIHelper.showToast(R.string.tips_request_fail);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_password);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new SetLoginPasswordPresenter(this, this);
        mPresenter.checkPassword();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.title_tv);

        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        UIHelper.showWidget(backView, true);
        mOkBtnView = findViewById(R.id.setPsw_tv_ok);
        mOkBtnView.setOnClickListener(this);

        mOldPasswordEt = findViewById(R.id.setPsw_et_oldPassword);
        mOldPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mOldPasswordEt.getText().toString())) {
                    mIsOldPswOk = false;
                } else {
                    mIsOldPswOk = true;
                }
                updateOkBtnStatus();
            }
        });
        mPasswordEt = findViewById(R.id.setPsw_et_password);
        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mPasswordEt.getText().toString())) {
                    mIsNewPswOk = false;
                } else {
                    mIsNewPswOk = true;
                }
                updateOkBtnStatus();
            }
        });
        mConfirmPasswordEt = findViewById(R.id.setPsw_et_confirmPassword);
        mConfirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mConfirmPasswordEt.getText().toString())) {
                    mIsConfirmPswOk = false;
                } else {
                    mIsConfirmPswOk = true;
                }
                updateOkBtnStatus();
            }
        });
        mOldView = findViewById(R.id.setPsw_ll_oldPassword);
    }

    private void updateOkBtnStatus() {
        if (mIsOldPswOk && mIsNewPswOk && mIsConfirmPswOk) {
            mOkBtnView.setEnabled(true);
        } else {
            mOkBtnView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(SetLoginPasswordActivity.this);
                break;
            case R.id.setPsw_tv_ok:
                if (mIsPswOK) {
                    mPresenter.updateLoginPassword(mOldPasswordEt.getText().toString(),
                            mPasswordEt.getText().toString(), mConfirmPasswordEt.getText().toString());
                } else {
                    mPresenter.setLoginPassword(mPasswordEt.getText().toString(), mConfirmPasswordEt.getText().toString());
                }
                break;
        }
    }

    @Override
    public void checkPasswordResult(boolean isOk) {
        Message msg = new Message();
        msg.obj = isOk;
        msg.what = MSG_SHOW_VIEW_BY_CHECK_RESULT;
        mHandler.sendMessage(msg);
    }

    @Override
    public void updateLoginPasswordResult(boolean isOk, Object rStr) {
        Message msg = new Message();
        msg.obj = rStr;
        if (isOk) {
            msg.what = MSG_UPDATE_LOGIN_PASSWORD_OK;
        } else {
            msg.what = MSG_SHOW_FAIL_TIP;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void setLoginPasswordResult(boolean isOk, Object Str) {
        Message msg = new Message();
        msg.obj = Str;
        if (isOk) {
            msg.what = MSG_SET_LOGIN_PASSWORD_OK;
        } else {
            msg.what = MSG_SHOW_FAIL_TIP;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void showWaitingDialog(String msgStr) {

    }

    @Override
    public void hiddenWaitingDialog() {

    }
}
