package cn.zcgames.lottery.personal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.home.bean.LotteryType;

/**
 * 推送消息开关适配器
 *
 * @author NorthStar
 * @date 2018/9/28 14:20
 */
public class NotifySetAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ItemListenerClick mItemListener;
    private List<LotteryType> mLotteryInfo;
    private final static int NOTIFY_HEAD = 0;//中奖通知开关
    private final static int NOTIFY_LOTTERY = 1;//开奖通知开关
    public static final String TAG = "MessageSettingActivity";
    public boolean mWinIsChecked = false;//是否打开中奖通知


    public NotifySetAdapter(Context context, List<LotteryType> lotteryTypeInfo) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mLotteryInfo = lotteryTypeInfo;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? NOTIFY_HEAD : NOTIFY_LOTTERY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NOTIFY_HEAD) {
            View headWinView = inflater.inflate(R.layout.notify_set_head_layout, parent, false);
            return new HeadViewHolder(headWinView);
        } else {
            View notifyView = inflater.inflate(R.layout.item_notify_set_layout, parent, false);
            return new NotifySetViewHolder(notifyView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        if (holder instanceof NotifySetViewHolder) {
            NotifySetViewHolder notifySetHolder = (NotifySetViewHolder) holder;
            int position = pos - 1;
            LotteryType lotteryTypeInfo = mLotteryInfo.get(position);
            String name = lotteryTypeInfo.getShow();
            if (!TextUtils.isEmpty(name)) notifySetHolder.lotteryNameTV.setText(name);
            notifySetHolder.lotteryBtn.setChecked(lotteryTypeInfo.isChecked());
            notifySetHolder.lotteryBtn.setOnClickListener(v -> {
                boolean checked = ((ToggleButton) v).isChecked();
                LogF.d(TAG, "name==>" + name + ", LotteryChecked==>" + checked);
                mItemListener.itemListener(position, checked);
            });
        } else if (holder instanceof HeadViewHolder) {
            HeadViewHolder headHolder = (HeadViewHolder) holder;
            headHolder.winBtn.setChecked(mWinIsChecked);
            headHolder.winBtn.setOnClickListener(v -> {
                boolean winIsChecked = ((ToggleButton) v).isChecked();
                LogF.d(TAG, "winIsChecked==>" + winIsChecked);
                mItemListener.winSetListener(winIsChecked);
            });
        }
    }

    public void setItemListener(ItemListenerClick itemListener) {
        mItemListener = itemListener;
    }

    public interface ItemListenerClick {
        void itemListener(int position, boolean isChecked);//开奖开关设置的事件

        void winSetListener(boolean isOpenWin);//中奖开关设置
    }

    public void setWinBtnState(boolean winIsChecked) {
        mWinIsChecked = winIsChecked;
    }

    @Override
    public int getItemCount() {
        return mLotteryInfo == null ? 1 : mLotteryInfo.size() + 1;
    }

    //中奖开关设置(头视图)
    class HeadViewHolder extends RecyclerView.ViewHolder {
        private ToggleButton winBtn;

        public HeadViewHolder(View itemView) {
            super(itemView);
            winBtn = itemView.findViewById(R.id.toggle_win);
        }
    }

    //开奖开关设置
    class NotifySetViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout lotteryRl;
        private ToggleButton lotteryBtn;
        private TextView lotteryNameTV;

        public NotifySetViewHolder(View itemView) {
            super(itemView);
            lotteryBtn = itemView.findViewById(R.id.toggle_lottery);
            lotteryNameTV = itemView.findViewById(R.id.tv_lottery_name);
        }
    }
}
