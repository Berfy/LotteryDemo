package cn.zcgames.lottery.home.view.activity.threeD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.response.ResultSequenceBean;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.home.presenter.ThreeDPresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.result.view.activity.ResultHistoryNewActivity;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.home.view.fragment.threed.DirectFragment;
import cn.zcgames.lottery.home.view.fragment.threed.GroupSixFragment;
import cn.zcgames.lottery.home.view.fragment.threed.GroupThreeFragment;
import cn.zcgames.lottery.home.view.iview.IThreeDActivity;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_PLAY_TYPE;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_3_D;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_GROUP_SIX;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_GROUP_THREE;

/**
 * 福彩3D
 * Berfy修改
 * 2018.8.23
 */
public class ThreeDActivity extends BaseActivity implements View.OnClickListener, IThreeDActivity, Animation.AnimationListener {

    private static final String TAG = "ThreeDActivity";

    private static final int MSG_SHOW_SEQUENCE = 0;

    private CustomPopupWindow mPopupWindow;
    private GroupSixFragment mSixFragment;
    private GroupThreeFragment mThreeFragment;
    private DirectFragment mDirectFragment;
    private View mSelectPlayStyleLayout, mSelectView, mChangeStyleView;
    private ImageView mTitleTipIv;
    private TextView mDirectTv, mGroupSixTv, mGroupThreeTv, mSequenceTv, mTitleTv;
    View mHistoryBtn;
    View mContainerView;
    View mHideLayout;

    private boolean isSelectLayoutShow = false;
    private int mCurrentPlayStyle = THREE_D_PLAY_DIRECT;
    private final String FRAGMENT_TAG_DIRECT = "DIRECT";
    private final String FRAGMENT_TAG_THREE = "THREE";
    private final String FRAGMENT_TAG_SIX = "SIX";
    private final String[] fragmentTags = new String[]{FRAGMENT_TAG_DIRECT,
            FRAGMENT_TAG_THREE, FRAGMENT_TAG_SIX};

    private int mPlayMode;//当前玩法
    private ResultSequenceBean.SequenceBean mSequenceBean;
    private boolean mIsFistInit = true;//是否第一次进入页面

    private Animation mInAnimation, mOutAnimation;
    private Animation mDown2UpAnimation, mUp2DownAnimation;

    private boolean mIsAnimRunning;

    private ThreeDPresenter mPresenter;

    List<TextView> mTextViewList;

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
        setContentView(R.layout.activity_lottery_three_d);
        initIntentData();
        initView();
        initPopupWindow();
        initPresenter();
        if (mPlayMode == THREE_D_PLAY_DIRECT) {
            mDirectTv.performClick();
        } else if (mPlayMode == THREE_D_PLAY_GROUP_SIX) {
            mGroupSixTv.performClick();
        } else if (mPlayMode == THREE_D_PLAY_GROUP_THREE) {
            mGroupThreeTv.performClick();
        }
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryBuyOkEvent(LotteryBuyOkEvent event) {
        if (event.isOK()) {
            this.finish();
        }
    }

    private void initPresenter() {
        mPresenter = new ThreeDPresenter(this, this);
        mPresenter.requestSequence();
    }

    private void initIntentData() {
        mPlayMode = getIntent().getIntExtra(PARAM_LOTTERY_PLAY_TYPE, THREE_D_PLAY_DIRECT);
    }

