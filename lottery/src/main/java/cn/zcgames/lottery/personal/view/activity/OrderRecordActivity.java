package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.BuyHistoryBean;
import cn.zcgames.lottery.personal.presenter.OrderRecordPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.personal.view.adapter.BuyHistoryViewPagerAdapter;
import cn.zcgames.lottery.personal.view.adapter.OrderRecordAdapter;
import cn.zcgames.lottery.view.common.refreshview.XRefreshView;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 投注记录列表
 *
 * @author NorthStar
 * @date 2018/8/20 16:52
 */
public class OrderRecordActivity extends BaseActivity implements IBaseView {

    private static final String TAG = "OrderRecordActivity";

    private static final int MSG_SHOW_DATA = 0;
    private static final int MSG_NO_DATA = 1;
    private static final int MSG_SHOW_DIALOG = 2;
    private static final int MSG_HIDE_DIALOG = 3;

    @BindView(R.id.buyHistory_vp_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.buyHistory_tv_all)
    TextView allTV;
    @BindView(R.id.buyHistory_tv_no)
    TextView notTV;
    @BindView(R.id.buyHistory_tv_done)
    TextView alreadyTV;

    private List<String> mTitles = new ArrayList<>();
    private List<View> mViewList = new ArrayList<>();
    private List<View> mEmptyViews = new ArrayList<>();
    private List<TextView> mTitleTvs = new ArrayList<>();
    private List<RecyclerView> mRecyclerViews = new ArrayList<>();
    private List<OrderRecordAdapter> mAdapters = new ArrayList<>();
    private List<XRefreshView> mXRefreshViews = new ArrayList<>();
    private XRefreshView mCurrXRefreshView;
    private View mCurrEmptyView;
    private OrderRecordAdapter mCurrentAdapter;
    private OrderRecordPresenter mPresenter;

