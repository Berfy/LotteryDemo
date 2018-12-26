package cn.zcgames.lottery.home.view.fragment.fastthree;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import java.lang.ref.WeakReference;
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
import cn.zcgames.lottery.home.view.iview.ITowSameSingleFragment;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;

import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_MORE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_ONE;

/**
 * 二同号 新
 *
 * @author Berfy
 * 2018.10.23
 */
public class TowSameFragment extends BaseFragment implements FastThreeSelectedListener, ITowSameSingleFragment, ILotteryOrderActivity {

    private static final String TAG = "TowSameMoreFragment";

    private static final int MSG_SHOW_SAME_MORE_BUTTON = 0;//二同号复选
    private static final int MSG_SHOW_SAME_SINGLE_SAME_BUTTON = 1;//二同号单选 同号
    private static final int MSG_SHOW_SAME_SINGLE_DIFF_BUTTON = 2;//二同号单选 不同号
    private static final int MSG_SELECT_RESULT_SAME = 3;
    private static final int MSG_SELECT_RESULT_DIFFERENT = 4;
    private static final int MSG_SELECT_RESULT_MORE = 5;
    private static final int MSG_SHOW_COUNT_VIEW = 6;//计算注数

    private static final int ADAPTER_TYPE_SAME = 0;
    private static final int ADAPTER_TYPE_DIFFERENT = 1;

    @BindView(R.id.tv_two_same_single)//单选提示
            TextView mTvTipSameSingle;
    @BindView(R.id.tv_two_same_more)//复选提示
            TextView mTvTipSameMore;
    @BindView(R.id.recyclerView_2Same)//同号
            RecyclerView mSameRv;
    @BindView(R.id.recyclerView_2Different)//不同号
            RecyclerView mDifferentRv;
    @BindView(R.id.recyclerView_towSameMore)//复选
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
    private MyHandler mHandler;
    private List<LotteryBall> mMoreButtons;
    private ThreeDifferentAdapter mMoreAdapter;
    private ThreeDifferentAdapter mSameAdapter, mDifferentAdapter;
    private List<LotteryBall> mSameSelect, mDifferentSelect;
    private long mSameCount = 0;
    private long mDifferentCount = 0;
    private long mMoreCount = 0;
    private long mSingleCount;
    private String mLotteryType;

    public static class MyHandler extends Handler {

        private WeakReference<TowSameFragment> reference;

