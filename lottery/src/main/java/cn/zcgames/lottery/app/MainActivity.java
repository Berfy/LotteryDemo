package cn.zcgames.lottery.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.qiaoxg.dialoglibrary.AlertDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.model.DetailMessage;
import cn.berfy.sdk.mvpbase.model.NotifyMessage;
import cn.berfy.sdk.mvpbase.model.NotifyType;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.NotificationUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.event.NetworkChangedEvent;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.presenter.PayPresenter;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.presenter.MinePresenter;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.activity.LotteryOrderDetailActivity;
import cn.zcgames.lottery.personal.view.activity.PhaseDetailActivity;
import cn.zcgames.lottery.home.view.fragment.LotteryFragment;
import cn.zcgames.lottery.personal.view.fragment.MineFragment;
import cn.zcgames.lottery.message.view.fragment.ResultFragment;
import cn.zcgames.lottery.utils.FileUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 首页
 * Berfy修改
 * 2018.8.23
 */
public class MainActivity extends BaseActivity implements IBaseView {

    @BindView(R.id.main_btn_lottery)
    Button mLotteryBtn;
    @BindView(R.id.main_btn_result)
    Button mResultBtn;
    @BindView(R.id.main_btn_mine)
    Button mMineBtn;

    public final int FRAGMENT_N = 3;
    public static final String TAG = "MainActivity";
    private final String FRAGMENT_TAG_LOTTERY = "lottery";
    private final String FRAGMENT_TAG_MINE = "mine";
    private final String FRAGMENT_TAG_RESULT = "result";
    public static final String KEY_MESSAGE = "msg";
    public static final String KEY_EXTRAS = "exit";
    public static final String KEY_IS_HOME = "is_home";
    public static final String MESSAGE_RECEIVED_ACTION = "action";

    private final String[] fragmentTags = new String[]{FRAGMENT_TAG_LOTTERY, FRAGMENT_TAG_RESULT,
            FRAGMENT_TAG_MINE};
    public final int[] tabsNormalBackIds = new int[]{R.drawable.tabbar_lottery,
            R.drawable.tabbar_result, R.drawable.tabbar_me};
    public final int[] tabsActiveBackIds = new int[]{R.drawable.tabbar_lottery_active,
            R.drawable.tabbar_result_active, R.drawable.tabbar_me_active};

