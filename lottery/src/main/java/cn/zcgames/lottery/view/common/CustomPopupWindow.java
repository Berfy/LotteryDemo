package cn.zcgames.lottery.view.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;

/**
 * 弹出动画的popupwindow
 */
public class CustomPopupWindow extends PopupWindow {

    private static final String TAG = "CustomPopupWindow";

    private Activity mContext;
    private boolean isShow;
    private ViewGroup mItemsView;

    // 根视图
    private View mRootView;
    // LayoutInflater
    LayoutInflater mInflater;

    /**
     * @param contentList 点击item的内容文字
     * @param clickList   点击item的事件
     *                    文字和click事件的list是对应绑定的
     */
    public CustomPopupWindow(Activity activity, List<String> contentList, List<View.OnClickListener> clickList) {
        this.mContext = activity;
        initLayout(contentList, clickList);
    }

    /**
     * 初始化Popup
     *
     * @param contentList 点击item内容的文字
     * @param clickList   点击item的事件
     */
    public void initLayout(List<String> contentList, List<View.OnClickListener> clickList) {

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = mInflater.inflate(R.layout.pop_root_double_color, null);
        setContentView(mRootView);

        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置PopUpWindow弹出的相关属性
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.argb(00, 00, 00, 00)));

        getContentView().setFocusableInTouchMode(true);

        mItemsView = (ViewGroup) mRootView.findViewById(R.id.linearLayout);

        //格式化点击item, 将文字和click事件一一绑定上去
        List<View> list = new ArrayList<>();
        for (int x = 0; x < contentList.size(); x++) {
            View view = View.inflate(mContext, R.layout.pop_item_double_color, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_content);
            View v_line = view.findViewById(R.id.v_line);
            textView.setText(contentList.get(x));
            view.setTag(contentList.get(x));
            mItemsView.addView(view);
            list.add(view);
            if (x == 0) {
                v_line.setVisibility(View.INVISIBLE);
            } else {
                v_line.setVisibility(View.VISIBLE);
            }
        }
        for (int x = 0; x < list.size(); x++) {
            list.get(x).setOnClickListener(clickList.get(x));
        }

    }

    /**
     * 弹出选项弹窗
     *
     * @param locationView 默认在该view的下方弹出, 和popupWindow类似
     */
    public void showPopupWindow(View locationView) {
        this.showAsDropDown(locationView, 0, -20);
    }

    /**
     * 隐藏popup
     */
    public void dismissPopupWindow() {
        isShow = false;
        this.dismiss();
    }

    public ViewGroup getLayout() {
        return mItemsView;
    }

    /**
     * popupwindow是否是显示状态
     */
    public boolean isShow() {
        return isShow;
    }

}
