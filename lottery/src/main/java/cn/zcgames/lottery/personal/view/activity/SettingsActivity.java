package cn.zcgames.lottery.personal.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;

import static cn.zcgames.lottery.app.ActivityConstants.*;

import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.MinePresenter;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 我的-设置
 *
 * @author NorthStar
 * @date 2018/8/20 16:36
 */
public class SettingsActivity extends BaseActivity implements IBaseView {
    public static final String TAG = "SettingsActivity";
    private MinePresenter mPresenter;
    private int actionType = -1;//设置支付密码也的启动类型:0,设置密码,1:修改密码,2:输入密码;

    //设置支付密码展示状态
    @BindView(R.id.tv_pay_pwd)
    TextView setInfoTV;
    @BindView(R.id.iv_pay_pwd)
    ImageView setInfoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_settings);
        setButterKnife(this);
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText(R.string.mine_settings);
        UIHelper.showWidget(findViewById(R.id.title_back), true);

    }

    private void initData() {
        mPresenter = new MinePresenter(this, this);
        boolean isSetPwdOk = MyApplication.getCurrLoginUser().isPayPasswordOk();
        setPaypassword(isSetPwdOk);
        UIHelper.showWidget(setInfoIV, !isSetPwdOk);
        actionType = isSetPwdOk ? 1 : 0;

    }

    private void setPaypassword(boolean isSetPwdOk) {
        setInfoTV.setText(isSetPwdOk ? "已设置" : "未设置");
        setInfoTV.setTextColor(StaticResourceUtils.getColorResourceById(isSetPwdOk ? R.color.color_333333 : R.color.color_app_main));
    }

    @OnClick({R.id.title_back, R.id.settings_tv_logout, R.id.setting_tv_noticeCenter, R.id.settings_tv_aboutUs,
            R.id.settings_tv_feedback, R.id.setting_rl_pay_pwd})
    public void onClick(View view) {
        if (!AppUtils.checkJump()) return;
        switch (view.getId()) {
            case R.id.title_back:
                goBack(SettingsActivity.this);
                break;

            case R.id.settings_tv_logout://退出登录
                mPresenter.setLogout();
                break;

            case R.id.setting_tv_noticeCenter://消息设置中心界面
                UIHelper.gotoActivity(SettingsActivity.this, MessageSettingActivity.class, false);
                break;

            case R.id.setting_rl_pay_pwd://设置支付密码界面
                if (actionType == 0) {//设置支付密码
                    //判断绑定手机号
                    UserBean userBean = MyApplication.getCurrLoginUser();
                    if (!userBean.isPhoneOk()) {
                        BindingPhoneActivity.bindingPhoneLauncher(this, AppConstants.REQUEST_BIND_PHONE);
                    } else {//设置支付密码
                        PayPasswordActivity.intoThisActivity(mContext, REQUEST_CODE_SET_PAYPASSWORD);
                    }
                } else if (actionType == 1) {//修改支付密码
                    VerifyCodeActivity.intoThisActivity(mContext,
                            PARAM_VALUE_UPDATE_PAY_PASSWORD, REQUEST_CODE_RESET_PAYPASSWORD);
                }
                break;
            case R.id.settings_tv_aboutUs://关于我们
                UIHelper.gotoActivity(SettingsActivity.this, AboutUsActivity.class, false);
                break;

            case R.id.settings_tv_feedback://反馈意见
                UIHelper.gotoActivity(SettingsActivity.this, FeedbackActivity.class, false);
                break;
        }
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
    }

    @Override
    public void showTipDialog(String msgStr) {
    }

    @Override
    public void hideTipDialog() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.REQUEST_BIND_PHONE:
                    PayPasswordActivity.intoThisActivity(mContext, REQUEST_CODE_SET_PAYPASSWORD);
                    break;
                case ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD://设置
                    ToastUtil.getInstances().showShort("支付密码设置成功");
                    setPaypassword(true);
                    UserBean user = MyApplication.getCurrLoginUser();
                    user.setIsPayPwd("2");
                    MyApplication.updateCurrLoginUser(user);

                    break;

                case ActivityConstants.REQUEST_CODE_RESET_PAYPASSWORD://修改
                    ToastUtil.getInstances().showShort("支付密码修改成功");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
