package cn.zcgames.lottery.home.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.listener.FastThreeSelectedListener;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * Created by admin on 2017/6/19.
 */

public class SumFragmentAdapter extends RecyclerView.Adapter<SumFragmentAdapter.SumViewHolder> {

    private static final String TAG = "SumFragmentAdapter";

    //快速选号类型，依次为：大、小、单、双
    public static final int QUICK_SELECT_TYPE_BIG = 2;
    public static final int QUICK_SELECT_TYPE_SMALL = -2;
    public static final int QUICK_SELECT_TYPE_SINGLE = 1;
    public static final int QUICK_SELECT_TYPE_DOUBLE = -1;

    //快速选号结果：无、大、小、单、双、双大、双小、单大、单小
    public static final int QUICK_SELECT_RESULT_NO = 0;
    public static final int QUICK_SELECT_RESULT_BIG = 1;
    public static final int QUICK_SELECT_RESULT_SMALL = 2;
    public static final int QUICK_SELECT_RESULT_SINGLE = 3;
    public static final int QUICK_SELECT_RESULT_DOUBLE = 4;
    public static final int QUICK_SELECT_RESULT_DOUBLE_BIG = 5;
    public static final int QUICK_SELECT_RESULT_DOUBLE_SMALL = 6;
    public static final int QUICK_SELECT_RESULT_SINGLE_BIG = 7;
    public static final int QUICK_SELECT_RESULT_SINGLE_SMALL = 8;

    //消息类型
    private static final int MSG_UPDATE_ADAPTER = 10;
    private static final int MSG_QUICK_SELECT = 11;

