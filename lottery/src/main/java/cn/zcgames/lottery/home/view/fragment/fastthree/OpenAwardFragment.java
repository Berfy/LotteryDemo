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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import static cn.zcgames.lottery.app.AppConstants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.OpenAwardBean;
import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.home.bean.TrendSettingChangeEvent;
import cn.zcgames.lottery.home.bean.TrendSettingUIChangeEvent;
import cn.zcgames.lottery.home.bean.TrendTypeData;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.adapter.WinningNumberAdapter;
import cn.zcgames.lottery.home.view.fragment.TrendChooseNumFragment;
import cn.zcgames.lottery.model.local.LotteryTrendData;
import cn.zcgames.lottery.model.local.LotteryUtils;

/**
 * 开奖列表走势图
 *
 * @date 2018/10/15 18:10
 */
public class OpenAwardFragment extends BaseFragment {
    @BindView(R.id.rv_open_award)
    RecyclerView mRVAwardNum;
    @BindView(R.id.ll_rank_periods)
    LinearLayout mLLRank;
    @BindView(R.id.iv_rank_icon)
    ImageView mIVRank;
    @BindView(R.id.tv_award_key_1)
    TextView mTVAwardKey1;
    @BindView(R.id.tv_award_key_2)
    TextView mTVAwardKey2;
    @BindView(R.id.tv_award_key_3)
    TextView mTVAwardKey3;

    private List<OpenAwardBean> awardDatas = new ArrayList<>();
    private WinningNumberAdapter winAdapter;
    private String mLotteryType;
    private int mPlayType;
    private Unbinder unbinder;
    public static final String TAG = "走势图-开奖列表";
    private List<TrendResponseData.TrendData> datas;
    private TrendChooseNumFragment chooseNumFragment;
    private boolean isPositive = true;//是否是顺序
    public static final String KEY_PLAT_TYPE = "playType";
    public static final String KEY_LOTTERY_TYPE = "lotteryType";
    public static final String KEY_TREND_DATA = "trendData";

    public static OpenAwardFragment newInstance(int playType, String lotteryType, List<TrendResponseData.TrendData> datas) {
        OpenAwardFragment fragment = new OpenAwardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PLAT_TYPE, playType);
        bundle.putString(KEY_LOTTERY_TYPE, lotteryType);
        bundle.putSerializable(KEY_TREND_DATA, (Serializable) datas);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OpenAwardFragment newInstance(String lotteryType, int playType, TrendTypeData data) {
        OpenAwardFragment fragment = new OpenAwardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PLAT_TYPE, playType);
        bundle.putString(KEY_LOTTERY_TYPE, lotteryType);
        bundle.putSerializable(KEY_TREND_TYPE_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getType();
        View awardView = inflater.inflate(R.layout.fragment_open_award, container, false);
        unbinder = ButterKnife.bind(this, awardView);
        EventBus.getDefault().register(this);
        initView();
        initTrendData(false);
        setSettingUI();
        initListener();
        return awardView;
    }

    private void getType() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPlayType = bundle.getInt(KEY_PLAT_TYPE, -1);
            mLotteryType = bundle.getString(KEY_LOTTERY_TYPE);
            datas = (List<TrendResponseData.TrendData>) bundle.getSerializable(KEY_TREND_DATA);

