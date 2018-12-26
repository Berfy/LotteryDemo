package cn.zcgames.lottery.personal.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zcgames.lottery.R;

/**
 * 当RecyclerView的数据为空时，显示的布局
 * Created by Rothschild on 2016-11-11.
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout empty_layout;
    public ImageView imageView;
    public TextView tv;

    public EmptyViewHolder(View itemView) {
        super(itemView);
        empty_layout = itemView.findViewById(R.id.noData_view);
        imageView = itemView.findViewById(R.id.iv_not_data_icon);
        tv = itemView.findViewById(R.id.tv_not_data_tip);
    }
}
