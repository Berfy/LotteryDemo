package cn.zcgames.lottery.home.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.bean.response.ResultSequenceBeanNew;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.view.activity.arrange3.Arrange3Activity;
import cn.zcgames.lottery.home.view.activity.threeD.ThreeDActivity;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.activity.BindingPhoneActivity;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.presenter.LotteryOrderPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.callback.ConfirmDialogCallback;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.activity.AlwaysColorActivity;
import cn.zcgames.lottery.home.view.activity.arrange5.Arrange5Activity;
import cn.zcgames.lottery.home.view.activity.doublecolor.DoubleColorActivity;
import cn.zcgames.lottery.home.lottery.elevenfive.view.activity.Eleven5Activity;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;
import cn.zcgames.lottery.home.view.activity.sevenStar.SevenStarActivity;
import cn.zcgames.lottery.home.view.activity.sevenhappy.SevenHappyActivity;
import cn.zcgames.lottery.home.view.adapter.LotteryOrderAdapter;
import cn.zcgames.lottery.home.view.adapter.leftSwipeRv.DoubleColorOrderLeftSwipeRv;
import cn.zcgames.lottery.home.view.adapter.leftSwipeRv.OnItemActionListener;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.home.listener.DoubleColorOrderChangedListener;
import cn.zcgames.lottery.personal.view.activity.PayPasswordActivity;
import cn.zcgames.lottery.personal.view.activity.VerifyCodeActivity;
import cn.zcgames.lottery.utils.DateUtils;

import static cn.zcgames.lottery.app.AppConstants.*;

import static cn.zcgames.lottery.app.ActivityConstants.*;

/**
 * 投注单
 * Berfy修改
 * 2018.8.23
 */
