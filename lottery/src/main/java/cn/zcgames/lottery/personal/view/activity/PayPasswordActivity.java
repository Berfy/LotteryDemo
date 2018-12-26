package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.Mobile;
import cn.zcgames.lottery.personal.presenter.SetPasswordPresenter;
import cn.zcgames.lottery.personal.view.adapter.KeyAdapter;
import cn.zcgames.lottery.personal.view.iview.ISetPasswordActivity;

/**
 * 支付密码界面
 *
 * @author NorthStar
 * @date 2018/11/7 16:06
 */
public class PayPasswordActivity extends BaseActivity implements ISetPasswordActivity {
    @BindView(R.id.gv_keyboard)
    GridView gvKeyboard; // 密码键盘
    @BindView(R.id.et_password_InputView)
    PasswordEditText etPwd; // 密码输入框
    @BindView(R.id.tv_pay_pwd_hint)
    TextView hintTv;
    public static final String TAG = "PayPasswordActivity";
    private SetPasswordPresenter presenter;
    private int actionType = 0;//设置支付密码也的启动类型:0,设置密码,1:修改密码,2:使用密码;
    private ArrayList<Map<String, String>> numList; // 数字按键序列
    private String password; // 正在输入的密码
    private String paypwd;//支付密码
    private String surePwd;//最终的支付密码
    private boolean isFristInput = true;//是否是第一次输入
    private String countryCode;
    private String numbers;
    private String captcha;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case AppConstants.KEYBOARD_INPUT:
                    int position = (int) msg.obj;
                    if (position < 11 && position != 9) {
                        // 点击0-9按键
                        String num = numList.get(position).get("num");
                        password = etPwd.getText().append(num).toString();
                        etPwd.setText(password);
                    } else {
                        // 点击退格键
                        if (position == 11) {
                            if (!TextUtils.isEmpty(password)) {
                                password = etPwd.getText().delete(password.length() - 1, password.length()).toString();
                                etPwd.setText(password);
                            }
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 用于修改密码回调
     *
     * @param context     前一个activity
     * @param captcha     设置和修改支付密码时要传入验证手机号验证码
     * @param RequestCode 请求码 0:设置密码,1:修改密码,2:输入密码并验证;
     */
    public static void intoThisActivity(Activity context, String countryCode, String numbers,
                                        String captcha, int RequestCode) {
        Intent intent = new Intent(context, PayPasswordActivity.class);
        intent.putExtra(ActivityConstants.PARAM_KEY_COUNTRYCODE, countryCode);
        intent.putExtra(ActivityConstants.PARAM_KEY_MOBILE, numbers);
        intent.putExtra(ActivityConstants.PARAM_KEY_CAPTCHA, captcha);
        intent.putExtra(ActivityConstants.PARAM_ACTION_TYPE, RequestCode);
        context.startActivityForResult(intent, RequestCode);
    }

    //用于设置及验证密码回调
    public static void intoThisActivity(Activity context, int RequestCode) {
        Intent intent = new Intent(context, PayPasswordActivity.class);
        intent.putExtra(ActivityConstants.PARAM_ACTION_TYPE, RequestCode);
        context.startActivityForResult(intent, RequestCode);
    }

    public static void intoThisActivity(Fragment fromFragment, int RequestCode) {
        Intent intent = new Intent(fromFragment.getActivity(), PayPasswordActivity.class);
        intent.putExtra(ActivityConstants.PARAM_ACTION_TYPE, RequestCode);
        fromFragment.startActivityForResult(intent, RequestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        actionType = getIntent().getIntExtra(ActivityConstants.PARAM_ACTION_TYPE, 0);
        switch (actionType) {
            case 0://初次设置
                captcha = getIntent().getStringExtra(ActivityConstants.PARAM_KEY_CAPTCHA);
                break;

            case 1://重置
                countryCode = getIntent().getStringExtra(ActivityConstants.PARAM_KEY_COUNTRYCODE);
                numbers = getIntent().getStringExtra(ActivityConstants.PARAM_KEY_MOBILE);
                captcha = getIntent().getStringExtra(ActivityConstants.PARAM_KEY_CAPTCHA);
                break;

            default:
                break;
        }
    }

    private void initView() {
        setContentView(R.layout.activity_pay_password);
        setButterKnife(this);
        presenter = new SetPasswordPresenter(this, this);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText(R.string.mine_pay_pwd);
        String hint = "";
        switch (actionType) {
            case 0:
                hint = "设置支付密码，支付密码将保护您的账户安全";
                break;
            case 1:
                hint = "请输入新密码";
                break;
            case 2:
                hint = "请输入支付密码";
                break;
        }
        hintTv.setText(hint);
        setListener();
    }

    private void setListener() {
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPwd.getText().length() == 4) {
                    if (isFristInput) {
                        paypwd = etPwd.getText().toString();
                    } else {
                        surePwd = etPwd.getText().toString();
                    }
                    LogF.d(TAG, "paypwd==>" + paypwd + ",surePwd==>" + surePwd);
                }
            }
        });
        initKeyboard();
    }

    @OnClick({R.id.title_back, R.id.setPsw_tv_ok})
    public void onClick(View view) {
        if (!AppUtils.checkJump()) return;
        switch (view.getId()) {
            case R.id.title_back:
                goBack(PayPasswordActivity.this);
                break;
            case R.id.setPsw_tv_ok:
                if (!TextUtils.isEmpty(paypwd)) {
                    if (actionType == 2) {//使用
                        Intent data = new Intent();
                        data.putExtra(ActivityConstants.KEY_PAYPASSWORD, paypwd);//支付密码
                        setResult(RESULT_OK, data);
                        goBack(PayPasswordActivity.this);
                    } else {
                        if (isFristInput) {
                            etPwd.getText().clear();
                            isFristInput = false;
                            hintTv.setText("确定支付密码");
                        } else {
                            if (!paypwd.equals(surePwd)) {
                                ToastUtil.getInstances().showShort("密码两次输入不一致,请重新输入");
                                paypwd = "";
                                surePwd = "";
                                etPwd.getText().clear();
                                isFristInput = true;
                                hintTv.setText("请输入支付密码");
                                return;
                            }
                            switch (actionType) {
                                case 0://设置
                                    /* presenter.setPayPassword(captcha, surePwd);*/
                                    VerifyCodeActivity.intoThisActivity(mContext,
                                            ActivityConstants.PARAM_VALUE_SET_PAY_PASSWORD, ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD);
                                    break;
                                case 1://修改
                                    presenter.updatePayPassword(new Mobile(countryCode, numbers), captcha, surePwd);
                                    break;
                            }
                        }
                    }
                } else {
                    ToastUtil.getInstances().showShort("请输入支付密码");
                }
                break;
        }
    }


    @Override
    public void setPasswordSuccess(int type, final String msg) {
        runOnUiThread(() -> {
            hideWaitingDialog();
            ToastUtil.getInstances().showShort(msg);
            // 根据type来确定下一步的去向
            switch (type) {
                case ActivityConstants.PARAM_VALUE_SET_PAY_PASSWORD://设置
                case ActivityConstants.PARAM_VALUE_UPDATE_PAY_PASSWORD://修改
                case ActivityConstants.PARAM_VALUE_VERIFIY_PAY_PASSWORD://验证
                    setOnActivityResult();
                    break;

            }
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
        showWaitingDialog(PayPasswordActivity.this, msg, false);
    }

    public void setOnActivityResult() {
        setResult(RESULT_OK, new Intent());
        goBack(PayPasswordActivity.this);
    }

    @Override
    public void requestVerifyResult(boolean isOk, Object msg) {
    }

    /**
     * 初始化密码键盘
     */
    private void initKeyboard() {
        final int number = 10;
        int[] keys = new int[number];
        for (int i = 0; i < 9; i++) {
            keys[i] = i + 1;
        }
        keys[9] = 0;
        /*getRandom(number, keys);*/
        numList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 9) {
                map.put("num", String.valueOf(keys[i]));
            } else if (i == 9) {
                map.put("num", "");
            } else if (i == 10) {
                map.put("num", String.valueOf(keys[9]));
            } else {
                map.put("num", "");
            }
            numList.add(map);
        }
        KeyAdapter keyAdapter = new KeyAdapter(mContext, numList, handler);
        gvKeyboard.setAdapter(keyAdapter);
    }

    //随机生成键盘数字
    private void getRandom(int number, int[] keys) {
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int p = random.nextInt(number);
            int tmp = keys[i];
            keys[i] = keys[p];
            keys[p] = tmp;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD://设置
                    String captcha = data.getStringExtra(HttpHelper.PARAMS_CAPTCHA);
                    if (!TextUtils.isEmpty(captcha)) {
                        presenter.setPayPassword(captcha, surePwd);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
