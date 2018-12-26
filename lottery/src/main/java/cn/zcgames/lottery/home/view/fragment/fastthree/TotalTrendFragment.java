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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.TrendData;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.home.view.fragment.ui.DDTrendChart;
import cn.zcgames.lottery.home.view.fragment.ui.LottoTrendView;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

public class TotalTrendFragment extends BaseFragment implements DDTrendChart.ISelectedChangeListener {
    private LottoTrendView trendView;
    private DDTrendChart mTrendChart;
    private List<TrendData> datas;
    private String[] names = {};
    private String titles = "3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18";
    private List<TrendResponseData.TrendData> missDatas;
    private int playType;
    private String lotteryType;
    private TrendChooseNumFragment chooseNumFragment;
    //    private TrendActivity.MyTouchListener myTouchListener;
    private float mLastX;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            mTrendChart.updateData("01", (ArrayList) paramMessage.obj);
        }
    };

    public static TotalTrendFragment newInstance(String lotteryType, int playType, List<TrendResponseData.TrendData> datas) {
        TotalTrendFragment basicFragment = new TotalTrendFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lotteryType", lotteryType);
        bundle.putInt("playType", playType);
        bundle.putSerializable("data", (Serializable) datas);
        basicFragment.setArguments(bundle);
        return basicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View totalView = inflater.inflate(R.layout.fragment_total_trend, container, false);
        EventBus.getDefault().register(this);
        initView(totalView);
        initChart();
        setData();
        setSettingCache(LotteryUtils.getTrendSettingCache(lotteryType));
        setSettingUI();
        return totalView;
    }

    public void initView(View view) {
        trendView = view.findViewById(R.id.lottery_trend_view);
        /*trendView.setZOrderOnTop(false);
        trendView.setZOrderMediaOverlay(true);
        trendView.getHolder().setFormat(PixelFormat.TRANSLUCENT);*/

        trendView.setMoveXListener(new LottoTrendView.SetMoveXPositionListener() {
            @Override
            public void setMoveX(int xPosition) {
//                LogF.d("滑动", "偏移量   " + xPosition);
                chooseNumFragment.scroll(-xPosition);
            }
        });
    }

    public void initChart() {
        mTrendChart = new DDTrendChart(getActivity(), trendView);
        trendView.setChart(mTrendChart);
        mTrendChart.setShowYilou(true);
        mTrendChart.setDrawLine(true);
        String[] nums = titles.split(",");
        mTrendChart.setBlueCount(nums.length);
        mTrendChart.setXTitles(titles);
        mTrendChart.setAddCountString(names);
        mTrendChart.setLineColor(ContextCompat.getColor(mContext, R.color.color_red_ball));
        mTrendChart.setSelectedChangeListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            lotteryType = bundle.getString("lotteryType");
            playType = bundle.getInt("playType");
            missDatas = (List<TrendResponseData.TrendData>) bundle.getSerializable("data");
        }

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        chooseNumFragment = TrendChooseNumFragment.newInstance(lotteryType, playType, 0, DeviceUtils.dpToPx(getActivity(), 63), DeviceUtils.dpToPx(getActivity(), 35), 0);
        transaction
                .add(R.id.fl_total_trend_select_num, chooseNumFragment)
                .show(chooseNumFragment)
                .commit();
        chooseNumFragment.setSelectLisenter(new TrendChooseNumAdapter.ChooseNumSelectedListener() {
            @Override
            public void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls) {
                LogF.d("走势图选号", "选号状态" + GsonUtil.getInstance().toJson(balls));
            }
        });
        chooseNumFragment.setChooseNumScrollerListener(new TrendChooseNumFragment.ChooseNumScrollerListener() {
            @Override
            public void onScrollChanged(boolean isScrolling, int dx, int dy) {
                trendView.setMoveX(isScrolling, dx);
            }
        });
        /** 接收MainActivity的Touch回调的对象，重写其中的onTouchEvent函数 */
