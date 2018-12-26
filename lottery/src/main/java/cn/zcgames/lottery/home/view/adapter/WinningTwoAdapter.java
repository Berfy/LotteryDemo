package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.home.bean.OpenAwardBean;

public class WinningTwoAdapter extends RecyclerView.Adapter<WinningTwoAdapter.WinViewHolder>{
    private Context mContext;
    private List<OpenAwardBean> datas;
    private LayoutInflater inflater;

    public WinningTwoAdapter(Context context, List<OpenAwardBean> data){
        this.mContext = context;
        this.datas = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public WinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.award_two_item, parent, false);
        WinViewHolder winHolder = new WinViewHolder(view);
        return winHolder;
    }

    @Override
    public void onBindViewHolder(WinViewHolder holder, int position) {
        OpenAwardBean openBean = datas.get(position);
        if(position % 2 == 0 ){
            holder.mLLBG.setBackground(mContext.getResources().getDrawable(R.color.color_white));
            holder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_white));
        }else{
            holder.mLLBG.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
            holder.mLLWaitNum.setBackground(mContext.getResources().getDrawable(R.color.color_f3f3ea));
        }
        if(TextUtils.equals("等待开奖", String.valueOf(openBean.getAwardNums()))){
            holder.mLLWaitNum.setVisibility(View.VISIBLE);
            holder.mLLBG.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mTVWaitText.getLayoutParams();
            params.weight = 4;
            holder.mTVWaitPeriod.setText(openBean.getPeriod());
            holder.mTVWaitText.setText(String.valueOf(openBean.getAwardNums()));
        }else{
            holder.mLLWaitNum.setVisibility(View.GONE);
            holder.mLLBG.setVisibility(View.VISIBLE);
            holder.mTVPeriod.setText(openBean.getPeriod());
            holder.mTVNums.setText(String.valueOf(openBean.getAwardNums()));
            holder.mTVNumState.setText(openBean.getNumState());
        }

    }

    @Override
    public int getItemCount() {
        return datas.size() > 0 ? datas.size() : 0;
    }

    class WinViewHolder extends RecyclerView.ViewHolder{
        TextView mTVPeriod, mTVNums, mTVNumState,mTVWaitPeriod, mTVWaitText;
        LinearLayout mLLBG, mLLWaitNum;

        public WinViewHolder(View itemView) {
            super(itemView);
            this.mLLBG = itemView.findViewById(R.id.ll_award_two_item);
            this.mTVPeriod = itemView.findViewById(R.id.tv_award_item_period);
            this.mTVNums = itemView.findViewById(R.id.tv_award_item_num);
            this.mTVNumState = itemView.findViewById(R.id.tv_award_item_state);
            mLLWaitNum = itemView.findViewById(R.id.ll_wait_num);
            mTVWaitPeriod = itemView.findViewById(R.id.tv_wait_item_period);
            mTVWaitText = itemView.findViewById(R.id.tv_wait_num_text);
        }
    }
}
