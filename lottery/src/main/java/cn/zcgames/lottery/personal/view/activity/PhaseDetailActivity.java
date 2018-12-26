package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.ChaseDetail_7Star_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_A5_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_AC_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_DC_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_SH_Bean;
import cn.zcgames.lottery.bean.ChaseDetail_TD_Bean;
import cn.zcgames.lottery.home.bean.DoubleColorOrderBet;
import cn.zcgames.lottery.bean.LotteryOrderDetail;
import cn.zcgames.lottery.bean.PeriodBean;
import cn.zcgames.lottery.bean.StakesBean;
import cn.zcgames.lottery.personal.model.ChaseDetailStatus;
import cn.zcgames.lottery.personal.presenter.AfterPhasePresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.personal.view.adapter.LotteryPhaseDetailAdapter;
import cn.zcgames.lottery.personal.view.adapter.ChaseDetailStatusAdapter;
import cn.zcgames.lottery.base.IBaseView;

import static cn.zcgames.lottery.app.ActivityConstants.*;
import static cn.zcgames.lottery.app.AppConstants.*;

/**
 * 追期详情
 *
 * @author NorthStar
 * @date 2018/8/20 17:43
 */
public class PhaseDetailActivity extends BaseActivity implements View.OnClickListener, IBaseView {

    private static final String TAG = "PhaseDetailActivity";

    private static final int MSG_UPDATE_DATA_VIEW = 0;
    private static final int MSG_NO_DATA = 1;

