package cn.zcgames.lottery.home.view.activity.fastthree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.response.ResultSequenceBeanNew;
import cn.zcgames.lottery.event.LotteryBuyOkEvent;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.presenter.FastThreePresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.home.view.activity.TrendActivity;
import cn.zcgames.lottery.home.view.fragment.fastthree.ThreeDifferentFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.ThreeDifferentOldFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.ThreeSameFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.TowSameFragment;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.view.common.CustomPopupWindow;
import cn.zcgames.lottery.home.view.fragment.fastthree.SumFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.ThreeSameAllFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.ThreeSameSingleFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.ThreeToAllFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.TowDifferentFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.TowSameMoreFragment;
import cn.zcgames.lottery.home.view.fragment.fastthree.TowSameSingleFragment;
import cn.zcgames.lottery.home.view.iview.IFastThreeActivity;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_PLAY_TYPE;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_TYPE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_MORE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ALL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_TO_ALL;
import static cn.zcgames.lottery.app.AppConstants.LATEST_OPEN_AWARD;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_SUM;
import static cn.zcgames.lottery.app.AppConstants.TREND_VIEW;

/**
 * 快3
 * Berfy修改
 * 2018.8.23
 */
public class FastThreeActivity extends BaseActivity implements IFastThreeActivity, Animation.AnimationListener {

    private static final String TAG = "FastThreeActivity";

    private static final int MSG_UPDATE_COUNT_DOWN = 0;
    private static final int MSG_REQUEST_CURRENT_SEQUENCE_OK = 3;
    private static final int MSG_REQUEST_CURRENT_SEQUENCE_FAIL = 4;

    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.title_back)
    ImageButton mBackBtn;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.imageView_label)
    ImageView mStyleLabelIv;
    @BindView(R.id.fast3_ll_changeStyle)
    LinearLayout mChangeStyleBtn;
    @BindView(R.id.fast3_tv_sequence)
    TextView mSequenceTv;
    @BindView(R.id.fast3_tv_countDown)
    TextView mCountDownTv;
    @BindView(R.id.ll_history)
    LinearLayout mLlHistory;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.fast3_ll_selectLayout)
    LinearLayout mSelectStyleLayout;
    @BindView(R.id.fast3_tv_sum)
    TextView fast3TvSum;
    @BindView(R.id.fast3_tv_3same_single)
    TextView fast3Tv3sameSingle;
    //    @BindView(R.id.fast3_tv_3same_all)
//    TextView fast3Tv3sameAll;
//    @BindView(R.id.fast3_tv_3to_all)
//    TextView fast3Tv3toAll;
//    @BindView(R.id.fast3_tv_2same_single)
//    TextView fast3Tv2sameSingle;
    @BindView(R.id.fast3_tv_2same_more)
    TextView fast3Tv2sameMore;
    @BindView(R.id.fast3_tv_3different)
    TextView fast3Tv3different;
    @BindView(R.id.fast3_tv_2different)
    TextView fast3Tv2different;

    private Animation mDown2UpAnimation, mUp2DownAnimation;

    private MyHandler mHandler;
    private boolean mIsAnimRunning;
    private TextView[] mSelectStyleBtns;
    private String mLotteryType;//彩种
    private int mPlayStyle;//玩法
    private String mLotteryName;//玩法
    private long mCountDownTime = 60 * 10 * 1000;//默认倒计时时间
    private boolean mIsFistInit = true;//是否第一次进入页面
    private boolean mIsSelectStyleLayoutShow = true;//选择玩法layout是否隐藏
    private FastThreePresenter mPresenter;//
    private ResultSequenceBeanNew.SequenceBean mCurrentSequence;//当前的期号

    private SumFragment mSumFragment;//和值
    private final String FRAGMENT_TAG_SUM = "sum";

    private ThreeSameFragment mThreeSameFragment;//三同号
    private final String FRAGMENT_TAG_THREE_SAME = "3Same";

    private ThreeSameSingleFragment mThreeSameSingleFragment;//三同号单选
    private final String FRAGMENT_TAG_THREE_SAME_SINGLE = "3SameSingle";

    private ThreeSameAllFragment mThreeSameAllFragment;//三同号通选
    private final String FRAGMENT_TAG_THREE_SAME_ALL = "3SameAll";

    private ThreeToAllFragment mThreeToAllFragment;//三连号通选
    private final String FRAGMENT_TAG_THREE_TO_ALL = "ThreeToAllFragment";

    private TowSameSingleFragment mTowSameSingleFragment;//二同号单选
    private final String FRAGMENT_TAG_TOW_SAME_SINGLE = "2SameSingle";

    private TowSameFragment mTowSameFragment;//二同号复选
    private final String FRAGMENT_TAG_TOW_SAME = "TowSameFragment";

    private TowSameMoreFragment mTowSameMoreFragment;//二同号复选
    private final String FRAGMENT_TAG_TOW_SAME_MORE = "TowSameMoreFragment";

    private ThreeDifferentFragment mThreeDifferentFragment;//三不同
    private final String FRAGMENT_TAG_THREE_DIFFERENT = "3different";

    private ThreeDifferentOldFragment mThreeDifferentOldFragment;//三不同
