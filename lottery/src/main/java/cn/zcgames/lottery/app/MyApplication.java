package cn.zcgames.lottery.app;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.berfy.sdk.mvpbase.config.CacheConstant;
import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.model.NotifyMessage;
import cn.berfy.sdk.mvpbase.model.NotifyType;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.CrashException;
import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.berfy.sdk.mvpbase.util.HostSettings;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.NotificationUtil;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.SystemUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.berfy.service.mqtt.MQTTManager;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.home.bean.LotteryPageDataResponseBean;
import cn.zcgames.lottery.personal.model.NotifyToggleInfo;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.model.local.SharedPreferencesUtils;
import cn.zcgames.lottery.utils.SystemUtils;

/**
 * 用于维护全局状态及配置
 */
public class MyApplication extends MultiDexApplication {

    private static final String TAG = "MyApplication";
    public static final String NOTIFY_TAG = "MQTT连接管理";

    //全局的上下文
    private static MyApplication instance;
    //当前设备的屏幕信息：分辨率，密度
    private static DisplayMetrics mDisplayMetrics;
    //当前网络是否可用
    private static boolean mNetworkIsOk;
    //控制通知及获取手机状态权限的开启
    public static boolean isPermissionShow = true;
    //获取手机状态权限的开启
    public static boolean isPhoneState = false;
    //屏幕宽度
    public static int SCREEN_WIDTH = -1;
    //屏幕高度


    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;
    private static List<String> topics = new ArrayList<>();

    private String msgId = "";//全局推送消息id,重复msgId不做处理

    @Override
    public void onCreate() {
        super.onCreate();
        Constant.APP_START_TIME = System.currentTimeMillis();
        instance = this;
        //设置debug模式
        Config.DEBUG = Constant.DEBUG;
        CacheConstant.XML_FILENAME = AppConstants.SPDATA_KEY_USER;
        CacheConstant.XML_FILENAME_PUBLIC = AppConstants.SPDATA_KEY_PUBLIC;
        CacheConstant.DIR_PUBLIC_ROOT = AppConstants.LOCAL_PATH;

        //全局Toast
        ToastUtil.init(getApplicationContext());

        //获取屏幕尺寸
        getScreenSize();

        //全局通知初始化
        NotificationUtil.init(getAppContext(), MainActivity.class);

        // 异常处理，不需要处理时注释掉这句即可！
        CrashException.getInstance().init(getAppContext(), SplashActivity.class);
    }

    /**
     * @return apk 打包时写入的渠道信息
     */
    public String getApkChannelInfo() {
        String channel = "";
//        if (Constant.DEBUG) {
//            return channel;
//        }
        ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(this.getApplicationContext());
        if (null != channelInfo) {
            channel = channelInfo.getChannel();
        }
        return channel;
    }

    /**
     * @return apk 打包时写入的客服信息，注册时使用
     */
    public String getApkKeFuInfo() {
//        if (Constant.DEBUG) {
//            return "";
//        }
        String value = WalleChannelReader.get(this, "kefu");
        Log.i(TAG, "getChannelInfo: -kefu" + value);
        return value;
    }


    public static MyApplication getInstance() {
        return instance;
    }

    //初始化Mqtt
    public void initMQTT(String userId) {
        LogF.d(TAG, "isPermissionShow==>" + isPhoneState);
        MQTTManager.init(getAppContext());
        setMessageListener();
        MQTTManager.getInstance().setHost(HostSettings.getMQTTHost());
        String deviceId = DeviceUtils.getDeviceId(getAppContext());
        String clientId = deviceId + userId;
        LogF.d(TAG, "clientId==>" + clientId);
        MQTTManager.getInstance().setClientId(clientId);
        MQTTManager.getInstance().setUserName("root");//zhuxin  zc-DB1234   root zcxy1234
        MQTTManager.getInstance().setPassWord("zcxy1234");
        MQTTManager.getInstance().connect(false);
        setNotifyTopic();
    }

    //注册消息推送监听
    public void setMessageListener() {
        MQTTManager.getInstance().onMessageArrivedListener((topic, notifyMessage) -> {
            if (notifyMessage != null) {
                doNotify(notifyMessage);
            }
        });
    }

