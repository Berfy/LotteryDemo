package cn.zcgames.lottery.home.view.activity.arrange5;

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
import cn.zcgames.lottery.home.presenter.Arrange5Presenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.adapter.FastThreeHistoryAdapter;
import cn.zcgames.lottery.home.view.adapter.ThreeDBallAdapter;
import cn.zcgames.lottery.result.view.activity.ResultHistoryNewActivity;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.home.view.iview.IArrange5Activity;
import cn.zcgames.lottery.utils.DateUtils;

import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_5;

/**
 * 排列5
 * Berfy修改
 * 2018.8.23
 */
public class Arrange5Activity extends BaseActivity implements ChooseNumSelectedListener,
        IArrange5Activity {

    private static final String TAG = "Arrange5Activity";
    @BindView(R.id.title_back)
    ImageButton backBtn;
    @BindView(R.id.arrange5_tv_sequence)
    TextView arrange5TvSequence;
    @BindView(R.id.arrange5_rl_history)
    RelativeLayout arrange5RlHistory;
    @BindView(R.id.recyclerView_million)
    RecyclerView mMillionRv;
    @BindView(R.id.recyclerView_thousand)
    RecyclerView mThousandRv;
    @BindView(R.id.recyclerView_hundred)
    RecyclerView mHundredRv;
    @BindView(R.id.recyclerView_ten)
    RecyclerView mTenRv;
    @BindView(R.id.recyclerView_one)
    RecyclerView mOneRv;
    @BindView(R.id.threeD_ib_delete)
    Button deleteBtn;
    @BindView(R.id.threeD_tv_num)
    TextView arrange5TvNum;
    @BindView(R.id.threeD_tv_money)
    TextView arrange5TvMoney;
    @BindView(R.id.threeD_tv_ok)
    TextView okBtn;
    @BindView(R.id.arrange5History_rv)
    RecyclerView mHistoryRv;
    @BindView(R.id.arrange5_ll_hideLayout)
    LinearLayout mHistoryHideLayout;

    private Context mContext;
    private int mOneType = 0;
    private int mTenType = 1;
    private int mHundredType = 2;
    private int mThousandType = 3;
    private int mMillionType = 4;
    private int mBallNumberPerRow = 5;
    private ThreeDBallAdapter mTenAdapter, mOneAdapter, mHundredAdapter, mThousandAdapter, mMillionAdapter;

    private ResultSequenceBean.SequenceBean mSequenceBean;

    private List<LotteryBall> mMillionBalls = new ArrayList<>();
    private List<LotteryBall> mThousandBall = new ArrayList<>();
    private List<LotteryBall> mHundredBall = new ArrayList<>();
    private List<LotteryBall> mTenBall = new ArrayList<>();
    private List<LotteryBall> mOneBall = new ArrayList<>();

    private Arrange5Presenter mPresenter;
    private FastThreeHistoryAdapter mHistoryAdapter;
    private boolean mIsHistoryLayoutShow = false;
    private CustomPopupWindow customPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_arrange5);
        ButterKnife.bind(this);
        this.mContext = this;
        initView();
        initAdapter();
        initPopupWindow();
        initPresenter();
    }

    private void initAdapter() {
        mHistoryAdapter = new FastThreeHistoryAdapter(LOTTERY_TYPE_ARRANGE_3, mContext, null);
        mHistoryRv.setAdapter(mHistoryAdapter);
    }

    private void initPopupWindow() {
        //下面的操作是初始化弹出数据
        ArrayList<String> strList = new ArrayList<>();
        strList.add("近期开奖");
        strList.add("玩法说明");


        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = (String) v.getTag();
                if (msg.equals("近期开奖")) {
                    ResultHistoryNewActivity.intoThisActivity(Arrange5Activity.this, LOTTERY_TYPE_ARRANGE_5);
                } else {
                    UIHelper.gotoActivity(Arrange5Activity.this, Arrange5DescriptionActivity.class, false);
                }
                customPopupWindow.dismissPopupWindow();
            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        customPopupWindow = new CustomPopupWindow(this, strList, clickList);
    }

    private void initPresenter() {
        mPresenter = new Arrange5Presenter(this, this);
        mPresenter.requestCurrentSequence();
        mPresenter.requestTopTenHistory();
    }

    private void initView() {
        ImageButton moreIb = (ImageButton) findViewById(R.id.title_right_button);
        moreIb.setImageResource(R.drawable.btn_more);
        mHundredRv.setLayoutManager(createGridLayoutManager());
        mTenRv.setLayoutManager(createGridLayoutManager());
        mOneRv.setLayoutManager(createGridLayoutManager());
        mThousandRv.setLayoutManager(createGridLayoutManager());
        mMillionRv.setLayoutManager(createGridLayoutManager());

        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);

        mMillionAdapter = createThreeDBallAdapter(mMillionType);
        mMillionRv.setAdapter(mMillionAdapter);
        mMillionRv.addItemDecoration(space);

        mThousandAdapter = createThreeDBallAdapter(mThousandType);
        mThousandRv.setAdapter(mThousandAdapter);
        mThousandRv.addItemDecoration(space);

        mHundredAdapter = createThreeDBallAdapter(mHundredType);
        mHundredRv.setAdapter(mHundredAdapter);
        mHundredRv.addItemDecoration(space);

        mTenAdapter = createThreeDBallAdapter(mTenType);
        mTenRv.setAdapter(mTenAdapter);
        mTenRv.addItemDecoration(space);

        mOneAdapter = createThreeDBallAdapter(mOneType);
        mOneRv.setAdapter(mOneAdapter);
        mOneRv.addItemDecoration(space);

        mHistoryRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private GridLayoutManager createGridLayoutManager() {
        return new GridLayoutManager(mContext, mBallNumberPerRow);
    }

    private ThreeDBallAdapter createThreeDBallAdapter(int type) {
        return new ThreeDBallAdapter(mContext, type, this);
    }

    @OnClick({R.id.title_back, R.id.title_right_button, R.id.threeD_tv_ok,
            R.id.threeD_ib_delete, R.id.arrange5_rl_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(Arrange5Activity.this);
                break;
            case R.id.title_right_button:
                //弹出选项弹窗
                customPopupWindow.showPopupWindow(view);
                break;
            case R.id.threeD_tv_ok:
                mPresenter.createLocalOrder(mMillionBalls, mThousandBall, mHundredBall, mTenBall, mOneBall, mCount);
                break;
            case R.id.threeD_ib_delete:
                clearAdapter();
                break;
            case R.id.arrange5_rl_history:
                UIHelper.showWidget(mHistoryHideLayout, !mIsHistoryLayoutShow);
                mIsHistoryLayoutShow = !mIsHistoryLayoutShow;
                break;
        }
    }

    private void clearAdapter() {
        mTenAdapter.clearSelectedBall();
        mOneAdapter.clearSelectedBall();
        mHundredAdapter.clearSelectedBall();
        mThousandAdapter.clearSelectedBall();
        mMillionAdapter.clearSelectedBall();
    }

    @Override
    public void onSelectBall(int type, List<LotteryBall> balls) {
        if (type == mMillionType) {
            mMillionBalls = balls;
        } else if (type == mThousandType) {
            mThousandBall = balls;
        } else if (type == mHundredType) {
            mHundredBall = balls;
        } else if (type == mTenType) {
            mTenBall = balls;
        } else if (type == mOneType) {
            mOneBall = balls;
        }
        updateCount();
    }

    private int mCount = 0;

    private void updateCount() {
        int millionSize = mMillionBalls.size();
        int thousandSize = mThousandBall.size();
        int hundredSize = mHundredBall.size();
        int tenSize = mTenBall.size();
        int oneSize = mOneBall.size();
        mCount = millionSize * thousandSize * tenSize * hundredSize * oneSize;
        arrange5TvNum.setText("共" + mCount + "注");
        arrange5TvMoney.setText(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_ARRANGE_5 + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }

    /**
     * @param context
     */
    public static void intoThisActivity(Activity context) {
        Intent i = new Intent(context, Arrange5Activity.class);
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
            LotteryOrderActivity.intoThisActivity(this, LOTTERY_TYPE_ARRANGE_5, ARRANGE_3_PLAY_DIRECT);
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
            arrange5TvSequence.setText(tip);
        }
    }
}
