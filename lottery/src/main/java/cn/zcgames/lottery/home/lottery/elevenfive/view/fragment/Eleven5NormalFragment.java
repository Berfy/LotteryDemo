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

import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_3;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_4;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_5;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_6;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_7;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_8;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_GROUP;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_GROUP;

/**
 * Created by admin on 2017/6/27.
 */

public class Eleven5NormalFragment extends BaseFragment implements ChooseNumSelectedListener,
        IEleven5Activity, ILotteryOrderActivity {

    private static final String TAG = "Eleven5NormalFragment";

    @BindView(R.id.normalLabel_tv)
    TextView mLabelTv;
    @BindView(R.id.normal_rv)
    RecyclerView mNormalRv;
    @BindView(R.id.threeD_btn_machine)
    Button mBtnMachine;
    @BindView(R.id.threeD_ib_delete)
    Button mDeleteBtn;
    @BindView(R.id.threeD_tv_num)
    TextView mNumTv;
    @BindView(R.id.threeD_tv_money)
    TextView mMoneyTv;
    @BindView(R.id.threeD_tv_ok)
    TextView mOkBtn;

    Unbinder unbinder;
    private String mLotteryType;
    private int mPlayType;
    private Eleven5BallAdapter mAdapter;
    private Eleven5Presenter mPresenter;
    private LotteryOrderPresenter mOrderPresenter;

    private long mCount;

    private List<LotteryBall> mSelectedBallList;

    private Context mContext;

    public static Eleven5NormalFragment newInstance(String lotteryType) {
        Eleven5NormalFragment fragment = new Eleven5NormalFragment();
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
        View view = inflater.inflate(R.layout.fragment_11_5_normal, container, false);
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
        mAdapter = new Eleven5BallAdapter(mContext, -1, this);
        mNormalRv.setAdapter(mAdapter);

    }

    private void initView() {
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
        mNormalRv.setLayoutManager(new GridLayoutManager(mContext, 6));
        mNormalRv.addItemDecoration(space);
    }

    public void setPlayStyle(int type) {
        this.mPlayType = type;
        if (isAdded()) {
            showView();
            resetDataView();
        }
    }

    private void resetDataView() {
        mNumTv.setText(0 + "注");
        mMoneyTv.setText(0 + "元");
        clearAdapter();
        if (mSelectedBallList != null) {
            mSelectedBallList.clear();
        }
    }

    private void showView() {
        switch (mPlayType) {
            case PLAY_11_5_FRONT_1_DIRECT:
                mLabelTv.setText(Html.fromHtml("至少选1个号，猜对第1个开奖号即中<font color=\"#dd3048\"> 13 </font>元"));
                break;
            case PLAY_11_5_ANY_2:
                mLabelTv.setText(Html.fromHtml("至少选2个号，猜对任意2个开奖号即中<font color=\"#dd3048\"> 6 </font>元"));
                break;
            case PLAY_11_5_FRONT_2_GROUP:
                mLabelTv.setText(Html.fromHtml("至少选2个号，猜对前2个开奖号即中<font color=\"#dd3048\"> 65 </font>元"));
                break;
            case PLAY_11_5_ANY_3:
                mLabelTv.setText(Html.fromHtml("至少选3个号，猜对任意3个开奖号即中<font color=\"#dd3048\"> 19 </font>元"));
                break;
            case PLAY_11_5_FRONT_3_GROUP:
                mLabelTv.setText(Html.fromHtml("至少选3个号，猜对前3个开奖号即中<font color=\"#dd3048\"> 195 </font>元"));
                break;
            case PLAY_11_5_ANY_4:
                mLabelTv.setText(Html.fromHtml("至少选4个号，猜对任意4个开奖号即中<font color=\"#dd3048\"> 78 </font>元"));
                break;
            case PLAY_11_5_ANY_5:
                mLabelTv.setText(Html.fromHtml("至少选5个号，猜对全部5个开奖号即中<font color=\"#dd3048\"> 540 </font>元"));
                break;
            case PLAY_11_5_ANY_6:
                mLabelTv.setText(Html.fromHtml("至少选6个号，选号包含5个开奖号即中<font color=\"#dd3048\"> 90 </font>元"));
                break;
            case PLAY_11_5_ANY_7:
                mLabelTv.setText(Html.fromHtml("至少选7个号，选号包含5个开奖号即中<font color=\"#dd3048\"> 26 </font>元"));
                break;
            case PLAY_11_5_ANY_8:
                mLabelTv.setText(Html.fromHtml("至少选8个号，选号包含5个开奖号即中<font color=\"#dd3048\"> 9 </font>元"));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                mOrderPresenter.create115LocalOrder(mSelectedBallList, null, null, mCount, mLotteryType, mPlayType);
                break;
        }
    }

    private void clearAdapter() {
        if (mAdapter != null) {
            mAdapter.clearSelectedBall();
        }
    }

    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        this.mSelectedBallList = balls;
        if (balls.size() > 0) {
            mBtnMachine.setVisibility(View.GONE);
            mDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mDeleteBtn.setVisibility(View.GONE);
        }
        mPresenter.getCountByPlayStyle(mSelectedBallList, null, null, mPlayType);
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
        mNumTv.setText(mCount + "注");
        mMoneyTv.setText(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }
}
