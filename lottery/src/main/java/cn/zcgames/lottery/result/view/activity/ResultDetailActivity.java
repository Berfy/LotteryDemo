package cn.zcgames.lottery.result.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.bean.response.DoubleColorDetailBean;
import cn.zcgames.lottery.result.presenter.ResultDetailPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.activity.AlwaysColorActivity;
import cn.zcgames.lottery.home.view.activity.arrange3.Arrange3Activity;
import cn.zcgames.lottery.home.view.activity.arrange5.Arrange5Activity;
import cn.zcgames.lottery.home.view.activity.doublecolor.DoubleColorActivity;
import cn.zcgames.lottery.home.lottery.elevenfive.view.activity.Eleven5Activity;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;
import cn.zcgames.lottery.home.view.activity.sevenStar.SevenStarActivity;
import cn.zcgames.lottery.home.view.activity.sevenhappy.SevenHappyActivity;
import cn.zcgames.lottery.home.view.activity.threeD.ThreeDActivity;
import cn.zcgames.lottery.home.view.adapter.DoubleColorDetailAdapter;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.utils.GsonUtils;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_DOUBLE_COLOR_HISTORY_BEAN;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_TYPE;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_STAR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_5;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_2_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_HAPPY;

/**
 * 开奖记录详情页
 * Berfy修改
 * 2018.8.23
 */