    //设置推送订阅话题
    public void setNotifyTopic() {
        if (MQTTManager.getInstance() != null) {
            UserBean user = getCurrLoginUser();
            if (user != null && !TextUtils.isEmpty(user.getPlayerId())) {
                LogF.d("SharedPreferencesUtils", "订阅时的userId==>" + user.getPlayerId());
                subscribeSystemTopic();//默认订阅系统通知
                String userId = user.getPlayerId();
                LinkedHashMap<String, NotifyToggleInfo> mTogglesMap = SharedPreferenceUtil.getHashMapData(
                        getAppContext(), AppConstants.NOTIFY_OPTION, NotifyToggleInfo.class);

                NotifyToggleInfo notifyToggleInfo = null;
                if (mTogglesMap != null) notifyToggleInfo = mTogglesMap.get(userId);
                if (notifyToggleInfo != null && notifyToggleInfo.getToggles() != null) {
                    boolean openWin = notifyToggleInfo.isOpenWin();
                    if (openWin && !TextUtils.isEmpty(userId)) subscribeWinTopic(userId);//中奖通知
                    List<LotteryType> typeList = notifyToggleInfo.getToggles();
                    if (typeList != null && typeList.size() > 0) {
                        for (LotteryType lotteryType : typeList) {
                            if (lotteryType.isChecked()) {
                                subscribeLotteryTopic(lotteryType);//开奖通知
                            }
                        }
                        LogF.d(NOTIFY_TAG, "lotterySize==>" + typeList.size());
                    }
                } else {//未设置的状态下,默认订阅中奖通知
                    subscribeWinTopic(userId);
                }
                setSubscribeTopic();//订阅话题推送
            }
        }
    }


    //注销所有订阅话题
    public void unSubscribeAllTopic() {
        if (MQTTManager.getInstance() != null) {
            //注销中奖通知
            UserBean user = MyApplication.getCurrLoginUser();
            if (user != null && !TextUtils.isEmpty(user.getPlayerId())) {
                String winTopic = String.format(Locale.CHINA, "%s%s/%s",
                        AppConstants.NOTIFY_WIN_TYPE, AppConstants.getChannelId(), user.getPlayerId());
                LogF.d("MQTT连接管理", "退出登录时的userId==>" + user.getPlayerId());
                MQTTManager.getInstance().unSubscribeTopic(winTopic);//订阅中奖话题推送
            }

            //注销系统通知
            String publicTopic = String.format(Locale.CHINA, "%s%s",
                    AppConstants.NOTIFY_SYSTEM_TYPE, AppConstants.getChannelId());
            MQTTManager.getInstance().unSubscribeTopic(publicTopic);

            //注销各彩种开奖通知
            LotteryPageDataResponseBean bean = SharedPreferencesUtils.getLotteryPageDataInfo();
            if (null != bean && null != bean.getPayload()) {
                List<LotteryType> lotteries = SharedPreferencesUtils.getLotteryPageDataInfo().getPayload().getLotteries();
                if (lotteries != null && lotteries.size() > 0) {
                    for (LotteryType lotteryType : lotteries) {
                        String lotteryTopic = String.format(Locale.CHINA, "%s%s",
                                AppConstants.NOTIFY_LOTTERY_TYPE, lotteryType.getName());
                        MQTTManager.getInstance().unSubscribeTopic(lotteryTopic);
                    }
                }
            }
            topics.clear();
        }
    }

    //订阅开奖通知
    private void subscribeLotteryTopic(LotteryType lotteryType) {
        String lotteryTopic = String.format(Locale.CHINA, "%s%s",
                AppConstants.NOTIFY_LOTTERY_TYPE, lotteryType.getName());
        LogF.d(NOTIFY_TAG, "lotteryTopic==>" + lotteryTopic + " ,topics.size()==> " + topics.size());
        topics.add(lotteryTopic);//订阅开奖话题推送
    }

    //订阅中奖通知
    private void subscribeWinTopic(String userId) {
        String winTopic = String.format(Locale.CHINA, "%s%s/%s",
                AppConstants.NOTIFY_WIN_TYPE, AppConstants.getChannelId(), userId);
        LogF.d("SharedPreferencesUtils", "订阅时的userId==>" + userId);
        topics.add(winTopic);//订阅中奖话题推送
        LogF.d(NOTIFY_TAG, "winTopic==>" + winTopic + " ,topics.size()==> " + topics.size());
    }

    //订阅系统通知
    private void subscribeSystemTopic() {
        String publicTopic = String.format(Locale.CHINA, "%s%s",
                AppConstants.NOTIFY_SYSTEM_TYPE, AppConstants.getChannelId());
        topics.add(publicTopic);//系统通知
    }

