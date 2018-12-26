package cn.zcgames.lottery.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qiaoxg.dialoglibrary.WaitingDialog;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.model.NotifyMessage;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.view.NotifyDialog;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * Created by admin on 2017/3/30.
 * App中的Activity都继承这个
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected Activity mContext;
    private WaitingDialog mWaitingDialog;
    private static Toast mToast;
    public boolean mIsDestroy;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityManager.getInstance().pushActivity(this);
        mIsDestroy = false;
        registerReceiver();
    }

    public void setButterKnife(Activity context) {
        unbinder = ButterKnife.bind(context);
    }

    /**
     * 显示mWaitingDialog
     *
     * @param context  上下文
     * @param msgStr   要显示的信息
     * @param isCancel 点击外边是否消失
     */
    public void showWaitingDialog(Activity context, String msgStr, boolean isCancel) {
        if (context.isFinishing()) return;
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
     * @param msgStrId 要显示的信息
     * @param isCancel 点击外边是否消失
     */
    public void showWaitingDialog(Activity context, int msgStrId, boolean isCancel) {

        if (context.isFinishing()) return;
        WaitingDialog instance = WaitingDialog.getInstance();
        instance.setContext(context);
        mWaitingDialog = instance.builder()
                .setCancelClickOutside(isCancel)
                .setMessageTip(msgStrId);
        mWaitingDialog.show();
    }

    /**
     * 隐藏mWaitingDialog
     */
    public void hideWaitingDialog() {
        if (mWaitingDialog == null) Log.e(TAG, "hideWaitingDialog: mWaitingDialog is null");
        if (mIsDestroy) Log.e(TAG, "hideWaitingDialog: mIsDestroy true");
        if (mWaitingDialog != null && !mIsDestroy) {
            mWaitingDialog.dismiss();
        }
    }

    /**
     * 显示吐司
     *
     * @param msgStr
     */
    public static void showToast(String msgStr) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getAppContext(), msgStr, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msgStr);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 返回
     *
     * @param activity
     */
    public void goBack(Activity activity) {
        activity.finish();
    }

    //注册广播接收器(接收用户推送消息)
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.BC_WIN_NOTIFY);//接到中奖通知时
        registerReceiver(mCommonReceiver, filter);
    }

    //注册广播接收器
    private BroadcastReceiver mCommonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case AppConstants.BC_WIN_NOTIFY://接到中奖通知时
                    NotifyMessage notifyMessage = (NotifyMessage) intent.getSerializableExtra(AppConstants.BC_NOTIFY_DATA);
                    //判断是否是栈顶Activity
                    Activity currentActivity = ActivityManager.getInstance().currentActivity();
                    if (mContext.getClass() == currentActivity.getClass()) {
                        NotifyDialog notifyDialog = new NotifyDialog(currentActivity);
                        if (notifyMessage != null) {
                            Log.d(TAG, "notifyMessage==>" + notifyMessage.getType());
                            notifyDialog.handlePushMessage(notifyMessage);//处理中奖信息
                        }
                    }
                    break;
            }
        }
    };


    /**
     * 显示确认对话框
     *
     * @param context
     */
    public void showExitAppConfirmDialog(final Activity context) {
        com.qiaoxg.dialoglibrary.AlertDialog alertDialog = new com.qiaoxg.dialoglibrary.AlertDialog(this)
                .builder()
                .setCancelable(false)
                .setMsg(R.string.tips_sure_to_logout)
                .setPositiveButton(StaticResourceUtils.getStringResourceById(R.string.btn_ok),
                        v -> context.finish()).setNegativeButton(StaticResourceUtils.getStringResourceById(R.string.btn_cancel),
                        v -> {
                        });
        alertDialog.show();
    }

    public void showMessageDialog(String msgString, View.OnClickListener listener) {
        com.qiaoxg.dialoglibrary.AlertDialog alertDialog = new com.qiaoxg.dialoglibrary.AlertDialog(this)
                .builder()
                .setCancelable(false)
                .setMsg(msgString)
                .setNegativeButton(StaticResourceUtils.getStringResourceById(R.string.btn_ok), listener);
        alertDialog.show();
    }

    public void showMessageDialog(int msgString, View.OnClickListener listener) {
        com.qiaoxg.dialoglibrary.AlertDialog alertDialog = new com.qiaoxg.dialoglibrary.AlertDialog(this)
                .builder()
                .setCancelable(false)
                .setMsg(msgString)
                .setNegativeButton(StaticResourceUtils.getStringResourceById(R.string.btn_ok), listener);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroy = true;
        if (unbinder != null) unbinder.unbind();
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
        if (null != mCommonReceiver)
            unregisterReceiver(mCommonReceiver);
        ActivityManager.getInstance().popActivity(this);
    }
}