    private void initView() {
        mDown2UpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_up_to_down);
        mUp2DownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_down_to_up);
        mDown2UpAnimation.setAnimationListener(this);
        mUp2DownAnimation.setAnimationListener(this);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleTv.setText("福彩3D");

        View backView = findViewById(R.id.title_back);
        UIHelper.showWidget(backView, true);
        backView.setOnClickListener(this);

        ImageButton imageButton = (ImageButton) findViewById(R.id.title_right_button);
        UIHelper.showWidget(imageButton, true);
        imageButton.setOnClickListener(this);

        mInAnimation = AnimationUtils.loadAnimation(this, R.anim.up_to_down_enter);
        mOutAnimation = AnimationUtils.loadAnimation(this, R.anim.up_to_down_exit);

        mSelectPlayStyleLayout = findViewById(R.id.threeD_rl_selectStyleLayout);
        mSelectView = findViewById(R.id.btn_view);
        mTitleTipIv = (ImageView) findViewById(R.id.imageView_label);

        mDirectTv = (TextView) findViewById(R.id.threeD_tv_direct);
        mDirectTv.setOnClickListener(this);
        mGroupSixTv = (TextView) findViewById(R.id.threeD_tv_group6);
        mGroupSixTv.setOnClickListener(this);
        mGroupThreeTv = (TextView) findViewById(R.id.threeD_tv_group3);
        mGroupThreeTv.setOnClickListener(this);
        mSequenceTv = (TextView) findViewById(R.id.threeD_tv_sequence);

        mTextViewList = new ArrayList<>();
        mTextViewList.add(mDirectTv);
        mTextViewList.add(mGroupThreeTv);
        mTextViewList.add(mGroupSixTv);

        mHistoryBtn = findViewById(R.id.historyTv);
        mHistoryBtn.setOnClickListener(this);

        mHideLayout = findViewById(R.id.hideLayout);
        mHideLayout.setOnClickListener(this);

        mContainerView = findViewById(R.id.fragment_container);

        mChangeStyleView = findViewById(R.id.threeD_ll_changeStyle);
        mChangeStyleView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hideLayout:
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainerView.getLayoutParams();
                lp.setMargins(0, 0, 0, 0);
                mContainerView.setLayoutParams(lp);
                UIHelper.showWidget(mHideLayout, false);
                break;
            case R.id.historyTv:
                int height = mHideLayout.getHeight();
                Log.e(TAG, "onClick: height is " + height);
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mContainerView.getLayoutParams();
                lp2.setMargins(0, UIHelper.px2dip(ThreeDActivity.this, 120), 0, 0);
                mContainerView.setLayoutParams(lp2);
                UIHelper.showWidget(mHideLayout, true);
                break;
            case R.id.title_back:
                goBack(ThreeDActivity.this);
                break;
            case R.id.title_right_button:
                mPopupWindow.showPopupWindow(v);
                break;
            case R.id.threeD_ll_changeStyle:
                showOrHiddenSelectStyleLayout(mCurrentPlayStyle);
                break;
            case R.id.threeD_tv_direct:
                showOrHiddenSelectStyleLayout(THREE_D_PLAY_DIRECT);
                onPlayStyleSelect(v);
                break;
            case R.id.threeD_tv_group3:
                showOrHiddenSelectStyleLayout(THREE_D_PLAY_GROUP_THREE);
                onPlayStyleSelect(v);
                break;
            case R.id.threeD_tv_group6:
                showOrHiddenSelectStyleLayout(THREE_D_PLAY_GROUP_SIX);
                onPlayStyleSelect(v);
                break;
        }
    }

    public void onPlayStyleSelect(View v) {
        if (mIsAnimRunning) {
            return;
        }
        int id = v.getId();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        if (id == R.id.threeD_tv_direct) {
            if (mDirectFragment == null) {
                mDirectFragment = new DirectFragment();
                transaction.add(R.id.fragment_container, mDirectFragment, FRAGMENT_TAG_DIRECT);
            }
            transaction.show(mDirectFragment);
        } else if (id == R.id.threeD_tv_group3) {
            if (mThreeFragment == null) {
                mThreeFragment = new GroupThreeFragment();
                transaction.add(R.id.fragment_container, mThreeFragment, FRAGMENT_TAG_THREE);
            }
            transaction.show(mThreeFragment);
        } else if (id == R.id.threeD_tv_group6) {
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
        if (mIsAnimRunning) {
            return;
        }
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
        if (mCurrentPlayStyle == THREE_D_PLAY_DIRECT) {
            mTitleTv.setText("福彩3D-直选");
        } else if (mCurrentPlayStyle == THREE_D_PLAY_GROUP_SIX) {
            mTitleTv.setText("福彩3D-组选六");
        } else if (mCurrentPlayStyle == THREE_D_PLAY_GROUP_THREE) {
            mTitleTv.setText("福彩3D-组选三");
        }
        updateTextViewStatus(mCurrentPlayStyle);
        if (!mIsFistInit)
            if (isSelectLayoutShow) {
                if (mTitleTipIv != null) {
                    mTitleTipIv.startAnimation(mUp2DownAnimation);
                }
            } else {
                mTitleTipIv.startAnimation(mDown2UpAnimation);
            }
        mIsFistInit = false;
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
                    ResultHistoryNewActivity.intoThisActivity(ThreeDActivity.this, LOTTERY_TYPE_3_D);
                } else {
                    UIHelper.gotoActivity(ThreeDActivity.this, ThreeDDescriptionActivity.class, false);
                }
                mPopupWindow.dismissPopupWindow();
            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        mPopupWindow = new CustomPopupWindow(this, strList, clickList);
    }

    @Override
    public void onTotalCountResult(int count) {

    }

    @Override
    public void onCreateDirectOrder(boolean isOk, String msgStr) {

    }

    @Override
    public void onRequestSequence(boolean isOk, Object obj) {
        if (isOk) {
            Message msg = new Message();
            msg.obj = obj;
            msg.what = MSG_SHOW_SEQUENCE;
            mHandler.sendMessage(msg);
        } else {
            UIHelper.showToast((String) obj);
        }
    }

    /**
     * 外界进入本界面的入口
     *
     * @param context
     */
    public static void intoThisActivity(Context context) {
        Intent i = new Intent(context, ThreeDActivity.class);
        i.putExtra(PARAM_LOTTERY_PLAY_TYPE, THREE_D_PLAY_DIRECT);
        context.startActivity(i);
    }

    /**
     * 外界进入本界面的入口
     *
     * @param context
     * @param mPlayMode 玩法
     */
    public static void intoThisActivity(Context context, int mPlayMode) {
        Intent i = new Intent(context, ThreeDActivity.class);
        i.putExtra(PARAM_LOTTERY_PLAY_TYPE, mPlayMode);
        context.startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
