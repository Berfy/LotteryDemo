package cn.zcgames.lottery.home.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.response.ResultSequenceBeanNew;
import cn.zcgames.lottery.home.bean.TrendDataResponse;
import cn.zcgames.lottery.home.bean.TrendRefreshEvent;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.callback.ConfirmDialogCallback;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.fragment.AlwaysColorTrendFragment;
import cn.zcgames.lottery.home.presenter.TrendPresenter;
import cn.zcgames.lottery.home.view.fragment.TrendSettingDialogFragment;
import cn.zcgames.lottery.home.view.fragment.elevenfive.Eleven5TrendFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.FastThreeTrendFragment;
import cn.zcgames.lottery.home.view.fragment.ui.LottoTrendView;
import cn.zcgames.lottery.home.view.iview.ITrendActivity;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_PLAY_TYPE;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_TYPE;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_SOURCE_TYPE;
import static cn.zcgames.lottery.app.AppConstants.*;

/**
 * 各彩种走势图
 */
public class TrendActivity extends BaseActivity implements ITrendActivity, Animation.AnimationListener {

    private final String TAG = "各彩种走势图";
    @BindView(R.id.ib_title_back)
    ImageButton mIBBack;
    @BindView(R.id.ib_refresh_trend)
    ImageButton mIbRefresh;
    @BindView(R.id.ib_right_button)
    ImageButton mIBSetting;
    @BindView(R.id.tv_trend_title)
    TextView mTVTitle;
    @BindView(R.id.iv_trend_label)
    ImageView mIVLabel;
    @BindView(R.id.fast_three_select_view)
    LinearLayout fast3SelectLL;
    @BindView(R.id.eleven_five_select_view)
    LinearLayout eleven5SelectLL;
    @BindView(R.id.always_color_select_view)
    LinearLayout alwayColorSelectLL;
    //11选5玩法选项
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
    @BindView(R.id.trend_selectLayout)
    LinearLayout mLLSelectLayout;
    //快三玩法选项
    @BindView(R.id.fast3_tv_sum)
    TextView fast3TvSum;
    @BindView(R.id.fast3_tv_3same_single)
    TextView fast3Tv3sameSingle;
    @BindView(R.id.fast3_tv_2same_more)
    TextView fast3Tv2sameMore;
    @BindView(R.id.fast3_tv_3different)
    TextView fast3Tv3different;
    @BindView(R.id.fast3_tv_2different)
    TextView fast3Tv2different;
    //时时彩玩法选项
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
    @BindView(R.id.lottery_trend_view)
    LottoTrendView lottoTrendView;

    private TextView[] mSelectStyleBtns;
    private Animation mDown2UpAnimation, mUp2DownAnimation;
    private MyHandler mHandler;
    private static final int MESSAGE_UPDATE = 0;
    private static final int MESSAGE_NODATA = 1;
    private static final int MSG_UPDATE_COUNT_DOWN = 2;
    private FastThreeTrendFragment sumTrendFragment;
    private Eleven5TrendFragment eleven5TrendFragment;
    private AlwaysColorTrendFragment alwaysColorTrendFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private boolean mIsSelectStyleLayoutShow = true;//选择玩法layout是否隐藏

    private TrendPresenter mPresenter;
    private String mLotteryType;
    private int mPlayType;
    private boolean mIsNeedListSort = true;//是否显示排列控制
    private boolean mIsNeedLine = true;//是否显示折现控制
    private boolean mIsNeedYilou = true;//是否显示遗漏控制
    private boolean mIsNeedStatistic = true;//是否需要显示统计控制
    private String title;
    private boolean mIsAnimRunning;
    private boolean mIsFistInit = true;//是否第一次进入页面

    private RotateAnimation mRotateAnimation;//刷新动画

    private LotteryModel mLotteryModel;
    private long mCountDownTime = 60 * 10 * 1000;//默认倒计时时间

    private TrendResponseData mTrendResponseData;//总数据
    private TrendResponseData mFixResponseData = new TrendResponseData();//对应期次数据
    private String mLotteryName;
    private String sourceType;//进入来源
    private TrendTypeData typeData;//各类走势图数据

