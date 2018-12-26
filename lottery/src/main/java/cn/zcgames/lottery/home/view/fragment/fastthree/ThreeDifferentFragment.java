package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.listener.FastThreeSelectedListener;
import cn.zcgames.lottery.home.presenter.FastThreeFragmentPresenter;
import cn.zcgames.lottery.home.presenter.LotteryOrderPresenter;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.view.adapter.ThreeDifferentAdapter;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_DANSHI;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_FUSHI;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_TO_ALL;

/**
 * 三不同号 新
 *
 * @author Berfy
 * 2018.10.23
 */
public class ThreeDifferentFragment extends BaseFragment implements FastThreeSelectedListener, IBaseView, ILotteryOrderActivity {

    private static final String TAG = "ThreeDifferentOldFragment";

    private Context mContext;

    private static final int MSG_SHOW_OPTION_BUTTON = 0;

    @BindView(R.id.tv_tip_three_diff)
    TextView mTvTipThreeDiff;
    @BindView(R.id.tv_tip_three_to_all)
    TextView mTvTipThreeToAll;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.tipTv)
    TextView tipTv;
    @BindView(R.id.fast3_ll_all)
    LinearLayout mLlAll;
    @BindView(R.id.recyclerView_3different)
    RecyclerView mThreeDifferentRv;
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
    private ThreeDifferentAdapter mAdapter;
    private int mCount, mThreeToAllCount;
    private boolean isClicked = false;
    private List<LotteryBall> mSelectButtons;
    private String mLotteryType;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_OPTION_BUTTON:
                    if (msg.obj != null && msg.obj instanceof List) {
                        List<LotteryBall> btns = (List<LotteryBall>) msg.obj;
                        mAdapter = new ThreeDifferentAdapter(AppConstants.FAST_THREE_3_DIFFERENT,
                                btns, ThreeDifferentFragment.this);
                        mThreeDifferentRv.setLayoutManager(new GridLayoutManager(mContext, 6));
                        mThreeDifferentRv.setAdapter(mAdapter);
                    }
                    break;
            }
        }
    };

    public static ThreeDifferentFragment newInstance(String lotteryType) {
        ThreeDifferentFragment fragment = new ThreeDifferentFragment();
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
        View threeSameSingleView = inflater.inflate(R.layout.fragment_three_different, container, false);
        unbinder = ButterKnife.bind(this, threeSameSingleView);
        initBundle();
        initPresenter();
        initView();
        return threeSameSingleView;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            LogF.d(TAG, "和值参数为空");
            return;
        }
        mLotteryType = bundle.getString(ActivityConstants.PARAM_LOTTERY_TYPE);
    }

    private void initPresenter() {
        mLotteryOrderPresenter = new LotteryOrderPresenter(getActivity(), this);
        mPresenter = new FastThreeFragmentPresenter(getActivity(), this);
        mPresenter.requestThreeDifferentOptions();
    }

    private void initView() {
        mTvTipThreeDiff.setText(Html.fromHtml("选3个不同号码，与开奖项相同即中<font color=\"#dd3048\"> 40 </font>元"));
        mTvTipThreeToAll.setText(Html.fromHtml("123/234/345/456任一开出即中<font color=\"#dd3048\"> 10 </font>元"));
    }

    private void showView() {
        if (isClicked) {
            mThreeToAllCount = 1;
            mLlAll.setSelected(true);
            titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
            tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
        } else {
            mThreeToAllCount = 0;
            mLlAll.setSelected(false);
            titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
            tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_7F7F7F));
        }
        updateCount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fast3_ll_all, R.id.threeD_btn_machine, R.id.threeD_ib_delete, R.id.threeD_tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fast3_ll_all:
                isClicked = !isClicked;
                showView();
                break;
            case R.id.threeD_ib_delete:
                mAdapter.clearSelect();
                isClicked = false;
                showView();
                break;
            case R.id.threeD_btn_machine:
                LotteryOrder lotteryOrder = LotteryUtils.machine(mLotteryType, FAST_THREE_3_DIFFERENT);
                if (null == lotteryOrder) {
                    return;
                }
                LotteryOrderDbUtils.addLotteryOrder(lotteryOrder);
                LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_3_DIFFERENT);
                getActivity().finish();
                break;
            case R.id.threeD_tv_ok:
                mLotteryOrderPresenter.createFast3SingleLocalOrder(mLotteryType, FAST_THREE_3_DIFFERENT, mSelectButtons, mCount);
                break;
        }
    }

    private void updateCount() {
        if ((null != mSelectButtons && mSelectButtons.size() > 0) || isClicked) {
            mBtnMachine.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mBtnDelete.setVisibility(View.GONE);
        }
        mCount = 0;
        if (null != mSelectButtons) {
            mCount = CalculateCountUtils.threeDifferentCount(mSelectButtons.size());
        }
        if (isClicked) {
            mThreeToAllCount = 1;
        }
        int total = mThreeToAllCount + mCount;
        mTvNum.setText("共" + total + "注");
        mTvMoney.setText(StringUtils.getNumberNoZero(total * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        Message msg = new Message();
        msg.what = MSG_SHOW_OPTION_BUTTON;
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
        mSelectButtons = balls;
        updateCount();
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

    }

    private void createThreeToAllOrder() {
        if (isClicked) {
            LotteryOrder order = new LotteryOrder();
            order.setPlayMode(FAST_THREE_3_TO_ALL);

            List<LotteryBall> mSelectButtons = new ArrayList<>();
            LotteryBall bean1 = new LotteryBall();
            bean1.setNumber(123);
            mSelectButtons.add(bean1);

            LotteryBall bean2 = new LotteryBall();
            bean2.setNumber(234);
            mSelectButtons.add(bean2);

            LotteryBall bean3 = new LotteryBall();
            bean3.setNumber(345);
            mSelectButtons.add(bean3);

            LotteryBall bean4 = new LotteryBall();
            bean4.setNumber(456);
            mSelectButtons.add(bean4);

            String numberStr = LotteryUtils.changeFT2BallNumber(mSelectButtons);
            order.setRedBall(numberStr);
            order.setTotalCount((long) mThreeToAllCount);
            order.setTotalMoney(StringUtils.getNumberNoZero(mThreeToAllCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
            if (mThreeToAllCount == 1) {
                order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
            } else {
                order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
            }
            order.setLotteryType(mLotteryType);
            if (LotteryOrderDbUtils.addLotteryOrder(order)) {
                mIsThreeToAllChoosed = true;
            } else {
                mIsThreeToAllChoosed = false;
                mAllErrorTip = "请选择投注号码";
            }
        }
    }

    private boolean mIsSingleChoosed, mIsThreeToAllChoosed;
    private String mSingleErrorTip, mAllErrorTip;

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        if (isOk) {
            mIsSingleChoosed = true;
        } else {
            mIsSingleChoosed = false;
            mSingleErrorTip = msgStr;
        }
        createThreeToAllOrder();
        if (mIsSingleChoosed || mIsThreeToAllChoosed) {
            mAdapter.clearSelect();
            isClicked = false;
            showView();
            LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_3_SAME_ONE);
            getActivity().finish();
        } else {
            showTipMessageDialog(!TextUtils.isEmpty(mSingleErrorTip) ? mSingleErrorTip : mAllErrorTip);
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
