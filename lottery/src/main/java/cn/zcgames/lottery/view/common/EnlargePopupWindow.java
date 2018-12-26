package cn.zcgames.lottery.view.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.UIHelper;

/**
 * 弹出动画的popupwindow
 */
public class EnlargePopupWindow extends PopupWindow {

    private static final String TAG = "EnlargePopupWindow";

    private Context mContext;
    private boolean isShow;
    private TextView mItemsView;

    // 根视图
    private View mRootView;

    public EnlargePopupWindow(Context activity) {
        this.mContext = activity;
        initLayout();
    }

    /**
     * 初始化Popup
     */
    public void initLayout() {

        mRootView = LayoutInflater.from(mContext).inflate(R.layout.pop_number_enlarge, null);
        setContentView(mRootView);

        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置PopUpWindow弹出的相关属性
        setTouchable(false);
        setOutsideTouchable(true);
        setFocusable(false);
        setBackgroundDrawable(new ColorDrawable(Color.argb(00, 00, 00, 00)));

        getContentView().setFocusableInTouchMode(true);

        mItemsView = (TextView) mRootView.findViewById(R.id.number_tv);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    EnlargePopupWindow.this.dismiss();
                }
                return false;
            }
        });

    }

    /**
     * 弹出选项弹窗
     *
     * @param locationView 默认在该view的下方弹出, 和popupWindow类似
     */
    public void showPopupWindow(View locationView, String numberStr, int ballType,int xoff, int yoff) {
        mItemsView.setText(numberStr);
        if (ballType == AppConstants.BALL_TYPE_BLUE) {
            mItemsView.setBackgroundResource(R.drawable.bg_enlarge_blue);
        } else if (ballType == AppConstants.DOUBLE_COLOR_DANTUO_BLUE) {
            mItemsView.setBackgroundResource(R.drawable.bg_enlarge_blue);
        } else if (ballType == AppConstants.DOUBLE_COLOR_DANTUO_RED_TUO) {
            mItemsView.setBackgroundResource(R.drawable.bg_enlarge_red);
        } else if (ballType == AppConstants.DOUBLE_COLOR_DANTUO_RED_DAN) {
            mItemsView.setBackgroundResource(R.drawable.bg_enlarge_red);
        } else if (ballType == AppConstants.BALL_TYPE_RED) {
            mItemsView.setBackgroundResource(R.drawable.bg_enlarge_red);
        }
        isShow = true;
//        this.showAsDropDown(locationView);
        this.showAsDropDown(locationView, -UIHelper.dip2px(mContext, xoff), -UIHelper.dip2px(mContext, yoff));
    }

    /**
     * 隐藏popup
     */
    public void dismissPopupWindow() {
        isShow = false;
        this.dismiss();
    }

    /**
     * popupwindow是否是显示状态
     */
    public boolean isShow() {
        return isShow;
    }

}