    /**
     * 进入此activity的入口
     *
     * @param fromActivity 来源
     * @param playStyle    玩法
     * @param sourceType   跳转来源类型
     */
    public static void intoThisActivity(Context fromActivity, String lotteryType, int playStyle, String sourceType) {
        Intent i = new Intent(fromActivity, TrendActivity.class);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
        i.putExtra(PARAM_LOTTERY_PLAY_TYPE, playStyle);
        i.putExtra(PARAM_SOURCE_TYPE, sourceType);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHelper.hideSystemTitleBar(this);
        EventBus.getDefault().register(this);
        initData();
        initView();
    }

    private void initData() {
        mLotteryType = getIntent().getStringExtra(PARAM_LOTTERY_TYPE);
        mPlayType = getIntent().getIntExtra(PARAM_LOTTERY_PLAY_TYPE, -1);
        sourceType = getIntent().getStringExtra(PARAM_SOURCE_TYPE);
        typeData = new TrendTypeData();
        mLotteryModel = new LotteryModel();
    }

    private void initView() {
        setContentView(R.layout.activity_trend);
        setButterKnife(this);
        mHandler = new MyHandler(this);
        mPresenter = new TrendPresenter(this, this);
        mDown2UpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_up_to_down);
        mUp2DownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_down_to_up);
        mDown2UpAnimation.setAnimationListener(this);
        mUp2DownAnimation.setAnimationListener(this);
        mLotteryName = (String) SharedPreferenceUtil.get(mContext, mLotteryType, "");
        mRotateAnimation = new RotateAnimation(0, 1800, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(5000);
        mRotateAnimation.setAnimationListener(this);
        UIHelper.showWaitingDialog(mContext, "加载中", true);
        getTrendData();
        showFragmentByPlayStyle();
    }

    private void getPeriod() {
        mLotteryModel.requestCurrentSequence(mLotteryType, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                if (isFinishing()) {
                    return;
                }
                if (msg instanceof ResultSequenceBeanNew.SequenceBean) {
                    ResultSequenceBeanNew.SequenceBean sequenceBean = (ResultSequenceBeanNew.SequenceBean) msg;
                    mCountDownTime = sequenceBean.getDraw_time_left();
                    if (null != mHandler) {
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler.sendEmptyMessage(MSG_UPDATE_COUNT_DOWN);
                    }
                }
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {

            }
        });
    }

    private void showFragmentByPlayStyle() {
        switch (mPlayType) {
            case FAST_THREE_SUM:
                fast3TvSum.performClick();
                break;
            case FAST_THREE_3_SAME_ONE:
                fast3Tv3sameSingle.performClick();
                break;
            case FAST_THREE_3_SAME_ALL:
                fast3Tv3sameSingle.performClick();
                break;
            case FAST_THREE_3_TO_ALL:
                fast3Tv3sameSingle.performClick();
                break;
            case FAST_THREE_3_DIFFERENT:
                fast3Tv3different.performClick();
                break;
            case FAST_THREE_2_SAME_ONE:
                fast3Tv2sameMore.performClick();
                break;
            case FAST_THREE_2_SAME_MORE:
                fast3Tv2sameMore.performClick();
                break;
            case FAST_THREE_2_DIFFERENT:
                fast3Tv2different.performClick();
                break;
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

    @OnClick({R.id.fast3_tv_sum, R.id.fast3_tv_2same_more, R.id.fast3_tv_3same_single,
            R.id.fast3_tv_3different, R.id.fast3_tv_2different, R.id.playAny2, R.id.playAny3,
            R.id.playAny4, R.id.playAny5, R.id.playAny6, R.id.playAny7, R.id.playAny8, R.id.playFontDirect1,
            R.id.playFontDirect2, R.id.playFontDirect3, R.id.playFontGroup2, R.id.playFontGroup3,
            R.id.playAnyDan2, R.id.playAnyDan3, R.id.playAnyDan4, R.id.playAnyDan5, R.id.playAnyDan6, R.id.playAnyDan7,
            R.id.playAnyDan8, R.id.playFontGroupDan2, R.id.playFontGroupDan3, R.id.always_tv_1direct, R.id.always_tv_2direct,
            R.id.always_tv_2group, R.id.always_tv_3direct, R.id.always_tv_3group3, R.id.always_tv_3group6,
            R.id.always_tv_5direct, R.id.always_tv_5all, R.id.always_tv_different, R.id.empty_view,
            R.id.ib_title_back, R.id.trend_play_style, R.id.ib_refresh_trend, R.id.ib_right_button, R.id.eleven5_empty_view, R.id.ac_empty_view})
    public void setPlayViewClick(View view) {
        switch (view.getId()) {
            case R.id.empty_view:
            case R.id.trend_play_style:
            case R.id.eleven5_empty_view:
            case R.id.ac_empty_view:
                showOrHideSelectLayout();
                break;
            case R.id.ib_title_back:
                goBack(TrendActivity.this);
                break;
            case R.id.ib_right_button:
                TrendSettingDialogFragment trendSettingDialogFragment
                        = TrendSettingDialogFragment.newInstance(mLotteryType, mIsNeedListSort, mIsNeedLine,
                        mIsNeedYilou, mIsNeedStatistic);
                trendSettingDialogFragment.show(getSupportFragmentManager(), "setting");
                break;
            case R.id.ib_refresh_trend:
                if (mIsAnimRunning)
                    return;
                getTrendData();
                break;
            //快三
            case R.id.fast3_tv_sum:
                mPlayType = FAST_THREE_SUM;
                title = mLotteryName + "-和值";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.fast3_tv_3same_single:
                mPlayType = FAST_THREE_3_SAME_ONE;
                title = mLotteryName + "-三同号";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            /*case R.id.fast3_tv_3same_all:
                mPlayType = FAST_THREE_3_SAME_ALL;
                title = mLotteryName + "-三同号通选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.fast3_tv_3to_all:
                mPlayType = FAST_THREE_3_TO_ALL;
                title = mLotteryName + "-三连号通选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;*/
            case R.id.fast3_tv_2same_more:
                mPlayType = FAST_THREE_2_SAME_MORE;
                title = mLotteryName + "-二同号";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            /*case R.id.fast3_tv_2same_more:
                mPlayType = FAST_THREE_2_SAME_MORE;
                title = mLotteryName + "-二同号复选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;*/
            case R.id.fast3_tv_3different:
                mPlayType = FAST_THREE_3_DIFFERENT;
                title = mLotteryName + "-三不同号";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.fast3_tv_2different:
                mPlayType = FAST_THREE_2_DIFFERENT;
                title = mLotteryName + "-二不同号";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            //11选5
            case R.id.playAny2:
                mPlayType = PLAY_11_5_ANY_2;
                title = mLotteryName + "-任选二";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAny3:
                mPlayType = PLAY_11_5_ANY_3;
                title = mLotteryName + "-任选三";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAny4:
                mPlayType = PLAY_11_5_ANY_4;
                title = mLotteryName + "-任选四";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAny5:
                mPlayType = PLAY_11_5_ANY_5;
                title = mLotteryName + "-任选五";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAny6:
                mPlayType = PLAY_11_5_ANY_6;
                title = mLotteryName + "-任选六";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAny7:
                mPlayType = PLAY_11_5_ANY_7;
                title = mLotteryName + "-任选七";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAny8:
                mPlayType = PLAY_11_5_ANY_8;
                title = mLotteryName + "-任选八";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playFontDirect1:
                mPlayType = PLAY_11_5_FRONT_1_DIRECT;
                title = mLotteryName + "-前一直选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playFontDirect2:
                mPlayType = PLAY_11_5_FRONT_2_DIRECT;
                title = mLotteryName + "-前二直选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playFontDirect3:
                mPlayType = PLAY_11_5_FRONT_3_DIRECT;
                title = mLotteryName + "-前三直选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playFontGroup2:
                mPlayType = PLAY_11_5_FRONT_2_GROUP;
                title = mLotteryName + "-前二组选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playFontGroup3:
                mPlayType = PLAY_11_5_FRONT_3_GROUP;
                title = mLotteryName + "-前三组选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAnyDan2:
                mPlayType = PLAY_11_5_ANY_2_DAN;
                title = mLotteryName + "-任选二-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAnyDan3:
                mPlayType = PLAY_11_5_ANY_3_DAN;
                title = mLotteryName + "-任选三-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAnyDan4:
                mPlayType = PLAY_11_5_ANY_4_DAN;
                title = mLotteryName + "-任选四-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAnyDan5:
                mPlayType = PLAY_11_5_ANY_5_DAN;
                title = mLotteryName + "-任选五-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAnyDan6:
                mPlayType = PLAY_11_5_ANY_6_DAN;
                title = mLotteryName + "-任选六-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAnyDan7:
                mPlayType = PLAY_11_5_ANY_7_DAN;
                title = mLotteryName + "-任选七-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playAnyDan8:
                mPlayType = PLAY_11_5_ANY_8_DAN;
                title = mLotteryName + "-任选八-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playFontGroupDan2:
                mPlayType = PLAY_11_5_FRONT_2_GROUP_DAN;
                title = mLotteryName + "-前二组选-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.playFontGroupDan3:
                mPlayType = PLAY_11_5_FRONT_3_GROUP_DAN;
                title = mLotteryName + "-前三组选-胆拖";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            //时时彩
            case R.id.always_tv_1direct:
                mPlayType = ALWAYS_COLOR_1_DIRECT;
                title = mLotteryName + "-一星直选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.always_tv_2direct:
                mPlayType = ALWAYS_COLOR_2_DIRECT;
                title = mLotteryName + "-二星直选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.always_tv_2group:
                mPlayType = ALWAYS_COLOR_2_GROUP;
                title = mLotteryName + "-二星组选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;

            case R.id.always_tv_3direct:
                mPlayType = ALWAYS_COLOR_3_DIRECT;
                title = mLotteryName + "-三星直选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.always_tv_3group3:
                mPlayType = ALWAYS_COLOR_3_GROUP_3;
                title = mLotteryName + "-三星组三";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.always_tv_3group6:
                mPlayType = ALWAYS_COLOR_3_GROUP_6;
                title = mLotteryName + "-三星组六";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;

            case R.id.always_tv_5direct:
                mPlayType = ALWAYS_COLOR_5_DIRECT;
                title = mLotteryName + "-五星直选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.always_tv_5all:
                mPlayType = ALWAYS_COLOR_5_ALL;
                title = mLotteryName + "-五星通选";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
            case R.id.always_tv_different:
                mPlayType = ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
                title = mLotteryName + "-大小单双";
                showOrHideSelectLayout();
                //更新视图
                updateTrendData(mFixResponseData);
                break;
        }
        if (!TextUtils.isEmpty(title)) mTVTitle.setText(title);
    }

    //设置玩法选择布局展示
    private void showOrHideSelectLayout() {
        if (mIsAnimRunning) {
            return;
        }
        UIHelper.showWidget(mLLSelectLayout, !mIsSelectStyleLayoutShow);
        if (!mIsSelectStyleLayoutShow) {
            switch (mLotteryType) {
                case LOTTERY_TYPE_FAST_3://快三
                case LOTTERY_TYPE_FAST_3_JS:
                case LOTTERY_TYPE_FAST_3_HB:
                case LOTTERY_TYPE_FAST_3_EASY:
                case LOTTERY_TYPE_FAST_3_NEW:
                    mSelectStyleBtns = new TextView[]{fast3TvSum, fast3Tv3sameSingle,
                            fast3Tv3sameSingle, fast3Tv3sameSingle, fast3Tv2sameMore,
                            fast3Tv2sameMore, fast3Tv3different, fast3Tv2different};
                    UIHelper.showWidget(fast3SelectLL, true);
                    UIHelper.showWidget(eleven5SelectLL, false);
                    UIHelper.showWidget(alwayColorSelectLL, false);
                    if (mPlayType == FAST_THREE_SUM) {
                        showSelectButton(0);
                    } else if (mPlayType == FAST_THREE_2_DIFFERENT) {
                        showSelectButton(7);
                    } else if (mPlayType == FAST_THREE_3_DIFFERENT) {
                        showSelectButton(6);
                    } else if (mPlayType == FAST_THREE_2_SAME_ONE) {
                        showSelectButton(4);
                    } else if (mPlayType == FAST_THREE_2_SAME_MORE) {
                        showSelectButton(5);
                    } else if (mPlayType == FAST_THREE_3_TO_ALL) {
                        showSelectButton(3);
                    } else if (mPlayType == FAST_THREE_3_SAME_ONE) {
                        showSelectButton(1);
                    } else if (mPlayType == FAST_THREE_3_SAME_ALL) {
                        showSelectButton(2);
                    }
                    break;
                case LOTTERY_TYPE_11_5://11选5
                case LOTTERY_TYPE_11_5_LUCKY:
                case LOTTERY_TYPE_11_5_OLD:
                case LOTTERY_TYPE_11_5_YILE:
                case LOTTERY_TYPE_11_5_YUE:
                    mSelectStyleBtns = new TextView[]{playAny2, playAny3, playAny4, playAny5, playAny6,
                            playAny7, playAny8, playFontDirect1, playFontDirect2, playFontDirect3,
                            playFontGroup2, playFontGroup3, playAnyDan2, playAnyDan3, playAnyDan4,
                            playAnyDan5, playAnyDan6, playAnyDan7, playAnyDan8, playFontGroupDan2,
                            playFontGroupDan3};
                    UIHelper.showWidget(fast3SelectLL, false);
                    UIHelper.showWidget(eleven5SelectLL, true);
                    UIHelper.showWidget(alwayColorSelectLL, false);
                    switch (mPlayType) {
                        case PLAY_11_5_ANY_2://任选2
                            showSelectButton(0);
                            break;

                        case PLAY_11_5_ANY_3://任选3
                            showSelectButton(1);
                            break;

                        case PLAY_11_5_ANY_4://任选4
                            showSelectButton(2);
                            break;

                        case PLAY_11_5_ANY_5://任选5
                            showSelectButton(3);
                            break;

                        case PLAY_11_5_ANY_6://任选6
                            showSelectButton(4);
                            break;

                        case PLAY_11_5_ANY_7://任选7
                            showSelectButton(5);
                            break;

                        case PLAY_11_5_ANY_8://任选8
                            showSelectButton(6);
                            break;

                        case PLAY_11_5_FRONT_1_DIRECT://前一直选
                            showSelectButton(7);
                            break;

                        case PLAY_11_5_FRONT_2_DIRECT://前二直选
                            showSelectButton(8);
                            break;

                        case PLAY_11_5_FRONT_3_DIRECT://前三直选
                            showSelectButton(9);
                            break;

                        case PLAY_11_5_FRONT_2_GROUP://前二组选
                            showSelectButton(10);
                            break;

                        case PLAY_11_5_FRONT_3_GROUP://前三组选
                            showSelectButton(11);
                            break;

                        case PLAY_11_5_ANY_2_DAN://任选2胆拖
                            showSelectButton(12);
                            break;

                        case PLAY_11_5_ANY_3_DAN://任选3胆拖:
                            showSelectButton(13);
                            break;
                        case PLAY_11_5_ANY_4_DAN://任选4胆拖:
                            showSelectButton(14);
                            break;

                        case PLAY_11_5_ANY_5_DAN://任选5胆拖:
                            showSelectButton(15);
                            break;
                        case PLAY_11_5_ANY_6_DAN://任选6胆拖:
                            showSelectButton(16);
                            break;
                        case PLAY_11_5_ANY_7_DAN://任选7胆拖:
                            showSelectButton(17);
                            break;
                        case PLAY_11_5_ANY_8_DAN://任选8胆拖:
                            showSelectButton(18);
                            break;
                        case PLAY_11_5_FRONT_2_GROUP_DAN://前二组选胆拖:
                            showSelectButton(19);
                            break;
                        case PLAY_11_5_FRONT_3_GROUP_DAN://前三组选胆拖:
                            showSelectButton(20);
                            break;
                    }
                    break;
                case LOTTERY_TYPE_ALWAYS_COLOR:
                case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                    UIHelper.showWidget(fast3SelectLL, false);
                    UIHelper.showWidget(eleven5SelectLL, false);
                    UIHelper.showWidget(alwayColorSelectLL, true);
                    mSelectStyleBtns = new TextView[]{alwaysTv1Direct, alwaysTv2Direct, alwaysTv2Group,
                            alwaysTv3Direct, alwaysTv3Group3, alwaysTv3Group6, alwaysTv5Direct, alwaysTv5All,
                            alwaysTvBigSingle};
                    switch (mPlayType) {
                        case ALWAYS_COLOR_1_DIRECT:
                            showSelectButton(0);
                            break;
                        case ALWAYS_COLOR_2_DIRECT:
                            showSelectButton(1);
                            break;
                        case ALWAYS_COLOR_2_GROUP:
                            showSelectButton(2);
                            break;
                        case ALWAYS_COLOR_3_DIRECT:
                            showSelectButton(3);
                            break;
                        case ALWAYS_COLOR_3_GROUP_3:
                            showSelectButton(4);
                            break;
                        case ALWAYS_COLOR_3_GROUP_6:
                            showSelectButton(5);
                            break;
                        case ALWAYS_COLOR_5_DIRECT:
                            showSelectButton(6);
                            break;
                        case ALWAYS_COLOR_5_ALL:
                            showSelectButton(7);
                            break;
                        case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                            showSelectButton(8);
                            break;
                    }
                    break;
            }
        }
        if (!mIsFistInit) {
            if (mIsSelectStyleLayoutShow) {
                if (mIVLabel != null) {
                    mIVLabel.startAnimation(mUp2DownAnimation);
                }
            } else {
                mIVLabel.startAnimation(mDown2UpAnimation);
            }
        }
        mIsFistInit = false;
        mIsSelectStyleLayoutShow = !mIsSelectStyleLayoutShow;
    }

    private void showSelectButton(int idx) {
        for (int i = 0; i < mSelectStyleBtns.length; i++) {
            if (idx != i) {
                mSelectStyleBtns[i].setSelected(false);
                mSelectStyleBtns[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_btn_round_sum_fragment_select_style));
                mSelectStyleBtns[i].setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_tab_text));
            } else {
                mSelectStyleBtns[i].setSelected(true);
                mSelectStyleBtns[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_bg_btn_round_normal));
                mSelectStyleBtns[i].setTextColor(StaticResourceUtils.getColorResourceById(R.color.white_normal));
            }
        }
    }

    //获取数据源
    private void getTrendData() {
        mIbRefresh.startAnimation(mRotateAnimation);
        getPeriod();
        mPresenter.getTrendData(mLotteryType);
    }

    //更新数据显示
    private void updateTrendData(TrendResponseData trendResponseData) {
        if (null == trendResponseData || null == trendResponseData.getData())
            return;
        switch (mLotteryType) {
            case LOTTERY_TYPE_FAST_3://快三
            case LOTTERY_TYPE_FAST_3_JS:
            case LOTTERY_TYPE_FAST_3_HB:
            case LOTTERY_TYPE_FAST_3_EASY:
            case LOTTERY_TYPE_FAST_3_NEW:
                switch (mPlayType) {
                    case FAST_THREE_SUM:
                    case FAST_THREE_3_SAME_ONE:
                    case FAST_THREE_3_SAME_ALL:
                    case FAST_THREE_3_TO_ALL:
                    case FAST_THREE_3_DIFFERENT:
                    case FAST_THREE_2_SAME_ONE:
                    case FAST_THREE_2_SAME_MORE:
                    case FAST_THREE_2_DIFFERENT:
                        mPresenter.getFastThreeData(mLotteryType, trendResponseData.getData());
                }
                break;
            case LOTTERY_TYPE_11_5://11选5
            case LOTTERY_TYPE_11_5_LUCKY:
            case LOTTERY_TYPE_11_5_OLD:
            case LOTTERY_TYPE_11_5_YILE:
            case LOTTERY_TYPE_11_5_YUE:
                switch (mPlayType) {
                    case PLAY_11_5_ANY_2://任选2
                    case PLAY_11_5_ANY_3://任选3
                    case PLAY_11_5_ANY_4://任选4
                    case PLAY_11_5_ANY_5://任选5
                    case PLAY_11_5_ANY_6://任选6
                    case PLAY_11_5_ANY_7://任选7
                    case PLAY_11_5_ANY_8://任选8
                    case PLAY_11_5_ANY_2_DAN://任选2胆拖
                    case PLAY_11_5_ANY_3_DAN://任选3胆拖
                    case PLAY_11_5_ANY_4_DAN://任选4胆拖
                    case PLAY_11_5_ANY_5_DAN://任选5胆拖
                    case PLAY_11_5_ANY_6_DAN://任选6胆拖
                    case PLAY_11_5_ANY_7_DAN://任选7胆拖
                    case PLAY_11_5_ANY_8_DAN://任选8胆拖
                    case PLAY_11_5_FRONT_1_DIRECT://前一直选
                    case PLAY_11_5_FRONT_2_GROUP://前二组选
                    case PLAY_11_5_FRONT_3_GROUP://前三组选
                    case PLAY_11_5_FRONT_2_DIRECT://前二直选
                    case PLAY_11_5_FRONT_3_DIRECT://前三直选
                    case PLAY_11_5_FRONT_2_GROUP_DAN://前二组选胆拖
                    case PLAY_11_5_FRONT_3_GROUP_DAN://前三组选胆拖
                        mPresenter.setEleven5TrendData(mLotteryType, mPlayType, trendResponseData.getData());
                        break;
                }
                break;
            case LOTTERY_TYPE_ALWAYS_COLOR://时时彩
            case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                mPresenter.getACTrendData(mLotteryType, mPlayType, trendResponseData.getData());
                switch (mPlayType) {
                    case ALWAYS_COLOR_1_DIRECT:
                        break;
                    case ALWAYS_COLOR_2_DIRECT:
                        break;
                    case ALWAYS_COLOR_2_GROUP:
                        break;
                    case ALWAYS_COLOR_3_DIRECT:
                        break;
                    case ALWAYS_COLOR_3_GROUP_3:
                        break;
                    case ALWAYS_COLOR_3_GROUP_6:
                        break;
                    case ALWAYS_COLOR_5_DIRECT:
                        break;
                    case ALWAYS_COLOR_5_ALL:
                        break;
                    case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                        break;
                }
                break;

        }
    }

    private void showData() {
        switch (mLotteryType) {
            case LOTTERY_TYPE_FAST_3://快三
            case LOTTERY_TYPE_FAST_3_JS:
            case LOTTERY_TYPE_FAST_3_HB:
            case LOTTERY_TYPE_FAST_3_EASY:
            case LOTTERY_TYPE_FAST_3_NEW:
                LotteryTrendData f3Data = new LotteryTrendData(mLotteryType, mPlayType, typeData, sourceType);
                f3Data.setData(mFixResponseData.getData());
                LogF.d(TAG, "快三解析后" + GsonUtil.getInstance().toJson(f3Data));
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                if (sumTrendFragment != null) {
                    EventBus.getDefault().post(f3Data);
                } else {
                    sumTrendFragment = FastThreeTrendFragment.newInstance(f3Data, mFixResponseData.getData(), sourceType);
                    transaction.add(R.id.fl_fragment_container, sumTrendFragment);
                    transaction.commit();
                }
                break;
            case LOTTERY_TYPE_11_5://11选5
            case LOTTERY_TYPE_11_5_LUCKY:
            case LOTTERY_TYPE_11_5_OLD:
            case LOTTERY_TYPE_11_5_YILE:
            case LOTTERY_TYPE_11_5_YUE:
                LotteryTrendData eleven5Data = new LotteryTrendData(mLotteryType, mPlayType, typeData, sourceType);
                eleven5Data.setData(mFixResponseData.getData());
                LogF.d(TAG, "11选5解析后" + GsonUtil.getInstance().toJson(eleven5Data));
                if (eleven5TrendFragment != null) {
                    eleven5TrendFragment.initTrendData(eleven5Data);
                } else {
                    eleven5TrendFragment = Eleven5TrendFragment.newInstance(eleven5Data);
                    setTrendFragment(eleven5TrendFragment);
                }
                break;
            case LOTTERY_TYPE_ALWAYS_COLOR://时时彩
            case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                LotteryTrendData acData = new LotteryTrendData(mLotteryType, mPlayType, typeData, sourceType);
                acData.setData(mFixResponseData.getData());
                LogF.d(TAG, "时时彩解析后" + GsonUtil.getInstance().toJson(acData));
                if (alwaysColorTrendFragment != null) {
                    alwaysColorTrendFragment.initTrendData(acData);
                } else {
                    alwaysColorTrendFragment = AlwaysColorTrendFragment.newInstance(acData);
                    setTrendFragment(alwaysColorTrendFragment);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendRefreshEvent trendRefreshEvent) {
        if (null == trendRefreshEvent) {
            return;
        }
        if (trendRefreshEvent.isNeedRefresh())
            getTrendData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingChangeEvent trendSettingChangeEvent) {
        if (null == trendSettingChangeEvent) {
            LogF.d(TAG, "设置修改 null == trendSettingChangeEvent");
            return;
        }
        LogF.d(TAG, "设置修改" + GsonUtil.getInstance().toJson(trendSettingChangeEvent));
        if (trendSettingChangeEvent.isNeedRefresh()) {
            fixData();
        }
    }

    private void fixData() {
        LogF.d(TAG, "设置修改  fixData");
        if (null == mTrendResponseData) {//没有原始数据 先获取
            LogF.d(TAG, "设置修改  null == mTrendResponseData");
            getTrendData();
            return;
        }
        TrendSettingBean trendSettingBean = LotteryUtils.getTrendSettingCache(mLotteryType);
        int size = 30;
        if (null != trendSettingBean) {
            switch (trendSettingBean.getPeriodPos()) {
                case 0:
                    size = 30;
                    break;
                case 1:
                    size = 50;
                    break;
                case 2:
                    size = 100;
                    break;
                case 3:
                    size = 200;
                    break;
            }
        }
        //处理期次所需数据
        List<TrendResponseData.TrendData> datas = mTrendResponseData.getData();
        LogF.d(TAG, "老数据" + GsonUtil.getInstance().toJson(datas));
        int oldSize = datas.size();
        LogF.d(TAG, "设置修改  新size=" + size + "  oldSize=" + oldSize);
        if (size >= oldSize) {//源数据不足直接用
            mFixResponseData.setData(mTrendResponseData.getData());
        } else {
            List<TrendResponseData.TrendData> newDatas = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                newDatas.add(datas.get(oldSize - size + i));
            }
            mFixResponseData.setData(newDatas);
            LogF.d(TAG, "新数据" + GsonUtil.getInstance().toJson(mFixResponseData));
        }
        updateTrendData(mFixResponseData);
    }

    //设置弹框布局显示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingUIChangeEvent trendSettingUIChangeEvent) {
        if (null == trendSettingUIChangeEvent) {
            return;
        }
        mIsNeedListSort = trendSettingUIChangeEvent.isNeedShowListAsc();
        mIsNeedLine = trendSettingUIChangeEvent.isNeedShowShowLine();
        mIsNeedYilou = trendSettingUIChangeEvent.isNeedShowShowYilou();
        mIsNeedStatistic = trendSettingUIChangeEvent.isNeedShowShowStatistic();
    }

    //设置走势图相关fragment布局
    private void setTrendFragment(Fragment fragment) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.fl_fragment_container, fragment);
        transaction.commit();
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

    @Override
    public void requestResult(boolean isOk, Object object) {
        if (null == mHandler) {
            return;
        }
        if (null != mRotateAnimation) {
            mRotateAnimation.cancel();
        }
        if (isOk) {
            //数据处理
            Message message = Message.obtain();
            message.what = MESSAGE_UPDATE;
            message.obj = object;
            mHandler.sendMessage(message);
        } else {
            Message message = Message.obtain();
            message.what = MESSAGE_NODATA;
            message.obj = object;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void resultData(TrendTypeData trendTypeData) {
        if (isFinishing()) return;
        UIHelper.hideWaitingDialog();
        typeData = trendTypeData;
        showData();
    }

    @SuppressLint("HandlerLeak")
    public class MyHandler extends Handler {

        private WeakReference<TrendActivity> reference;

        private MyHandler(TrendActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == reference.get()) {
                return;
            }
            switch (msg.what) {
                case MESSAGE_UPDATE:
                    //数据处理
                    if (msg.obj instanceof TrendDataResponse) {
                        TrendDataResponse trendDataResponse = (TrendDataResponse) msg.obj;
                        mTrendResponseData = trendDataResponse.getPayLoad().getTrend();
                        Collections.reverse(mTrendResponseData.getData());
                        reference.get().fixData();
                    } else if (msg.obj instanceof TrendResponseData) {
                        mTrendResponseData = (TrendResponseData) msg.obj;
                        Collections.reverse(mTrendResponseData.getData());
                        reference.get().fixData();
                    } else {
                        UIHelper.hideWaitingDialog();
                    }
                    break;
                case MESSAGE_NODATA://获取失败
                    //相应操作
                    ToastUtil.getInstances().showShort(msg.obj.toString());
                    UIHelper.hideWaitingDialog();
                    finish();
                    break;
                case MSG_UPDATE_COUNT_DOWN:
                    if (reference.get().isFinishing()) {
                        return;
                    }
                    reference.get().mCountDownTime--;
                    if (reference.get().mCountDownTime >= 0) {
                        sendEmptyMessageDelayed(MSG_UPDATE_COUNT_DOWN, 1000);
                    } else {
                        if (ActivityManager.getInstance().currentActivity() != reference.get()) {
                            Log.d(TAG, "不是当前类 不显示弹框");
                            return;
                        }
                        Log.d(TAG, "当前类 显示弹框");
                        getTrendData();
                        UIHelper.showTipDialog(reference.get(), getString(R.string.tip_period_timeout), new ConfirmDialogCallback() {
                            @Override
                            public void onConfirmResult(boolean isOk) {
                            }
                        });
                    }
                    break;
            }
        }

    }


    @Override
    public void showTipDialog(String msgStr) {

    }

    @Override
    public void hideTipDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != mHandler)
            mHandler.removeCallbacksAndMessages(null);
    }
}
