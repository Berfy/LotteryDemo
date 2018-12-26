package cn.zcgames.lottery.home.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.bean.response.ResponseBaseBean;
import cn.zcgames.lottery.bean.response.ResultBuyLotteryBeanNew;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.bean.response.ResultBuyLotteryBean;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.event.UserInfoUpdateEvent;
import cn.zcgames.lottery.personal.presenter.MinePresenter;
import cn.zcgames.lottery.personal.view.activity.LotteryOrderDetailActivity;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.activity.PayPasswordActivity;
import cn.zcgames.lottery.personal.view.activity.PhaseDetailActivity;
import cn.zcgames.lottery.personal.view.activity.RechargeActivity;
import cn.zcgames.lottery.home.presenter.PayPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.iview.IPayActivity;
import cn.zcgames.lottery.personal.view.activity.VerifyCodeActivity;
import cn.zcgames.lottery.personal.view.fragment.MineFragment;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_TYPE;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_ORDER_STR;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_TOTAL_MONEY;

/**
 * 余额支付
 * Berfy修改2018.8.27
 */
public class PayActivity extends BaseActivity implements View.OnClickListener, IPayActivity<ResponseBaseBean> {

    private static final String TAG = "PayActivity";

    private static final int MSG_REQUEST_REMAIN_OK = 0;
    private static final int MSG_REQUEST_REMAIN_FAIL = 1;
    private static final int MSG_GO_TO_PAY = 2;
    private static final int MSG_GO_TO_RECHARGE = 3;
    private static final int MSG_BUY_OK = 4;
    private static final int MSG_BUY_FAIL = 5;

    private View mOkBtn;

    private String mOrderMoney;//订单总金额
    private double mRemainMoney = 0;//账户余额
    private boolean mIsGetRemain;//是否获取到了余额
    private String mOrderString;
    private String mLotteryType;//彩票类型
    private boolean mNeedRecharge;//是否需要充值

    @BindView(R.id.order_tv_money)
    public TextView mMoneyTv;
    @BindView(R.id.order_tv_remain)
    public TextView mRemainTv;