    private int mLastViewId;
    private LotteryFragment mLotteryFragment;
    private ResultFragment mResultFragment;
    private MineFragment mMineFragment;
    private Button[] tabs;
    public static final String INTENT_EXTRA_DATA = "data";//跳转的数据
    private MinePresenter mMinePresenter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPresenter();
        mLotteryBtn.performClick();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission) {
                        FileUtils.createFile(AppConstants.LOCAL_PATH, false);
                        FileUtils.createFile(AppConstants.LOCAL_PATH + "/log/", false);
                    }
                });

        if (MyApplication.isPermissionShow) {
            setPermission(rxPermissions);
        }
    }

    private void initPresenter() {
        mMinePresenter = new MinePresenter(this, this);
    }

    private void setPermission(RxPermissions rxPermissions) {
        //设置获取设备标识权限(用于推送)
        setPhoneStatePermission(rxPermissions);
        //设置手机通知权限
        NotificationUtil notifyUtil = NotificationUtil.getInstance();
        boolean notificationEnabled = notifyUtil.isNotificationEnabled(mContext);
        if (!notificationEnabled) notifyUtil.getNotifyPermission(MainActivity.this);
        MyApplication.isPermissionShow = false;
    }

    @SuppressLint("CheckResult")
    private void setPhoneStatePermission(RxPermissions rxPermissions) {
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> {
                    if (permission) {
                        MyApplication.isPhoneState = true;
                        UserBean user = MyApplication.getCurrLoginUser();
                        if (user != null && !TextUtils.isEmpty(user.getPlayerId())) {
                            //注册MQTT
                            MyApplication.getInstance().initMQTT(user.getPlayerId());
                        }
                    } else {
                        AlertDialog confirmDialog = new AlertDialog(MainActivity.this)
                                .builder()
                                .setCancelable(false)
                                .setTitle(getString(R.string.tips_tip))
                                .setMsg("无法获取您的手机设备信息，您将无法使用“开奖推送”功能，是否获取?")
                                .setNegativeButton("取消", v -> MyApplication.isPhoneState = false)
                                .setPositiveButton("确认", v -> setPhoneStatePermission(rxPermissions));
                        confirmDialog.show();
                    }
                });
    }

    //立即投注的跳转事件
    public static void intoThisActivity(Activity fromActivity, boolean isHome) {
        LogF.d(TAG, "isHome==>" + isHome);
        Intent intent = new Intent(fromActivity, MainActivity.class);
        intent.putExtra(KEY_IS_HOME, isHome);
        if (isHome) intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        boolean isHome = intent.getBooleanExtra(KEY_IS_HOME, false);
        LogF.d(TAG, "isHome==>" + isHome);
        if (isHome) mLotteryBtn.performClick();//跳转到首页
        receiveNotify(intent);//接收到消息
    }

    private void receiveNotify(Intent intent) {
        LogF.d(MyApplication.NOTIFY_TAG, "跳转" + intent.getSerializableExtra(INTENT_EXTRA_DATA));
        if (intent.hasExtra(INTENT_EXTRA_DATA)) {
            NotifyMessage notifyMessage = (NotifyMessage) intent.getSerializableExtra(INTENT_EXTRA_DATA);
            if (null == notifyMessage) return;
            handlePushMessage(notifyMessage);
        }
    }

    private void handlePushMessage(NotifyMessage notifyMessage) {
        switch (notifyMessage.getType()) {
            case NotifyType.TYPE_3://中奖通知
                DetailMessage body = notifyMessage.getBody();
                if (body != null) {
                    //检查是否登录
                    if (!MyApplication.isLogin()) {
                        showGotoLogin();
                    } else {
                        String orderId = body.getOrderId();
                        if (!TextUtils.isEmpty(orderId)) {
                            int chase = Integer.valueOf(body.getChase());
                            LogF.d(MyApplication.NOTIFY_TAG, "chase==>" + chase + " ,orderId==>" + orderId);
                            if (chase > 1) {
                                PhaseDetailActivity.intoThisActivity(MainActivity.this,
                                        orderId, body.getLotteryName());//追期管理
                            } else {
                                LotteryOrderDetailActivity.inToThisActivity(MainActivity.this,
                                        orderId, body.getLotteryName());//订单详情
                            }
                        }
                    }
                }
                break;
        }
    }

    //提示登录对话框
    private void showGotoLogin() {
        UIHelper.showConfirmDialog(this,
                R.string.tips_win_message,
                isOk -> {
                    if (isOk)
                        UIHelper.gotoActivity(this, LoginActivity.class, false);
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkChangedEvent event) {

        if (event.getNetworkType() == AppConstants.NETWORK_TYPE_MOBILE) {
            UIHelper.showToast("手机网络");
        } else if (event.getNetworkType() == AppConstants.NETWORK_TYPE_NONE) {
            UIHelper.showToast("没有网络");
        } else if (event.getNetworkType() == AppConstants.NETWORK_TYPE_WIFI) {
            UIHelper.showToast("wifi网络");
        }

    }

    private void initView() {
        setButterKnife(this);
        EventBus.getDefault().register(this);
        tabs = new Button[]{mLotteryBtn, mResultBtn, mMineBtn};
    }

    /**
     * 处理tabar按钮的点击事件，切换fragment
     *
     * @param v
     */
    public void onTabSelect(View v) {
        int id = v.getId();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        setNormalBackgrounds();
        if (id == R.id.main_btn_lottery) {
            if (mLotteryFragment == null) {
                mLotteryFragment = new LotteryFragment();
                transaction.add(R.id.main_rl_fragment_container, mLotteryFragment, FRAGMENT_TAG_LOTTERY);
            }
            transaction.show(mLotteryFragment);
            if (id != mLastViewId) {
                mLotteryFragment.requestData();
            }
        } else if (id == R.id.main_btn_mine) {
            if (mMineFragment == null) {
                mMineFragment = new MineFragment();
                transaction.add(R.id.main_rl_fragment_container, mMineFragment, FRAGMENT_TAG_MINE);
            }
            transaction.show(mMineFragment);
            mMineFragment.requestMineData();
        } else if (id == R.id.main_btn_result) {
            if (mResultFragment == null) {
                mResultFragment = new ResultFragment();
                transaction.add(R.id.main_rl_fragment_container, mResultFragment, FRAGMENT_TAG_RESULT);
            }
            transaction.show(mResultFragment);
        }
        int pos;
        for (pos = 0; pos < FRAGMENT_N; pos++) {
            if (tabs[pos] == v) {
                break;
            }
        }
        transaction.commit();
        setTopDrawable(tabs[pos], tabsActiveBackIds[pos]);
        mLastViewId = id;
    }

    private void setNormalBackgrounds() {
        for (int i = 0; i < tabs.length; i++) {
            Button v = tabs[i];
            setTopDrawable(v, tabsNormalBackIds[i]);
            v.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_tab_text));
        }
    }

    private void setTopDrawable(Button v, int resId) {
        v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(resId), null, null);
        v.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_main_light));
    }

    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showExitAppConfirmDialog(MainActivity.this);
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMinePresenter.requestMineData();
        //        MQTTManager.getInstance().connect(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void requestResult(boolean isOk, Object object) {
        if (isOk) Log.d(TAG, "用户数据请求成功");
    }

    @Override
    public void showTipDialog(String msgStr) {
    }

    @Override
    public void hideTipDialog() {
    }
}
