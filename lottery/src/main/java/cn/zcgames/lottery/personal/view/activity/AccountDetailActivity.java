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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.personal.model.AccountDetail;
import cn.zcgames.lottery.personal.presenter.AccountDetailPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.personal.view.adapter.AccountDetailAdapter;
import cn.zcgames.lottery.personal.view.adapter.AccountDetailViewPagerAdapter;
import cn.zcgames.lottery.view.common.refreshview.XRefreshView;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 我的-账户明细界面
 *
 * @author NorthStar
 * @date 2018/8/20 17:19
 */
public class AccountDetailActivity extends BaseActivity implements IBaseView {

    private static final String TAG = "AccountDetailActivity";

    private static final int MSG_UPDATE_ADAPTER = 0;
    private static final int MSG_NO_DATA = 1;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_award)
    TextView tvAward;
    @BindView(R.id.tv_withdraw)
    TextView tvWithdraw;
    @BindView(R.id.detail_vp_viewPager)
    ViewPager mViePager;

    private List<View> mEmptyViews = new ArrayList<>();
    private List<View> mViewPagerViews = new ArrayList<>();
    private List<TextView> mTitleTextViews = new ArrayList<>();
    private List<RecyclerView> mRecyclerViews = new ArrayList<>();
    private List<XRefreshView> mXRefreshViews = new ArrayList<>();
    private List<AccountDetailAdapter> mAdapterList = new ArrayList<>();

    private View mCurrEmptyView;

    private XRefreshView mCurrXRefreshView;
    private AccountDetailAdapter mCurrentAdapter;
    private int mCurrentPosition = 0;
    private int mTotalPagers = 5;
    private int mStartPageIndex = 1;
    private int mPageSize = 20;
    private boolean mIsLoadMore = false;

    private AccountDetailPresenter mPresenter;

    private MyHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
        initViewPagerViews();
        initPresenter();
        showDataByPosition(mCurrentPosition);
        setListener();
    }

    private void initPresenter() {
        mPresenter = new AccountDetailPresenter(this, this);
    }

    private void initViewPagerViews() {
        for (int i = 0; i < mTotalPagers; i++) {
            AccountDetailAdapter adapter = new AccountDetailAdapter(this, i);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.view_pager_account_detail, null);
            RecyclerView rv = view1.findViewById(R.id.viewPager_rv_content);
            rv.setLayoutManager(mLayoutManager);
            rv.setAdapter(adapter);
            XRefreshView xv = view1.findViewById(R.id.xrefreshview);
            xv.setPullRefreshEnable(true);
            xv.setSilenceLoadMore();
            //设置刷新完成以后，headerView(刷新LOGO)固定的时间
            xv.setPinnedTime(1000);
            xv.setMoveForHorizontal(true);
            xv.startRefresh();
            xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onLoadMore(boolean isSilence) {
                    mStartPageIndex++;
                    mIsLoadMore = true;
                    LogF.d(TAG, "onLoadMore--position==>" + mCurrentPosition);
                    mPresenter.requestAccountDetail(mCurrentPosition, mStartPageIndex, mPageSize);
                }

                @Override
                public void onRefresh() {
                    mStartPageIndex = 1;
                    mIsLoadMore = false;
                    LogF.d(TAG, "onRefresh--position==>" + mCurrentPosition);
                    mPresenter.requestAccountDetail(mCurrentPosition, mStartPageIndex, mPageSize);
                }
            });
            mXRefreshViews.add(xv);
            mRecyclerViews.add(rv);
            mViewPagerViews.add(view1);
            mAdapterList.add(adapter);
            mEmptyViews.add(view1.findViewById(R.id.viewPager_tv_noData));
        }
        AccountDetailViewPagerAdapter mViewPagerAdapter = new AccountDetailViewPagerAdapter(mViewPagerViews);
        mViePager.setAdapter(mViewPagerAdapter);
    }

    private void initView() {
        setContentView(R.layout.activity_account_detail);
        setButterKnife(this);
        mHandler = new MyHandler(AccountDetailActivity.this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_account_details);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        mTitleTextViews.add((TextView) findViewById(R.id.detail_tv_all));
        mTitleTextViews.add((TextView) findViewById(R.id.detail_tv_buy));
        mTitleTextViews.add((TextView) findViewById(R.id.detail_tv_recharge));
        mTitleTextViews.add((TextView) findViewById(R.id.detail_tv_paijiang));
        mTitleTextViews.add((TextView) findViewById(R.id.detail_tv_withDraw));
    }


    @OnClick({R.id.title_back, R.id.detail_tv_all, R.id.detail_tv_buy, R.id.detail_tv_recharge,
            R.id.detail_tv_paijiang, R.id.detail_tv_withDraw})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(AccountDetailActivity.this);
                break;
            case R.id.detail_tv_all:
                setSelectPos(0);
                break;
            case R.id.detail_tv_buy:
                setSelectPos(1);
                break;
            case R.id.detail_tv_recharge:
                setSelectPos(2);
                break;
            case R.id.detail_tv_paijiang:
                setSelectPos(3);
                break;
            case R.id.detail_tv_withDraw:
                setSelectPos(4);
                break;
        }
    }

    private void setSelectPos(int pos) {
        if (mCurrentPosition != pos) {
            mViePager.setCurrentItem(pos);
        }
    }

    private void showData(AccountDetail bean) {
        UIHelper.showWidget(mCurrEmptyView, false);
        UIHelper.showWidget(mCurrXRefreshView, true);
        //总计数据
        setTotal(tvBuy, bean.getBuy());
        setTotal(tvAward, bean.getAward());
        setTotal(tvRecharge, bean.getRecharge());
        setTotal(tvWithdraw, bean.getWithdraw());
        if (bean.getDetails() == null || bean.getDetails().size() <= 0) {
            mHandler.sendEmptyMessage(MSG_NO_DATA);
            return;
        }
        mCurrentAdapter.setAccountDetailBeans(bean.getDetails(), mIsLoadMore);
    }

    //设置总计数据
    private void setTotal(TextView tv, String total) {
        String labelStr = StaticResourceUtils.getStringResourceById(R.string.lottery_yuan);
        if (TextUtils.isEmpty(total)) return;
        tv.setText(String.format("%s%s", StringUtils.getCash(total, AppConstants.DIGITS), labelStr));
    }

    public void gotoBuyLottery(View v) {
        MainActivity.intoThisActivity(AccountDetailActivity.this, true);
    }

    private void showDataByPosition(int position) {
        mCurrentPosition = position;
        mIsLoadMore = false;
        mStartPageIndex = 1;
        for (int i = 0; i < mTitleTextViews.size(); i++) {
            TextView tv = mTitleTextViews.get(i);
            if (position == i) {
                if (position == 0) {
                    setTextView(tv, 0, R.drawable.shape_btn_round_corner_red_left);
                } else if (position == mTotalPagers - 1) {
                    setTextView(tv, 0, R.drawable.shape_btn_round_corner_red_right);
                } else {
                    setTextView(tv, 0, R.color.color_app_main);
                }
            } else {
                if (i == 0) {
                    setTextView(tv, 1, R.drawable.shape_btn_round_corner_white_left);
                } else if (i == mTotalPagers - 1) {
                    setTextView(tv, 1, R.drawable.shape_btn_round_corner_white_right);
                } else {
                    setTextView(tv, 1, R.color.color_f4f4f4);
                }
            }
        }
        mCurrentAdapter = mAdapterList.get(position);
        mCurrXRefreshView = mXRefreshViews.get(position);
        mCurrEmptyView = mEmptyViews.get(position);

        //设置刷新完成以后，headerview固定的时间
        mCurrXRefreshView.setPinnedTime(1000);
        mCurrXRefreshView.setPullRefreshEnable(true);
        mCurrXRefreshView.setPullLoadEnable(true);
        mCurrXRefreshView.setAutoLoadMore(true);
        mCurrXRefreshView.setSilenceLoadMore();
        //        mCurrentAdapter.setAccountDetailBeans(initData(position));
        mPresenter.requestAccountDetail(mCurrentPosition, mStartPageIndex, mPageSize);
    }

    private void setTextView(TextView tv, int type, int BackgroundId) {
        tv.setTextColor(StaticResourceUtils.getColorResourceById(type == 0 ? R.color.color_FFFFFF : R.color.color_999999));
        tv.setBackgroundResource(BackgroundId);
    }

    private void setListener() {
        mViePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogF.d(TAG, "onLoadMore--position==>" + position);
                showDataByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        mCurrXRefreshView.stopLoadMore();
        mCurrXRefreshView.stopRefresh();
        Message msg = new Message();
        msg.obj = object;
        if (isOk) {
            msg.what = MSG_UPDATE_ADAPTER;
        } else {
            msg.what = MSG_NO_DATA;
        }
        if (mHandler!=null) mHandler.sendMessage(msg);
    }

    @Override
    public void showTipDialog(String msgStr) {
//        if (mCurrentAdapter != null && mCurrentAdapter.getItemCount() > 0) return;
//        showWaitingDialog(this, msgStr, false);
    }

    @Override
    public void hideTipDialog() {
//        if (mCurrentAdapter != null && mCurrentAdapter.getItemCount() > 0) return;
//        hideWaitingDialog();
    }

    private static class MyHandler extends Handler {
        private WeakReference<AccountDetailActivity> weakReference;
        MyHandler(AccountDetailActivity activity) {
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AccountDetailActivity tempActivity = weakReference.get();
            if (tempActivity == null || tempActivity.isFinishing()) return;
            switch (msg.what) {
                case MSG_UPDATE_ADAPTER:
                    if (msg.obj != null && msg.obj instanceof AccountDetail) {
                        AccountDetail bean = (AccountDetail) msg.obj;
                        tempActivity.showData(bean);
                    } else {
                        tempActivity.mHandler.sendEmptyMessage(MSG_NO_DATA);
                    }
                    break;
                case MSG_NO_DATA:
                    if (tempActivity.mCurrentAdapter.getItemCount() <= 0) {
                        UIHelper.showWidget(tempActivity.mCurrEmptyView, true);
                        UIHelper.showWidget(tempActivity.mCurrXRefreshView, false);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}