    private PayPresenter mPresenter;
    private MinePresenter mMinePresenter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_BUY_OK:
                    if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                            || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                        ResultBuyLotteryBeanNew.BuyLotteryDataBean bean = (ResultBuyLotteryBeanNew.BuyLotteryDataBean) msg.obj;
                        if (null == bean) {
                            ToastUtil.getInstances().showShort(R.string.pay_order_failed);
                        } else {
                            if (bean.getChase() > 1) {
                                PhaseDetailActivity.intoThisActivity(PayActivity.this, bean.getOrder_id(), mLotteryType);
                            } else {
                                LotteryOrderDetailActivity.inToThisActivity(PayActivity.this, bean.getOrder_id(), mLotteryType);
                            }
                            LotteryOrderDbUtils.deleteAllLotteryOrder();
                            PayActivity.this.finish();
                            EventBus.getDefault().post(new LotteryBuyOkEvent(true));
                            ToastUtil.getInstances().showShort(R.string.pay_order_suc);
                        }
                    } else {
                        ResultBuyLotteryBean.BuyLotteryDataBean bean = (ResultBuyLotteryBean.BuyLotteryDataBean) msg.obj;
                        if (null != bean) {
                            UserBean user = MyApplication.getCurrLoginUser();
                            if (mNeedRecharge) {
                                user.setMoney("0");
                            } else {
                                double money = Long.valueOf(user.getMoney()) - Double.valueOf(mOrderMoney);
                                user.setMoney(String.valueOf(money));
                            }
                            MyApplication.updateCurrLoginUser(user);
                            EventBus.getDefault().post(new UserInfoUpdateEvent(user));

                            if (!TextUtils.isEmpty(bean.getType()) && bean.getType().equals("chase")) {
                                PhaseDetailActivity.intoThisActivity(PayActivity.this, bean.getId(), bean.getUrl());
                            } else {
                                LotteryOrderDetailActivity.inToThisActivity(PayActivity.this, bean.getId(), mLotteryType);
                            }
                            LotteryOrderDbUtils.deleteAllLotteryOrder();
                            PayActivity.this.finish();
                            // SEND BUY OK EVENT
                            EventBus.getDefault().post(new LotteryBuyOkEvent(true));
                            ToastUtil.getInstances().showShort(R.string.pay_order_suc);
                        } else {
                            ToastUtil.getInstances().showShort(R.string.pay_order_failed);
                        }
                    }
                    break;
                case MSG_BUY_FAIL:
                    String buyFailTip = (String) msg.obj;
                    UIHelper.showToast(buyFailTip);
                    mOkBtn.setEnabled(true);
                    break;
                case MSG_GO_TO_RECHARGE:
                    showRemainToRecharge();
                    break;
                case MSG_GO_TO_PAY:
                    UserBean userBean = MyApplication.getCurrLoginUser();
                    if (!userBean.isPayPasswordOk()) {
                        PayPasswordActivity.intoThisActivity(mContext, ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD);
                        return;
                    }
                    PayPasswordActivity.intoThisActivity(PayActivity.this, ActivityConstants.REQUEST_CODE_VERIFIY_PAYPASSWORD);
                    break;
                case MSG_REQUEST_REMAIN_OK:
                    if (msg.obj instanceof ResponseBaseBean) {
                        ResponseBaseBean responseBaseBean = (ResponseBaseBean) msg.obj;
                        if (!TextUtils.isEmpty(responseBaseBean.getRemain())) {
                            mIsGetRemain = true;
                            try {
                                mRemainMoney = Double.valueOf(responseBaseBean.getRemain()) / 100;
                                mRemainTv.setText(StringUtils.getCash(responseBaseBean.getRemain(), AppConstants.DIGITS));
                                if (mRemainMoney >= Double.valueOf(mOrderMoney)) {
                                    mNeedRecharge = false;
                                } else {
                                    mNeedRecharge = true;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                getRemainFailed();
                            }
                        } else {
                            getRemainFailed();
                        }
                    } else {
                        getRemainFailed();
                    }
                    break;
                case MSG_REQUEST_REMAIN_FAIL:
                    mIsGetRemain = false;
                    if (msg.obj instanceof String) {
                        String errorStr = (String) msg.obj;
                        showToast(errorStr);
                    }
                    break;
            }
        }
    };

    private void showRemainToRecharge() {
        AlertDialog dialog = new AlertDialog(this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("您的余额不足，是否去充值？")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setPositiveButton("去充值", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转充值页面
                        UIHelper.gotoActivity(PayActivity.this, RechargeActivity.class, false);
                    }
                });
        dialog.show();
    }

    private void getRemainFailed() {
        Message msg1 = new Message();
        msg1.what = MSG_REQUEST_REMAIN_FAIL;
        msg1.obj = "获取余额失败";
        mHandler.sendMessage(msg1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initIntentData();
        initPresenter();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMinePresenter.requestMineData();
    }

    private void initIntentData() {
        Intent i = getIntent();
        mOrderMoney = i.getStringExtra(PARAM_TOTAL_MONEY);
        mLotteryType = i.getStringExtra(PARAM_LOTTERY_TYPE);
        mOrderString = i.getStringExtra(PARAM_ORDER_STR);
    }

    private void initPresenter() {
        mPresenter = new PayPresenter(this, this);
        mMinePresenter = new MinePresenter(this, this);
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText(R.string.mine_pay);

        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        UIHelper.showWidget(backView, true);

        mMoneyTv.setText(mOrderMoney + getString(R.string.lottery_yuan));

        mOkBtn = findViewById(R.id.pay_rb_ok);
        mOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(PayActivity.this);
                break;
            case R.id.pay_rb_ok:
                if (!mIsGetRemain) {
                    ToastUtil.getInstances().showShort(R.string.lottery_buy_no_remain_tip);
                    mMinePresenter.requestMineData();
                    return;
                }
                if (mNeedRecharge) {
                    mHandler.sendEmptyMessage(MSG_GO_TO_RECHARGE);
                } else {
                    mHandler.sendEmptyMessage(MSG_GO_TO_PAY);
                }
                break;
        }
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        Message msg = new Message();
        if (isOk) {
            try {
                UserBean userBean = (UserBean) object;
                if (null != userBean) {
                    ResponseBaseBean responseBaseBean = new ResponseBaseBean();
                    responseBaseBean.setRemain(userBean.getMoney());
                    msg.what = MSG_REQUEST_REMAIN_OK;
                    msg.obj = responseBaseBean;
                } else {
                    msg.what = MSG_REQUEST_REMAIN_FAIL;
                    msg.obj = msg;
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg.what = MSG_REQUEST_REMAIN_FAIL;
                msg.obj = msg;
            }
        } else {
            msg.what = MSG_REQUEST_REMAIN_FAIL;
            msg.obj = msg;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void requestResultOk(ResponseBaseBean object) {
        Message msg = new Message();
        msg.what = MSG_REQUEST_REMAIN_OK;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

    @Override
    public void requestResultFailed(boolean isNeedLogin, String msg) {
        Message message = new Message();
        if (isNeedLogin) {//需要登录
            UIHelper.gotoActivity(this, LoginActivity.class, false);
            return;
        }
        message.what = MSG_REQUEST_REMAIN_FAIL;
        message.obj = msg;
        mHandler.sendMessage(message);
    }

    @Override
    public void showTipDialog(String msgStr) {
        showWaitingDialog(PayActivity.this, msgStr, false);
    }

    @Override
    public void hideTipDialog() {
        hideWaitingDialog();
    }

    @Override
    public void onBuyResult(boolean isOk, Object obj) {
        Message msg = new Message();
        if (isOk) {
            msg.what = MSG_BUY_OK;
        } else {
            msg.what = MSG_BUY_FAIL;
        }
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    /**
     * 进入此activity的入口
     *
     * @param fromActivity
     * @param mTotalMoney
     * @param mLotteryType
     * @param orderStr
     */
    public static void intoThisActivity(Activity fromActivity, String mTotalMoney, String mLotteryType, String orderStr) {
        Intent i = new Intent(fromActivity, PayActivity.class);
        i.putExtra(PARAM_TOTAL_MONEY, mTotalMoney);
        i.putExtra(PARAM_LOTTERY_TYPE, mLotteryType);
        i.putExtra(PARAM_ORDER_STR, orderStr);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ActivityConstants.REQUEST_CODE_SET_PAYPASSWORD://设置
                    LogF.d(TAG, "设置支付密码成功");
                    //设置完成跳转输入支付密码
                    ToastUtil.getInstances().showShort("为了确保资金安全，请输入安全密码完成支付");
                    PayPasswordActivity.intoThisActivity(mContext, ActivityConstants.REQUEST_CODE_VERIFIY_PAYPASSWORD);
                    break;
                case ActivityConstants.REQUEST_CODE_VERIFIY_PAYPASSWORD://输入完成
                    LogF.d(TAG, "输入支付密码成功");
                    mOkBtn.setEnabled(false);
                    if (null != data && data.hasExtra(ActivityConstants.KEY_PAYPASSWORD)) {
                        mPresenter.buyLottery(mLotteryType, mOrderString, mOrderMoney, data.getStringExtra(ActivityConstants.KEY_PAYPASSWORD));
                    } else {
                        ToastUtil.getInstances().showShort("验证失败");
                    }
                    break;
            }
        }
    }
}
