package cn.zcgames.lottery.home.view.activity.arrange3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.bean.response.ResultSequenceBean;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.home.presenter.Arrange3Presenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.adapter.FastThreeHistoryAdapter;
import cn.zcgames.lottery.result.view.activity.ResultHistoryNewActivity;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.home.view.fragment.arrange3.DirectFragment;
import cn.zcgames.lottery.home.view.fragment.arrange3.GroupSixFragment;
import cn.zcgames.lottery.home.view.fragment.arrange3.GroupThreeFragment;
import cn.zcgames.lottery.home.view.iview.IArrange3Activity;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_PLAY_TYPE;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_GROUP_SIX;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_GROUP_THREE;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_3;

/**
 * 排列3
 * Berfy修改
 * 2018.8.23
 */
public class Arrange3Activity extends BaseActivity implements View.OnClickListener, IArrange3Activity {

    private static final String TAG = "Arrange3Activity";
    @BindView(R.id.arrange3_rv_history)
    RecyclerView mHistoryRv;
    @BindView(R.id.arrange3_ll_hideLayout)
    LinearLayout mHideLayout;

    private Context mContext;

    private static final int MSG_SHOW_SEQUENCE = 0;

    private GroupSixFragment mSixFragment;
    private GroupThreeFragment mThreeFragment;
    private DirectFragment mDirectFragment;
    private View mSelectPlayStyleLayout, mSelectView, mChangeStyleView;
    private ImageView mTitleTipIv;
    private TextView mDirectTv, mGroupSixTv, mGroupThreeTv, mSequenceTv, mTitleTv;

    private boolean isSelectLayoutShow = true;
    private int mCurrentPlayStyle = ARRANGE_3_PLAY_DIRECT;
    private final String FRAGMENT_TAG_DIRECT = "DIRECT";
    private final String FRAGMENT_TAG_THREE = "THREE";
    private final String FRAGMENT_TAG_SIX = "SIX";
    private final String[] fragmentTags = new String[]{FRAGMENT_TAG_DIRECT,
            FRAGMENT_TAG_THREE, FRAGMENT_TAG_SIX};

    private int mPlayMode;//当前玩法
    private ResultSequenceBean.SequenceBean mSequenceBean;

    private Animation mInAnimation, mOutAnimation;
    private List<TextView> mTextViewList;
    private Arrange3Presenter mPresenter;