//        myTouchListener = new TrendActivity.MyTouchListener() {
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//                //处理手势事件（根据个人需要去返回和逻辑的处理）
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mLastX = event.getX();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float x = event.getX();
//                        int dx = (int) (x - mLastX);
////                        int dy = (int) (y-mLastY);
////                        LogF.d("111111", "X轴偏移量  " + dx);
//                        /*if(dy > dx){
//                            chooseNumFragment.scroll(0);
//                        }else{
//                            chooseNumFragment.scroll(-dx);
//                        }*/
////                        chooseNumFragment.scroll(-dx);
//
////                        LogF.d("111111", "X轴偏移量  " + dx);
////                        chooseNumFragment.scroll(-dx);
//                        mLastX = x;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//
//                        break;
//                }
//            }
//        };
//        ((TrendActivity)this.getActivity()).registerMyTouchListener(myTouchListener);
    }


    public void setData() {

        datas = new ArrayList<>();
        parseData(missDatas);
    }

    public void parseData(List<TrendResponseData.TrendData> missDatas) {
        for (int i = 0; i < missDatas.size(); i++) {
            TrendResponseData.TrendData missBean = missDatas.get(i);
            String winNumber = "", period = "", times = "";
            if (missBean != null) {
                period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
                for (int j = 0; j < missBean.getWinnerNumber().size(); j++) {
                    if (j == missBean.getWinnerNumber().size() - 1) {
                        winNumber = winNumber + missBean.getWinnerNumber().get(j);
                    } else {
                        winNumber = winNumber + missBean.getWinnerNumber().get(j) + " ";
                    }
                }
                if (missBean.getMissNumber().getSum().size() > 0) {
                    for (int j = 0; j < missBean.getMissNumber().getSum().size(); j++) {
                        if (j == missBean.getMissNumber().getSum().size() - 1) {
                            times = times + missBean.getMissNumber().getSum().get(j);
                        } else {
                            times = times + missBean.getMissNumber().getSum().get(j) + ",";
                        }
                    }
                } else {
                    times = "等待开奖";
                }

                TrendData trendData = new TrendData();
                trendData.setPid(period);
                trendData.setType("row");
                trendData.setBlue(times);
                datas.add(trendData);
            }
        }
        frequencyTimes(missDatas);
        handler.sendMessage(handler.obtainMessage(120, datas));
    }

    public void frequencyTimes(List<TrendResponseData.TrendData> missDatas) {
        int[] showTimes = new int[titles.length()];
        int[] tempAvgMiss = new int[titles.length()];
        int[] avgMiss = new int[titles.length()];
        int[] maxMiss = new int[titles.length()];
        int[] maxContinue = new int[titles.length()];
        int[] tempContinue = new int[titles.length()];

//        TrendResponseData.TrendData lastBean = missDatas.get(missDatas.size()-1);
//        if(lastBean.getMissNumber().getSum().size() > 0){

        for (int i = 0; i < missDatas.size(); i++) {
            TrendResponseData.TrendData missBean = missDatas.get(i);
            List<String> numbers = missBean.getMissNumber().getSum();
            for (int j = 0; j < numbers.size(); j++) {
                //出现次数
                if (TextUtils.equals("0", numbers.get(j))) {
                    showTimes[j]++;
                }
                //最大遗漏
                if (Integer.parseInt(numbers.get(j)) > maxMiss[j]) {
                    maxMiss[j] = Integer.parseInt(numbers.get(j));
//                        tempAvgMiss[j] += maxMiss[j];
                }
                //最大连出
                if (TextUtils.equals("0", numbers.get(j))) {
                    tempContinue[j]++;
                    if (maxContinue[j] < tempContinue[j]) {
                        maxContinue[j] = tempContinue[j];
                    }
                } else {
                    tempContinue[j] = 0;
                }
                //平均遗漏
                avgMiss[j] += Integer.parseInt(numbers.get(j));
            }

        }
        for (int i = 0; i < avgMiss.length; i++) {
            avgMiss[i] = avgMiss[i] / datas.size();
        }

        Log.d("111111", "出现次数==" + showTimes.toString() + "最大遗漏==" + maxMiss.toString() + "平均遗漏==" + avgMiss.toString() + "连出=" + maxContinue.toString());
        ArrayList<TrendData> basicData = new ArrayList<>();
        String showTime2 = "", avgMiss2 = "", maxMiss2 = "", maxContinue2 = "";
        showTime2 = arrayToString(showTime2, showTimes);
        avgMiss2 = arrayToString(avgMiss2, avgMiss);
        maxMiss2 = arrayToString(maxMiss2, maxMiss);
        maxContinue2 = arrayToString(maxContinue2, maxContinue);
        String[] tabs = {showTime2, avgMiss2, maxMiss2, maxContinue2};
        String[] types = {"dis", "avg", "mmv", "mlv"};
        for (int j = 0; j < tabs.length; j++) {
            TrendData trendData = new TrendData();
            trendData.setBlue(tabs[j]);
            trendData.setType(types[j]);
            basicData.add(trendData);
        }
        datas.addAll(basicData);
//        }

    }

    public String arrayToString(String name, int[] names) {
        for (int j = 0; j < names.length; j++) {
            if (j == names.length - 1) {
                name = name + names[j];
            } else {
                name = name + names[j] + ",";
            }
        }
        return name;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLotteryPlayType(LotteryTrendData data) {
        if (data != null) {
            missDatas = data.getData();
            datas.clear();
            parseData(missDatas);
            setSettingCache(LotteryUtils.getTrendSettingCache(lotteryType));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingChangeEvent settingEvent) {
        TrendSettingBean trendSettingBean = settingEvent.getTrendSettingBean();
        setSettingCache(trendSettingBean);
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
            //选号部分
            chooseNumFragment.updateView(lotteryType, playType, 0);
        }
    }

    public void setSettingUI() {
        TrendSettingUIChangeEvent event = new TrendSettingUIChangeEvent();
        event.setNeedShowShowLine(true);
        event.setNeedShowShowYilou(true);
        event.setNeedShowShowStatistic(true);
        EventBus.getDefault().post(event);
    }

    //设置的缓存
    public void setSettingCache(TrendSettingBean trendSettingBean) {
        if (!trendSettingBean.isShowLine()) {
            mTrendChart.setDrawLine(false);
        } else {
            mTrendChart.setDrawLine(true);
        }
        if (!trendSettingBean.isShowYilou()) {
            mTrendChart.setShowYilou(false);
        } else {
            mTrendChart.setShowYilou(true);
        }
        List<TrendData> tempData = new ArrayList<>();
        if (!trendSettingBean.isShowStatistic()) {
            mTrendChart.setHideLastFourth(true);
            tempData.addAll(datas);
            for (int i = 0; i < 4; i++) {
                tempData.remove(tempData.size() - 1);
            }
        } else {
            mTrendChart.setHideLastFourth(false);
        }
        if (!trendSettingBean.isShowStatistic()) {
            handler.sendMessage(handler.obtainMessage(120, tempData));
        } else {
            handler.sendMessage(handler.obtainMessage(120, datas));
        }
    }

}
