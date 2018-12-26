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
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.bean.ChaseBillBeanNew;
import cn.zcgames.lottery.view.common.refreshview.recyclerview.BaseRecyclerAdapter;

/**
 * 用于追期管理的适配器
 *
 * @author NorthStar
 * @date 2018/9/7 16:04
 */
public class AfterPhaseAdapter extends BaseRecyclerAdapter<AfterPhaseAdapter.HistoryHolder> {

    private static final String TAG = "OrderRecordAdapter";

    private List<ChaseBillBeanNew.ChaseBillList> mHistoryList;
    private Context mContext;
    private View.OnClickListener mClickListener;

    public AfterPhaseAdapter(Context mContext, View.OnClickListener listener) {
        this.mContext = mContext;
        this.mClickListener = listener;
        mHistoryList = new ArrayList<>();
    }

    public void setChaseBillBeanList(List<ChaseBillBeanNew.ChaseBillList> list, boolean mIsLoadMore) {
        if (mIsLoadMore) {
            mHistoryList.addAll(list);
        } else {
            if (list == null) {
                mHistoryList.clear();
            } else {
                mHistoryList = list;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_buy_history, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public HistoryHolder getViewHolder(View view) {
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position, boolean isItem) {
        ChaseBillBeanNew.ChaseBillList bean = mHistoryList.get(position);
        holder.contentTv.setText(String.format("%s元", StringUtils.getCash(bean.getCost(), AppConstants.DIGITS)));
        String title = String.format(Locale.CHINA, "共追%s期，已追%s期", bean.getChase(), bean.getChased());
        holder.timeTv.setText(title);
        String name = (String) SharedPreferenceUtil.get(mContext, bean.getName(), "");
        holder.titleTv.setText(name);
        holder.typeTv.setText(bean.getChase().equals(bean.getChased()) ? "已结束" : "追期中");
        holder.typeTv.setTextColor(CommonUtil.getColor(mContext, bean.getChase().equals(bean.getChased()) ? R.color.color_CCCCCC : R.color.color_666666));
        holder.parentView.setTag(bean);
    }

    @Override
    public int getAdapterItemCount() {
        return mHistoryList.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView timeTv, titleTv, contentTv, typeTv, positionTv;
        private View parentView;

        public HistoryHolder(View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.history_tv_time);
            titleTv = itemView.findViewById(R.id.history_tv_title);
            contentTv = itemView.findViewById(R.id.history_tv_content);
            typeTv = itemView.findViewById(R.id.history_tv_type);
            parentView = itemView.findViewById(R.id.history_rl_parentView);
            positionTv = itemView.findViewById(R.id.positionTv);
            parentView.setOnClickListener(mClickListener);
        }
    }
}