    private int mPlayType;//玩法
    private List<LotteryBall> mButtons;//全部的button
    private Context mContext;
    private FastThreeSelectedListener mSelectedListener;//选号监听器
    private List<LotteryBall> mSelectedButtons;//选中的button
    private int mSelectResultType;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_ADAPTER:
                    if (msg.obj != null && msg.obj instanceof LotteryBall) {
                        LotteryBall bean = (LotteryBall) msg.obj;
                        if (mSelectedButtons.contains(bean)) {
                            mSelectedButtons.remove(bean);
                        } else {
                            mSelectedButtons.add(bean);
                        }
                        bean.setSelected(!bean.isSelected());
                        notifyDataSetChanged();
                        mSelectedListener.onSelectBall(mPlayType, QUICK_SELECT_RESULT_NO, mSelectedButtons);
                    }
                    break;
                case MSG_QUICK_SELECT:
                    mSelectedButtons.clear();
                    for (LotteryBall bean : mButtons) {
                        if (bean.isSelected() && !mSelectedButtons.contains(bean)) {
                            mSelectedButtons.add(bean);
                        }
                    }
                    mSelectedListener.onSelectBall(mPlayType, mSelectResultType, mSelectedButtons);
                    notifyDataSetChanged();
                    break;
                case QUICK_SELECT_RESULT_NO:
                    for (LotteryBall bean : mButtons) {
                        bean.setSelected(false);
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_NO;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_BIG:
                    for (LotteryBall bean : mButtons) {
                        if (bean.getNumberInt() >= 11) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_BIG;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_SMALL:
                    for (LotteryBall bean : mButtons) {
                        if (bean.getNumberInt() < 11) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_SMALL;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_SINGLE:
                    for (LotteryBall bean : mButtons) {
                        if (bean.getNumberInt() % 2 == 1) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_SINGLE;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_DOUBLE:
                    for (LotteryBall bean : mButtons) {
                        if (bean.getNumberInt() % 2 == 0) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_DOUBLE;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_DOUBLE_BIG:
                    for (LotteryBall bean : mButtons) {
                        int number = bean.getNumberInt();
                        if (number >= 11 && number % 2 == 0) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_DOUBLE_BIG;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_DOUBLE_SMALL:
                    for (LotteryBall bean : mButtons) {
                        int number = bean.getNumberInt();
                        if (number < 11 && number % 2 == 0) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_DOUBLE_SMALL;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_SINGLE_BIG:
                    for (LotteryBall bean : mButtons) {
                        int number = bean.getNumberInt();
                        if (number >= 11 && number % 2 == 1) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_SINGLE_BIG;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
                case QUICK_SELECT_RESULT_SINGLE_SMALL:
                    for (LotteryBall bean : mButtons) {
                        int number = bean.getNumberInt();
                        if (number < 11 && number % 2 == 1) {
                            bean.setSelected(true);
                        } else {
                            bean.setSelected(false);
                        }
                    }
                    mSelectResultType = QUICK_SELECT_RESULT_SINGLE_SMALL;
                    mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
                    break;
            }
        }
    };

    public SumFragmentAdapter(int playType, List<LotteryBall> btns, Context context, FastThreeSelectedListener listener) {
        mPlayType = playType;
        mButtons = btns;
        mContext = context;
        mSelectedListener = listener;
        mSelectedButtons = new ArrayList<>();
    }

    /**
     * 机选
     */
    public void machine(int sum) {
        mSelectedButtons.clear();
        for (LotteryBall bean : mButtons) {
            if (sum == bean.getNumberInt()) {
                bean.setSelected(true);
            } else {
                bean.setSelected(false);
            }
        }
        mHandler.sendEmptyMessage(MSG_QUICK_SELECT);
    }

    /**
     * 清空选择
     */
    public void clearSelect() {
        mSelectedButtons.clear();
        for (LotteryBall bean : mButtons) {
            bean.setSelected(false);
        }
        mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_NO);
        notifyDataSetChanged();
        mSelectedListener.onSelectBall(mPlayType, -1, mSelectedButtons);
    }

    public void selectStatus(boolean bigSelected, boolean smallSelected, boolean singleSelected, boolean doubleSelected) {
        if (bigSelected) {
            if (singleSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_SINGLE_BIG);
            } else if (doubleSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_DOUBLE_BIG);
            } else {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_BIG);
            }
        } else if (smallSelected) {
            if (singleSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_SINGLE_SMALL);
            } else if (doubleSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_DOUBLE_SMALL);
            } else {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_SMALL);
            }
        } else if (singleSelected) {
            if (bigSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_SINGLE_BIG);
            } else if (smallSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_SINGLE_SMALL);
            } else {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_SINGLE);
            }
        } else if (doubleSelected) {
            if (bigSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_DOUBLE_BIG);
            } else if (smallSelected) {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_DOUBLE_SMALL);
            } else {
                mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_DOUBLE);
            }
        } else {
            mHandler.sendEmptyMessage(QUICK_SELECT_RESULT_NO);
        }
    }

    @Override
    public SumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sum_fragment, parent, false);
        return new SumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SumViewHolder holder, int position) {
        LotteryBall bean = mButtons.get(position);
        holder.titleTv.setText(bean.getNumberInt() + "");
        holder.tipTv.setText(bean.getTip());
        if (bean.isSelected()) {
            holder.parentView.setSelected(true);
            holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
            holder.tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
        } else {
            holder.parentView.setSelected(false);
            holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
            holder.tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_7F7F7F));
        }
    }

    @Override
    public void onBindViewHolder(SumViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            LotteryBall bean = mButtons.get(position);
            if (bean.isSelected()) {
                holder.parentView.setSelected(true);
                holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
                holder.tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
            } else {
                holder.parentView.setSelected(false);
                holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
                holder.tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_7F7F7F));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mButtons.size();
    }

    public class SumViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv, tipTv;
        private LinearLayout parentView;

        public SumViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.sum_btn_title);
            tipTv = (TextView) itemView.findViewById(R.id.sum_btn_tip);
            parentView = (LinearLayout) itemView.findViewById(R.id.sum_ll_parent);
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LotteryBall bean = mButtons.get(getAdapterPosition());
                    if (mSelectedButtons.contains(bean)) {
                        mSelectedButtons.remove(bean);
                    } else {
                        mSelectedButtons.add(bean);
                    }
                    bean.setSelected(!bean.isSelected());
                    mSelectedListener.onSelectBall(mPlayType, 0, mSelectedButtons);
                    notifyItemChanged(getAdapterPosition(), "123");
                }
            });
        }
    }
}
