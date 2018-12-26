package cn.zcgames.lottery.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qiaoxg.dialoglibrary.WaitingDialog;

import cn.berfy.sdk.mvpbase.util.AnimUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.callback.ConfirmDialogCallback;

/**
 * Created by admin on 2017/3/30.
 */

public class UIHelper {

    private static final String TAG = "UIHelper";

    private static WaitingDialog mWaitingDialog;

    /**
     * 显示提示框
     *
     * @param context
     * @param msgStrId
     * @param callback
     */
    public static void showTipDialog(Activity context, int msgStrId,
                                      final ConfirmDialogCallback callback) {
        showTipDialog(context, context.getString(msgStrId), callback);
    }

    /**
     * 显示提示框
     *
     * @param context
     * @param msg
     * @param callback
     */
    public static void showTipDialog(Activity context, String msg,
                                      final ConfirmDialogCallback callback) {
        com.qiaoxg.dialoglibrary.AlertDialog dialog = new com.qiaoxg.dialoglibrary.AlertDialog(context)
                .builder()
                .setMsg(msg)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onConfirmResult(true);
                    }
                });
        if (!context.isFinishing())
            dialog.show();
    }

    /**
     * 显示确认对话框
     *
     * @param context
     * @param msgStrId
     * @param callback
     */
    public static void showConfirmDialog(Activity context, int msgStrId,
                                         final ConfirmDialogCallback callback) {
        com.qiaoxg.dialoglibrary.AlertDialog dialog = new com.qiaoxg.dialoglibrary.AlertDialog(context)
                .builder()
                .setMsg(msgStrId)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onConfirmResult(true);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onConfirmResult(false);
                    }
                });
        if (!context.isFinishing())
            dialog.show();
    }

    /**
     * 显示确认对话框
     *
     * @param context
     * @param msgStr
     * @param callback
     */
    public static void showConfirmDialog(Activity context, String msgStr,
                                         final ConfirmDialogCallback callback) {
        com.qiaoxg.dialoglibrary.AlertDialog dialog = new com.qiaoxg.dialoglibrary.AlertDialog(context)
                .builder()
                .setMsg(msgStr)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onConfirmResult(true);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onConfirmResult(false);
                    }
                });
        if (!context.isFinishing())
            dialog.show();
    }

    /**
     * 显示确认对话框
     *
     * @param context
     * @param content   提示文字
     * @param leftText  左侧按钮文字
     * @param rightText 右侧按钮文字
     * @param callback
     */
    public static void showConfirmDialog(Activity context, String content, String leftText, String rightText,
                                         final ConfirmDialogCallback callback) {
        com.qiaoxg.dialoglibrary.AlertDialog dialog = new com.qiaoxg.dialoglibrary.AlertDialog(context)
                .builder()
                .setMsg(content)
                .setPositiveButton(leftText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onConfirmResult(true);
                    }
                })
                .setNegativeButton(rightText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onConfirmResult(false);
                    }
                });
        if (!context.isFinishing())
            dialog.show();
    }

    /**
     * 显示吐司
     *
     * @param msgStr
     */
    public static void showToast(String msgStr) {
        ToastUtil.getInstances().showShort(msgStr);
    }

    /**
     * 显示吐司
     *
     * @param msgStrId
     */
    public static void showToast(int msgStrId) {
        ToastUtil.getInstances().showShort(msgStrId);
    }

    /**
     * 隐藏系统的标题栏和状态栏
     *
     * @param activity
     */
    public static void hideSystemTitleBar(Activity activity) {
        setStatusTransparent(activity);
    }

    /**
     * 说明：Android 4.4+ 设置状态栏透明
     */
    private static void setStatusTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0+ 实现
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4 实现
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private static int mSystemUiVisibility = -1;

    /**
     * 状态栏黑色设置
     */
    public static void darkStatusBar(Activity activity, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView == null) return;
            if (-1 == mSystemUiVisibility) {//保存默认UI主题
                mSystemUiVisibility = decorView.getSystemUiVisibility();
            } else {
                decorView.setSystemUiVisibility(mSystemUiVisibility);
            }
            int vis = decorView.getSystemUiVisibility();
            if (isDark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
            LogF.d(TAG, "黑色标题栏 " + isDark);
        }
    }

    /**
     * 设置View的显示状态
     *
     * @param v
     * @param isShow
     */
    public static void showWidget(View v, boolean isShow) {
        if (v == null) return;
        int visibleInt = isShow ? View.VISIBLE : View.GONE;
        v.setVisibility(visibleInt);
    }

    /**
     * Activity间跳转
     *
     * @param fromActivity
     * @param toActivity
     */
    public static void gotoActivity(Activity fromActivity, Class toActivity, boolean isFinish) {
        gotoActivity(fromActivity, toActivity, isFinish, null);
    }

    /**
     * Activity间跳转
     *
     * @param fromActivity
     * @param toActivity
     */
    public static void gotoActivity(Activity fromActivity, Class toActivity,
                                    boolean isFinish, Intent intent) {
        if (AnimUtil.checkJump()) {
            if (isFinish) fromActivity.finish();
            if (intent != null) {
                intent.setClass(fromActivity, toActivity);
                fromActivity.startActivity(intent);
            } else {
                fromActivity.startActivity(new Intent(fromActivity, toActivity));
            }
        }
    }

    /**
     * 设置view是否可用
     *
     * @param v
     * @param isEnable
     */
    public static void setWidgetEnable(View v, boolean isEnable) {
        if (v == null) return;
        if (isEnable == v.isEnabled()) return;
        v.setEnabled(isEnable);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param context （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = MyApplication.getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param context  （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = MyApplication.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param context （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = MyApplication.getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param context （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = MyApplication.getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 显示mWaitingDialog
     *
     * @param context  上下文
     * @param msgStr   要显示的信息
     * @param isCancel 点击外边是否消失
     */
    public static void showWaitingDialog(Context context, String msgStr, boolean isCancel) {
        if (context == null) return;
        WaitingDialog instance = WaitingDialog.getInstance();
        instance.setContext(context);
        mWaitingDialog = instance
                .builder()
                .setCancelClickOutside(isCancel)
                .setMessageTip(msgStr);
        mWaitingDialog.show();
    }

    /**
     * 显示mWaitingDialog
     *
     * @param context  上下文
     * @param msg      要显示的信息
     * @param isCancel 点击外边是否消失
     */
    public static void showWaitingDialog(Activity context, int msg, boolean isCancel) {
        if (context == null) return;
        WaitingDialog instance = WaitingDialog.getInstance();
        instance.setContext(context);
        mWaitingDialog = instance
                .builder()
                .setCancelClickOutside(isCancel)
                .setMessageTip(msg);
        mWaitingDialog.show();
    }

    /**
     * 隐藏mWaitingDialog
     */
    public static void hideWaitingDialog() {
        if (mWaitingDialog != null) {
            mWaitingDialog.dismiss();
        }
    }
}