    //订阅所选推送
    private void setSubscribeTopic() {
        if (topics != null && topics.size() > 0) {
            if (MQTTManager.getInstance().isConnected()) {
                MQTTManager.getInstance().setTopic(topics);//设置订阅推送话题}
                for (int i = 0; i < topics.size(); i++) {
                    MQTTManager.getInstance().subscribeToTopic(topics.get(i));//订阅的推送话题
                }
            } else {
                MQTTManager.getInstance().setTopic(topics);//设置订阅推送话题}
            }
            LogF.d(NOTIFY_TAG, "size==>" + topics.size());
        }
    }

    //处理推送消息
    public void doNotify(NotifyMessage notifyMessage) {
        String alert = notifyMessage.getAlert();
        if (!msgId.equals(notifyMessage.getMsgId())) {
            msgId = notifyMessage.getMsgId();
            switch (notifyMessage.getType()) {
                case NotifyType.TYPE_1://广播消息
                case NotifyType.TYPE_2://开奖通知
                    LogF.d(NOTIFY_TAG, "收到的开奖推送消息==>" + alert);
                    ToastUtil.getInstances().showLong(alert);//Toast一下就行
                    break;
                case NotifyType.TYPE_3:
                    //App后台运行再通知 前台中奖弹框, 开奖是Toast
                    Intent intent = new Intent(getAppContext(), MainActivity.class);
                    intent.putExtra("data", notifyMessage);
                    LogF.d("中奖推送消息", "收到的中奖推送消息" + alert);
                    NotificationUtil notifyUtils = NotificationUtil.getInstance();
                    notifyUtils.reset();
                    if (AppUtils.isBackground(getAppContext())) {//后台
                        notifyUtils.setIcon(R.drawable.app_logo);
                        notifyUtils.setTitle("游彩");
                        notifyUtils.setContent(StringUtils.getInfo(alert));
                        notifyUtils.setIntent(intent);
                        notifyUtils.notify(String.valueOf(NotifyType.TYPE_3));
                        LogF.d("中奖推送消息", "走了中奖推送通知" + alert);
                    } else {
                        //发送全局广播
                        LogF.d("中奖推送消息", "走了中奖广播" + alert);
                        Intent winIntent = new Intent(AppConstants.BC_WIN_NOTIFY);
                        winIntent.putExtra(AppConstants.BC_NOTIFY_DATA, notifyMessage);
                        sendBroadcast(winIntent);
                    }
                    LogF.d(NOTIFY_TAG, "收到推送 isBackground==>" + AppUtils.isBackground(instance));
                    break;
            }
        }
    }

    public static boolean isNetworkIsOk() {
        return mNetworkIsOk;
    }

    public static void setNetworkIsOk(boolean mNetworkIsOk) {
        MyApplication.mNetworkIsOk = mNetworkIsOk;
    }

    public static String getTokenId() {
        UserBean user = getCurrLoginUser();
        return user != null ? user.getTokenId() : "";
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getTokenId());
    }

    public static Context getAppContext() {
        return instance;
    }

    public static MyApplication instance() {
        return instance;
    }

    /**
     * get current logined or lastest user
     *
     * @return
     */
    public static UserBean getCurrLoginUser() {
        return SharedPreferencesUtils.getLoginUserInfo();
    }

    /**
     * 更新userInfo
     *
     * @param user
     */
    public static void updateCurrLoginUser(UserBean user) {
        SharedPreferencesUtils.updateLoginUserInfo(user);
    }

    /**
     * 退出登录之后调用此方法，不然下次进来会有缓存
     */
    public static void logout() {
        updateCurrLoginUser(null);
        SharedPreferencesUtils.clearLoginUserInfo();
    }

    /**
     * 获取设备的屏幕信息，包括：密度、宽和高的像素
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
        if (mDisplayMetrics == null) {
            mDisplayMetrics = SystemUtils.getDensity();
        }
        return mDisplayMetrics;
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    static {
        PlatformConfig.setWeixin(AppConstants.THIRD_PLATFORM_WECHAT_ID, AppConstants.THIRD_PLATFORM_WECHAT_KEY);
        PlatformConfig.setQQZone(AppConstants.THIRD_PLATFORM_QQ_ID, AppConstants.THIRD_PLATFORM_QQ_KEY);
    }

    //获取屏幕尺寸
    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert windowManager != null;
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }
}
