package cn.zcgames.lottery.home.lottery.alwaycolor.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.view.adapter.ThreeDBallAdapter;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.base.BaseFragment;

import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;

/**
 * Created by admin on 2017/6/27.
 */

public class FiveAllFragment extends BaseFragment implements ChooseNumSelectedListener, ILotteryOrderActivity {
    private static final String TAG = "FiveAllFragment";

    @BindView(R.id.recyclerView_million)
    RecyclerView mMillionRv;
    @BindView(R.id.recyclerView_thousand)
    RecyclerView mThousandRv;
    @BindView(R.id.recyclerView_hundred)
    RecyclerView mHundredRv;
    @BindView(R.id.recyclerView_ten)
    RecyclerView mTenRv;
    @BindView(R.id.recyclerView_one)
    RecyclerView mOneRv;
    @BindView(R.id.threeD_btn_machine)
    Button mBtnMachine;
    @BindView(R.id.threeD_ib_delete)
    Button mBtnDelete;
    @BindView(R.id.threeD_tv_num)
    TextView alwaysTvNum;
    @BindView(R.id.threeD_tv_money)
    TextView alwaysTvMoney;
    @BindView(R.id.threeD_tv_ok)
    TextView alwaysTvOk;
    Unbinder unbinder;
    @BindView(R.id.label0)
    TextView label10;
    @BindView(R.id.label3)
    TextView label3;
    @BindView(R.id.rl_million)
    RelativeLayout rlMillion;
    @BindView(R.id.rl_thousand)
    RelativeLayout rlThousand;
    @BindView(R.id.rl_hundred)
    RelativeLayout rlHundred;
    @BindView(R.id.rl_ten)
    RelativeLayout rlTen;

    private ThreeDBallAdapter mTenAdapter, mOneAdapter, mHundredAdapter, mThousandAdapter, mMillionAdapter;
    //adapter的类型
    private int mOneType = 0;
    private int mTenType = 1;
    private int mHundredType = 2;
    private int mThousandType = 3;
    private int mMillionType = 4;

    private int mBallNumberPerRow = 5;
    private int mPlayType;
    private String mLotteryType;

    private LotteryOrderPresenter mPresenter;

    public static FiveAllFragment newInstance(String lotteryType, int playType) {
        FiveAllFragment fragment = new FiveAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        bundle.putInt(ActivityConstants.PARAM_LOTTERY_PLAY_TYPE, playType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_five_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        initBundle();
        initView();
        initAdapter();
        updateViewByPlayType();
        initPresenter();
        return view;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            LogF.d(TAG, "和值参数为空");
            return;
        }
        mLotteryType = bundle.getString(ActivityConstants.PARAM_LOTTERY_TYPE);
        mPlayType = bundle.getInt(ActivityConstants.PARAM_LOTTERY_PLAY_TYPE);
    }

    private void initPresenter() {
        mPresenter = new LotteryOrderPresenter(getActivity(), this);
    }

    private void initView() {
        mHundredRv.setLayoutManager(createGridLayoutManager());
        mTenRv.setLayoutManager(createGridLayoutManager());
        mOneRv.setLayoutManager(createGridLayoutManager());
        mThousandRv.setLayoutManager(createGridLayoutManager());
        mMillionRv.setLayoutManager(createGridLayoutManager());
        updateViewByPlayType();
    }

    private GridLayoutManager createGridLayoutManager() {
        return new GridLayoutManager(getActivity(), mBallNumberPerRow);
    }

