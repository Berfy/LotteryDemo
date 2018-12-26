package cn.zcgames.lottery.home.lottery.alwaycolor.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
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
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.presenter.LotteryOrderPresenter;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;

/**
 * @author Berfy
 * 时时彩-大小单双
 */
public class NotNumberFragment extends BaseFragment implements ILotteryOrderActivity {

    private static final String TAG = "NotNumberFragment";
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
    @BindView(R.id.label0)
    TextView label10;
    Unbinder unbinder;

    private TextView[] mTenButtons;
    private TextView[] mOneButtons;
    private String mTenNumber;
    private String mOneNumber;
    private String mLotteryType;

    private LotteryOrderPresenter mPresenter;

    public static NotNumberFragment newInstance(String lotteryType) {
        NotNumberFragment fragment = new NotNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.PARAM_LOTTERY_TYPE, lotteryType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_number, container, false);
        unbinder = ButterKnife.bind(this, view);
        initBundle();
        initView();
        mPresenter = new LotteryOrderPresenter(getActivity(), this);
        return view;
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

    private void initView() {
        mTenButtons = new TextView[]{tenBig, tenSmall, tenSingle, tenDouble};
        mOneButtons = new TextView[]{oneBig, oneSmall, oneSingle, oneDouble};
        label10.setText(Html.fromHtml("每位至少选1个号，猜对开奖后2位的属性即中<font color=\"#dd3048\"> 4 </font>元"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ten_big, R.id.ten_small, R.id.ten_single, R.id.ten_double,
            R.id.one_big, R.id.one_small, R.id.one_single, R.id.one_double,
            R.id.threeD_btn_machine, R.id.threeD_ib_delete, R.id.threeD_tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ten_big:
                mTenNumber = "大";
                updateBtnStatus(0, 0);
                break;
            case R.id.ten_small:
                mTenNumber = "小";
                updateBtnStatus(0, 1);
                break;
            case R.id.ten_single:
                mTenNumber = "单";
                updateBtnStatus(0, 2);
                break;
            case R.id.ten_double:
                mTenNumber = "双";
                updateBtnStatus(0, 3);
                break;
            case R.id.one_big:
                mOneNumber = "大";
                updateBtnStatus(1, 0);
                break;
            case R.id.one_small:
                mOneNumber = "小";
                updateBtnStatus(1, 1);
                break;
            case R.id.one_single:
                mOneNumber = "单";
                updateBtnStatus(1, 2);
                break;
            case R.id.one_double:
                mOneNumber = "双";
                updateBtnStatus(1, 3);
                break;
            case R.id.threeD_btn_machine:
                LotteryOrder lotteryOrder = LotteryUtils.machine(mLotteryType, ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
                if (null == lotteryOrder) {
                    return;
                }
                LotteryOrderDbUtils.addLotteryOrder(lotteryOrder);
                LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
                getActivity().finish();
                break;
            case R.id.threeD_ib_delete:
                updateBtnStatus(1, 4);
                updateBtnStatus(0, 4);
                mTenSelected = false;
                mOneSelected = false;
                updateCount();
                break;
            case R.id.threeD_tv_ok:
                if (!TextUtils.isEmpty(mTenNumber) && !TextUtils.isEmpty(mOneNumber)) {
                    List<LotteryBall> ten = new ArrayList<>();
                    LotteryBall tenBall = new LotteryBall();
                    tenBall.setNumber(mTenNumber);
                    ten.add(tenBall);

                    List<LotteryBall> one = new ArrayList<>();
                    LotteryBall oneBall = new LotteryBall();
                    oneBall.setNumber(mOneNumber);
                    one.add(oneBall);
                    mPresenter.createAlwayColorLocalOrder(mLotteryType,
                            AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE, null, null, null, ten, one, 1);
                } else {
                    showTipMessageDialog("请选择投注");
                }

                break;
        }
    }

    private boolean mTenSelected, mOneSelected;

    private void updateBtnStatus(int type, int position) {
        if (type == 0) {
            for (int i = 0; i < mTenButtons.length; i++) {
                if (i == position) {
                    mTenSelected = true;
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
            for (int i = 0; i < mOneButtons.length; i++) {
                if (i == position) {
                    mOneSelected = true;
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
        updateCount();
    }

    private void updateCount() {
        if (mOneSelected || mTenSelected) {
            mBtnMachine.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.VISIBLE);
        } else {
            mBtnMachine.setVisibility(View.VISIBLE);
            mBtnDelete.setVisibility(View.GONE);
        }
        if (mOneSelected && mTenSelected) {
            alwaysTvNum.setText("共1注");
            alwaysTvMoney.setText(StringUtils.getNumberNoZero((float) SharedPreferenceUtil.get(getActivity(), mLotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
        } else {
            alwaysTvNum.setText("共0注");
            alwaysTvMoney.setText("0元");
        }
    }

    @Override
    public void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr) {

    }

    @Override
    public void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr) {
        if (isOk) {
            LotteryOrderActivity.intoThisActivity(getActivity(), mLotteryType, ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
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
