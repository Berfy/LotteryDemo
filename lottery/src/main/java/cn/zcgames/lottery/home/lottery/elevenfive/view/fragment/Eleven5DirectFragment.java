package cn.zcgames.lottery.home.lottery.elevenfive.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_DIRECT;

/**
 * 11选5 前二直选 前三直选
 * Created by admin on 2017/6/27.
 * Berfy修改 2018.9.5
 */
public class Eleven5DirectFragment extends BaseFragment implements ChooseNumSelectedListener,
        IEleven5Activity, ILotteryOrderActivity {

    private static final String TAG = "Eleven5DanFragment";

    private static final int BALL_TYPE_ONE = 0;
    private static final int BALL_TYPE_TOW = 1;
    private static final int BALL_TYPE_THREE = 2;

    @BindView(R.id.danLabel_tv)
    TextView mLabelTv;
    @BindView(R.id.one_rv)
    RecyclerView mOneRv;
    @BindView(R.id.one_rl)
    RelativeLayout mOneRl;
    @BindView(R.id.tow_rv)
    RecyclerView mTowRv;
    @BindView(R.id.tow_rl)
    RelativeLayout mTowRl;
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
    @BindView(R.id.three_rv)
    RecyclerView mThreeRv;
    @BindView(R.id.three_rl)
    LinearLayout mThreeRl;

    Unbinder unbinder;
    private String mLotteryType;
    private int mPlayType;
    private Eleven5BallAdapter mOneAdapter, mTowAdapter, mThreeAdapter;

    private List<LotteryBall> mOneBallList, mTowBallList, mThreeBallList;
    private Eleven5Presenter mPresenter;
    private LotteryOrderPresenter mOrderPresenter;
    private Context mContext;
    private long mCount;

    public static Eleven5DirectFragment newInstance(String lotteryType) {
        Eleven5DirectFragment fragment = new Eleven5DirectFragment();
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
        View view = inflater.inflate(R.layout.fragment_11_5_direct, container, false);
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
        mOneAdapter = new Eleven5BallAdapter(mContext, BALL_TYPE_ONE, false, this);
        mOneRv.setAdapter(mOneAdapter);

        mTowAdapter = new Eleven5BallAdapter(mContext, BALL_TYPE_TOW, false, this);
        mTowRv.setAdapter(mTowAdapter);

        mThreeAdapter = new Eleven5BallAdapter(mContext, BALL_TYPE_THREE, false, this);
        mThreeRv.setAdapter(mThreeAdapter);
    }

    private void initView() {
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
        mOneRv.setLayoutManager(new GridLayoutManager(mContext, 6));
        mOneRv.addItemDecoration(space);
        mTowRv.setLayoutManager(new GridLayoutManager(mContext, 6));
        mTowRv.addItemDecoration(space);
        mThreeRv.setLayoutManager(new GridLayoutManager(mContext, 6));
        mThreeRv.addItemDecoration(space);
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
        if (mOneBallList != null) {
            mOneBallList.clear();
        }
        if (mThreeBallList != null) {
            mThreeBallList.clear();
        }
        if (mTowBallList != null) {
            mTowBallList.clear();
        }
    }

    private void showView() {
        switch (mPlayType) {
            case PLAY_11_5_FRONT_2_DIRECT://前二直选
                mLabelTv.setText(Html.fromHtml("每位至少选1个号，按位猜对开奖前2位即中<font color=\"#dd3048\"> 130 </font>元"));
                UIHelper.showWidget(mThreeRl, false);
                break;
            case PLAY_11_5_FRONT_3_DIRECT://前三直选
                mLabelTv.setText(Html.fromHtml("每位至少选1个号，按位猜对开奖前3位即中<font color=\"#dd3048\"> 1170 </font>元"));
                UIHelper.showWidget(mThreeRl, true);
                break;
        }
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
                mOrderPresenter.create115LocalOrder(mOneBallList, mTowBallList, mThreeBallList, mCount, mLotteryType, mPlayType);
                break;
        }
    }

    private void clearAdapter() {
        if (mOneAdapter != null) {
            mOneAdapter.clearSelectedBall();
        }
        if (mTowAdapter != null) {
            mTowAdapter.clearSelectedBall();
        }
        if (mThreeAdapter != null) {
            mThreeAdapter.clearSelectedBall();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        if (type == BALL_TYPE_ONE) {
            this.mOneBallList = balls;
//            mTowAdapter.setIgnoreData(balls);
//            mThreeAdapter.setIgnoreData(balls);
        } else if (type == BALL_TYPE_TOW) {
            this.mTowBallList = balls;
//            mOneAdapter.setIgnoreData(balls);
//            mThreeAdapter.setIgnoreData(balls);
        } else {
            this.mThreeBallList = balls;
//            mOneAdapter.setIgnoreData(balls);
//            mTowAdapter.setIgnoreData(balls);
        }
        if ((null != mOneBallList && mOneBallList.size() > 0)
                || (null != mTowBallList && mTowBallList.size() > 0)
                || (null != mThreeBallList && mThreeBallList.size() > 0)) {
            mBtnMachine.setVisibility(View.GONE);
            mDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mDeleteBtn.setVisibility(View.GONE);
        }
        mPresenter.getCountByPlayStyle(mOneBallList, mTowBallList, mThreeBallList, mPlayType);
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
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

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
