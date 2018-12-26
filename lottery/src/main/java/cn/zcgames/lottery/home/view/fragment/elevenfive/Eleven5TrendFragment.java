package cn.zcgames.lottery.home.view.fragment.elevenfive;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.DisplayUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.view.fragment.fastthree.LotteryTrendBaseFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.OpenAwardFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.TrendFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;

import static cn.zcgames.lottery.app.AppConstants.*;

/**
 * 十一选五走势图页面
 * 任选二、任选三、任选四、任选五、任选六、
 * 任选七、任选八、前二组选、前三组选
 * 前一直选,前二直选,前三直选走势图
 *
 * @author NorthStar
 * @date 2018/10/12 18:45
 */
public class Eleven5TrendFragment extends LotteryTrendBaseFragment {
    @BindView(R.id.tv_title1)
    TextView mTvTitle1;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.tv_title3)
    TextView mTvTitle3;
    @BindView(R.id.tv_title4)
    TextView mTvTitle4;

    @BindView(R.id.v_line1)
    View mVLine1;
    @BindView(R.id.v_line2)
    View mVLine2;
    @BindView(R.id.v_line3)
    View mVLine3;
    @BindView(R.id.v_line4)
    View mVLine4;

    @BindView(R.id.ll_title1)
    LinearLayout mLlTitle1;
    @BindView(R.id.ll_title2)
    LinearLayout mLlTitle2;
    @BindView(R.id.ll_title3)
    LinearLayout mLlTitle3;
    @BindView(R.id.ll_title4)
    LinearLayout mLlTitle4;

    private final String FRAGMENT_TAG_OPEN = "open";//开奖
    private final String FRAGMENT_TAG_BASIC = "basic";//走势
    private final String FRAGMENT_TAG_ANY_FORM = "any_form";//除前一外的形态
    private final String FRAGMENT_TAG_DIRECT_FORM = "direct_form";//前一的形态
    private final String FRAGMENT_TAG_TREND_BAI = "trend_bai";//百位走势图
    private final String FRAGMENT_TAG_TREND_QIAN = "trend_qian";//千位走势图
    private final String FRAGMENT_TAG_TREND_WAN = "trend_wan";//万位走势图
    private final String FRAGMENT_TAG_FRONT_2_GROUP = "trend_group2";//前二组选 走势 numPos -2
    private final String FRAGMENT_TAG_FRONT_3_GROUP = "trend_group3";//前三组选 走势 numPos -2
    private Unbinder unbinder;

    private int mPlayType;
    private String mLotteryType;
    public static final String TAG = "Eleven5TrendFragment";

    private OpenAwardFragment openAwardFragment;
    private AnyBasicTrendFragment basicTrendFragment;
    private AnyFormFragment formTrendFragment;
    private TrendFragment directFormFragment;//前一形态走势
    private TrendFragment mTrendBAIFragment;//百位走势
    private TrendFragment mTrendQIANFragment;//千位走势
    private TrendFragment mTrendWANFragment;//万位走势
    private TrendFragment mTrendFragmentGroup2Basic;//前二组选走势 numPos -2
    private TrendFragment mTrendFragmentGroup3Basic;//前三组选走势 numPos -2
    private String[] mFragmentTags = {FRAGMENT_TAG_OPEN, FRAGMENT_TAG_BASIC,FRAGMENT_TAG_TREND_WAN,
            FRAGMENT_TAG_TREND_QIAN,FRAGMENT_TAG_TREND_BAI,FRAGMENT_TAG_DIRECT_FORM, FRAGMENT_TAG_ANY_FORM,
            FRAGMENT_TAG_FRONT_2_GROUP, FRAGMENT_TAG_FRONT_3_GROUP};
    private FragmentManager manager;
    private TrendTypeData mTypeData;
    private String sourceType;
    private int mPosition;

    public static Eleven5TrendFragment newInstance(LotteryTrendData data) {
        Eleven5TrendFragment fragment = new Eleven5TrendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TREND_TYPE_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View trendView = inflater.inflate(R.layout.fragment_sum_trend, container, false);
        unbinder = ButterKnife.bind(this, trendView);
        initData();
        initView();
        initListener();
        return trendView;
    }

    //获取彩种数据及玩法
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            LotteryTrendData data = (LotteryTrendData) bundle.getSerializable(KEY_TREND_TYPE_DATA);
            if (data != null) {
                mLotteryType = data.getLotteryType();
                mPlayType = data.getPlayType();
                mTypeData = data.getTypeData();
                sourceType = data.getSourceType();
                LogF.d(TAG, "任选--彩种类型==>" + mLotteryType + " ,玩法类型==>" + mPlayType
                        + "来源类型==>" + sourceType);
            }
        }
    }

    public void initView() {
        manager = getChildFragmentManager();
        updateTitle();
        //设置展示的界面
        if (TextUtils.equals(TREND_VIEW, sourceType)) {
            mPosition = 1;//默认为基本走势
        } else if (TextUtils.equals(LATEST_OPEN_AWARD, sourceType)) {
            mPosition = 0;
        }
        showFragment(mPosition);
    }

    public void initTrendData(LotteryTrendData data) {
        mLotteryType = data.getLotteryType();
        mPlayType = data.getPlayType();
        mTypeData = data.getTypeData();
        LogF.d(TAG, "11选5切换玩法 彩种类型==>" + mLotteryType + " ,玩法类型==>" + mPlayType);
        EventBus.getDefault().post(data);//传输最新走势数据
        updateTitle();
        showFragment(mPosition);
    }

    private void updateTitle() {
        mTvTitle1.setText("开奖");
        mLlTitle4.setVisibility(View.GONE);
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
            case PLAY_11_5_FRONT_2_GROUP://前二组选
            case PLAY_11_5_FRONT_2_GROUP_DAN://前二组选胆拖
            case PLAY_11_5_FRONT_3_GROUP://前三组选
            case PLAY_11_5_FRONT_3_GROUP_DAN://前三组选胆拖
            case PLAY_11_5_FRONT_1_DIRECT://前一直选
                mTvTitle2.setText("走势");
                mTvTitle3.setText("形态");
                if (mPosition > 2) {
                    mPosition = 1;//超出页面默认走势
                }
                break;

            case PLAY_11_5_FRONT_2_DIRECT://前二直选
                mTvTitle2.setText("万位走势");
                mTvTitle3.setText("千位走势");
                if (mPosition > 2) {
                    mPosition = 1;//超出页面默认万位走势
                }
                break;
            case PLAY_11_5_FRONT_3_DIRECT://前三直选
                mTvTitle2.setText("万位走势");
                mTvTitle3.setText("千位走势");
                mTvTitle4.setText("百位走势");
                if (mPosition > 3) {
                    mPosition = 1;//超出页面默认万位走势
                }
                mLlTitle4.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateTitleColor(int pos) {
        setBGColor(mTvTitle1, mVLine1, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle2, mVLine2, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle3, mVLine3, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTvTitle4, mVLine4, R.color.color_504f58, R.color.color_d7d6d2);
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
        }
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
    }

    /**
     * @param pos 切换到制定Fragment 玩法的位置不固定
     */
    private void showFragment(int pos) {
        mPosition = pos;
        updateTitleColor(pos);
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        switch (pos) {
            case 0:
                showAwardFrag(transaction);
                break;
            case 1:
                switch (mPlayType) {
                    case PLAY_11_5_FRONT_1_DIRECT://前一直选
                    case PLAY_11_5_FRONT_2_DIRECT://前二直选
                    case PLAY_11_5_FRONT_3_DIRECT://前三直选
                        showWanTrendFrag(transaction);
                        break;
                    default:
                        showBasicTrendFrag(mPlayType, transaction);
                        break;
                }
                break;
            case 2:
                switch (mPlayType) {
                    case PLAY_11_5_FRONT_1_DIRECT://前一直选-形态
                        showDirect1Form(transaction);
                        break;
                    case PLAY_11_5_FRONT_2_DIRECT://前二直选-千位
                    case PLAY_11_5_FRONT_3_DIRECT://前三直选-千位
                        showQianTrendFrag(transaction);
                        break;
                    default:
                        showFormFragment(transaction);//任选和组选-形态
                        break;
                }
                break;
            case 3:
                switch (mPlayType) {
                    case PLAY_11_5_FRONT_3_DIRECT://前三直选-百位走势
                        showBaiTrendFrag(transaction);
                        break;
                }
                break;
        }
        transaction.commit();
    }


    //开奖走势图
    private void showAwardFrag(FragmentTransaction transaction) {
        if (openAwardFragment == null && mTypeData != null) {
            openAwardFragment = OpenAwardFragment.newInstance(mLotteryType, mPlayType, mTypeData);
            transaction.add(R.id.fl_trend_container, openAwardFragment, FRAGMENT_TAG_OPEN);
        }
        transaction.show(openAwardFragment);
        LogF.d(TAG, "开奖");
    }

    //基本走势图
    private void showBasicTrendFrag(int playType, FragmentTransaction transaction) {
        switch (playType) {
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
                if (basicTrendFragment == null && mTypeData != null) {
                    basicTrendFragment = AnyBasicTrendFragment.newInstance(new LotteryTrendData(mLotteryType, mPlayType, mTypeData));
                    transaction.add(R.id.fl_trend_container, basicTrendFragment, FRAGMENT_TAG_BASIC);
                }
                transaction.show(basicTrendFragment);
                LogF.d(TAG, "任选基本走势");
                break;
            case PLAY_11_5_FRONT_2_GROUP://前二组选
            case PLAY_11_5_FRONT_2_GROUP_DAN://前二组选胆拖
                if (mTrendFragmentGroup2Basic == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 11;
                    //重新new
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType,
                            mTypeData, -2);
                    mTrendFragmentGroup2Basic = TrendFragment.newInstance(lotteryTrendData, xItemWidth,
                            leftTitleItemWidth, 0, false);
                    transaction.add(R.id.fl_trend_container, mTrendFragmentGroup2Basic, FRAGMENT_TAG_FRONT_2_GROUP);
                }
                transaction.show(mTrendFragmentGroup2Basic);
                LogF.d(TAG, "前二组选");
                break;
            case PLAY_11_5_FRONT_3_GROUP://前三组选
            case PLAY_11_5_FRONT_3_GROUP_DAN://前三组选胆拖
                if (mTrendFragmentGroup3Basic == null && mTypeData != null) {
                    int width = DisplayUtil.getDisplayWidth(getActivity());
                    int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
                    int xItemWidth = (width - leftTitleItemWidth) / 11;
                    //重新new
                    LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType,
                            mTypeData, -2);

                    mTrendFragmentGroup3Basic = TrendFragment.newInstance(lotteryTrendData, xItemWidth,
                            leftTitleItemWidth, 0, false);
                    transaction.add(R.id.fl_trend_container, mTrendFragmentGroup3Basic, FRAGMENT_TAG_FRONT_3_GROUP);
                }
                transaction.show(mTrendFragmentGroup3Basic);
                LogF.d(TAG, "前三组选");
                break;
        }
    }

    //百位走势
    private void showBaiTrendFrag(FragmentTransaction transaction) {
        if (mTrendBAIFragment == null && mTypeData != null) {
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 11;
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
            int xItemWidth = (width - leftTitleItemWidth) / 11;
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
            int xItemWidth = (width - leftTitleItemWidth) / 11;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, 5);
            mTrendWANFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, 0);
            transaction.add(R.id.fl_trend_container, mTrendWANFragment, FRAGMENT_TAG_TREND_WAN);
        }
        transaction.show(mTrendWANFragment);
        LogF.d(TAG, "万位走势");
    }

    //设置任选形态走势图
    private void showFormFragment(FragmentTransaction transaction) {
        LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData);
        if (formTrendFragment == null && mTypeData != null) {
            formTrendFragment = AnyFormFragment.newInstance(lotteryTrendData);
            transaction.add(R.id.fl_trend_container, formTrendFragment, FRAGMENT_TAG_ANY_FORM);
        }
        transaction.show(formTrendFragment);
    }

    //设置前一形态
    private void showDirect1Form(FragmentTransaction transaction) {
        if (directFormFragment == null && mTypeData != null) {
            LogF.d(TAG, "前一形态 init");
            int width = DisplayUtil.getDisplayWidth(getActivity());
            int leftTitleItemWidth = DisplayUtil.dip2px(mContext, 60);
            int xItemWidth = (width - leftTitleItemWidth) / 7;
            //重新new
            LotteryTrendData lotteryTrendData = new LotteryTrendData(mLotteryType, mPlayType, mTypeData, -3);
            ArrayList<Integer> colorPaint = new ArrayList<>();
            colorPaint.add(R.color.trend_avg_jiou_color);
            colorPaint.add(R.color.trend_avg_zhihe_color);
            colorPaint.add(R.color.trend_ball_purple);
            directFormFragment = TrendFragment.newInstance(lotteryTrendData, xItemWidth,
                    leftTitleItemWidth, 0, TREND_STYLE_FORM, colorPaint, true);
            transaction.add(R.id.fl_trend_container, directFormFragment, FRAGMENT_TAG_DIRECT_FORM);
        }
        transaction.show(directFormFragment);
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

    //设置tab背景色
    public void setBGColor(TextView textView, View view, int textColor, int bgColor) {
        if (mContext != null) {
            textView.setTextColor(CommonUtil.getColor(mContext, textColor));
            view.setBackground(CommonUtil.getDrawable(mContext, bgColor));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