    private int mCurrentPosition = 0;//当前viewpager的position
    private String mCurrLotteryStatus;//当前查询的开奖状态：全部、已开奖、未开奖
    private int mTotalViewCount = 3;//viewpager包含的view的个数
    private boolean mIsLoadMore = false;//是否加载更多
    private int mStartPageIdx = 0;//开始页码为0
    private int mPageMaxNum = 20;//一页最多20条数据

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_DATA:
                    if (msg.obj != null && msg.obj instanceof List) {
                        UIHelper.showWidget(mCurrEmptyView, false);
                        UIHelper.showWidget(mCurrXRefreshView, true);
                        List<BuyHistoryBean> beans = (List<BuyHistoryBean>) msg.obj;
                        //                        mCurrentAdapter.setHistoryList(beans, mIsLoadMore);
                    } else {
                        mHandler.sendEmptyMessage(MSG_NO_DATA);
                    }
                    break;
                case MSG_HIDE_DIALOG:
                    hideWaitingDialog();
                    break;
                case MSG_NO_DATA:
                    if (mCurrentAdapter.getItemCount() <= 0) {
                        UIHelper.showWidget(mCurrEmptyView, true);
                        UIHelper.showWidget(mCurrXRefreshView, false);
                    }
                    break;
                case MSG_SHOW_DIALOG:
                    showWaitingDialog(OrderRecordActivity.this, R.string.tips_loading, false);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
        initViewPager();
        showDataByPosition(mCurrentPosition);
        setListener();
    }

    private void initPresenter() {
        mPresenter = new OrderRecordPresenter(this, this);
    }

    private void initView() {
        setContentView(R.layout.activity_order_history);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_buy_lottery_history);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        setTitles(allTV, R.string.mine_order_all);
        setTitles(notTV, R.string.mine_order_not_open);
        setTitles(alreadyTV, R.string.mine_order_open);
    }

    private void setTitles(TextView tv, int titleId) {
        mTitleTvs.add(tv);
        mTitles.add(StaticResourceUtils.getStringResourceById(titleId));
    }

    private void initViewPager() {
        BuyHistoryViewPagerAdapter mViewPagerAdapter = new BuyHistoryViewPagerAdapter(mViewList);
        for (int i = 0; i < mTotalViewCount; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_pager_account_detail, null);
            XRefreshView xv = view.findViewById(R.id.xrefreshview);
            RecyclerView rv = view.findViewById(R.id.viewPager_rv_content);
            rv.setLayoutManager(new LinearLayoutManager(this));
            OrderRecordAdapter adapter = new OrderRecordAdapter(this, itemView -> {
                //TODO 新增彩种需要适配 Y
                BuyHistoryBean bean = (BuyHistoryBean) itemView.getTag();
                String lotteryType = (String) SharedPreferenceUtil.get(mContext, bean.getName(), "");
                if (TextUtils.isEmpty(lotteryType)) {
                    ToastUtil.getInstances().showShort(R.string.lottery_no);
                    return;
                }
                LotteryOrderDetailActivity.inToThisActivity(OrderRecordActivity.this, bean.getId(), lotteryType);
            });
            rv.setAdapter(adapter);
            mXRefreshViews.add(xv);
            mAdapters.add(adapter);
            mRecyclerViews.add(rv);
            mViewList.add(view);
            mEmptyViews.add(view.findViewById(R.id.viewPager_tv_noData));
        }
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showDataByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mCurrXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSilence) {
                mStartPageIdx++;
                mIsLoadMore = true;
                mPresenter.requestOrderRecord(mStartPageIdx, mPageMaxNum, mCurrLotteryStatus);
            }

            @Override
            public void onRefresh() {
                mStartPageIdx = 0;
                mIsLoadMore = false;
                mPresenter.requestOrderRecord(mStartPageIdx, mPageMaxNum, mCurrLotteryStatus);
            }
        });
    }

    @OnClick({R.id.title_back, R.id.buyHistory_tv_all, R.id.buyHistory_tv_no, R.id.buyHistory_tv_done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(OrderRecordActivity.this);
                break;
            case R.id.buyHistory_tv_all:
                if (mCurrentPosition != 0) {
                    mViewPager.setCurrentItem(0);
                }
                break;
            case R.id.buyHistory_tv_no:
                if (mCurrentPosition != 1) {
                    mViewPager.setCurrentItem(1);
                }
                break;
            case R.id.buyHistory_tv_done:
                if (mCurrentPosition != 2) {
                    mViewPager.setCurrentItem(2);
                }
                break;
        }
    }


    private void showDataByPosition(int position) {
        this.mCurrentPosition = position;
        mCurrLotteryStatus = mTitles.get(position);
        mIsLoadMore = false;
        for (int i = 0; i < mTitleTvs.size(); i++) {
            TextView tv = mTitleTvs.get(i);
            if (position == i) {
                if (position == 0) {
                    setTextView(tv, 0, R.drawable.shape_btn_round_corner_red_left);
                } else if (position == mTotalViewCount - 1) {
                    setTextView(tv, 0, R.drawable.shape_btn_round_corner_red_right);
                } else {
                    setTextView(tv, 0, R.color.color_app_main);
                }
            } else {
                if (i == 0) {
                    setTextView(tv, 1, R.drawable.shape_btn_round_corner_white_left);
                } else if (i == mTotalViewCount - 1) {
                    setTextView(tv, 1, R.drawable.shape_btn_round_corner_white_right);
                } else {
                    setTextView(tv, 1, R.color.color_FFFFFF);
                }
            }
        }

        mCurrentAdapter = mAdapters.get(position);
        mCurrEmptyView = mEmptyViews.get(position);
        RecyclerView mCurrRecyclerView = mRecyclerViews.get(position);
        mCurrXRefreshView = mXRefreshViews.get(position);

        mCurrRecyclerView.setHasFixedSize(true);
        mCurrXRefreshView.setPullRefreshEnable(false);
        mCurrXRefreshView.setSilenceLoadMore();
        //设置刷新完成以后，headerview固定的时间
        mCurrXRefreshView.setPinnedTime(1000);
        mCurrXRefreshView.setMoveForHorizontal(true);
        //        mCurrXRefreshView.setAutoRefresh(true);

        int itemCount = mCurrentAdapter.getItemCount();
        if (itemCount <= 0) {
            mStartPageIdx = 0;
            mPresenter.requestOrderRecord(mStartPageIdx, mPageMaxNum, mCurrLotteryStatus);
        } else {
            mStartPageIdx = itemCount / mPageMaxNum - 1;
        }
    }

    private void setTextView(TextView tv, int type, int BackgroundId) {
        tv.setTextColor(StaticResourceUtils.getColorResourceById(type == 0 ? R.color.color_FFFFFF : R.color.color_app_main));
        tv.setBackgroundResource(BackgroundId);
    }


    //点击立即投注的跳转
    public void gotoBuyLottery(View v) {
        MainActivity.intoThisActivity(OrderRecordActivity.this, true);
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        mCurrXRefreshView.stopLoadMore();
        mCurrXRefreshView.stopRefresh();
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
    public void showTipDialog(String msgStr) {
        mHandler.sendEmptyMessage(MSG_SHOW_DIALOG);
    }

    @Override
    public void hideTipDialog() {
        mHandler.sendEmptyMessage(MSG_HIDE_DIALOG);
    }
}
