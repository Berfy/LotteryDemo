package cn.zcgames.lottery.result.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.home.bean.ResultHistoryListData;
import cn.zcgames.lottery.result.presenter.ResultHistoryPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.activity.AlwaysColorActivity;
import cn.zcgames.lottery.home.lottery.elevenfive.view.activity.Eleven5Activity;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;
import cn.zcgames.lottery.home.view.adapter.LotteryHistoryAdapter;
import cn.zcgames.lottery.view.common.refreshview.XRefreshView;
import cn.zcgames.lottery.base.IBaseView;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_ACTIVITY_TITLE;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_TYPE;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_SUM;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_EASY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_HB;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_JS;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_NEW;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2;

/**
 * 新的近期开奖记录
 * Berfy修改
 * 2018.8.23
 */
public class ResultHistoryNewActivity extends BaseActivity implements View.OnClickListener, IBaseView {

    private static final String TAG = "ResultHistoryNewActivity";

    @BindView(R.id.btn_buy)
    Button mBtnBuy;

    private static final int MSG_NO_DATA = 0;
    private static final int MSG_UPDATE_ADAPTER = 1;
    private static final int MSG_SHOW_WAITING_DIALOG = 2;
    private static final int MSG_HIDDEN_WAITING_DIALOG = 3;

    private XRefreshView mXRefreshView;
    private RecyclerView mDateListRv;
    private LotteryHistoryAdapter mAdapter;
    private View mNoDataView;

    //Activity标题
    private String mTitle;
    private String mLotteryName;//彩种名称

    //彩票类型
    private String mLotteryType = LOTTERY_TYPE_FAST_3;

    //url参数
    private String mUrlParam;

    private int mStartPage = 1;
    private int mPageSize = 20;
    private long mTs;
    private boolean mIsLoadMore = false;

