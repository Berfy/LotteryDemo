package cn.zcgames.lottery.home.lottery.elevenfive.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.response.ResultSequenceBeanNew;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.lottery.elevenfive.view.fragment.Eleven5DirectFragment;
import cn.zcgames.lottery.home.lottery.elevenfive.view.fragment.Eleven5NormalFragment;
import cn.zcgames.lottery.home.lottery.elevenfive.presenter.Eleven5Presenter;
import cn.zcgames.lottery.home.view.activity.TrendActivity;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.home.lottery.elevenfive.view.fragment.Eleven5DanFragment;
import cn.zcgames.lottery.home.view.iview.IEleven5Activity;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_PLAY_TYPE;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_TYPE;
import static cn.zcgames.lottery.app.AppConstants.*;

/**
 * 11选5
 * Berfy修改
 * 2018.8.23
 */
public class Eleven5Activity extends BaseActivity implements IEleven5Activity, Animation.AnimationListener {

    private static final String TAG = "Eleven5Activity";
    @BindView(R.id.eleven5_empty_view)
    View emptyView;

    @BindView(R.id.playAny2)
    TextView playAny2;
    @BindView(R.id.playAny3)
    TextView playAny3;
    @BindView(R.id.playAny4)
    TextView playAny4;
    @BindView(R.id.playAny5)
    TextView playAny5;
    @BindView(R.id.playAny6)
    TextView playAny6;
    @BindView(R.id.playAny7)
    TextView playAny7;
    @BindView(R.id.playAny8)
    TextView playAny8;
    @BindView(R.id.playFontDirect1)
    TextView playFontDirect1;
    @BindView(R.id.playFontDirect2)
    TextView playFontDirect2;
    @BindView(R.id.playFontDirect3)
    TextView playFontDirect3;
    @BindView(R.id.playFontGroup2)
    TextView playFontGroup2;
    @BindView(R.id.playFontGroup3)
    TextView playFontGroup3;
    @BindView(R.id.playAnyDan2)
    TextView playAnyDan2;
    @BindView(R.id.playAnyDan3)
    TextView playAnyDan3;
    @BindView(R.id.playAnyDan4)
    TextView playAnyDan4;
    @BindView(R.id.playAnyDan5)
    TextView playAnyDan5;
    @BindView(R.id.playAnyDan6)
    TextView playAnyDan6;
    @BindView(R.id.playAnyDan7)
    TextView playAnyDan7;
    @BindView(R.id.playAnyDan8)
    TextView playAnyDan8;
    @BindView(R.id.playFontGroupDan2)
    TextView playFontGroupDan2;
    @BindView(R.id.playFontGroupDan3)
    TextView playFontGroupDan3;

    private Context mContext;

    private static final int MSG_UPDATE_COUNT_DOWN = 0;
    private static final int MSG_NO_HISTORY_DATA = 2;
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

    private TextView[] mSelectStyleBtns;
    private String mLotteryType;//彩种
    private int mPlayStyle = PLAY_11_5_ANY_2;//玩法
    private String mLotteryName;
    private long mCountDownTime = 60 * 10 * 1000;//默认倒计时时间
    private boolean mIsHideLayoutShow = false;//历史开奖是否隐藏
    private boolean mIsFistInit = true;//是否第一次进入页面
    private boolean mIsSelectStyleLayoutShow = true;//选择玩法layout是否隐藏
    private Eleven5Presenter mPresenter;//
    private ResultSequenceBeanNew.SequenceBean mCurrentSequence;//当前的期号

    private Eleven5DanFragment mDanFragment;
    private static final String DAN_FRAGMENT_TAG = "DAN";

    private Eleven5NormalFragment mNormalFragment;
    private static final String NORMAL_FRAGMENT_TAG = "NORMAL";

    private Eleven5DirectFragment mDirectFragment;
    private static final String DIRECT_FRAGMENT_TAG = "DIRECT";

