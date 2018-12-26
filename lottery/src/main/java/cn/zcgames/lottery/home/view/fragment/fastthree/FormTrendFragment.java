package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.FormTrendBean;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.view.adapter.FormTrendAdapter;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

public class FormTrendFragment extends BaseFragment {
    private RecyclerView mRVForm;
    private List<FormTrendBean> formDatas;
    private FormTrendAdapter formAdapter;
    private int playType;
    private String lotteryType;
    private List<TrendResponseData.TrendData> datas;
    private TrendChooseNumFragment chooseNumFragment;
    private boolean isShowBottom = true;


    public static FormTrendFragment newInstance(String lotteryType, int playType, List<TrendResponseData.TrendData> datas){
        FormTrendFragment awardFragment = new FormTrendFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lotteryType", lotteryType);
        bundle.putInt("playType", playType);
        bundle.putSerializable("data", (Serializable) datas);
        awardFragment.setArguments(bundle);
        return awardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View formView = inflater.inflate(R.layout.fragment_form_trend, container, false);
        EventBus.getDefault().register(this);
        initView(formView);
        initData();
        setSettingCache(LotteryUtils.getTrendSettingCache(lotteryType));
        setSettingUI();
        return formView;
    }

    private void initView(View view) {
        mRVForm = view.findViewById(R.id.rv_form_trend);

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
                .add(R.id.fl_form_trend_select_num, chooseNumFragment)
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
        formDatas = new ArrayList<>();

        parseData();
        mRVForm.setLayoutManager(new LinearLayoutManager(getActivity()));
        formAdapter = new FormTrendAdapter(getActivity(),"三");
        setFillData();
        formAdapter.showLastView(isShowBottom);
        mRVForm.setAdapter(formAdapter);
        mRVForm.scrollToPosition(formDatas.size()-1);
    }

    //填充数据
    public void setFillData(){
        formAdapter.setDataFun(formDatas);
        if(isShowBottom){
            formAdapter.setDataSize(formDatas.size()+1);
        }else{
            formAdapter.setDataSize(formDatas.size());
        }
    }

    public void parseData(){
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData missBean = datas.get(i);
            String winNumber ="", period = "", times = "";
            if(missBean != null){
                period = missBean.getPeriod().substring(missBean.getPeriod().length()-2, missBean.getPeriod().length());
                if(missBean.getWinnerNumber().size() > 0){
                    for (int j = 0; j < missBean.getWinnerNumber().size(); j++) {
                        winNumber = winNumber+missBean.getWinnerNumber().get(j)+" ";
                    }
                }else{
                    winNumber = "等待开奖";
                }
                if(missBean.getMissNumber().getXingtai().size() > 0){
                    for (int j = 0; j < missBean.getMissNumber().getXingtai().size(); j++) {
                        if(j == missBean.getMissNumber().getXingtai().size() - 1){
                            times = times+missBean.getMissNumber().getXingtai().get(j);
                        }else{
                            if(j != 2 && j != 4){
                                times = times+missBean.getMissNumber().getXingtai().get(j)+",";
                            }
                        }
                    }
                }else{
                    times = "";
                }

                FormTrendBean formBean = new FormTrendBean();
                formBean.setPeriod(period+"期");
                formBean.setNumbers(winNumber);
                formBean.setTimes(times);
                formDatas.add(formBean);
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
                List<String> numbers = missBean.getMissNumber().getXingtai();
                //出现次数
                for (int j = 0; j < numbers.size(); j++) {
                    if(TextUtils.equals("0", numbers.get(j))){
                        showTimes[j]++;
                    }
                }
                //最大遗漏
                for (int j = 0; j < numbers.size(); j++) {
                    if( Integer.parseInt(numbers.get(j)) > maxMiss[j]){
                        maxMiss[j] = Integer.parseInt(numbers.get(j));
                    }
                }
                //最大连出
                for (int j = 0; j < numbers.size(); j++) {
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
            avgMiss = calculateAvg(6, showTimes);

            Log.d("111111","出现次数=="+showTimes.toString()+"最大遗漏=="+maxMiss.toString()+"平均遗漏=="+avgMiss.toString()+"连出="+maxContinue.toString());
            ArrayList<FormTrendBean> basicData = new ArrayList<>();
            String showTime2="", avgMiss2="", maxMiss2="", maxContinue2="";
            showTime2 = arrayToString(showTime2,showTimes);
            avgMiss2 = arrayToString(avgMiss2,avgMiss);
            maxMiss2 = arrayToString(maxMiss2,maxMiss);
            maxContinue2 = arrayToString(maxContinue2,maxContinue);
            String[] tabs = {showTime2, avgMiss2, maxMiss2, maxContinue2};
            for (int j = 0; j < tabs.length; j++) {
                FormTrendBean formTrendBean = new FormTrendBean();
                formTrendBean.setTimes(tabs[j]);
                basicData.add(formTrendBean);
            }
            formDatas.addAll(basicData);
            isShowBottom  = false;
        }

    }

    public String arrayToString(String name, int[] names){
        for (int j = 0; j < names.length; j++) {
            if(j == names.length - 1){
                name = name+names[j];
            }else{
                if(j != 2 && j != 4){
                    name = name+names[j]+",";
                }
            }
        }
        return name;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLotteryPlayType(LotteryTrendData data){
        if (datas != null) {
            datas = data.getData();
            lotteryType = data.getLotteryType();
            playType = data.getPlayType();
            formDatas.clear();
            parseData();
            formAdapter.showLastView(isShowBottom);
            setSettingCache(LotteryUtils.getTrendSettingCache(lotteryType));
            mRVForm.scrollToPosition(formDatas.size()-1);
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

    //设置的缓存
    public void setSettingCache(TrendSettingBean trendSettingBean){
        if(!trendSettingBean.isShowYilou()){
            formAdapter.showMissNum(false);
        }else{
            formAdapter.showMissNum(true);
        }
        if(!trendSettingBean.isShowStatistic()){
            List<FormTrendBean> tempData = new ArrayList<>();
            tempData.addAll(formDatas);
            formAdapter.hideLastFourth(true);
            //false的时候去掉最后四行
            if(isShowBottom){
                formAdapter.setDataSize(tempData.size());
            }else{
                for (int i = 0; i < 4; i++) {
                    tempData.remove(tempData.size()-1);
                }
                formAdapter.setDataSize(tempData.size());
            }
            formAdapter.setDataFun(tempData);
            mRVForm.scrollToPosition(tempData.size() - 1);
        }else{
            formAdapter.hideLastFourth(false);
            setFillData();
            mRVForm.scrollToPosition(formDatas.size() - 1);
        }
        formAdapter.notifyDataSetChanged();
    }

    //计算平均遗漏
    public int[] calculateAvg(int position, int[] showTime){
        int[] num = new int[position];
        int[] sum = new int[position];
        int[] tempNumber = new int[position];
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData missBean = datas.get(i);
            List<String> numbers = missBean.getMissNumber().getXingtai();
            for (int j = 0; j < num.length; j++) {
                if(Integer.parseInt(numbers.get(j))>tempNumber[j]){
                    tempNumber[j] = Integer.parseInt(numbers.get(j));
                }else if(0 == Integer.parseInt(numbers.get(j))){
                    sum[j] +=tempNumber[j];
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