    @BindView(R.id.title_back)
    ImageButton titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_right_button)
    ImageButton titleRightButton;
    @BindView(R.id.chaseDetail_tv_lotteryTitle)
    TextView mTvLotteryTitle;
    @BindView(R.id.chaseDetail_tv_orderMoney)
    TextView mTvOrderMoney;
    @BindView(R.id.chaseDetail_tv_chase)
    TextView mTvChase;
    @BindView(R.id.chaseDetail_tv_wardMoney)
    TextView mRewardMoneyTv;
    @BindView(R.id.chaseDetail_tv_date)
    TextView mTvDate;
    @BindView(R.id.chaseDetail_tv_count)
    TextView mTvCount;
    @BindView(R.id.chaseDetail_rv_order)
    RecyclerView mRvOrder;
    @BindView(R.id.chaseDetail_rv_ward)
    RecyclerView mRvWard;
    @BindView(R.id.iv_lottery)
    ImageView lotteryIv;

    private String mBillId;
    private String title;
    private String mLotteryName;//彩票名称
    private ChaseDetail_DC_Bean mDCDetail;
    private ChaseDetail_TD_Bean mTDDetail;
    private LotteryOrderDetail newDetail;
    private ChaseDetail_SH_Bean mSHDetail;
    private ChaseDetail_AC_Bean mACDetail;
    private ChaseDetail_A5_Bean mA5Detail;
    private ChaseDetail_7Star_Bean m7SDetail;
    private List<DoubleColorOrderBet> mBets = new ArrayList<>();
    private List<StakesBean> stakesBeans = new ArrayList<>();
    private List<ChaseDetailStatus> mChaseStatusList = new ArrayList<>();
    private AfterPhasePresenter mPresenter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_DATA_VIEW:
                    updateUI(msg);
                    break;
                case MSG_NO_DATA:
                    setEmpty();
                    break;
            }
        }
    };

    //无数据的重试
    private void setEmpty() {
        AlertDialog dialog = new AlertDialog(PhaseDetailActivity.this)
                .builder()
                .setCancelable(false)
                .setMsg("请求数据发生错误，重新请求?")
                .setNegativeButton("取消", v -> goBack(PhaseDetailActivity.this))
                .setPositiveButton("重试", v -> getPhaseDetail());
        dialog.show();
    }

    //获取网络数据更新UI
    private void updateUI(Message msg) {
        if (msg.obj != null && msg.obj instanceof LotteryOrderDetail) {
            //TODO 新增彩种需要适配的地方 Y
            if (mLotteryName.contains(LOTTERY_TYPE_2_COLOR)) {
                mDCDetail = (ChaseDetail_DC_Bean) msg.obj;
                //                            showView(mDCDetail);
            } else if (mLotteryName.contains(LOTTERY_TYPE_3_D) ||
                    mLotteryName.contains(LOTTERY_TYPE_ARRANGE_3)) {
                mTDDetail = (ChaseDetail_TD_Bean) msg.obj;
                //                            showView(mTDDetail);
            } else if (mLotteryName.contains(LOTTERY_TYPE_FAST_3)
                    || mLotteryName.contains(LOTTERY_TYPE_FAST_3_JS)
                    || mLotteryName.contains(LOTTERY_TYPE_FAST_3_HB)
                    || mLotteryName.contains(LOTTERY_TYPE_FAST_3_NEW)
                    || mLotteryName.contains(LOTTERY_TYPE_FAST_3_EASY)
                    || mLotteryName.equals(LOTTERY_TYPE_11_5)
                    || mLotteryName.equals(LOTTERY_TYPE_11_5_OLD)
                    || mLotteryName.equals(LOTTERY_TYPE_11_5_LUCKY)
                    || mLotteryName.equals(LOTTERY_TYPE_11_5_YUE)
                    || mLotteryName.equals(LOTTERY_TYPE_11_5_YILE)
                    || mLotteryName.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                    || mLotteryName.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                newDetail = (LotteryOrderDetail) msg.obj;//快3,11选5与时时彩
                showView(newDetail);
            } else if (mLotteryName.contains("seven") && mLotteryName.contains("happy")) {
                mSHDetail = (ChaseDetail_SH_Bean) msg.obj;
                //                            showView(mSHDetail);
            } else if (mLotteryName.contains(LOTTERY_TYPE_ARRANGE_5)) {
                mA5Detail = (ChaseDetail_A5_Bean) msg.obj;
                //                            showView(mA5Detail);
            } else if (mLotteryName.contains(LOTTERY_TYPE_7_STAR)) {
                m7SDetail = (ChaseDetail_7Star_Bean) msg.obj;
                //                            showView(m7SDetail);
            } else {
                Log.e(TAG, "handleMessage: ==============no implement===================");
            }
        } else {
            mHandler.sendEmptyMessage(MSG_NO_DATA);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_detail);
        ButterKnife.bind(this);
        initIntent();
        initView();
        initPresenter();
    }

    private void initIntent() {
        mBillId = getIntent().getStringExtra(PARAM_ID);
        mLotteryName = getIntent().getStringExtra(PARAM_LOTTERY_TYPE);
        LogF.d(TAG, "mLotteryName==>" + mLotteryName);
    }

    private void showView(LotteryOrderDetail bean) {
        //TODO 彩种icon
        String imageUrl = bean.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(this)
                    .load(imageUrl)
                    .error(R.drawable.icon_place)
                    .placeholder(R.drawable.icon_place)
                    .into(lotteryIv);
        }

        mTvOrderMoney.setText(String.format("%s元", StringUtils.getCash(bean.getCost(), DIGITS)));

        String chaseAll = String.format("%s-%s（共追%s期，已追%s期）", bean.getPeriod_start(),
                bean.getPeriod_end(), bean.getChase(), bean.getChased());
        String chase = String.format("（共追%s期，已追%s期）", bean.getChase(), bean.getChased());
        SpannableStringBuilder style = StringUtils.stringChangeColor(chaseAll, chase, null, "#FF999999");
        mTvChase.setText(style);

        String reward = String.format("%s元", StringUtils.getCash(bean.getTotal_rewards(), DIGITS));
        mRewardMoneyTv.setText("0".equals(bean.getChased()) ? "--" : reward);
        mRewardMoneyTv.setTextColor(CommonUtil.getColor(this, "0".equals(bean.getChased()) ?
                R.color.color_333333 : R.color.color_red));
        mTvDate.setText(TimeUtil.format("yyyy-MM-dd HH:mm", bean.getCreated()));

        String stakesCount = bean.getStakesCount();
        mTvCount.setText(String.format(Locale.CHINA, "%s倍 共%s注", bean.getTimes(), stakesCount));

        setAdapter(bean);
        //        mChaseStatusList = bean.get;//追期列表
        List<PeriodBean> periodData = bean.getPeriod();
        if (periodData == null || periodData.size() <= 0) return;
        setStateAdapter(periodData);
    }

    //配置期次适配器
    private void setStateAdapter(List<PeriodBean> bean) {
        for (int i = 0; i < bean.size(); i++) {
            ChaseDetailStatus status = new ChaseDetailStatus();
            PeriodBean currentPeriod = bean.get(i);
            status.setSequence((currentPeriod.getPeriod()));
            String phaseState = "";
            String rewardState = "";
                //期数状态 1: 输； 2: 赢； 3: 未开奖
                int state = currentPeriod.getStatus();
                    switch (state) {
                        case 3:
                            phaseState = "未追期";
                            rewardState = "未开奖";
                            break;

                        case 2:
                            phaseState = "已追期";
                            rewardState = "已中奖";
                            break;

                        default:
                            phaseState = "已追期";
                            rewardState = "未中奖";
                            break;
                    }
                StringBuilder winNumbersBuilder = new StringBuilder();
                for (String num : currentPeriod.getWinNumbers()) {
                    winNumbersBuilder.append(num);
                    winNumbersBuilder.append(" ");
                }
                status.setWinNumber(winNumbersBuilder.toString());
                status.setBillStatus(phaseState);
                status.setRewardStatus(rewardState);
                mChaseStatusList.add(status);
        }
        ChaseDetailStatusAdapter statusAdapter = new ChaseDetailStatusAdapter(PhaseDetailActivity.this, mChaseStatusList);
        mRvWard.setAdapter(statusAdapter);
    }

    private void setAdapter(LotteryOrderDetail bean) {
        setLotteryTitle(bean);
        LotteryPhaseDetailAdapter adapter = new LotteryPhaseDetailAdapter(stakesBeans, this, mLotteryName);
        mRvOrder.setAdapter(adapter);
    }

    private void setLotteryTitle(LotteryOrderDetail bean) {
        //TODO 新增彩种需要适配的地方 Y
        //        if (mLotteryType.contains(LOTTERY_TYPE_2_COLOR)) {
        //            mBets = mDCDetail.getBets();
        //            title = "双色球";
        //            adapter = new DetailChaseOrderAdapter(mBets, this, LOTTERY_TYPE_2_COLOR);
        //        } else if (mLotteryType.contains(LOTTERY_TYPE_3_D)) {
        //            mBets = LotteryUtils.switchThreeDBillToDCBet(mTDDetail.getBets());
        //            title = "福彩3D";
        //            adapter = new DetailChaseOrderAdapter(mBets, this, LOTTERY_TYPE_3_D);
        //        } else if (mLotteryType.contains( LOTTERY_TYPE_FAST_3)) {
        //            mBets = LotteryUtils.switchA3BillToDCBet(mTDDetail.getBets());
        //            title = "排列三";
        //            adapter = new DetailChaseOrderAdapter(mBets, this, LOTTERY_TYPE_ARRANGE_3);
        //        } else if (mLotteryType.contains("seven") && mLotteryType.contains("happy")) {
        //            title = "七乐彩";
        //            mBets = LotteryUtils.switchSH2DCBet(mSHDetail.getBets().getPlain());
        //            adapter = new DetailChaseOrderAdapter(mBets, this, LOTTERY_TYPE_7_HAPPY);
        //        } else if (mLotteryType.contains(LOTTERY_TYPE_ALWAYS_COLOR)) {
        //            title = "时时彩";
        //            mBets = LotteryUtils.switchAC2DCBet(mACDetail.getBets());
        //            adapter = new DetailChaseOrderAdapter(mBets, this, LOTTERY_TYPE_ALWAYS_COLOR);
        //        } else if (mLotteryType.contains(LOTTERY_TYPE_ARRANGE_5)) {
        //            title = "排列五";
        //            mBets = LotteryUtils.switchA52DCBet(mA5Detail.getBets().getDirect());
        //            adapter = new DetailChaseOrderAdapter(mBets, this, LOTTERY_TYPE_ARRANGE_5);
        //        } else if (mLotteryType.contains(LOTTERY_TYPE_7_STAR)) {
        //            title = "七星彩";
        //            mBets = LotteryUtils.switch7Star2DCBet(m7SDetail.getBets().getDirect());
        //            adapter = new DetailChaseOrderAdapter(mBets, this, LOTTERY_TYPE_7_STAR);
        //        }
        title = (String) SharedPreferenceUtil.get(mContext, mLotteryName, "");
        stakesBeans = bean.getStakes();
        mTvLotteryTitle.setText(title);
    }

    private void initPresenter() {
        mPresenter = new AfterPhasePresenter(this);
        getPhaseDetail();
    }

    private void initView() {
        titleTv.setText("追期详情");
        UIHelper.showWidget(titleBack, true);
        titleBack.setOnClickListener(this);

        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        mRvWard.setLayoutManager(new LinearLayoutManager(this));

    }

    //请求追期数据
    private void getPhaseDetail() {
        if (TextUtils.isEmpty(mLotteryName)) {
            LogF.d(TAG, "没有匹配的彩票名称");
            return;
        }
        mPresenter.requestPhaseBillDetail(mLotteryName, mBillId);
    }

    public static void intoThisActivity(Activity fromActivity, String orderId, String
            lotteryName) {
        if (TextUtils.isEmpty(orderId)) {
            LogF.e(TAG, "orderId no find");
            return;
        }
        LogF.d(TAG, orderId);
        Intent i = new Intent(fromActivity, PhaseDetailActivity.class);
        i.putExtra(PARAM_ID, orderId);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryName);
        fromActivity.startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(PhaseDetailActivity.this);
                break;
        }
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        Message msg = new Message();
        msg.obj = object;
        if (isOk) {
            msg.what = MSG_UPDATE_DATA_VIEW;
        } else {
            msg.what = MSG_NO_DATA;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void showTipDialog(String msgStr) {
        showWaitingDialog(PhaseDetailActivity.this, msgStr, false);
    }

    @Override
    public void hideTipDialog() {
        hideWaitingDialog();
    }
}
