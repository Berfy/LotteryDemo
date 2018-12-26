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
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_GROUP_THREE;

/**
 * Berfy修改 2018.10.12
 * 3D 组三
 * */
public class GroupThreeFragment extends BaseFragment implements View.OnClickListener, ChooseNumSelectedListener, IThreeDActivity {

    private static final String TAG = "GroupThreeFragment";

    private TextView mTotalMoneyTv, mTotalCountTv;
    private RecyclerView mRecyclerView;
    private ThreeDBallAdapter mAdapter;

    private List<LotteryBall> mSelectedBalls;
    private int mTotalCount;

    private ThreeDPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_three, container, false);
        initView(view);
        initAdapter();
        initPresenter();
        return view;
    }

    private void initPresenter() {
        mPresenter = new ThreeDPresenter(getActivity(), this);
    }

    private void initAdapter() {
        mAdapter = new ThreeDBallAdapter(getActivity(), 0, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView(View view) {
        view.findViewById(R.id.threeD_ib_delete).setOnClickListener(this);
        view.findViewById(R.id.threeD_tv_ok).setOnClickListener(this);

        mTotalMoneyTv = (TextView) view.findViewById(R.id.threeD_tv_money);
        mTotalCountTv = (TextView) view.findViewById(R.id.threeD_tv_num);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 5);
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_hundred);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.addItemDecoration(space);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.threeD_ib_delete:
                mAdapter.clearSelectedBall();
                break;
            case R.id.threeD_tv_ok:
                createOrder();
                break;
        }
    }

    private void createOrder() {
        mPresenter.createGroupOrder(THREE_D_PLAY_GROUP_THREE, mSelectedBalls, mTotalCount);
    }


    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        mSelectedBalls = balls;
        mPresenter.getGroup3Count(mSelectedBalls);
    }

    @Override
    public void onTotalCountResult(int count) {
        mTotalCount = count;
        mTotalCountTv.setText(count + "注");
        mTotalMoneyTv.setText(StringUtils.getNumberNoZero(count *  (float) SharedPreferenceUtil.get(getActivity(), AppConstants.LOTTERY_TYPE_3_D + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }

    @Override
    public void onCreateDirectOrder(boolean isOk, String msgStr) {
        if (!isOk) {
            showTipDialog(msgStr);
        } else {
            mAdapter.clearSelectedBall();
            LotteryOrderActivity.intoThisActivity(getActivity(), LOTTERY_TYPE_3_D, THREE_D_PLAY_GROUP_THREE);
            getActivity().finish();
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
            mAdapter.clearSelectedBall();
        }
    }
}
