package cn.zcgames.lottery.home.lottery.alwaycolor.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;

import static cn.zcgames.lottery.app.AppConstants.*;

import cn.zcgames.lottery.bean.response.ResultSequenceBeanNew;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.lottery.alwaycolor.presenter.AlwaysColorPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.lottery.elevenfive.view.activity.Eleven5Activity;
import cn.zcgames.lottery.home.view.activity.TrendActivity;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.result.view.activity.ResultHistoryNewActivity;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.fragment.FiveAllFragment;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.fragment.NotNumberFragment;
import cn.zcgames.lottery.home.view.iview.IAlwaysColorActivity;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;


/**
 * 重庆时时彩
 * Berfy修改
 * 2018.8.23
 */
public class AlwaysColorActivity extends BaseActivity implements IAlwaysColorActivity {

    private static final String TAG = "FastThreeActivity";
    @BindView(R.id.ac_empty_view)
    View emptyView;

    private Context mContext;

    private static final int MSG_UPDATE_COUNT_DOWN = 0;
    private static final int MSG_REQUEST_CURRENT_SEQUENCE_OK = 3;
    private static final int MSG_REQUEST_CURRENT_SEQUENCE_FAIL = 4;

    @BindView(R.id.title_back)
    ImageButton mBackBtn;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.imageView_label_alwaysColor)
    ImageView mStyleLabelIv;
    @BindView(R.id.always_ll_changeStyle)
    LinearLayout mChangeStyleBtn;
    @BindView(R.id.always_tv_sequence)
    TextView mSequenceTv;
    @BindView(R.id.always_tv_countDown)
    TextView mCountDownTv;
    @BindView(R.id.ll_history)
    LinearLayout mLlHistory;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.always_ll_selectLayout)
    LinearLayout mSelectStyleLayout;
    @BindView(R.id.always_tv_5all)
    TextView alwaysTv5All;
    @BindView(R.id.always_tv_5direct)
    TextView alwaysTv5Direct;
    @BindView(R.id.always_tv_3direct)
    TextView alwaysTv3Direct;
    @BindView(R.id.always_tv_2direct)
    TextView alwaysTv2Direct;
    @BindView(R.id.always_tv_2group)
    TextView alwaysTv2Group;
    @BindView(R.id.always_tv_3group3)
    TextView alwaysTv3Group3;
    @BindView(R.id.always_tv_3group6)
    TextView alwaysTv3Group6;
    @BindView(R.id.always_tv_1direct)
    TextView alwaysTv1Direct;
    @BindView(R.id.always_tv_different)
    TextView alwaysTvBigSingle;

    @BindView(R.id.always_rv_history)
    RecyclerView mHistoryRv;
    @BindView(R.id.always_ll_hideLayout)
    LinearLayout mHideLayoutLl;

    private TextView[] mSelectStyleBtns;
    private String mLotteryType;//彩种
    private String mLotteryName;//彩种名称
    private int mPlayStyle = ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;//玩法
    private long mCountDownTime = 60 * 10 * 1000;//默认倒计时时间
    private boolean mIsHideLayoutShow = false;//历史开奖是否隐藏
    private boolean mIsSelectStyleLayoutShow = true;//选择玩法layout是否隐藏
    private AlwaysColorPresenter mPresenter;//
    private ResultSequenceBeanNew.SequenceBean mCurrentSequence;//当前的期号

    private FiveAllFragment mFiveAllFragment, mFiveDirectFragment,
            mThreeDirectFragment, mTowDirectFragment,
            mTowGroupFragment, m3Group3Fragment, m3Group6Fragment, mOneDirectFragment;
    private final String FRAGMENT_TAG_FIVE_ALL = "5aLL";
    private final String FRAGMENT_TAG_FIVE_DIRECT = "5direct";
    private final String FRAGMENT_TAG_THREE_DIRECT = "3direct";
    private final String FRAGMENT_TAG_TOW_DIRECT = "2direct";
    private final String FRAGMENT_TAG_TOW_GROUP = "2group";
    private final String FRAGMENT_TAG_Three_GROUP_Three = "3group3";
    private final String FRAGMENT_TAG_Three_GROUP_SIX = "3group6";
    private final String FRAGMENT_TAG_ONE_DIRECT = "1direct";

    private NotNumberFragment mNotNumberFragment;
    private final String FRAGMENT_TAG_NOT_NUMBER = "notNumber";
    private CustomPopupWindow customPopupWindow;

    private final String[] fragmentTags = new String[]{FRAGMENT_TAG_NOT_NUMBER, FRAGMENT_TAG_ONE_DIRECT, FRAGMENT_TAG_TOW_DIRECT,
            FRAGMENT_TAG_TOW_GROUP, FRAGMENT_TAG_THREE_DIRECT, FRAGMENT_TAG_Three_GROUP_Three,
            FRAGMENT_TAG_Three_GROUP_SIX, FRAGMENT_TAG_FIVE_DIRECT, FRAGMENT_TAG_FIVE_ALL};
    private MyHandler mHandler;

    private static class MyHandler extends Handler {

        private WeakReference<AlwaysColorActivity> reference;

        private MyHandler(AlwaysColorActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == reference.get()) {
                return;
            }
            if (reference.get().isFinishing()) {
                return;
            }
            switch (msg.what) {
                case MSG_UPDATE_COUNT_DOWN:
                    reference.get().mCountDownTv.setText(DateUtils.formatTime_mm_ss(reference.get().mCountDownTime * 1000));
                    reference.get().mCountDownTime = reference.get().mCountDownTime - 1;
                    if (reference.get().mCountDownTime >= 0) {
                        sendEmptyMessageDelayed(MSG_UPDATE_COUNT_DOWN, 1000);
                    } else {
                        reference.get().showConfirmNextChase();
                    }
                    break;
                case MSG_REQUEST_CURRENT_SEQUENCE_OK:
                    if (msg.obj instanceof ResultSequenceBeanNew.SequenceBean) {
                        reference.get().mCurrentSequence = (ResultSequenceBeanNew.SequenceBean) msg.obj;
                        reference.get().showCurrentSequence(reference.get().mCurrentSequence);
                    }
                    break;
            }
        }
    }

    private void showCurrentSequence(ResultSequenceBeanNew.SequenceBean bean) {
        mSequenceTv.setText("距" + bean.getCur_period() + "期截止");
        mCountDownTime = bean.getDraw_time_left();

        //true允许购买
        boolean orderState = getOrderState();
        LogF.d("111111", "当前是否可以购彩===" + orderState);
        if (orderState) {
            if (null != mHandler)
                if (mCountDownTime <= 0) {
                    //如果获取的截止时间小于等于0，稍后1s再去请求期号
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_COUNT_DOWN, 1000);
                } else {
                    mHandler.sendEmptyMessage(MSG_UPDATE_COUNT_DOWN);
                }
        } else {
            mCountDownTv.setText("暂停销售");
        }

    }

    private void showConfirmNextChase() {
        if (ActivityManager.getInstance().currentActivity() != this) {
            return;
        }
        AlertDialog dialog = new AlertDialog(this)
                .builder()
                .setTitle(getString(R.string.tips_tip))
                .setMsg(getString(R.string.tip_period_timeout))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.btn_cancel), v ->
                        goBack(AlwaysColorActivity.this)).setPositiveButton(getString(R.string.tip_good),
                        v -> requestData());
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_always_color);
        this.mContext = this;
        ButterKnife.bind(this);
        initIntent();
        mDown2UpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_up_to_down);
        mUp2DownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_down_to_up);
        initView();
        showFragmentByPlayStyle();
        initPopupWindow();
        initPresenter();
        EventBus.getDefault().register(this);
    }

    private Animation mDown2UpAnimation, mUp2DownAnimation;

    private void initPresenter() {
        mPresenter = new AlwaysColorPresenter(this);
        requestData();
    }

    private void requestData() {
        mPresenter.requestCurrentSequence(mLotteryType);
    }

    private void initView() {
        mHandler = new MyHandler(this);
        ImageButton moreIb = findViewById(R.id.title_right_button);
        moreIb.setImageResource(R.drawable.btn_more);
        mSelectStyleBtns = new TextView[]{alwaysTv1Direct, alwaysTv2Direct, alwaysTv2Group,
                alwaysTv3Direct, alwaysTv3Group3, alwaysTv3Group6, alwaysTv5Direct, alwaysTv5All,
                alwaysTvBigSingle};
    }

    private void showFragmentByPlayStyle() {
        LogF.d(TAG, "切换选项" + mPlayStyle);
        switch (mPlayStyle) {
            case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                alwaysTvBigSingle.performClick();
                break;
            case ALWAYS_COLOR_1_DIRECT:
                alwaysTv1Direct.performClick();
                break;
            case ALWAYS_COLOR_2_GROUP:
                alwaysTv2Group.performClick();
                break;
            case ALWAYS_COLOR_2_DIRECT:
                alwaysTv2Direct.performClick();
                break;
            case ALWAYS_COLOR_3_DIRECT:
                alwaysTv3Direct.performClick();
                break;
            case ALWAYS_COLOR_3_GROUP_3:
                alwaysTv3Group3.performClick();
                break;
            case ALWAYS_COLOR_3_GROUP_6:
                alwaysTv3Group6.performClick();
                break;
            case ALWAYS_COLOR_5_DIRECT:
                alwaysTv5Direct.performClick();
                break;
            case ALWAYS_COLOR_5_ALL:
                alwaysTv5All.performClick();
                break;
        }
    }

    private void initIntent() {
        mLotteryType = getIntent().getStringExtra(ActivityConstants.PARAM_LOTTERY_TYPE);
        mPlayStyle = getIntent().getIntExtra(ActivityConstants.PARAM_LOTTERY_PLAY_TYPE, ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
        switch (mLotteryType) {
            case LOTTERY_TYPE_ALWAYS_COLOR:
                mLotteryName = getString(R.string.lottery_type_always_color);
                break;
            case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                mLotteryName = getString(R.string.lottery_type_always_color_new);
                break;
        }
    }

    private void initPopupWindow() {
        //下面的操作是初始化弹出数据
        ArrayList<String> strList = new ArrayList<>();
        strList.add(getString(R.string.lottery_title_result_trend));
        strList.add(getString(R.string.lottery_title_history_result));
        strList.add(getString(R.string.lottery_title_play_info));


        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener clickListener = v -> {
            String msg = (String) v.getTag();
            if (msg.equals(getString(R.string.lottery_title_history_result))) {
                TrendActivity.intoThisActivity(mContext, mLotteryType, mPlayStyle, LATEST_OPEN_AWARD);
            } else if (msg.equals(getString(R.string.lottery_title_result_trend))) {
                TrendActivity.intoThisActivity(mContext, mLotteryType, mPlayStyle, TREND_VIEW);
            } else {
                UIHelper.gotoActivity(AlwaysColorActivity.this, ACDescriptionActivity.class, false);
            }
            customPopupWindow.dismissPopupWindow();
        };
        clickList.add(clickListener);
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        customPopupWindow = new CustomPopupWindow(this, strList, clickList);
    }

    public void onPlayStyleSelect(View v) {
        showOrHideSelectLayout();
        int id = v.getId();
        String title = "";
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        switch (id) {
            case R.id.always_tv_1direct:
                if (mOneDirectFragment == null) {
                    mOneDirectFragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_1_DIRECT);
                    transaction.add(R.id.fragment_container, mOneDirectFragment, FRAGMENT_TAG_ONE_DIRECT);
                }
                transaction.show(mOneDirectFragment);
                mPlayStyle = ALWAYS_COLOR_1_DIRECT;
                title = mLotteryName + "-一星直选";
                break;

            case R.id.always_tv_2direct:
                if (mTowDirectFragment == null) {
                    mTowDirectFragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_2_DIRECT);
                    transaction.add(R.id.fragment_container, mTowDirectFragment, FRAGMENT_TAG_TOW_DIRECT);
                }
                transaction.show(mTowDirectFragment);
                mPlayStyle = ALWAYS_COLOR_2_DIRECT;
                title = mLotteryName + "-二星直选";
                break;

            case R.id.always_tv_2group:
                if (mTowGroupFragment == null) {
                    mTowGroupFragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_2_GROUP);
                    transaction.add(R.id.fragment_container, mTowGroupFragment, FRAGMENT_TAG_TOW_GROUP);
                }
                transaction.show(mTowGroupFragment);
                mPlayStyle = ALWAYS_COLOR_2_GROUP;
                title = mLotteryName + "-二星组选";
                break;

            case R.id.always_tv_3direct:
                if (mThreeDirectFragment == null) {
                    mThreeDirectFragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_3_DIRECT);
                    transaction.add(R.id.fragment_container, mThreeDirectFragment, FRAGMENT_TAG_THREE_DIRECT);
                }
                transaction.show(mThreeDirectFragment);
                mPlayStyle = ALWAYS_COLOR_3_DIRECT;
                title = mLotteryName + "-三星直选";
                break;

            case R.id.always_tv_3group3:
                if (m3Group3Fragment == null) {
                    m3Group3Fragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_3_GROUP_3);
                    transaction.add(R.id.fragment_container, m3Group3Fragment, FRAGMENT_TAG_Three_GROUP_Three);
                }
                transaction.show(m3Group3Fragment);
                mPlayStyle = ALWAYS_COLOR_3_GROUP_3;
                title = mLotteryName + "-三星组三";
                break;

            case R.id.always_tv_3group6:
                if (m3Group6Fragment == null) {
                    m3Group6Fragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_3_GROUP_6);
                    transaction.add(R.id.fragment_container, m3Group6Fragment, FRAGMENT_TAG_Three_GROUP_SIX);
                }
                transaction.show(m3Group6Fragment);
                mPlayStyle = ALWAYS_COLOR_3_GROUP_6;
                title = mLotteryName + "-三星组六";
                break;

            case R.id.always_tv_5direct:
                if (mFiveDirectFragment == null) {
                    mFiveDirectFragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_5_DIRECT);
                    transaction.add(R.id.fragment_container, mFiveDirectFragment, FRAGMENT_TAG_FIVE_DIRECT);
                }
                transaction.show(mFiveDirectFragment);
                mPlayStyle = ALWAYS_COLOR_5_DIRECT;
                title = mLotteryName + "-五星直选";
                break;
            case R.id.always_tv_5all:
                if (mFiveAllFragment == null) {
                    mFiveAllFragment = FiveAllFragment.newInstance(mLotteryType, ALWAYS_COLOR_5_ALL);
                    transaction.add(R.id.fragment_container, mFiveAllFragment, FRAGMENT_TAG_FIVE_ALL);
                }
                transaction.show(mFiveAllFragment);
                mPlayStyle = ALWAYS_COLOR_5_ALL;
                title = mLotteryName + "-五星通选";
                break;
            case R.id.always_tv_different:
                if (mNotNumberFragment == null) {
                    mNotNumberFragment = NotNumberFragment.newInstance(mLotteryType);
                    transaction.add(R.id.fragment_container, mNotNumberFragment, FRAGMENT_TAG_NOT_NUMBER);
                }
                transaction.show(mNotNumberFragment);
                mPlayStyle = ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
                title = mLotteryName + "-大小单双";
                break;
        }
        mTitleTv.setText(title);
        transaction.commit();
    }

    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    @OnClick({R.id.title_back, R.id.always_ll_changeStyle, R.id.title_right_button, R.id.ll_history, R.id.always_rl_history,
            R.id.always_tv_5all, R.id.always_tv_2direct, R.id.always_tv_5direct, R.id.always_tv_3direct,
            R.id.always_tv_different, R.id.always_tv_2group, R.id.always_tv_3group3, R.id.always_tv_3group6,
            R.id.always_tv_1direct})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(AlwaysColorActivity.this);
                break;
            case R.id.always_ll_changeStyle:
                showOrHideSelectLayout();
                break;
            case R.id.title_right_button:
                //                UIHelper.gotoActivity(this, ACDescriptionActivity.class, false);
                //弹出选项弹窗
                customPopupWindow.showPopupWindow(view);
                break;
            case R.id.ll_history:
                TrendActivity.intoThisActivity(mContext, mLotteryType, mPlayStyle, LATEST_OPEN_AWARD);
                break;
            case R.id.always_rl_history:
                UIHelper.showWidget(mHideLayoutLl, !mIsHideLayoutShow);
                mIsHideLayoutShow = !mIsHideLayoutShow;
                break;
            case R.id.always_tv_5all:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_2direct:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_5direct:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_3direct:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_different:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_2group:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_3group3:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_3group6:
                onPlayStyleSelect(view);
                break;
            case R.id.always_tv_1direct:
                onPlayStyleSelect(view);
                break;
        }
    }

    private void showOrHideSelectLayout() {
        UIHelper.showWidget(mSelectStyleLayout, !mIsSelectStyleLayoutShow);
        if (!mIsSelectStyleLayoutShow) {
            if (mPlayStyle == ALWAYS_COLOR_1_DIRECT) {
                showSelectButton(0);
            } else if (mPlayStyle == ALWAYS_COLOR_2_DIRECT) {
                showSelectButton(1);
            } else if (mPlayStyle == ALWAYS_COLOR_2_GROUP) {
                showSelectButton(2);
            } else if (mPlayStyle == ALWAYS_COLOR_3_DIRECT) {
                showSelectButton(3);
            } else if (mPlayStyle == ALWAYS_COLOR_3_GROUP_3) {
                showSelectButton(4);
            } else if (mPlayStyle == ALWAYS_COLOR_3_GROUP_6) {
                showSelectButton(5);
            } else if (mPlayStyle == ALWAYS_COLOR_5_DIRECT) {
                showSelectButton(6);
            } else if (mPlayStyle == ALWAYS_COLOR_5_ALL) {
                showSelectButton(7);
            } else if (mPlayStyle == ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE) {
                showSelectButton(8);
            }
        }

        if (mIsSelectStyleLayoutShow) {
            if (mStyleLabelIv != null) {
                mStyleLabelIv.startAnimation(mUp2DownAnimation);
            }
        } else {
            mStyleLabelIv.startAnimation(mDown2UpAnimation);
        }
        mIsSelectStyleLayoutShow = !mIsSelectStyleLayoutShow;
    }

    private void showSelectButton(int pos) {

        for (int i = 0; i < mSelectStyleBtns.length; i++) {
            if (pos != i) {
                mSelectStyleBtns[i].setSelected(false);
                mSelectStyleBtns[i].setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_tab_text));
            } else {
                mSelectStyleBtns[i].setSelected(true);
                mSelectStyleBtns[i].setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_red_ball));
            }
        }
    }

    /**
     * 进入此activity的入口
     *
     * @param fromActivity
     * @param playStyle
     */
    public static void intoThisActivity(Context fromActivity, String lotteryType, int playStyle) {
        Intent i = new Intent(fromActivity, AlwaysColorActivity.class);
        i.putExtra(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        i.putExtra(ActivityConstants.PARAM_LOTTERY_PLAY_TYPE, playStyle);
        fromActivity.startActivity(i);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryBuyOkEvent(LotteryBuyOkEvent event) {
        if (event.isOK()) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    //开奖记录返回结果
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
    public void requestCurrentSequenceResult(boolean isOk, Object obj) {
        if (mHandler == null) {
            return;
        }
        if (isOk) {
            Message msg = new Message();
            msg.what = MSG_REQUEST_CURRENT_SEQUENCE_OK;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        } else {
            mHandler.sendEmptyMessage(MSG_REQUEST_CURRENT_SEQUENCE_FAIL);
        }
    }

    @Override
    public void onCreateLocalOrderResult(boolean isOk, String msgStr) {

    }

    @OnClick(R.id.ac_empty_view)
    public void onViewClicked() {
        showOrHideSelectLayout();
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

}
