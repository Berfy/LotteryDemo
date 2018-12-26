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
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.bean.response.ResultBallBean;
import cn.zcgames.lottery.bean.response.ResultPageDataBean;
import cn.zcgames.lottery.result.view.activity.ResultHistoryNewActivity;
import cn.zcgames.lottery.view.common.refreshview.recyclerview.BaseRecyclerAdapter;
import cn.zcgames.lottery.model.local.LotteryUtils;

/**
 * Created by admin on 2017/4/28.
 * Berfy修改
 * 开奖列表适配器
 */
public class LotteryResultAdapter extends BaseRecyclerAdapter<LotteryResultAdapter.ResultHolder> implements View.OnClickListener {

    private static final String TAG = "LotteryResultAdapter";

    private List<ResultPageDataBean> mResultList;
    private Context mContext;

    public LotteryResultAdapter(Context context) {
        this.mContext = context;
        mResultList = new ArrayList<>();
    }

    public void setResultList(List<ResultPageDataBean> list) {
        if (list == null) {
            return;
        }
        this.mResultList = list;
        LogF.d(TAG, "开奖列表数量" + mResultList.size());
        notifyDataSetChanged();
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_result_list, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public ResultHolder getViewHolder(View view) {
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position, boolean isItem) {
        ResultPageDataBean bean = mResultList.get(position);
        holder.mTitleTv.setText(bean.getName());
//        holder.mDateTimeTv.setText(DateUtils.formatTime_MM_dd_E(bean.getDatetime()));
        holder.mDateTimeTv.setText(TimeUtil.format("yyyy-MM-dd HH:mm", bean.getDatetime()));
        holder.mPeriodTv.setText("第" + bean.getPeriod() + "期");
        holder.mBallView.removeAllViews();

        List<ResultBallBean> balls = bean.getBall();
        LogF.d(TAG, "开奖列表数量" + GsonUtil.getInstance().toJson(bean));
        if (null != balls) {
            //TODO 新增彩种需要适配 Y
            if (bean.getType().equals("dice")) {
                holder.mTvSum.setVisibility(View.VISIBLE);
                String[] dices = new String[0];
                for (int i = 0; i < balls.size(); i++) {
                    ResultBallBean ball = balls.get(i);
                    if (null != ball.getValue() && ball.getValue().length > 0)
                        dices = ball.getValue();
                }
                if (dices != null) {
                    int sum = 0;
                    for (int i = 0; i < dices.length; i++) {
                        sum += Integer.parseInt(dices[i]);
                        holder.mBallView.addView(LotteryUtils.createFast3BallView(mContext, Integer.parseInt(dices[i]), 40));
                    }
                    holder.mTvSum.setText("和值: " + sum);
                }
            } else {
                holder.mTvSum.setVisibility(View.GONE);
                if (bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                    try {
                        holder.mTvSum.setText(LotteryUtils.formatBigSmallSingleDouble(Integer.valueOf(balls.get(0).getValue()[3]))
                                + " | " + LotteryUtils.formatBigSmallSingleDouble(Integer.valueOf(balls.get(0).getValue()[4])));
                        holder.mTvSum.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < balls.size(); i++) {
                    ResultBallBean ball = balls.get(i);
                    for (String number : ball.getValue()) {
                        holder.mBallView.addView(LotteryUtils.createDCBallView(mContext, number, ball.getColor(), 30));
                    }
                }
            }

            holder.mParentView.setTag(bean);
            holder.mBallView.setTag(bean);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mResultList.size();
    }

    @Override
    public void onClick(View v) {
        //TODO 新增彩种需要适配 Y
        if (v.getTag() instanceof ResultPageDataBean) {
            ResultPageDataBean bean = (ResultPageDataBean) v.getTag();
            if (TextUtils.isEmpty(bean.getLott_inner_name())) {
                ToastUtil.getInstances().showShort(R.string.lottery_no);
            } else {
                if (bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_FAST_3)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_FAST_3_EASY)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)) {
                    ResultHistoryNewActivity.intoThisActivity((Activity) mContext, bean.getLott_inner_name(), bean.getName());
                } else if (bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_11_5)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
                    ResultHistoryNewActivity.intoThisActivity((Activity) mContext, bean.getLott_inner_name(), bean.getName());
                } else if (bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                        || bean.getLott_inner_name().equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                    ResultHistoryNewActivity.intoThisActivity((Activity) mContext, bean.getLott_inner_name(), bean.getName());
                } else {
                    ToastUtil.getInstances().showShort(R.string.lottery_no);
                }
            }
        }
    }

    public class ResultHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTv, mPeriodTv, mDateTimeTv, mTvSum;
        private View mParentView;
        private LinearLayout mBallView;

        public ResultHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.result_tv_title);
            mPeriodTv = (TextView) itemView.findViewById(R.id.result_tv_periodNumber);
            mDateTimeTv = (TextView) itemView.findViewById(R.id.result_tv_dateTime);
            mParentView = itemView.findViewById(R.id.result_rl_parentView);
            mTvSum = itemView.findViewById(R.id.history_tv_sum);
            mParentView.setOnClickListener(LotteryResultAdapter.this);
            mBallView = (LinearLayout) itemView.findViewById(R.id.result_ll_ballView);
            mBallView.setOnClickListener(LotteryResultAdapter.this);
        }
    }

}