public class LotteryOrderActivity extends BaseActivity implements View.OnClickListener,
        ILotteryOrderActivity,
        DoubleColorOrderChangedListener,
        OnItemActionListener {

    private static final String TAG = "LotteryOrderActivity";

    private static final int MSG_BUY_OK = 0;
    private static final int MSG_BUY_FAIL = 1;
    private static final int MSG_START_COUNT_DOWN = 2;

    List<LotteryOrder> mOrders = new ArrayList<>();
    LotteryOrderAdapter mAdapter;
    DoubleColorOrderLeftSwipeRv mOrderListRv;
    @BindView(R.id.order_tv_sequence)
    TextView mTvSequence;
    @BindView(R.id.order_tv_countDown)
    TextView mTvCountDown;
    @BindView(R.id.order_ll_sequence)
    RelativeLayout mLlSequence;

    private TextView mTotalMoneyTv, mTotalCountTv;
    private EditText mMultipleEt, mChaseEt;

    private LotteryOrderPresenter mPresenter;
    private LotteryOrder mDeleteLotteryOrder;
    private double mMoney = 0;
    private String mTotalMoney;
    private long mTotalCount = 0;
    private int mMultiple = 1, mChase = 1;
    private int mMaxChase = 500;//最大追期数
    private int mMaxMultiple = 99999;//最大倍数
    private String mLotteryType;//当前彩票种类
    private int mPlayMode;//当前玩法
    private ResultSequenceBeanNew.SequenceBean mSequenceBean;//当前期号
    private long mCountDownNumber;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mHandler == null) {
                return;
            }
            switch (msg.what) {
//                case MSG_BUY_OK:
//                    Intent i = new Intent(LotteryOrderActivity.this, PayActivity.class);
//                    i.putExtra(PARAM_TOTAL_MONEY, mTotalMoney);
//                    startActivity(i);
//
//                    UserBean user = MyApplication.getCurrLoginUser();
//                    long money = Long.valueOf(user.getMoney()) - mTotalMoney;
//                    user.setMoney(String.valueOf(money));
//                    MyApplication.updateCurrLoginUser(user);
//                    EventBus.getDefault().post(new UserInfoUpdateEvent(user));
//                    LotteryOrderDbUtils.deleteAllLotteryOrder();
//                    break;
//                case MSG_BUY_FAIL:
//                    break;
                case MSG_START_COUNT_DOWN:
                    mTvCountDown.setText(DateUtils.formatTime_mm_ss(mCountDownNumber * 1000));
                    mCountDownNumber--;
                    if (mCountDownNumber >= 0) {
                        mHandler.sendEmptyMessageDelayed(MSG_START_COUNT_DOWN, 1000);
                    } else {
                        UIHelper.showToast("本期截止时间已过期，购买下一期");
//                        mPresenter.requestSequence(mLotteryType);
                        showConfirmNextChase();
                    }
                    break;
            }
        }
    };

    private void showConfirmNextChase() {
        AlertDialog dialog = new AlertDialog(LotteryOrderActivity.this)
                .builder()
                .setTitle(getString(R.string.tips_tip))
                .setMsg(getString(R.string.tip_period_timeout))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.tip_good), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.requestSequence(mLotteryType);
                    }
                });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_order);
        ButterKnife.bind(this);
        initIntentData();
        initView();
        intAdapter();
        initPresenter();

        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryBuyOkEvent(LotteryBuyOkEvent event) {
        if (event.isOK()) {
            this.finish();
        }
    }

    private void initIntentData() {
        mLotteryType = getIntent().getStringExtra(PARAM_LOTTERY_TYPE);
        mPlayMode = getIntent().getIntExtra(PARAM_LOTTERY_PLAY_TYPE, 0);
        Log.e(TAG, "initIntentData: mLotteryType is " + mLotteryType + " and mPlayMode is " + mPlayMode);
    }

    private void initPresenter() {
        mPresenter = new LotteryOrderPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.requestSequence(mLotteryType);
        //TODO 新增彩种需要适配的地方 Y
        mPresenter.initDoubleColorData(mLotteryType, -1);
    }

    private void intAdapter() {
        mAdapter = new LotteryOrderAdapter(this, this, this);
        mOrderListRv.setAdapter(mAdapter);
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)
                || mLotteryType.equals(LOTTERY_TYPE_11_5)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_LUCKY)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_OLD)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_YUE)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_YILE)
                || mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                || mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            UIHelper.showWidget(mLlSequence, true);
            titleTv.setText((String) SharedPreferenceUtil.get(mContext, mLotteryType, ""));
        } else {
            UIHelper.showWidget(mLlSequence, false);
        }


        View backBtn = findViewById(R.id.title_back);
        UIHelper.showWidget(backBtn, true);
        backBtn.setOnClickListener(this);

        mOrderListRv = findViewById(R.id.order_rv_orderList);
        mOrderListRv.setLayoutManager(new LinearLayoutManager(this));
        mOrderListRv.setOnItemActionListener(this);

        findViewById(R.id.order_btn_manualAdd).setOnClickListener(this);
        findViewById(R.id.order_btn_machineAdd).setOnClickListener(this);
        findViewById(R.id.order_btn_pay).setOnClickListener(this);

        mTotalCountTv = findViewById(R.id.order_tv_totalCount);
        mTotalMoneyTv = findViewById(R.id.order_tv_totalMoney);

        mMultipleEt = findViewById(R.id.multiple);
        mMultipleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = mMultipleEt.getText().toString();
                try {
                    if (!TextUtils.isEmpty(inputStr) && inputStr.startsWith("0")) {
                        inputStr = inputStr.substring(1, inputStr.length());
                        resetEditText(mMultipleEt, inputStr);
                    }
                    mMultiple = Integer.parseInt(inputStr);
                    if (mMultiple > mMaxMultiple) {
                        UIHelper.showToast("最大倍数为" + mMaxMultiple);
                        resetEditText(mMultipleEt, "" + mMaxMultiple);
                        mMultiple = mMaxMultiple;
                    } else if (mMultiple == 0) {
                        mMultiple = 1;
                        resetEditText(mMultipleEt, "1");
                    }
                    showTotalInfo();
                } catch (NumberFormatException e) {
                    mMultiple = 1;
                }
            }
        });
        mChaseEt = findViewById(R.id.chase);
        mChaseEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = mChaseEt.getText().toString();
                try {
                    if (!TextUtils.isEmpty(inputStr) && inputStr.startsWith("0")) {
                        inputStr = inputStr.substring(1, inputStr.length());
                        resetEditText(mChaseEt, inputStr);
                    }
                    mChase = Integer.parseInt(inputStr);
                    if (mChase > mMaxChase) {
                        UIHelper.showToast("最大追期为" + mMaxChase + "期");
                        resetEditText(mChaseEt, "" + mMaxChase);
                        mChase = mMaxChase;
                    } else if (mChase == 0) {
                        mChase = 1;
                        resetEditText(mChaseEt, "1");
                    }
                    showTotalInfo();
                } catch (NumberFormatException e) {
                    mChase = 1;
                }
            }
        });
        mMultipleEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mMultipleEt.selectAll();
            }
        });
        mChaseEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mChaseEt.selectAll();
            }
        });
    }

    private void resetEditText(EditText v, String hintStr) {
        v.setText(hintStr);
        v.setSelection(v.getText().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack();
                break;
            case R.id.order_btn_manualAdd:
                //TODO 新增彩种需要适配的地方
                Log.e(TAG, "onClick:mPlayMode is " + mPlayMode);
                if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
                    FastThreeActivity.intoThisActivity(LotteryOrderActivity.this, mLotteryType, mPlayMode);
                } else if (mLotteryType.equals(LOTTERY_TYPE_11_5)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_OLD)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_LUCKY)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_YUE)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_YILE)) {
                    Eleven5Activity.intoThisActivity(LotteryOrderActivity.this, mLotteryType, mPlayMode);
                } else if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                        || mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                    AlwaysColorActivity.intoThisActivity(LotteryOrderActivity.this, mLotteryType, mPlayMode);
                } else if (mLotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
                    UIHelper.gotoActivity(LotteryOrderActivity.this, DoubleColorActivity.class, false);
                } else if (mLotteryType.equals(LOTTERY_TYPE_3_D)) {
                    ThreeDActivity.intoThisActivity(LotteryOrderActivity.this, THREE_D_PLAY_DIRECT);
                } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
                    Arrange3Activity.intoThisActivity(LotteryOrderActivity.this, ARRANGE_3_PLAY_DIRECT);
                } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
                    Arrange5Activity.intoThisActivity(LotteryOrderActivity.this);
                } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
                    SevenHappyActivity.intoThisActivity(LotteryOrderActivity.this);
                } else if (mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {
                    SevenStarActivity.intoThisActivity(LotteryOrderActivity.this);
                }
                break;
            case R.id.order_btn_machineAdd:
                mPresenter.machineAdd(true, mLotteryType, mPlayMode);
                break;
            case R.id.order_btn_pay:
                if (mSequenceBean == null) {
                    ToastUtil.getInstances().showShort("正在获取当前期次信息");
                    mPresenter.requestSequence(mLotteryType);
                    return;
                }
                //true允许购买
                boolean orderState = getOrderState();
                LogF.d("111111", "当前是否可以购彩===" + orderState);
                if (orderState) {
                    //先判断登录
                    if (MyApplication.getCurrLoginUser() == null) {
                        UIHelper.showToast("请先登录");
                        UIHelper.gotoActivity(this, LoginActivity.class, false);
                        hideWaitingDialog();
                        return;
                    }
                    //判断绑定手机号
                    UserBean userBean = MyApplication.getCurrLoginUser();
                    if (!userBean.isPhoneOk()) {
                        BindingPhoneActivity.bindingPhoneLauncher(this, 0);
                        return;
                    }
                    if (!userBean.isPayPasswordOk()) {
                        PayPasswordActivity.intoThisActivity(mContext, REQUEST_CODE_SET_PAYPASSWORD);
                        return;
                    }

                    //判断设置安全密码
                    //创建订单
                    mPresenter.createOrder(mOrders, mChase, mMultiple, mPlayMode, mLotteryType, mSequenceBean.getCur_period());
                } else {
                    ToastUtil.getInstances().showShort("该彩种已暂停销售");
                }
                break;
        }
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {
        if (isOk) {
            // SEND BUY OK EVENT
//            EventBus.getDefault().post(new LotteryBuyOkEvent(true));
            PayActivity.intoThisActivity(LotteryOrderActivity.this, mTotalMoney, mLotteryType, (String) msgStr);
        } else {
            UIHelper.showToast((String) msgStr);
            if (isNeedLogin) {//需要登录
                UIHelper.gotoActivity(this, LoginActivity.class, false);
            }
        }
    }

    @Override
    public void machineAddResult(LotteryOrder order, boolean isOk) {
        if (isOk) {
            mAdapter.addOneOrder(order);
            checkLatestOrder();
        } else {
            UIHelper.showToast("机选失败");
        }

    }

    @Override
    public void deleteResult(boolean isOk, Object errorStr) {
        if (isOk) {
            mAdapter.deleteOrder(mDeleteLotteryOrder);
            checkLatestOrder();
        } else {
            UIHelper.showToast((String) errorStr);
        }
    }

    @Override
    public void showWaitingDialog(String msgStr) {
        showWaitingDialog(LotteryOrderActivity.this, msgStr, false);
    }

    @Override
    public void hiddenWaitingDialog() {
        hideWaitingDialog();
    }

    @Override
    public void initLotteryOrderListResult(List<LotteryOrder> mOrders) {
        this.mOrders = mOrders;
        mAdapter.setOrderList(mOrders);
        checkLatestOrder();
    }

    @Override
    public void onRequestSequence(boolean b, Object msgStr) {
        if (b) {
            if (msgStr instanceof ResultSequenceBeanNew.SequenceBean) {//新的
                mSequenceBean = (ResultSequenceBeanNew.SequenceBean) msgStr;
                if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                        || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_OLD)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_YUE)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_LUCKY)
                        || mLotteryType.equals(LOTTERY_TYPE_11_5_YILE)
                        || mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                        || mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                    mCountDownNumber = mSequenceBean.getDraw_time_left();
                    if (mCountDownNumber < 0) {
                        mCountDownNumber *= -1;
                    }
                    mTvSequence.setText("距" + mSequenceBean.getCur_period() + "期截止");
                    //true允许购买
                    boolean orderState = getOrderState();
                    LogF.d("111111", "当前是否可以购彩===" + orderState);
                    if (orderState) {
                        //取消上一次倒计时
                        if (null != mHandler) {
                            mHandler.removeMessages(MSG_START_COUNT_DOWN);
                            mHandler.sendEmptyMessage(MSG_START_COUNT_DOWN);
                        }
                    } else {
                        mTvCountDown.setText("暂停销售");
                    }
                }
            }
        } else {
            UIHelper.showToast("获取当前期号失败");
        }
    }

    @Override
    public void OrderChangedListener(long totalCount, String totalMoney) {
        this.mTotalCount = totalCount;
        this.mMoney = Double.valueOf(totalMoney);
        showTotalInfo();
    }

    private void showTotalInfo() {
        mTotalCountTv.setText(mTotalCount + "注" + mMultiple + "倍" + mChase + "期");
        mTotalMoney = StringUtils.getNumberNoZero(mMoney * mMultiple * mChase);
        mTotalMoneyTv.setText("共 " + mTotalMoney + " 元");
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnItemDelete(int position) {
        mDeleteLotteryOrder = mAdapter.getOrderByPosition(position);
        if (mDeleteLotteryOrder != null) {
            mPresenter.deleteDoubleColorOrder(mDeleteLotteryOrder);
        } else {
        }
    }

    private void checkLatestOrder() {
        LotteryOrder lotteryOrder = mAdapter.getLastestOrder();
        if (null != lotteryOrder) {
            mPlayMode = lotteryOrder.getPlayMode();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        if (mOrders != null && mOrders.size() > 0) {
//            LotteryOrderDbUtils.deleteAllLotteryOrder();
            UIHelper.showConfirmDialog(LotteryOrderActivity.this,
                    "是否清除本次选号?",
                    "清除",
                    "保存",
                    new ConfirmDialogCallback() {
                        @Override
                        public void onConfirmResult(boolean isOk) {
                            if (isOk) {//清除
                                List<LotteryOrder> lotteryOrders = LotteryOrderDbUtils.listAllLotteryOrder(mLotteryType);
                                if (null != lotteryOrders) {
                                    LotteryOrderDbUtils.deleteLotteryOrder(lotteryOrders);
                                }
//                                if (deleteOk) {
//                                    UIHelper.showToast("清空成功");
//                                    goBack(LotteryOrderActivity.this);
//                                } else {
//                                    UIHelper.showToast("清空失败");
//                                }
                                goBack(LotteryOrderActivity.this);
                            } else {
                                goBack(LotteryOrderActivity.this);
                                ToastUtil.getInstances().showShort(R.string.lottery_home_cache_tip);
                            }
                        }
                    });
        } else {
            goBack(LotteryOrderActivity.this);
        }
    }

    /**
     * @param context     前一个activity
     * @param lotteryType 彩票类别
     * @param playMode    玩法
     */
    public static void intoThisActivity(Context context, String lotteryType, int playMode) {
        Intent i = new Intent(context, LotteryOrderActivity.class);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
        i.putExtra(PARAM_LOTTERY_PLAY_TYPE, playMode);
        context.startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeMessages(MSG_START_COUNT_DOWN);
            mHandler = null;
        }
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {

    }

    //是否允许购买
    public boolean getOrderState() {
        boolean state = false;
        LotteryPageDataResponseBean lotteryBean = SharedPreferencesUtils.getLotteryPageDataInfo();
        List<LotteryType> lotteryTypes = lotteryBean.getPayload().getLotteries();
        for (int i = 0; i < lotteryTypes.size(); i++) {
            LotteryType typeBean = lotteryTypes.get(i);
            if (TextUtils.equals(mLotteryType, typeBean.getName())) {
                if (TextUtils.equals("2", typeBean.getLottery_state())) {
                    state = true; //销售
                } else if (TextUtils.equals("1", typeBean.getLottery_state())) {
                    state = false; //暂停销售
                }
            }
        }
        return state;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1://设置支付密码成功
                    LogF.d(TAG, "设置支付密码成功");
                    //创建订单
                    mPresenter.createOrder(mOrders, mChase, mMultiple, mPlayMode, mLotteryType, mSequenceBean.getCur_period());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
