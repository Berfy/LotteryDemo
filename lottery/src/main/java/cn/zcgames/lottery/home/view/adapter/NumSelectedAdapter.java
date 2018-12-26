package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.NumSelectedBean;

public class NumSelectedAdapter extends RecyclerView.Adapter<NumSelectedAdapter.SumViewHolder> {
    private List<NumSelectedBean> datas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnSumItemSelectedListener sumListener;

    public NumSelectedAdapter(Context context, List<NumSelectedBean> data){
        this.datas = data;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public SumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sumView = inflater.inflate(R.layout.sum_select_item, parent, false);
        return new SumViewHolder(sumView);
    }

    @Override
    public void onBindViewHolder(SumViewHolder holder, int position) {
        NumSelectedBean sumBean = datas.get(position);
        holder.mTVNum.setText(sumBean.getNum());
        if(sumBean.isSelected()){
            setSelectedBG(holder.mTVNum, R.drawable.select_num_item_selected, R.color.color_white);
        }else{
            setSelectedBG(holder.mTVNum, R.drawable.select_num_item_normal, R.color.color_dd3048);
        }
        holder.mTVNum.setOnClickListener(v ->{
            if(sumBean.isSelected()){
                sumBean.setSelected(false);
            }else{
                sumBean.setSelected(true);
            }
            notifyDataSetChanged();
            sumListener.onItemSelected();
        });
    }

    public void setSelectedBG(TextView textView, int drawable, int textColor){
        textView.setBackground(mContext.getResources().getDrawable(drawable));
        textView.setTextColor(mContext.getResources().getColor(textColor));
    }

    @Override
    public int getItemCount() {
        return datas.size() > 0 ? datas.size() : 0;
    }

    class SumViewHolder extends RecyclerView.ViewHolder{
        TextView mTVNum;

        public SumViewHolder(View itemView) {
            super(itemView);
            this.mTVNum = itemView.findViewById(R.id.tv_select_number_item);
        }
    }

    public interface OnSumItemSelectedListener{
        void onItemSelected();
    }

    public void setSumSelectedListener(OnSumItemSelectedListener listener){
        this.sumListener = listener;
    }
}
