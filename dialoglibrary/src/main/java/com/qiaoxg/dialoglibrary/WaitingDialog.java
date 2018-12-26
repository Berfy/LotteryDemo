package com.qiaoxg.dialoglibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 加载时的等待框
 *
 * @author NorthStar
 * @date 2018/9/6 13:40
 */

public class WaitingDialog {

    private Dialog dialog;
    private Context mContext;
    private TextView tipTv;

    //饿汉式单例,线程安全
    private WaitingDialog() {
    }

    @SuppressLint("StaticFieldLeak")
    private static WaitingDialog instance = new WaitingDialog();

    public static WaitingDialog getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.CustomLoadingDialogStyle);
    }

    public WaitingDialog builder() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_waiting, null);
        tipTv = view.findViewById(R.id.tipTextView);
        if (dialog == null) dialog = new Dialog(mContext, R.style.CustomLoadingDialogStyle);
        dialog.setContentView(view);
        return instance;
    }

    public WaitingDialog setMessageTip(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            tipTv.setText(msg);
        }
        return instance;
    }

    public WaitingDialog setMessageTip(int msgId) {
        tipTv.setText(msgId);
        return instance;
    }

    public WaitingDialog setCancelable(boolean isCancel) {
        dialog.setCancelable(isCancel);
        return instance;
    }

    public WaitingDialog setCancelClickOutside(boolean isCancel) {
        dialog.setCanceledOnTouchOutside(isCancel);
        return instance;
    }

    public void show() {
        if (dialog == null || isShowing()) return;
        if (canUse()) dialog.show();
        Log.d("WaitingDialog", "isShowing==>" + isShowing() + " ,dialog==>" + dialog);
    }

    private boolean canUse() {
        return !(mContext instanceof Activity) || !((Activity) mContext).isFinishing();
    }

    public void dismiss() {
        if (dialog == null || !isShowing()) return;
        if (canUse()) dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }
}
