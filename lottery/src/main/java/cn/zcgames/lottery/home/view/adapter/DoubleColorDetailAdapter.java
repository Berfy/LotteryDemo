package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.response.DoubleColorDetailBean;

/**
 * Created by admin on 2017/4/7.
 */

public class DoubleColorDetailAdapter extends RecyclerView.Adapter<DoubleColorDetailAdapter.DetailViewHolder> {

    private Context mContext;
    private List<DoubleColorDetailBean.Prize> mData;
    private String mLotteryType;

    public DoubleColorDetailAdapter(Context context, String lotteryType) {
        this.mContext = context;
        this.mLotteryType = lotteryType;
        mData = new ArrayList<>();
    }

    public void setDoubleColorHistorys(List<DoubleColorDetailBean.Prize> list) {
        if (list == null) {
            return;
        }
        this.mData = list;
        notifyDataSetChanged();
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_double_color_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        DoubleColorDetailBean.Prize bean = mData.get(position);
        holder.level.setText(bean.getLevel());
//        if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_SEVEN_HAPPY)) {
//            holder.winning_bet.setText(bean.getWinning_bet());
//            holder.winning_prize.setText(bean.getWinning_money());
//        } else {
        holder.winning_bet.setText(bean.getWinning_bet());
        holder.winning_prize.setText(bean.getWinning_prize());
//        }
        if (position % 2 == 1) {
            holder.parentView.setBackgroundResource(R.color.color_FFFFFF);
        } else {
            holder.parentView.setBackgroundResource(R.color.color_app_bg);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        private TextView level;
        private TextView winning_bet;
        private TextView winning_prize;
        private LinearLayout parentView;

        public DetailViewHolder(View itemView) {
            super(itemView);
            level = (TextView) itemView.findViewById(R.id.level);
            winning_bet = (TextView) itemView.findViewById(R.id.winning_bet);
            winning_prize = (TextView) itemView.findViewById(R.id.winning_prize);
            parentView = (LinearLayout) itemView.findViewById(R.id.parent_view);
        }
    }
}