    private final String[] fragmentTags = new String[]{
            DAN_FRAGMENT_TAG, NORMAL_FRAGMENT_TAG, DIRECT_FRAGMENT_TAG};

    private CustomPopupWindow customPopupWindow;

    private Animation mDown2UpAnimation, mUp2DownAnimation;

    private boolean mIsAnimRunning;
    private String title = "";//玩法
    private MyHandler mHandler;

    private static class MyHandler extends Handler {

        private WeakReference<Eleven5Activity> reference;

        private MyHandler(Eleven5Activity activity) {
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
                    reference.get().mCountDownTime--;
                    if (reference.get().mCountDownTime >= 0) {
                        sendEmptyMessageDelayed(MSG_UPDATE_COUNT_DOWN, 1000);
                    } else {
                        reference.get().showConfirmNextChase();
                    }
                    break;
                case MSG_NO_HISTORY_DATA:
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
        if (mCountDownTime < 0) {
            mCountDownTime *= -1;
        }
        //true允许购买
        boolean orderState = getOrderState();
        LogF.d("111111", "当前是否可以购彩===" + orderState);
        if (orderState) {
            if (null != mHandler)
                mHandler.sendEmptyMessage(MSG_UPDATE_COUNT_DOWN);
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
                .setNegativeButton(getString(R.string.btn_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBack(Eleven5Activity.this);
                    }
                }).setPositiveButton(getString(R.string.tip_good), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestData();
                    }
                });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_11_5);
        this.mContext = this;
        ButterKnife.bind(this);
        initIntent();
        mDown2UpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_up_to_down);
        mUp2DownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_down_to_up);
        mDown2UpAnimation.setAnimationListener(this);
        mUp2DownAnimation.setAnimationListener(this);
        initView();
        initPopupWindow();
        showFragmentByPlayStyle();
        initPresenter();
        EventBus.getDefault().register(this);
    }

    private void initPresenter() {
        mPresenter = new Eleven5Presenter(this, this);
        requestData();
    }

    private void requestData() {
        mPresenter.requestCurrentSequence(mLotteryType);
    }

    private void initView() {
        mHandler = new MyHandler(this);
        ImageButton moreIb = (ImageButton) findViewById(R.id.title_right_button);
        moreIb.setImageResource(R.drawable.btn_more);
        UIHelper.showWidget(moreIb, true);
        mSelectStyleBtns = new TextView[]{
                playAny2,
                playAny3,
                playAny4,
                playAny5,
                playAny6,
                playAny7,
                playAny8,
                playFontDirect1,
                playFontDirect2,
                playFontDirect3,
                playFontGroup2,
                playFontGroup3,
                playAnyDan2,
                playAnyDan3,
                playAnyDan4,
                playAnyDan5,
                playAnyDan6,
                playAnyDan7,
                playAnyDan8,
                playFontGroupDan2,
                playFontGroupDan3};
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
//                ResultHistoryNewActivity.intoThisActivity(Eleven5Activity.this, mLotteryType);
                TrendActivity.intoThisActivity(Eleven5Activity.this, mLotteryType, mPlayStyle, LATEST_OPEN_AWARD);
            } else if (msg.equals(getString(R.string.lottery_title_result_trend))) {
                TrendActivity.intoThisActivity(Eleven5Activity.this, mLotteryType, mPlayStyle, TREND_VIEW);
            } else {
                UIHelper.gotoActivity(Eleven5Activity.this, Eleven5DescriptionActivity.class, false);
            }
            customPopupWindow.dismissPopupWindow();
        };
        clickList.add(clickListener);
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        customPopupWindow = new CustomPopupWindow(this, strList, clickList);
    }

    private void showFragmentByPlayStyle() {
        switch (mPlayStyle) {
            case PLAY_11_5_ANY_2:
                playAny2.performClick();
                break;
            case PLAY_11_5_ANY_3:
                playAny3.performClick();
                break;
            case PLAY_11_5_ANY_4:
                playAny4.performClick();
                break;
            case PLAY_11_5_ANY_5:
                playAny5.performClick();
                break;
            case PLAY_11_5_ANY_6:
                playAny6.performClick();
                break;
            case PLAY_11_5_ANY_7:
                playAny7.performClick();
                break;
            case PLAY_11_5_ANY_8:
                playAny8.performClick();
                break;
            case PLAY_11_5_ANY_2_DAN:
                playAnyDan2.performClick();
                break;
            case PLAY_11_5_ANY_3_DAN:
                playAnyDan3.performClick();
                break;
            case PLAY_11_5_ANY_4_DAN:
                playAnyDan4.performClick();
                break;
            case PLAY_11_5_ANY_5_DAN:
                playAnyDan5.performClick();
                break;
            case PLAY_11_5_ANY_6_DAN:
                playAnyDan6.performClick();
                break;
            case PLAY_11_5_ANY_7_DAN:
                playAnyDan7.performClick();
                break;
            case PLAY_11_5_ANY_8_DAN:
                playAnyDan8.performClick();
                break;
            case PLAY_11_5_FRONT_1_DIRECT:
                playFontDirect1.performClick();
                break;
            case PLAY_11_5_FRONT_2_DIRECT:
                playFontDirect2.performClick();
                break;
            case PLAY_11_5_FRONT_2_GROUP:
                playFontGroup2.performClick();
                break;
            case PLAY_11_5_FRONT_2_GROUP_DAN:
                playFontGroupDan2.performClick();
                break;
            case PLAY_11_5_FRONT_3_DIRECT:
                playFontDirect3.performClick();
                break;
            case PLAY_11_5_FRONT_3_GROUP:
                playFontGroup3.performClick();
                break;
            case PLAY_11_5_FRONT_3_GROUP_DAN:
                playFontGroupDan3.performClick();
                break;
        }
    }

    private void initIntent() {
        mLotteryType = getIntent().getStringExtra(PARAM_LOTTERY_TYPE);
        mPlayStyle = getIntent().getIntExtra(PARAM_LOTTERY_PLAY_TYPE, FAST_THREE_SUM);
        mLotteryName = (String) SharedPreferenceUtil.get(mContext, mLotteryType, "");
    }

    public void onPlayStyleSelect(View v) {
        if (mIsAnimRunning) {
            return;
        }
        showOrHideSelectLayout();
        int id = v.getId();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        switch (id) {
            case R.id.playAny2:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_ANY_2);
                mPlayStyle = PLAY_11_5_ANY_2;
                title = mLotteryName + "-任选二";
                break;
            case R.id.playAny3:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_ANY_3);
                mPlayStyle = PLAY_11_5_ANY_3;
                title = mLotteryName + "-任选三";
                break;
            case R.id.playAny4:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_ANY_4);
                mPlayStyle = PLAY_11_5_ANY_4;
                title = mLotteryName + "-任选四";
                break;
            case R.id.playAny5:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_ANY_5);
                mPlayStyle = PLAY_11_5_ANY_5;
                title = mLotteryName + "-任选五";
                break;
            case R.id.playAny6:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_ANY_6);
                mPlayStyle = PLAY_11_5_ANY_6;
                title = mLotteryName + "-任选六";
                break;
            case R.id.playAny7:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_ANY_7);
                mPlayStyle = PLAY_11_5_ANY_7;
                title = mLotteryName + "-任选七";
                break;
            case R.id.playAny8:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_ANY_8);
                mPlayStyle = PLAY_11_5_ANY_8;
                title = mLotteryName + "-任选八";
                break;
            case R.id.playFontDirect1:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_FRONT_1_DIRECT);
                mPlayStyle = PLAY_11_5_FRONT_1_DIRECT;
                title = mLotteryName + "-前一直选";
                break;
            case R.id.playFontDirect2:
                transactionDirectFragment(transaction);
                mDirectFragment.setPlayStyle(PLAY_11_5_FRONT_2_DIRECT);
                mPlayStyle = PLAY_11_5_FRONT_2_DIRECT;
                title = mLotteryName + "-前二直选";
                break;
            case R.id.playFontDirect3:
                transactionDirectFragment(transaction);
                mDirectFragment.setPlayStyle(PLAY_11_5_FRONT_3_DIRECT);
                mPlayStyle = PLAY_11_5_FRONT_3_DIRECT;
                title = mLotteryName + "-前三直选";
                break;
            case R.id.playFontGroup2:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_FRONT_2_GROUP);
                mPlayStyle = PLAY_11_5_FRONT_2_GROUP;
                title = mLotteryName + "-前二组选";
                break;
            case R.id.playFontGroup3:
                transactionNormalFragment(transaction);
                mNormalFragment.setPlayStyle(PLAY_11_5_FRONT_3_GROUP);
                mPlayStyle = PLAY_11_5_FRONT_3_GROUP;
                title = mLotteryName + "-前三组选";
                break;
            case R.id.playAnyDan2:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_ANY_2_DAN);
                mPlayStyle = PLAY_11_5_ANY_2_DAN;
                title = mLotteryName + "-任选二-胆拖";
                break;
            case R.id.playAnyDan3:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_ANY_3_DAN);
                mPlayStyle = PLAY_11_5_ANY_3_DAN;
                title = mLotteryName + "-任选三-胆拖";
                break;
            case R.id.playAnyDan4:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_ANY_4_DAN);
                mPlayStyle = PLAY_11_5_ANY_4_DAN;
                title = mLotteryName + "-任选四-胆拖";
                break;
            case R.id.playAnyDan5:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_ANY_5_DAN);
                mPlayStyle = PLAY_11_5_ANY_5_DAN;
                title = mLotteryName + "-任选五-胆拖";
                break;
            case R.id.playAnyDan6:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_ANY_6_DAN);
                mPlayStyle = PLAY_11_5_ANY_6_DAN;
                title = mLotteryName + "-任选六-胆拖";
                break;
            case R.id.playAnyDan7:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_ANY_7_DAN);
                mPlayStyle = PLAY_11_5_ANY_7_DAN;
                title = mLotteryName + "-任选七-胆拖";
                break;
            case R.id.playAnyDan8:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_ANY_8_DAN);
                mPlayStyle = PLAY_11_5_ANY_8_DAN;
                title = mLotteryName + "-任选八-胆拖";
                break;
            case R.id.playFontGroupDan2:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_FRONT_2_GROUP_DAN);
                mPlayStyle = PLAY_11_5_FRONT_2_GROUP_DAN;
                title = mLotteryName + "-前二组选-胆拖";
                break;
            case R.id.playFontGroupDan3:
                transactionDanFragment(transaction);
                mDanFragment.setPlayStyle(PLAY_11_5_FRONT_3_GROUP_DAN);
                mPlayStyle = PLAY_11_5_FRONT_3_GROUP_DAN;
                title = mLotteryName + "-前三组选-胆拖";
                break;
        }
        mTitleTv.setText(title);
        transaction.commit();
    }

    private void transactionNormalFragment(FragmentTransaction transaction) {
        if (mNormalFragment == null) {
            mNormalFragment = Eleven5NormalFragment.newInstance(mLotteryType);
            transaction.add(R.id.fragment_container, mNormalFragment, NORMAL_FRAGMENT_TAG);
        }
        transaction.show(mNormalFragment);
    }

    private void transactionDanFragment(FragmentTransaction transaction) {
        if (mDanFragment == null) {
            mDanFragment = Eleven5DanFragment.newInstance(mLotteryType);
            transaction.add(R.id.fragment_container, mDanFragment, DAN_FRAGMENT_TAG);
        }
        transaction.show(mDanFragment);
    }

    private void transactionDirectFragment(FragmentTransaction transaction) {
        if (mDirectFragment == null) {
            mDirectFragment = Eleven5DirectFragment.newInstance(mLotteryType);
            transaction.add(R.id.fragment_container, mDirectFragment, DIRECT_FRAGMENT_TAG);
        }
        transaction.show(mDirectFragment);
    }

    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    @OnClick({R.id.title_back, R.id.always_ll_changeStyle, R.id.title_right_button, R.id.ll_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(Eleven5Activity.this);
                break;
            case R.id.always_ll_changeStyle:
                if (mIsAnimRunning) {
                    return;
                }
                showOrHideSelectLayout();
                break;
            case R.id.title_right_button:
                //弹出选项弹窗
                customPopupWindow.showAsDropDown(view);
                break;
            case R.id.ll_history:
                TrendActivity.intoThisActivity(mContext, mLotteryType, mPlayStyle, LATEST_OPEN_AWARD);
                break;
        }
    }

    private void showOrHideSelectLayout() {
        UIHelper.showWidget(mSelectStyleLayout, !mIsSelectStyleLayoutShow);
        if (!mIsSelectStyleLayoutShow) {
            int position = 0;
            switch (mPlayStyle) {
                case PLAY_11_5_ANY_2:
                    position = 0;
                    break;
                case PLAY_11_5_ANY_3:
                    position = 1;
                    break;
                case PLAY_11_5_ANY_4:
                    position = 2;
                    break;
                case PLAY_11_5_ANY_5:
                    position = 3;
                    break;
                case PLAY_11_5_ANY_6:
                    position = 4;
                    break;
                case PLAY_11_5_ANY_7:
                    position = 5;
                    break;
                case PLAY_11_5_ANY_8:
                    position = 6;
                    break;
                case PLAY_11_5_FRONT_1_DIRECT:
                    position = 7;
                    break;
                case PLAY_11_5_FRONT_2_DIRECT:
                    position = 8;
                    break;
                case PLAY_11_5_FRONT_3_DIRECT:
                    position = 9;
                    break;
                case PLAY_11_5_FRONT_2_GROUP:
                    position = 10;
                    break;
                case PLAY_11_5_FRONT_3_GROUP:
                    position = 11;
                    break;
                case PLAY_11_5_ANY_2_DAN:
                    position = 12;
                    break;
                case PLAY_11_5_ANY_3_DAN:
                    position = 13;
                    break;
                case PLAY_11_5_ANY_4_DAN:
                    position = 14;
                    break;
                case PLAY_11_5_ANY_5_DAN:
                    position = 15;
                    break;
                case PLAY_11_5_ANY_6_DAN:
                    position = 16;
                    break;
                case PLAY_11_5_ANY_7_DAN:
                    position = 17;
                    break;
                case PLAY_11_5_ANY_8_DAN:
                    position = 18;
                    break;
                case PLAY_11_5_FRONT_2_GROUP_DAN:
                    position = 19;
                    break;
                case PLAY_11_5_FRONT_3_GROUP_DAN:
                    position = 20;
                    break;
            }
            showSelectButton(position);
        }
        if (!mIsFistInit) {
            if (mIsSelectStyleLayoutShow) {
                if (mStyleLabelIv != null) {
                    mStyleLabelIv.startAnimation(mUp2DownAnimation);
                }
            } else {
                mStyleLabelIv.startAnimation(mDown2UpAnimation);
            }
        }
        mIsFistInit = false;
        mIsSelectStyleLayoutShow = !mIsSelectStyleLayoutShow;
    }

    private void showSelectButton(int idx) {
        LogF.d(TAG, "选择 " + idx + "  " + mSelectStyleBtns.length);
        for (int i = 0; i < mSelectStyleBtns.length; i++) {
            if (idx != i) {
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
        Intent i = new Intent(fromActivity, Eleven5Activity.class);
        i.putExtra(PARAM_LOTTERY_PLAY_TYPE, playStyle);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
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
    public void onRequestSequence(boolean isOk, Object obj) {
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
    public void onTotalCount(long count) {

    }

    @OnClick(R.id.eleven5_empty_view)
    public void onViewClicked() {
        showOrHideSelectLayout();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mIsAnimRunning = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mIsAnimRunning = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

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
