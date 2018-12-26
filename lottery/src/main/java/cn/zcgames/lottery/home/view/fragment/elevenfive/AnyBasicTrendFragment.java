package cn.zcgames.lottery.home.view.fragment.elevenfive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.bean.BasicTrendBean;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.view.adapter.AnyBasicAdapter;

import static cn.zcgames.lottery.app.AppConstants.*;

import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

/**
 * 11选5,任选及组选的基本走势图
 *
 * @author NorthStar
 * @date 2018/10/16 18:39
 */
public class AnyBasicTrendFragment extends BaseFragment {
    @BindView(R.id.rv_basic_trend)
    RecyclerView mRVShow;
    private AnyBasicAdapter basicAdapter;
    private List<BasicTrendBean> basicDatas = new ArrayList<>();
    private Unbinder unbinder;
    private int mPlayType;
    private String mLotteryType;
    boolean isShowEmptyView = false;
    public static final String TAG = "AnyBasicTrendFragment";
    private TrendChooseNumFragment chooseNumFragment;


    public static AnyBasicTrendFragment newInstance(LotteryTrendData data) {
        AnyBasicTrendFragment basicFragment = new AnyBasicTrendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TREND_TYPE_DATA, data);
        basicFragment.setArguments(bundle);
        return basicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View basicView = inflater.inflate(R.layout.fragment_any_basic_trend, container, false);
        unbinder = ButterKnife.bind(this, basicView);
        EventBus.getDefault().register(this);
        setSettingUI();
        initData();
        return basicView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            LotteryTrendData datas = (LotteryTrendData) bundle.getSerializable(KEY_TREND_TYPE_DATA);
            if (datas != null && datas.getTypeData() != null) {
                TrendTypeData data = datas.getTypeData();
                basicDatas = data.getBasicDatas();
                mLotteryType = datas.getLotteryType();
                mPlayType = datas.getPlayType();
                if (basicDatas != null && basicDatas.size() > 0) {
                    for (BasicTrendBean basicData : basicDatas) {
                        LogF.d(TAG, "中奖号" + basicData.numbers);
                    }
                    int end = basicDatas.size() - 1;
                    isShowEmptyView = basicDatas.get(end).isWaitAward();
                    basicAdapter = new AnyBasicAdapter(getActivity());
                    mRVShow.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRVShow.setAdapter(basicAdapter);
                    mRVShow.scrollToPosition(end);
                    TrendSettingBean trendSettingBean = LotteryUtils.getTrendSettingCache(mLotteryType);
                    setDialog(trendSettingBean);
                    setChooseNum();
                }
            }
        }
    }

    private void setChooseNum() {
        //选号部分
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        chooseNumFragment = TrendChooseNumFragment.newInstance(mLotteryType, mPlayType, 5);
        transaction
                .add(R.id.fl_basic_select_num, chooseNumFragment)
                .show(chooseNumFragment)
                .commit();

        chooseNumFragment.setSelectLisenter((lotteryType, playType, numPos, balls)
                -> LogF.d("走势图选号", "选号状态" + GsonUtil.getInstance().toJson(balls)));
    }

    //更新任选
    public void updateChooseNumFragmet(int numPos) {
        if (chooseNumFragment != null) chooseNumFragment.updateView(mLotteryType, mPlayType, numPos);
        if (this.basicDatas != null && this.basicDatas.size() > 0) {
            basicAdapter.setData(this.basicDatas);
            basicAdapter.notifyDataSetChanged();
            isShowEmptyView = basicDatas.get(basicDatas.size() - 1).isWaitAward();
            TrendSettingBean trendSettingBean = LotteryUtils.getTrendSettingCache(mLotteryType);
            setDialog(trendSettingBean);
        }
    }

    //更新最新期次数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTrendData(LotteryTrendData data) {
        if (data != null) {
            TrendTypeData typeData = data.getTypeData();
            String lotteryType = data.getLotteryType();
            if (!TextUtils.isEmpty(lotteryType) && typeData != null) {
                mLotteryType = lotteryType;
                mPlayType = data.getPlayType();
                basicDatas = typeData.getBasicDatas();
                updateChooseNumFragmet(5);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingChangeEvent settingEvent) {
        TrendSettingBean trendSettingBean = settingEvent.getTrendSettingBean();
        setDialog(trendSettingBean);
    }

    private void setDialog(TrendSettingBean trendSettingBean) {
        basicAdapter.showMissNum(trendSettingBean.isShowYilou());
        if (!trendSettingBean.isShowStatistic()) {//隐藏统计
            List<BasicTrendBean> tempData = new ArrayList<>(basicDatas);
            basicAdapter.hideLastFourth(true);
            //false的时候去掉最后四行
            if (isShowEmptyView) {
                basicAdapter.setDataSize(tempData.size());
            } else {
                for (int i = 0; i < 4; i++) {
                    tempData.remove(tempData.size() - 1);
                }
                basicAdapter.setDataSize(tempData.size());
            }
            basicAdapter.setData(tempData);
        } else {
            basicAdapter.hideLastFourth(false);
            basicAdapter.setData(basicDatas);
            if (isShowEmptyView) {
                basicAdapter.setDataSize(basicDatas.size() + 1);
            } else {
                basicAdapter.setDataSize(basicDatas.size());
            }
            mRVShow.scrollToPosition(basicDatas.size() - 1);
        }
        basicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
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
        event.setNeedShowShowYilou(true);
        event.setNeedShowShowStatistic(true);
        EventBus.getDefault().post(event);
    }
}
