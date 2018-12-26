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

public class ThreeSameSingleAdapter extends RecyclerView.Adapter<ThreeSameSingleAdapter.SumViewHolder> {

    private static final String TAG = "SumFragmentAdapter";

    private static final int MSG_UPDATE_ADAPTER = 0;

    private int mPlayType;//玩法
    private List<LotteryBall> mButtons;
    private Context mContext;
    private int mLastSelectedType;
    private FastThreeSelectedListener mSelectedListener;
    private List<LotteryBall> mSelectedButtons;

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
                        mSelectedListener.onSelectBall(mPlayType, -1, mSelectedButtons);
                    }
                    break;
            }
        }
    };

    public ThreeSameSingleAdapter(int playType, List<LotteryBall> btns, Context context, FastThreeSelectedListener listener) {
        mPlayType = playType;
        mButtons = btns;
        mContext = context;
        mSelectedListener = listener;
        mSelectedButtons = new ArrayList<>();
    }

    public void clearSelect() {
        mSelectedButtons.clear();
        for (LotteryBall bean : mButtons) {
            bean.setSelected(false);
        }
        notifyDataSetChanged();
        mSelectedListener.onSelectBall(mPlayType, -1, mSelectedButtons);
    }

    /**
     * 机选
     */
    public void machine(int num) {
        mSelectedButtons.clear();
        for (LotteryBall bean : mButtons) {
            if (bean.getNumberInt() == num) {
                bean.setSelected(true);
                mSelectedButtons.add(bean);
            } else {
                bean.setSelected(false);
                mSelectedButtons.remove(bean);
            }
        }
        notifyDataSetChanged();
        mSelectedListener.onSelectBall(mPlayType, -1, mSelectedButtons);
    }

    @Override
    public SumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sum_fragment, parent, false);
        return new SumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SumViewHolder holder, int position) {
        LotteryBall bean = mButtons.get(position);
        holder.titleTv.setText(bean.getNumber());
        holder.tipTv.setText(bean.getTip());
        if (bean.isSelected()) {
            holder.parentView.setSelected(true);
            holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_app_white));
            holder.tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_ECE7E7));
        } else {
            holder.parentView.setSelected(false);
            holder.titleTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_333333));
            holder.tipTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_7F7F7F));
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
                    notifyDataSetChanged();
                    mSelectedListener.onSelectBall(mPlayType, -1, mSelectedButtons);
                }
            });
        }
    }
}
