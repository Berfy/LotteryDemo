package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.TrendData;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.home.view.fragment.ui.DDTrendChart;
import cn.zcgames.lottery.home.view.fragment.ui.LottoTrendView;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_DIFFERENT;

/**
 * @author HJH
 * Berfy修改
 * 2018.10.22
 * 新的走势图适配
 */
public class TrendFragment extends BaseFragment implements DDTrendChart.ISelectedChangeListener {

    private final String TAG = "走势图";
    private LottoTrendView mTrendView;
    private DDTrendChart mTrendChart;
    private List<TrendData> mDatas;
    private List<String> mTitles;
    private List<String> mLeftTitles;
    private String mLotteryType;
    private int mPlayType;
    private int mNumPos;//页面标记  区分个位-万位（1-5） 跨度0 振幅-1 基本走势-2 形态-3
    private String mTrrendStyle;//AppConstants.TREND_STYLE_BASIC 基本走势 AppConstants.TREND_STYLE_FORM 形态
    private List<Integer> mPaintColors = new ArrayList<>();
    private boolean mIsShowTitle;//标题栏可以设置不显示
    private TrendChooseNumFragment mChooseNumFragment;
    //    private TrendActivity.MyTouchListener myTouchListener;
    private float mLastX;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            mTrendChart.updateData(mTrrendStyle, (ArrayList) paramMessage.obj);
        }
    };

    /**
     * @param lotteryTrendData   数据
     * @param xItemWidth         内容item宽度
     * @param leftTitleItemWidth 左边第一列标题宽度
     * @param leftSecondRow      左边第二列标题宽度
     * @param trendStyle         走势图风格 AppConstants.TREND_STYLE_BASIC 基本走势 AppConstants.TREND_STYLE_FORM 形态
     * @param isShowTitle        标题栏可以设置不显示
     * @param paintColors        画笔颜色 支持形态多个一行多球的 左到右依次的显示颜色
     */
    public static TrendFragment newInstance(LotteryTrendData lotteryTrendData,
                                            int xItemWidth, int leftTitleItemWidth, int leftSecondRow, String trendStyle,
                                            ArrayList<Integer> paintColors, boolean isShowTitle) {
        TrendFragment basicFragment = new TrendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", lotteryTrendData);
        bundle.putSerializable("xItemWidth", xItemWidth);
        bundle.putSerializable("leftTitleItemWidth", leftTitleItemWidth);
        bundle.putSerializable("leftSecondRow", leftSecondRow);
        bundle.putBoolean("isShowLine", true);
        bundle.putString("trendStyle", trendStyle);//走势图风格
        bundle.putIntegerArrayList("paintColors", paintColors);//形态布局  画笔颜色
        bundle.putBoolean("isShowTitle", isShowTitle);//走势图风格
        basicFragment.setArguments(bundle);
        return basicFragment;
    }

    /**
     * @param lotteryTrendData   数据
     * @param xItemWidth         内容item宽度
     * @param leftTitleItemWidth 左边第一列标题宽度
     * @param leftSecondRow      左边第二列标题宽度
     * @param trendStyle         走势图风格 AppConstants.TREND_STYLE_BASIC 基本走势 AppConstants.TREND_STYLE_FORM 形态
     * @param paintColors        画笔颜色  支持形态多个一行多球的 左到右依次的显示颜色
     */
    public static TrendFragment newInstance(LotteryTrendData lotteryTrendData,
                                            int xItemWidth, int leftTitleItemWidth, int leftSecondRow, String trendStyle,
                                            ArrayList<Integer> paintColors) {
        TrendFragment basicFragment = new TrendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", lotteryTrendData);
        bundle.putSerializable("xItemWidth", xItemWidth);
        bundle.putSerializable("leftTitleItemWidth", leftTitleItemWidth);
        bundle.putSerializable("leftSecondRow", leftSecondRow);
        bundle.putBoolean("isShowLine", true);
        bundle.putString("trendStyle", trendStyle);//走势图风格
        bundle.putIntegerArrayList("paintColors", paintColors);//颜色  支持形态多个一行多球的 左到右依次的显示颜色
        basicFragment.setArguments(bundle);
        return basicFragment;
    }

    public static TrendFragment newInstance(LotteryTrendData lotteryTrendData,
                                            int xItemWidth, int leftTitleItemWidth, int leftSecondRow, boolean isShowLine) {
        TrendFragment basicFragment = new TrendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", lotteryTrendData);
        bundle.putSerializable("xItemWidth", xItemWidth);
        bundle.putSerializable("leftTitleItemWidth", leftTitleItemWidth);
        bundle.putSerializable("leftSecondRow", leftSecondRow);
        bundle.putBoolean("isShowLine", isShowLine);
        basicFragment.setArguments(bundle);
        return basicFragment;
    }

    public static TrendFragment newInstance(LotteryTrendData lotteryTrendData,
                                            int xItemWidth, int leftTitleItemWidth, int leftSecondRow) {
        return newInstance(lotteryTrendData, xItemWidth, leftTitleItemWidth, leftSecondRow, true);
    }

    public static TrendFragment newInstance(LotteryTrendData lotteryTrendData) {
        return newInstance(lotteryTrendData, 0, 0, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View totalView = inflater.inflate(R.layout.fragment_total_trend, container, false);
        EventBus.getDefault().register(this);
        initView(totalView);
        initChart();
        setSettingCache(LotteryUtils.getTrendSettingCache(mLotteryType));
        setSettingUI();
        return totalView;
    }

    public void initView(View view) {
        mTrendView = view.findViewById(R.id.lottery_trend_view);
        mTrendView.setMoveXListener(new LottoTrendView.SetMoveXPositionListener() {
            @Override
            public void setMoveX(int xPosition) {
                //                LogF.d("滑动", "偏移量   " + xPosition);
                mChooseNumFragment.scroll(-xPosition);
            }
        });
    }

    private void initChart() {
        getTrendData();
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mChooseNumFragment = TrendChooseNumFragment.newInstance(mLotteryType, mPlayType, mNumPos);
        transaction
                .add(R.id.fl_total_trend_select_num, mChooseNumFragment)
                .show(mChooseNumFragment)
                .commit();
        mChooseNumFragment.setSelectLisenter(new TrendChooseNumAdapter.ChooseNumSelectedListener() {
            @Override
            public void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls) {
                LogF.d("走势图选号", "选号状态" + GsonUtil.getInstance().toJson(balls));
            }
        });
        mChooseNumFragment.setChooseNumScrollerListener(new TrendChooseNumFragment.ChooseNumScrollerListener() {
            @Override
            public void onScrollChanged(boolean isScrolling, int dx, int dy) {
                mTrendView.setMoveX(isScrolling, dx);
            }
        });
        mHandler.sendMessage(mHandler.obtainMessage(120, mDatas));
    }

    private void getTrendData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            LotteryTrendData lotteryTrendData = (LotteryTrendData) bundle.getSerializable("data");
            if (null != lotteryTrendData) {
                mNumPos = lotteryTrendData.getNumPos();
                fixData(lotteryTrendData);
            }
            int xItemWidth = bundle.getInt("xItemWidth");
            int leftTitleItemWidth = bundle.getInt("leftTitleItemWidth");
            int leftSecondRow = bundle.getInt("leftSecondRow", 0);
            mTrrendStyle = bundle.getString("trendStyle", AppConstants.TREND_STYLE_BASIC);
            mPaintColors = bundle.getIntegerArrayList("paintColors");
            mIsShowTitle = bundle.getBoolean("isShowTitle", true);
            if (xItemWidth > 0 && leftTitleItemWidth > 0)
                mTrendChart = new DDTrendChart(getActivity(), mTrendView, xItemWidth, leftTitleItemWidth, 0, mPaintColors);
            else {
                mTrendChart = new DDTrendChart(getActivity(), mTrendView);
            }
            mTrendChart.setShowTitle(mIsShowTitle);
            mTrendChart.drawLeftTop();
            mTrendChart.setDrawLine(bundle.getBoolean("isShowLine"));
            LogF.d(TAG, "走势图参数 lotteryType=" + mLotteryType + " playType" + mPlayType + "  xItemtWidth="
                    + xItemWidth + " leftTitleItemWidth=" + leftTitleItemWidth);
        } else {
            LogF.d(TAG, "参数为空" + GsonUtil.getInstance().toJson(bundle));
            mTrendChart = new DDTrendChart(getActivity(), mTrendView);
            mTrendChart.setDrawLine(true);
        }
        mTrendView.setChart(mTrendChart);
        mTrendChart.setLineColor(ContextCompat.getColor(mContext, R.color.color_red_ball));
        mTrendChart.setShowYilou(true);
        mTrendChart.setSelectedChangeListener(this);

        mTrendChart.setSelectedChangeListener(this);
        initChartTitle();
        if (FAST_THREE_2_DIFFERENT == mPlayType) {
            mTrendChart.setDrawLine(false);
        }
    }

    //拼接成需要的标题格式
    public String addTitle(List<String> nameTitles) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String title : nameTitles) {
            stringBuilder.append(title);
            stringBuilder.append(",");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return stringBuilder.toString();
    }

    //设置标题和左侧增加列
    public void initChartTitle() {

        //万位-十位颜色不一致
        switch (mNumPos) {
            case 2:
                mTrendChart.setLineColor(ContextCompat.getColor(mContext, R.color.color_trend_ssc_shi));
                break;
            case 3:
                mTrendChart.setLineColor(ContextCompat.getColor(mContext, R.color.color_trend_ssc_bai));
                break;
            case 4:
                mTrendChart.setLineColor(ContextCompat.getColor(mContext, R.color.color_trend_ssc_qian));
                break;
            case 5:
                mTrendChart.setLineColor(ContextCompat.getColor(mContext, R.color.color_trend_ssc_wan));
                break;
            default:
                mTrendChart.setLineColor(ContextCompat.getColor(mContext, R.color.color_red_ball));
                break;
        }

        if (null != mTitles && null != mLeftTitles) {
            mTrendChart.setBlueCount(mTitles.size());
            String tempTitle = addTitle(mTitles);
            mTrendChart.setXTitles(tempTitle);
            String[] left = new String[mLeftTitles.size()];
            mLeftTitles.toArray(left);
            mTrendChart.setAddCountString(left);
        }

    }

    private void fixData(LotteryTrendData lotteryTrendData) {
        mLotteryType = lotteryTrendData.getLotteryType();
        mPlayType = lotteryTrendData.getPlayType();
        LogF.d(TAG, "走势图 页面位置" + mNumPos);
        switch (mLotteryType) {
            case AppConstants.LOTTERY_TYPE_FAST_3:
            case AppConstants.LOTTERY_TYPE_FAST_3_JS:
            case AppConstants.LOTTERY_TYPE_FAST_3_HB:
            case AppConstants.LOTTERY_TYPE_FAST_3_EASY:
            case AppConstants.LOTTERY_TYPE_FAST_3_NEW:
                mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType + "_" + mPlayType);
                mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType + "_" + mPlayType);
                mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType + "_" + mPlayType);
                break;
            case AppConstants.LOTTERY_TYPE_11_5:
            case AppConstants.LOTTERY_TYPE_11_5_OLD:
            case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
            case AppConstants.LOTTERY_TYPE_11_5_YILE:
            case AppConstants.LOTTERY_TYPE_11_5_YUE:
                switch (mPlayType) {
                    //前一,前二,前三,走势数据
                    case AppConstants.PLAY_11_5_FRONT_1_DIRECT:
                    case AppConstants.PLAY_11_5_FRONT_2_DIRECT:
                    case AppConstants.PLAY_11_5_FRONT_3_DIRECT:
                            LogF.d("222", "11选5直选 mLotteryType==>"+mLotteryType+" ,mNumPos==>"+mNumPos);
                            mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType + "_" + mNumPos);
                            LogF.d("222", "11选5直选mDatas" + GsonUtil.getInstance().toJson(mDatas));

                            mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType + "_" + mNumPos);
                            LogF.d("222", "11选5直选mLeftTitles" + GsonUtil.getInstance().toJson(mLeftTitles));

                            mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType + "_" + mNumPos);
                            LogF.d("222", "11选5直选mTitles" + GsonUtil.getInstance().toJson(mTitles));
                        break;

                    case AppConstants.PLAY_11_5_FRONT_2_GROUP://组选数据
                    case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
                    case AppConstants.PLAY_11_5_FRONT_3_GROUP:
                    case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                        LogF.d("222", "11选5组选 mLotteryType==>"+mLotteryType+" ,mNumPos==>"+mNumPos);
                        mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType + "_"
                                + mPlayType + "_" + mNumPos);
                        mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType
                                + "_" + mPlayType + "_4");
                        mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType + "_"
                                + mPlayType + "_4");
                        break;
                }
                break;
            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR:
            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                switch (mPlayType) {
                    case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                        mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType
                                + "_" + mNumPos);
                        mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType
                                + "_" + mNumPos);
                        mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType
                                + "_" + mNumPos);
                        break;
                    case AppConstants.ALWAYS_COLOR_1_DIRECT:
                        if (mNumPos == -1) {//个位振幅
                            mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType
                                    + "_" + -1);
                            mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType
                                    + "_" + -1);
                            mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType
                                    + "_" + -1);
                        } else {//个位走势
                            mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType
                                    + "_" + 1);
                            mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType
                                    + "_" + 1);
                            mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType
                                    + "_" + 1);
                        }
                        break;
                    case AppConstants.ALWAYS_COLOR_2_DIRECT:
                    case AppConstants.ALWAYS_COLOR_3_DIRECT:
                    case AppConstants.ALWAYS_COLOR_5_DIRECT:
                    case AppConstants.ALWAYS_COLOR_5_ALL:
                        mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType
                                + "_" + mNumPos);
                        mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType
                                + "_" + mNumPos);
                        mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType
                                + "_" + mNumPos);
                        break;
                    case AppConstants.ALWAYS_COLOR_2_GROUP:
                    case AppConstants.ALWAYS_COLOR_3_GROUP_3:
                    case AppConstants.ALWAYS_COLOR_3_GROUP_6:
                        mDatas = lotteryTrendData.getTypeData().getTrendDatas().get(mLotteryType
                                + "_" + mPlayType + "_" + mNumPos);
                        mLeftTitles = lotteryTrendData.getTypeData().getTrendLeftTitles().get(mLotteryType
                                + "_" + mPlayType + "_" + mNumPos);
                        mTitles = lotteryTrendData.getTypeData().getTrendTitles().get(mLotteryType
                                + "_" + mPlayType + "_" + mNumPos);
                        break;
                }
                break;
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLotteryPlayType(LotteryTrendData data) {
        if (null != data) {
            fixData(data);
            initChartTitle();
            setSettingCache(LotteryUtils.getTrendSettingCache(mLotteryType));
            mChooseNumFragment.updateView(mLotteryType, mPlayType, mNumPos);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingChangeEvent settingEvent) {
        if (!settingEvent.isNeedRefresh()) {//获取期次不刷新
            TrendSettingBean trendSettingBean = settingEvent.getTrendSettingBean();
            setSettingCache(trendSettingBean);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //        ((TrendActivity)this.getActivity()).registerMyTouchListener(myTouchListener);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSelectedChange(TreeSet<Integer> treeSet, TreeSet<Integer> treeSet2) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            setSettingUI();
        }
    }

    public void setSettingUI() {
        TrendSettingUIChangeEvent event = new TrendSettingUIChangeEvent();
        switch (mPlayType) {
            case AppConstants.FAST_THREE_2_DIFFERENT://不需要折线
                event.setNeedShowShowLine(false);
                break;
            case AppConstants.ALWAYS_COLOR_2_GROUP:
            case AppConstants.ALWAYS_COLOR_3_GROUP_3:
            case AppConstants.ALWAYS_COLOR_3_GROUP_6://不需要折线
            case AppConstants.PLAY_11_5_FRONT_2_GROUP://组选数据
            case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
            case AppConstants.PLAY_11_5_FRONT_3_GROUP:
            case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                if (mNumPos == -2) {
                    event.setNeedShowShowLine(false);
                } else {
                    event.setNeedShowShowLine(true);
                }
                break;
            default:
                event.setNeedShowShowLine(true);
                break;
        }
        if (FAST_THREE_2_DIFFERENT == mPlayType) {
        } else {
        }
        event.setNeedShowShowYilou(true);
        event.setNeedShowShowStatistic(true);
        EventBus.getDefault().post(event);
    }

    //设置的缓存
    public void setSettingCache(TrendSettingBean trendSettingBean) {
        switch (mPlayType) {
            case AppConstants.FAST_THREE_2_DIFFERENT://不需要折线
                mTrendChart.setDrawLine(false);
                break;
            case AppConstants.ALWAYS_COLOR_2_GROUP:
            case AppConstants.ALWAYS_COLOR_3_GROUP_3:
            case AppConstants.ALWAYS_COLOR_3_GROUP_6://不需要折线
            case AppConstants.PLAY_11_5_FRONT_2_GROUP://组选数据
            case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
            case AppConstants.PLAY_11_5_FRONT_3_GROUP:
            case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                if (mNumPos == -2) {//走势
                    mTrendChart.setDrawLine(false);
                } else {
                    if (!trendSettingBean.isShowLine()) {
                        mTrendChart.setDrawLine(false);
                    } else {
                        mTrendChart.setDrawLine(true);
                    }
                }
                break;
            default:
                if (!trendSettingBean.isShowLine()) {
                    mTrendChart.setDrawLine(false);
                } else {
                    mTrendChart.setDrawLine(true);
                }
                break;
        }
        if (!trendSettingBean.isShowYilou()) {
            mTrendChart.setShowYilou(false);
        } else {
            mTrendChart.setShowYilou(true);
        }
        List<TrendData> tempData = new ArrayList<>();
        if (!trendSettingBean.isShowStatistic()) {
            mTrendChart.setHideLastFourth(true);
            tempData.addAll(mDatas);
            for (int i = 0; i < 4; i++) {
                tempData.remove(tempData.size() - 1);
            }
        } else {
            mTrendChart.setHideLastFourth(false);
        }
        if (!trendSettingBean.isShowStatistic()) {
            mHandler.sendMessage(mHandler.obtainMessage(120, tempData));
        } else {
            mHandler.sendMessage(mHandler.obtainMessage(120, mDatas));
        }
    }

}
