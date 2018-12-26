package cn.zcgames.lottery.personal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.bean.response.OrderRecordInfo;
import cn.zcgames.lottery.view.common.refreshview.recyclerview.BaseRecyclerAdapter;

/**
 * 投注记录列表的adapter
 *
 * @author NorthStar
 * @date 2018/9/18 14:43
 */
public class OrderRecordAdapter extends BaseRecyclerAdapter<OrderRecordAdapter.HistoryHolder> {

    private static final String TAG = "OrderRecordAdapter";

    private List<OrderRecordInfo.OrderList> orderLists;
    private Context mContext;
    private View.OnClickListener mClick;
    private LayoutInflater inflater;

    public OrderRecordAdapter(Context context, View.OnClickListener click) {
        this.mContext = context;
        mClick = click;
        inflater = LayoutInflater.from(mContext);
        orderLists = new ArrayList<>();
    }

    public void setHistoryList(List<OrderRecordInfo.OrderList> list, boolean mIsLoadMore) {
        if (mIsLoadMore) {
            orderLists.addAll(list);
        } else {
            if (list == null) {
                orderLists.clear();
            } else {
                orderLists = list;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = inflater.inflate(R.layout.item_buy_history, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public HistoryHolder getViewHolder(View view) {
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position, boolean isItem) {
        OrderRecordInfo.OrderList bean = orderLists.get(position);
        long time = bean.getCreatd();
        holder.timeTv.setText(TimeUtil.format("MM月dd日 HH:mm", time));
        holder.contentTv.setText(String.format(Locale.CHINA, "金额:%s元", StringUtils.getCash(bean.getAmount(), AppConstants.DIGITS)));
        String name = (String) SharedPreferenceUtil.get(mContext, bean.getName(), "");
        holder.titleTv.setText(name);
        //        holder.positionTv.setText((position + 1) + "");
        int status = bean.getStatus();//2: 未开奖； 3: 已开奖
        switch (status) {
            case 2:
                holder.typeTv.setText(CommonUtil.getString(mContext, R.string.mine_order_not_open));
                break;

            case 3:
                holder.typeTv.setText(CommonUtil.getString(mContext, R.string.mine_order_open));
                break;
        }
        holder.parentView.setTag(bean);
        //item点击事件
        holder.parentView.setOnClickListener(mClick);
    }

    @Override
    public int getAdapterItemCount() {
        return orderLists.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView timeTv, titleTv, contentTv, typeTv;
        private View parentView;

        public HistoryHolder(View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.history_tv_time);
            titleTv = itemView.findViewById(R.id.history_tv_title);
            contentTv = itemView.findViewById(R.id.history_tv_content);
            typeTv = itemView.findViewById(R.id.history_tv_type);
            parentView = itemView.findViewById(R.id.history_rl_parentView);
        }
    }
}
