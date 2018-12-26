package cn.zcgames.lottery.view;

import android.app.Activity;
import android.text.TextUtils;

import com.qiaoxg.dialoglibrary.AlertDialog;

import cn.berfy.sdk.mvpbase.model.DetailMessage;
import cn.berfy.sdk.mvpbase.model.NotifyMessage;
import cn.berfy.sdk.mvpbase.model.NotifyType;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.activity.LotteryOrderDetailActivity;
import cn.zcgames.lottery.personal.view.activity.PhaseDetailActivity;

public class NotifyDialog {

    private Activity mContext;
    public NotifyDialog(Activity context) {
        mContext = context;
    }

    public void handlePushMessage(NotifyMessage notifyMessage) {
        switch (notifyMessage.getType()) {
            case NotifyType.TYPE_3://中奖通知
                String hint = notifyMessage.getAlert();
                DetailMessage body = notifyMessage.getBody();
                if (body != null) {
                    if (mContext != null && !mContext.isFinishing() && !mContext.isDestroyed()) {
                        //检查是否登录
                        if (!MyApplication.isLogin()) {
                            showGotoLogin();
                        } else {
                            showConfirmNextChase(body, hint);
                        }
                    }
                }
                break;
        }
    }

    //提示登录对话框
    private void showGotoLogin() {
        AlertDialog alertDialog = new AlertDialog(mContext)
                .builder()
                .setTitle(mContext.getString(R.string.tips_tip))
                .setCancelable(false)
                .setMsg(mContext.getString(R.string.tips_win_message))
                .setNegativeButton("取消", v -> {
                })
                .setPositiveButton("确认", v -> UIHelper.gotoActivity(mContext, LoginActivity.class, false));
        alertDialog.show();
    }

    private void showConfirmNextChase(DetailMessage msg, String hint) {
        AlertDialog alertDialog = new AlertDialog(mContext)
                .builder()
                .setTitle(mContext.getString(R.string.tips_tip))
                .setCancelable(false)
                .setMsg(hint)
                .setNegativeButton("取消", v -> {
                })
                .setPositiveButton("确认", v -> {

                    String orderId = msg.getOrderId();
                    if (!TextUtils.isEmpty(orderId)) {
                        int chase = Integer.valueOf(msg.getChase());
                        LogF.d(MyApplication.NOTIFY_TAG, "chase==>" + chase + " ,orderId==>" + orderId);
                        if (chase > 1) {
                            PhaseDetailActivity.intoThisActivity(mContext,
                                    orderId, msg.getLotteryName());//追期管理
                            if (mContext instanceof PhaseDetailActivity) {
                                mContext.finish();//防止重复初始化Dialog
                            }
                        } else {
                            LotteryOrderDetailActivity.inToThisActivity(mContext, orderId, msg.getLotteryName());//订单详情
                            if (mContext instanceof LotteryOrderDetailActivity) {
                                mContext.finish();//防止重复初始化Dialog
                            }
                        }
                    }
                });
        alertDialog.show();
        LogF.d(MyApplication.NOTIFY_TAG, "走了showConfirmNextChase");
    }
}
