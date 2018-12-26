package cn.zcgames.lottery.message.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseFragment;
import cn.zcgames.lottery.bean.response.ResultBeanData;
import cn.zcgames.lottery.message.presenter.ResultFragmentPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.activity.MessageSettingActivity;
import cn.zcgames.lottery.home.view.adapter.LotteryResultAdapter;
import cn.zcgames.lottery.view.common.refreshview.XRefreshView;
import cn.zcgames.lottery.base.IBaseView;

/**
 * Created by admin on 2017/3/31.
 * 首页-开奖
 */
public class ResultFragment extends BaseFragment implements IBaseView, View.OnClickListener {

    private static final String TAG = "ResultFragment";

    private static final int MSG_UPDATE_ADAPTER = 0;
    private static final int MSG_NO_DATA = 1;
    private static final int MSG_SHOW_WAITING_DIALOG = 2;
    private static final int MSG_HIDDEN_WAITING_DIALOG = 3;

    private TextView mTvRight;
    private TextView mTitleTv;
    private View mNoDataView;

    private RecyclerView mLotteryListRv;
    private LotteryResultAdapter mAdapter;

    private ResultFragmentPresenter mPresenter;

    private XRefreshView mXRefreshView;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_ADAPTER:
                    if (msg.obj != null && msg.obj instanceof ResultBeanData) {
                        ResultBeanData bean = (ResultBeanData) msg.obj;
                        if (bean.getPayload() == null) {
                            mHandler.sendEmptyMessage(MSG_NO_DATA);
                            return;
                        }
                        mAdapter.setResultList(bean.getPayload());
                        UIHelper.showWidget(mNoDataView, false);
                        UIHelper.showWidget(mLotteryListRv, true);
                    } else {
                        mHandler.sendEmptyMessage(MSG_NO_DATA);
                    }
                    break;
                case MSG_NO_DATA:
                    UIHelper.showWidget(mNoDataView, true);
                    UIHelper.showWidget(mLotteryListRv, false);
                    break;
                case MSG_SHOW_WAITING_DIALOG:
                    UIHelper.showWaitingDialog(getActivity(), R.string.tips_loading, false);
                    break;
                case MSG_HIDDEN_WAITING_DIALOG:
                    UIHelper.hideWaitingDialog();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initAdapter();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new ResultFragmentPresenter(getActivity(), this);
//        mPresenter.requestWinningInfo();
    }

    private void initAdapter() {
        mAdapter = new LotteryResultAdapter(getActivity());
        mLotteryListRv.setAdapter(mAdapter);
    }

    private void initView() {
        mTvRight = getView().findViewById(R.id.title_right_tv);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(R.string.result_notify_title);
        mTvRight.setOnClickListener(this);
        mTitleTv = (TextView) getView().findViewById(R.id.title_tv);
        mTitleTv.setText(R.string.result_title);

        mLotteryListRv = (RecyclerView) getView().findViewById(R.id.result_rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mLotteryListRv.setLayoutManager(manager);

        mNoDataView = getView().findViewById(R.id.noData_ll);
        mNoDataView.setOnClickListener(this);

        mXRefreshView = (XRefreshView) getView().findViewById(R.id.result_XRefreshView);
        mXRefreshView.setPullRefreshEnable(true);
        mXRefreshView.setPinnedTime(500);
        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestWinningInfo();
            }
        });
        mXRefreshView.startRefresh();
    }

    @Override
    public void requestResult(boolean isOk, Object result) {
        mXRefreshView.stopRefresh();
        LogF.d(TAG, "开奖结果返回值" + isOk + "  " + GsonUtil.getInstance().toJson(result));
        if (isOk) {
            Message msg = new Message();
            msg.what = MSG_UPDATE_ADAPTER;
            msg.obj = result;
            mHandler.sendMessage(msg);
        } else {
            mHandler.sendEmptyMessage(MSG_NO_DATA);
            UIHelper.showToast((String) result);
        }
    }

    @Override
    public void showTipDialog(String msgStr) {
        Message msg = new Message();
        msg.what = MSG_SHOW_WAITING_DIALOG;
        msg.obj = msgStr;
        mHandler.sendMessage(msg);
    }

    @Override
    public void hideTipDialog() {
        mHandler.sendEmptyMessage(MSG_HIDDEN_WAITING_DIALOG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noData_ll:
                mPresenter.requestWinningInfo();
                break;
            case R.id.title_right_tv:
                if (!MyApplication.isLogin()) {
                    UIHelper.showConfirmDialog(getActivity(),
                            R.string.tips_no_login,
                            isOk -> {
                                if (isOk) UIHelper.gotoActivity(getActivity(), LoginActivity.class, false);
                            });
                } else {
                    UIHelper.gotoActivity(getActivity(), MessageSettingActivity.class, false);
                }
                break;
        }
    }
}
