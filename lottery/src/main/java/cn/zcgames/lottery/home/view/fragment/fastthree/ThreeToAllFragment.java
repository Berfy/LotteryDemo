package cn.zcgames.lottery.home.view.fragment.fastthree;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_DANSHI;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_FUSHI;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_TO_ALL;

/**
 * Created by admin on 2017/6/20.
 */

public class ThreeToAllFragment extends BaseFragment {

    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.tipTv)
    TextView tipTv;
    @BindView(R.id.fast3_ll_all)
    LinearLayout mLlAll;
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

    private boolean isClicked = false;
    private int mCount = 0;
    private String mLotteryType;

    public static ThreeToAllFragment newInstance(String lotteryType) {
        ThreeToAllFragment fragment = new ThreeToAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View threeSameSingleView = inflater.inflate(R.layout.fragment_three_to_all, container, false);
        unbinder = ButterKnife.bind(this, threeSameSingleView);
        mBtnMachine.setVisibility(View.GONE);
        initBundle();
        return threeSameSingleView;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        mLotteryType = bundle.getString(ActivityConstants.PARAM_LOTTERY_TYPE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.threeD_ib_delete, R.id.threeD_tv_ok, R.id.fast3_ll_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.threeD_ib_delete:
                isClicked = !isClicked;
                showView();
                break;
            case R.id.threeD_tv_ok:
                createLocalOrder();
                break;
            case R.id.fast3_ll_all:
                isClicked = !isClicked;
                showView();
                break;
        }
    }

    private void createLocalOrder() {
        if (mCount <= 0) {
            showTipMessageDialog("请选择投注号码");
            return;
        }

        boolean isOk;
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
        order.setTotalCount((long) mCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        if (mCount == 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        }
        order.setLotteryType(mLotteryType);
        isOk = LotteryOrderDbUtils.addLotteryOrder(order);
        if (isOk) {
            LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, FAST_THREE_3_TO_ALL);
            getActivity().finish();
        }

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

    private void showView() {
        if (isClicked) {
            mCount = 1;
            mLlAll.setSelected(true);
            titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
            tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
        } else {
            mCount = 0;
            mLlAll.setSelected(false);
            titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
            tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_7F7F7F));
        }
        mTvNum.setText("共" + mCount + "注");
        mTvMoney.setText(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }
}