//    private final String FRAGMENT_TAG_THREE_DIFFERENT = "3different";

    private TowDifferentFragment mTowDifferentFragment;//二不同
    private final String FRAGMENT_TAG_TOW_DIFFERENT = "2different";

    private final String[] fragmentTags = new String[]{FRAGMENT_TAG_SUM, FRAGMENT_TAG_THREE_SAME_SINGLE,
            FRAGMENT_TAG_THREE_SAME, FRAGMENT_TAG_THREE_SAME_ALL, FRAGMENT_TAG_THREE_TO_ALL, FRAGMENT_TAG_TOW_SAME_SINGLE,
            FRAGMENT_TAG_TOW_SAME_MORE, FRAGMENT_TAG_TOW_SAME, FRAGMENT_TAG_THREE_DIFFERENT, FRAGMENT_TAG_TOW_DIFFERENT
    };

    private CustomPopupWindow customPopupWindow;

    private static class MyHandler extends Handler {

        private WeakReference<FastThreeActivity> reference;

        private MyHandler(FastThreeActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == reference.get()) {
                return;
            }
            if (reference.get().isFinishing()) {
                return;
            }
            switch (msg.what) {
                case MSG_UPDATE_COUNT_DOWN:
                    reference.get().mCountDownTv.setText(DateUtils.formatTime_mm_ss(reference.get().mCountDownTime * 1000));
                    reference.get().mCountDownTime = reference.get().mCountDownTime - 1;
                    if (reference.get().mCountDownTime >= 0) {
                        sendEmptyMessageDelayed(MSG_UPDATE_COUNT_DOWN, 1000);
                    } else {
                        reference.get().mCountDownTv.setText("正在开奖");
//                        if (mIsActivityResume) {
                        reference.get().showConfirmNextChase();
//                        } else {
//                            requestData();
//                        }
                    }
                    break;
                case MSG_REQUEST_CURRENT_SEQUENCE_OK:
                    if (msg.obj != null && msg.obj instanceof ResultSequenceBeanNew.SequenceBean) {
                        reference.get().mCurrentSequence = (ResultSequenceBeanNew.SequenceBean) msg.obj;
                        reference.get().showCurrentSequence(reference.get().mCurrentSequence);
                    }
                    break;
            }
        }

    }

    private void showCurrentSequence(ResultSequenceBeanNew.SequenceBean bean) {
        mSequenceTv.setText("距" + bean.getCur_period() + "期截止");
        mCountDownTime = bean.getDraw_time_left();
        //true允许购买
        boolean orderState = getOrderState();
        LogF.d("111111", "当前是否可以购彩==="+orderState);
        if(orderState){
            //取消上一次倒计时
            if (null != mHandler) {
                mHandler.sendEmptyMessage(MSG_UPDATE_COUNT_DOWN);
            }
        }else{
            mCountDownTv.setText("暂停销售");
        }
    }

    private void showConfirmNextChase() {
        if (ActivityManager.getInstance().currentActivity() != this) {
            return;
        }
        AlertDialog dialog = new AlertDialog(this)
                .builder()
                .setTitle(getString(R.string.tips_tip))
                .setMsg(getString(R.string.tip_period_timeout))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.btn_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBack(FastThreeActivity.this);
                    }
                }).setPositiveButton(getString(R.string.tip_good), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestData();
                    }
                });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_fast_three);
        this.mContext = this;
        ButterKnife.bind(this);
        initIntent();
        initView();
        initPopupWindow();
        showFragmentByPlayStyle();
        initPresenter();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryBuyOkEvent(LotteryBuyOkEvent event) {
        if (event.isOK()) {
            this.finish();
        }
    }

    private void initPopupWindow() {
        //下面的操作是初始化弹出数据
        ArrayList<String> strList = new ArrayList<>();
        strList.add(getString(R.string.lottery_title_result_trend));
        strList.add(getString(R.string.lottery_title_history_result));
        strList.add(getString(R.string.lottery_title_play_info));

        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = (String) v.getTag();
                if (msg.equals(getString(R.string.lottery_title_history_result))) {
                    TrendActivity.intoThisActivity(mContext, mLotteryType, mPlayStyle, LATEST_OPEN_AWARD);
                } else if (msg.equals(getString(R.string.lottery_title_result_trend))) {
                    TrendActivity.intoThisActivity(mContext, mLotteryType, mPlayStyle, TREND_VIEW);
                } else {
                    UIHelper.gotoActivity(FastThreeActivity.this, FastThreeDescriptionActivity.class, false);
                }
                customPopupWindow.dismissPopupWindow();
            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        customPopupWindow = new CustomPopupWindow(this, strList, clickList);
    }

    private void initPresenter() {
        mPresenter = new FastThreePresenter(this, this);
        requestData();
    }

    private void requestData() {
        mPresenter.requestCurrentSequence(mLotteryType);
    }

    private void initView() {
        mHandler = new MyHandler(this);
        mDown2UpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_up_to_down);
        mUp2DownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_down_to_up);
        mDown2UpAnimation.setAnimationListener(this);
        mUp2DownAnimation.setAnimationListener(this);
        ImageButton moreIb = (ImageButton) findViewById(R.id.title_right_button);
        moreIb.setImageResource(R.drawable.btn_more);
        mSelectStyleBtns = new TextView[]{fast3TvSum, fast3Tv3sameSingle,
                fast3Tv3sameSingle, fast3Tv3sameSingle, fast3Tv2sameMore,
                fast3Tv2sameMore, fast3Tv3different, fast3Tv2different};
    }

    private void showFragmentByPlayStyle() {
        switch (mPlayStyle) {
            case FAST_THREE_SUM:
                fast3TvSum.performClick();
                break;
            case FAST_THREE_3_DIFFERENT:
                fast3Tv3different.performClick();
                break;
            case FAST_THREE_3_SAME_ALL:
                fast3Tv3sameSingle.performClick();
                break;
            case FAST_THREE_3_SAME_ONE:
                fast3Tv3sameSingle.performClick();
                break;
            case FAST_THREE_2_DIFFERENT:
                fast3Tv2different.performClick();
                break;
            case FAST_THREE_2_SAME_MORE:
                fast3Tv2sameMore.performClick();
                break;
            case FAST_THREE_2_SAME_ONE:
                fast3Tv2sameMore.performClick();
                break;
            case FAST_THREE_3_TO_ALL:
                fast3Tv3sameSingle.performClick();
                break;
        }
    }

    private void initIntent() {
        mPlayStyle = getIntent().getIntExtra(PARAM_LOTTERY_PLAY_TYPE, FAST_THREE_SUM);
        mLotteryType = getIntent().getStringExtra(PARAM_LOTTERY_TYPE);
        mLotteryName = (String) SharedPreferenceUtil.get(mContext, mLotteryType, "");
    }

    public void onPlayStyleSelect(View v) {
        if (mIsAnimRunning) {
            return;
        }
        showOrHideSelectLayout();
        int id = v.getId();
        String title = "";
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        switch (id) {
            case R.id.fast3_tv_sum:
                if (mSumFragment == null) {
                    mSumFragment = SumFragment.newInstance(mLotteryType);
                    transaction.add(R.id.fragment_container, mSumFragment, FRAGMENT_TAG_SUM);
                }
                transaction.show(mSumFragment);
                mPlayStyle = FAST_THREE_SUM;
                title = mLotteryName + "-和值";
                break;
            case R.id.fast3_tv_2different:
                if (mTowDifferentFragment == null) {
                    mTowDifferentFragment = TowDifferentFragment.newInstance(mLotteryType);
                    transaction.add(R.id.fragment_container, mTowDifferentFragment, FRAGMENT_TAG_TOW_DIFFERENT);
                }
                transaction.show(mTowDifferentFragment);
                mPlayStyle = FAST_THREE_2_DIFFERENT;
                title = mLotteryName + "-二不同号";
                break;
            case R.id.fast3_tv_2same_more:
                if (mTowSameFragment == null) {
                    mTowSameFragment = TowSameFragment.newInstance(mLotteryType);
                    transaction.add(R.id.fragment_container, mTowSameFragment, FRAGMENT_TAG_TOW_SAME);
                }
                transaction.show(mTowSameFragment);
                mPlayStyle = FAST_THREE_2_SAME_MORE;
                title = mLotteryName + "-二同号";
                break;
//            case R.id.fast3_tv_2same_single:
//                if (mTowSameSingleFragment == null) {
//                    mTowSameSingleFragment = TowSameSingleFragment.newInstance(mLotteryType);
//                    transaction.add(R.id.fragment_container, mTowSameSingleFragment, FRAGMENT_TAG_TOW_SAME_SINGLE);
//                }
//                transaction.show(mTowSameSingleFragment);
//                mPlayStyle = FAST_THREE_2_SAME_ONE;
//                title = mLotteryName + "-二同号单选";
//                break;
            case R.id.fast3_tv_3different:
                if (mThreeDifferentFragment == null) {
                    mThreeDifferentFragment = ThreeDifferentFragment.newInstance(mLotteryType);
                    transaction.add(R.id.fragment_container, mThreeDifferentFragment, FRAGMENT_TAG_THREE_DIFFERENT);
                }
                transaction.show(mThreeDifferentFragment);
                mPlayStyle = FAST_THREE_3_DIFFERENT;
                title = mLotteryName + "-三不同号";
                break;
//            case R.id.fast3_tv_3same_all:
//                if (mThreeSameAllFragment == null) {
//                    mThreeSameAllFragment = ThreeSameAllFragment.newInstance(mLotteryType);
//                    transaction.add(R.id.fragment_container, mThreeSameAllFragment, FRAGMENT_TAG_THREE_SAME_ALL);
//                }
//                transaction.show(mThreeSameAllFragment);
//                mPlayStyle = FAST_THREE_3_SAME_ALL;
//                title = mLotteryName + "-三同号通选";
//                break;
            case R.id.fast3_tv_3same_single:
                if (mThreeSameFragment == null) {
                    mThreeSameFragment = ThreeSameFragment.newInstance(mLotteryType);
                    transaction.add(R.id.fragment_container, mThreeSameFragment, FRAGMENT_TAG_THREE_SAME);
                }
                transaction.show(mThreeSameFragment);
                mPlayStyle = FAST_THREE_3_SAME_ONE;
                title = mLotteryName + "-三同号";
                break;
//            case R.id.fast3_tv_3to_all:
//                if (mThreeToAllFragment == null) {
//                    mThreeToAllFragment = ThreeToAllFragment.newInstance(mLotteryType);
//                    transaction.add(R.id.fragment_container, mThreeToAllFragment, FRAGMENT_TAG_THREE_TO_ALL);
//                }
//                transaction.show(mThreeToAllFragment);
//                mPlayStyle = FAST_THREE_3_TO_ALL;
//                title = mLotteryName + "-三连号通选";
//                break;
        }
        mTitleTv.setText(title);
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

    @OnClick({R.id.title_back, R.id.fast3_ll_changeStyle, R.id.title_right_button,
            R.id.ll_history, R.id.fast3_tv_sum, R.id.fast3_tv_3same_single, R.id.fast3_tv_2same_more,
            R.id.fast3_tv_3different, R.id.fast3_tv_2different})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(FastThreeActivity.this);
                break;
            case R.id.fast3_ll_changeStyle:
                showOrHideSelectLayout();
                break;
            case R.id.title_right_button:
//                UIHelper.gotoActivity(FastThreeActivity.this, FastThreeDescriptionActivity.class, false);
                //弹出选项弹窗
                customPopupWindow.showPopupWindow(view);
                break;
            case R.id.ll_history:
                TrendActivity.intoThisActivity(mContext, mLotteryType, mPlayStyle, LATEST_OPEN_AWARD);
                break;
            case R.id.fast3_tv_sum:
                onPlayStyleSelect(view);
                break;
            case R.id.fast3_tv_3same_single:
                onPlayStyleSelect(view);
                break;
            case R.id.fast3_tv_2same_more:
                onPlayStyleSelect(view);
                break;
            case R.id.fast3_tv_3different:
                onPlayStyleSelect(view);
                break;
            case R.id.fast3_tv_2different:
                onPlayStyleSelect(view);
                break;
        }
    }

    private void showOrHideSelectLayout() {
        if (mIsAnimRunning) {
            return;
        }
        UIHelper.showWidget(mSelectStyleLayout, !mIsSelectStyleLayoutShow);
        if (!mIsSelectStyleLayoutShow) {
            if (mPlayStyle == FAST_THREE_SUM) {
                showSelectButton(0);
            } else if (mPlayStyle == FAST_THREE_2_DIFFERENT) {
                showSelectButton(7);
            } else if (mPlayStyle == FAST_THREE_3_DIFFERENT) {
                showSelectButton(6);
            } else if (mPlayStyle == FAST_THREE_2_SAME_ONE) {
                showSelectButton(4);
            } else if (mPlayStyle == FAST_THREE_2_SAME_MORE) {
                showSelectButton(5);
            } else if (mPlayStyle == FAST_THREE_3_TO_ALL) {
                showSelectButton(3);
            } else if (mPlayStyle == FAST_THREE_3_SAME_ONE) {
                showSelectButton(1);
            } else if (mPlayStyle == FAST_THREE_3_SAME_ALL) {
                showSelectButton(2);
            }
        }
        if (!mIsFistInit) {
            if (mIsSelectStyleLayoutShow) {
                if (mStyleLabelIv != null) {
                    mStyleLabelIv.startAnimation(mUp2DownAnimation);
                }
            } else {
                mStyleLabelIv.startAnimation(mDown2UpAnimation);
            }
        }
        mIsFistInit = false;
        mIsSelectStyleLayoutShow = !mIsSelectStyleLayoutShow;
    }

    private void showSelectButton(int idx) {
        for (int i = 0; i < mSelectStyleBtns.length; i++) {
            if (idx != i) {
                mSelectStyleBtns[i].setSelected(false);
                mSelectStyleBtns[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_btn_round_sum_fragment_select_style));
                mSelectStyleBtns[i].setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_tab_text));
            } else {
                mSelectStyleBtns[i].setSelected(true);
                mSelectStyleBtns[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_bg_btn_round_normal));
                mSelectStyleBtns[i].setTextColor(StaticResourceUtils.getColorResourceById(R.color.white_normal));
            }
        }
    }

    /**
     * 进入此activity的入口
     *
     * @param fromActivity
     * @param playStyle
     */
    public static void intoThisActivity(Context fromActivity, String lotteryType, int playStyle) {
        Intent i = new Intent(fromActivity, FastThreeActivity.class);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
        i.putExtra(PARAM_LOTTERY_PLAY_TYPE, playStyle);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_UPDATE_COUNT_DOWN);
    }

    @Override
    public void showTipDialog(String msgStr) {

    }

    @Override
    public void hideTipDialog() {

    }

    @Override
    public void requestResult(boolean isOk, Object object) {

    }

    @Override
    public void requestCurrentSequenceResult(boolean isOk, Object obj) {
        if (isOk && mHandler != null) {
            Message msg = new Message();
            msg.what = MSG_REQUEST_CURRENT_SEQUENCE_OK;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        } else {
            mHandler.sendEmptyMessage(MSG_REQUEST_CURRENT_SEQUENCE_FAIL);
        }
    }

    @OnClick(R.id.empty_view)
    public void onViewClicked() {
        showOrHideSelectLayout();
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

    //是否允许购买
    public boolean getOrderState(){
        boolean state = false;
        LotteryPageDataResponseBean lotteryBean = SharedPreferencesUtils.getLotteryPageDataInfo();
        List<LotteryType> lotteryTypes = lotteryBean.getPayload().getLotteries();
        for (int i = 0; i < lotteryTypes.size(); i++) {
            LotteryType typeBean = lotteryTypes.get(i);
            if(TextUtils.equals(mLotteryType, typeBean.getName())){
                if(TextUtils.equals("2",typeBean.getLottery_state())){
                    state = true; //销售
                }else if(TextUtils.equals("1",typeBean.getLottery_state())){
                    state = false; //暂停销售
                }
            }
        }
        return state;
    }

}
