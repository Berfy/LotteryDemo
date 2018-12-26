package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.annotation.SuppressLint;
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
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.presenter.LotteryOrderPresenter;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.home.presenter.FastThreeFragmentPresenter;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.listener.FastThreeSelectedListener;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_SUM;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_BIG;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_DOUBLE;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_DOUBLE_BIG;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_DOUBLE_SMALL;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_NO;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_SINGLE;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_SINGLE_BIG;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_SINGLE_SMALL;
import static cn.zcgames.lottery.home.view.adapter.SumFragmentAdapter.QUICK_SELECT_RESULT_SMALL;

/**
 * Created by admin on 2017/6/19.
 */

public class SumFragment extends BaseFragment implements FastThreeSelectedListener, IBaseView, ILotteryOrderActivity {

    private static final String TAG = "SumFragment";

    private static final int MSG_SHOW_OPTIONS = 0;
    private static final int MSG_UPDATE_ORDER_INFO = 1;
    private static final int MSG_RESET_BUTTON = 2;

    @BindView(R.id.recyclerView_sum)
    RecyclerView mSumRv;
    @BindView(R.id.sum_btn_big)
    TextView mBtnBig;
    @BindView(R.id.sum_btn_small)
    TextView mBtnSmall;
    @BindView(R.id.sum_btn_single)
    TextView mBtnSingle;
    @BindView(R.id.sum_btn_double)
    TextView mBtnDouble;
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

    private List<TextView> mButtonList;

    Unbinder unbinder;
    SumFragmentAdapter mAdapter;
    FastThreeFragmentPresenter mPresenter;
    private LotteryOrderPresenter mLotteryOrderPresenter;

