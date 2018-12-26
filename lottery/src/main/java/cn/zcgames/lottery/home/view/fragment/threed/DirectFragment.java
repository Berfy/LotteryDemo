package cn.zcgames.lottery.home.view.fragment.threed;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import java.util.List;

import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.listener.ChooseNumSelectedListener;
import cn.zcgames.lottery.home.presenter.ThreeDPresenter;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.view.adapter.ThreeDBallAdapter;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.home.view.iview.IThreeDActivity;

import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_3_D;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_DIRECT;

/**
 * Berfy修改 2018.8.28
 * 福彩3D-直选
 */
public class DirectFragment extends BaseFragment implements View.OnClickListener, ChooseNumSelectedListener, IThreeDActivity {

    private static final String TAG = "DirectFragment";

    private TextView mTotalMoneyTv, mTotalCountTv;
    private RecyclerView mHundredRv, mTenRv, mOneRv;
    private ThreeDBallAdapter mTenAdapter, mOneAdapter, mHundredAdapter;

    //每行球的个数
    private int mBallNumberPerRow = 5;

    //adapter的类型
    private int mOneType = 0;
    private int mTenType = 1;
    private int mHundredType = 2;

    private int mTotalCount;

    private List<LotteryBall> mHundredBalls, mTenBalls, mOneBalls;

    private ThreeDPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direct, container, false);
        initView(view);
        initAdapter();
        initPresenter();
        return view;
    }

    private void initPresenter() {
        mPresenter = new ThreeDPresenter(getActivity(), this);
    }

    private void initAdapter() {
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);

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

    private void initView(View view) {
        view.findViewById(R.id.threeD_ib_delete).setOnClickListener(this);
        view.findViewById(R.id.threeD_tv_ok).setOnClickListener(this);
        mTotalMoneyTv = (TextView) view.findViewById(R.id.threeD_tv_money);
        mTotalCountTv = (TextView) view.findViewById(R.id.threeD_tv_num);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), mBallNumberPerRow);
        mHundredRv = (RecyclerView) view.findViewById(R.id.recyclerView_hundred);
        mHundredRv.setLayoutManager(glm);

        GridLayoutManager glm1 = new GridLayoutManager(getActivity(), mBallNumberPerRow);
        mTenRv = (RecyclerView) view.findViewById(R.id.recyclerView_ten);
        mTenRv.setLayoutManager(glm1);

        GridLayoutManager glm2 = new GridLayoutManager(getActivity(), mBallNumberPerRow);
        mOneRv = (RecyclerView) view.findViewById(R.id.recyclerView_one);
        mOneRv.setLayoutManager(glm2);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.threeD_ib_delete:
                clearAdapter();
                break;
            case R.id.threeD_tv_ok:
                createOrder();
                break;
        }
    }

    private void createOrder() {
        mPresenter.createDirectOrder(THREE_D_PLAY_DIRECT, mHundredBalls, mTenBalls, mOneBalls, mTotalCount);
    }

    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        if (type == mHundredType) {
            mHundredBalls = balls;
        } else if (type == mTenType) {
            mTenBalls = balls;
        } else {
            mOneBalls = balls;
        }
        mPresenter.getDirectTotalCount(mHundredBalls, mTenBalls, mOneBalls);
    }

    @Override
    public void onTotalCountResult(int count) {

        mTotalCount = count;
        mTotalCountTv.setText(mTotalCount + "注");
        mTotalMoneyTv.setText(StringUtils.getNumberNoZero(count * (float) SharedPreferenceUtil.get(getActivity(), AppConstants.LOTTERY_TYPE_3_D + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }

    @Override
    public void onCreateDirectOrder(boolean isOk, String msgStr) {
        if (isOk) {
            clearAdapter();
            LotteryOrderActivity.intoThisActivity(getActivity(), LOTTERY_TYPE_3_D, THREE_D_PLAY_DIRECT);
            getActivity().finish();
        } else {
            showTipDialog(msgStr);
        }
    }

    private void showTipDialog(String msgString) {
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
    public void onRequestSequence(boolean isOk, Object msg) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            clearAdapter();
        }
    }

    private void clearAdapter() {
        mTenAdapter.clearSelectedBall();
        mOneAdapter.clearSelectedBall();
        mHundredAdapter.clearSelectedBall();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