    private void updateViewByPlayType() {
        if (label10 == null) {
            return;
        }
        if (mPlayType == AppConstants.ALWAYS_COLOR_5_DIRECT) {
            label10.setText(Html.fromHtml("每位至少选1个号，按位猜对开奖号即中<font color=\"#dd3048\"> 100000 </font>元"));
            label3.setText("个位");
        } else if (mPlayType == AppConstants.ALWAYS_COLOR_5_ALL) {
            label10.setText(Html.fromHtml("每位至少选1个号，按位猜对开奖号最高奖<font color=\"#dd3048\"> 20440 </font>元"));
            label3.setText("个位");
        } else if (mPlayType == AppConstants.ALWAYS_COLOR_3_DIRECT) {
            label10.setText(Html.fromHtml("每位至少选1个号，按位猜对开奖后3位即中<font color=\"#dd3048\"> 1000 </font>元"));
            label3.setText("个位");
            if (rlMillion != null) {
                UIHelper.showWidget(rlMillion, false);
            }
            if (rlThousand != null) {
                UIHelper.showWidget(rlThousand, false);
            }
        } else if (mPlayType == AppConstants.ALWAYS_COLOR_2_DIRECT) {
            label10.setText(Html.fromHtml("每位至少选1个号，按位猜对开奖后2位即中<font color=\"#dd3048\"> 100 </font>元"));
            label3.setText("个位");
            if (rlMillion != null) {
                UIHelper.showWidget(rlMillion, false);
            }
            if (rlThousand != null) {
                UIHelper.showWidget(rlThousand, false);
            }
            if (rlHundred != null) {
                UIHelper.showWidget(rlHundred, false);
            }
        } else if (mPlayType == AppConstants.ALWAYS_COLOR_2_GROUP) {
            label10.setText(Html.fromHtml("至少选2个号，猜对开奖后2位（顺序不限）即中<font color=\"#dd3048\"> 50 </font>元"));
            label3.setText("选号");
            if (rlMillion != null) {
                UIHelper.showWidget(rlMillion, false);
            }
            if (rlThousand != null) {
                UIHelper.showWidget(rlThousand, false);
            }
            if (rlHundred != null) {
                UIHelper.showWidget(rlHundred, false);
            }
            if (rlTen != null) {
                UIHelper.showWidget(rlTen, false);
            }
        } else if (mPlayType == AppConstants.ALWAYS_COLOR_3_GROUP_3) {
            label10.setText(Html.fromHtml("至少选2个号，猜对开奖后3位（顺序不限）中<font color=\"#dd3048\"> 320 </font>元"));
            label3.setText("选号");
            if (rlMillion != null) {
                UIHelper.showWidget(rlMillion, false);
            }
            if (rlThousand != null) {
                UIHelper.showWidget(rlThousand, false);
            }
            if (rlHundred != null) {
                UIHelper.showWidget(rlHundred, false);
            }
            if (rlTen != null) {
                UIHelper.showWidget(rlTen, false);
            }
        } else if (mPlayType == AppConstants.ALWAYS_COLOR_3_GROUP_6) {
            label10.setText(Html.fromHtml("至少选3个号，猜对开奖后3位（顺序不限）中<font color=\"#dd3048\"> 160 </font>元"));
            label3.setText("选号");
            if (rlMillion != null) {
                UIHelper.showWidget(rlMillion, false);
            }
            if (rlThousand != null) {
                UIHelper.showWidget(rlThousand, false);
            }
            if (rlHundred != null) {
                UIHelper.showWidget(rlHundred, false);
            }
            if (rlTen != null) {
                UIHelper.showWidget(rlTen, false);
            }
        } else if (mPlayType == ALWAYS_COLOR_1_DIRECT) {
            label10.setText(Html.fromHtml("至少选1个号，猜对开奖号码最后1位即中<font color=\"#dd3048\"> 10 </font>元"));
            label3.setText("个位");
            if (rlMillion != null) {
                UIHelper.showWidget(rlMillion, false);
            }
            if (rlThousand != null) {
                UIHelper.showWidget(rlThousand, false);
            }
            if (rlHundred != null) {
                UIHelper.showWidget(rlHundred, false);
            }
            if (rlTen != null) {
                UIHelper.showWidget(rlTen, false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initAdapter() {
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);

        mMillionAdapter = new ThreeDBallAdapter(getActivity(), mMillionType, this);
        mMillionRv.setAdapter(mMillionAdapter);
        mMillionRv.addItemDecoration(space);

        mThousandAdapter = new ThreeDBallAdapter(getActivity(), mThousandType, this);
        mThousandRv.setAdapter(mThousandAdapter);
        mThousandRv.addItemDecoration(space);

        mHundredAdapter = new ThreeDBallAdapter(getActivity(), mHundredType, this);
        mHundredRv.setAdapter(mHundredAdapter);
        mHundredRv.addItemDecoration(space);

        mTenAdapter = new ThreeDBallAdapter(getActivity(), mTenType, this);
        mTenRv.setAdapter(mTenAdapter);
        mTenRv.addItemDecoration(space);

        mOneAdapter = new ThreeDBallAdapter(getActivity(), mOneType, this);
        mOneRv.setAdapter(mOneAdapter);
        mOneRv.addItemDecoration(space);
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
                LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, mPlayType);
                getActivity().finish();
                break;
            case R.id.threeD_ib_delete:
                mTenAdapter.clearSelectedBall();
                mOneAdapter.clearSelectedBall();
                mHundredAdapter.clearSelectedBall();
                mThousandAdapter.clearSelectedBall();
                mMillionAdapter.clearSelectedBall();
                break;
            case R.id.threeD_tv_ok:
                mPresenter.createAlwayColorLocalOrder(mLotteryType,
                        mPlayType, mMillionBalls, mThousandBall, mHundredBall, mTenBall, mOneBall, mCount);
                break;
        }
    }

    private List<LotteryBall> mMillionBalls = new ArrayList<>();
    private List<LotteryBall> mThousandBall = new ArrayList<>();
    private List<LotteryBall> mHundredBall = new ArrayList<>();
    private List<LotteryBall> mTenBall = new ArrayList<>();
    private List<LotteryBall> mOneBall = new ArrayList<>();

    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        if (type == mMillionType) {
            mMillionBalls = balls;
        } else if (type == mThousandType) {
            mThousandBall = balls;
        } else if (type == mHundredType) {
            mHundredBall = balls;
        } else if (type == mTenType) {
            mTenBall = balls;
        } else if (type == mOneType) {
            mOneBall = balls;
        }
        if ((null != mMillionBalls && mMillionBalls.size() > 0)
                || (null != mThousandBall && mThousandBall.size() > 0)
                || (null != mHundredBall && mHundredBall.size() > 0)
                || (null != mTenBall && mTenBall.size() > 0)
                || (null != mOneBall && mOneBall.size() > 0)) {
            mBtnMachine.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mBtnDelete.setVisibility(View.GONE);
        }
        updateCount();
    }

    private long mCount = 0;

    private void updateCount() {
        mCount = CalculateCountUtils.getACCount(mPlayType, mMillionBalls, mThousandBall, mHundredBall, mTenBall, mOneBall);
        alwaysTvNum.setText("共" + mCount + "注");
        alwaysTvMoney.setText(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

    }

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        if (isOk) {
            LotteryOrderActivity.intoThisActivity(getActivity(), LOTTERY_TYPE_ALWAYS_COLOR, mPlayType);
            getActivity().finish();
        } else {
            showTipMessageDialog(msgStr);
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

    private void showTipMessageDialog(String msgString) {
        AlertDialog dialog = new AlertDialog(getActivity())
                .builder()
                .setMsg(msgString)
                .setCancelable(false)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.show();
    }
}
