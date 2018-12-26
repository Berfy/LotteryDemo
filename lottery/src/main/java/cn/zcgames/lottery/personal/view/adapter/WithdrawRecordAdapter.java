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
import cn.zcgames.lottery.personal.model.WithdrawRecord;
import cn.zcgames.lottery.personal.view.holder.EmptyViewHolder;
import cn.zcgames.lottery.view.common.refreshview.recyclerview.BaseRecyclerAdapter;

/**
 * 提现记录的adapter
 *
 * @author NorthStar
 * @date 2018/9/11 14:24
 */
public class WithdrawRecordAdapter extends BaseRecyclerAdapter<WithdrawRecordAdapter.RecordHolder> {

    private static final String TAG = "RecordAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    private List<WithdrawRecord> recordList;


    public WithdrawRecordAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        recordList = new ArrayList<>();
    }

    public void setHistoryList(List<WithdrawRecord> recordData, boolean mIsLoadMore) {
        if (recordData == null || recordData.size() == 0) return;
        if (mIsLoadMore) {
            recordList.addAll(recordData);
        } else {
            this.recordList = recordData;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecordHolder getViewHolder(View view) {
        return new RecordHolder(view);
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        return new RecordHolder(inflater.inflate(R.layout.item_withdraw_record_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecordHolder recordHolder, int position, boolean isItem) {
        WithdrawRecord itemData = recordList.get(position);
        //1.提现金额
        long amount = itemData.getAmount();
        if (amount != 0) {
            String withdrawCash = StringUtils.getCash(String.valueOf(amount), AppConstants.DIGITS);
            recordHolder.cashTV.setText(String.format(Locale.CHINA, "¥%s", withdrawCash));
        }


        //2.日期
        long created = itemData.getCreated();
        if (created != 0) {
            String time = TimeUtil.timestampConvertDate("yyyy-MM-dd HH:mm:ss", created);
            recordHolder.date.setText(time);
        }

        //3. 提现状态  2:已完成 3:申请 4:被拒绝
        int status = itemData.getStatus();
        switch (status) {
            case 2:
                recordHolder.statusTv.setText("提现完成");
                break;

            case 3:
                recordHolder.statusTv.setText("已申请");
                break;

            case 4:
                recordHolder.statusTv.setText("被拒绝");
                break;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return recordList.size();
    }


    public class RecordHolder extends RecyclerView.ViewHolder {

        private TextView cashTV;
        private TextView date;
        private TextView statusTv;

        private RecordHolder(View itemView) {
            super(itemView);
            cashTV = itemView.findViewById(R.id.tv_withdrawal_cash);
            date = itemView.findViewById(R.id.tv_withdrawal_date);
            statusTv = itemView.findViewById(R.id.tv_withdrawal_state);
        }
    }
}
