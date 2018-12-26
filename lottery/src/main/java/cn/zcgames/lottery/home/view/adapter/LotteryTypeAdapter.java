package cn.zcgames.lottery.home.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.bean.LotteryType;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.home.view.activity.LotteryOrderActivity;
import cn.zcgames.lottery.home.lottery.alwaycolor.view.activity.AlwaysColorActivity;
import cn.zcgames.lottery.home.lottery.elevenfive.view.activity.Eleven5Activity;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.countdowntimer.CountDownTimerSupport;
import cn.zcgames.lottery.utils.countdowntimer.OnCountDownTimerListener;

import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_SUM;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_STAR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_5;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_2_COLOR;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_HAPPY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_3_D;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_EASY;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_HB;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_JS;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_FAST_3_NEW;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2;

/**
 * 首页彩种适配器
 * Berfy修改
 * 2018.8.23
 */
public class LotteryTypeAdapter extends RecyclerView.Adapter<LotteryTypeAdapter.TypeViewHolder> {

    private static final String TAG = "LotteryTypeAdapter";

    private Context mContext;
    private OnLotteryDataRefreshListener mOnLotteryDataRefreshListener;
    private List<LotteryType> mLotteryTypeList;
    private SparseArray<CountDownTimerSupport> mTimerArray;
    private CountDownTimerSupport mCountDownTimerSupport;

    public LotteryTypeAdapter(Context context, OnLotteryDataRefreshListener onLotteryDataRefreshListener) {
        this.mContext = context;
        mOnLotteryDataRefreshListener = onLotteryDataRefreshListener;
        mLotteryTypeList = new ArrayList<>();
        mTimerArray = new SparseArray<>();
        startTimer();
    }

