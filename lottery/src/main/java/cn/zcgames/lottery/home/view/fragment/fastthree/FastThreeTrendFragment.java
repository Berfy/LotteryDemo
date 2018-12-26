package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.OpenAwardBean;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.model.local.LotteryTrendData;

import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_MORE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ALL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_TO_ALL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_SUM;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_11_5;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_11_5_LUCKY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_11_5_OLD;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_11_5_YILE;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_11_5_YUE;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_EASY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_HB;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_JS;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_NEW;

public class FastThreeTrendFragment extends LotteryTrendBaseFragment {
    private TextView mTVOpenNum, mTVBasicTrend, mTVSumTrend;
    private View mViewOpen, mViewBasic, mViewSum;
    private LinearLayout mLLOpen, mLLBasic, mLLSum;
    private final String FRAGMENT_TAG_OPEN = "open";
    private final String FRAGMENT_TAG_BASIC = "basic";
    private final String FRAGMENT_TAG_SUM = "sum";
    private final String FRAGMENT_TAG_OPEN_TWO = "openTwo";
    private final String FRAGMENT_TAG_FORM_TREND = "formTrend";
    private final String FRAGMENT_TAG_DISTRIBUTE_ONE = "distributeOne";
    private final String FRAGMENT_TAG_DISTRIBUTE_TWO = "distributeTwo";

    private OpenAwardFragment openAwardFragment;
    private SumBasicTrendFragment basicTrendFragment;
    //    private TotalTrendFragment totalTrendFragment;
    private TrendFragment trendFragment;
    private TrendFragment trendFragment2Diff;
    private OpenAwardTwoFragment openTwoFragment;
    private FormTrendFragment formTrendFragment;
    private NumDistributeFragment distributeOneFragment;
    private TotalTrendFragment totalTrendFragment;
    //    private NumDistributeTwoFragment distributeTwoFragment;
    private String[] fragmentTags = {FRAGMENT_TAG_OPEN, FRAGMENT_TAG_BASIC, FRAGMENT_TAG_SUM, FRAGMENT_TAG_OPEN_TWO,
            FRAGMENT_TAG_FORM_TREND, FRAGMENT_TAG_DISTRIBUTE_ONE, FRAGMENT_TAG_DISTRIBUTE_TWO};
    private FragmentManager manager;
    private boolean openIsSelected, basicIsSelected, sumIsSelected;

    private List<TrendResponseData.TrendData> missDatas = new ArrayList<>();
    private LotteryTrendData mLotteryTrendData;
    private List<OpenAwardBean> awardDatas;
    private String lotteryType;
    private int playType;
    private String turnType;


    public static FastThreeTrendFragment newInstance(LotteryTrendData lotteryTrendData, List<TrendResponseData.TrendData> data, String turnType) {
        FastThreeTrendFragment trendFragment = new FastThreeTrendFragment();
        Bundle bundle = new Bundle();
        if (null != lotteryTrendData) {
            bundle.putString("lotteryType", lotteryTrendData.getLotteryType());
            bundle.putInt("playType", lotteryTrendData.getPlayType());
        }
        bundle.putString("turnType", turnType);
        bundle.putSerializable("lotteryTrendData", lotteryTrendData);
        bundle.putSerializable("data", (Serializable) data);
        trendFragment.setArguments(bundle);
        return trendFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View sumView = inflater.inflate(R.layout.fragment_sum_trend, container, false);
        EventBus.getDefault().register(this);
        initView(sumView);
        initData();
        initListener();
        return sumView;
    }

