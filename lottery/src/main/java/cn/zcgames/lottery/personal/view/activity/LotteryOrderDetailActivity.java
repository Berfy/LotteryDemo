package cn.zcgames.lottery.personal.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qiaoxg.dialoglibrary.ShareDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.zcgames.lottery.R;

import static cn.zcgames.lottery.app.ActivityConstants.*;

import static cn.zcgames.lottery.app.AppConstants.*;

import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.LotteryOrderDetail;
import cn.zcgames.lottery.bean.PeriodBean;
import cn.zcgames.lottery.bean.StakesBean;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.personal.presenter.OrderDetailPresenter;
import cn.zcgames.lottery.personal.view.adapter.LotteryDetailAdapter;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.activity.AlwaysColorActivity;
import cn.zcgames.lottery.home.lottery.elevenfive.view.activity.Eleven5Activity;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;
import cn.zcgames.lottery.utils.PermissionUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;


/**
 * 投注详情列表
 *
 * @author NorthStar
 * @date 2018/8/20 17:43
 */
public class LotteryOrderDetailActivity extends BaseActivity implements IBaseView, UMShareListener {

    private static final String TAG = "FastThreeOrderDetailActivity";

    public static final int MSG_UPDATE_DATA = 0;
    public static final int MSG_NO_DATA = 1;

    @BindView(R.id.detail_tv_lotteryTime)
    TextView mLotteryTimeTv;
    @BindView(R.id.detail_tv_orderId)
    TextView mOrderIdTv;
    @BindView(R.id.orderDetail_tv_lotteryStatus)
    TextView mLotteryStatusTv;
    @BindView(R.id.orderDetail_tv_orderStatus)
    TextView mOrderStatusTv;
    @BindView(R.id.orderDetail_tv_period)
    TextView mSequenceTv;
    @BindView(R.id.detail_tv_cost)
    TextView mCostTv;
    @BindView(R.id.detail_tv_createTime)
    TextView mCreateTimeTv;
    @BindView(R.id.detail_tv_multiple)
    TextView mMultipleTv;
    @BindView(R.id.orderDetail_tv_rewardMoney)
    TextView mRewardMoneyTv;
    @BindView(R.id.orderDetail_tv_win)
    TextView mWinTv;
    @BindView(R.id.orderDetail_tv_title)
    TextView mLotteryTitle;
    @BindView(R.id.listView)
    RecyclerView mListView;
    @BindView(R.id.title_right_button)
    ImageButton ib;
    @BindView(R.id.parent_ll_layout)
    View parentView;


    private String mOrderId;//订单id
    private String mLotteryName;//彩票类型
    private LotteryOrderDetail mLotteryOrderDetail;
    private List<StakesBean> stakesBeans = new ArrayList<>();