public class ResultDetailActivity extends BaseActivity implements View.OnClickListener,
        IBaseView {

    private static final String TAG = "ResultDetailActivity";

    private static final int MSG_NO_DATA = 0;
    private static final int MSG_UPDATE_VIEW = 1;
    private static final int MSG_SHOW_WAITING_DIALOG = 2;
    private static final int MSG_HIDDEN_WAITING_DIALOG = 3;
    @BindView(R.id.detail_ll_prize_pool)
    LinearLayout mPrizePoolLl;
    @BindView(R.id.detail_tv_sum)
    TextView mTvSum;//和值
    @BindView(R.id.type_fast_three)
    View mPrizrFastThree;//快三奖金和玩法
    @BindView(R.id.type_115)
    View mPrizr115;//11选5奖金和玩法
    @BindView(R.id.type_ac)
    View mPrizrAc;//时时彩奖金和玩法

    private String mSequence;
    private LotteryResultHistory mLotteryResultHistory;

    private RecyclerView mPrizeRv;
    private DoubleColorDetailAdapter mAdapter;
    private TextView mSequenceTv, mTimeTv, mTotalTv, mPoolTv;
    private View mNoDataView, mDataView;
    private LinearLayout mBallView;

    private ResultDetailPresenter mPresenter;

    private String mLotteryType;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mHandler == null || mIsDestroy) {
                return;
            }
            switch (msg.what) {
                case MSG_NO_DATA:
                    UIHelper.showWidget(mNoDataView, true);
                    UIHelper.showWidget(mDataView, false);
                    break;
                case MSG_UPDATE_VIEW:
                    if (msg.obj instanceof DoubleColorDetailBean.DoubleColorDetail) {
                        DoubleColorDetailBean.DoubleColorDetail bean = (DoubleColorDetailBean.DoubleColorDetail) msg.obj;
                        showData(bean);
                        UIHelper.showWidget(mNoDataView, false);
                        UIHelper.showWidget(mDataView, true);
                    } else {
                        mHandler.sendEmptyMessage(MSG_NO_DATA);
                    }
                    break;
                case MSG_SHOW_WAITING_DIALOG:
                    showWaitingDialog(ResultDetailActivity.this, R.string.tips_loading, false);
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
        setContentView(R.layout.activity_result_detail);
        ButterKnife.bind(this);
        initIntentData();
        initView();
        initAdapter();
        initPresenter();
        showData();
    }

    private void initPresenter() {
        mPresenter = new ResultDetailPresenter(this, this);
    }

    private void initIntentData() {
        Intent i = getIntent();
        mLotteryType = i.getStringExtra(PARAM_LOTTERY_TYPE);
        String jsonStr = i.getStringExtra(PARAM_DOUBLE_COLOR_HISTORY_BEAN);
        LogF.d(TAG, "本地结果" + jsonStr);
        mLotteryResultHistory = GsonUtils.formatStringToDoubleColorHistory(jsonStr);
        mSequence = mLotteryResultHistory.getSequence();
    }

    private void initAdapter() {
        mAdapter = new DoubleColorDetailAdapter(this, mLotteryType);
        mPrizeRv.setAdapter(mAdapter);
    }

    private void initView() {
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText(R.string.result_detail);
//        if (mLotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
//            titleTv.setText(R.string.result_detail_doubleColor);
//        } else {
//            titleTv.setText(R.string.result_detail_3d);
//        }

        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        UIHelper.showWidget(backView, true);

        mPrizeRv = (RecyclerView) findViewById(R.id.sequence_rv_prizes);
        mPrizeRv.setLayoutManager(new LinearLayoutManager(this));

        mSequenceTv = (TextView) findViewById(R.id.detail_tv_sequence);
        mTimeTv = (TextView) findViewById(R.id.detail_tv_datetime);
        mTotalTv = (TextView) findViewById(R.id.detail_tv_total_selling);
        mPoolTv = (TextView) findViewById(R.id.detail_tv_prize_pool);

        mNoDataView = findViewById(R.id.noData_view);
        mDataView = findViewById(R.id.data_view);
        findViewById(R.id.resultDetail_tv_gotoBuy).setOnClickListener(this);

        mBallView = (LinearLayout) findViewById(R.id.detail_ll_ballView);
    }

    private void showData() {
        mPresenter.showDoubleColorView(mLotteryResultHistory, mBallView, mTvSum, mLotteryType);
        DoubleColorDetailBean.DoubleColorDetail bean = new DoubleColorDetailBean().new DoubleColorDetail();
        bean.setDatetime(mLotteryResultHistory.getDatetime());
        bean.setSequence(mSequence);
        bean.setInfo(new ArrayList<>());
        bean.setPrizes(
                new ArrayList<>());
        Message msg = new Message();
        msg.what = MSG_UPDATE_VIEW;
        msg.obj = bean;
        mHandler.sendMessage(msg);
    }

    private void showData(DoubleColorDetailBean.DoubleColorDetail mDoubleColorDetail) {
        mSequenceTv.setText("第" + mDoubleColorDetail.getSequence() + "期");
        mTimeTv.setText(mDoubleColorDetail.getDatetime());
        //TODO 新增彩种需要适配 Y
        if (mLotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
            mTotalTv.setText(mDoubleColorDetail.getTotal_selling());
            mPoolTv.setText(mDoubleColorDetail.getPrize_pool());
            UIHelper.showWidget(mPrizePoolLl, true);
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
            mTotalTv.setText(mDoubleColorDetail.getSale_volume());
            mPoolTv.setText(mDoubleColorDetail.getPrize_pool());
            UIHelper.showWidget(mPrizePoolLl, true);
        } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
            mTotalTv.setText(mDoubleColorDetail.getTotal_selling());
            mPoolTv.setText(mDoubleColorDetail.getPrize_pool());
            UIHelper.showWidget(mPrizePoolLl, true);
            switchInfo2Prices(mDoubleColorDetail);
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {
            mTotalTv.setText(mDoubleColorDetail.getTotal_selling());
            mPoolTv.setText(mDoubleColorDetail.getPrize_pool());
            UIHelper.showWidget(mPrizePoolLl, true);
            switchInfo2Prices(mDoubleColorDetail);
        } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            mTotalTv.setText(mDoubleColorDetail.getTotal_selling());
            UIHelper.showWidget(mPrizePoolLl, false);
            switchInfo2Prices(mDoubleColorDetail);
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_EASY)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)) {
            mPrizrFastThree.setVisibility(View.VISIBLE);
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
            mPrizr115.setVisibility(View.VISIBLE);
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            mPrizrAc.setVisibility(View.VISIBLE);
        } else {
            List<DoubleColorDetailBean.Prize> prizes = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                DoubleColorDetailBean.Prize p = new DoubleColorDetailBean.Prize();
                if (i == 0) {
                    p.setLevel("直选");
                    p.setWinning_prize("1,040");
                } else if (i == 1) {
                    p.setLevel("组选3");
                    p.setWinning_prize("346");
                } else {
                    p.setLevel("组选6");
                    p.setWinning_prize("173");
                }
                p.setWinning_bet("_");
                prizes.add(p);
            }

            mDoubleColorDetail.setPrizes(prizes);
            mTotalTv.setText(mDoubleColorDetail.getSale_volume());
            UIHelper.showWidget(mPrizePoolLl, false);
        }
        mAdapter.setDoubleColorHistorys(mDoubleColorDetail.getPrizes());
    }

    private void switchInfo2Prices(DoubleColorDetailBean.DoubleColorDetail mDoubleColorDetail) {
        List<DoubleColorDetailBean.Prize> infos = mDoubleColorDetail.getInfo();

        List<DoubleColorDetailBean.Prize> prizes = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            DoubleColorDetailBean.Prize p = new DoubleColorDetailBean.Prize();
            DoubleColorDetailBean.Prize info = infos.get(i);
            p.setLevel(info.getName());
            p.setWinning_bet(info.getNum());
            p.setWinning_prize(info.getPrice());
            prizes.add(p);
        }
        mDoubleColorDetail.setPrizes(prizes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(ResultDetailActivity.this);
                break;
            case R.id.resultDetail_tv_gotoBuy:
                //TODO 新增彩种需要适配 Y
                if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_EASY)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)) {
                    FastThreeActivity.intoThisActivity(ResultDetailActivity.this, mLotteryType, AppConstants.FAST_THREE_SUM);
                } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
                    Eleven5Activity.intoThisActivity(ResultDetailActivity.this, mLotteryType, AppConstants.PLAY_11_5_ANY_2);
                } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                        || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                    AlwaysColorActivity.intoThisActivity(ResultDetailActivity.this, mLotteryType, AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
                } else if (mLotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
                    UIHelper.gotoActivity(ResultDetailActivity.this, DoubleColorActivity.class, false);
                } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
                    SevenHappyActivity.intoThisActivity(ResultDetailActivity.this);
                } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
                    UIHelper.gotoActivity(ResultDetailActivity.this, Arrange3Activity.class, false);
                } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
                    UIHelper.gotoActivity(ResultDetailActivity.this, Arrange5Activity.class, false);
                } else if (mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {
                    SevenStarActivity.intoThisActivity(ResultDetailActivity.this);
                } else if (mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {
                    SevenStarActivity.intoThisActivity(ResultDetailActivity.this);
                } else {
                    UIHelper.gotoActivity(ResultDetailActivity.this, ThreeDActivity.class, false);
                }
                break;
        }
    }

    @Override
    public void requestResult(boolean isOk, Object obj) {
        if (isOk) {
            Message msg = new Message();
            msg.what = MSG_UPDATE_VIEW;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        } else {
            UIHelper.showToast((String) obj);
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


    /**
     * 进入这个页面的入口
     *
     * @param fromActivity
     * @param lotteryType
     * @param jsonStr
     */
    public static void intoThisActivity(Activity fromActivity, String lotteryType, String jsonStr) {
        Intent i = new Intent(fromActivity, ResultDetailActivity.class);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
        i.putExtra(PARAM_DOUBLE_COLOR_HISTORY_BEAN, jsonStr);
        fromActivity.startActivity(i);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler = null;
        }
    }
}
