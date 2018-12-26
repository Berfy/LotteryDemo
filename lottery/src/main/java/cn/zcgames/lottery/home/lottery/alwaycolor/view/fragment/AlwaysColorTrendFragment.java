package cn.zcgames.lottery.home.lottery.alwaycolor.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.DisplayUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.view.fragment.fastthree.OpenAwardFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.TrendFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;

import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_GROUP;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_3;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_6;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_ALL;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
import static cn.zcgames.lottery.app.AppConstants.KEY_TREND_TYPE_DATA;
import static cn.zcgames.lottery.app.AppConstants.LATEST_OPEN_AWARD;
import static cn.zcgames.lottery.app.AppConstants.TREND_VIEW;

/**
 * 时时彩走势图
 *
 * @author NorthStar
 * @date 2018/10/12 18:45
 */
public class AlwaysColorTrendFragment extends BaseFragment {

    public static final String TAG = "时时彩走势图";
    @BindView(R.id.tv_title1)
    TextView mTvTitle1;
    @BindView(R.id.v_line1)
    View mVLine1;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.v_line2)
    View mVLine2;
    @BindView(R.id.tv_title3)
    TextView mTvTitle3;
    @BindView(R.id.v_line3)
    View mVLine3;
    @BindView(R.id.tv_title4)
    TextView mTvTitle4;
    @BindView(R.id.v_line4)
    View mVLine4;
    @BindView(R.id.tv_title5)
    TextView mTvTitle5;
    @BindView(R.id.v_line5)
    View mVLine5;
    @BindView(R.id.tv_title6)
    TextView mTvTitle6;
    @BindView(R.id.v_line6)
    View mVLine6;
    @BindView(R.id.ll_title1)
    LinearLayout mLlTitle1;
    @BindView(R.id.ll_title2)
    LinearLayout mLlTitle2;
    @BindView(R.id.ll_title3)
    LinearLayout mLlTitle3;
    @BindView(R.id.ll_title4)
    LinearLayout mLlTitle4;
    @BindView(R.id.ll_title5)
    LinearLayout mLlTitle5;
    @BindView(R.id.ll_title6)
    LinearLayout mLlTitle6;

    private final String FRAGMENT_TAG_OPEN = "open";//开奖 列表
    private final String FRAGMENT_TAG_TREND_GE = "trend_ge";//走势图
    private final String FRAGMENT_TAG_TREND_SHI = "trend_shi";//走势图
    private final String FRAGMENT_TAG_TREND_BAI = "trend_bai";//走势图
    private final String FRAGMENT_TAG_TREND_QIAN = "trend_qian";//走势图
    private final String FRAGMENT_TAG_TREND_WAN = "trend_wan";//走势图
    private final String FRAGMENT_TAG_TREND_ZF = "trend_zf";//个位振幅 numPos -1
    private final String FRAGMENT_TAG_TREND_KD_2GROUP = "trend_kd_2group";//跨度 二星组选 numPos 0
    private final String FRAGMENT_TAG_TREND_KD_3GROUP3 = "trend_kd_3group3";//跨度 三星组三六 numPos 0
    private final String FRAGMENT_TAG_TREND_KD_3GROUP6 = "trend_kd_3group6";//跨度 三星组三六 numPos 0
    private final String FRAGMENT_TAG_BASIC_2GROUP = "basic_trend_2group";//2星组选 走势 numPos -2
    private final String FRAGMENT_TAG_BASIC_3GROUP3 = "basic_trend_3group3";//三星组三 走势 numPos -2
    private final String FRAGMENT_TAG_BASIC_3GROUP6 = "basic_trend_3group6";//三星组六 走势 numPos -2
    private final String FRAGMENT_TAG_FORM = "form";//形态走势 大小单双
    private Unbinder mUnbinder;

    private OpenAwardFragment mOpenAwardFragment;
    private TrendFragment mTrendGeFragment;//个位走势
    private TrendFragment mTrendZFFragment;//个位振幅 numPos -1
    private TrendFragment mTrendKDFragment2Group;//二星组选 跨度 numPos 0
    private TrendFragment mTrendKDFragment2GroupBasic;//二星组选走势numPos -2
    private TrendFragment mTrendKDFragment3Group3;//三星组三 跨度 numPos 0
    private TrendFragment mTrendKDFragment3Group6;//三星组六 跨度 numPos 0
    private TrendFragment mTrendKDFragment3Group3Basic;//三星组三走势 numPos -2
    private TrendFragment mTrendKDFragment3Group6Basic;//三星组六走势 numPos -2
    private TrendFragment mTrendSHIFragment;//十位走势
    private TrendFragment mTrendBAIFragment;//百位走势
    private TrendFragment mTrendQIANFragment;//千位走势
    private TrendFragment mTrendWANFragment;//万位走势
    private AlwaysColorFormTrendFragment mTrendFormFragment;
    private String[] mFragmentTags = {FRAGMENT_TAG_OPEN, FRAGMENT_TAG_TREND_GE, FRAGMENT_TAG_TREND_SHI, FRAGMENT_TAG_TREND_BAI,
            FRAGMENT_TAG_TREND_QIAN, FRAGMENT_TAG_TREND_WAN, FRAGMENT_TAG_TREND_ZF, FRAGMENT_TAG_TREND_KD_2GROUP,
            FRAGMENT_TAG_TREND_KD_3GROUP3, FRAGMENT_TAG_TREND_KD_3GROUP6, FRAGMENT_TAG_BASIC_2GROUP, FRAGMENT_TAG_BASIC_3GROUP3,
            FRAGMENT_TAG_BASIC_3GROUP6, FRAGMENT_TAG_FORM};
    private FragmentManager mFragmentManager;
    private String mLotteryType;
    private int mPlayType;
    private TrendTypeData mTypeData;
    private int mPosition;
    private String mFromType;

    public static AlwaysColorTrendFragment newInstance(LotteryTrendData data) {
        AlwaysColorTrendFragment fragment = new AlwaysColorTrendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TREND_TYPE_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View trendView = inflater.inflate(R.layout.fragment_sum_trend, container, false);
        mUnbinder = ButterKnife.bind(this, trendView);
        getType();
        initView();
        initListener();
        return trendView;
    }

    public void initView() {
        mFragmentManager = getChildFragmentManager();
        updateTitle();
        //设置展示的界面
        if (TextUtils.equals(TREND_VIEW, mFromType)) {
            mPosition = 1;
        } else if (TextUtils.equals(LATEST_OPEN_AWARD, mFromType)) {
            mPosition = 0;
        }

        showFragment(mPosition);
    }

    //获取彩种及玩法
    public void getType() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            LotteryTrendData data = (LotteryTrendData) bundle.getSerializable(KEY_TREND_TYPE_DATA);
            if (data != null) {
                mLotteryType = data.getLotteryType();
                mPlayType = data.getPlayType();
                mTypeData = data.getTypeData();
                mFromType = data.getSourceType();
                LogF.d(TAG, "时时彩--彩种类型==>" + mLotteryType + " ,玩法类型==>" + mPlayType
                        + "来源类型==>" + mFromType);
            }
        }
    }

    //更新数据和视图
    public void initTrendData(LotteryTrendData data) {
        mLotteryType = data.getLotteryType();
        mPlayType = data.getPlayType();
        mTypeData = data.getTypeData();
        LogF.d(TAG, "时时彩切换玩法 彩种类型==>" + mLotteryType + " ,玩法类型==>" + mPlayType);
        EventBus.getDefault().post(data);//传输最新走势数据
        updateTitle();
        showFragment(mPosition);
    }

    public void initListener() {
        mLlTitle1.setOnClickListener(v -> {
            showFragment(0);
        });

        mLlTitle2.setOnClickListener(v -> {
            showFragment(1);
        });

        mLlTitle3.setOnClickListener(v -> {
            showFragment(2);
        });

        mLlTitle4.setOnClickListener(v -> {
            showFragment(3);
        });

        mLlTitle5.setOnClickListener(v -> {
            showFragment(4);
        });

        mLlTitle6.setOnClickListener(v -> {
            showFragment(5);
        });
    }

    private void updateTitle() {
        mTvTitle1.setText("开奖");
        mLlTitle3.setVisibility(View.GONE);
        mLlTitle4.setVisibility(View.GONE);
        mLlTitle5.setVisibility(View.GONE);
        mLlTitle6.setVisibility(View.GONE);
        switch (mPlayType) {
            case ALWAYS_COLOR_1_DIRECT:
                mLlTitle3.setVisibility(View.VISIBLE);
                mTvTitle2.setText("个位走势");
                mTvTitle3.setText("个位振幅");
                if (mPosition > 2) {
                    mPosition = 1;//超出页面默认个位走势
                }
                break;
            case ALWAYS_COLOR_2_DIRECT:
                mLlTitle3.setVisibility(View.VISIBLE);
                mTvTitle2.setText("十位走势");
                mTvTitle3.setText("个位走势");
                if (mPosition > 2) {
                    mPosition = 1;//超出页面默认十位走势
                }
                break;
            case ALWAYS_COLOR_2_GROUP:
                mLlTitle3.setVisibility(View.VISIBLE);
                mTvTitle2.setText("走势");
                mTvTitle3.setText("跨度");
                if (mPosition > 2) {
                    mPosition = 1;//超出页面默认走势
                }
                break;
            case ALWAYS_COLOR_3_DIRECT:
                mLlTitle3.setVisibility(View.VISIBLE);
                mLlTitle4.setVisibility(View.VISIBLE);
                mTvTitle2.setText("百位走势");
                mTvTitle3.setText("十位走势");
                mTvTitle4.setText("个位走势");
                if (mPosition > 3) {
                    mPosition = 1;//超出页面默认百位走势
                }
                break;
            case ALWAYS_COLOR_3_GROUP_3:
                mLlTitle3.setVisibility(View.VISIBLE);
                mTvTitle2.setText("走势");
                mTvTitle3.setText("跨度");
                if (mPosition > 2) {
                    mPosition = 1;//超出页面默认走势
                }
                break;
            case ALWAYS_COLOR_3_GROUP_6:
                mLlTitle3.setVisibility(View.VISIBLE);
                mTvTitle2.setText("走势");
                mTvTitle3.setText("跨度");
                if (mPosition > 2) {
                    mPosition = 1;//超出页面默认走势
                }
                break;
            case ALWAYS_COLOR_5_DIRECT:
                mLlTitle2.setVisibility(View.VISIBLE);
                mLlTitle3.setVisibility(View.VISIBLE);
                mLlTitle4.setVisibility(View.VISIBLE);
                mLlTitle5.setVisibility(View.VISIBLE);
                mLlTitle6.setVisibility(View.VISIBLE);
                mTvTitle2.setText("万位走势");
                mTvTitle3.setText("千位走势");
                mTvTitle4.setText("百位走势");
                mTvTitle5.setText("十位走势");
                mTvTitle6.setText("个位走势");
                break;
            case ALWAYS_COLOR_5_ALL:
                mLlTitle2.setVisibility(View.VISIBLE);
                mLlTitle3.setVisibility(View.VISIBLE);
                mLlTitle4.setVisibility(View.VISIBLE);
                mLlTitle5.setVisibility(View.VISIBLE);
                mLlTitle6.setVisibility(View.VISIBLE);
                mTvTitle2.setText("万位走势");
                mTvTitle3.setText("千位走势");
                mTvTitle4.setText("百位走势");
                mTvTitle5.setText("十位走势");
                mTvTitle6.setText("个位走势");
                break;
            case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                mTvTitle2.setText("形态走势");
                if (mPosition > 1) {
                    mPosition = 1;//超出页面默认形态走势
                }
                break;
        }
    }

    private void updateTitleColor(int pos) {
        setBGColor(mTvTitle1, mVLine1, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle2, mVLine2, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle3, mVLine3, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle4, mVLine4, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle5, mVLine5, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle6, mVLine6, R.color.color_504f58, R.color.color_d7d6d2);
        switch (pos) {
            case 0:
                setBGColor(mTvTitle1, mVLine1, R.color.color_dd3048, R.color.color_dd3048);
                break;
            case 1:
                setBGColor(mTvTitle2, mVLine2, R.color.color_dd3048, R.color.color_dd3048);
                break;
            case 2:
                setBGColor(mTvTitle3, mVLine3, R.color.color_dd3048, R.color.color_dd3048);
                break;
            case 3:
                setBGColor(mTvTitle4, mVLine4, R.color.color_dd3048, R.color.color_dd3048);
                break;
            case 4:
                setBGColor(mTvTitle5, mVLine5, R.color.color_dd3048, R.color.color_dd3048);
                break;
            case 5:
                setBGColor(mTvTitle6, mVLine6, R.color.color_dd3048, R.color.color_dd3048);
                break;
        }
    }

    /**
     * @param pos 切换到制定Fragment 玩法的位置不固定
     */
    private void showFragment(int pos) {
        mPosition = pos;
        updateTitleColor(pos);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(mFragmentManager, transaction);
        switch (pos) {
            case 0:
                showAwardFrag(transaction);
                break;
            case 1:
                switch (mPlayType) {
                    case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE://大小单双形态走势
                        showFormTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_1_DIRECT://个位走势
                        showGeTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_2_DIRECT://十位走势
                        showShiTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_3_DIRECT://百位走势
                        showBaiTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_2_GROUP://二星组选
                        showBasicTrendFrag(mPlayType, transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_3_GROUP_3://三星组三 走势
                    case AppConstants.ALWAYS_COLOR_3_GROUP_6://三星组六
                        showBasicTrendFrag(mPlayType, transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_5_DIRECT://五星 万位走势
                    case AppConstants.ALWAYS_COLOR_5_ALL:
                        showWanTrendFrag(transaction);
                        break;
                }
                break;
            case 2:
                switch (mPlayType) {
                    case AppConstants.ALWAYS_COLOR_1_DIRECT://个位振幅
                        showZFTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_2_DIRECT://个位走势
                        showGeTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_3_DIRECT://十位走势
                        showShiTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_2_GROUP://二星组选 跨度
                        showKDTrendFrag(mPlayType, transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_3_GROUP_3://三星组三 跨度
                    case AppConstants.ALWAYS_COLOR_3_GROUP_6://三星组六
                        showKDTrendFrag(mPlayType, transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_5_DIRECT://五星 千位走势
                    case AppConstants.ALWAYS_COLOR_5_ALL:
                        showQianTrendFrag(transaction);
                        break;
                }
                break;
            case 3:
                switch (mPlayType) {
                    case AppConstants.ALWAYS_COLOR_3_DIRECT://前三直选 个位走势
                        showGeTrendFrag(transaction);
                        break;
                    case AppConstants.ALWAYS_COLOR_5_DIRECT://五星 百位走势
                    case AppConstants.ALWAYS_COLOR_5_ALL:
                        showBaiTrendFrag(transaction);
                        break;
                }
                break;
            case 4:
                switch (mPlayType) {
                    case AppConstants.ALWAYS_COLOR_5_DIRECT://五星 十位走势
                    case AppConstants.ALWAYS_COLOR_5_ALL:
                        showShiTrendFrag(transaction);
                        break;
                }
                break;
            case 5:
                switch (mPlayType) {
                    case AppConstants.ALWAYS_COLOR_5_DIRECT://五星 个位走势
                    case AppConstants.ALWAYS_COLOR_5_ALL:
                        showGeTrendFrag(transaction);
                        break;
                }
                break;
        }
        transaction.commit();
    }

    private void showAwardFrag(FragmentTransaction transaction) {
        if (mOpenAwardFragment == null && mTypeData != null) {
            LogF.d(TAG, "开奖 init");
            mOpenAwardFragment = OpenAwardFragment.newInstance(mLotteryType, mPlayType, mTypeData);
            transaction.add(R.id.fl_trend_container, mOpenAwardFragment, FRAGMENT_TAG_OPEN);
        }
        transaction.show(mOpenAwardFragment);
        LogF.d(TAG, "开奖");
    }

    //个位走势
    private void showGeTrendFrag(FragmentTransaction transaction) {
        if (mTrendGeFragment == null && mTypeData != null) {
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 10;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 1);
            mTrendGeFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
            transaction.add(R.id.fl_trend_container, mTrendGeFragment, FRAGMENT_TAG_TREND_GE);
        }
        transaction.show(mTrendGeFragment);
        LogF.d(TAG, "个位走势");
    }

    //十位走势
    private void showShiTrendFrag(FragmentTransaction transaction) {
        if (mTrendSHIFragment == null && mTypeData != null) {
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 10;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 2);
            mTrendSHIFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
            transaction.add(R.id.fl_trend_container, mTrendSHIFragment, FRAGMENT_TAG_TREND_SHI);
        }
        transaction.show(mTrendSHIFragment);
        LogF.d(TAG, "十位走势");
    }

    //百位走势
    private void showBaiTrendFrag(FragmentTransaction transaction) {
        if (mTrendBAIFragment == null && mTypeData != null) {
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 10;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 3);
            mTrendBAIFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
            transaction.add(R.id.fl_trend_container, mTrendBAIFragment, FRAGMENT_TAG_TREND_BAI);
        }
        transaction.show(mTrendBAIFragment);
        LogF.d(TAG, "百位走势");
    }

    //千位走势
    private void showQianTrendFrag(FragmentTransaction transaction) {
        if (mTrendQIANFragment == null && mTypeData != null) {
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 10;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 4);
            mTrendQIANFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
            transaction.add(R.id.fl_trend_container, mTrendQIANFragment, FRAGMENT_TAG_TREND_QIAN);
        }
        transaction.show(mTrendQIANFragment);
        LogF.d(TAG, "千位走势");
    }

    //万位走势
    private void showWanTrendFrag(FragmentTransaction transaction) {
        if (mTrendWANFragment == null && mTypeData != null) {
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 10;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 5);
            mTrendWANFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
            transaction.add(R.id.fl_trend_container, mTrendWANFragment, FRAGMENT_TAG_TREND_WAN);
        } else {
        }
        transaction.show(mTrendWANFragment);
        LogF.d(TAG, "万位走势");
    }

    //跨度
    private void showKDTrendFrag(int playType, FragmentTransaction transaction) {
        switch (playType) {
            case AppConstants.ALWAYS_COLOR_2_GROUP:
                if (mTrendKDFragment2Group == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 10;
                    //重新new  跨度 numPos=0
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 0);
                    mTrendKDFragment2Group = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
                    transaction.add(R.id.fl_trend_container, mTrendKDFragment2Group, FRAGMENT_TAG_TREND_KD_2GROUP);
                }
                transaction.show(mTrendKDFragment2Group);
                LogF.d(TAG, "二星组选跨度");
                break;
            case AppConstants.ALWAYS_COLOR_3_GROUP_3:
                if (mTrendKDFragment3Group3 == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 10;
                    //重新new  跨度 numPos=0
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 0);
                    mTrendKDFragment3Group3 = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
                    transaction.add(R.id.fl_trend_container, mTrendKDFragment3Group3, FRAGMENT_TAG_TREND_KD_3GROUP3);
                }
                transaction.show(mTrendKDFragment3Group3);
                LogF.d(TAG, "三星组三跨度");
                break;
            case AppConstants.ALWAYS_COLOR_3_GROUP_6:
                if (mTrendKDFragment3Group6 == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 10;
                    //重新new  跨度 numPos=0
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 0);
                    mTrendKDFragment3Group6 = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
                    transaction.add(R.id.fl_trend_container, mTrendKDFragment3Group6, FRAGMENT_TAG_TREND_KD_3GROUP6);
                }
                transaction.show(mTrendKDFragment3Group6);
                LogF.d(TAG, "三星组六跨度");
                break;
        }
    }

    //振幅
    private void showZFTrendFrag(FragmentTransaction transaction) {
        if (mTrendZFFragment == null && mTypeData != null) {
            LogF.d(TAG, "个位振幅初始化");
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 10;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, -1);
            mTrendZFFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
            transaction.add(R.id.fl_trend_container, mTrendZFFragment, FRAGMENT_TAG_TREND_ZF);
        }
        transaction.show(mTrendZFFragment);
        LogF.d(TAG, "个位振幅");
    }

    //走势
    private void showBasicTrendFrag(int playType, FragmentTransaction transaction) {
        switch (playType) {
            case AppConstants.ALWAYS_COLOR_2_GROUP:
                if (mTrendKDFragment2GroupBasic == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 10;
                    //重新new
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType,
                            mTypeData, -2);
                    mTrendKDFragment2GroupBasic = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0, false);
                    transaction.add(R.id.fl_trend_container, mTrendKDFragment2GroupBasic, FRAGMENT_TAG_BASIC_2GROUP);
                }
                transaction.show(mTrendKDFragment2GroupBasic);
                LogF.d(TAG, "二星组选走势");
                break;
            case AppConstants.ALWAYS_COLOR_3_GROUP_3:
                if (mTrendKDFragment3Group3Basic == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 10;
                    //重新new
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, -2);
                    mTrendKDFragment3Group3Basic = TrendFragment.newInstance(lotteryTrendData, xItemWidth,
                            leftTitleItemWidth, 0, false);
                    transaction.add(R.id.fl_trend_container, mTrendKDFragment3Group3Basic, FRAGMENT_TAG_BASIC_3GROUP3);
                }
                transaction.show(mTrendKDFragment3Group3Basic);
                LogF.d(TAG, "三星组三走势");
                break;
            case AppConstants.ALWAYS_COLOR_3_GROUP_6:
                if (mTrendKDFragment3Group6Basic == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 10;
                    //重新new
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType,
                            mTypeData, -2);
                    mTrendKDFragment3Group6Basic = TrendFragment.newInstance(lotteryTrendData, xItemWidth,
                            leftTitleItemWidth, 0,
                            false);
                    transaction.add(R.id.fl_trend_container, mTrendKDFragment3Group6Basic, FRAGMENT_TAG_BASIC_3GROUP6);
                }
                transaction.show(mTrendKDFragment3Group6Basic);
                LogF.d(TAG, "三星组六走势");
                break;
        }
    }

    //形态
    private void showFormTrendFrag(FragmentTransaction transaction) {
        if (mTrendFormFragment == null && mTypeData != null) {
            LogF.d(TAG, "形态走势 init");
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int leftTitleSecondItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth - leftTitleItemWidth) / 8;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, -3);
            mTrendFormFragment = AlwaysColorFormTrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, leftTitleSecondItemWidth);
            transaction.add(R.id.fl_trend_container, mTrendFormFragment, FRAGMENT_TAG_FORM);
        }
        transaction.show(mTrendFormFragment);
        LogF.d(TAG, "形态走势");
    }

    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (String fragmentTag : mFragmentTags) {
            LogF.d(TAG, "隐藏Fragments  " + fragmentTag);
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
                LogF.d(TAG, "隐藏Fragments  已隐藏" + fragmentTag);
            }
        }
    }


    public void setBGColor(TextView textView, View view, int textColor, int bgColor) {
        textView.setTextColor(getActivity().getResources().getColor(textColor));
        view.setBackground(getActivity().getResources().getDrawable(bgColor));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mUnbinder)
            mUnbinder.unbind();
    }
}