    private OrderDetailPresenter mPresenter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_DATA:
                    updateUI(msg);
                    break;
                case MSG_NO_DATA:
                    showRequestAgainDialog();
                    break;
            }
        }
    };

    private void updateUI(Message msg) {
        if (msg.obj != null && msg.obj instanceof LotteryOrderDetail) {
            showData((LotteryOrderDetail) msg.obj);
        } else {
            hideTipDialog();
            LogF.d(TAG, "handleMessage: msg.obj is null or instanceof error !");
        }
    }

    private void showRequestAgainDialog() {
        UIHelper.showConfirmDialog(this, "请求失败，重新请求吗?", isOk -> {
            if (isOk) {
                getData();
            } else {
                goBack(LotteryOrderDetailActivity.this);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
        initIntentData();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new OrderDetailPresenter(this, this);
        getData();
    }

    private void getData() {
        if (TextUtils.isEmpty(mLotteryName)) {
            LogF.d(TAG, "没有匹配的彩票名称");
            return;
        }
        if (!TextUtils.isEmpty(mOrderId)) {
            mPresenter.requestOrderDetail(mLotteryName, mOrderId);
        }
    }

    private void initIntentData() {
        Intent i = getIntent();
        mOrderId = i.getStringExtra(PARAM_ID);
        mLotteryName = i.getStringExtra(PARAM_LOTTERY_TYPE);
    }

    private void initView() {
        setContentView(R.layout.activity_order_detail);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_order_detail);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
        UIHelper.showWidget(ib, false);
        ib.setImageResource(R.drawable.btn_share);
        //防止scrollView自动滑动到底部
        parentView.setFocusable(true);
        parentView.setFocusableInTouchMode(true);
        parentView.requestFocus();
        mListView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showData(LotteryOrderDetail bean) {
        if (bean == null) return;
        mLotteryOrderDetail = bean;
        //订单支付状态 1.未支付 其他:已支付
        int status = bean.getStatus();
        if (status == 1) {
            mOrderStatusTv.setText(CommonUtil.getString(this, R.string.not_pay));
        } else {
            mOrderStatusTv.setText(CommonUtil.getString(this, R.string.has_pay));
        }
        List<PeriodBean> period = bean.getPeriod();
        List<StakesBean> stakes = bean.getStakes();
        if (period != null) {
            PeriodBean currentPeriod = period.get(0);
            //开奖时间
            long dateTime = (long) currentPeriod.getDrawTs();
            if (dateTime != 0) {
                mLotteryTimeTv.setText(TimeUtil.format("yyyy-MM-dd HH:mm", dateTime));
            } else {
                mLotteryTimeTv.setText("—— ——");
            }
            mOrderIdTv.setText(mOrderId);

            //设置中奖状态: 1: 输； 2: 赢； 3: 未开奖
            String lotteryState = "";
            int winStatus = currentPeriod.getStatus();
            switch (winStatus) {
                case 1:
                    lotteryState = getString(R.string.lose);
                    break;

                case 2:
                    lotteryState = getString(R.string.win);
                    break;

                case 3:
                    lotteryState = getString(R.string.wait);
                    break;
            }
            mLotteryStatusTv.setText(lotteryState);
            List<String> winStakesIndex = currentPeriod.getWinStakesIndex();
            if (winStakesIndex != null && winStakesIndex.size() > 0) {
                for (String indexStr : winStakesIndex) {
                    stakes.get(Integer.parseInt(indexStr)).setStatus(2);
                }
            }
            StringBuilder winNumbersBuilder = new StringBuilder();
            List<String> winNumbers = currentPeriod.getWinNumbers();
            if (winNumbers != null && winNumbers.size() > 0) {
                for (String num : winNumbers) {
                    winNumbersBuilder.append(num);
                    winNumbersBuilder.append(" ");
                }
                mWinTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_red_ball));
                mWinTv.setText(winNumbersBuilder.toString());
            } else {
                mWinTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
                mWinTv.setText("--");
            }

            //税后奖金
            String reward = String.format("%s元", StringUtils.getCash(bean.getTotal_rewards(), DIGITS));
            mRewardMoneyTv.setText(winStatus == 3 ? "--" : reward);
        }
        stakesBeans.clear();
        stakesBeans.addAll(stakes);

        //购票金额
        mCostTv.setText(String.format("%s元", StringUtils.getCash(bean.getCost(), DIGITS)));

        mCreateTimeTv.setText(TimeUtil.format("yyyy-MM-dd HH:mm", bean.getCreated()));
        mMultipleTv.setText(String.format(Locale.CHINA, "%s倍%s注", bean.getTimes(), bean.getStakesCount()));
        String mSequence = bean.getPeriod_start();
        if (!TextUtils.isEmpty(mSequence))

        {
            mSequenceTv.setText(String.format("第%s期", mSequence));
        }

        setLotteryTitle();

        LotteryDetailAdapter adapter = new LotteryDetailAdapter(stakesBeans, this, mLotteryName);
        mListView.setAdapter(adapter);

        hideTipDialog();

    }

    private void setLotteryTitle() {
        //彩种logo
        //Picasso.with(this).load(HTTP + bill.getImg_url()).into(mLotteryHeader);
        String title = (String) SharedPreferenceUtil.get(mContext, mLotteryName, "");
        mLotteryTitle.setText(title);
    }

    @OnClick({R.id.title_back, R.id.orderDetail_tv_goOnBuy, R.id.orderDetail_tv_buyThisOrder, R.id.title_right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(LotteryOrderDetailActivity.this);
                break;
            case R.id.orderDetail_tv_goOnBuy://继续购买下注
                switch (mLotteryName) {
                    case LOTTERY_TYPE_FAST_3:
                    case LOTTERY_TYPE_FAST_3_JS:
                    case LOTTERY_TYPE_FAST_3_HB:
                    case LOTTERY_TYPE_FAST_3_EASY:
                    case LOTTERY_TYPE_FAST_3_NEW:
                        FastThreeActivity.intoThisActivity(LotteryOrderDetailActivity.this,
                                mLotteryName, FAST_THREE_SUM);//快3
                        break;
                    case LOTTERY_TYPE_11_5:
                    case LOTTERY_TYPE_11_5_OLD:
                    case LOTTERY_TYPE_11_5_LUCKY:
                    case LOTTERY_TYPE_11_5_YUE:
                    case LOTTERY_TYPE_11_5_YILE:
                        Eleven5Activity.intoThisActivity(LotteryOrderDetailActivity.this,
                                mLotteryName, PLAY_11_5_ANY_2);//11选5
                        break;
                    case LOTTERY_TYPE_ALWAYS_COLOR:
                    case LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                        AlwaysColorActivity.intoThisActivity(LotteryOrderDetailActivity.this,
                                mLotteryName, ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);//时时彩
                        break;
                }
                LotteryOrderDetailActivity.this.finish();
                break;
            case R.id.orderDetail_tv_buyThisOrder://继续购买本单号码
                if (stakesBeans == null) return;
                //再来一单
                showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading_order_again_buy));
                Constant.EXECUTOR.execute(() -> {
                    LotteryUtils.againCreateOrder(mLotteryName, mLotteryOrderDetail, stakesBeans);
                    hideTipDialog();
                    runOnUiThread(() -> {
                        finish();
                        LotteryOrderActivity.intoThisActivity(LotteryOrderDetailActivity.this, mLotteryName, -1);
                    });
                });
                break;
            case R.id.title_right_button:
                writeExternalStorage();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void writeExternalStorage() {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = requestCode -> {
        switch (requestCode) {
            case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                break;
            case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                ShareDialog dialog = new ShareDialog(LotteryOrderDetailActivity.this)
                        .builder()
                        .setFriendClickListener(v -> ShareOrderDetail(SHARE_MEDIA.WEIXIN_CIRCLE))
                        .setQQClickListener(v -> ShareOrderDetail(SHARE_MEDIA.QQ))
                        .setWeixinClickListener(v -> ShareOrderDetail(SHARE_MEDIA.WEIXIN));
                dialog.show();
                break;
            default:
                break;
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private void ShareOrderDetail(SHARE_MEDIA platform) {
        UMImage thumb = new UMImage(LotteryOrderDetailActivity.this, R.drawable.app_logo);
        UMWeb web = new UMWeb("http://www.123test.com");
        web.setThumb(thumb);
        web.setDescription("分享测试");
        web.setTitle("这是标题");
        new ShareAction(LotteryOrderDetailActivity.this).withMedia(web).setPlatform(platform).setCallback(this).share();
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        UIHelper.showToast("分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        UIHelper.showToast("分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        UIHelper.showToast("分享取消");
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        Message msg = new Message();
        if (isOk) {
            msg.what = MSG_UPDATE_DATA;
        } else {
            msg.what = MSG_NO_DATA;
            hideTipDialog();
        }
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

    @Override
    public void showTipDialog(String msgStr) {
        showWaitingDialog(this, msgStr, false);
    }

    @Override
    public void hideTipDialog() {
        hideWaitingDialog();
    }


    /**
     * 其他activity进入此activity的入口
     *
     * @param fromActivity 发送请求的activity
     * @param orderId      订单号
     * @param lotteryName  彩种类型
     */
    public static void inToThisActivity(Activity fromActivity,
                                        String orderId,
                                        String lotteryName) {

        if (TextUtils.isEmpty(orderId)) {
            LogF.e(TAG, "orderId no find");
            return;
        }
        Intent i = new Intent(fromActivity, LotteryOrderDetailActivity.class);
        i.putExtra(PARAM_ID, orderId);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryName);
        fromActivity.startActivity(i);
    }
}
