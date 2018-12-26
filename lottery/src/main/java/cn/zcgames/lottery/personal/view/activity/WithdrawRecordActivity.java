package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.berfy.sdk.mvpbase.util.AnimUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.WithdrawRecord;
import cn.zcgames.lottery.personal.model.WithdrawRecordInfo;
import cn.zcgames.lottery.personal.presenter.WithdrawPresenter;
import cn.zcgames.lottery.personal.view.adapter.WithdrawRecordAdapter;
import cn.zcgames.lottery.personal.view.iview.IWithdrawView;
import cn.zcgames.lottery.view.common.refreshview.XRefreshView;

/**
 * 提现记录界面
 *
 * @author NorthStar
 * @date 2018/9/11 13:49
 */
public class WithdrawRecordActivity extends BaseActivity implements IWithdrawView {

    @BindView(R.id.xrefreshview)
    XRefreshView layoutSwipeRefresh;

    @BindView(R.id.withdraw_rv)
    RecyclerView recordRV;

    @BindView(R.id.title_back)
    ImageButton backIv;

    @BindView(R.id.noData_view)
    LinearLayout mCurrEmptyView;

    private WithdrawRecordAdapter mAdapter;
    private long ts = 0;
    private int mStartPageIdx = 1;
    private boolean mIsLoadMore = false;//是否加载更多
    public static final String TAG = "WithdrawRecordActivity";
    private WithdrawPresenter mPresenter;

    private static final int MSG_SHOW_DATA = 0;
    private static final int MSG_NO_DATA = 1;
    private static final int MSG_SHOW_DIALOG = 2;
    private static final int MSG_HIDE_DIALOG = 3;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_DATA:
                    if (msg.obj != null && msg.obj instanceof WithdrawRecordInfo.withdrawBean) {
                        UIHelper.showWidget(mCurrEmptyView, false);
                        UIHelper.showWidget(layoutSwipeRefresh, true);
                        WithdrawRecordInfo.withdrawBean recordInfo = (WithdrawRecordInfo.withdrawBean) msg.obj;
                        ts = -1;
                        List<WithdrawRecord> withdrawData = recordInfo.getWithdrawData();
                        mAdapter.setHistoryList(withdrawData, mIsLoadMore);
                    }
                    mHandler.sendEmptyMessage(MSG_NO_DATA);
                    break;
                case MSG_HIDE_DIALOG:
                    hideWaitingDialog();
                    break;
                case MSG_NO_DATA:
                    if (mAdapter.getItemCount() <= 0) {
                        UIHelper.showWidget(mCurrEmptyView, true);
                        UIHelper.showWidget(layoutSwipeRefresh, false);
                    }
                    break;
                case MSG_SHOW_DIALOG:
                    showWaitingDialog(WithdrawRecordActivity.this, R.string.tips_loading, false);
                    break;
            }
        }
    };

    /**
     * 跳转回调
     *
     * @param context 上下文
     */
    public static void launcher(Activity context) {
        Intent intent = new Intent(context, WithdrawRecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        AnimUtil.jump2NextPage(context, intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        setTitleBal();
        setRV();
        mPresenter.requestWithdrawRecord(mStartPageIdx, ts);
    }

    private void setRV() {
        LinearLayoutManager linearManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recordRV.setLayoutManager(linearManager);
        //设置条目的默认动画
        recordRV.setItemAnimator(new DefaultItemAnimator());
        recordRV.setHasFixedSize(true);
        mAdapter = new WithdrawRecordAdapter(this);
        recordRV.setAdapter(mAdapter);
        layoutSwipeRefresh.setPullRefreshEnable(true);
        layoutSwipeRefresh.setSilenceLoadMore();
        //设置刷新完成以后，headerView(刷新LOGO)固定的时间
        layoutSwipeRefresh.setPinnedTime(1000);
        layoutSwipeRefresh.setMoveForHorizontal(true);
        layoutSwipeRefresh.startRefresh();
        setListener();
    }

    private void setListener() {
        layoutSwipeRefresh.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSilence) {
                mStartPageIdx++;
                mIsLoadMore = true;
                mPresenter.requestWithdrawRecord(mStartPageIdx, ts);
            }

            @Override
            public void onRefresh() {
                mStartPageIdx = 1;
                mIsLoadMore = false;
                mPresenter.requestWithdrawRecord(mStartPageIdx, ts);
            }
        });
        backIv.setOnClickListener(v -> goBack(WithdrawRecordActivity.this));
    }

    private void setTitleBal() {
        UIHelper.hideSystemTitleBar(this);
        setContentView(R.layout.activity_withdraw_record);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.withdraw_record);
        UIHelper.showWidget(backIv, true);
        mPresenter = new WithdrawPresenter(this, this);
    }


    @Override
    public void requestResult(boolean isOk, Object object) {
        layoutSwipeRefresh.stopLoadMore();
        layoutSwipeRefresh.stopRefresh();
        if (isOk) {
            Message msg = new Message();
            msg.what = MSG_SHOW_DATA;
            msg.obj = object;
            mHandler.sendMessage(msg);
        } else {
            mHandler.sendEmptyMessage(MSG_NO_DATA);
        }
    }

    @Override
    public void showTipDialog(String msg) {
//        showWaitingDialog(WithdrawRecordActivity.this, msg, false);
    }

    @Override
    public void hideTipDialog() {
//        hideWaitingDialog();
    }

    @Override
    public void getWithdrawWay(boolean isOk, Object object) {
    }
}