    private FastThreeHistoryAdapter mHistoryAdapter;
    private boolean mIsHistoryLayoutShow = false;
    private CustomPopupWindow customPopupWindow;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_arrange3);
        ButterKnife.bind(this);
        this.mContext = this;
        initIntentData();
        initView();
        initPresenter();
        initAdapter();
        initPopupWindow();
        if (mPlayMode == ARRANGE_3_PLAY_DIRECT) {
            mDirectTv.performClick();
        } else if (mPlayMode == ARRANGE_3_PLAY_GROUP_SIX) {
            mGroupSixTv.performClick();
        } else if (mPlayMode == ARRANGE_3_PLAY_GROUP_THREE) {
            mGroupThreeTv.performClick();
        }
        EventBus.getDefault().register(this);
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
                    ResultHistoryNewActivity.intoThisActivity(Arrange3Activity.this, LOTTERY_TYPE_ARRANGE_3);
                } else {
                    UIHelper.gotoActivity(Arrange3Activity.this, Arrange3DescriptionActivity.class, false);
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
        mPresenter = new Arrange3Presenter(this, this);
        mPresenter.requestSequence();
        mPresenter.requestHistory();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryBuyOkEvent(LotteryBuyOkEvent event) {
        if (event.isOK()) {
            this.finish();
        }
    }

    private void initIntentData() {
        mPlayMode = getIntent().getIntExtra(PARAM_LOTTERY_PLAY_TYPE, ARRANGE_3_PLAY_DIRECT);
    }

    private void initView() {
        ImageButton moreIb = (ImageButton) findViewById(R.id.title_right_button);
        moreIb.setImageResource(R.drawable.btn_more);
        moreIb.setOnClickListener(this);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleTv.setText("排列三-直选");

        View backView = findViewById(R.id.title_back);
        UIHelper.showWidget(backView, true);
        backView.setOnClickListener(this);

        mInAnimation = AnimationUtils.loadAnimation(this, R.anim.up_to_down_enter);
        mOutAnimation = AnimationUtils.loadAnimation(this, R.anim.up_to_down_exit);

        mSelectPlayStyleLayout = findViewById(R.id.arrange3_rl_selectStyleLayout);
        mSelectView = findViewById(R.id.btn_view);
        mTitleTipIv = (ImageView) findViewById(R.id.imageView_label);

        mDirectTv = (TextView) findViewById(R.id.arrange3_tv_direct);
        mDirectTv.setOnClickListener(this);
        mGroupSixTv = (TextView) findViewById(R.id.arrange3_tv_group6);
        mGroupSixTv.setOnClickListener(this);
        mGroupThreeTv = (TextView) findViewById(R.id.arrange3_tv_group3);
        mGroupThreeTv.setOnClickListener(this);
        mSequenceTv = (TextView) findViewById(R.id.arrange3_tv_sequence);

        mTextViewList = new ArrayList<>();
        mTextViewList.add(mDirectTv);
        mTextViewList.add(mGroupThreeTv);
        mTextViewList.add(mGroupSixTv);

        mChangeStyleView = findViewById(R.id.arrange3_ll_changeStyle);
        mChangeStyleView.setOnClickListener(this);

        findViewById(R.id.arrange3_rl_history).setOnClickListener(this);

        mHistoryRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrange3_rl_history:
                UIHelper.showWidget(mHideLayout, !mIsHistoryLayoutShow);
                mIsHistoryLayoutShow = !mIsHistoryLayoutShow;
                break;
            case R.id.title_back:
                goBack(Arrange3Activity.this);
                break;
            case R.id.title_right_button:
                //弹出选项弹窗
                customPopupWindow.showPopupWindow(v);
                break;
            case R.id.arrange3_ll_changeStyle:
                showOrHiddenSelectStyleLayout(mCurrentPlayStyle);
                break;
            case R.id.arrange3_tv_direct:
                showOrHiddenSelectStyleLayout(ARRANGE_3_PLAY_DIRECT);
                onPlayStyleSelect(R.id.arrange3_tv_direct);
                break;
            case R.id.arrange3_tv_group3:
                showOrHiddenSelectStyleLayout(ARRANGE_3_PLAY_GROUP_THREE);
                onPlayStyleSelect(R.id.arrange3_tv_group3);
                break;
            case R.id.arrange3_tv_group6:
                showOrHiddenSelectStyleLayout(ARRANGE_3_PLAY_GROUP_SIX);
                onPlayStyleSelect(R.id.arrange3_tv_group6);
                break;
        }
    }

    public void onPlayStyleSelect(int id) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        if (id == R.id.arrange3_tv_direct) {
            if (mDirectFragment == null) {
                mDirectFragment = new DirectFragment();
                transaction.add(R.id.fragment_container, mDirectFragment, FRAGMENT_TAG_DIRECT);
            }
            transaction.show(mDirectFragment);
        } else if (id == R.id.arrange3_tv_group3) {
            if (mThreeFragment == null) {
                mThreeFragment = new GroupThreeFragment();
                transaction.add(R.id.fragment_container, mThreeFragment, FRAGMENT_TAG_THREE);
            }
            transaction.show(mThreeFragment);
        } else if (id == R.id.arrange3_tv_group6) {
            if (mSixFragment == null) {
                mSixFragment = new GroupSixFragment();
                transaction.add(R.id.fragment_container, mSixFragment, FRAGMENT_TAG_SIX);
            }
            transaction.show(mSixFragment);
        }
        transaction.commit();
    }

    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    private void showOrHiddenSelectStyleLayout(int playStyle) {
        mCurrentPlayStyle = playStyle;
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

        if (isSelectLayoutShow) {
            mTitleTipIv.setImageResource(R.drawable.label_jiantou_down);
        } else {
            mTitleTipIv.setImageResource(R.drawable.label_jiantou_up);
        }
        if (mCurrentPlayStyle == ARRANGE_3_PLAY_DIRECT) {
            mTitleTv.setText("排列三-直选");
        } else if (mCurrentPlayStyle == ARRANGE_3_PLAY_GROUP_SIX) {
            mTitleTv.setText("排列三-组选六");
        } else if (mCurrentPlayStyle == ARRANGE_3_PLAY_GROUP_THREE) {
            mTitleTv.setText("排列三-组选三");
        }
        updateTextViewStatus(mCurrentPlayStyle);
        isSelectLayoutShow = !isSelectLayoutShow;
    }

    private void updateTextViewStatus(int selectedIndex) {
        for (int i = 0; i < mTextViewList.size(); i++) {
            TextView tv = mTextViewList.get(i);
            if (i == selectedIndex) {
                tv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main));
                tv.setSelected(true);
            } else {
                tv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_tab_text));
                tv.setSelected(false);
            }
        }
    }

    /**
     * 外界进入本界面的入口
     *
     * @param fromActivity
     * @param mPlayMode    玩法
     */
    public static void intoThisActivity(Activity fromActivity, int mPlayMode) {
        Intent i = new Intent(fromActivity, Arrange3Activity.class);
        i.putExtra(PARAM_LOTTERY_PLAY_TYPE, mPlayMode);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onTotalCountResult(int count) {

    }

    @Override
    public void onCreateDirectOrder(boolean isOk, String msgStr) {

    }

    @Override
    public void onRequestSequence(boolean isOk, Object msgStr) {
        if (isOk) {
            Message msg = new Message();
            msg.obj = msgStr;
            msg.what = MSG_SHOW_SEQUENCE;
            mHandler.sendMessage(msg);
        } else {
            UIHelper.showToast((String) msgStr);
        }
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
    public void showTipDialog(String msg) {
        UIHelper.showWaitingDialog(this, msg, false);
    }

    @Override
    public void hideTipDialog() {
        UIHelper.hideWaitingDialog();
    }
}
