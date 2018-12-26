package com.qiaoxg.dialoglibrary;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by admin on 2017/7/18.
 */

public class ShareDialog {

    Dialog dialog;
    Context context;
    View parentView, qqView, weixinView, friendView;
    private Display display;

    public ShareDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ShareDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_share_dialog, null);
        view.setMinimumWidth(display.getWidth());
        qqView = view.findViewById(R.id.share_qq);
        weixinView = view.findViewById(R.id.share_weixin);
        friendView = view.findViewById(R.id.share_friend);

        dialog = new Dialog(context,R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public ShareDialog setQQClickListener(final View.OnClickListener listener) {
        qqView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public ShareDialog setWeixinClickListener(final View.OnClickListener listener) {
        weixinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public ShareDialog setFriendClickListener(final View.OnClickListener listener) {
        friendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }
}
