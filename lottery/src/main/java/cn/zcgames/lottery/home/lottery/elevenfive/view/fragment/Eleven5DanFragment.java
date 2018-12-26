package cn.zcgames.lottery.home.lottery.elevenfive.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.listener.ChooseNumSelectedListener;
import cn.zcgames.lottery.home.presenter.LotteryOrderPresenter;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.home.lottery.elevenfive.presenter.Eleven5Presenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.callback.ConfirmDialogCallback;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.view.adapter.Eleven5BallAdapter;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.view.iview.IEleven5Activity;

import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_3_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_4_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_5_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_6_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_7_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_8_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN;

/**
 * Created by admin on 2017/6/27.
 */

public class Eleven5DanFragment extends BaseFragment implements ChooseNumSelectedListener,
        IEleven5Activity, ILotteryOrderActivity {

    private static final String TAG = "Eleven5DanFragment";

    private static final int BALL_TYPE_DAN = 0;
    private static final int BALL_TYPE_TUO = 1;

    @BindView(R.id.danLabel_tv)
    TextView mDanLabelTv;
    @BindView(R.id.danLabel_price_tv)
    TextView mDanPriceLabelTv;
    @BindView(R.id.dan_rv)
    RecyclerView mDanRv;
    @BindView(R.id.dan_rl)
    RelativeLayout mDanRl;
    @BindView(R.id.tuoLabel_tv)
    TextView mTuoLabelTv;
    @BindView(R.id.tuo_rv)
    RecyclerView mTuoRv;
    @BindView(R.id.tuo_rl)
    RelativeLayout mTuoRl;
    @BindView(R.id.threeD_btn_machine)
    Button mBtnMachine;
    @BindView(R.id.threeD_ib_delete)
    Button mDeleteBtn;
    @BindView(R.id.threeD_tv_num)
    TextView mCountTv;
    @BindView(R.id.threeD_tv_money)
    TextView mMoneyTv;
    @BindView(R.id.threeD_tv_ok)
    TextView mOkBtn;

    Unbinder unbinder;
    private String mLotteryType;
    private int mPlayType;
    private Eleven5BallAdapter mDanAdapter, mTuoAdapter;

    private List<LotteryBall> mDanBallList;
    private List<LotteryBall> mTuoBallList;
    private Eleven5Presenter mPresenter;
    private LotteryOrderPresenter mOrderPresenter;
    private Context mContext;
    private long mCount;

    public static Eleven5DanFragment newInstance(String lotteryType) {
        Eleven5DanFragment fragment = new Eleven5DanFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_11_5_dan, container, false);
        unbinder = ButterKnife.bind(this, view);
        initBundle();
        initView();
        initAdapter();
        showView();
        initPresenter();
        return view;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        mLotteryType = bundle.getString(ActivityConstants.PARAM_LOTTERY_TYPE);
    }

    private void initPresenter() {
        mPresenter = new Eleven5Presenter(getActivity(), this);
        mOrderPresenter = new LotteryOrderPresenter(getActivity(), this);
    }

    private void initAdapter() {
        mDanAdapter = new Eleven5BallAdapter(mContext, BALL_TYPE_DAN, this);
        mDanRv.setAdapter(mDanAdapter);

        mTuoAdapter = new Eleven5BallAdapter(mContext, BALL_TYPE_TUO, this);
        mTuoAdapter.setMaxSelectBallNum(10);
        mTuoRv.setAdapter(mTuoAdapter);
    }

    private void initView() {
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
        mDanRv.setLayoutManager(new GridLayoutManager(mContext, 6));
        mDanRv.addItemDecoration(space);
        mTuoRv.setLayoutManager(new GridLayoutManager(mContext, 6));
        mTuoRv.addItemDecoration(space);
    }

    public void setPlayStyle(int type) {
        this.mPlayType = type;
        if (isAdded()) {
            showView();
            resetDataView();
        }
    }

    private void resetDataView() {
        mCountTv.setText(0 + "注");
        mMoneyTv.setText(0 + "元");
        clearAdapter();
        if (mDanBallList != null) {
            mDanBallList.clear();
        }
        if (mTuoBallList != null) {
            mTuoBallList.clear();
        }
    }

    private void showView() {
        String danTip = "";
        String tuoTip = "我认为可能出的号码";
        switch (mPlayType) {
            case PLAY_11_5_ANY_2_DAN:
                danTip = "我认为必出的号码 至少选1个，最多1个";
                mDanPriceLabelTv.setText(Html.fromHtml("猜对任意2个开奖号即中<font color=\"#dd3048\"> 6 </font>元"));
                mDanAdapter.setMaxSelectBallNum(1);
                break;
            case PLAY_11_5_FRONT_2_GROUP_DAN:
                danTip = "我认为必出的号码 至少选1个，最多1个";
                mDanPriceLabelTv.setText(Html.fromHtml("猜对前2个开奖号即中<font color=\"#dd3048\"> 65 </font>元"));
                mDanAdapter.setMaxSelectBallNum(1);
                break;
            case PLAY_11_5_ANY_3_DAN:
                danTip = "我认为必出的号码 至少选1个，最多2个";
                mDanPriceLabelTv.setText(Html.fromHtml("猜对任意3个开奖号即中<font color=\"#dd3048\"> 19 </font>元"));
                mDanAdapter.setMaxSelectBallNum(2);
                break;
            case PLAY_11_5_FRONT_3_GROUP_DAN:
                danTip = "我认为必出的号码 至少选1个，最多2个";
                mDanPriceLabelTv.setText(Html.fromHtml("猜对前3个开奖号即中<font color=\"#dd3048\"> 195 </font>元"));
                mDanAdapter.setMaxSelectBallNum(2);
                break;
            case PLAY_11_5_ANY_4_DAN:
                danTip = "我认为必出的号码 至少选1个，最多3个";
                mDanPriceLabelTv.setText(Html.fromHtml("猜对任意4个开奖号即中<font color=\"#dd3048\"> 78 </font>元"));
                mDanAdapter.setMaxSelectBallNum(3);
                break;
            case PLAY_11_5_ANY_5_DAN:
                danTip = "我认为必出的号码 至少选1个，最多4个";
                mDanPriceLabelTv.setText(Html.fromHtml("猜对全部5个开奖号即中<font color=\"#dd3048\"> 540 </font>元"));
                mDanAdapter.setMaxSelectBallNum(4);
                break;
            case PLAY_11_5_ANY_6_DAN:
                danTip = "我认为必出的号码 至少选1个，最多5个";
                mDanPriceLabelTv.setText(Html.fromHtml("选号包含5个开奖号即中<font color=\"#dd3048\"> 90 </font>元"));
                mDanAdapter.setMaxSelectBallNum(5);
                break;
            case PLAY_11_5_ANY_7_DAN:
                danTip = "我认为必出的号码 至少选1个，最多6个";
                mDanPriceLabelTv.setText(Html.fromHtml("选号包含5个开奖号即中<font color=\"#dd3048\"> 26 </font>元"));
                mDanAdapter.setMaxSelectBallNum(6);
                break;
            case PLAY_11_5_ANY_8_DAN:
                danTip = "我认为必出的号码 至少选1个，最多7个";
                mDanPriceLabelTv.setText(Html.fromHtml("选号包含5个开奖号即中<font color=\"#dd3048\"> 9 </font>元"));
                mDanAdapter.setMaxSelectBallNum(7);
                break;
        }
        mDanLabelTv.setText(danTip);
        mTuoLabelTv.setText(tuoTip);
    }

    @OnClick({R.id.threeD_btn_machine, R.id.threeD_ib_delete, R.id.threeD_tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.threeD_btn_machine:
                LotteryOrder lotteryOrder = LotteryUtils.machine(mLotteryType, mPlayType);
                if (null == lotteryOrder) {
                    return;
                }
                LotteryOrderDbUtils.addLotteryOrder(lotteryOrder);
                LotteryOrderActivity.intoThisActivity(mContext, mLotteryType, mPlayType);
                getActivity().finish();
                break;
            case R.id.threeD_ib_delete:
                clearAdapter();
                break;
            case R.id.threeD_tv_ok:
                mOrderPresenter.create115LocalOrder(mDanBallList, mTuoBallList, null, mCount, mLotteryType, mPlayType);
                break;
        }
    }

    private void clearAdapter() {
        if (mDanAdapter != null) {
            mDanAdapter.clearSelectedBall();
        }
        if (mTuoAdapter != null) {
            mTuoAdapter.clearSelectedBall();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        LogF.d(TAG, "选择的球" + GsonUtil.getInstance().toJson(balls));
        Log.e(TAG, "onSelectBall: type is " + type);
        if (type == BALL_TYPE_DAN) {
            this.mDanBallList = balls;
            mTuoAdapter.setIgnoreData(mDanBallList);
        } else {
            this.mTuoBallList = balls;
            mDanAdapter.setIgnoreData(mTuoBallList);
        }
        if ((null != mDanBallList && mDanBallList.size() > 0)
                || (null != mTuoBallList && mTuoBallList.size() > 0)) {
            mBtnMachine.setVisibility(View.GONE);
            mDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mDeleteBtn.setVisibility(View.GONE);
        }
        mPresenter.getCountByPlayStyle(mDanBallList, mTuoBallList, null, mPlayType);
    }

    @Override
    public void requestResult(boolean isOk, Object object) {

    }

    @Override
    public void showTipDialog(String msgStr) {

    }

    @Override
    public void hideTipDialog() {

    }

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        if (isOk) {
            LotteryOrderActivity.intoThisActivity((Activity) mContext, mLotteryType, mPlayType);
            getActivity().finish();
        } else {
            UIHelper.showTipDialog((Activity) mContext, msgStr, new ConfirmDialogCallback() {
                @Override
                public void onConfirmResult(boolean isOk) {

                }
            });
        }
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

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
    public void onRequestSequence(boolean isOk, Object msg) {

    }

    @Override
    public void onTotalCount(long count) {
        this.mCount = count;
        mCountTv.setText(mCount + "注");
        mMoneyTv.setText(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }
}