    private List<LotteryBall> mButtons = new ArrayList<>();
    private List<LotteryBall> mSelectButtons;
    private int mCount = 0;
    private String mLotteryType;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_OPTIONS:
                    if (msg.obj != null && msg.obj instanceof List) {
                        mButtons = (List<LotteryBall>) msg.obj;
                        mAdapter = new SumFragmentAdapter(AppConstants.FAST_THREE_SUM, mButtons, getActivity(), SumFragment.this);
                        mSumRv.setAdapter(mAdapter);
                    }
                    break;
                case MSG_UPDATE_ORDER_INFO:
                    if (msg.obj != null && msg.obj instanceof List) {
                        mSelectButtons = (List<LotteryBall>) msg.obj;
                        mCount = mSelectButtons.size();
                        mTvNum.setText("共" + mCount + "注");
                        mTvMoney.setText(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
                    }
                    break;
                case MSG_RESET_BUTTON:
                    mBtnDouble.setSelected(false);
                    mBtnSmall.setSelected(false);
                    mBtnBig.setSelected(false);
                    mBtnSingle.setSelected(false);
                    updateBtnStatus(-1, -1);
                    break;
            }
        }
    };

    public static SumFragment newInstance(String lotteryType) {
        SumFragment fragment = new SumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View sumView = inflater.inflate(R.layout.fragment_sum, container, false);
        unbinder = ButterKnife.bind(this, sumView);
        initView(sumView);
        initBundle();
        initPresenter();
        return sumView;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            LogF.d(TAG, "和值参数为空");
            return;
        }
        mLotteryType = bundle.getString(ActivityConstants.PARAM_LOTTERY_TYPE);
        LogF.d(TAG, "和值参数 " + mLotteryType);
    }

    private void initPresenter() {
        mPresenter = new FastThreeFragmentPresenter(getActivity(), this);
        mLotteryOrderPresenter = new LotteryOrderPresenter(getActivity(), this);
        mPresenter.requestSumFragmentOptions();
    }

    private void initView(View sumView) {
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
        mSumRv.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mSumRv.addItemDecoration(space);

        //防止scrollView自动滑动到底部
        View parentView = sumView.findViewById(R.id.sumFragment_view);
        parentView.setFocusable(true);
        parentView.setFocusableInTouchMode(true);
        parentView.requestFocus();

        mButtonList = new ArrayList<>();
        mButtonList.add(mBtnBig);
        mButtonList.add(mBtnSmall);
        mButtonList.add(mBtnSingle);
        mButtonList.add(mBtnDouble);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.sum_btn_big, R.id.sum_btn_small, R.id.sum_btn_single,
            R.id.sum_btn_double, R.id.threeD_btn_machine, R.id.threeD_ib_delete, R.id.threeD_tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sum_btn_big:
                if (mBtnSmall.isSelected()) {
                    mBtnSmall.setSelected(false);
                }
                if (!mBtnBig.isSelected()) {
                    mBtnBig.setSelected(true);
                    mBtnBig.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                    mBtnSmall.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                } else {
                    mBtnBig.setSelected(false);
                    mBtnBig.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                    mBtnSmall.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                }
                buttonStatus();
                break;
            case R.id.sum_btn_small:
                if (mBtnBig.isSelected()) {
                    mBtnBig.setSelected(false);
                } else {
                }
                if (!mBtnSmall.isSelected()) {
                    mBtnSmall.setSelected(true);
                    mBtnSmall.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                    mBtnBig.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                } else {
                    mBtnSmall.setSelected(false);
                    mBtnSmall.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                    mBtnBig.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                }
                buttonStatus();
                break;
            case R.id.sum_btn_single:
                if (mBtnDouble.isSelected()) {
                    mBtnDouble.setSelected(false);
                } else {
                }
                if (!mBtnSingle.isSelected()) {
                    mBtnSingle.setSelected(true);
                    mBtnSingle.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                    mBtnDouble.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                } else {
                    mBtnSingle.setSelected(false);
                    mBtnSingle.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                    mBtnDouble.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                }
                buttonStatus();
                break;
            case R.id.sum_btn_double:
                if (mBtnSingle.isSelected()) {
                    mBtnSingle.setSelected(false);
                } else {

                }
                if (!mBtnDouble.isSelected()) {
                    mBtnDouble.setSelected(true);
                    mBtnDouble.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                    mBtnSingle.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                } else {
                    mBtnDouble.setSelected(false);
                    mBtnDouble.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                    mBtnSingle.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                }

                buttonStatus();
                break;
            case R.id.threeD_btn_machine:
                LotteryOrder lotteryOrder = LotteryUtils.machine(mLotteryType, FAST_THREE_SUM);
                if (null == lotteryOrder) {
                    return;
                }
                LotteryOrderDbUtils.addLotteryOrder(lotteryOrder);
                LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_SUM);
                getActivity().finish();
                break;
            case R.id.threeD_ib_delete:
                mAdapter.clearSelect();
                break;
            case R.id.threeD_tv_ok:
                mLotteryOrderPresenter.createFast3SingleLocalOrder(mLotteryType, FAST_THREE_SUM, mSelectButtons, mCount);
                break;
        }
    }

    private void buttonStatus() {
        mAdapter.selectStatus(mBtnBig.isSelected(), mBtnSmall.isSelected(),
                mBtnSingle.isSelected(), mBtnDouble.isSelected());
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        Message msg = new Message();
        msg.what = MSG_SHOW_OPTIONS;
        msg.obj = object;
        if (null != mHandler)
            mHandler.sendMessage(msg);
    }

    @Override
    public void showTipDialog(String msgStr) {

    }

    @Override
    public void hideTipDialog() {

    }

    @Override
    public void onSelectBall(int playType, int type, List<LotteryBall> balls) {
        Message msg = new Message();
        msg.what = MSG_UPDATE_ORDER_INFO;
        msg.obj = balls;
        if (null != mHandler)
            mHandler.sendMessage(msg);
        if (balls.size() > 0) {
            mBtnMachine.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mBtnDelete.setVisibility(View.GONE);
        }
        switch (type) {
            case QUICK_SELECT_RESULT_NO:
                if (null != mHandler)
                    mHandler.sendEmptyMessage(MSG_RESET_BUTTON);
                break;
            case QUICK_SELECT_RESULT_BIG:
                updateBtnStatus(-1, 0);
                break;
            case QUICK_SELECT_RESULT_SMALL:
                updateBtnStatus(-1, 1);
                break;
            case QUICK_SELECT_RESULT_SINGLE:
                updateBtnStatus(-1, 2);
                break;
            case QUICK_SELECT_RESULT_DOUBLE:
                updateBtnStatus(-1, 3);
                break;
            case QUICK_SELECT_RESULT_DOUBLE_BIG:
                updateBtnStatus(0, 3);
                break;
            case QUICK_SELECT_RESULT_DOUBLE_SMALL:
                updateBtnStatus(1, 3);
                break;
            case QUICK_SELECT_RESULT_SINGLE_BIG:
                updateBtnStatus(0, 2);
                break;
            case QUICK_SELECT_RESULT_SINGLE_SMALL:
                updateBtnStatus(1, 2);
                break;
        }
    }

    //big,small,single,double
    private void updateBtnStatus(int a, int b) {
        for (int i = 0; i < mButtonList.size(); i++) {
            TextView tv = mButtonList.get(i);
            if (i == a || i == b) {
                tv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
            } else {
                tv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_black));
            }
        }
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

    }

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        if (isOk) {
            mAdapter.clearSelect();
            LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_SUM);
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
