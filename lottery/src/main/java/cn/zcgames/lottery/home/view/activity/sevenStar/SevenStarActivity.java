package cn.zcgames.lottery.home.view.activity.sevenStar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.bean.response.ResultSequenceBean;
import cn.zcgames.lottery.home.listener.ChooseNumSelectedListener;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.presenter.Star7Presenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.adapter.FastThreeHistoryAdapter;
import cn.zcgames.lottery.home.view.adapter.ThreeDBallAdapter;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.home.view.iview.IStar7Activity;
import cn.zcgames.lottery.utils.DateUtils;

import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_STAR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_3;
import static cn.zcgames.lottery.app.AppConstants.SEVEN_STAR_PLAY_DIRECT;

/**
 * 七星彩
 * Berfy修改
 * 2018.8.23
 */
public class SevenStarActivity extends BaseActivity implements ChooseNumSelectedListener,
        IStar7Activity {

    private static final String TAG = "Arrange5Activity";
    @BindView(R.id.title_back)
    ImageButton backBtn;
    @BindView(R.id.title_right_button)
    TextView rightBtn;
    @BindView(R.id.SevenStar_tv_sequence)
    TextView SevenStarTvSequence;
    @BindView(R.id.SevenStar_rl_history)
    RelativeLayout SevenStarRlHistory;
    @BindView(R.id.threeD_ib_delete)
    Button deleteBtn;
    @BindView(R.id.threeD_tv_num)
    TextView SevenStarTvNum;
    @BindView(R.id.threeD_tv_money)
    TextView SevenStarTvMoney;
    @BindView(R.id.threeD_tv_ok)
    TextView okBtn;
    @BindView(R.id.SevenStarHistory_rv)
    RecyclerView mHistoryRv;
    @BindView(R.id.SevenStar_ll_hideLayout)
    LinearLayout mHistoryHideLayout;

    TextView oneLabelTv, towLabelTv, threeLabelTv, fourLabelTv, fiveLabelTv, sixLabelTv, sevenLabelTv;
    RecyclerView oneDataRv, towDataRv, threeDataRv, fourDataRv, fiveDataRv, sixDataRv, sevenDataRv;

    private Context mContext;
    private int mOneType = 0;
    private int mTowType = 1;
    private int mThreeType = 2;
    private int mFourType = 3;
    private int mFiveType = 4;
    private int mSixType = 5;
    private int mSevenType = 6;
    private int mBallNumberPerRow = 5;
    private ThreeDBallAdapter mOneAdapter, mTowAdapter, mThreeAdapter,
            mFourAdapter, mFiveAdapter, mSixAdapter, mSevenAdapter;

    private ResultSequenceBean.SequenceBean mSequenceBean;

    private List<LotteryBall> mFiveBalls = new ArrayList<>();
    private List<LotteryBall> mFourBall = new ArrayList<>();
    private List<LotteryBall> mThreeBall = new ArrayList<>();
    private List<LotteryBall> mTowBall = new ArrayList<>();
    private List<LotteryBall> mOneBall = new ArrayList<>();
    private List<LotteryBall> mSixBall = new ArrayList<>();
    private List<LotteryBall> mSevenBall = new ArrayList<>();

    private Star7Presenter mPresenter;
    private FastThreeHistoryAdapter mHistoryAdapter;
    private boolean mIsHistoryLayoutShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_7_star);
        ButterKnife.bind(this);
        this.mContext = this;
        initView();
        initAdapter();
        initPresenter();
    }

    private void initAdapter() {
        mHistoryAdapter = new FastThreeHistoryAdapter(LOTTERY_TYPE_ARRANGE_3, mContext, null);
        mHistoryRv.setAdapter(mHistoryAdapter);
    }

    private void initPresenter() {
        mPresenter = new Star7Presenter(this, this);
        mPresenter.requestCurrentSequence();
        mPresenter.requestTopTenHistory();
    }

    private void initView() {
        oneLabelTv = (TextView) findViewById(R.id.rvLabel1_tv);
        oneLabelTv.setText("第一位");
        towLabelTv = (TextView) findViewById(R.id.rvLabel2_tv);
        towLabelTv.setText("第二位");
        threeLabelTv = (TextView) findViewById(R.id.rvLabel3_tv);
        threeLabelTv.setText("第三位");
        fourLabelTv = (TextView) findViewById(R.id.rvLabel4_tv);
        fourLabelTv.setText("第四位");
        fiveLabelTv = (TextView) findViewById(R.id.rvLabel5_tv);
        fiveLabelTv.setText("第五位");
        sixLabelTv = (TextView) findViewById(R.id.rvLabel6_tv);
        sixLabelTv.setText("第六位");
        sevenLabelTv = (TextView) findViewById(R.id.rvLabel7_tv);
        sevenLabelTv.setText("第七位");

        oneDataRv = (RecyclerView) findViewById(R.id.rvData1_rv);
        towDataRv = (RecyclerView) findViewById(R.id.rvData2_rv);
        threeDataRv = (RecyclerView) findViewById(R.id.rvData3_rv);
        fourDataRv = (RecyclerView) findViewById(R.id.rvData4_rv);
        fiveDataRv = (RecyclerView) findViewById(R.id.rvData5_rv);
        sixDataRv = (RecyclerView) findViewById(R.id.rvData6_rv);
        sevenDataRv = (RecyclerView) findViewById(R.id.rvData7_rv);

        oneDataRv.setLayoutManager(createGridLayoutManager());
        towDataRv.setLayoutManager(createGridLayoutManager());
        threeDataRv.setLayoutManager(createGridLayoutManager());
        fourDataRv.setLayoutManager(createGridLayoutManager());
        fiveDataRv.setLayoutManager(createGridLayoutManager());
        sixDataRv.setLayoutManager(createGridLayoutManager());
        sevenDataRv.setLayoutManager(createGridLayoutManager());

        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);

        mFiveAdapter = createThreeDBallAdapter(mFiveType);
        fiveDataRv.setAdapter(mFiveAdapter);
        fiveDataRv.addItemDecoration(space);

        mFourAdapter = createThreeDBallAdapter(mFourType);
        fourDataRv.setAdapter(mFourAdapter);
        fourDataRv.addItemDecoration(space);

        mThreeAdapter = createThreeDBallAdapter(mThreeType);
        threeDataRv.setAdapter(mThreeAdapter);
        threeDataRv.addItemDecoration(space);

        mTowAdapter = createThreeDBallAdapter(mTowType);
        towDataRv.setAdapter(mTowAdapter);
        towDataRv.addItemDecoration(space);

        mOneAdapter = createThreeDBallAdapter(mOneType);
        oneDataRv.setAdapter(mOneAdapter);
        oneDataRv.addItemDecoration(space);

        mSixAdapter = createThreeDBallAdapter(mSixType);
        sixDataRv.setAdapter(mSixAdapter);
        sixDataRv.addItemDecoration(space);

        mSevenAdapter = createThreeDBallAdapter(mSevenType);
        sevenDataRv.setAdapter(mSevenAdapter);
        sevenDataRv.addItemDecoration(space);

        mHistoryRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private GridLayoutManager createGridLayoutManager() {
        return new GridLayoutManager(mContext, mBallNumberPerRow);
    }

    private ThreeDBallAdapter createThreeDBallAdapter(int type) {
        return new ThreeDBallAdapter(mContext, type, this);
    }

    @OnClick({R.id.title_back, R.id.title_right_button, R.id.threeD_tv_ok,
            R.id.threeD_ib_delete, R.id.SevenStar_rl_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(SevenStarActivity.this);
                break;
            case R.id.title_right_button:
                UIHelper.showToast("玩法说明");
                break;
            case R.id.threeD_tv_ok:
                mPresenter.createLocalOrder(mOneBall, mTowBall, mThreeBall, mFourBall, mFiveBalls, mSixBall, mSevenBall, mCount);
                break;
            case R.id.threeD_ib_delete:
                clearAdapter();
                break;
            case R.id.SevenStar_rl_history:
                UIHelper.showWidget(mHistoryHideLayout, !mIsHistoryLayoutShow);
                mIsHistoryLayoutShow = !mIsHistoryLayoutShow;
                break;
        }
    }

    private void clearAdapter() {
        mOneAdapter.clearSelectedBall();
        mTowAdapter.clearSelectedBall();
        mThreeAdapter.clearSelectedBall();
        mFourAdapter.clearSelectedBall();
        mFiveAdapter.clearSelectedBall();
        mSixAdapter.clearSelectedBall();
        mSevenAdapter.clearSelectedBall();
    }

    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        if (type == mFiveType) {
            mFiveBalls = balls;
        } else if (type == mFourType) {
            mFourBall = balls;
        } else if (type == mThreeType) {
            mThreeBall = balls;
        } else if (type == mTowType) {
            mTowBall = balls;
        } else if (type == mOneType) {
            mOneBall = balls;
        } else if (type == mSixType) {
            mSixBall = balls;
        } else if (type == mSevenType) {
            mSevenBall = balls;
        }
        updateCount();
    }

    private int mCount = 0;

    private void updateCount() {
        int millionSize = mFiveBalls.size();
        int thousandSize = mFourBall.size();
        int hundredSize = mThreeBall.size();
        int tenSize = mTowBall.size();
        int oneSize = mOneBall.size();
        int six = mSixBall.size();
        int seven = mSevenBall.size();
        mCount = millionSize * thousandSize * tenSize * hundredSize * oneSize * six * seven;
        SevenStarTvNum.setText("共" + mCount + "注");
        SevenStarTvMoney.setText(StringUtils.getNumberNoZero(mCount *  (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_7_STAR + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }

    /**
     * @param context
     */
    public static void intoThisActivity(Activity context) {
        Intent i = new Intent(context, SevenStarActivity.class);
        context.startActivity(i);
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        if (isOk) {
            List<LotteryResultHistory> dataList = (List<LotteryResultHistory>) object;
            mHistoryAdapter.setSHData(dataList);
        } else {
        }
    }

    @Override
    public void showTipDialog(String msgStr) {

    }

    @Override
    public void hideTipDialog() {

    }

    @Override
    public void onCreateOrder(boolean isOk, String msgStr) {
        if (isOk) {
            clearAdapter();
            LotteryOrderActivity.intoThisActivity(this, LOTTERY_TYPE_7_STAR, SEVEN_STAR_PLAY_DIRECT);
            finish();
        } else {
            showThisTipDialog(msgStr);
        }
    }

    private void showThisTipDialog(String msgString) {
        AlertDialog dialog = new AlertDialog(this)
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
        if (isOk) {
            mSequenceBean = (ResultSequenceBean.SequenceBean) msg;
            String deadTime = DateUtils.formatDateTime(mSequenceBean.getDeadline().substring(0, 19));
            String lotteryTime = DateUtils.parseUTC2HH_mm(mSequenceBean.getLottery_time().substring(0, 19));
            String sequence = mSequenceBean.getSequence();
            String tip = "第" + sequence + "期 " + deadTime + "截止投注，" + lotteryTime + "开奖";
            SevenStarTvSequence.setText(tip);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFiveBalls != null) {
            mFiveBalls.clear();
            mFiveBalls = null;
        }
        if (mFourBall != null) {
            mFourBall.clear();
            mFourBall = null;
        }
        if (mThreeBall != null) {
            mThreeBall.clear();
            mThreeBall = null;
        }
        if (mTowBall != null) {
            mTowBall.clear();
            mTowBall = null;
        }
        if (mOneBall != null) {
            mOneBall.clear();
            mOneBall = null;
        }
        if (mSixBall != null) {
            mSixBall.clear();
            mSixBall = null;
        }
        if (mSevenBall != null) {
            mSevenBall.clear();
            mSevenBall = null;
        }
        mOneAdapter = null;
        mTowAdapter = null;
        mThreeAdapter = null;
        mFourAdapter = null;
        mFiveAdapter = null;
        mSixAdapter = null;
        mSevenAdapter = null;

    }
}
