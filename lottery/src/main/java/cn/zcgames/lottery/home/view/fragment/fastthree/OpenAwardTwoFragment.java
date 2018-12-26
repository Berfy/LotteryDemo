package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.OpenAwardBean;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.adapter.WinningTwoAdapter;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

public class OpenAwardTwoFragment extends BaseFragment {
    private RecyclerView mRVAwardNum;
    private LinearLayout mLLRank;
    private ImageView mIVRank;
    private List<OpenAwardBean> awardDatas;
    private WinningTwoAdapter winAdapter;
    private List<TrendResponseData.TrendData> datas;
    private int playType;
    private String lotteryType;
    private TrendChooseNumFragment chooseNumFragment;
    private boolean changeRank;
    private boolean isPositive = true;//是否是顺序


    public static OpenAwardTwoFragment newInstance(String lotteryType, int playType, List<TrendResponseData.TrendData> datas){
        OpenAwardTwoFragment awardFragment = new OpenAwardTwoFragment();
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

        View awardView = inflater.inflate(R.layout.fragment_open_award_two, container, false);
        EventBus.getDefault().register(this);
        initView(awardView);
        initData();
        initListener();
        setSettingCache();
        setSettingUI();
        return awardView;
    }

    public void initView(View view){
        mRVAwardNum = view.findViewById(R.id.rv_open_award_two);
        mLLRank = view.findViewById(R.id.ll_rank_periods_two);
        mIVRank = view.findViewById(R.id.iv_rank_icon_two);

        Bundle bundle = getArguments();
        if(bundle != null){
            lotteryType = bundle.getString("lotteryType");
            playType = bundle.getInt("playType");
            datas = (List<TrendResponseData.TrendData>) bundle.getSerializable("data");
        }

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        chooseNumFragment = TrendChooseNumFragment.newInstance(lotteryType, playType, 0);
        transaction
                .add(R.id.fl_award_two_select_num, chooseNumFragment)
                .show(chooseNumFragment)
                .commit();
        chooseNumFragment.setSelectLisenter(new TrendChooseNumAdapter.ChooseNumSelectedListener() {
            @Override
            public void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls) {
                LogF.d("走势图选号", "选号状态" + GsonUtil.getInstance().toJson(balls));
            }
        });

    }

    public void initData(){

        awardDatas = new ArrayList<>();
        parseData();
        winAdapter = new WinningTwoAdapter(getActivity(), awardDatas);
        mRVAwardNum.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRVAwardNum.setAdapter(winAdapter);
        mRVAwardNum.scrollToPosition(awardDatas.size()-1);
    }

    public void parseData(){
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData missBean = datas.get(i);
            String winNumber ="", formSize = "", period = "";
            int one = 0, two = 0, three = 0;
            if(missBean != null){
                period = missBean.getPeriod().substring(missBean.getPeriod().length()-2, missBean.getPeriod().length());
                if(missBean.getWinnerNumber().size() > 0){
                    for (int j = 0; j < missBean.getWinnerNumber().size(); j++) {
                        winNumber = winNumber+missBean.getWinnerNumber().get(j)+" ";
                    }
                    one = Integer.parseInt(missBean.getWinnerNumber().get(0));
                    two = Integer.parseInt(missBean.getWinnerNumber().get(1));
                    three = Integer.parseInt(missBean.getWinnerNumber().get(2));
                    if((two - one == 1) && (three - two == 1)){
                        formSize = "三连号";
                    }else if(two == one && two == three){
                        formSize = "三同号";
                    }else if((two == one) || (two == three) || (one ==three)){
                        formSize = "二同号";
                    }else{
                        formSize = "三不同号";
                    }
                }else{
                    winNumber = "等待开奖";
                }

                OpenAwardBean awardBean = new OpenAwardBean();
                awardBean.setPeriod(period+"期");
                awardBean.setAwardNums(winNumber);
                awardBean.setNumState(formSize);
                awardDatas.add(awardBean);
            }
        }
    }

    public void initListener(){
        //确定按钮
        mLLRank.setOnClickListener(v->{
            TrendSettingBean trendSettingCache = LotteryUtils.getTrendSettingCache(lotteryType);
            if (!isPositive) {
                isPositive = true;
                trendSettingCache.setListAsc(true);//顺序
                LotteryUtils.saveTrendSettingCache(lotteryType, trendSettingCache);
                setOrder();
            } else {
                isPositive = false;
                trendSettingCache.setListAsc(false);//倒序
                LotteryUtils.saveTrendSettingCache(lotteryType, trendSettingCache);
                setOrder();
            }
            winAdapter.notifyDataSetChanged();
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLotteryPlayType(LotteryTrendData data){
        if (data!=null) {
            datas = data.getData();
            lotteryType = data.getLotteryType();
            playType = data.getPlayType();
            awardDatas.clear();
            parseData();
            setSettingCache();
            mRVAwardNum.scrollToPosition(awardDatas.size()-1);
            chooseNumFragment.updateView(lotteryType, playType, 0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingChangeEvent event) {
        if (event != null && event.getTrendSettingBean() != null) {
            //排序是否改变
            boolean listAsc = event.getTrendSettingBean().isListAsc();
            boolean isChange = isPositive != listAsc;
            if(isChange){
                if (listAsc) {
                    isPositive = true;
                    setOrder();
                } else {
                    isPositive = false;
                    setOrder();
                }
            }
            winAdapter.notifyDataSetChanged();
        }
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
        }
    }

    public void setSettingUI(){
        TrendSettingUIChangeEvent event = new TrendSettingUIChangeEvent();
        event.setNeedShowListAsc(true);
        EventBus.getDefault().post(event);
    }

    private void setOrder() {
        Collections.reverse(awardDatas);
        mIVRank.setImageDrawable(getActivity().getResources().getDrawable(isPositive ?
                R.drawable.lottery_rank_up : R.drawable.lottery_rank_down));
        mRVAwardNum.scrollToPosition(isPositive ? awardDatas.size() - 1 : 0);
    }

    //设置缓存
    public void setSettingCache(){
        TrendSettingBean trendSettingBean = LotteryUtils.getTrendSettingCache(lotteryType);
        isPositive = trendSettingBean.isListAsc();
        if (!isPositive) {
            setOrder();
            winAdapter.notifyDataSetChanged();
        }
    }

}
