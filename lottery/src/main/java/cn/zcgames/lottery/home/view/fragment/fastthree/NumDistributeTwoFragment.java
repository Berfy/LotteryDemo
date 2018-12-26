package cn.zcgames.lottery.home.view.fragment.fastthree;

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

public class NumDistributeTwoFragment extends BaseFragment implements DDTrendChart.ISelectedChangeListener {
    private LottoTrendView trendView;
    private DDTrendChart mTrendChart;
    private List<TrendData> datas;
    private String[] names = {"开奖号"};
    private String titles = "12,13,14,15,16,23,24,25,26,34,35,36,45,46,56";
    private List<TrendResponseData.TrendData> missDatas;
    private int playType;
    private String lotteryType;
    private TrendChooseNumFragment chooseNumFragment;

    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            super.handleMessage(paramMessage);
            mTrendChart.updateData("01", (ArrayList) paramMessage.obj);
        }
    };

    public static NumDistributeTwoFragment newInstance(String lotteryType, int playType, List<TrendResponseData.TrendData> datas) {
        NumDistributeTwoFragment basicFragment = new NumDistributeTwoFragment();
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
        View totalView = inflater.inflate(R.layout.fragment_distrbute_two, container, false);
        EventBus.getDefault().register(this);
        initView(totalView);
        initChart();
        setData();
        setSettingCache(LotteryUtils.getTrendSettingCache(lotteryType));
        setSettingUI();
        return totalView;
    }

    public void initView(View view) {
        trendView = view.findViewById(R.id.lottery_distrbute_view);
        /*trendView.setZOrderOnTop(false);
        trendView.getHolder().setFormat(PixelFormat.TRANSLUCENT);*/

    }

    public void initChart() {
        mTrendChart = new DDTrendChart(getActivity(), trendView);
        trendView.setChart(mTrendChart);
        mTrendChart.setShowYilou(true);
        mTrendChart.setDrawLine(false);
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
        chooseNumFragment = TrendChooseNumFragment.newInstance(lotteryType, playType, 0);
        transaction
                .add(R.id.fl_dis_two_select_num, chooseNumFragment)
                .show(chooseNumFragment)
                .commit();
        chooseNumFragment.setSelectLisenter(new TrendChooseNumAdapter.ChooseNumSelectedListener() {
            @Override
            public void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls) {
                LogF.d("走势图选号", "选号状态" + GsonUtil.getInstance().toJson(balls));
            }
        });

    }

    public void setData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            missDatas = (List<TrendResponseData.TrendData>) bundle.getSerializable("data");
        }
        datas = new ArrayList<>();
        parseData();
    }

    public void parseData() {
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
                for (int j = 0; j < missBean.getMissNumber().getErbutong().size(); j++) {
                    if (j == missBean.getMissNumber().getErbutong().size() - 1) {
                        times = times + missBean.getMissNumber().getErbutong().get(j);
                    } else {
                        times = times + missBean.getMissNumber().getErbutong().get(j) + ",";
                    }
                }

                TrendData trendData = new TrendData();
                trendData.setPid(period);
                trendData.setWinNumbers(winNumber);
                trendData.setType("row");
                trendData.setBlue(times);
                datas.add(trendData);
            }
        }
        frequencyTimes();
        handler.sendMessage(handler.obtainMessage(120, datas));
    }

    public void frequencyTimes() {
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
            List<String> numbers = missBean.getMissNumber().getErbutong();
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
        //平均遗漏
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
        if (datas != null) {
            missDatas = data.getData();
            datas.clear();
            parseData();
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
        event.setNeedShowShowYilou(true);
        event.setNeedShowShowStatistic(true);
        EventBus.getDefault().post(event);
    }

    //设置的缓存
    public void setSettingCache(TrendSettingBean trendSettingBean) {
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
        mTrendChart.setDrawLine(false);
        if (!trendSettingBean.isShowStatistic()) {
            handler.sendMessage(handler.obtainMessage(120, tempData));
        } else {
            handler.sendMessage(handler.obtainMessage(120, datas));
        }
    }
}
