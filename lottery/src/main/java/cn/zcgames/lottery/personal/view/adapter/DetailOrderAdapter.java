package cn.zcgames.lottery.personal.view.adapter;

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

/**
 * 投注详情adapter
 *
 * @author NorthStar
 * @date 2018/8/20 18:16
 */
public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.BetViewHolder> {
    private static final String TAG = "DetailOrderAdapter";

    public static final String ADAPTER_ITEM_TYPE = "LOTTERY_RESULT_NUMBER";

    private List<DoubleColorOrderBet> mDataList;
    private Context mContext;
    private String mLotteryType;

    public DetailOrderAdapter(List<DoubleColorOrderBet> list, Context context, String lotteryType) {
        this.mDataList = list;
        this.mContext = context;
        this.mLotteryType = lotteryType;
    }

    @Override
    public BetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_simple_detail, parent, false);
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
        long num;

        int blueSize = 0;
        if (bean.getBlue() != null) {
            blueSize = bean.getBlue().size();
            Collections.sort(bean.getBlue());
        }
        String playMode = bean.getMode();
        if (playMode.equals(ADAPTER_ITEM_TYPE)) {
            title = "开奖号码";
            int redSize = bean.getRed().size();
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
        if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR) || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW) || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ARRANGE_5)) {
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
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_7_STAR)) {
            title = "投注号码";
            if (!playMode.equals(ADAPTER_ITEM_TYPE)) {
                numStr = LotteryUtils.getStringByStringList(bean.getRed(), bean.getBlue(),
                        bean.getDan(), bean.getTuo(), bean.getFive(), bean.getSix(), bean.getSeven());
            }
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_7_HAPPY)) {
            int redSize = bean.getRed().size();
            num = CalculateCountUtils.calculateSevenCount(redSize);
            title = "普通" + num + "注";
            Collections.sort(bean.getRed());
            for (int x = 0; x < redSize; x++) {
                if (x == 0) {
                    numStr = bean.getRed().get(x);
                } else {
                    numStr = numStr + "  " + bean.getRed().get(x);
                }
            }
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_2_COLOR)) {
            if (playMode.equals(AppConstants.DC_PLAY_STYLE_NORMAL)) {
                int redSize = bean.getRed().size();
                num = CalculateCountUtils.doubleColorOrdinary(redSize, blueSize);
                title = "普通" + num + "注";
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

            } else if (playMode.equals(AppConstants.DC_PLAY_STYLE_DANTUO)) {
                int danSize = bean.getDan().size();
                int tuoSize = bean.getTuo().size();
                num = CalculateCountUtils.doubleColorDantuo(danSize, tuoSize, blueSize);
                title = "胆拖" + num + "注";

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
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_3_D) || mLotteryType.equals(AppConstants.LOTTERY_TYPE_ARRANGE_3)) {
            if (playMode.equals(AppConstants.TD_PLAY_GROUP_SIX) || playMode.equals(AppConstants.ARRANGE_3_PLAY_GROUP_SIX)) {
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
            } else if (playMode.equals(AppConstants.TD_PLAY_GROUP_THREE) || playMode.equals(AppConstants.ARRANGE_3_PLAY_GROUP_THREE)) {
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
            } else if (playMode.equals(AppConstants.TD_PLAY_DIRECT) || playMode.equals(AppConstants.ARRANGE_3_PLAY_DIRECT)) {
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
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_EASY)) {
            switch (playMode) {
                case AppConstants.FAST_THREE_ORDER_TYPE_2_DIFFERENT: {
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
                    break;
                }
                case AppConstants.FAST_THREE_ORDER_TYPE_SUM: {
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
                    break;
                }
                case AppConstants.FAST_THREE_ORDER_TYPE_2_SAME_MORE: {
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
                    break;
                }
                case AppConstants.FAST_THREE_ORDER_TYPE_2_SAME_ONE: {
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
                    break;
                }
                case AppConstants.FAST_THREE_ORDER_TYPE_3_DIFFERENT: {
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
                    break;
                }
                case AppConstants.FAST_THREE_ORDER_TYPE_3_SAME_ALL:
                    title = "三同号通选";
                    numStr = "111, 222, 333, 444, 555, 666";
                    break;
                case AppConstants.FAST_THREE_ORDER_TYPE_3_SAME_ONE: {
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
                    break;
                }
                case AppConstants.FAST_THREE_ORDER_TYPE_3_TO_ALL:
                    title = "三连号通选";
                    numStr = "123, 234, 345, 456";
                    break;
            }
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
            switch (Integer.valueOf(playMode)) {
                case AppConstants.PLAY_11_5_FRONT_1_DIRECT:
                    title = "前一直选";
                    break;
                case AppConstants.PLAY_11_5_ANY_2:
                    title = "任选二";
                    break;
                case AppConstants.PLAY_11_5_FRONT_2_GROUP:
                    title = "前二组选";
                    break;
                case AppConstants.PLAY_11_5_ANY_3:
                    title = "任选三";
                    break;
                case AppConstants.PLAY_11_5_FRONT_3_GROUP:
                    title = "前三组选";
                    break;
                case AppConstants.PLAY_11_5_ANY_4:
                    title = "任选四";
                    break;
                case AppConstants.PLAY_11_5_ANY_5:
                    title = "任选五";
                    break;
                case AppConstants.PLAY_11_5_ANY_6:
                    title = "任选六";
                    break;
                case AppConstants.PLAY_11_5_ANY_7:
                    title = "任选六";
                    break;
                case AppConstants.PLAY_11_5_ANY_8:
                    title = "任选六";
                    break;
                case AppConstants.PLAY_11_5_FRONT_2_DIRECT:
                    title = "前二直选";
                    break;
                case AppConstants.PLAY_11_5_FRONT_3_DIRECT:
                    title = "前三直选";
                    break;
                case AppConstants.PLAY_11_5_ANY_2_DAN:
                    title = "任选二-胆拖";
                    break;
                case AppConstants.PLAY_11_5_ANY_3_DAN:
                    title = "任选三-胆拖";
                    break;
                case AppConstants.PLAY_11_5_ANY_4_DAN:
                    title = "任选四-胆拖";
                    break;
                case AppConstants.PLAY_11_5_ANY_5_DAN:
                    title = "任选五-胆拖";
                    break;
                case AppConstants.PLAY_11_5_ANY_6_DAN:
                    title = "任选六-胆拖";
                    break;
                case AppConstants.PLAY_11_5_ANY_7_DAN:
                    title = "任选七-胆拖";
                    break;
                case AppConstants.PLAY_11_5_ANY_8_DAN:
                    title = "任选八-胆拖";
                    break;
            }
            int orderType = 0;//bean.getOrderType() 11选5 类型(0.复试, 1.单式)
            if (orderType == AppConstants.DOUBLE_COLOR_ORDER_TYPE_FUSHI) {
                title += " " + StaticResourceUtils.getStringResourceById(R.string.lottery_fushi);
            } else {
                title += " " + StaticResourceUtils.getStringResourceById(R.string.lottery_danshi);
            }

            StringBuilder number_11_5 = new StringBuilder();
            List<String> wanBallList = bean.getRed();
            switch (Integer.valueOf(playMode)) {
                case AppConstants.PLAY_11_5_ANY_2:
                case AppConstants.PLAY_11_5_ANY_3:
                case AppConstants.PLAY_11_5_ANY_4:
                case AppConstants.PLAY_11_5_ANY_5:
                case AppConstants.PLAY_11_5_ANY_6:
                case AppConstants.PLAY_11_5_ANY_7:
                case AppConstants.PLAY_11_5_ANY_8:
                case AppConstants.PLAY_11_5_FRONT_1_DIRECT:
                case AppConstants.PLAY_11_5_FRONT_2_GROUP:
                case AppConstants.PLAY_11_5_FRONT_3_GROUP:
                    if (null != wanBallList) {
                        for (String num1 : wanBallList) {
                            number_11_5.append(num1);
                            number_11_5.append(",");
                        }
                        if (number_11_5.length() > 0) {
                            number_11_5.delete(number_11_5.length() - 1, number_11_5.length());
                        }
                    }
                    break;
                case AppConstants.PLAY_11_5_FRONT_2_DIRECT:
                    if (null != wanBallList) {
                        for (String num2 : wanBallList) {
                            number_11_5.append(num2);
                            number_11_5.append(" ");
                        }
                        number_11_5.append("| ");
                    }
                    if (null != wanBallList) {
                        for (String num3 : wanBallList) {
                            number_11_5.append(num3);
                            number_11_5.append(" ");
                        }
                    }
                    break;
                case AppConstants.PLAY_11_5_FRONT_3_DIRECT:
                    if (null != wanBallList) {
                        for (String num4 : wanBallList) {
                            number_11_5.append(num4);
                            number_11_5.append(" ");
                        }
                        number_11_5.append("| ");
                    }
                    if (null != wanBallList) {
                        for (String num5 : wanBallList) {
                            number_11_5.append(num5);
                            number_11_5.append(" ");
                        }
                        number_11_5.append("| ");
                    }
                    if (null != wanBallList) {
                        for (String num6 : wanBallList) {
                            number_11_5.append(num6);
                            number_11_5.append(" ");
                        }
                    }
                    break;
                case AppConstants.PLAY_11_5_ANY_2_DAN:
                case AppConstants.PLAY_11_5_ANY_3_DAN:
                case AppConstants.PLAY_11_5_ANY_4_DAN:
                case AppConstants.PLAY_11_5_ANY_5_DAN:
                case AppConstants.PLAY_11_5_ANY_6_DAN:
                case AppConstants.PLAY_11_5_ANY_7_DAN:
                case AppConstants.PLAY_11_5_ANY_8_DAN:
                case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
                case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                    if (null != wanBallList) {
                        number_11_5.append("(");
                        for (String num7 : wanBallList) {
                            number_11_5.append(num7);
                        }
                        number_11_5.append(")");
                        number_11_5.append(" ");
                    }
                    if (null != wanBallList) {
                        for (String num8 : wanBallList) {
                            number_11_5.append(num8);
                            number_11_5.append(" ");
                        }
                    }
                    break;
            }
            numStr = number_11_5.toString();
        }

        if (!mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3)
                && !mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_JS)
                && !mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_HB)
                && !mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_NEW)
                && !mLotteryType.equals(AppConstants.LOTTERY_TYPE_FAST_3_EASY)
                && !mLotteryType.equals(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR)
                && !mLotteryType.equals(AppConstants.LOTTERY_TYPE_ARRANGE_5)) {
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
    }

    public class BetViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private TextView numberTv;

        public BetViewHolder(View itemView) {
            super(itemView);
            numberTv = itemView.findViewById(R.id.ball_dan);
            titleTv = itemView.findViewById(R.id.title);
        }
    }
}
