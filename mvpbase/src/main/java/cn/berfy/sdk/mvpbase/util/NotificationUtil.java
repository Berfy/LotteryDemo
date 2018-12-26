package cn.berfy.sdk.mvpbase.util;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.berfy.sdk.mvpbase.R;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.model.Notify;
import cn.berfy.sdk.mvpbase.view.dialog.CommonDialog;

/**
 * Created by Berfy on 2017/11/21.
 * 通知管理
 */
public class NotificationUtil {

    private final String TAG = "NotificationUtil";
    private static NotificationUtil mInstance;
    private Context mContext;
    private int mIconResId;
    private String mTitle;
    private String mContent;
    private Intent mIntent;
    private NotificationManager mNotificationManager;
    private List<Notify> mCaches = new ArrayList<>();

    synchronized public static NotificationUtil init(Context context, Class defaultIntentClass) {
        if (null == mInstance) {
            synchronized (NotificationUtil.class) {
                if (null == mInstance) {
                    mInstance = new NotificationUtil(context, defaultIntentClass);
                }
            }
        }
        return mInstance;
    }

    public static NotificationUtil getInstance() {
        if (null == mInstance) {
            throw new NullPointerException("没有初始化NotifycationUtil");
        }
        return mInstance;
    }

    private NotificationUtil(Context context, Class defaultIntentClass) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mIntent = new Intent(context, defaultIntentClass);
    }

    public void setIcon(int iconResId) {
        mIconResId = iconResId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void reset() {
        mTitle = "";
        mContent = "";
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public void notify(int id) {
        if (TextUtils.isEmpty(mContent)) {
            LogF.d(TAG, "通知==> 没有内容  不予显示" + mContent);
            mContent = "";
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = mContext.getString(R.string.app_name);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, mIntent, PendingIntent.FLAG_ONE_SHOT);
        LogF.d(TAG, "通知==>" + mTitle);
        int iconId = mIconResId;
        Notification msgNotification = makeNotification(pendingIntent, mTitle, mContent, mContent, iconId, true, true);
        mNotificationManager.notify(id, msgNotification);
        addCache(new Notify("", id));
    }

    public void notify(String tag) {
        if (TextUtils.isEmpty(mContent)) {
            return;
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = mContext.getString(R.string.app_name);
        }
        int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        LogF.d(TAG, "通知==>" + mTitle);
        int iconId = mIconResId;
        Notification msgNotification = makeNotification(pendingIntent, mTitle, mContent, mContent, iconId, true, true);
        mNotificationManager.notify(tag + "", id, msgNotification);
        addCache(new Notify(tag, id));
    }

    private Notification makeNotification(PendingIntent pendingIntent, String title, String content, String tickerText,
                                          int iconId, boolean ring, boolean vibrate) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            /**
             * 当sdk版本大于26
             *
             * 谷歌从安卓7.0 Nougat就已经开始背景后台限制
             * 通知通道：安卓8.0（Android O）还引入了通知通道功能（Notification channels），
             * 重新定义通知内容中的应用程序类别，可以让开发者给予用户更精确的通知管理。
             */
            String id = "channel_Lottery";
            String description = "666";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            //为channel添加属性
            //channel.enableVibration(true); 震动
            //channel.enableLights(true);提示灯
            mNotificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(mContext, id);
            setNotify(pendingIntent, title, content, tickerText, iconId, ring, vibrate, builder);
        } else {
            builder = new NotificationCompat.Builder(mContext);
            setNotify(pendingIntent, title, content, tickerText, iconId, ring, vibrate, builder);
        }
        return builder.build();
    }

    private void setNotify(PendingIntent pendingIntent, String title, String content, String tickerText,
                           int iconId, boolean ring, boolean vibrate, NotificationCompat.Builder builder) {
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(tickerText)
                .setSmallIcon(iconId);

        int defaults = Notification.DEFAULT_LIGHTS;
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }

        if (ring) {
            defaults |= Notification.DEFAULT_SOUND;
        }

        builder.setDefaults(defaults);
    }

    private void addCache(Notify notify) {
        LogF.d(TAG, "通知缓存 " + GsonUtil.getInstance().toJson(notify) + "  size=" + mCaches.size());
        boolean isHas = false;
        for (Notify cache : mCaches) {
            if (cache.equals(notify)) {
                isHas = true;
            }
        }
        if (!isHas) {
            mCaches.add(notify);
        }
        LogF.d(TAG, "通知缓存后大小 " + mCaches.size());
    }

    private void removeCache(Notify notify) {
        mCaches.remove(notify);
    }

    public void clearNotify(String tag) {
        LogF.d(TAG, "清除通知缓存 tag=" + tag);
        int size = mCaches.size();
        if (size == 0) {
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Notify cache = mCaches.get(i);
            if (tag.equals(cache.getTag())) {
                LogF.d(TAG, "清除通知缓存 有通知  id=" + cache.getId() + " tag=" + tag);
                mNotificationManager.cancel(tag, cache.getId());
                removeCache(cache);
                continue;
            }
        }
    }

    public void clearNotify(int id) {
        LogF.d(TAG, "清除通知缓存 id=" + id);
        int size = mCaches.size();
        if (size == 0) {
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Notify cache = mCaches.get(i);
            if (id == cache.getId()) {
                LogF.d(TAG, "清除通知缓存 有通知  id=" + cache.getId() + " tag=" + cache.getTag());
                mNotificationManager.cancel(cache.getId());
                removeCache(cache);
            }
        }
    }

    public void clearAll() {
        LogF.d(TAG, "清除全部通知缓存");
        int size = mCaches.size();
        if (size == 0) {
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Notify cache = mCaches.get(i);
            mNotificationManager.cancel(cache.getId());
        }
        mCaches.clear();
        mNotificationManager.cancelAll();
    }


    public boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass;      /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getNotifyPermission(Activity context) {
        CommonDialog notifyDialog = new CommonDialog(context);
        notifyDialog.showDialog("无法获取您的通知权限，您将无法收到中奖通知，是否去设置?",
                "取消", "确定", (dialog, which) -> dialog.dismiss(),
                (dialog, which) -> {
                    // 进入设置系统应用权限界面
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                        context.startActivity(intent);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("app_package", context.getPackageName());
                        intent.putExtra("app_uid", context.getApplicationInfo().uid);
                        context.startActivity(intent);
                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                        context.startActivity(intent);
                    }
                });
    }
}