        private MyHandler(TowSameFragment towSameFragment) {
            reference = new WeakReference<>(towSameFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_SAME_MORE_BUTTON:
                    if (msg.obj != null && msg.obj instanceof List) {
                        List<LotteryBall> btns = (List<LotteryBall>) msg.obj;
                        reference.get().mMoreAdapter = new ThreeDifferentAdapter(AppConstants.FAST_THREE_2_SAME_MORE, btns, reference.get());
                        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
                        reference.get().mThreeDifferentRv.setLayoutManager(new GridLayoutManager(reference.get().mContext, 3));
                        reference.get().mThreeDifferentRv.addItemDecoration(space);
                        reference.get().mThreeDifferentRv.setAdapter(reference.get().mMoreAdapter);
                    }
                    break;
                case MSG_SHOW_SAME_SINGLE_SAME_BUTTON:
                    if (msg.obj != null && msg.obj instanceof List) {
                        List<LotteryBall> btns = (List<LotteryBall>) msg.obj;
                        reference.get().mSameAdapter = new ThreeDifferentAdapter(AppConstants.FAST_THREE_2_SAME_ONE, btns, reference.get(), ADAPTER_TYPE_SAME);
                        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
                        reference.get().mSameRv.setLayoutManager(new GridLayoutManager(reference.get().mContext, 6));
                        reference.get().mSameRv.addItemDecoration(space);
                        reference.get().mSameRv.setAdapter(reference.get().mSameAdapter);
                    }
                    break;
                case MSG_SHOW_SAME_SINGLE_DIFF_BUTTON:
                    if (msg.obj != null && msg.obj instanceof List) {
                        List<LotteryBall> btns = (List<LotteryBall>) msg.obj;
                        reference.get().mDifferentAdapter = new ThreeDifferentAdapter(AppConstants.FAST_THREE_2_SAME_ONE, btns, reference.get(), ADAPTER_TYPE_DIFFERENT);
                        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
                        reference.get().mDifferentRv.setLayoutManager(new GridLayoutManager(reference.get().mContext, 6));
                        reference.get().mDifferentRv.addItemDecoration(space);
                        reference.get().mDifferentRv.setAdapter(reference.get().mDifferentAdapter);
                    }
                    break;
                case MSG_SELECT_RESULT_MORE:
                    if (msg.obj != null && msg.obj instanceof List) {
                        reference.get().mMoreButtons = (List<LotteryBall>) msg.obj;
                        reference.get().mMoreCount = reference.get().mMoreButtons.size();
                        LogF.d(TAG, "复选 " + reference.get().mMoreCount);
                        sendEmptyMessage(MSG_SHOW_COUNT_VIEW);
                    }
                    break;
                case MSG_SELECT_RESULT_SAME:
                    if (msg.obj != null && msg.obj instanceof List) {
                        reference.get().mSameSelect = (List<LotteryBall>) msg.obj;
                        reference.get().mSameCount = reference.get().mSameSelect.size();
                        LogF.d(TAG, "同号 " + reference.get().mSameCount);
                        sendEmptyMessage(MSG_SHOW_COUNT_VIEW);
                        reference.get().mDifferentAdapter.setIgnoreData(reference.get().mSameSelect);
                    }
                    break;
                case MSG_SELECT_RESULT_DIFFERENT:
                    if (msg.obj != null && msg.obj instanceof List) {
                        reference.get().mDifferentSelect = (List<LotteryBall>) msg.obj;
                        reference.get().mDifferentCount = reference.get().mDifferentSelect.size();
                        LogF.d(TAG, "不同号 " + reference.get().mDifferentCount);
                        sendEmptyMessage(MSG_SHOW_COUNT_VIEW);
                        reference.get().mSameAdapter.setIgnoreData(reference.get().mDifferentSelect);
                    }
                    break;
                case MSG_SHOW_COUNT_VIEW:
                    reference.get().mSingleCount = reference.get().mSameCount * reference.get().mDifferentCount;
                    long totalCount = reference.get().mSingleCount + reference.get().mMoreCount;
                    reference.get().mTvNum.setText("共" + totalCount + "注");
                    reference.get().mTvMoney.setText(StringUtils.getNumberNoZero(totalCount * (float) SharedPreferenceUtil.get(reference.get().getActivity(), reference.get().mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
                    break;
            }
        }
    }

    public static TowSameFragment newInstance(String lotteryType) {
        TowSameFragment fragment = new TowSameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mHandler = new MyHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View threeSameSingleView = inflater.inflate(R.layout.fragment_tow_same, container, false);
        unbinder = ButterKnife.bind(this, threeSameSingleView);
        initBundle();
        initPresenter();
        initView();
        return threeSameSingleView;
    }

    private void initView() {
        mTvTipSameSingle.setText(Html.fromHtml("选择同号和不同号的组合，奖金<font color=\"#dd3048\"> 80 </font>元"));
        mTvTipSameMore.setText(Html.fromHtml("猜开奖中两个指定的相同号码，奖金<font color=\"#dd3048\"> 15 </font>元"));
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
        mPresenter = new FastThreeFragmentPresenter(mContext, this);
        mPresenter.requestTowSameMoreOptions();
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
                mMoreAdapter.clearSelect();
                mSameAdapter.clearSelect();
                mDifferentAdapter.clearSelect();
                break;
            case R.id.threeD_btn_machine:
                LotteryOrder lotteryOrder = LotteryUtils.machine(mLotteryType, FAST_THREE_2_SAME_MORE);
                if (null == lotteryOrder) {
                    return;
                }
                LotteryOrderDbUtils.addLotteryOrder(lotteryOrder);
                LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_2_SAME_MORE);
                getActivity().finish();
                break;
            case R.id.threeD_tv_ok:
                //单选复选依次生成订单
                mLotteryOrderPresenter.createFast3LocalOrder(mLotteryType, FAST_THREE_2_SAME_ONE,
                        mSameSelect, mDifferentSelect, mSingleCount);
                mLotteryOrderPresenter.createFast3SingleLocalOrder(mLotteryType, FAST_THREE_2_SAME_MORE, mMoreButtons, mMoreCount);
                break;
        }
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        Message msg = new Message();
        msg.what = MSG_SHOW_SAME_MORE_BUTTON;
        msg.obj = object;
        if (null != mHandler)
            mHandler.sendMessage(msg);
    }

    @Override
    public void requestTowSameSingleSame(Object obj) {
        Message msg = new Message();
        msg.what = MSG_SHOW_SAME_SINGLE_SAME_BUTTON;
        msg.obj = obj;
        if (null != mHandler)
            mHandler.sendMessage(msg);
    }

    @Override
    public void requestTowSameSingleDifferent(Object obj) {
        Message msg = new Message();
        msg.what = MSG_SHOW_SAME_SINGLE_DIFF_BUTTON;
        msg.obj = obj;
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
        if (balls.size() > 0) {
            mBtnMachine.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mBtnDelete.setVisibility(View.GONE);
        }
        Message msg = new Message();
        msg.obj = balls;
        if (playType == AppConstants.FAST_THREE_2_SAME_ONE) {
            if (type == ADAPTER_TYPE_SAME) {
                msg.what = MSG_SELECT_RESULT_SAME;
            } else {
                msg.what = MSG_SELECT_RESULT_DIFFERENT;
            }
        } else {
            msg.what = MSG_SELECT_RESULT_MORE;
        }
        if (null != mHandler)
            mHandler.sendMessage(msg);
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

    }

    private boolean mIsMoreChoossed, mIsSingleChoossed;
    private String mMoreErrorTip, mSingleErrorTip;

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        //单选复选依次生成订单
        if (playType == AppConstants.FAST_THREE_2_SAME_ONE) {
            if (isOk) {
                mIsSingleChoossed = true;
            } else {
                mIsSingleChoossed = false;
                mSingleErrorTip = msgStr;
            }
        } else {//复选生成后判断
            if (isOk) {
                mIsMoreChoossed = true;
            } else {
                mIsMoreChoossed = false;
                mMoreErrorTip = msgStr;
            }
            if (mIsMoreChoossed || mIsSingleChoossed) {
                mMoreAdapter.clearSelect();
                mSameAdapter.clearSelect();
                mDifferentAdapter.clearSelect();
                LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_2_SAME_MORE);
                getActivity().finish();
            } else {
                showTipMessageDialog(!TextUtils.isEmpty(mSingleErrorTip) ? mSingleErrorTip : mMoreErrorTip);
            }
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
