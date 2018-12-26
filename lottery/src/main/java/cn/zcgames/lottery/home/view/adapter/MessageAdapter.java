package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.listener.ListenerItemClick;
import cn.berfy.sdk.mvpbase.model.MessageBean;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.view.common.refreshview.recyclerview.BaseRecyclerAdapter;

/**
 * 站内消息适配器
 *
 * @author NorthStar
 * @date 2018/9/27 12:01
 */
public class MessageAdapter extends BaseRecyclerAdapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private List<MessageBean> mMessageList = new ArrayList<>();
    private ListenerItemClick mListener;

    public MessageAdapter(Context context, ListenerItemClick mListener) {
        this.mContext = context;
        this.mListener = mListener;
    }

    public void setMessageList(List<MessageBean> list, boolean mIsLoadMore) {
        if (list == null) return;
        if (mIsLoadMore) {
            mMessageList.addAll(list);
        } else {
            mMessageList = list;
        }
        notifyDataSetChanged();
    }

    public List<MessageBean> getMessageList() {
        return mMessageList;
    }

    @Override
    public MessageViewHolder getViewHolder(View view) {
        return new MessageViewHolder(view);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position, boolean isItem) {
        MessageBean bean = mMessageList.get(position);
        holder.titleTv.setText(bean.getTitle());
        holder.contentTv.setText(bean.getBody());
        long created = bean.getCreated();
        if (created != 0) {
            String time = TimeUtil.timestampConvertDate("yyyy-MM-dd HH:mm:ss", created);
            holder.timeTv.setText(time);
        }
        holder.itemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemListener(v, position);
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout itemRl;
        private TextView titleTv;
        private TextView contentTv;
        private TextView timeTv;

        public MessageViewHolder(View itemView) {
            super(itemView);
            itemRl = itemView.findViewById(R.id.rl_system_message);
            titleTv = itemView.findViewById(R.id.message_tv_title);
            contentTv = itemView.findViewById(R.id.message_tv_content);
            timeTv = itemView.findViewById(R.id.message_tv_time);
        }
    }
}
