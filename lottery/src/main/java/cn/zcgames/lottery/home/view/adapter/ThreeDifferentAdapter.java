package cn.zcgames.lottery.home.view.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.listener.FastThreeSelectedListener;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * Created by admin on 2017/6/20.
 */

public class ThreeDifferentAdapter extends RecyclerView.Adapter<ThreeDifferentAdapter.ThreeDifferentViewHolder> {

    private static final String TAG = "ThreeDifferentAdapter";

    private static final int MSG_SELECT_BUTTON = 0;
    private static final int MSG_IGNORE_BUTTON = 1;

    private int mPlayType;//玩法
    private List<LotteryBall> mData;
    private List<LotteryBall> mSelectedButtons;
    private FastThreeSelectedListener mSelectListener;
    private int mAdapterType;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SELECT_BUTTON:
                    if (msg.obj != null && msg.obj instanceof LotteryBall) {
                        LotteryBall bean = (LotteryBall) msg.obj;
                        if (mSelectedButtons.contains(bean)) {
                            mSelectedButtons.remove(bean);
                        } else {
                            mSelectedButtons.add(bean);
                        }
                        bean.setSelected(!bean.isSelected());
                        notifyDataSetChanged();
                        mSelectListener.onSelectBall(mPlayType, mAdapterType, mSelectedButtons);
                    }
                    break;
                case MSG_IGNORE_BUTTON:
                    notifyDataSetChanged();
                    mSelectListener.onSelectBall(mPlayType, mAdapterType, mSelectedButtons);
                    break;
            }
        }
    };

    public ThreeDifferentAdapter(int playType, List<LotteryBall> data, FastThreeSelectedListener listener) {
        mPlayType = playType;
        mData = data;
        mSelectListener = listener;
        mSelectedButtons = new ArrayList<>();
    }

    public ThreeDifferentAdapter(int playType, List<LotteryBall> data, FastThreeSelectedListener listener, int type) {
        mPlayType = playType;
        mData = data;
        mSelectListener = listener;
        mAdapterType = type;
        mSelectedButtons = new ArrayList<>();
    }

    public void clearSelect() {
        mSelectedButtons.clear();
        for (LotteryBall bean : mData) {
            bean.setSelected(false);
        }
        mHandler.sendEmptyMessage(MSG_IGNORE_BUTTON);
    }

    /**
     * 机选
     */
    public void machine(List<Integer> nums) {
        mSelectedButtons.clear();
        for (LotteryBall bean : mData) {
            if (nums.contains(bean.getNumberInt())) {
                bean.setSelected(true);
                mSelectedButtons.add(bean);
            } else {
                bean.setSelected(false);
                mSelectedButtons.remove(bean);
            }
        }
        notifyDataSetChanged();
        mSelectListener.onSelectBall(mPlayType, mAdapterType, mSelectedButtons);
    }

    public void setIgnoreData(List<LotteryBall> selectBean) {
        boolean isChange = false;
        for (LotteryBall ignoreBean : selectBean) {
            for (int i = 0; i < mSelectedButtons.size(); i++) {
                LotteryBall bean = mSelectedButtons.get(i);
                if (ignoreBean.getNumberInt() > 10) {
                    if ((bean.getNumberInt() * 10 + bean.getNumberInt()) == ignoreBean.getNumberInt()) {
                        bean.setSelected(!bean.isSelected());
                        mSelectedButtons.remove(bean);
                        isChange = true;
                    } else {

                    }
                } else {
                    if ((ignoreBean.getNumberInt() * 10 + ignoreBean.getNumberInt()) == bean.getNumberInt()) {
                        bean.setSelected(!bean.isSelected());
                        mSelectedButtons.remove(bean);
                        isChange = true;
                    } else {

                    }
                }
            }
        }
        if (isChange) {
            mHandler.sendEmptyMessage(MSG_IGNORE_BUTTON);
        }
    }

    @Override
    public ThreeDifferentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_three_different_fragment, parent, false);
        return new ThreeDifferentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThreeDifferentViewHolder holder, int position) {
        LotteryBall bean = mData.get(position);
        holder.titleTv.setText(bean.getNumberInt() + "");
        if (bean.isSelected()) {
            holder.parentView.setSelected(true);
            holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
        } else {
            holder.parentView.setSelected(false);
            holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ThreeDifferentViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv;
        View parentView;

        public ThreeDifferentViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.sum_btn_title);
            parentView = itemView.findViewById(R.id.sum_ll_parent);
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LotteryBall bean = mData.get(getAdapterPosition());
                    if (mSelectedButtons.contains(bean)) {
                        mSelectedButtons.remove(bean);
                    } else {
                        mSelectedButtons.add(bean);
                    }
                    bean.setSelected(!bean.isSelected());
                    notifyDataSetChanged();
                    mSelectListener.onSelectBall(mPlayType, mAdapterType, mSelectedButtons);
                }
            });
        }
    }
}
