package cn.zcgames.lottery.personal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.personal.model.AccountDetailBean;
import cn.zcgames.lottery.view.common.refreshview.recyclerview.BaseRecyclerAdapter;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_AWARD;
import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_BUY;
import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_RECHARGE;
import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_WITHDRAW;

/**
 * 账户明细的adapter
 */
public class AccountDetailAdapter extends BaseRecyclerAdapter<AccountDetailAdapter.DetailHolder> {

    private Context mContext;
    private int mAdapterType;
    private List<AccountDetailBean> mAccountDetailBeans;

    public AccountDetailAdapter(Context context, int type) {
        this.mContext = context;
        this.mAdapterType = type;
        mAccountDetailBeans = new ArrayList<>();
    }

    public void setAccountDetailBeans(List<AccountDetailBean> mAccountDetailBeans, boolean isLoadMore) {
        if (isLoadMore) {
            this.mAccountDetailBeans.addAll(mAccountDetailBeans);
        } else {
            this.mAccountDetailBeans.clear();
            this.mAccountDetailBeans = mAccountDetailBeans;
        }
        notifyDataSetChanged();
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_account_detail, parent, false);
        return new DetailHolder(view);
    }

    @Override
    public DetailHolder getViewHolder(View view) {
        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position, boolean isItem) {
        AccountDetailBean bean = mAccountDetailBeans.get(position);
        String created = bean.getTime();
        if (!TextUtils.isEmpty(created)) {
            String time = TimeUtil.timestampConvertDate("yyyy-MM-dd HH:mm:ss", Long.parseLong(created));
            holder.timeTv.setText("时间:\t\t" + time);
        }
        String type = bean.getType();
        String detailType = "";
        int colorType = 0;//1:+ 2:-
        switch (type) {
            case "1":
                detailType = "充值";
                colorType = 1;
                break;

            case "2":
                detailType = "购彩";
                colorType = 2;
                break;

            case "3":
                detailType = "提现";
                colorType = 2;
                break;

            case "4":
                detailType = "中奖";
                colorType = 1;
                break;
        }

        if (colorType != 0) {
            int textColorId = colorType == 1 ? R.color.color_red : R.color.color_blue_ball;
            String textPre = colorType == 1 ? "+" : "-";
            holder.amountTv.setTextColor(StaticResourceUtils.getColorResourceById(textColorId));
            String rawNum = String.valueOf(bean.getAmount());
            holder.amountTv.setText(String.format(Locale.CHINA, "%s%s元", textPre, StringUtils.getCash(rawNum, AppConstants.DIGITS)));
        }

        holder.typeTv.setText(String.format(Locale.CHINA, "类型:\t\t%s", detailType));
        String remain = String.valueOf(bean.getRemain());
        holder.remainTv.setText(String.format(Locale.CHINA, "%s%s", StringUtils.getCash(remain, AppConstants.DIGITS),
                StaticResourceUtils.getStringResourceById(R.string.lottery_yuan)));
    }

    @Override
    public int getAdapterItemCount() {
        return mAccountDetailBeans.size();
    }


    public class DetailHolder extends RecyclerView.ViewHolder {

        private TextView timeTv, amountTv, typeTv, remainTv;
        private View parentView;

        public DetailHolder(View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.detail_tv_time);
            amountTv = itemView.findViewById(R.id.detail_tv_amount);
            remainTv = itemView.findViewById(R.id.detail_tv_remain);
            typeTv = itemView.findViewById(R.id.detail_tv_type);
        }
    }

}
