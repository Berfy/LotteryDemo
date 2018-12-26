package cn.zcgames.lottery.message.view.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.berfy.sdk.mvpbase.model.MessageBean;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.berfy.sdk.mvpbase.model.MessageInfo;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.presenter.MessagePresenter;
import cn.zcgames.lottery.home.view.adapter.MessageAdapter;
import cn.zcgames.lottery.personal.view.activity.LotteryOrderDetailActivity;
import cn.zcgames.lottery.personal.view.activity.PhaseDetailActivity;
import cn.zcgames.lottery.view.common.refreshview.XRefreshView;

/**
 * 站内信息
 *
 * @author NorthStar
 * @date 2018/8/21 17:42
 */
public class MessageCenterActivity extends BaseActivity implements IBaseView {

    @BindView(R.id.message_rv_list)
    RecyclerView mMessageListRv;

    @BindView(R.id.message_xrv)
    XRefreshView mXRecyclerView;

    @BindView(R.id.title_back)
    View backView;

    @BindView(R.id.noData_ll)
    LinearLayout mNoDataView;


    private MessageAdapter mAdapter;
    private boolean mIsLoadMore = false;//是否加载更多
    private int mStartPageIdx = 1;//开始页码为0
    private long ts = 0;
    public static final String TAG = "MessageCenterActivity";
    private MessagePresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        mPresenter = new MessagePresenter(this, this);
    }

    private void initView() {
        setContentView(R.layout.activity_message_center);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_message);
        UIHelper.showWidget(backView, true);
        initRV();
    }

    private void initRV() {
        mMessageListRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(this, (view, position) -> {
            List<MessageBean> messageList = mAdapter.getMessageList();
            MessageBean messageBean = messageList.get(position);
            String orderId = messageBean.getOrderId();
            if (!TextUtils.isEmpty(orderId)) {
                int chase = Integer.valueOf(messageBean.getChase());
                LogF.d(MyApplication.NOTIFY_TAG, "chase==>" + chase + " ,orderId==>" + orderId);
                if (chase > 1) {
                    PhaseDetailActivity.intoThisActivity(MessageCenterActivity.this,
                            orderId, messageBean.getLotteryName());//追期管理
                } else {
                    LotteryOrderDetailActivity.inToThisActivity(MessageCenterActivity.this,
                            orderId, messageBean.getLotteryName());//订单详情
                }
            } else {
                MessageDetailActivity.inToThisActivity(MessageCenterActivity.this, messageBean);
            }

        });
        mMessageListRv.setAdapter(mAdapter);
        mMessageListRv.setHasFixedSize(true);
        mXRecyclerView.setPullRefreshEnable(true);
        mXRecyclerView.setSilenceLoadMore();
        //设置刷新完成以后，headerView固定的时间
        mXRecyclerView.setPinnedTime(1000);
        mXRecyclerView.setMoveForHorizontal(true);

        int itemCount = mAdapter.getItemCount();
        if (itemCount <= 0) {
            mStartPageIdx = 1;
            mPresenter.requestMessage(mStartPageIdx, ts);
        } else {
            int mPageMaxNum = 20;
            int mStartPage = itemCount / mPageMaxNum;
            mStartPageIdx = mStartPage == 0 ? 1 : mStartPage;
        }

        LogF.d(TAG, "itemCount==>" + itemCount + ", mStartPageIdx==>" + mStartPageIdx);
        setListener();
    }

    private void setListener() {
        mXRecyclerView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSilence) {
                LogF.d(TAG, "mStartPageIdx==>" + mStartPageIdx);
                mIsLoadMore = true;
                mStartPageIdx++;
                mPresenter.requestMessage(mStartPageIdx, ts);
            }

            @Override
            public void onRefresh() {
                mStartPageIdx = 1;
                mIsLoadMore = false;
                mPresenter.requestMessage(mStartPageIdx, ts);
            }
        });

        backView.setOnClickListener(v -> goBack(MessageCenterActivity.this));
    }


    //获取网络数据更新UI
    private void updateUI(Object obj) {
        if (obj != null && obj instanceof MessageInfo) {
            UIHelper.showWidget(mNoDataView, false);
            UIHelper.showWidget(mXRecyclerView, true);
            MessageInfo Info = (MessageInfo) obj;
            List<MessageBean> msgList = Info.getList();
            if (msgList == null || msgList.size() == 0) {
                setEmpty();
                return;
            }
            ts = -1;
            mAdapter.setMessageList(msgList, mIsLoadMore);
        } else {
            setEmpty();
        }
        mXRecyclerView.stopRefresh();
        mXRecyclerView.stopLoadMore();
    }

    //暂无数据
    private void setEmpty() {
        mStartPageIdx--;
        if (mAdapter.getItemCount() <= 0) {
            UIHelper.showWidget(mNoDataView, true);
            UIHelper.showWidget(mXRecyclerView, false);
        }
        if (mXRecyclerView != null) {
            mXRecyclerView.stopRefresh();
            mXRecyclerView.stopLoadMore();
        }
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        if (mContext == null || mContext.isFinishing()) return;
        if (isOk) {
            updateUI(object);
        } else {
            setEmpty();
        }
    }

    @Override
    public void showTipDialog(String msgStr) {
    }

    @Override
    public void hideTipDialog() {
    }
}
