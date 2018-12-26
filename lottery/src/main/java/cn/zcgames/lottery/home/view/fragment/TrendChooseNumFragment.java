package cn.zcgames.lottery.home.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.ChooseNumberBean;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.bean.response.ResultSequenceBeanNew;
import cn.zcgames.lottery.home.bean.ChooseNumberInfoBean;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.callback.ConfirmDialogCallback;
import cn.zcgames.lottery.home.presenter.LotteryOrderPresenter;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.view.adapter.TrendChooseNumAdapter;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * author: Berfy
 * date: 2018/10/16
 * 走势图页面选号
 */
public class TrendChooseNumFragment extends BaseFragment implements TrendChooseNumAdapter.ChooseNumSelectedListener, ILotteryOrderActivity {

    private final String TAG = "走势图页面选号Frag";
    @BindView(R.id.ll_select_num)
    LinearLayout mLlSleletNum;
    @BindView(R.id.tv_ok)
    TextView mTvOk;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.tv_play_type)
    TextView mTvPlayType;
    //大小单双
    @BindView(R.id.ll_dxds)
    LinearLayout mLlDxds;
    @BindView(R.id.ten_big)
    TextView tenBig;
    @BindView(R.id.ten_small)
    TextView tenSmall;
    @BindView(R.id.ten_single)
    TextView tenSingle;
    @BindView(R.id.ten_double)
    TextView tenDouble;
    @BindView(R.id.one_big)
    TextView oneBig;
    @BindView(R.id.one_small)
    TextView oneSmall;
    @BindView(R.id.one_single)
    TextView oneSingle;
    @BindView(R.id.one_double)
    TextView oneDouble;

    @BindView(R.id.rv_ballView)
    RecyclerView mRv;
    @BindView(R.id.ll_price_info)
    LinearLayout mLlPrice;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_period)
    TextView mTvPeriod;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;

    private Unbinder mUnbinder;
    private String mLotteryType;//彩种
    private int mPlayType;//玩法
    private boolean mIsShowNumber;//是否显示选号
    private int mNumPosition;//个十百千万 1-5
    private int mTitleWidth;
    private int mItemWidth;//宽高
    private int mItemHeight;
    private ChooseNumberInfoBean mChooseNumberInfoBean;//选号详情
    private long mCountDownTime = 60 * 10 * 1000;//默认倒计时时间

    private static final int MSG_UPDATE_COUNT_DOWN = 0;
    private MyHandler mHandler;

    private LinearLayoutManager mLinearLayoutManager;
    private TrendChooseNumAdapter mAdapter;
    private TrendChooseNumAdapter.ChooseNumSelectedListener mChooseNumSelectedListener;
    private ChooseNumScrollerListener mChooseNumScrollerListener;

    //获取接口信息
    private LotteryModel mLotteryModel;
    private LotteryOrderPresenter mPresenter;

    private TextView[] mTenButtons;
    private TextView[] mOneButtons;

    /**
     * @param lotteryType  彩种
     * @param playType     玩法
     * @param isShowNumber 是否显示选号
     * @param numPos       选号位数 个位（1星）1-万位（5星）5  胆1拖2 任选5  跨度0 振幅-1 基本走势-2 形态-3
     * @param titleWidth   选号左侧title的宽度
     * @param itemWidth    选号item的宽度
     * @param itemHeight   选号item的高度
     */
    public static TrendChooseNumFragment newInstance(String lotteryType, int playType, boolean isShowNumber, int numPos, int titleWidth, int itemWidth, int itemHeight) {
        TrendChooseNumFragment trendChooseNumFragment = new TrendChooseNumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        bundle.putInt(ActivityConstants.PARAM_LOTTERY_PLAY_TYPE, playType);
        bundle.putBoolean("isShowNumber", isShowNumber);
        bundle.putInt(ActivityConstants.PARAM_LOTTERY_PLAY_NUM_POS, numPos);
        bundle.putInt("titleWidth", titleWidth);
        bundle.putInt(ActivityConstants.PARAM_KEY_WIDTH, itemWidth);
        bundle.putInt(ActivityConstants.PARAM_KEY_HEIGHT, itemHeight);
        trendChooseNumFragment.setArguments(bundle);
        return trendChooseNumFragment;
    }

    /**
     * @param lotteryType 彩种
     * @param playType    玩法
     * @param numPos      选号位数 个位（1星）1-万位（5星）5  胆1拖2 任选5  跨度0 振幅-1 基本走势-2 形态-3
     * @param titleWidth  选号左侧title的宽度
     * @param itemWidth   选号item的宽度
     * @param itemHeight  选号item的高度
     */
    public static TrendChooseNumFragment newInstance(String lotteryType, int playType, int numPos, int titleWidth, int itemWidth, int itemHeight) {
        return newInstance(lotteryType, playType, true, numPos, titleWidth, itemWidth, itemHeight);
    }

    /**
     * @param lotteryType  彩种
     * @param playType     玩法
     * @param isShowNumber 是否显示选号
     * @param numPos       选号位数 个位（1星）1-万位（5星）5  胆1拖2 任选5  跨度0 振幅-1 基本走势-2 形态-3
     */
    public static TrendChooseNumFragment newInstance(String lotteryType, int playType, boolean isShowNumber, int numPos) {
        return newInstance(lotteryType, playType, isShowNumber, numPos, 0, 0, 0);
    }

    /**
     * @param lotteryType 彩种
     * @param playType    玩法
     * @param numPos      选号位数 个位（1星）1-万位（5星）5  胆1拖2 任选5  跨度0 振幅-1 基本走势-2 形态-3
     */
    public static TrendChooseNumFragment newInstance(String lotteryType, int playType, int numPos) {
        return newInstance(lotteryType, playType, true, numPos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogF.d(TAG, "生命周期 onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogF.d(TAG, "生命周期 onCreateView");
        View view = inflater.inflate(R.layout.fragment_trend_choose_layout, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initBundle();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initBundle() {
        if (null == getArguments()) {
            return;
        }
        Bundle bundle = getArguments();
        mLotteryType = bundle.getString(ActivityConstants.PARAM_LOTTERY_TYPE);
        mPlayType = bundle.getInt(ActivityConstants.PARAM_LOTTERY_PLAY_TYPE);
        mIsShowNumber = bundle.getBoolean("isShowNumber", true);
        mNumPosition = bundle.getInt(ActivityConstants.PARAM_LOTTERY_PLAY_NUM_POS);
        mTitleWidth = bundle.getInt("titleWidth");
        mItemWidth = bundle.getInt(ActivityConstants.PARAM_KEY_WIDTH);
        mItemHeight = bundle.getInt(ActivityConstants.PARAM_KEY_HEIGHT);
    }

    private void initView() {
        mHandler = new MyHandler(this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mLotteryModel = new LotteryModel();
        mPresenter = new LotteryOrderPresenter(getActivity(), this);
        getPeriod();
        if (mTitleWidth > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLlTitle.getLayoutParams();
            layoutParams.width = mTitleWidth;
        }
        mLlSleletNum.setVisibility(mIsShowNumber ? View.VISIBLE : View.GONE);
        mVLine.setVisibility(mIsShowNumber ? View.VISIBLE : View.GONE);
        mTvOk.setVisibility(mIsShowNumber ? View.VISIBLE : View.GONE);
        mRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogF.d(TAG, "列表滑动 dx=" + dx + " dy=" + dy);
                if (null != mChooseNumScrollerListener) {
                    mChooseNumScrollerListener.onScrollChanged(true, dx, dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LogF.d(TAG, "列表滑动停止");
                    if (null != mChooseNumScrollerListener) {
                        mChooseNumScrollerListener.onScrollChanged(false, 0, 0);
                    }
                }
            }
        });
        mTenButtons = new TextView[]{tenBig, tenSmall, tenSingle, tenDouble};
        mOneButtons = new TextView[]{oneBig, oneSmall, oneSingle, oneDouble};
        updateView(mLotteryType, mPlayType, mNumPosition);
    }

    private void getPeriod() {
        mLotteryModel.requestCurrentSequence(mLotteryType, new NormalCallback() {
            @Override
            public void responseOk(Object msg) {
                if (isDestroy()) {
                    return;
                }
                if (msg instanceof ResultSequenceBeanNew.SequenceBean) {
                    ResultSequenceBeanNew.SequenceBean sequenceBean = (ResultSequenceBeanNew.SequenceBean) msg;
                    mCountDownTime = sequenceBean.getDraw_time_left();
                    mTvPeriod.setText(String.format("距离%s投注截止: ", sequenceBean.getCur_period()));

                    //true允许购买
                    boolean orderState = getOrderState();
                    LogF.d("111111", "当前是否可以购彩==="+orderState);
                    if(orderState){
                        if (null != mHandler) {
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessage(MSG_UPDATE_COUNT_DOWN);
                        }
                    }else{
                        mTvEndTime.setText("暂停销售");
                    }


                }
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {

            }
        });
    }

    public void setSelectLisenter(TrendChooseNumAdapter.ChooseNumSelectedListener lisenter) {
        mChooseNumSelectedListener = lisenter;
    }

    public void setChooseNumScrollerListener(ChooseNumScrollerListener lisenter) {
        mChooseNumScrollerListener = lisenter;
    }

    public void updateView(String lotteryType, int playType, int numPos) {
        mLotteryType = lotteryType;
        mPlayType = playType;
        mNumPosition = numPos;
        mChooseNumberInfoBean = null;//切换玩法清空选号数据
        LogF.d(TAG, "updateView");
        if (!isViewCreated()) {
            LogF.d(TAG, "!mIsViewCreated");
            return;
        }
        if (TextUtils.isEmpty(lotteryType) || playType == -1) {
            LogF.d(TAG, "null");
            return;
        }
        mLlSleletNum.setVisibility(View.VISIBLE);
        mTvOk.setVisibility(View.VISIBLE);
        mRv.setVisibility(View.VISIBLE);
        mVLine.setVisibility(View.VISIBLE);
        mLlDxds.setVisibility(View.GONE);
        LogF.d(TAG, "updateView lotteryType=" + lotteryType + " playType=" + playType + " numPos=" + numPos);
        switch (lotteryType) {
            case AppConstants.LOTTERY_TYPE_FAST_3:
            case AppConstants.LOTTERY_TYPE_FAST_3_JS:
            case AppConstants.LOTTERY_TYPE_FAST_3_HB:
            case AppConstants.LOTTERY_TYPE_FAST_3_NEW:
            case AppConstants.LOTTERY_TYPE_FAST_3_EASY:
                mRv.setLayoutManager(mLinearLayoutManager);
                List<LotteryBall> balls = null;
                //快三numPos默认1个位
                switch (playType) {
                    case AppConstants.FAST_THREE_SUM:
                        balls = LotteryUtils.createFast3SumBallList();
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, balls, this);
                        mTvPlayType.setText(R.string.select_fast_three_sum);
                        break;
                    case AppConstants.FAST_THREE_3_SAME_ONE://三同号
                        balls = LotteryUtils.createFast3ThreeSameBallList();
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, balls, this);
                        mTvPlayType.setText(R.string.select_fast_three_three_same);
                        break;
                    case AppConstants.FAST_THREE_3_SAME_ALL:
                        balls = LotteryUtils.createFast3ThreeSameAllBallList();
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, balls, this);
                        mTvPlayType.setText(R.string.select_fast_three_three_same_all);
                        break;
                    case AppConstants.FAST_THREE_3_DIFFERENT://三不同
                        balls = LotteryUtils.createFast3BallList();
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, balls, this);
                        mTvPlayType.setText(R.string.select_fast_three_three_diff);
                        break;
                    case AppConstants.FAST_THREE_3_TO_ALL://三连号
                        balls = LotteryUtils.createFast3ThreeToAllBallList();
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, balls, this);
                        mTvPlayType.setText(R.string.select_fast_three_three_to_all);
                        break;
                    case AppConstants.FAST_THREE_2_SAME_ONE://二同号
                    case AppConstants.FAST_THREE_2_SAME_MORE:
                        balls = LotteryUtils.createFast3TwoSameBallList();
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, balls, this);
                        mTvPlayType.setText(R.string.select_fast_three_two_same);
                        break;
                    case AppConstants.FAST_THREE_2_DIFFERENT://二不同号
                        balls = LotteryUtils.createFast3BallList();
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, balls, this);
                        mTvPlayType.setText(R.string.select_fast_three_two_diff);
                        break;
                }
                if (null != mAdapter && null != balls) {
                    //                    ChooseNumberBean chooseNumberBean = LotteryUtils.getChooseNumberCache(mLotteryType, mPlayType, 1);
                    //                    for (LotteryBall lotteryBall : balls) {
                    //                        for (LotteryBall lotteryBallCache : chooseNumberBean.getLotteryBalls()) {
                    //                            if (lotteryBall.getNumber().equals(lotteryBallCache.getNumber())) {
                    //                                lotteryBall.setSelected(true);
                    //                            }
                    //                        }
                    //                    }
                    //                    onSelectBall(mLotteryType, mPlayType, 1, chooseNumberBean.getLotteryBalls());
                    mRv.setAdapter(mAdapter);
                }
                break;
            case AppConstants.LOTTERY_TYPE_11_5:
            case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
            case AppConstants.LOTTERY_TYPE_11_5_OLD:
            case AppConstants.LOTTERY_TYPE_11_5_YILE:
            case AppConstants.LOTTERY_TYPE_11_5_YUE:
                //11选5 numPos默认5 万位
                switch (playType) {
                    case AppConstants.PLAY_11_5_ANY_2:
                        mTvPlayType.setText(R.string.select_115_ren2);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_ANY_3:
                        mTvPlayType.setText(R.string.select_115_ren3);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_ANY_4:
                        mTvPlayType.setText(R.string.select_115_ren4);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_ANY_5:
                        mTvPlayType.setText(R.string.select_115_ren5);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_ANY_6:
                        mTvPlayType.setText(R.string.select_115_ren6);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_ANY_7:
                        mTvPlayType.setText(R.string.select_115_ren7);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_ANY_8:
                        mTvPlayType.setText(R.string.select_115_ren8);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_FRONT_2_GROUP://前二组选
                    case AppConstants.PLAY_11_5_FRONT_3_GROUP://前三组选
                        mTvPlayType.setText(R.string.select_num);
                        LogF.d(TAG, "zuxuan==>" + numPos);
                        mNumPosition = 5;
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType, mNumPosition, mItemWidth, mItemHeight,
                                LotteryUtils.create115BallList(), this);
                        break;
                    case AppConstants.PLAY_11_5_FRONT_1_DIRECT://前一直选
                        mNumPosition = 5;
                        setPos(lotteryType, LotteryUtils.create115BallList(), playType, mNumPosition);
                        break;
                    case AppConstants.PLAY_11_5_FRONT_2_DIRECT://前二直选
                    case AppConstants.PLAY_11_5_FRONT_3_DIRECT://前三直选
                        setPos(lotteryType, LotteryUtils.create115BallList(), playType, numPos);
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
                        mNumPosition = -1;//都是不显示选号
                        setPos(lotteryType, LotteryUtils.create115BallList(), playType, mNumPosition);
                        break;
                }
                if (null != mAdapter) {
                    mRv.setLayoutManager(mLinearLayoutManager);
                    mRv.setAdapter(mAdapter);
                }
                break;
            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR:
            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                switch (playType) {
                    case AppConstants.ALWAYS_COLOR_1_DIRECT://一星直选
                        mNumPosition = 1;//都是个位
                        break;
                    case AppConstants.ALWAYS_COLOR_2_DIRECT://二星直选
                        break;
                    case AppConstants.ALWAYS_COLOR_3_DIRECT://三星直选

                        break;
                    case AppConstants.ALWAYS_COLOR_5_DIRECT://五星直选
                        break;
                    case AppConstants.ALWAYS_COLOR_5_ALL://五星通选
                        break;
                    case AppConstants.ALWAYS_COLOR_2_GROUP://二星组选
                        mTvPlayType.setText(R.string.select_num);
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, LotteryUtils.createACBallList(), this);
                        break;
                    case AppConstants.ALWAYS_COLOR_3_GROUP_3://三星组三
                    case AppConstants.ALWAYS_COLOR_3_GROUP_6://三星组六
                        mNumPosition = 0;
                        mTvPlayType.setText(R.string.select_num);
                        mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                                numPos, mItemWidth, mItemHeight, LotteryUtils.createACBallList(), this);
                        break;
                    case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE://大小单双
                        mNumPosition = 0;
                        mTvPlayType.setText(R.string.select_num);
                        mRv.setVisibility(View.GONE);
                        mLlDxds.setVisibility(View.VISIBLE);
                        break;
                }
                LogF.d(TAG, "时时彩  numPos=" + numPos);
                setPos(lotteryType, LotteryUtils.createACBallList(), playType, numPos);

                if (null != mAdapter) {
                    mRv.setLayoutManager(mLinearLayoutManager);
                    mRv.setAdapter(mAdapter);
                }
                break;
        }
    }

    private void setPos(String lotteryType, List<LotteryBall> ballList, int playType, int numPos) {
        //按位数
        switch (mNumPosition) {
            case -1:
                mLlSleletNum.setVisibility(View.GONE);
                mVLine.setVisibility(View.GONE);
                mTvOk.setVisibility(View.GONE);
                break;
            case 1:
                mTvPlayType.setText(R.string.select_ge);
                mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                        numPos, mItemWidth, mItemHeight, ballList, this);
                break;
            case 2:
                mTvPlayType.setText(R.string.select_shi);
                mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                        numPos, mItemWidth, mItemHeight, ballList, this);
                break;
            case 3:
                mTvPlayType.setText(R.string.select_bai);
                mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                        numPos, mItemWidth, mItemHeight, ballList, this);

                break;
            case 4:
                mTvPlayType.setText(R.string.select_qian);
                mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                        numPos, mItemWidth, mItemHeight, ballList, this);
                break;
            case 5:
            case -3:
                mTvPlayType.setText(R.string.select_wan);
                mAdapter = new TrendChooseNumAdapter(getActivity(), lotteryType, playType,
                        numPos, mItemWidth, mItemHeight, ballList, this);
                break;
        }
    }

    @OnClick({R.id.ten_big, R.id.ten_small, R.id.ten_single, R.id.ten_double,
            R.id.one_big, R.id.one_small, R.id.one_single, R.id.one_double, R.id.tv_ok})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ten_big:
                updateBtnStatus(0, 0);
                break;
            case R.id.ten_small:
                updateBtnStatus(0, 1);
                break;
            case R.id.ten_single:
                updateBtnStatus(0, 2);
                break;
            case R.id.ten_double:
                updateBtnStatus(0, 3);
                break;
            case R.id.one_big:
                updateBtnStatus(1, 0);
                break;
            case R.id.one_small:
                updateBtnStatus(1, 1);
                break;
            case R.id.one_single:
                updateBtnStatus(1, 2);
                break;
            case R.id.one_double:
                updateBtnStatus(1, 3);
                break;
            case R.id.tv_ok:
                if (null == mChooseNumberInfoBean) {
                    UIHelper.showTipDialog(getActivity(), "请选择投注号码", new ConfirmDialogCallback() {
                        @Override
                        public void onConfirmResult(boolean isOk) {

                        }
                    });
                    return;
                }
                switch (mLotteryType) {
                    case AppConstants.LOTTERY_TYPE_FAST_3:
                    case AppConstants.LOTTERY_TYPE_FAST_3_JS:
                    case AppConstants.LOTTERY_TYPE_FAST_3_HB:
                    case AppConstants.LOTTERY_TYPE_FAST_3_NEW:
                    case AppConstants.LOTTERY_TYPE_FAST_3_EASY:
                        switch (mPlayType) {
                            case AppConstants.FAST_THREE_SUM:
                            case AppConstants.FAST_THREE_2_DIFFERENT://二不同号
                            case AppConstants.FAST_THREE_2_SAME_MORE://二同号  单选复选统一
                            case AppConstants.FAST_THREE_3_DIFFERENT://三不同号
                            case AppConstants.FAST_THREE_3_SAME_ONE://三同号单选、通选、三连号统一
                                mPresenter.createFast3SingleLocalOrder(mLotteryType, mPlayType,
                                        mChooseNumberInfoBean.getGe(), mChooseNumberInfoBean.getCount());
                                break;
                            case AppConstants.FAST_THREE_3_SAME_ALL://三同号单选、通选、三连号统一
                                mPresenter.createFast3SingleLocalOrder(mLotteryType, mPlayType,
                                        mChooseNumberInfoBean.getGe(), mChooseNumberInfoBean.getCount());
                                break;
                            case AppConstants.FAST_THREE_3_TO_ALL://三同号单选、通选、三连号统一
                                mPresenter.createFast3SingleLocalOrder(mLotteryType, mPlayType,
                                        mChooseNumberInfoBean.getGe(), mChooseNumberInfoBean.getCount());
                                break;
                            case AppConstants.FAST_THREE_2_SAME_ONE://二同号  单选复选统一
                                mPresenter.createFast3LocalOrder(mLotteryType, mPlayType,
                                        mChooseNumberInfoBean.getGe(), mChooseNumberInfoBean.getShi(), mChooseNumberInfoBean.getCount());
                                break;
                        }
                        break;
                    case AppConstants.LOTTERY_TYPE_11_5:
                    case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
                    case AppConstants.LOTTERY_TYPE_11_5_OLD:
                    case AppConstants.LOTTERY_TYPE_11_5_YILE:
                    case AppConstants.LOTTERY_TYPE_11_5_YUE:
                        mPresenter.create115LocalOrder(mChooseNumberInfoBean.getWan(), mChooseNumberInfoBean.getQian(),
                                mChooseNumberInfoBean.getBai(), mChooseNumberInfoBean.getCount(), mLotteryType, mPlayType);
                        break;
                    case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR:
                    case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                        mPresenter.createAlwayColorLocalOrder(mLotteryType, mPlayType,
                                mChooseNumberInfoBean.getWan(), mChooseNumberInfoBean.getQian(), mChooseNumberInfoBean.getBai(),
                                mChooseNumberInfoBean.getShi(), mChooseNumberInfoBean.getGe(), mChooseNumberInfoBean.getCount());
                        break;
                }
                break;
        }
    }

    //postion 0大1小2单3双  -1不显示
    private void updateBtnStatus(int type, int position) {
        LotteryBall ball = new LotteryBall();
        switch (position) {
            case 0:
                ball.setNumber("大");
                break;
            case 1:
                ball.setNumber("小");
                break;
            case 2:
                ball.setNumber("单");
                break;
            case 3:
                ball.setNumber("双");
                break;
        }
        List<LotteryBall> lotteryBallTens = new ArrayList<>();
        if (position > -1)//清空号码不显示
            lotteryBallTens.add(ball);
        if (type == 0) {
            //十位
            onSelectBall(mLotteryType, mPlayType, 2, lotteryBallTens);
            for (int i = 0; i < mTenButtons.length; i++) {
                if (i == position) {
                    mTenButtons[i].setBackgroundResource(R.drawable.shape_bg_red_ball_selected);
                    mTenButtons[i].setTextColor(StaticResourceUtils.
                            getColorResourceById(R.color.color_app_white));
                } else {
                    mTenButtons[i].setBackgroundResource(R.drawable.shape_bg_ball_normal);
                    mTenButtons[i].setTextColor(StaticResourceUtils.
                            getColorResourceById(R.color.color_999999));
                }
            }
        }

        if (type == 1) {
            //个位
            onSelectBall(mLotteryType, mPlayType, 1, lotteryBallTens);
            for (int i = 0; i < mOneButtons.length; i++) {
                if (i == position) {
                    mOneButtons[i].setBackgroundResource(R.drawable.shape_bg_red_ball_selected);
                    mOneButtons[i].setTextColor(StaticResourceUtils.
                            getColorResourceById(R.color.color_app_white));
                } else {
                    mOneButtons[i].setBackgroundResource(R.drawable.shape_bg_ball_normal);
                    mOneButtons[i].setTextColor(StaticResourceUtils.
                            getColorResourceById(R.color.color_999999));
                }
            }
        }
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

    }

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        if (isOk) {
            if (null != mAdapter)
                mAdapter.clearSelect();
            if (playType == AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE) {
                updateBtnStatus(0, -1);
                updateBtnStatus(1, -1);
            }
            LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, mPlayType);
        } else {
            UIHelper.showTipDialog(getActivity(), msgStr, new ConfirmDialogCallback() {
                @Override
                public void onConfirmResult(boolean isOk) {

                }
            });
        }
    }

    @Override
    public void machineAddResult(LotteryOrder order, boolean isOk) {

    }

    @Override
    public void deleteResult(boolean isOk, Object errorStr) {

    }

    @Override
    public void showWaitingDialog(String msgStr) {

    }

    @Override
    public void hiddenWaitingDialog() {

    }

    @Override
    public void initLotteryOrderListResult(List<LotteryOrder> mOrders) {

    }

    @Override
    public void onRequestSequence(boolean b, Object msgStr) {

    }

    /**
     * @param dx 控制左右滑动的偏移量
     */
    public void scroll(int dx) {
        if (!isViewCreated()) {
            return;
        }
        mRv.scrollBy(dx, 0);
    }

    @Override
    public void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls) {
        if (null != mChooseNumSelectedListener) {
            mChooseNumSelectedListener.onSelectBall(lotteryType, playType, numPos, balls);
        }
        LotteryUtils.saveChooseNumberCache(lotteryType, playType, numPos, balls);
        mChooseNumberInfoBean = CalculateCountUtils.getLotteryChoosePriceInfo(lotteryType, playType);
        //        StringBuilder sbPrice = new StringBuilder();
        //        if (null != mChooseNumberInfoBean.getPrice() && null != mChooseNumberInfoBean.getWinPrice()) {
        //            if (mChooseNumberInfoBean.getPrice().size() > 0 && mChooseNumberInfoBean.getWinPrice().size() > 0) {
        //                sbPrice.append("奖金");
        //                for (String price : mChooseNumberInfoBean.getPrice()) {
        //                    sbPrice.append("<font color=\"#dd3048\">" + price + "</font>");
        //                    sbPrice.append("~");
        //                }
        //                sbPrice.delete(sbPrice.length() - 1, sbPrice.length());
        //                sbPrice.append("元");
        //                sbPrice.append("，盈利");
        //                for (String price : mChooseNumberInfoBean.getWinPrice()) {
        //                    sbPrice.append("<font color=\"#dd3048\">" + price + "</font>");
        //                    sbPrice.append("~");
        //                }
        //                sbPrice.delete(sbPrice.length() - 1, sbPrice.length());
        //                sbPrice.append("元");
        //                mLlPrice.setVisibility(View.VISIBLE);
        //                mTvPrice.setText(Html.fromHtml(sbPrice.toString()));
        //            } else {
        //                mLlPrice.setVisibility(View.GONE);
        //            }
        //        } else {
        //            mLlPrice.setVisibility(View.GONE);
        //        }
    }

    public static class MyHandler extends Handler {

        WeakReference<TrendChooseNumFragment> reference;

        public MyHandler(TrendChooseNumFragment trendChooseNumFragment) {
            reference = new WeakReference<>(trendChooseNumFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == reference.get()) {
                return;
            }
            switch (msg.what) {
                case MSG_UPDATE_COUNT_DOWN:
                    if (reference.get().isDestroy()) {
                        return;
                    }
                    reference.get().mTvEndTime.setText(DateUtils.formatTime_mm_ss(reference.get().mCountDownTime * 1000));
                    reference.get().mCountDownTime--;
                    if (reference.get().mCountDownTime >= 0) {
                        sendEmptyMessageDelayed(MSG_UPDATE_COUNT_DOWN, 1000);
                    } else {
                        reference.get().getPeriod();
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogF.d(TAG, "生命周期 onDestroy");
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        clearCache();
    }

    //下单后清空所有
    private void clearCache() {
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, -3);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, -2);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, -1);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, 0);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, 1);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, 2);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, 3);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, 4);
        LotteryUtils.clearChooseNumberCache(mLotteryType, mPlayType, 5);
    }

    public interface ChooseNumScrollerListener {
        void onScrollChanged(boolean isScrolling, int dx, int dy);
    }

    //是否允许购买
    public boolean getOrderState(){
        boolean state = false;
        LotteryPageDataResponseBean lotteryBean = SharedPreferencesUtils.getLotteryPageDataInfo();
        List<LotteryType> lotteryTypes = lotteryBean.getPayload().getLotteries();
        for (int i = 0; i < lotteryTypes.size(); i++) {
            LotteryType typeBean = lotteryTypes.get(i);
            if(TextUtils.equals(mLotteryType, typeBean.getName())){
                if(TextUtils.equals("2",typeBean.getLottery_state())){
                    state = true; //销售
                }else if(TextUtils.equals("1",typeBean.getLottery_state())){
                    state = false; //暂停销售
                }
            }
        }
        return state;
    }

}
