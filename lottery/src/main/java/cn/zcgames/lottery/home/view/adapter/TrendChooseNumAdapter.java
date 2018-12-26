package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.LotteryBall;

/**
 * author: Berfy
 * date: 2018/10/16
 * 走势图选号适配器
 */
public class TrendChooseNumAdapter extends RecyclerView.Adapter<TrendChooseNumAdapter.ChooseNumViewHolder> {

    private List<LotteryBall> mButtons;//全部的button
    private Context mContext;
    private String mLotteryType;//彩种
    private int mPlayType;//玩法
    private int mNumPosition;//个十百千万 1-
    private int mItemWidth;//宽高
    private int mItemHeight;
    private TrendChooseNumAdapter.ChooseNumSelectedListener mSelectedListener;//选号监听器
    private List<LotteryBall> mSelectedButtons;//选中的button

    public TrendChooseNumAdapter(Context context, String lotteryType, int playType, int numPos, List<LotteryBall> btns, TrendChooseNumAdapter.ChooseNumSelectedListener listener) {
        mContext = context;
        mLotteryType = lotteryType;
        mPlayType = playType;
        mNumPosition = numPos;
        mButtons = btns;
        mSelectedListener = listener;
        mSelectedButtons = new ArrayList<>();
    }

    public TrendChooseNumAdapter(Context context, String lotteryType, int playType, int numPos, int itemWidth, int itemHeight,
                                 List<LotteryBall> btns, TrendChooseNumAdapter.ChooseNumSelectedListener listener) {
        mContext = context;
        mLotteryType = lotteryType;
        mPlayType = playType;
        mNumPosition = numPos;
        mItemWidth = itemWidth;
        mItemHeight = itemHeight;
        mButtons = btns;
        mSelectedListener = listener;
        mSelectedButtons = new ArrayList<>();
    }

    @Override
    public ChooseNumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseNumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_trend_choose_num_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ChooseNumViewHolder holder, int position) {
        LotteryBall lotteryBall = mButtons.get(position);
        if (mPlayType == AppConstants.FAST_THREE_2_SAME_ONE || mPlayType == AppConstants.FAST_THREE_2_SAME_MORE) {
            holder.tvNum.setText(lotteryBall.getNumber() + "*");
        } else {
            holder.tvNum.setText(lotteryBall.getNumber());
        }
        if (lotteryBall.isSelected()) {
            holder.tvNum.setBackgroundResource(R.drawable.shape_bg_btn_trend_choose_num_selected);
            holder.tvNum.setTextColor(ContextCompat.getColor(mContext, R.color.white_normal));
        } else {
            holder.tvNum.setBackgroundResource(R.drawable.shape_bg_btn_trend_choose_num);
            holder.tvNum.setTextColor(ContextCompat.getColor(mContext, R.color.color_app_main));
        }
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = mItemWidth > 0 ? mItemWidth : ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = mItemHeight > 0 ? mItemHeight : ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mButtons.size();
    }

    /**
     * 清空选择
     */
    public void clearSelect() {
        mSelectedButtons.clear();
        for (LotteryBall bean : mButtons) {
            bean.setSelected(false);
        }
        notifyDataSetChanged();
        mSelectedListener.onSelectBall(mLotteryType, mPlayType, -1, mSelectedButtons);
    }

    public class ChooseNumViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNum;
        private LinearLayout parentView;

        public ChooseNumViewHolder(View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_num);
            tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LotteryBall bean = mButtons.get(getAdapterPosition());
                    if (mSelectedButtons.contains(bean)) {
                        mSelectedButtons.remove(bean);
                    } else {
                        mSelectedButtons.add(bean);
                    }
                    bean.setSelected(!bean.isSelected());
                    mSelectedListener.onSelectBall(mLotteryType, mPlayType, mNumPosition, mSelectedButtons);
                    notifyItemChanged(getAdapterPosition(), "123");
                }
            });
        }
    }

    public interface ChooseNumSelectedListener {
        void onSelectBall(String lotteryType, int playType, int numPos, List<LotteryBall> balls);
    }
}
