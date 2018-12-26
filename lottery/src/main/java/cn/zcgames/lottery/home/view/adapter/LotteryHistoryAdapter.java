package cn.zcgames.lottery.home.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.ResultHistoryListData;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.result.view.activity.ResultDetailActivity;
import cn.zcgames.lottery.view.common.refreshview.recyclerview.BaseRecyclerAdapter;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;

/**
 * 彩种近期开奖记录列表适配
 *
 * @author Berfy
 * 2018.8.23
 */
public class LotteryHistoryAdapter extends BaseRecyclerAdapter<LotteryHistoryAdapter.HistoryViewHolder> implements View.OnClickListener {

    private static final String TAG = "LotteryHistoryAdapter";
    private Context mContext;
    private List<ResultHistoryListData.HistoryList> mFastThree = new ArrayList<>();
    private String mLotteryType;

    public LotteryHistoryAdapter(Context context, String type) {
        this.mContext = context;
        this.mLotteryType = type;
    }

    public void setData(List<ResultHistoryListData.HistoryList> list, boolean isLoadMore) {
        if (list == null) {
            return;
        }
        if (isLoadMore) {
            this.mFastThree.addAll(list);
        } else {
            this.mFastThree = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_double_color_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public HistoryViewHolder getViewHolder(View view) {
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position, boolean isItem) {
        //TODO 新增彩种需要适配
        if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_EASY)) {
            ResultHistoryListData.HistoryList bean = mFastThree.get(position);
            holder.titleTv.setText("第" + bean.getPeriod() + "期");
            holder.timeTv.setText(TimeUtil.format("yyyy-MM-dd HH:mm", bean.getDraw_time()));
            holder.ballView.removeAllViews();
            holder.sumTv.setText("和值: " + bean.getSum());
            UIHelper.showWidget(holder.sumTv, true);
            List<String> numbers = bean.getNumbers();
            for (String number : numbers) {
                holder.ballView.addView(LotteryUtils.createFast3BallView(mContext, Integer.parseInt(number), 30));
            }
            holder.parentView.setTag(bean);
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
            ResultHistoryListData.HistoryList bean = mFastThree.get(position);
            holder.titleTv.setText("第" + bean.getPeriod() + "期");
            holder.timeTv.setText(TimeUtil.format("yyyy-MM-dd HH:mm", bean.getDraw_time()));
            holder.ballView.removeAllViews();
            UIHelper.showWidget(holder.sumTv, false);
            List<String> numbers = bean.getNumbers();
            for (String number : numbers) {
                holder.ballView.addView(LotteryUtils.createDCBallView(mContext, number, "dd3048", 30));
            }
            holder.parentView.setTag(bean);
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            ResultHistoryListData.HistoryList bean = mFastThree.get(position);
            holder.titleTv.setText("第" + bean.getPeriod() + "期");
            holder.timeTv.setText(TimeUtil.format("yyyy-MM-dd HH:mm", bean.getDraw_time()));
            holder.ballView.removeAllViews();
            List<String> numbers = bean.getNumbers();
            for (String number : numbers) {
                holder.ballView.addView(LotteryUtils.createDCBallView(mContext, number, "dd3048", 30));
            }
            holder.parentView.setTag(bean);
            try {
                holder.sumTv.setText(LotteryUtils.formatBigSmallSingleDouble(Integer.valueOf(bean.getNumbers().get(3)))
                        + " | " + LotteryUtils.formatBigSmallSingleDouble(Integer.valueOf(bean.getNumbers().get(4))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            UIHelper.showWidget(holder.sumTv, true);
        }
    }

    private void showBallView(HistoryViewHolder holder, LotteryResultHistory bean) {
        List<LotteryResultHistory.DoubleColorNumber> balls = bean.getBall();
        for (int i = 0; i < balls.size(); i++) {
            LotteryResultHistory.DoubleColorNumber ball = balls.get(i);
            for (String number : ball.getValue()) {
                holder.ballView.addView(LotteryUtils.createDCBallView(mContext, number, ball.getColor(), 30));
            }
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mFastThree.size();
    }

    @Override
    public void onClick(View v) {
        Object obj = v.getTag();
        if (obj == null) {
            return;
        }
        String jsonStr = GsonUtils.getGsonInstance().toJson(obj);
        //TODO 新增彩种需要适配 Y
        if (!TextUtils.isEmpty(jsonStr)) {
            if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_EASY)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)) {
//                ResultDetailFastThreeActivity.intoThisActivity((Activity) mContext, mLotteryType, jsonStr);
                fomatNew(obj, jsonStr);
            } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
                fomatNew(obj, jsonStr);
            } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                    || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                fomatNew(obj, jsonStr);
            } else {
                ResultDetailActivity.intoThisActivity((Activity) mContext, mLotteryType, jsonStr);
            }
        }
    }

    private void fomatNew(Object obj, String jsonStr) {
        //新格式转换成 老的格式
        if (obj instanceof ResultHistoryListData.HistoryList) {
            ResultHistoryListData.HistoryList bean
                    = GsonUtil.getInstance().toClass(jsonStr, ResultHistoryListData.HistoryList.class);
            if (null != bean) {
                LotteryResultHistory lotteryResultHistory = new LotteryResultHistory();
                lotteryResultHistory.setSequence(bean.getPeriod());
                lotteryResultHistory.setDatetime(TimeUtil.format("yyyy-MM-dd HH:mm", bean.getDraw_time()));
                List<LotteryResultHistory.DoubleColorNumber> doubleColorNumbers = new ArrayList<>();
                LotteryResultHistory.DoubleColorNumber red = new LotteryResultHistory.DoubleColorNumber();
                red.setColor("dd3048");
                String[] balls = new String[bean.getNumbers().size()];
                bean.getNumbers().toArray(balls);
                red.setValue(balls);
                red.setSum(bean.getSum());
                doubleColorNumbers.add(red);
                lotteryResultHistory.setBall(doubleColorNumbers);
                ResultDetailActivity.intoThisActivity((Activity) mContext,
                        mLotteryType, GsonUtil.getInstance().toJson(lotteryResultHistory));
            }
        } else {
            ResultDetailActivity.intoThisActivity((Activity) mContext, mLotteryType, jsonStr);
        }
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv;
        private TextView timeTv;
        private LinearLayout ballView;
        private View parentView;
        private TextView sumTv;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.hisotry_tv_title);
            timeTv = (TextView) itemView.findViewById(R.id.hisotry_tv_time);
            ballView = (LinearLayout) itemView.findViewById(R.id.history_ll_ballView);
            parentView = itemView.findViewById(R.id.history_rl_doubleColor);
            parentView.setOnClickListener(LotteryHistoryAdapter.this);
            sumTv = (TextView) itemView.findViewById(R.id.history_tv_sum);
        }
    }
}
