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
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.OpenAwardBean;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.adapter.WinningNumberAdapter;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

import static cn.zcgames.lottery.app.AppConstants.KEY_TREND_TYPE_DATA;

/**
 * 11_5任选及前二与前三的组选形态趋势图
 *
 * @author NorthStar
 * @date 2018/10/16 15:41
 */
public class AnyFormFragment extends BaseFragment {
    @BindView(R.id.rv_award_form)
    RecyclerView mRVFormNum;
    @BindView(R.id.ll_rank_periods)
    LinearLayout mLLRank;
    @BindView(R.id.iv_rank_icon)
    ImageView mIVRank;

    private List<OpenAwardBean> formDatas = new ArrayList<>();
    private WinningNumberAdapter formAdapter;//开奖号码适配器
    private int mPlayType;
    private String mLotteryType;
    private Unbinder unbinder;
    public static final String TAG = "走势图数据";
    private boolean isPositive = true;//是否是顺序
    private TrendChooseNumFragment chooseNumFragment;

    public static AnyFormFragment newInstance(LotteryTrendData data) {
        AnyFormFragment fragment = new AnyFormFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TREND_TYPE_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getType();
        View awardView = inflater.inflate(R.layout.fragment_form_layout, container, false);
        unbinder = ButterKnife.bind(this, awardView);
        EventBus.getDefault().register(this);
        initView();
        initTrendData();
        setSettingUI();
        initListener();
        return awardView;
    }

    private void getType() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            LotteryTrendData datas = (LotteryTrendData) bundle.getSerializable(KEY_TREND_TYPE_DATA);
            if (datas != null && datas.getTypeData() != null) {
                mPlayType = datas.getPlayType();
                mLotteryType = datas.getLotteryType();
                TrendTypeData typeData = datas.getTypeData();
                List<OpenAwardBean> formData = typeData.getFormDatas();
                if (formData != null && formData.size() > 0) {
                    formDatas.clear();
                    formDatas.addAll(formData);
                }
                LogF.d(TAG, "开奖--彩种类型==>" + mLotteryType + " ,玩法类型==>" + mPlayType);
            }
        }
    }

    private void initView() {
        initAdapter();
    }

    public void initEleven5Data() {
        if (formDatas != null && formDatas.size() > 0) {
            formAdapter.update(mLotteryType, mPlayType);
            formAdapter.setData(formDatas);
            setDialog();
        }
    }

    public void setDialog() {
        TrendSettingBean trendSettingBean = LotteryUtils.getTrendSettingCache(mLotteryType);
        if (isPositive != trendSettingBean.isListAsc()) {
            isPositive = trendSettingBean.isListAsc();
            setOrder();
            formAdapter.notifyDataSetChanged();
        } else {
            mIVRank.setImageDrawable(getActivity().getResources().getDrawable(isPositive ?
                    R.drawable.lottery_rank_up : R.drawable.lottery_rank_down));
            mRVFormNum.scrollToPosition(isPositive ? formDatas.size() - 1 : 0);
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

        chooseNumFragment.setSelectLisenter(new TrendChooseNumAdapter.ChooseNumSelectedListener() {
            @Override
            public void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls) {
                LogF.d("走势图选号", "选号状态" + GsonUtil.getInstance().toJson(balls));
            }
        });
    }

    //numPos 任选传5
    public void updateChooseNumFragmet(int numPos) {
        if (chooseNumFragment != null)
            chooseNumFragment.updateView(mLotteryType, mPlayType, numPos);
        initEleven5Data();
    }


    private void initAdapter() {
        formAdapter = new WinningNumberAdapter(getActivity(), mLotteryType, mPlayType);
        mRVFormNum.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRVFormNum.setAdapter(formAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingChangeEvent event) {
        if (event != null && event.getTrendSettingBean() != null) {
            //排序是否改变
            boolean isChange = isPositive != event.getTrendSettingBean().isListAsc();
            if (isChange) {
                if (event.getTrendSettingBean().isListAsc() && !isPositive) {
                    isPositive = true;
                    setOrder();
                } else {
                    isPositive = false;
                    setOrder();
                }
                formAdapter.notifyDataSetChanged();
            }
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
                formDatas = typeData.getFormDatas();
                TrendSettingBean trendSettingCache = LotteryUtils.getTrendSettingCache(mLotteryType);
                trendSettingCache.setListAsc(isPositive);//顺序
                mLotteryType = lotteryType;
                LotteryUtils.saveTrendSettingCache(mLotteryType, trendSettingCache);
                if (!isPositive) Collections.reverse(formDatas);
                updateChooseNumFragmet(5);
            }
        }
    }


    //获取彩种及玩法
    public void initTrendData() {
        initEleven5Data();
        setChooseNum();
    }

    public void initListener() {
        //确定按钮
        mLLRank.setOnClickListener(v -> {
            TrendSettingBean trendSettingCache = LotteryUtils.getTrendSettingCache(mLotteryType);
            if (!isPositive) {
                isPositive = true;
                trendSettingCache.setListAsc(true);//顺序
                LotteryUtils.saveTrendSettingCache(mLotteryType, trendSettingCache);
                setOrder();
            } else {
                isPositive = false;
                trendSettingCache.setListAsc(false);//倒序
                LotteryUtils.saveTrendSettingCache(mLotteryType, trendSettingCache);
                setOrder();
            }
            formAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            setSettingUI();
        }
    }

    private void setOrder() {
        Collections.reverse(formDatas);
        mIVRank.setImageDrawable(getActivity().getResources().getDrawable(isPositive ?
                R.drawable.lottery_rank_up : R.drawable.lottery_rank_down));
        mRVFormNum.scrollToPosition(isPositive ? formDatas.size() - 1 : 0);
    }

    public void setSettingUI() {
        TrendSettingUIChangeEvent event = new TrendSettingUIChangeEvent();
        event.setNeedShowListAsc(true);
        event.setNeedShowShowYilou(false);
        event.setNeedShowShowLine(false);
        event.setNeedShowShowStatistic(false);
        EventBus.getDefault().post(event);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
