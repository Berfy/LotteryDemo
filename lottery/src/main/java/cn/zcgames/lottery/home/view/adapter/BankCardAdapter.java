package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.BankCardBean;

/**
 * Created by admin on 2017/4/12.
 */

public class BankCardAdapter extends RecyclerView.Adapter<BankCardAdapter.BankViewHolder> {

    private static final String TAG = "LotteryOrderAdapter";

    private List<BankCardBean> mBankList;
    private Context mContext;

    public BankCardAdapter(Context context) {
        this.mContext = context;
        mBankList = new ArrayList<>();
    }

    public void setOrderList(List<BankCardBean> list) {
        if (list == null) {
            return;
        }
        mBankList = list;
        notifyDataSetChanged();
    }

    public void deleteOrder(BankCardBean card) {
        if (card != null && mBankList != null && mBankList.size() > 0) {
            mBankList.remove(card);
            notifyDataSetChanged();
        } else {
        }
    }

    public BankCardBean getOrderByPosition(int orderPosition) {
        if (mBankList != null && mBankList.size() >= orderPosition) {
            return mBankList.get(orderPosition);
        }
        return null;
    }

    @Override
    public BankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_card, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BankViewHolder holder, int position) {
        final BankCardBean order = mBankList.get(position);
        String title = order.getTitle();
        holder.nameTv.setText(title);
        holder.userNameTv.setText(order.getName());

        int headerId = R.drawable.label_zhongguo;
        if (title.contains("农业")) {
            headerId = R.drawable.label_nongye;
        } else if (title.contains("工商")) {
            headerId = R.drawable.label_gongshang;
        }
        holder.bankLogoIv.setImageResource(headerId);

//        holder.bankLogoIv.setImageResource(order.getLogoId());
    }

    @Override
    public int getItemCount() {
        return mBankList.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv, userNameTv;
        private View mParentView;
        public LinearLayout order_ll;
        public TextView mDeleteTv;
        private ImageView bankLogoIv;

        public BankViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.bank_name);
            userNameTv = (TextView) itemView.findViewById(R.id.bank_userName);
            mParentView = itemView.findViewById(R.id.bank_rl);
            order_ll = (LinearLayout) itemView.findViewById(R.id.bank_ll);
            mDeleteTv = (TextView) itemView.findViewById(R.id.delete_tv);
            bankLogoIv = (ImageView) itemView.findViewById(R.id.bank_logo);
        }
    }

}
