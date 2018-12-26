package cn.zcgames.lottery.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.home.bean.DoubleColorOrderBet;
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_GROUP_SIX;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_GROUP_THREE;
import static cn.zcgames.lottery.app.AppConstants.DC_PLAY_STYLE_DANTUO;
import static cn.zcgames.lottery.app.AppConstants.DC_PLAY_STYLE_NORMAL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_2_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_2_SAME_MORE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_2_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_3_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_3_SAME_ALL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_3_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_3_TO_ALL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_ORDER_TYPE_SUM;
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
import static cn.zcgames.lottery.app.AppConstants.TD_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.TD_PLAY_GROUP_SIX;
import static cn.zcgames.lottery.app.AppConstants.TD_PLAY_GROUP_THREE;

/**
 * Created by admin on 2017/5/22.
 */

public class DetailChaseOrderAdapter extends RecyclerView.Adapter<DetailChaseOrderAdapter.BetViewHolder> {
    private static final String TAG = "DetailOrderAdapter";

    public static final String ADAPTER_ITEM_TYPE = "LOTTERY_RESULT_NUMBER";

    private List<DoubleColorOrderBet> mDataList;
    private Context mContext;
    private String mLotteryType;

    public DetailChaseOrderAdapter(List<DoubleColorOrderBet> list, Context context, String lotteryType) {
        this.mDataList = list;
        this.mContext = context;
        this.mLotteryType = lotteryType;
    }

