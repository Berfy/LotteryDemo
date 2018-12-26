package com.qiaoxg.dialoglibrary;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class WheelViewDialog {
    private static final String TAG = "WheelViewDialog";

    private Context context;
    private Dialog dialog;
    private Display display;

    private TextView okTv, cancelTv;

    private WheelView mWheelView;

    private String mSelectedBankName;

    onWheelViewClickListener monWheelViewClickListener;

    public WheelViewDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public WheelViewDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_wheel, null);

        view.setMinimumWidth(display.getWidth());

        mWheelView = (WheelView) view.findViewById(R.id.wheel_view);
        mWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mSelectedBankName = item;
            }
        });
        okTv = (TextView) view.findViewById(R.id.ok_tv);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monWheelViewClickListener.onOkClicked(mSelectedBankName);
                dialog.dismiss();
            }
        });
        cancelTv = (TextView) view.findViewById(R.id.cancel_tv);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monWheelViewClickListener.onOkClicked("");
                dialog.dismiss();
            }
        });

        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public WheelViewDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public WheelViewDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public WheelViewDialog setBankNameList(List<String> nameList) {
        mWheelView.setItems(nameList);
        return this;
    }

    public void setSelection(int position) {
        mWheelView.setSelection(position);
        mSelectedBankName = mWheelView.getSelectedItem();
    }

    public WheelViewDialog addWheelViewListener(onWheelViewClickListener listener) {
        this.monWheelViewClickListener = listener;
        return this;
    }

    public WheelViewDialog setOkListener(View.OnClickListener listener) {
        okTv.setOnClickListener(listener);
        return this;
    }

    public WheelViewDialog setCancelListener(View.OnClickListener listener) {
        okTv.setOnClickListener(listener);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public interface onWheelViewClickListener {
        void onOkClicked(String bankName);

        void onCancelClicked();
    }
}
