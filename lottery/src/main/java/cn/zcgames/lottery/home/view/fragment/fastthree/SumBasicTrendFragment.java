package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
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
import java.util.Arrays;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.bean.BasicTrendBean;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.view.adapter.SumBasicAdapter;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

public class SumBasicTrendFragment extends BaseFragment {
    private RecyclerView mRVShow;
    private SumBasicAdapter basicAdapter;
    private List<BasicTrendBean> awardDatas;
    private int playType;
    private String lotteryType;
    private List<TrendResponseData.TrendData> datas;
    private TrendChooseNumFragment chooseNumFragment;
    private boolean isShowBottom = true;


    public static SumBasicTrendFragment newInstance(String lotteryType, int playType, List<TrendResponseData.TrendData> datas){
        SumBasicTrendFragment basicFragment = new SumBasicTrendFragment();
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
        View basicView = inflater.inflate(R.layout.fragment_sum_basic_trend, container, false);
        EventBus.getDefault().register(this);
        initView(basicView);
        initData();
        setSettingCache(LotteryUtils.getTrendSettingCache(lotteryType));
        initListener();
        setSettingUI();
        return basicView;
    }

    private void initListener() {

    }

    public void initView(View view){
        mRVShow = view.findViewById(R.id.rv_basic_trend);

        Bundle bundle = getArguments();
        if(bundle != null){
            lotteryType = bundle.getString("lotteryType");
            playType = bundle.getInt("playType");
            datas = (List<TrendResponseData.TrendData>) bundle.getSerializable("data");
        }

        //选号部分
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        chooseNumFragment = TrendChooseNumFragment.newInstance(lotteryType, playType, 0);
        transaction
                .add(R.id.fl_basic_select_num, chooseNumFragment)
                .show(chooseNumFragment)
                .commit();
        chooseNumFragment.setSelectLisenter(new TrendChooseNumAdapter.ChooseNumSelectedListener() {
            @Override
            public void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls) {
                LogF.d("走势图选号", "选号状态" + GsonUtil.getInstance().toJson(balls));
            }
        });
    }

    private void initData() {

        awardDatas = new ArrayList<>();
        parseData();

        basicAdapter = new SumBasicAdapter(getActivity());
        setFillData();
        basicAdapter.showLastView(isShowBottom);
        mRVShow.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        mRVShow.setAdapter(basicAdapter);
        mRVShow.scrollToPosition(awardDatas.size()-1);
    }

    public void setFillData(){
        basicAdapter.setDataFun(awardDatas);
        if(isShowBottom){
            basicAdapter.setDataSize(awardDatas.size()+1);
        }else{
            basicAdapter.setDataSize(awardDatas.size());
        }
    }

    public void parseData(){
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData missBean = datas.get(i);
            String winNumber ="", period = "", times = "";
            int sum = 0,diffValue = 0;
            if(missBean != null){
                period = missBean.getPeriod().substring(missBean.getPeriod().length()-2, missBean.getPeriod().length()) + "期";
                if(missBean.getWinnerNumber().size() > 0){
                    for (int j = 0; j < missBean.getWinnerNumber().size(); j++) {
                        if(j == missBean.getWinnerNumber().size() - 1){
                            winNumber = winNumber+missBean.getWinnerNumber().get(j);
                        }else{
                            winNumber = winNumber+missBean.getWinnerNumber().get(j)+",";
                        }
                        sum+=Integer.parseInt(missBean.getWinnerNumber().get(j));
                    }
                    int one = 0, two = 0, three = 0;
                    one = Integer.parseInt(missBean.getWinnerNumber().get(0));
                    two = Integer.parseInt(missBean.getWinnerNumber().get(1));
                    three = Integer.parseInt(missBean.getWinnerNumber().get(2));
                    int[] nums = {one, two, three};
                    Arrays.sort(nums);
                    diffValue = nums[nums.length-1] - nums[0];
                }else{
                    winNumber = "等待开奖";
                }
                if(missBean.getMissNumber().getShuzi().size() > 0){
                    for (int j = 0; j < missBean.getMissNumber().getShuzi().size(); j++) {
                        if(j == missBean.getMissNumber().getShuzi().size() - 1){
                            times = times+missBean.getMissNumber().getShuzi().get(j);
                        }else{
                            times = times+missBean.getMissNumber().getShuzi().get(j)+",";
                        }
                    }
                }else{
                    times = "";
                }

                BasicTrendBean basicBean = new BasicTrendBean();
                basicBean.periods = period;
                basicBean.sum = sum;
                basicBean.differ = diffValue;
                basicBean.numbers = winNumber;
                basicBean.singleNumbers = times;
                awardDatas.add(basicBean);
            }
        }
        isShowBottom = true;
        frequencyTimes();
    }

    public void frequencyTimes(){
        int[] showTimes = {0,0,0,0,0,0};
        int[] avgMiss = {0,0,0,0,0,0};
        int[] maxMiss = {0,0,0,0,0,0};
        int[] maxContinue = {0,0,0,0,0,0};
        int[] tempContinue = {0,0,0,0,0,0};
        boolean isCalcuData = true;
//        TrendResponseData.TrendData lastBean = datas.get(datas.size()-1);
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData lastBean = datas.get(i);
            if(lastBean.getMissNumber().getShuzi().size() == 0){
                isCalcuData = false;
            }
        }
        if(isCalcuData){

            for (int i = 0; i < datas.size(); i++) {
                TrendResponseData.TrendData missBean = datas.get(i);
                List<String> numbers = missBean.getMissNumber().getShuzi();

                for (int j = 0; j < numbers.size(); j++) {
                    //出现次数
                    if(TextUtils.equals("0", numbers.get(j))){
                        showTimes[j]++;
                    }
                    //最大遗漏
                    if( Integer.parseInt(numbers.get(j)) > maxMiss[j]){
                        maxMiss[j] = Integer.parseInt(numbers.get(j));
//                        tempAvgMiss[j] += maxMiss[j];
                    }
                    //最大连出
                    if(TextUtils.equals("0", numbers.get(j))){
                        tempContinue[j]++;
                        if(maxContinue[j]<tempContinue[j]){
                            maxContinue[j] = tempContinue[j];
                        }
                    }else{
                        tempContinue[j] = 0;
                    }
                }
            }
            //平均遗漏
            avgMiss = calculateAvg(showTimes);

            Log.d("111111","出现次数=="+showTimes.toString()+"最大遗漏=="+maxMiss.toString()+"平均遗漏=="+avgMiss.toString()+"连出="+maxContinue.toString());
            ArrayList<BasicTrendBean> basicData = new ArrayList<>();
            String showTime2="", avgMiss2="", maxMiss2="", maxContinue2="";
            showTime2 = arrayToString(showTime2,showTimes);
            avgMiss2 = arrayToString(avgMiss2,avgMiss);
            maxMiss2 = arrayToString(maxMiss2,maxMiss);
            maxContinue2 = arrayToString(maxContinue2,maxContinue);
            String[] tabs = {showTime2, avgMiss2, maxMiss2, maxContinue2};
            for (int j = 0; j < tabs.length; j++) {
                BasicTrendBean basicTrendBean = new BasicTrendBean();
                basicTrendBean.singleNumbers = tabs[j];
                basicData.add(basicTrendBean);
            }
            awardDatas.addAll(basicData);
            isShowBottom  = false;
        }

    }

    public String arrayToString(String name, int[] names){
        for (int j = 0; j < names.length; j++) {
            if(j == names.length - 1){
                name = name+names[j];
            }else{
                name = name+names[j]+",";
            }
        }
        return name;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLotteryPlayType(LotteryTrendData data){
        if (data!=null) {
            datas = data.getData();
            lotteryType = data.getLotteryType();
            playType = data.getPlayType();
            awardDatas.clear();
            parseData();
            basicAdapter.showLastView(isShowBottom);
            setSettingCache(LotteryUtils.getTrendSettingCache(lotteryType));
            mRVShow.scrollToPosition(awardDatas.size()-1);
            chooseNumFragment.updateView(lotteryType, playType, 0);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        }else{
            setSettingUI();
            //选号部分
            chooseNumFragment.updateView(lotteryType, playType, 0);
        }
    }

    public void setSettingUI(){
        TrendSettingUIChangeEvent event = new TrendSettingUIChangeEvent();
        event.setNeedShowShowYilou(true);
        event.setNeedShowShowStatistic(true);
        EventBus.getDefault().post(event);
    }

    //拿取缓存的内容
    public void setSettingCache(TrendSettingBean trendSettingBean){
        if(!trendSettingBean.isShowYilou()){
            basicAdapter.showMissNum(false);
        }else{
            basicAdapter.showMissNum(true);
        }
        if(!trendSettingBean.isShowStatistic()){
            List<BasicTrendBean> tempData = new ArrayList<>();
            tempData.addAll(awardDatas);
            basicAdapter.hideLastFourth(true);
            //false的时候去掉最后四行
            if(isShowBottom){
                basicAdapter.setDataSize(tempData.size());
            }else{
                for (int i = 0; i < 4; i++) {
                    tempData.remove(tempData.size()-1);
                }
                basicAdapter.setDataSize(tempData.size());
            }
            basicAdapter.setDataFun(tempData);
            mRVShow.scrollToPosition(tempData.size()-1);
        }else{
            basicAdapter.hideLastFourth(false);
            setFillData();
            mRVShow.scrollToPosition(awardDatas.size()-1);
        }
        basicAdapter.notifyDataSetChanged();
    }

    //计算平均遗漏
    public int[] calculateAvg(int[] showTime){
        int[] num = new int[6];
        int[] sum = new int[6];
        int[] tempNumber = new int[6];
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData missBean = datas.get(i);
            List<String> numbers = missBean.getMissNumber().getShuzi();
            for (int j = 0; j < num.length; j++) {
                if(Integer.parseInt(numbers.get(j))>tempNumber[j]){
                    tempNumber[j] = Integer.parseInt(numbers.get(j));
                }else if(0 == Integer.parseInt(numbers.get(j))){
                    sum[j] += tempNumber[j];
                    tempNumber[j] = 0;
                }
            }
        }

        //求和
        for (int i = 0; i < num.length; i++) {
            if(tempNumber[i] > 0){
                sum[i]+=tempNumber[i];
            }
            Log.d("111111", "求出的和是："+sum[i]);
            num[i] = sum[i]/(showTime[i] + 1);
            Log.d("111111", "求出的平均值是："+num[i]);
        }
        return num;
    }

}
