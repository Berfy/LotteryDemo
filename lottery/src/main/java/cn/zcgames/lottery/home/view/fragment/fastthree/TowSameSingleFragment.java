package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

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
import cn.zcgames.lottery.home.presenter.LotteryOrderPresenter;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.home.presenter.FastThreeFragmentPresenter;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.view.adapter.ThreeDifferentAdapter;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.view.iview.ITowSameSingleFragment;
import cn.zcgames.lottery.home.listener.FastThreeSelectedListener;

import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_ONE;

/**
 * Created by admin on 2017/6/20.
 */

public class TowSameSingleFragment extends BaseFragment implements ITowSameSingleFragment,
        FastThreeSelectedListener, ILotteryOrderActivity {

    private static final String TAG = "TowSameSingleFragment";

    private Context mContext;

    private static final int MSG_SHOW_SAME_OPTION_BUTTON = 0;
    private static final int MSG_SHOW_DIFFERENT_OPTION_BUTTON = 1;
    private static final int MSG_SELECT_RESULT_SAME = 2;
    private static final int MSG_SELECT_RESULT_DIFFERENT = 3;
    private static final int MSG_SHOW_COUNT_VIEW = 4;

    private static final int ADAPTER_TYPE_SAME = 0;
    private static final int ADAPTER_TYPE_DIFFERENT = 1;

    @BindView(R.id.recyclerView_2Different)
    RecyclerView mDifferentRv;
    @BindView(R.id.recyclerView_2Same)
    RecyclerView mSameRv;
    @BindView(R.id.threeD_ib_delete)
    Button mBtnDelete;
    @BindView(R.id.threeD_btn_machine)
    Button mBtnMachine;
    @BindView(R.id.threeD_tv_num)
    TextView mTvNum;
    @BindView(R.id.threeD_tv_money)
    TextView mTvMoney;
    @BindView(R.id.threeD_tv_ok)
    TextView mTvOk;
    @BindView(R.id.sumFragment_view)
    RelativeLayout mFragmentView;
    Unbinder unbinder;

    private FastThreeFragmentPresenter mPresenter;
    private LotteryOrderPresenter mLotteryOrderPresenter;
    private int mSameCount = 0;
    private int mDifferentCount = 0;
    private ThreeDifferentAdapter mSameAdapter, mDifferentAdapter;
    private List<LotteryBall> mSameSelect, mDifferentSelect;
    private int mTotalCount = 0;
    private String mLotteryType;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_SAME_OPTION_BUTTON:
                    if (msg.obj != null && msg.obj instanceof List) {
                        List<LotteryBall> btns = (List<LotteryBall>) msg.obj;
                        mSameAdapter = new ThreeDifferentAdapter(AppConstants.FAST_THREE_2_SAME_ONE, btns, TowSameSingleFragment.this, ADAPTER_TYPE_SAME);
                        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
                        mSameRv.setLayoutManager(new GridLayoutManager(mContext, 6));
                        mSameRv.addItemDecoration(space);
                        mSameRv.setAdapter(mSameAdapter);
                    }
                    break;
                case MSG_SHOW_DIFFERENT_OPTION_BUTTON:
                    if (msg.obj != null && msg.obj instanceof List) {
                        List<LotteryBall> btns = (List<LotteryBall>) msg.obj;
                        mDifferentAdapter = new ThreeDifferentAdapter(AppConstants.FAST_THREE_2_SAME_ONE, btns, TowSameSingleFragment.this, ADAPTER_TYPE_DIFFERENT);
                        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
                        mDifferentRv.setLayoutManager(new GridLayoutManager(mContext, 6));
                        mDifferentRv.addItemDecoration(space);
                        mDifferentRv.setAdapter(mDifferentAdapter);
                    }
                    break;
                case MSG_SELECT_RESULT_SAME:
                    if (msg.obj != null && msg.obj instanceof List) {
                        mSameSelect = (List<LotteryBall>) msg.obj;
                        mSameCount = mSameSelect.size();
                        mHandler.sendEmptyMessage(MSG_SHOW_COUNT_VIEW);
                        mDifferentAdapter.setIgnoreData(mSameSelect);
                    }
                    break;
                case MSG_SELECT_RESULT_DIFFERENT:
                    if (msg.obj != null && msg.obj instanceof List) {
                        mDifferentSelect = (List<LotteryBall>) msg.obj;
                        mDifferentCount = mDifferentSelect.size();
                        mHandler.sendEmptyMessage(MSG_SHOW_COUNT_VIEW);
                        mSameAdapter.setIgnoreData(mDifferentSelect);
                    }
                    break;
                case MSG_SHOW_COUNT_VIEW:
                    mTotalCount = mSameCount * mDifferentCount;
                    mTvNum.setText("共" + mTotalCount + "注");
                    mTvMoney.setText(StringUtils.getNumberNoZero(mTotalCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
                    break;
            }
        }
    };

    public static TowSameSingleFragment newInstance(String lotteryType) {
        TowSameSingleFragment fragment = new TowSameSingleFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View threeSameSingleView = inflater.inflate(R.layout.fragment_tow_same_single, container, false);
        unbinder = ButterKnife.bind(this, threeSameSingleView);
        initBundle();
        initPresenter();
        return threeSameSingleView;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        mLotteryType = bundle.getString(ActivityConstants.PARAM_LOTTERY_TYPE);
    }

    private void initPresenter() {
        mLotteryOrderPresenter = new LotteryOrderPresenter(getActivity(), this);
        mPresenter = new FastThreeFragmentPresenter(getActivity(), this);
        mPresenter.requestTowSameSingleDifferent();
        mPresenter.requestTowSameSingleSame();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.threeD_btn_machine, R.id.threeD_ib_delete, R.id.threeD_tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.threeD_ib_delete:
                mSameAdapter.clearSelect();
                mDifferentAdapter.clearSelect();
                break;
            case R.id.threeD_btn_machine:
                LotteryOrder lotteryOrder = LotteryUtils.machine(mLotteryType, FAST_THREE_2_SAME_ONE);
                if (null == lotteryOrder) {
                    return;
                }
                LotteryOrderDbUtils.addLotteryOrder(lotteryOrder);
                LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_2_SAME_ONE);
                getActivity().finish();
                break;
            case R.id.threeD_tv_ok:
                mLotteryOrderPresenter.createFast3LocalOrder(mLotteryType, FAST_THREE_2_SAME_ONE, mSameSelect, mDifferentSelect, mTotalCount);
                break;
        }
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
    public void requestTowSameSingleSame(Object obj) {
        Message msg = new Message();
        msg.what = MSG_SHOW_SAME_OPTION_BUTTON;
        msg.obj = obj;
        if (null != mHandler)
            mHandler.sendMessage(msg);
    }

    @Override
    public void requestTowSameSingleDifferent(Object obj) {
        Message msg = new Message();
        msg.what = MSG_SHOW_DIFFERENT_OPTION_BUTTON;
        msg.obj = obj;
        if (null != mHandler)
            mHandler.sendMessage(msg);
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

    }

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        if (isOk) {
            mSameAdapter.clearSelect();
            mDifferentAdapter.clearSelect();
            LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_2_SAME_ONE);
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

    @Override
    public void onSelectBall(int playType, int type, List<LotteryBall> balls) {
        if (balls.size() > 0) {
            mBtnMachine.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mBtnDelete.setVisibility(View.GONE);
        }
        Message msg = new Message();
        msg.obj = balls;
        if (type == ADAPTER_TYPE_SAME) {
            msg.what = MSG_SELECT_RESULT_SAME;
        } else {
            msg.what = MSG_SELECT_RESULT_DIFFERENT;
        }
        if (null != mHandler)
            mHandler.sendMessage(msg);
    }
}
