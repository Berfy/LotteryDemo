package cn.zcgames.lottery.personal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.personal.model.ChaseDetailStatus;

/**
 * Created by admin on 2017/7/17.
 */

public class ChaseDetailStatusAdapter extends RecyclerView.Adapter<ChaseDetailStatusAdapter.ChaseStatusViewHolder> {

    private List<ChaseDetailStatus> mChaseStatusList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;

    public ChaseDetailStatusAdapter(Context context, List<ChaseDetailStatus> mChaseStatusList) {
        this.mChaseStatusList = mChaseStatusList;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ChaseStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_chase_status, parent, false);
        return new ChaseStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChaseStatusViewHolder holder, int position) {
        ChaseDetailStatus bean = mChaseStatusList.get(position);
        holder.chaseTitle.setText(bean.getSequence());
        holder.chaseStatus.setText(bean.getBillStatus());
        holder.wardStatus.setText(bean.getRewardStatus());
        holder.winStatus.setText(bean.getWinNumber());
        if (position % 2 == 1) {
            holder.parentView.setBackgroundResource(R.color.color_FFFFFF);
        } else {
            holder.parentView.setBackgroundResource(R.color.color_app_bg);
        }
    }

    @Override
    public int getItemCount() {
        return mChaseStatusList.size();
    }

    public class ChaseStatusViewHolder extends RecyclerView.ViewHolder {

        TextView chaseTitle;
        TextView chaseStatus;
        TextView wardStatus;
        TextView winStatus;
        View parentView;

        public ChaseStatusViewHolder(View itemView) {
            super(itemView);
            chaseTitle = itemView.findViewById(R.id.chaseStatus_title);
            chaseStatus = itemView.findViewById(R.id.chaseStatus_status);
            wardStatus = itemView.findViewById(R.id.chaseStatus_ward);
            winStatus = itemView.findViewById(R.id.chaseStatus_win);
            parentView = itemView.findViewById(R.id.parent_ll_layout);
        }
    }
}
