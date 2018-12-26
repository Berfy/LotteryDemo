package cn.zcgames.lottery.personal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.berfy.sdk.mvpbase.view.dialog.CommonDialog;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.WithdrawPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.view.iview.IWithdrawView;
import cn.zcgames.lottery.app.MainActivity;

import static cn.zcgames.lottery.personal.view.activity.WithDrawWayActivity.KEY_WITHDRAW_WAY;

/**
 * 提现界面
 *
 * @author NorthStar
 * @date 2018/8/20 16:48
 */
public class WithDrawActivity extends BaseActivity implements IWithdrawView {

    @BindView(R.id.withdraw_way_tv)
    TextView functionTv;//提现方式

    @BindView(R.id.tv_withdrawal_cash)
    TextView cashTV;//可提取的金额

    @BindView(R.id.warning_tv)
    TextView warnTV;//警告

    @BindView(R.id.tv_withdraw_tip)
    TextView tipTV;//提现手续费

    @BindView(R.id.withdrawal_cash_et)
    EditText cashEt;//要提取的金额

    @BindView(R.id.title_right_tv)
    TextView rightTitle;

    private UserBean user;
    private WithdrawPresenter mPresenter;
    private int withdrawWay = -1;//100.支付宝 200.微信
    private String canWithdraw = "0";//可提现余额
    private static final int CHANGE_QR_CODE_SELECT = 222;
    private CommonDialog remindDialog;//提现成功提示
    private static final String TAG = "WithDrawActivity";
    private boolean hasWithdraw;
    private Long withdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initData();
        initView();
    }

    private void initData() {
        mPresenter = new WithdrawPresenter(this, this);
        user = MyApplication.getCurrLoginUser();
    }

    private void initView() {
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_money_cash);
        rightTitle.setText(R.string.withdraw_record);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        UIHelper.showWidget(rightTitle, true);
        remindDialog = new CommonDialog(this);
        String income = user.getIncome();
        if (!TextUtils.isEmpty(income)) {
            canWithdraw = StringUtils.getCash(income, AppConstants.DIGITS);
            cashTV.setText(canWithdraw);
        }
        mPresenter.requestUserInfo();
        setChangeListener();
    }


    @OnClick({R.id.title_right_tv, R.id.title_back, R.id.tv_commit, R.id.all_withdrawal_tv, R.id.withdraw_way_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_tv:
                WithdrawRecordActivity.launcher(WithDrawActivity.this);
                break;

            case R.id.title_back:
                goBack(WithDrawActivity.this);
                break;

            case R.id.all_withdrawal_tv:
                cashEt.setText(cashTV.getText().toString());
                cashEt.setSelection(cashEt.getText().toString().length());
                break;

            case R.id.withdraw_way_ll:
                if (!AppUtils.checkJump()) return;
                WithDrawWayActivity.launcher(this, withdrawWay, CHANGE_QR_CODE_SELECT);
                break;

            case R.id.tv_commit:
                goWithdraw();
                break;
        }
    }

    //提现设置
    private void goWithdraw() {
        if (!AppUtils.checkJump()) return;
        if (withdrawWay == -1) {
            ToastUtil.getInstances().showShort("请在提现方式中添加提现二维码");
        } else if (!hasWithdraw) {
            ToastUtil.getInstances().showShort("请输入正确金额");
        } else {
            PayPasswordActivity.intoThisActivity(this, ActivityConstants.REQUEST_CODE_VERIFIY_PAYPASSWORD);
        }
    }

    private void setChangeListener() {
        cashEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setEnable(cashEt.getText().toString());
            }
        });
    }

    private void setEnable(String cash) {
        if (TextUtils.isEmpty(cash)) {
            warnTV.setVisibility(View.INVISIBLE);
        } else {
            Double canWithdraws = Double.valueOf(canWithdraw);
            warnTV.setVisibility(View.INVISIBLE);
            if (!StringUtils.isNumber(cash)) {
                setWarning("*输入金额无法识别,请重新输入");
            } else if (canWithdraws == 0.0) {
                setWarning("*您的可提现金额为0");
            } else if (Double.valueOf(cash) > canWithdraws) {
                setWarning("*金额已超过可提现余额,请重新输入");
            } else {
                String tempWithdraw = StringUtils.getNumberNoZero(Double.valueOf(cash) * 100);
                LogF.d(TAG, "tempWithdraw==>" + tempWithdraw);
                withdraw = Long.valueOf(tempWithdraw);
                hasWithdraw = true;
            }
        }
        //        commitBtn.setEnabled(hasWithdraw && withdrawWay != -1);
    }

    private void setWarning(String warning) {
        hasWithdraw = false;
        warnTV.setVisibility(View.VISIBLE);
        warnTV.setText(warning);
    }

    @Override
    public void requestResult(boolean isOk, Object data) {
        if (isOk) {
            remindDialog.showTipDialog("温馨提示", "提交成功，我们会尽快处理您的提现申请。",
                    "确定", (Dialog, which) -> {
                        remindDialog.dismiss();
                        ActivityManager.getInstance().popAllActivityExceptOne(MainActivity.class);
                    });
        } else {
            if (data != null && data instanceof String) {
                ToastUtil.getInstances().showShort(data.toString());
            }
        }
    }

    @Override
    public void getWithdrawWay(boolean isOk, Object object) {
        if (isOk) {
            UserBean tempUser = (UserBean) object;
            if (tempUser != null) {
                String tip = tempUser.getTip();
                if (!TextUtils.isEmpty(tip)) {
                    tipTV.setText(String.format(Locale.CHINA, "提现会收取%s手续费", tip));
                }
                String ali_qr = tempUser.getAli_qr();
                String wx_qr = tempUser.getWx_qr();
                if (!TextUtils.isEmpty(ali_qr)) {
                    withdrawWay = 100;
                    functionTv.setText("支付宝收款码");
                } else if (!TextUtils.isEmpty(wx_qr)) {
                    withdrawWay = 200;
                    functionTv.setText("微信收款码");
                } else {
                    withdrawWay = -1;
                    functionTv.setText("请选择您的收款码");
                }
                LogF.d(TAG, "qrWx==>" + wx_qr + " ,qrAli==>" + ali_qr + " ,withdrawWay==>" + withdrawWay);
            }

        } else {
            functionTv.setText("请选择您的收款码");
            LogF.d(TAG, "拉取失败");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case CHANGE_QR_CODE_SELECT:
                if (resultCode == RESULT_OK) {
                    int way = data.getIntExtra(KEY_WITHDRAW_WAY, -1);
                    functionTv.setText(way == 0 ? "微信收款码" : "支付宝收款码");
                    withdrawWay = way == 0 ? 200 : 100;
                    LogF.d(TAG, "withdrawWay==>" + withdrawWay);
                    //                    commitBtn.setEnabled(hasWithdraw);
                }
                break;
            case ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD://设置
                LogF.d(TAG, "设置支付密码成功");
                //设置完成跳转输入支付密码
                ToastUtil.getInstances().showShort("为了确保资金安全，请输入安全密码完成支付");
                PayPasswordActivity.intoThisActivity(this, ActivityConstants.REQUEST_CODE_VERIFIY_PAYPASSWORD);
                break;
            case ActivityConstants.REQUEST_CODE_VERIFIY_PAYPASSWORD://输入完成
                //输入完毕调用接口
                String payPassword = data.getStringExtra(ActivityConstants.KEY_PAYPASSWORD);
                LogF.d(TAG, "输入支付密码成功支付密码是==>"+payPassword);
                if (!TextUtils.isEmpty(payPassword)) {
                    mPresenter.withdraw(withdraw, withdrawWay, payPassword);
                }
                break;
        }
    }

    @Override
    public void showTipDialog(String msg) {
        showWaitingDialog(WithDrawActivity.this, msg, false);
    }

    @Override
    public void hideTipDialog() {
        hideWaitingDialog();
    }

}