    public void setLotteryTypeList(List<LotteryType> list) {
        if (list == null) {
            return;
        }
        mLotteryTypeList = list;
//        LotteryType arrange5 = new LotteryType();
//        arrange5.setName("排列5");
//        arrange5.setUrl(list.get(0).getUrl());
//        arrange5.setTag(LOTTERY_TYPE_ARRANGE_5);
//        lotteryTypeList.add(arrange5);

        notifyDataSetChanged();
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lottery_type_tow, null);
        return new TypeViewHolder(view);
    }

    private void startTimer() {
        if (null == mCountDownTimerSupport) {
            mCountDownTimerSupport = new CountDownTimerSupport();
            setTimerMillis(600 * 1000, 1000, mCountDownTimerSupport);
            mCountDownTimerSupport.setOnCountDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onTick(long millisUntilFinished) {
                    LogF.d(TAG, "计时器" + millisUntilFinished);
                    for (LotteryType type : mLotteryTypeList) {
                        long time = Long.parseLong(TextUtils.isEmpty(type.getDraw_datetime()) ? "0" : type.getDraw_datetime());
                        if (time > 0) {//如果原始数据小于0  不启用计时
                            time -= 1000;
                            type.setStaking_countdown(time + "");
                            if (time < 0) {
                                type.setStaking_countdown("-1");
                                refresh();
                            }
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onFinish() {
                    LogF.d(TAG, "计时器完成 重新开始");
                    mCountDownTimerSupport.reset();
                    setTimerMillis(600 * 1000, 1000, mCountDownTimerSupport);
                    startTimer();
                }
            });
        }
        mCountDownTimerSupport.start();
    }

    public void stopTimer() {
        if (null != mCountDownTimerSupport) {
            mCountDownTimerSupport.stop();
            mCountDownTimerSupport = null;
        }
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position) {
        LotteryType type = mLotteryTypeList.get(position);
//        if (!TextUtils.isEmpty(type.getUrl())) {
//            Picasso.with(mContext)
//                    .load(type.getUrl())
//                    .error(R.drawable.icon_place)
//                    .placeholder(R.drawable.icon_place)
//                    .into(holder.headerIv);
        Glide.with(mContext)
                .load(type.getIcon_url())
                .error(R.drawable.icon_place)
                .placeholder(R.drawable.icon_place)
                .into(holder.headerIv);
//        } else {
//            holder.headerIv.setBackgroundResource(R.drawable.icon_place);
//        }
        holder.titleTv.setText(type.getShow());
        //清除前一个缓存
        if (null != holder.downTimer) {
            holder.downTimer.stop();
        }
        //倒计时  1黑白  暂停销售  2彩色  正常倒计时
        if (TextUtils.equals("2", type.getLottery_state())) {
            long time = Long.parseLong(TextUtils.isEmpty(type.getDraw_datetime()) ? "0" : type.getDraw_datetime());
            if (!TextUtils.isEmpty(type.getDraw_datetime()) && time > 0) {
                holder.mTVDeadLine.setVisibility(View.VISIBLE);
                CountDownTimerSupport countDownTimerSupport = mTimerArray.get(holder.mTVDeadLine.hashCode());
                holder.mTVDeadLine.setText("距截止：" + DateUtils.formatTime_MM_SS(time));
//                if (null != countDownTimerSupport) {//当前item 拥有计时器 避免重复
//                    if (!countDownTimerSupport.isFinish()) {
//                        countDownTimerSupport.stop();
//                    }
//                } else {//当前item 没有计时器
//                    countDownTimerSupport = new CountDownTimerSupport();
//                    mTimerArray.put(holder.mTVDeadLine.hashCode(), countDownTimerSupport);
//                }
//                holder.downTimer = countDownTimerSupport;
//                setTimerMillis(Long.parseLong(type.getDraw_datetime()), 1000, holder.downTimer);
//                holder.downTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        holder.mTVDeadLine.setText("距截止：" + DateUtils.formatTime_MM_SS(millisUntilFinished));
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        holder.mTVDeadLine.setText("等待开奖");
//                        resetDownTimer(holder.downTimer);
//                    }
//                });
//                holder.downTimer.start();
            } else {
                if (time == -1) {
                    holder.mTVDeadLine.setVisibility(View.VISIBLE);
                    holder.mTVDeadLine.setText("等待开奖");
                } else {
                    holder.mTVDeadLine.setVisibility(View.GONE);
                }
            }
        } else if (TextUtils.equals("1", type.getLottery_state())) {
            holder.mTVDeadLine.setVisibility(View.VISIBLE);
            holder.mTVDeadLine.setText("暂停销售");
        }

        Log.e(TAG, "onBindViewHolder: tag is " + type.getName());
        holder.parentView.setTag(type);
        if (position % 2 == 1 || position == getItemCount() - 1) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mLotteryTypeList.size();
    }

    public class TypeViewHolder extends RecyclerView.ViewHolder {

        private View parentView;
        private View vLine;
        private TextView titleTv, mTVDeadLine;
        private ImageView headerIv;
        private CountDownTimerSupport downTimer;

        public TypeViewHolder(View v) {
            super(v);
            titleTv = (TextView) v.findViewById(R.id.lottery_tv_title);
            mTVDeadLine = v.findViewById(R.id.lottery_tv_status);
            headerIv = (ImageView) v.findViewById(R.id.lottery_iv_header);
            parentView = v.findViewById(R.id.lottery_rl_parentView);
            vLine = v.findViewById(R.id.v_right_line);
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 新增彩种需要适配 Y
                    final LotteryType type = (LotteryType) v.getTag();
                    List<LotteryOrder> lotteryOrders = LotteryOrderDbUtils.listAllLotteryOrder(type.getName());
                    if (null != lotteryOrders && lotteryOrders.size() > 0) {
                        LotteryOrderActivity.intoThisActivity(mContext, type.getName(), -1);
                        ToastUtil.getInstances().showShort(R.string.lottery_order_cache_tip);
                    } else {
                        if (type.getName().equals(LOTTERY_TYPE_2_COLOR)) {//双色球)
//                            DoubleColorActivity.intoThisActivity(mContext);
                            ToastUtil.getInstances().showShort(R.string.lottery_no);
                        } else if (type.getName().equals(LOTTERY_TYPE_3_D)) {//福彩3d
//                            ThreeDActivity.intoThisActivity(mContext);
                            ToastUtil.getInstances().showShort(R.string.lottery_no);
                        } else if (type.getName().equals(LOTTERY_TYPE_FAST_3)//快三
                                || type.getName().equals(LOTTERY_TYPE_FAST_3_JS)
                                || type.getName().equals(LOTTERY_TYPE_FAST_3_HB)
                                || type.getName().equals(LOTTERY_TYPE_FAST_3_NEW)
                                || type.getName().equals(LOTTERY_TYPE_FAST_3_EASY)) {
                            FastThreeActivity.intoThisActivity((Activity) mContext, type.getName(), FAST_THREE_SUM);
                        } else if (type.getName().equals(AppConstants.LOTTERY_TYPE_11_5)
                                || type.getName().equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                                || type.getName().equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                                || type.getName().equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                                || type.getName().equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
                            Eleven5Activity.intoThisActivity((Activity) mContext, type.getName(), PLAY_11_5_ANY_2);
                        } else if (type.getName().equals(LOTTERY_TYPE_ALWAYS_COLOR)
                                || type.getName().equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                            AlwaysColorActivity.intoThisActivity((Activity) mContext, type.getName(), ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
                        } else if (type.getName().equals(LOTTERY_TYPE_7_HAPPY)) {
//                            SevenHappyActivity.intoThisActivity((Activity) mContext);
                            ToastUtil.getInstances().showShort(R.string.lottery_no);
                        } else if (type.getName().equals(LOTTERY_TYPE_ARRANGE_3)) {
//                            Arrange3Activity.intoThisActivity((Activity) mContext, ARRANGE_3_PLAY_DIRECT);
                            ToastUtil.getInstances().showShort(R.string.lottery_no);
                        } else if (type.getName().equals(LOTTERY_TYPE_ARRANGE_5)) {
//                            Arrange5Activity.intoThisActivity((Activity) mContext);
                            ToastUtil.getInstances().showShort(R.string.lottery_no);
                        } else if (type.getName().equals(LOTTERY_TYPE_7_STAR)) {
//                            SevenStarActivity.intoThisActivity((Activity) mContext);
                            ToastUtil.getInstances().showShort(R.string.lottery_no);
                        } else {
                            ToastUtil.getInstances().showShort(R.string.lottery_no);
                        }
                    }
                }
            });
        }
    }

    //取消所有定时器
    public void cancelAllCountTimers() {
        if (null == mTimerArray) {
            return;
        }
        for (int i = 0; i < mTimerArray.size(); i++) {
            CountDownTimerSupport timer = mTimerArray.get(mTimerArray.keyAt(i));
            if (null != timer) {
                timer.stop();
            }
        }
    }

    //设置倒计时的时间
    private void setTimerMillis(long totalTime, long period, CountDownTimerSupport timer) {
        timer.setMillisInFuture(totalTime);
        timer.setCountDownInterval(period);
    }

    //重新倒计时
    public void refresh() {
//        setTimerMillis(10 * 60 * 1000, 1000, timer);
//        timer.reset();
//        timer.start();
        Log.d("111111", "倒计时为0，重新获取首页数据");
        if (null != mOnLotteryDataRefreshListener) {
            mOnLotteryDataRefreshListener.onRefresh();
        }
    }

    public interface OnLotteryDataRefreshListener {
        void onRefresh();
    }
}
