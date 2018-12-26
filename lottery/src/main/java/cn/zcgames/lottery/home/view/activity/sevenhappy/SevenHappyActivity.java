package cn.zcgames.lottery.home.view.activity.sevenhappy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.presenter.SevenHappyPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.adapter.FastThreeHistoryAdapter;
import cn.zcgames.lottery.home.view.adapter.SevenNormalAdapter;
import cn.zcgames.lottery.result.view.activity.ResultHistoryNewActivity;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.home.view.iview.ISevenActivity;
import cn.zcgames.lottery.home.listener.SevenSelectedListener;
import cn.zcgames.lottery.utils.DateUtils;

import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_HAPPY;
import static cn.zcgames.lottery.app.AppConstants.SEVEN_HAPPY_PLAY_STYLE_NORMAL;

/**
 * 七乐彩
 * Berfy修改
 * 2018.8.23
 */
public class SevenHappyActivity extends BaseActivity implements SevenSelectedListener, ISevenActivity {

    private static final String TAG = "SevenHappyActivity";

    @BindView(R.id.title_back)
    ImageButton titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.seven_ll_changeStyle)
    LinearLayout sevenLlChangeStyle;
    @BindView(R.id.seven_titleBar)
    LinearLayout mLlTitleBar;
    @BindView(R.id.seven_tv_sequence)
    TextView sevenTvSequence;
    @BindView(R.id.seven_tv_containView)
    TextView sevenTvContainView;
    @BindView(R.id.seven_ib_history)
    ImageView sevenIbHistory;
    @BindView(R.id.seven_rl_history)
    RelativeLayout sevenRlHistory;
    @BindView(R.id.seven_tips)
    LinearLayout sevenTips;
    @BindView(R.id.seven_rv_history)
    RecyclerView sevenRvHistory;
    @BindView(R.id.seven_ll_hideLayout)
    LinearLayout sevenLlHideLayout;
    @BindView(R.id.seven_rv_ballNumber)
    RecyclerView mBallNumberRv;
    @BindView(R.id.threeD_ib_delete)
    Button sevenIbDelete;
    @BindView(R.id.threeD_tv_num)
    TextView sevenTvNum;
    @BindView(R.id.threeD_tv_money)
    TextView sevenTvMoney;
    @BindView(R.id.threeD_tv_ok)
    TextView sevenTvOk;

    private SevenNormalAdapter mBallAdapter;

    private SevenHappyPresenter mSevenHappyPresenter;

    private boolean mIsHistoryShow = false;

    private List<LotteryBall> mSelectedBall;
    private int mCount;

    private FastThreeHistoryAdapter mHistoryAdapter;
    private CustomPopupWindow customPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_seven_happy);
        ButterKnife.bind(this);
        initView();
        initAdapter();
        initPopupWindow();
        initPresenter();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryBuyOkEvent(LotteryBuyOkEvent event) {
        if (event.isOK()) {
            this.finish();
        }
    }

    private void initView() {
        ImageButton moreIb = (ImageButton) findViewById(R.id.title_right_button);
        moreIb.setImageResource(R.drawable.btn_more);
    }

    private void initPresenter() {
        mSevenHappyPresenter = new SevenHappyPresenter(this, this);
        mSevenHappyPresenter.requestTopTenHistory();
        mSevenHappyPresenter.requestCurrentSequence();
    }

    private void initAdapter() {
        mBallAdapter = new SevenNormalAdapter(this, this);
        mBallNumberRv.setAdapter(mBallAdapter);
        mBallNumberRv.setLayoutManager(new GridLayoutManager(this, 7));
        mBallNumberRv.addItemDecoration(new DBASpaceItemDecoration(20));
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
                    ResultHistoryNewActivity.intoThisActivity(SevenHappyActivity.this, LOTTERY_TYPE_7_HAPPY);
                } else {
                    UIHelper.gotoActivity(SevenHappyActivity.this, SHDescriptionActivity.class, false);
                }
                customPopupWindow.dismissPopupWindow();
            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        customPopupWindow = new CustomPopupWindow(this, strList, clickList);
    }

    @OnClick({R.id.title_back,
            R.id.title_right_button, R.id.seven_rl_history, R.id.threeD_ib_delete, R.id.threeD_tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(this);
                break;
            case R.id.title_right_button:
                //弹出选项弹窗
                customPopupWindow.showPopupWindow(view);
                break;
            case R.id.seven_rl_history:
                UIHelper.showWidget(sevenLlHideLayout, !mIsHistoryShow);
                mIsHistoryShow = !mIsHistoryShow;
                break;
            case R.id.threeD_ib_delete:
                mBallAdapter.clearSelectedBall();
                break;
            case R.id.threeD_tv_ok:
                mSevenHappyPresenter.createLocalOrder(mSelectedBall, mCount);
                break;
        }
    }

    /**
     * 进入此activity的入口
     *
     * @param fromActivity
     */
    public static void intoThisActivity(Activity fromActivity) {
        Intent i = new Intent(fromActivity, SevenHappyActivity.class);
        fromActivity.startActivity(i);
    }

    @Override
    public void onSelectBall(List<LotteryBall> balls) {
        this.mSelectedBall = balls;
        mSevenHappyPresenter.calculateOrderCount(balls.size());
    }

    //请求历史开奖记录
    @Override
    public void requestResult(boolean isOk, Object object) {
        if (isOk) {
            List<LotteryResultHistory> dataList = (List<LotteryResultHistory>) object;
            mHistoryAdapter = new FastThreeHistoryAdapter(LOTTERY_TYPE_7_HAPPY, this, null);
            sevenRvHistory.setAdapter(mHistoryAdapter);
            sevenRvHistory.setLayoutManager(new LinearLayoutManager(this));
            mHistoryAdapter.setSHData(dataList);
        }
    }

    @Override
    public void showTipDialog(String msgStr) {

    }

    @Override
    public void hideTipDialog() {

    }

    @Override
    public void onSelectedOrderCount(int count) {
        this.mCount = count;
        sevenTvNum.setText("共" + mCount + "注");
        sevenTvMoney.setText(StringUtils.getNumberNoZero(mCount *  (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_7_HAPPY + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + "元");
    }

    @Override
    public void onResultCurrentSequence(boolean b, Object msgStr) {
        if (b) {
            ResultSequenceBean.SequenceBean bean = (ResultSequenceBean.SequenceBean) msgStr;
            String deadTime = DateUtils.formatDateTime(bean.getDeadline().substring(0, 19));
            String lotteryTime = DateUtils.parseUTC2HH_mm(bean.getLottery_time().substring(0, 19));
            String sequence = bean.getSequence();
            String tip = "第" + sequence + "期 " + deadTime + "截止投注，" + lotteryTime + "开奖";
            sevenTvSequence.setText(tip);
        }
    }

    @Override
    public void onResultCreateLocalOrder(boolean b, String msgStr) {
        if (b) {
            LotteryOrderActivity.intoThisActivity(SevenHappyActivity.this, LOTTERY_TYPE_7_HAPPY, SEVEN_HAPPY_PLAY_STYLE_NORMAL);
            finish();
        } else {
//            UIHelper.showToast(msgStr);
            showMessageDialog(msgStr, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
