package cn.zcgames.lottery.home.view.activity.doublecolor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.response.ResultSequenceBean;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.presenter.DoubleColorPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.adapter.DoubleColorDantuoAdapter;
import cn.zcgames.lottery.home.view.adapter.DoubleColorNormalAdapter;
import cn.zcgames.lottery.result.view.activity.ResultHistoryNewActivity;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.view.common.DBASpaceItemDecoration;
import cn.zcgames.lottery.home.view.iview.IDoubleColorActivity;
import cn.zcgames.lottery.home.listener.DoubleColorDantuoSelectBallListener;
import cn.zcgames.lottery.home.listener.DoubleColorNormalSelectBallListener;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_PLAY_STYLE_DANTUO;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_PLAY_STYLE_NORMAL;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_2_COLOR;

/**
 * 双色球
 * Berfy修改
 * 2018.8.23
 */
public class DoubleColorActivity extends BaseActivity implements
        View.OnClickListener,
        DoubleColorNormalSelectBallListener,
        DoubleColorDantuoSelectBallListener,
        IDoubleColorActivity, Animation.AnimationListener {

    private final int MSG_UPDATE_NORMAL_NOTE_INFO = 0;
    private final int MSG_UPDATE_DANTUO_NOTE_INFO = 1;
    private final int MSG_CREATE_ORDER_OK = 2;
    private final int MSG_SHOW_SEQUENCE = 3;

    private final String TAG = "DoubleColorActivity";

    private TextView mTitleTv, mTotalNumberTv, mTotalMoneyTv;
    private DoubleColorNormalAdapter mActiveRedBallAdapter, mActiveBlueBallAdapter;
    private DoubleColorDantuoAdapter mRedDanAdapter, mRedTuoAdapter, mDanTuoBlueAdapter;
    private RecyclerView mNormalRedRv, mNormalBlueRv, mDantuoBlueRv, mDantuoRedDanRv, mDantuoRedTuoRv;
    private View mActiveNormalRL, mActiveDantuoRL;
    private View mSelectPlayStyleLayout, mSelectView, mChangeStyleView;
    private boolean mIsFistInit = true;//是否第一次进入页面
    private boolean isSelectLayoutShow = false;

    private ImageView mTitleTipIv;
    private TextView mNormalAddTv, mDantuoAddTv, mSequenceTv;

    private Animation mInAnimation, mOutAnimation;
    private Animation mDown2UpAnimation, mUp2DownAnimation;

    private boolean mIsAnimRunning;

    //每行球的个数
    private int mBallNumberPerRow = 7;

    private long mNormalTotalNumber = 0;
    private long mDantuoTotalNumber = 0;//总共的投注数
    private List<LotteryBall> mSelectedRedBall = new ArrayList<>();
    private List<LotteryBall> mSelectedBlueBall = new ArrayList<>();
    private List<LotteryBall> mSelectedDantuoBlueBall = new ArrayList<>();
    private List<LotteryBall> mSelectedRedDanBall = new ArrayList<>();
    private List<LotteryBall> mSelectedRedTuoBall = new ArrayList<>();

    private CustomPopupWindow customPopupWindow;
    private int mCurrentPlayStyle = DOUBLE_COLOR_PLAY_STYLE_NORMAL;

    private DoubleColorPresenter mPresenter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_NORMAL_NOTE_INFO:
                    mPresenter.getNormalTotalCount(mSelectedBlueBall, mSelectedRedBall);
                    break;
                case MSG_UPDATE_DANTUO_NOTE_INFO:
                    mPresenter.getDantuoTotalCoount(mSelectedDantuoBlueBall, mSelectedRedDanBall, mSelectedRedTuoBall);
                    break;
                case MSG_CREATE_ORDER_OK:
                    LotteryOrderActivity.intoThisActivity(DoubleColorActivity.this, LOTTERY_TYPE_2_COLOR, 0);
                    finish();
                    break;
                case MSG_SHOW_SEQUENCE:
                    if (msg.obj != null && msg.obj instanceof ResultSequenceBean.SequenceBean) {
                        mSequenceBean = (ResultSequenceBean.SequenceBean) msg.obj;
                        String deadTime = DateUtils.formatDateTime(mSequenceBean.getDeadline().substring(0, 19));
                        String lotteryTime = DateUtils.parseUTC2HH_mm(mSequenceBean.getLottery_time().substring(0, 19));
                        String sequence = mSequenceBean.getSequence();
                        String tip = "第" + sequence + "期 " + deadTime + "截止投注，" + lotteryTime + "开奖";
                        mSequenceTv.setText(tip);
                    }
                    break;
            }
        }
    };

    /**
     * 进入此activity的入口
     *
     * @param context
     */
    public static void intoThisActivity(Context context) {
        Intent i = new Intent(context, DoubleColorActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_double_color);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initAdapter();
        initPopupWindow();
        initPresenter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryBuyOkEvent(LotteryBuyOkEvent event) {
        if (event.isOK()) {
            this.finish();
        }
    }

    private void initPresenter() {
        mPresenter = new DoubleColorPresenter(this, this);
        mPresenter.requestCurrentSequence(LOTTERY_TYPE_2_COLOR);
    }

    private void initAdapter() {
        mActiveRedBallAdapter = new DoubleColorNormalAdapter(this, AppConstants.BALL_TYPE_RED, this);
        mNormalRedRv.setAdapter(mActiveRedBallAdapter);

        mActiveBlueBallAdapter = new DoubleColorNormalAdapter(this, AppConstants.BALL_TYPE_BLUE, this);
        mNormalBlueRv.setAdapter(mActiveBlueBallAdapter);

        mRedDanAdapter = new DoubleColorDantuoAdapter(this, AppConstants.DOUBLE_COLOR_DANTUO_RED_DAN, this);
        mDantuoRedDanRv.setAdapter(mRedDanAdapter);

        mRedTuoAdapter = new DoubleColorDantuoAdapter(this, AppConstants.DOUBLE_COLOR_DANTUO_RED_TUO, this);
        mDantuoRedTuoRv.setAdapter(mRedTuoAdapter);

        mDanTuoBlueAdapter = new DoubleColorDantuoAdapter(this, AppConstants.DOUBLE_COLOR_DANTUO_BLUE, this);
        mDantuoBlueRv.setAdapter(mDanTuoBlueAdapter);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mDown2UpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_up_to_down);
        mUp2DownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_down_to_up);
        mDown2UpAnimation.setAnimationListener(this);
        mUp2DownAnimation.setAnimationListener(this);
        DBASpaceItemDecoration space = new DBASpaceItemDecoration(20);

        GridLayoutManager glm = new GridLayoutManager(this, mBallNumberPerRow);
        mNormalRedRv = (RecyclerView) findViewById(R.id.active_rv_redBall);
        mNormalRedRv.setLayoutManager(glm);
        mNormalRedRv.addItemDecoration(space);

        GridLayoutManager glm2 = new GridLayoutManager(this, mBallNumberPerRow);
        mNormalBlueRv = (RecyclerView) findViewById(R.id.active_rv_blueBall);
        mNormalBlueRv.setLayoutManager(glm2);
        mNormalBlueRv.addItemDecoration(space);

        GridLayoutManager glm3 = new GridLayoutManager(this, mBallNumberPerRow);
        mDantuoBlueRv = (RecyclerView) findViewById(R.id.active_rv_blueBallDanTuo);
        mDantuoBlueRv.setLayoutManager(glm3);
        mDantuoBlueRv.addItemDecoration(space);

        GridLayoutManager glm4 = new GridLayoutManager(this, mBallNumberPerRow);
        mDantuoRedDanRv = (RecyclerView) findViewById(R.id.active_rv_redBallDan);
        mDantuoRedDanRv.setLayoutManager(glm4);
        mDantuoRedDanRv.addItemDecoration(space);

        GridLayoutManager glm5 = new GridLayoutManager(this, mBallNumberPerRow);
        mDantuoRedTuoRv = (RecyclerView) findViewById(R.id.active_rv_redBallTuo);
        mDantuoRedTuoRv.setLayoutManager(glm5);
        mDantuoRedTuoRv.addItemDecoration(space);

        mTotalMoneyTv = (TextView) findViewById(R.id.threeD_tv_money);
        mTotalNumberTv = (TextView) findViewById(R.id.threeD_tv_num);

        mActiveNormalRL = findViewById(R.id.view_normal);
        mActiveDantuoRL = findViewById(R.id.view_dantuo);

        View okBtn = findViewById(R.id.threeD_tv_ok);
        okBtn.setOnClickListener(this);

        mSelectPlayStyleLayout = findViewById(R.id.doubleColor_rl_selectStyleLayout);
        mSelectView = findViewById(R.id.btn_view);
        mTitleTipIv = (ImageView) findViewById(R.id.imageView_label);

        mNormalAddTv = (TextView) findViewById(R.id.doubleColor_tv_normal);
        mNormalAddTv.setOnClickListener(this);
        mDantuoAddTv = (TextView) findViewById(R.id.doubleColor_tv_dantuo);
        mDantuoAddTv.setOnClickListener(this);

        findViewById(R.id.threeD_ib_delete).setOnClickListener(this);
        findViewById(R.id.empty_view).setOnClickListener(this);
        mSequenceTv = (TextView) findViewById(R.id.doubleColor_tv_sequence);

        mChangeStyleView = findViewById(R.id.doubleColor_ll_changeStyle);
        mChangeStyleView.setOnClickListener(this);
    }


    private void initTitleBar() {
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleTv.setText("双色球-普通");
        ImageButton backIb = (ImageButton) findViewById(R.id.title_back);
        UIHelper.showWidget(backIb, true);
        backIb.setOnClickListener(this);
        ImageButton moreIb = (ImageButton) findViewById(R.id.title_right_button);
        moreIb.setImageResource(R.drawable.btn_more);
        UIHelper.showWidget(moreIb, true);
        moreIb.setOnClickListener(this);

        mInAnimation = AnimationUtils.loadAnimation(this, R.anim.up_to_down_enter);
        mOutAnimation = AnimationUtils.loadAnimation(this, R.anim.up_to_down_exit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(DoubleColorActivity.this);
                break;
            case R.id.title_right_button:
                //弹出选项弹窗
                customPopupWindow.showPopupWindow(v);
                break;
            case R.id.doubleColor_ll_changeStyle:
                showOrHiddenSelectStyleLayout();
                break;
            case R.id.threeD_tv_ok:
                if (mCurrentPlayStyle == DOUBLE_COLOR_PLAY_STYLE_NORMAL) {
                    mPresenter.createNormalOrder(mSelectedBlueBall, mSelectedRedBall, mCurrentPlayStyle, mNormalTotalNumber);
                } else {
                    mPresenter.createDantuoOrder(mSelectedDantuoBlueBall, mSelectedRedDanBall, mSelectedRedTuoBall, mCurrentPlayStyle, mDantuoTotalNumber);
                }
                break;
            case R.id.doubleColor_tv_normal:
                changePlayStyle(DOUBLE_COLOR_PLAY_STYLE_NORMAL);
                break;
            case R.id.doubleColor_tv_dantuo:
                changePlayStyle(DOUBLE_COLOR_PLAY_STYLE_DANTUO);
                break;
            case R.id.threeD_ib_delete:
                clearSelectedBall();
                break;
            case R.id.empty_view:
                showOrHiddenSelectStyleLayout();
                break;
        }
    }

    private void clearSelectedBall() {
        mActiveRedBallAdapter.clearSelectedBall();
        mActiveBlueBallAdapter.clearSelectedBall();
        mRedDanAdapter.clearSelectedBall();
        mRedTuoAdapter.clearSelectedBall();
        mDanTuoBlueAdapter.clearSelectedBall();
    }

    private void showOrHiddenSelectStyleLayout() {
//        mSelectPlayStyleLayout.setAnimation(R.style.change_header_anim);
        if (isSelectLayoutShow) {
            mSelectView.startAnimation(mOutAnimation);
            //为等待动画完成，等待500毫秒再隐藏布局
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    UIHelper.showWidget(mSelectPlayStyleLayout, false);
                }
            }, 500);
        } else {
            UIHelper.showWidget(mSelectPlayStyleLayout, true);
            mSelectView.startAnimation(mInAnimation);
        }

        boolean isNormal = mCurrentPlayStyle == DOUBLE_COLOR_PLAY_STYLE_NORMAL;
        mDantuoAddTv.setSelected(!isNormal);
        mNormalAddTv.setSelected(isNormal);
        if (isNormal) {
            mNormalAddTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main));
            mDantuoAddTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_tab_text));
        } else {
            mNormalAddTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_tab_text));
            mDantuoAddTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main));
        }
        if (!mIsFistInit) {
            if (isSelectLayoutShow) {
                if (mTitleTipIv != null) {
                    mTitleTipIv.startAnimation(mUp2DownAnimation);
                }
            } else {
                mTitleTipIv.startAnimation(mDown2UpAnimation);
            }
        }
        mIsFistInit = false;
        isSelectLayoutShow = !isSelectLayoutShow;
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
                    ResultHistoryNewActivity.intoThisActivity(DoubleColorActivity.this, LOTTERY_TYPE_2_COLOR);
                } else {
                    UIHelper.gotoActivity(DoubleColorActivity.this, DCDescriptionActivity.class, false);
                }
                customPopupWindow.dismissPopupWindow();
            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        customPopupWindow = new CustomPopupWindow(this, strList, clickList);
    }

    private void changePlayStyle(int playStyle) {
        if (mIsAnimRunning) {
            return;
        }
        showOrHiddenSelectStyleLayout();
        if (playStyle == DOUBLE_COLOR_PLAY_STYLE_DANTUO) {
            mTitleTv.setText("双色球-胆拖");
            UIHelper.showWidget(mActiveDantuoRL, true);
            UIHelper.showWidget(mActiveNormalRL, false);
            mTotalNumberTv.setText(mDantuoTotalNumber + " 注");
            mTotalMoneyTv.setText(StringUtils.getNumberNoZero(mDantuoTotalNumber * (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_2_COLOR + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + " 元");
        } else {
            mTitleTv.setText("双色球-普通");
            UIHelper.showWidget(mActiveDantuoRL, false);
            UIHelper.showWidget(mActiveNormalRL, true);
            mTotalNumberTv.setText(mNormalTotalNumber + " 注");
            mTotalMoneyTv.setText(StringUtils.getNumberNoZero(mNormalTotalNumber * (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_2_COLOR + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + " 元");
        }
        mCurrentPlayStyle = playStyle;
    }

    @Override
    public void onSelectRedBallChanged(List<LotteryBall> numberList) {
        this.mSelectedRedBall = numberList;
        mHandler.sendEmptyMessage(MSG_UPDATE_NORMAL_NOTE_INFO);
    }

    @Override
    public void onSelectBlueBallChanged(List<LotteryBall> numberList) {
        this.mSelectedBlueBall = numberList;
        mHandler.sendEmptyMessage(MSG_UPDATE_NORMAL_NOTE_INFO);
    }

    @Override
    public void onSelectRedDanChanged(List<LotteryBall> numberList) {
        this.mSelectedRedDanBall = numberList;
        mHandler.sendEmptyMessage(MSG_UPDATE_DANTUO_NOTE_INFO);
    }

    @Override
    public void onSelectRedTuoChanged(List<LotteryBall> numberList) {
        this.mSelectedRedTuoBall = numberList;
        mHandler.sendEmptyMessage(MSG_UPDATE_DANTUO_NOTE_INFO);
    }

    @Override
    public void onSelectBlueDantuoChanged(List<LotteryBall> numberList) {
        this.mSelectedDantuoBlueBall = numberList;
        mHandler.sendEmptyMessage(MSG_UPDATE_DANTUO_NOTE_INFO);
    }

    @Override
    public void onSetIgnoreNumberListener(LotteryBall ball) {
        if (ball.getType() == AppConstants.DOUBLE_COLOR_DANTUO_RED_DAN) {
            mRedTuoAdapter.setIgnoreList(ball);
        } else if (ball.getType() == AppConstants.DOUBLE_COLOR_DANTUO_RED_TUO) {
            mRedDanAdapter.setIgnoreList(ball);
        }
    }

    @Override
    public void createNormalOrderResult(boolean isOk, String msgStr) {
        if (isOk) {
            mHandler.sendEmptyMessage(MSG_CREATE_ORDER_OK);
        } else {
            showMessageDialog(msgStr, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public void createDantuoOrderResult(boolean isOk, String msgStr) {
        if (isOk) {
            mHandler.sendEmptyMessage(MSG_CREATE_ORDER_OK);
        } else {
            showMessageDialog(msgStr, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    @Override
    public void getNormalTotalResult(long totalCount) {
        mNormalTotalNumber = totalCount;
        mTotalNumberTv.setText(totalCount + " 注");
        mTotalMoneyTv.setText(StringUtils.getNumberNoZero(totalCount * (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_2_COLOR + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + " 元");
    }

    @Override
    public void getDantuoTotalCountResul(long totalCount) {
        mDantuoTotalNumber = totalCount;
        mTotalNumberTv.setText(totalCount + " 注");
        mTotalMoneyTv.setText(StringUtils.getNumberNoZero(totalCount * (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_2_COLOR + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)) + " 元");
    }

    @Override
    public void showDialog(String msgStr) {

    }

    @Override
    public void hiddenDialog() {

    }

    private ResultSequenceBean.SequenceBean mSequenceBean;

    @Override
    public void requestSequenceResult(boolean isOK, Object sequence) {
        if (isOK) {
            Message msg = new Message();
            msg.obj = sequence;
            msg.what = MSG_SHOW_SEQUENCE;
            mHandler.sendMessage(msg);
        } else {
            UIHelper.showToast((String) sequence);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mIsAnimRunning = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mIsAnimRunning = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