    private ResultHistoryPresenter mPresenter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mIsDestroy) {
                mHandler.removeMessages(MSG_SHOW_WAITING_DIALOG);
                return;
            }
            switch (msg.what) {
                case MSG_NO_DATA:
                    if (mAdapter.getItemCount() <= 0) {
                        UIHelper.showWidget(mNoDataView, true);
                        UIHelper.showWidget(mXRefreshView, false);
                    }
                    break;
                case MSG_UPDATE_ADAPTER:
                    if (msg.obj instanceof ResultHistoryListData) {
                        ResultHistoryListData data = (ResultHistoryListData) msg.obj;
                        if (null != data.getList() && data.getList().size() > 0) {
                            mStartPage++;
                        }
                        mTs = data.getTs();
                        mAdapter.setData(data.getList(), mIsLoadMore);
                        UIHelper.showWidget(mNoDataView, false);
                        UIHelper.showWidget(mXRefreshView, true);
                    } else {

                        mHandler.sendEmptyMessage(MSG_NO_DATA);
                    }
                    break;
                case MSG_SHOW_WAITING_DIALOG:
                    showWaitingDialog(ResultHistoryNewActivity.this, R.string.tips_loading, false);
                    break;
                case MSG_HIDDEN_WAITING_DIALOG:
                    hideWaitingDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_color_history);
        ButterKnife.bind(this);
        initIntentData();
        initView();
        initAdapter();
        initPresenter();
    }

    private void initIntentData() {
        mLotteryType = getIntent().getStringExtra(PARAM_LOTTERY_TYPE);
        mUrlParam = HttpHelper.FAST_THREE_LATEST_WINNING;
        if (getIntent().hasExtra(PARAM_ACTIVITY_TITLE)) {
            mLotteryName = getIntent().getStringExtra(PARAM_ACTIVITY_TITLE);
        } else {
            mLotteryName = (String) SharedPreferenceUtil.get(mContext, mLotteryType, "");
        }
        mTitle = mLotteryName + "近期开奖";
    }

    private void initPresenter() {
        mPresenter = new ResultHistoryPresenter(this, this);
        mTs = System.currentTimeMillis();
//        mPresenter.requestF3ResultHistory(mType, mStartPage, mPageSize, mTs);
    }

    private void initAdapter() {
        mAdapter = new LotteryHistoryAdapter(this, mLotteryType);
        mDateListRv.setAdapter(mAdapter);
    }

    private void initView() {
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText(mTitle);
        mBtnBuy.setText("购买" + mLotteryName);
        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        UIHelper.showWidget(backView, true);

        mDateListRv = (RecyclerView) findViewById(R.id.history_rv_dateList);
        mXRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mDateListRv.setLayoutManager(manager);

        mXRefreshView.setPullRefreshEnable(true);
        mXRefreshView.setSilenceLoadMore();
        //设置刷新完成以后，headerview固定的时间
        mXRefreshView.setPinnedTime(1000);
        mXRefreshView.setMoveForHorizontal(true);
//        mCurrXRefreshView.setAutoRefresh(true);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSilence) {
                mIsLoadMore = true;
                mPresenter.requestResultHistory(mLotteryType, mStartPage, mPageSize, mTs);
            }

            @Override
            public void onRefresh() {
                mIsLoadMore = false;
                mStartPage = 1;
                mTs = System.currentTimeMillis();
                mPresenter.requestResultHistory(mLotteryType, mStartPage, mPageSize, mTs);
            }
        });

        mNoDataView = findViewById(R.id.noData_view);
        mXRefreshView.startRefresh();
    }

    @OnClick({R.id.btn_buy})
    public void viewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_buy:
                switch (mLotteryType) {
                    case LOTTERY_TYPE_FAST_3:
                    case LOTTERY_TYPE_FAST_3_JS:
                    case LOTTERY_TYPE_FAST_3_HB:
                    case LOTTERY_TYPE_FAST_3_NEW:
                    case LOTTERY_TYPE_FAST_3_EASY:
                        FastThreeActivity.intoThisActivity(mContext, mLotteryType, FAST_THREE_SUM);
                        break;
                    case AppConstants.LOTTERY_TYPE_11_5:
                    case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
                    case AppConstants.LOTTERY_TYPE_11_5_OLD:
                    case AppConstants.LOTTERY_TYPE_11_5_YUE:
                    case AppConstants.LOTTERY_TYPE_11_5_YILE:
                        Eleven5Activity.intoThisActivity(mContext, mLotteryType, PLAY_11_5_ANY_2);
                        break;
                    case LOTTERY_TYPE_ALWAYS_COLOR:
                    case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                        AlwaysColorActivity.intoThisActivity(mContext, mLotteryType, ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
                        break;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(ResultHistoryNewActivity.this);
                break;
        }
    }

    @Override
    public void requestResult(boolean isOk, Object obj) {
        mXRefreshView.stopRefresh();
        mXRefreshView.stopLoadMore();
        if (isOk) {
            Message msg = new Message();
            msg.what = MSG_UPDATE_ADAPTER;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        } else {
            mHandler.sendEmptyMessage(MSG_NO_DATA);
        }
    }

    @Override
    public void showTipDialog(String msgStr) {
        mHandler.sendEmptyMessage(MSG_SHOW_WAITING_DIALOG);
    }

    @Override
    public void hideTipDialog() {
        mHandler.sendEmptyMessage(MSG_HIDDEN_WAITING_DIALOG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void intoThisActivity(Activity fromActivity, String lotteryType) {
        Intent i = new Intent(fromActivity, ResultHistoryNewActivity.class);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
        fromActivity.startActivity(i);
    }

    public static void intoThisActivity(Activity fromActivity, String lotteryType, String title) {
        Intent i = new Intent(fromActivity, ResultHistoryNewActivity.class);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
        i.putExtra(PARAM_ACTIVITY_TITLE, title);
        fromActivity.startActivity(i);
    }
}
