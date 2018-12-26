package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.home.bean.AlwaysColorHistory;
import cn.zcgames.lottery.home.bean.FastThreeHistory;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_HAPPY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_EASY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_HB;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_JS;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_NEW;

/**
 * Created by admin on 2017/6/20.
 */

public class FastThreeHistoryAdapter extends RecyclerView.Adapter<FastThreeHistoryAdapter.HistoryViewHolder> {

    private static final String TAG = "FastThreeHistoryAdapter";

    private List<FastThreeHistory> mF3Data;
    private List<AlwaysColorHistory> mACData;
    private List<LotteryResultHistory> mSHData;
    private Context mContext;
    private View.OnClickListener mListener;
    private boolean isSumFragment = true;
    private String mLotteryType;

    public FastThreeHistoryAdapter(String lotteryType, Context context, View.OnClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mLotteryType = lotteryType;
        if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            mF3Data = new ArrayList<>();
        } else if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)) {
            mACData = new ArrayList<>();
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY) || mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            mSHData = new ArrayList<>();
        }
    }

    public void setFragmentType(boolean isSumFragment) {
        this.isSumFragment = isSumFragment;
        notifyDataSetChanged();
    }

    public void setF3Data(List<FastThreeHistory> data) {
        this.mF3Data = data;
        FastThreeHistory bean = new FastThreeHistory();
        bean.setDatetime(null);
        bean.setDetail("");
        bean.setSequence("");

        FastThreeHistory.Dice dice = new FastThreeHistory.Dice();
        dice.setSequence("期次");
        List<String> numbers = new ArrayList<String>();
        numbers.add("开奖号码");
        dice.setNumber(numbers);
        dice.setSingle_or_double("单双");
        dice.setSize("大小");
        dice.setForm("投注方式");
        dice.setSum("和值");

        bean.setDice(dice);
        mF3Data.add(0, bean);

        notifyDataSetChanged();
    }

    public void setACData(List<AlwaysColorHistory> data) {
        this.mACData = data;
        AlwaysColorHistory titleBean = new AlwaysColorHistory();
        titleBean.setAward_time("");
        titleBean.setDate("");
        List<String> numbers = new ArrayList<String>();
        numbers.add("开奖号码");
        titleBean.setNumber(numbers);
        titleBean.setSequence("期次");

        mACData.add(0, titleBean);

        notifyDataSetChanged();
    }

    public void setSHData(List<LotteryResultHistory> data) {
        this.mSHData = data;
        LotteryResultHistory titleBean = new LotteryResultHistory();
        titleBean.setSequence("期次");
        mSHData.add(0, titleBean);
        notifyDataSetChanged();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_seven_happy, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_fast_three, parent, false);
        }
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            FastThreeHistory bean = mF3Data.get(position);
            holder.bigOrSmallTv.setText(bean.getDice().getSize());
            holder.singleOrDoubleTv.setText(bean.getDice().getSingle_or_double());
            List<String> numbers = bean.getDice().getNumber();
            holder.numberLl.removeAllViews();
            String sequence = bean.getDice().getSequence();
            if (!sequence.equals("期次")) {
                sequence = sequence + "期";
            }
            holder.sequenceTv.setText(sequence);

            for (String number : numbers) {
                if (!number.equals("开奖号码")) {
                    holder.numberLl.addView(LotteryUtils.createFast3BallView(mContext, Integer.parseInt(number), 30));
                }
            }
            for (String number : numbers) {
                holder.numberLl.addView(LotteryUtils.createTextView(mContext, number));
            }
            holder.parentView.setTag(bean);

            if (isSumFragment) {
                holder.playTypeTv.setText(bean.getDice().getSum());
                UIHelper.showWidget(holder.bigOrSmallTv, true);
                UIHelper.showWidget(holder.singleOrDoubleTv, true);
            } else {
                holder.playTypeTv.setText(bean.getDice().getForm());
                UIHelper.showWidget(holder.bigOrSmallTv, false);
                UIHelper.showWidget(holder.singleOrDoubleTv, false);
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)) {
            AlwaysColorHistory bean = mACData.get(position);
            List<String> numbers = bean.getNumber();
            String sequence = bean.getSequence();
            if (!sequence.equals("期次")) {
                sequence = sequence + "期";
            }
            holder.sequenceTv.setText(sequence);

            String numberStr = "";
            for (String number : numbers) {
                numberStr = numberStr + " " + number;
            }
            if (numberStr.contains("开奖号码")) {
                holder.playTypeTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_666666));
            } else {
                holder.playTypeTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_red_ball));
            }
            holder.playTypeTv.setText(numberStr);
            holder.parentView.setTag(bean);
//            if (isSumFragment) {
//                holder.playTypeTv.setText(bean.getDice().getSum());
//                UIHelper.showWidget(holder.bigOrSmallTv, true);
//                UIHelper.showWidget(holder.singleOrDoubleTv, true);
//            } else {
//                holder.playTypeTv.setText(bean.getDice().getForm());
//                UIHelper.showWidget(holder.bigOrSmallTv, false);
//                UIHelper.showWidget(holder.singleOrDoubleTv, false);
//            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY) || mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            LotteryResultHistory bean = mSHData.get(position);
            List<LotteryResultHistory.DoubleColorNumber> balls = bean.getBall();
            String sequence = bean.getSequence();
            if (!sequence.equals("期次")) {
                sequence = sequence + "期";
                String[] redNumbers = null, blueNumbers = null;
                for (LotteryResultHistory.DoubleColorNumber ball : balls) {
                    if (ball.getColor().equals("DC143C") || ball.getColor().equals("#cc0033")) {
                        redNumbers = ball.getValue();
                    } else {
                        blueNumbers = ball.getValue();
                    }
                }
                holder.playTypeTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_red_ball));
                SpannableString ss = LotteryUtils.getSpannableNormalString(redNumbers, blueNumbers);
                holder.playTypeTv.setText(ss);
            } else {
                holder.playTypeTv.setText("开奖号码");
                holder.playTypeTv.setTextColor(StaticResourceUtils.getColorResourceById(R.color.color_666666));
            }
            holder.sequenceTv.setText(sequence);
            holder.parentView.setTag(bean);
        }

        if (position % 2 != 0) {
            holder.parentView.setBackgroundResource(R.color.color_FFFFFF);
        } else {
            holder.parentView.setBackgroundResource(R.color.color_app_bg);
        }

    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            size = mF3Data.size();
        } else if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)) {
            size = mACData.size();
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY) || mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            size = mSHData.size();
        }
        return size;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView sequenceTv;
        LinearLayout numberLl;
        TextView playTypeTv;
        TextView bigOrSmallTv;
        TextView singleOrDoubleTv;
        RelativeLayout parentView;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            if (!mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
                numberLl = (LinearLayout) itemView.findViewById(R.id.numberTv);
                bigOrSmallTv = (TextView) itemView.findViewById(R.id.bigOrSmallTv);
                singleOrDoubleTv = (TextView) itemView.findViewById(R.id.singleOrDoubleTv);
            }
            sequenceTv = (TextView) itemView.findViewById(R.id.sequenceTv);
            playTypeTv = (TextView) itemView.findViewById(R.id.playTypeTv);
            parentView = (RelativeLayout) itemView.findViewById(R.id.history_rl_parentView);
            parentView.setOnClickListener(mListener);
        }
    }

}
