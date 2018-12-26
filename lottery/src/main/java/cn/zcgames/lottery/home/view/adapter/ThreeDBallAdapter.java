package cn.zcgames.lottery.home.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.listener.ChooseNumSelectedListener;
import cn.zcgames.lottery.view.common.EnlargePopupWindow;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.SystemUtils;

import static cn.zcgames.lottery.app.AppConstants.BALL_MAX_SELECTED_NUMBER_RED;
import static cn.zcgames.lottery.utils.SystemUtils.DENSITY_356;

/**
 * Created by admin on 2017/4/1.
 */

public class ThreeDBallAdapter extends RecyclerView.Adapter<ThreeDBallAdapter.BallViewHolder> {

    private static final String TAG = "ThreeDBallAdapter";

    private static final int MSG_UPDATE_ADAPTER = 0;
    private static final int MSG_HIDDEN_POPUPWINDOW = 1;

    private Context mContext;
    private List<LotteryBall> mBallList;
    private ColorStateList mRedCs, mSelectedCs;
    private ChooseNumSelectedListener mListener;
    private List<LotteryBall> mSelectedRedBalls;
    private int mBallType;

    private EnlargePopupWindow popupWindow;
    private int mPopupXOff;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HIDDEN_POPUPWINDOW:
                    popupWindow.dismissPopupWindow();
                    break;
                case MSG_UPDATE_ADAPTER:
                    LotteryBall ball = (LotteryBall) msg.obj;
                    if (mSelectedRedBalls.contains(ball)) {
                        mSelectedRedBalls.remove(ball);
                    } else {
                        if (mSelectedRedBalls.size() >= BALL_MAX_SELECTED_NUMBER_RED) {
                            UIHelper.showToast("最多投注16个红球");
                            return;
                        } else {
                            mSelectedRedBalls.add(ball);
                        }
                    }
                    mListener.onSelectBall(mBallType, mSelectedRedBalls);
                    ball.setSelected(!ball.isSelected());
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    public ThreeDBallAdapter(Context context, int ballType, ChooseNumSelectedListener listener) {
        this.mContext = context;
        this.mBallType = ballType;
        this.mListener = listener;
        mBallList = new ArrayList<>();
        initTypeData();
        mRedCs = mContext.getResources().getColorStateList(R.color.color_red_ball);
        mSelectedCs = mContext.getResources().getColorStateList(R.color.color_FFFFFF);
        mSelectedRedBalls = new ArrayList<>();
        popupWindow = new EnlargePopupWindow(mContext);
        mPopupXOff = getXOff();
    }

    private void initTypeData() {
        for (int i = 0; i < 10; i++) {
            LotteryBall ball = new LotteryBall();
            ball.setNumber(LotteryUtils.mThreeDNumbers[i]);
            ball.setSelected(false);
            ball.setType(AppConstants.BALL_TYPE_RED);
            mBallList.add(ball);
        }
    }

    public void clearSelectedBall() {
        if (mSelectedRedBalls != null && mSelectedRedBalls.size() > 0) {
            for (int i = 0; i < mSelectedRedBalls.size(); i++) {
                mSelectedRedBalls.get(i).setSelected(false);
            }

        }
        notifyDataSetChanged();
        mSelectedRedBalls.clear();
        mListener.onSelectBall(mBallType, mSelectedRedBalls);
    }

    @Override
    public BallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_double_color_ball, parent, false);
        return new BallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BallViewHolder holder, int position) {
        LotteryBall ball = mBallList.get(position);
        holder.titleTv.setText(ball.getNumber());
        holder.titleTv.setTag(ball);
        holder.parentView.setTag(ball);
        if (ball.isSelected()) {
            holder.titleTv.setTextColor(mSelectedCs);
            holder.titleTv.setBackgroundResource(R.drawable.shape_bg_red_ball_selected);
        } else {
            holder.titleTv.setTextColor(mRedCs);
            holder.titleTv.setBackgroundResource(R.drawable.shape_bg_ball_normal);
        }
    }

    @Override
    public int getItemCount() {
        return mBallList.size();
    }

    public class BallViewHolder extends RecyclerView.ViewHolder {

        private View parentView;
        private TextView titleTv;

        public BallViewHolder(View v) {
            super(v);
            titleTv = (TextView) v.findViewById(R.id.doubleColorBall_tv_number);
            parentView = v.findViewById(R.id.doubleColorBall_rl_parentView);

            parentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    SystemUtils.getDensity();
                    LotteryBall ball = (LotteryBall) v.getTag();
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        popupWindow.showPopupWindow(v, ball.getNumber(), ball.getType(), mPopupXOff, 101);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        popupWindow.dismissPopupWindow();
                        Message msg = new Message();
                        msg.obj = ball;
                        msg.what = MSG_UPDATE_ADAPTER;
                        mHandler.sendMessage(msg);
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        mHandler.sendEmptyMessageDelayed(MSG_HIDDEN_POPUPWINDOW, 500);
                    }
                    return true;
                }
            });
        }
    }

    public int getXOff() {
        int xoff = -9;
        int densityDpi = MyApplication.getDisplayMetrics().densityDpi;
        if (densityDpi == SystemUtils.DENSITY_XXHIGH) {
            xoff = 3;
        } else if (densityDpi == SystemUtils.DENSITY_XHIGH) {
            xoff = 4;
        } else if (densityDpi == SystemUtils.DENSITY_HIGH) {
            xoff = 5;
        } else if (densityDpi == SystemUtils.DENSITY_MEDIUM) {
            xoff = 7;
        } else if (densityDpi == DENSITY_356) {
            xoff = -9;
        }
        return xoff;
    }

}