            TrendTypeData data = (TrendTypeData) bundle.getSerializable(KEY_TREND_TYPE_DATA);
            if (data != null && data.getAwardDatas().size() > 0) {
                awardDatas = data.getAwardDatas();
                LogF.d(TAG, "awardDatas.size()" + awardDatas.size());
            }
            LogF.d(TAG, "开奖--彩种类型==>" + mLotteryType + " ,玩法类型==>" + mPlayType);
        }
    }

    private void initView() {
        initAdapter();
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (mPlayType == AppConstants.PLAY_11_5_FRONT_2_DIRECT
                || mPlayType == AppConstants.PLAY_11_5_FRONT_3_DIRECT) {
            chooseNumFragment = TrendChooseNumFragment.newInstance(mLotteryType, mPlayType, -1);
        } else {
            chooseNumFragment = TrendChooseNumFragment.newInstance(mLotteryType, mPlayType, 0);
        }
        transaction
                .add(R.id.fl_award_select_num, chooseNumFragment)
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
    }

    public void initData() {
        LogF.d(TAG, "刷新页面 initData");
        if (awardDatas != null && awardDatas.size() > 0) {
            winAdapter.setData(awardDatas);
            setDialog();
            LogF.d(TAG, "刷新页面 initData完毕");
        }
    }

    public void setDialog() {
        TrendSettingBean trendSettingBean = LotteryUtils.getTrendSettingCache(mLotteryType);
        boolean listAsc = trendSettingBean.isListAsc();
        LogF.d(TAG, "setDialog--listAsc==>" + listAsc + " ,isPositive==>" + isPositive + " ,playType==>" + mPlayType);
        if (isPositive != listAsc) {
            isPositive = listAsc;
            LogF.d(TAG, "new isPositive==>" + isPositive);
            setOrder();
            winAdapter.notifyDataSetChanged();
        } else {
            LogF.d(TAG, "setDialog2--isPositive==>" + isPositive + " ,playType==>" + mPlayType);
            mIVRank.setImageDrawable(getActivity().getResources().getDrawable(isPositive ?
                    R.drawable.lottery_rank_up : R.drawable.lottery_rank_down));
            mRVAwardNum.scrollToPosition(isPositive ? awardDatas.size() - 1 : 0);
        }
    }

    private void initAdapter() {
        winAdapter = new WinningNumberAdapter(getActivity(), mLotteryType, mPlayType);
        mRVAwardNum.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRVAwardNum.setAdapter(winAdapter);
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
            winAdapter.notifyDataSetChanged();
        });
    }


    //获取彩种及玩法
    public void initTrendData(boolean isUpdate) {
        LogF.d(TAG, "刷新页面 isUpdate=" + isUpdate);
        if (!TextUtils.isEmpty(mLotteryType)) {
            switch (mLotteryType) {
                case LOTTERY_TYPE_FAST_3://快三
                case LOTTERY_TYPE_FAST_3_JS:
                case LOTTERY_TYPE_FAST_3_HB:
                case LOTTERY_TYPE_FAST_3_EASY:
                case LOTTERY_TYPE_FAST_3_NEW:
                    if (isUpdate) {
                        awardDatas.clear();
                        parseData();
                        winAdapter.setData(awardDatas);
                        winAdapter.update(mLotteryType, mPlayType);
                        winAdapter.notifyDataSetChanged();
                        mRVAwardNum.scrollToPosition(awardDatas.size() - 1);
                        chooseNumFragment.updateView(mLotteryType, mPlayType, 0);
                    } else {
                        initFast3Data();
                    }
                    break;
                case LOTTERY_TYPE_11_5://11选5
                case LOTTERY_TYPE_11_5_LUCKY:
                case LOTTERY_TYPE_11_5_OLD:
                case LOTTERY_TYPE_11_5_YILE:
                case LOTTERY_TYPE_11_5_YUE:
                    mTVAwardKey2.setText("跨度");
                    mTVAwardKey3.setText("重号个数");
                    if (isUpdate && !isPositive) Collections.reverse(awardDatas);
                    switch (mPlayType) {
                        case PLAY_11_5_ANY_2://任选2
                        case PLAY_11_5_ANY_3://任选3
                        case PLAY_11_5_ANY_4://任选4
                        case PLAY_11_5_ANY_5://任选5
                        case PLAY_11_5_ANY_6://任选6
                        case PLAY_11_5_ANY_7://任选7
                        case PLAY_11_5_ANY_8://任选8
                        case PLAY_11_5_FRONT_2_GROUP://前二组选
                        case PLAY_11_5_FRONT_3_GROUP://前三组选
                        case PLAY_11_5_FRONT_1_DIRECT://前一直选
                            updateChooseNumFragmet(5);
                            winAdapter.update(mLotteryType, mPlayType);
                            winAdapter.notifyDataSetChanged();//切换玩法刷新页面
                            initData();
                            break;
                        case PLAY_11_5_FRONT_2_DIRECT://前二直选
                        case PLAY_11_5_FRONT_3_DIRECT://前三直选
                            updateChooseNumFragmet(-1);
                            winAdapter.update(mLotteryType, mPlayType);
                            winAdapter.notifyDataSetChanged();//切换玩法刷新页面
                            initData();
                            break;

                        case AppConstants.PLAY_11_5_ANY_2_DAN://任选2胆拖
                        case AppConstants.PLAY_11_5_ANY_3_DAN://任选3胆拖
                        case AppConstants.PLAY_11_5_ANY_4_DAN://任选4胆拖
                        case AppConstants.PLAY_11_5_ANY_5_DAN://任选5胆拖
                        case AppConstants.PLAY_11_5_ANY_6_DAN://任选6胆拖
                        case AppConstants.PLAY_11_5_ANY_7_DAN://任选7胆拖
                        case AppConstants.PLAY_11_5_ANY_8_DAN://任选8胆拖
                        case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN://前二组选胆拖
                        case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN://前三组选胆拖
                            updateChooseNumFragmet(-1);
                            winAdapter.update(mLotteryType, mPlayType);
                            winAdapter.notifyDataSetChanged();//切换玩法刷新页面
                            initData();
                            break;

                    }
                    break;
                case LOTTERY_TYPE_ALWAYS_COLOR:
                case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                    mTVAwardKey1.setText("十位");
                    mTVAwardKey2.setText("个位");
                    mTVAwardKey3.setText("后三");
                    LogF.d(TAG, "刷新页面 时时彩");
                    if (isUpdate) {
                        if (!isPositive) Collections.reverse(awardDatas);
                    }
                    switch (mPlayType) {
                        case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                            if (isUpdate) {
                                updateChooseNumFragmet(0);
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_1_DIRECT:
                            if (isUpdate) {
                                updateChooseNumFragmet(1);
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_2_DIRECT:
                            if (isUpdate) {
                                updateChooseNumFragmet(-1);
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_3_DIRECT:
                            if (isUpdate) {
                                updateChooseNumFragmet(-1);
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_5_DIRECT:
                        case AppConstants.ALWAYS_COLOR_5_ALL:
                            if (isUpdate) {
                                updateChooseNumFragmet(-1);
                            }
                            break;
                        case AppConstants.ALWAYS_COLOR_2_GROUP:
                        case AppConstants.ALWAYS_COLOR_3_GROUP_3:
                        case AppConstants.ALWAYS_COLOR_3_GROUP_6:
                            if (isUpdate) {
                                updateChooseNumFragmet(1);
                            }
                            break;
                    }
                    winAdapter.update(mLotteryType, mPlayType);
                    winAdapter.notifyDataSetChanged();//切换玩法刷新页面
                    initData();
                    break;
            }
        }
    }

    public void initFast3Data() {
        awardDatas = new ArrayList<>();
        parseData();
        winAdapter.setData(awardDatas);
        mRVAwardNum.scrollToPosition(awardDatas.size() - 1);

        //获取缓存
        TrendSettingBean trendSettingBean = LotteryUtils.getTrendSettingCache(mLotteryType);
        isPositive = trendSettingBean.isListAsc();
        if (!isPositive) {
            setOrder();
            winAdapter.notifyDataSetChanged();
        }
    }

    public void parseData() {
        for (int i = 0; i < datas.size(); i++) {
            TrendResponseData.TrendData missBean = datas.get(i);
            String winNumber = "", sumSize = "", sumState = "", period = "";
            int sum = 0;
            if (missBean != null) {
                period = missBean.getPeriod().substring(missBean.getPeriod().length() - 2, missBean.getPeriod().length());
                if (missBean.getWinnerNumber().size() > 0) {
                    for (int j = 0; j < missBean.getWinnerNumber().size(); j++) {
                        winNumber = winNumber + missBean.getWinnerNumber().get(j) + " ";
                        sum += Integer.parseInt(missBean.getWinnerNumber().get(j));
                    }
                    if (sum % 2 == 0) {
                        sumState = "双";
                    } else {
                        sumState = "单";
                    }
                    if (sum >= 11) {
                        sumSize = "大";
                    } else {
                        sumSize = "小";
                    }
                } else {
                    winNumber = "等待开奖";
                }

                OpenAwardBean awardBean = new OpenAwardBean();
                awardBean.setPeriod(period + "期");
                awardBean.setAwardNums(winNumber);
                awardBean.setKey3(String.valueOf(sum));
                awardBean.setKey4(sumSize);
                awardBean.setKey5(sumState);
                awardDatas.add(awardBean);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTrendData(LotteryTrendData data) {
        if (data != null) {
            mLotteryType = data.getLotteryType();
            mPlayType = data.getPlayType();
            datas = data.getData();
            String lotteryType = data.getLotteryType();
            TrendTypeData typeData = data.getTypeData();
            if (!TextUtils.isEmpty(lotteryType) && typeData != null) {
                awardDatas = typeData.getAwardDatas();
                TrendSettingBean trendSettingCache = LotteryUtils.getTrendSettingCache(mLotteryType);
                trendSettingCache.setListAsc(isPositive);//顺序
                LotteryUtils.saveTrendSettingCache(mLotteryType, trendSettingCache);
                initTrendData(true);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChanged(TrendSettingChangeEvent event) {
        if (event != null && event.getTrendSettingBean() != null) {
            //排序是否改变
            boolean listAsc = event.getTrendSettingBean().isListAsc();
            boolean isChange = isPositive != listAsc;
            LogF.d(TAG, "onSettingChanged--listAsc==>" + listAsc + " ,isPositive==>" + isPositive + " ,playType==>" + mPlayType);
            if (isChange) {
                if (listAsc) {
                    isPositive = true;
                    setOrder();
                } else {
                    isPositive = false;
                    setOrder();
                }
                winAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setOrder() {
        LogF.d(TAG, "setOrder--isPositive==>" + isPositive + " ,playType==>" + mPlayType);
        Collections.reverse(awardDatas);
        mIVRank.setImageDrawable(getActivity().getResources().getDrawable(isPositive ?
                R.drawable.lottery_rank_up : R.drawable.lottery_rank_down));
        mRVAwardNum.scrollToPosition(isPositive ? awardDatas.size() - 1 : 0);
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
        event.setNeedShowListAsc(true);
        EventBus.getDefault().post(event);
    }
}
