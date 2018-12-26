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
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.response.OrderRecordInfo;
import cn.zcgames.lottery.personal.presenter.OrderRecordPresenter;
import cn.zcgames.lottery.personal.view.adapter.BuyHistoryViewPagerAdapter;
import cn.zcgames.lottery.personal.view.adapter.OrderRecordAdapter;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.view.common.refreshview.XRefreshView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 投注记录列表
 *
 * @author NorthStar
 * @date 2018/8/20 16:52
 */
public class LotteryOrderRecordActivity extends BaseActivity implements IBaseView, ViewPager.OnPageChangeListener {

    private static final String TAG = "LotteryOrderRecordActivity";

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
    private int mCurrLotteryStatus = 1;//当前查询的开奖状态：1.全部、2.已开奖、3.未开奖
    private int mTotalViewCount = 3;//viewpager包含的view的个数
    private boolean mIsLoadMore = false;//是否加载更多
    private int mStartPageIdx = 1;//开始页码为0
    private long ts = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_DATA:
                    updateUI(msg);
                    break;
                case MSG_HIDE_DIALOG:
                    hideWaitingDialog();
                    break;
                case MSG_NO_DATA:
                    setEmpty();
                    break;
                case MSG_SHOW_DIALOG:
                    showWaitingDialog(LotteryOrderRecordActivity.this, R.string.tips_loading, false);
                    break;
            }
        }
    };

    //暂无数据
    private void setEmpty() {
        if (mStartPageIdx == 1) {
            mCurrentAdapter.setHistoryList(null, false);
        }
        mStartPageIdx--;
        if (mCurrentAdapter.getItemCount() <= 0) {
            UIHelper.showWidget(mCurrEmptyView, true);
            UIHelper.showWidget(mCurrXRefreshView, false);
        }
        mCurrXRefreshView.stopRefresh();
        mCurrXRefreshView.stopLoadMore();
    }

    //获取网络数据更新UI
    private void updateUI(Message msg) {
        if (msg.obj != null && msg.obj instanceof OrderRecordInfo) {
            UIHelper.showWidget(mCurrEmptyView, false);
            UIHelper.showWidget(mCurrXRefreshView, true);
            OrderRecordInfo recordInfo = (OrderRecordInfo) msg.obj;
            List<OrderRecordInfo.OrderList> orderList = recordInfo.getOrderList();
            if (orderList == null || orderList.size() == 0) {
                mHandler.sendEmptyMessage(MSG_NO_DATA);
                return;
            }
            ts = -1;
            mCurrentAdapter.setHistoryList(orderList, mIsLoadMore);
        } else {
            mHandler.sendEmptyMessage(MSG_NO_DATA);
        }
        mCurrXRefreshView.stopRefresh();
        mCurrXRefreshView.stopLoadMore();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
        initViewPager();
        showDataByPosition(mCurrentPosition);
        mViewPager.setOnPageChangeListener(this);
    }

    private void initPresenter() {
        mPresenter = new OrderRecordPresenter(this, this);
    }

    private void initView() {
        setContentView(R.layout.activity_order_history);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_buy_lottery_history);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        mTitleTvs.add(allTV);
        mTitleTvs.add(notTV);
        mTitleTvs.add(alreadyTV);
    }

    private void initViewPager() {
        BuyHistoryViewPagerAdapter mViewPagerAdapter = new BuyHistoryViewPagerAdapter(mViewList);
        for (int i = 0; i < mTotalViewCount; i++) {
            setTabView();
        }
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    //设置页面数据
    private void setTabView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_pager_account_detail, null);
        RecyclerView rv = view.findViewById(R.id.viewPager_rv_content);
        rv.setLayoutManager(new LinearLayoutManager(this));
        OrderRecordAdapter adapter = new OrderRecordAdapter(this, itemView -> {
            OrderRecordInfo.OrderList bean = (OrderRecordInfo.OrderList) itemView.getTag();
            setLotteryAdapter(bean);//新增彩种需要适配
        });
        XRefreshView xv = view.findViewById(R.id.xrefreshview);
        xv.setPullRefreshEnable(true);
        xv.setSilenceLoadMore();
        //设置刷新完成以后，headerView(刷新LOGO)固定的时间
        xv.setPinnedTime(1000);
        xv.setMoveForHorizontal(true);
        xv.startRefresh();
        xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSilence) {
                LogF.d(TAG, "mStartPageIdx==>" + mStartPageIdx);
                mIsLoadMore = true;
                mStartPageIdx++;
                mPresenter.requestOrderRecord(mStartPageIdx, ts, mCurrLotteryStatus);
            }

            @Override
            public void onRefresh() {
                mStartPageIdx = 1;
                mIsLoadMore = false;
                mPresenter.requestOrderRecord(mStartPageIdx, ts, mCurrLotteryStatus);
            }
        });
        mXRefreshViews.add(xv);
        rv.setAdapter(adapter);
        mAdapters.add(adapter);
        mRecyclerViews.add(rv);
        mViewList.add(view);
        mEmptyViews.add(view.findViewById(R.id.viewPager_tv_noData));
    }

    private void setLotteryAdapter(OrderRecordInfo.OrderList bean) {
        String lotteryType;
        String name = bean.getName();
        if (!TextUtils.isEmpty(name)) {
            if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_double_color))) {
                lotteryType = AppConstants.LOTTERY_TYPE_2_COLOR;
            } else if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_fucai_threeD1))
                    || name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_fucai_threeD2))) {
                lotteryType = AppConstants.LOTTERY_TYPE_3_D;
            } else if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_always_color))) {
                lotteryType = AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;
            } else if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_always_color_new))) {
                lotteryType = AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW;
            } else if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_seven_happy))) {
                lotteryType = AppConstants.LOTTERY_TYPE_7_HAPPY;
            } else if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_a3))) {
                lotteryType = AppConstants.LOTTERY_TYPE_ARRANGE_3;
            } else if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_a5))) {
                lotteryType = AppConstants.LOTTERY_TYPE_ARRANGE_5;
            } else if (name.equals(StaticResourceUtils.getStringResourceById(R.string.lottery_type_7star))) {
                lotteryType = AppConstants.LOTTERY_TYPE_7_STAR;
            } else {
                lotteryType = name;
            }
        } else {
            ToastUtil.getInstances().showShort(R.string.lottery_no);
            return;
        }
        LotteryOrderDetailActivity.inToThisActivity(LotteryOrderRecordActivity.this, bean.getOrderId(), lotteryType);
    }


    @OnClick({R.id.title_back, R.id.buyHistory_tv_all, R.id.buyHistory_tv_no, R.id.buyHistory_tv_done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(LotteryOrderRecordActivity.this);
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
        mCurrLotteryStatus = position + 1;
        mIsLoadMore = false;
        setTabState(position);
        mCurrentAdapter = mAdapters.get(position);
        RecyclerView mCurrRecyclerView = mRecyclerViews.get(position);
        mCurrRecyclerView.setHasFixedSize(true);
        mCurrXRefreshView = mXRefreshViews.get(position);
        mCurrXRefreshView.setPullRefreshEnable(true);
        mCurrXRefreshView.setSilenceLoadMore();
        mCurrEmptyView = mEmptyViews.get(position);
        //设置刷新完成以后，headerView固定的时间
        mCurrXRefreshView.setPinnedTime(1000);
        mCurrXRefreshView.setMoveForHorizontal(true);

        int itemCount = mCurrentAdapter.getItemCount();
        if (itemCount <= 0) {
            mStartPageIdx = 1;
            mPresenter.requestOrderRecord(mStartPageIdx, ts, mCurrLotteryStatus);
        } else {
            int mPageMaxNum = 20;
            int mStartPage= itemCount / mPageMaxNum;
            mStartPageIdx = mStartPage == 0 ? 1 : mStartPage;
        }

        LogF.d(TAG, "itemCount==>" + itemCount + ", mStartPageIdx==>" + mStartPageIdx);
    }

    //切换title状态
    private void setTabState(int position) {
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
                    setTextView(tv, 1, R.color.color_f4f4f4);
                }
            }
        }
    }

    private void setTextView(TextView tv, int type, int BackgroundId) {
        tv.setTextColor(StaticResourceUtils.getColorResourceById(type == 0 ? R.color.color_FFFFFF : R.color.color_999999));
        tv.setBackgroundResource(BackgroundId);
    }


    public void gotoBuyLottery(View v) {
        MainActivity.intoThisActivity(LotteryOrderRecordActivity.this, true);
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
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
        /*if (mCurrentAdapter != null && mCurrentAdapter.getItemCount() > 0) return;
        mHandler.sendEmptyMessage(MSG_SHOW_DIALOG);*/
    }

    @Override
    public void hideTipDialog() {
       /* if (mCurrentAdapter != null && mCurrentAdapter.getItemCount() > 0) return;
        mHandler.sendEmptyMessage(MSG_HIDE_DIALOG);*/
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        showDataByPosition(position);
        LogF.d(TAG, "走了--onPageSelected()");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