    public void initView(View view) {
        mTVOpenNum = view.findViewById(R.id.tv_title1);
        mTVBasicTrend = view.findViewById(R.id.tv_title2);
        mTVSumTrend = view.findViewById(R.id.tv_title3);
        mViewOpen = view.findViewById(R.id.v_line1);
        mViewBasic = view.findViewById(R.id.v_line2);
        mViewSum = view.findViewById(R.id.v_line3);
        mLLOpen = view.findViewById(R.id.ll_title1);
        mLLBasic = view.findViewById(R.id.ll_title2);
        mLLSum = view.findViewById(R.id.ll_title3);
    }

    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            lotteryType = bundle.getString("lotteryType");
            playType = bundle.getInt("playType");
            turnType = bundle.getString("turnType");
            mLotteryTrendData = (LotteryTrendData) bundle.getSerializable("lotteryTrendData");
            missDatas = (List<TrendResponseData.TrendData>) bundle.getSerializable("data");
//            getDataFromNet(bundle);
        }
        Log.d("111111", "切换的类型 ：lotteryType=" + lotteryType + ",playType=" + playType + ",turnType =" + turnType);
        //改变选项标题
        setLayoutTitle(playType);
        manager = getChildFragmentManager();
        //设置展示的界面
        changeFragment();

    }

    //根据跳转类型切换界面
    public void changeFragment() {
        if (TextUtils.equals("trendView", turnType)) {
            switch (playType) {
                case FAST_THREE_3_SAME_ONE:
                case FAST_THREE_3_SAME_ALL:
                case FAST_THREE_3_TO_ALL:
                case FAST_THREE_3_DIFFERENT:
                case FAST_THREE_2_SAME_ONE:
                case FAST_THREE_2_SAME_MORE:
                case FAST_THREE_2_DIFFERENT:
                    initSecondBackGround();
                    break;
                case FAST_THREE_SUM:
                    initThirdBackGround();
                    break;

            }
        } else if (TextUtils.equals("openAward", turnType)) {
            initFirstBackGround();
        }
    }

    /**
     * public static final int FAST_THREE_SUM = 3;//和值
     * public static final int FAST_THREE_3_SAME_ONE = 4;//三同号单选
     * public static final int FAST_THREE_3_SAME_ALL = 5;//三同号通选
     * public static final int FAST_THREE_3_TO_ALL = 6;//三连号通选
     * public static final int FAST_THREE_3_DIFFERENT = 7;//三不同号
     * public static final int FAST_THREE_2_SAME_ONE = 8;//二同号单选
     * public static final int FAST_THREE_2_SAME_MORE = 9;//二同号复选
     * public static final int FAST_THREE_2_DIFFERENT = 10;//二不同号
     */
    public void initListener() {

        mLLOpen.setOnClickListener(v -> {
            if (openIsSelected) {
                return;
            } else {
                initFirstBackGround();
                EventBus.getDefault().post(new TrendSettingUIChangeEvent());
            }
        });

        mLLBasic.setOnClickListener(v -> {
            if (basicIsSelected) {
                return;
            } else {
                initSecondBackGround();
                EventBus.getDefault().post(new TrendSettingUIChangeEvent());
            }
        });

        mLLSum.setOnClickListener(v -> {
            if (sumIsSelected) {
                return;
            } else {
                initThirdBackGround();
                EventBus.getDefault().post(new TrendSettingUIChangeEvent());
            }
        });

    }

    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    public void showFirstFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (FAST_THREE_SUM == playType) {
            if (openAwardFragment == null) {
//                openAwardFragment = OpenAwardFragment.newInstance(playType, lotteryType, awardDatas);
                openAwardFragment = OpenAwardFragment.newInstance(playType, lotteryType, missDatas);
                transaction.add(R.id.fl_trend_container, openAwardFragment, FRAGMENT_TAG_OPEN);
            }
            hideFragments(manager, transaction);
            transaction.show(openAwardFragment);
        } else {
            if (openTwoFragment == null) {
                openTwoFragment = OpenAwardTwoFragment.newInstance(lotteryType, playType, missDatas);
                transaction.add(R.id.fl_trend_container, openTwoFragment, FRAGMENT_TAG_OPEN_TWO);
            }
            hideFragments(manager, transaction);
            transaction.show(openTwoFragment);
        }
        transaction.commit();
    }

    public void showSecondFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (basicTrendFragment == null) {
            basicTrendFragment = SumBasicTrendFragment.newInstance(lotteryType, playType, missDatas);
            transaction.add(R.id.fl_trend_container, basicTrendFragment, FRAGMENT_TAG_BASIC);
        }
        hideFragments(manager, transaction);
        transaction.show(basicTrendFragment);
        transaction.commit();
    }

    public void showLastFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (playType) {
            case FAST_THREE_SUM:
//                if (totalTrendFragment == null) {
//                    totalTrendFragment = TotalTrendFragment.newInstance(lotteryType, playType, missDatas);
//                    transaction.add(R.id.fl_trend_container, totalTrendFragment, FRAGMENT_TAG_SUM);
//                }
//                hideFragments(manager, transaction);
//                transaction.show(totalTrendFragment);

                if (trendFragment == null) {
                    trendFragment = TrendFragment.newInstance(mLotteryTrendData);
                    transaction.add(R.id.fl_trend_container, trendFragment, FRAGMENT_TAG_SUM);
                }
                hideFragments(manager, transaction);
                transaction.show(trendFragment);


                break;
            case FAST_THREE_3_SAME_ONE:
            case FAST_THREE_3_SAME_ALL:
            case FAST_THREE_3_TO_ALL:
            case FAST_THREE_3_DIFFERENT:
                if (formTrendFragment == null) {
                    formTrendFragment = FormTrendFragment.newInstance(lotteryType, playType, missDatas);
                    transaction.add(R.id.fl_trend_container, formTrendFragment, FRAGMENT_TAG_FORM_TREND);
                }
                hideFragments(manager, transaction);
                transaction.show(formTrendFragment);
                break;
            case FAST_THREE_2_SAME_ONE:
            case FAST_THREE_2_SAME_MORE:
                if (distributeOneFragment == null) {
                    distributeOneFragment = NumDistributeFragment.newInstance(lotteryType, playType, missDatas);
                    transaction.add(R.id.fl_trend_container, distributeOneFragment, FRAGMENT_TAG_DISTRIBUTE_ONE);
                }
                hideFragments(manager, transaction);
                transaction.show(distributeOneFragment);
                break;
            case FAST_THREE_2_DIFFERENT:
//                if (distributeTwoFragment == null) {
//                    distributeTwoFragment = NumDistributeTwoFragment.newInstance(lotteryType, playType, missDatas);
//                    transaction.add(R.id.fl_trend_container, distributeTwoFragment, FRAGMENT_TAG_DISTRIBUTE_TWO);
//                }
//                hideFragments(manager, transaction);
//                transaction.show(distributeTwoFragment);
                if (trendFragment2Diff == null) {
                    trendFragment2Diff = TrendFragment.newInstance(mLotteryTrendData);
                    transaction.add(R.id.fl_trend_container, trendFragment2Diff, FRAGMENT_TAG_DISTRIBUTE_TWO);
                }
                hideFragments(manager, transaction);
                transaction.show(trendFragment2Diff);
                break;
        }
        transaction.commit();
    }

    public void initThirdBackGround() {
        setBGColor(mTVOpenNum, mViewOpen, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTVBasicTrend, mViewBasic, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTVSumTrend, mViewSum, R.color.color_dd3048, R.color.color_dd3048);
        openIsSelected = false;
        basicIsSelected = false;
        sumIsSelected = true;
        showLastFragment();
    }

    public void initSecondBackGround() {
        setBGColor(mTVOpenNum, mViewOpen, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTVBasicTrend, mViewBasic, R.color.color_dd3048, R.color.color_dd3048);
        setBGColor(mTVSumTrend, mViewSum, R.color.color_504f58, R.color.color_d7d6d2);
        openIsSelected = false;
        basicIsSelected = true;
        sumIsSelected = false;
        showSecondFragment();
    }

    public void initFirstBackGround() {
        setBGColor(mTVOpenNum, mViewOpen, R.color.color_dd3048, R.color.color_dd3048);
        setBGColor(mTVBasicTrend, mViewBasic, R.color.color_504f58, R.color.color_d7d6d2);
        setBGColor(mTVSumTrend, mViewSum, R.color.color_504f58, R.color.color_d7d6d2);
        openIsSelected = true;
        basicIsSelected = false;
        sumIsSelected = false;
        showFirstFragment();
    }

    public void setBGColor(TextView textView, View view, int textColor, int bgColor) {
        textView.setTextColor(getActivity().getResources().getColor(textColor));
        view.setBackground(getActivity().getResources().getDrawable(bgColor));
    }

    public void setLayoutTitle(int playTypw) {
        switch (playTypw) {
            case FAST_THREE_SUM:
                setLayoutName(mTVBasicTrend, "基本走势");
                setLayoutName(mTVSumTrend, "和值走势");
                break;
            case FAST_THREE_3_SAME_ONE:
            case FAST_THREE_3_SAME_ALL:
            case FAST_THREE_3_TO_ALL:
            case FAST_THREE_3_DIFFERENT:
                setLayoutName(mTVBasicTrend, "基本走势");
                setLayoutName(mTVSumTrend, "形态走势");
                break;
            case FAST_THREE_2_SAME_ONE:
            case FAST_THREE_2_SAME_MORE:
            case FAST_THREE_2_DIFFERENT:
                setLayoutName(mTVBasicTrend, "基本走势");
                setLayoutName(mTVSumTrend, "号码分布");
                break;
        }
    }

    public void setLayoutName(TextView mTV, String name) {
        mTV.setText(name);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLotteryPlayType(LotteryTrendData data) {
        if (data != null) {
            mLotteryTrendData = data;

            /*lotteryType = data.getLotteryType();
            playType = data.getPlayType();
            if(missDatas.size() == data.getData().size()){
                if (openIsSelected) {
                    showFirstFragment();
                } else if (sumIsSelected) {
                    if (playType == FAST_THREE_SUM) {
                        showLastFragment();
                    } else {
                        mLLBasic.performClick();
                    }
                }
            }*/

            //防止期数改变时跳动
            if(missDatas.size() == data.getData().size()){
                //防止刷新数据时跳动
                if(playType != data.getPlayType()){
                    boolean isTwoDiffer;
                    isTwoDiffer = playType == FAST_THREE_2_DIFFERENT ? true : false;
                    lotteryType = data.getLotteryType();
                    playType = data.getPlayType();
                    if (openIsSelected) {
                        showFirstFragment();
                    } else if (sumIsSelected) {
                        if (playType == FAST_THREE_SUM) {
                            if(isTwoDiffer){
                                mLLBasic.performClick();
                            }else{
                                showLastFragment();
                            }
                        } else {
                            mLLBasic.performClick();
                        }
                    }
                }else{
                    lotteryType = data.getLotteryType();
                    playType = data.getPlayType();
                }
            }else{
                lotteryType = data.getLotteryType();
                playType = data.getPlayType();
            }
            missDatas = data.getData();
            Log.d("111111", "切换的类型2 ：lotteryType=" + lotteryType + ",playType=" + playType);

            //改变选项标题
            setLayoutTitle(playType);
        }
    }


    public void getDataFromNet(Bundle bundle) {
        switch (lotteryType) {
            case LOTTERY_TYPE_FAST_3://快三
            case LOTTERY_TYPE_FAST_3_JS:
            case LOTTERY_TYPE_FAST_3_HB:
            case LOTTERY_TYPE_FAST_3_EASY:
            case LOTTERY_TYPE_FAST_3_NEW:
//                List<OpenAwardBean> openAwardBeans;//开奖列表
//                for (TrendResponseData.TrendData trendData : trendResponseData.getData()) {
//                    OpenAwardBean openAwardBean = new OpenAwardBean();
//                    openAwardBean.setAwardNums(trendData.getWinnerNumber());
//                }
                switch (playType) {
                    case FAST_THREE_SUM:
                        awardDatas = (List<OpenAwardBean>) bundle.getSerializable("data");
                    case FAST_THREE_3_SAME_ONE:
                    case FAST_THREE_3_SAME_ALL:
                    case FAST_THREE_3_TO_ALL:
                    case FAST_THREE_3_DIFFERENT:
                    case FAST_THREE_2_SAME_ONE:
                    case FAST_THREE_2_SAME_MORE:
                    case FAST_THREE_2_DIFFERENT:

                }
                break;
            case LOTTERY_TYPE_11_5://11选5
            case LOTTERY_TYPE_11_5_LUCKY:
            case LOTTERY_TYPE_11_5_OLD:
            case LOTTERY_TYPE_11_5_YILE:
            case LOTTERY_TYPE_11_5_YUE:
                break;

            case LOTTERY_TYPE_ALWAYS_COLOR://时时彩
            case LOTTERY_TYPE_ALWAYS_COLOR_NEW:

                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


}