    @Override
    public BetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chase_detail, parent, false);
        return new BetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BetViewHolder holder, int position) {
        showData(position, holder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private void showData(int position, BetViewHolder holder) {
        DoubleColorOrderBet bean = mDataList.get(position);

        String title = "";
        String numStr = "";
        long zhushu;

        int blueSize = 0;
        if (bean.getBlue() != null) {
            blueSize = bean.getBlue().size();
            Collections.sort(bean.getBlue());
        }
        String playMode = bean.getMode();
        if (playMode.equals(ADAPTER_ITEM_TYPE)) {
            int redSize = bean.getRed().size();
            title = "开奖号码";
            Collections.sort(bean.getRed());
            for (int x = 0; x < redSize; x++) {
                if (x == 0) {
                    numStr = bean.getRed().get(x);
                } else {
                    numStr = numStr + "  " + bean.getRed().get(x);
                }
            }
            for (int x = 0; x < blueSize; x++) {
                numStr = numStr + "  " + bean.getBlue().get(x);
            }
        }
        //TODO 新增彩种需要适配 Y
        if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR) || mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW) || mLotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
            int modeInt = -1;
            switch (playMode) {
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_5_ALL:
                    title = "五星通选";
                    modeInt = AppConstants.ALWAYS_COLOR_5_ALL;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_BIG_SMALL_SINGLE_DOUBLE:
                    title = "大小单双";
                    modeInt = AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_1_DIRECT:
                    title = "一星直选";
                    modeInt = AppConstants.ALWAYS_COLOR_1_DIRECT;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_2_DIRECT:
                    title = "二星直选";
                    modeInt = AppConstants.ALWAYS_COLOR_2_DIRECT;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_2_GROUP:
                    title = "二星组选";
                    modeInt = AppConstants.ALWAYS_COLOR_2_GROUP;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_3_DIRECT:
                    title = "三星直选";
                    modeInt = AppConstants.ALWAYS_COLOR_3_DIRECT;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_3_GROUP_3:
                    title = "三星组三";
                    modeInt = AppConstants.ALWAYS_COLOR_3_GROUP_3;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_3_GROUP_6:
                    title = "三星组六";
                    modeInt = AppConstants.ALWAYS_COLOR_3_GROUP_6;
                    break;
                case AppConstants.ALWAYS_COLOR_ORDER_TYPE_5_DIRECT:
                    title = "五星直选";
                    modeInt = AppConstants.ALWAYS_COLOR_5_DIRECT;
                    break;
                case AppConstants.A5_PLAY_DIRECT:
                    title = "投注号码";
                    modeInt = AppConstants.ARRANGE_5_PLAY_DIRECT;
                    break;
            }
            if (!playMode.equals(ADAPTER_ITEM_TYPE)) {
                numStr = LotteryUtils.getStringByStringList(bean.getRed(), bean.getBlue(),
                        bean.getDan(), bean.getTuo(), bean.getFive(), null, null);
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {
            title = "投注号码";
            if (!playMode.equals(ADAPTER_ITEM_TYPE)) {
                numStr = LotteryUtils.getStringByStringList(bean.getRed(), bean.getBlue(),
                        bean.getDan(), bean.getTuo(), bean.getFive(), bean.getSix(), bean.getSeven());
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
            int redSize = bean.getRed().size();
            zhushu = CalculateCountUtils.calculateSevenCount(redSize);
            title = "普通" + zhushu + "注";
            Collections.sort(bean.getRed());
            for (int x = 0; x < redSize; x++) {
                if (x == 0) {
                    numStr = bean.getRed().get(x);
                } else {
                    numStr = numStr + "  " + bean.getRed().get(x);
                }
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
            if (playMode.equals(DC_PLAY_STYLE_NORMAL)) {
                int redSize = bean.getRed().size();
                zhushu = CalculateCountUtils.doubleColorOrdinary(redSize, blueSize);
                title = "普通" + zhushu + "注";
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    if (x == 0) {
                        numStr = bean.getRed().get(x);
                    } else {
                        numStr = numStr + "  " + bean.getRed().get(x);
                    }
                }
                for (int x = 0; x < blueSize; x++) {
                    numStr = numStr + "  " + bean.getBlue().get(x);
                }

            } else if (playMode.equals(DC_PLAY_STYLE_DANTUO)) {
                int danSize = bean.getDan().size();
                int tuoSize = bean.getTuo().size();
                zhushu = CalculateCountUtils.doubleColorDantuo(danSize, tuoSize, blueSize);
                title = "胆拖" + zhushu + "注";

                Collections.sort(bean.getDan());
                Collections.sort(bean.getTuo());
                for (int x = 0; x < danSize; x++) {
                    if (x == 0) {
                        numStr = "(" + bean.getDan().get(x);
                    } else if (x == danSize - 1) {
                        numStr = numStr + "  " + bean.getDan().get(x) + ") ";
                    } else {
                        numStr = numStr + "  " + bean.getDan().get(x);
                    }
                }
                if (bean.getTuo() != null) {
                    for (int x = 0; x < tuoSize; x++) {
                        numStr = numStr + "  " + bean.getTuo().get(x);
                    }
                }
                for (int x = 0; x < blueSize; x++) {
                    numStr = numStr + "  " + bean.getBlue().get(x);
                }
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_3_D) || mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            if (playMode.equals(TD_PLAY_GROUP_SIX) || playMode.equals(ARRANGE_3_PLAY_GROUP_SIX)) {
                title = "组选六";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    if (x == 0) {
                        numStr = bean.getRed().get(x);
                    } else {
                        numStr = numStr + ", " + bean.getRed().get(x);
                    }
                }
            } else if (playMode.equals(TD_PLAY_GROUP_THREE) || playMode.equals(ARRANGE_3_PLAY_GROUP_THREE)) {
                title = "组选三";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    if (x == 0) {
                        numStr = bean.getRed().get(x);
                    } else {
                        numStr = numStr + ", " + bean.getRed().get(x);
                    }
                }
            } else if (playMode.equals(TD_PLAY_DIRECT) || playMode.equals(ARRANGE_3_PLAY_DIRECT)) {
                title = "直选";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    if (x == 0) {
                        numStr = bean.getRed().get(x);
                    } else {
                        numStr = numStr + bean.getRed().get(x);
                    }
                }
                int danSize = bean.getDan().size();
                Collections.sort(bean.getDan());
                for (int x = 0; x < danSize; x++) {
                    if (x == 0) {
                        numStr = numStr + ", " + bean.getDan().get(x);
                    } else {
                        numStr = numStr + bean.getDan().get(x);
                    }
                }
                if (bean.getTuo() != null) {
                    int tuoSize = bean.getTuo().size();
                    Collections.sort(bean.getTuo());
                    for (int x = 0; x < tuoSize; x++) {
                        if (x == 0) {
                            numStr = numStr + ", " + bean.getTuo().get(x);
                        } else {
                            numStr = numStr + bean.getTuo().get(x);
                        }
                    }
                }
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            if (playMode.equals(FAST_THREE_ORDER_TYPE_2_DIFFERENT)) {
                title = "二不同号";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    if (x == 0) {
                        numStr = bean.getRed().get(x);
                    } else {
                        numStr = numStr + ", " + bean.getRed().get(x);
                    }
                }
            } else if (playMode.equals(FAST_THREE_ORDER_TYPE_SUM)) {
                title = "和值";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    if (x == 0) {
                        numStr = bean.getRed().get(x);
                    } else {
                        numStr = numStr + ", " + bean.getRed().get(x);
                    }
                }
            } else if (playMode.equals(FAST_THREE_ORDER_TYPE_2_SAME_MORE)) {
                title = "二同号复选";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    String beanStr = bean.getRed().get(x) + bean.getRed().get(x);
                    if (x == 0) {
                        numStr = beanStr;
                    } else {
                        numStr = numStr + ", " + beanStr;
                    }
                }
            } else if (playMode.equals(FAST_THREE_ORDER_TYPE_2_SAME_ONE)) {
                title = "二同号单选";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int i = 0; i < redSize; i++) {
                    String beanStr = bean.getRed().get(i) + bean.getRed().get(i);
                    if (i == 0) {
                        numStr = beanStr;
                    } else {
                        numStr = numStr + ", " + beanStr;
                    }
                }
                if (blueSize == 1) {
                    numStr = numStr + " (" + bean.getBlue().get(0) + ")";
                } else {
                    for (int i = 0; i < blueSize; i++) {
                        String beanStr = bean.getBlue().get(i);
                        if (i == 0) {
                            numStr = numStr + " (" + beanStr;
                        } else if (i == blueSize - 1) {
                            numStr = numStr + ", " + beanStr + ")";
                        } else {
                            numStr = numStr + ", " + beanStr;
                        }
                    }
                }
            } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_DIFFERENT)) {
                title = "三不同号";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());
                for (int x = 0; x < redSize; x++) {
                    if (x == 0) {
                        numStr = bean.getRed().get(x);
                    } else {
                        numStr = numStr + ", " + bean.getRed().get(x);
                    }
                }
            } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_SAME_ALL)) {
                title = "三同号通选";
                numStr = "111, 222, 333, 444, 555, 666";
            } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_SAME_ONE)) {
                title = "三同号单选";
                int redSize = bean.getRed().size();
                Collections.sort(bean.getRed());

                for (int i = 0; i < redSize; i++) {
                    String beanStr = bean.getRed().get(i);
                    String realNumber = beanStr + beanStr + beanStr;
                    if (i == 0) {
                        numStr = realNumber;
                    } else {
                        numStr = numStr + ", " + realNumber;
                    }
                }
            } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_TO_ALL)) {
                title = "三连号通选";
                numStr = "123, 234, 345, 456";
            }
        } else {

        }

        if (!mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                && !mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                && !mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                && !mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                && !mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)
                && !mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                && !mLotteryType.equals(LOTTERY_TYPE_ARRANGE_5) && !mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {
            int blueLength = blueSize * 4;
            int length = numStr.length();
            int startIdx = length - blueLength;
            SpannableString ss = new SpannableString(numStr);
            ss.setSpan(new ForegroundColorSpan(StaticResourceUtils.getColorResourceById(R.color.color_blue_ball)),
                    startIdx, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(StaticResourceUtils.getColorResourceById(R.color.color_red_ball)),
                    0, startIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.numberTv.setText(ss);
        } else {
            holder.numberTv.setText(numStr);
        }

        holder.titleTv.setText(title);

        if (position % 2 == 1) {
            holder.parentView.setBackgroundResource(R.color.color_FFFFFF);
        } else {
            holder.parentView.setBackgroundResource(R.color.color_app_bg);
        }

    }

    public class BetViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private TextView numberTv;
        private View parentView;

        public BetViewHolder(View itemView) {
            super(itemView);
            numberTv = (TextView) itemView.findViewById(R.id.ball_dan);
            titleTv = (TextView) itemView.findViewById(R.id.title);
            parentView = itemView.findViewById(R.id.parentView_ll);
        }
    }
}
