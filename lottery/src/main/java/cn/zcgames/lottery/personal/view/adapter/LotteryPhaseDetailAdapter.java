package cn.zcgames.lottery.personal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.bean.StakesBean;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.*;

/**
 * 追期详情的adapter
 *
 * @author NorthStar
 * @date 2018/8/20 18:16
 */
public class LotteryPhaseDetailAdapter extends RecyclerView.Adapter<LotteryPhaseDetailAdapter.BetViewHolder> {
    private static final String TAG = "FastThreeDetailAdapter";

    private List<StakesBean> mDataList;
    private Context mContext;
    private String mLotteryName;
    private List<String> numList;

    public LotteryPhaseDetailAdapter(List<StakesBean> list, Context context, String mLotteryName) {
        this.mDataList = list;
        this.mContext = context;
        this.mLotteryName = mLotteryName;
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
        StakesBean bean = mDataList.get(position);
        String title = "";
        StringBuilder numStr = new StringBuilder();
        int blueSize = 0;
        String playMode = bean.getGamePlay();//投注玩法
        //        int status = bean.getStatus();
        //设置中奖投注号个号颜色
        //        holder.numberTv.setTextColor(CommonUtil.getColor(mContext, status == 2 ?
        //                R.color.color_red : R.color.color_333333));

        numList = bean.getNumbers();
        if (playMode.equals(FAST_THREE_ORDER_TYPE_SUM)) {
            title = "和值";//猜中开奖号相加的和
            numStr = new StringBuilder(String.valueOf(String.valueOf(bean.getSum())));
        } else if (playMode.equals(FAST_THREE_ORDER_TYPE_2_DIFFERENT)) {
            title = "二不同号";//猜中开奖中不相同的2个号
            int redSize = numList.size();
            Collections.sort(numList);
            for (int i = 0; i < redSize; i++) {
                if (i == 0) {
                    numStr = new StringBuilder(String.valueOf(numList.get(i)));
                } else {
                    numStr.append(", ").append(numList.get(i));
                }
            }
        } else if (playMode.equals(FAST_THREE_ORDER_TYPE_2_SAME_MORE)) {
            title = "二同号复选";//猜中开奖中相同的2个号
            String num = numList.get(0);
            numStr.append(num).append(num);
        } else if (playMode.equals(FAST_THREE_ORDER_TYPE_2_SAME_ONE)) {
            title = "二同号单选";//猜中3个号(有2个号相同)
            String singleStr;//单号
            String doubleStr;//同号
            int size = numList.size();
            if (size == 3) {
                boolean isFirstDiff = !numList.get(0)
                        .equals(numList.get(1));//不同号是否在第一位，否则在最后
                if (isFirstDiff) {
                    singleStr = "(" + numList.get(0) + ")";
                    doubleStr = numList.get(1) + numList.get(2);
                } else {
                    doubleStr = numList.get(0) + numList.get(1);
                    singleStr = "(" + numList.get(2) + ")";
                }
                LogF.d(TAG, "singleStr=>" + singleStr);
                numStr.append(doubleStr).append(singleStr);
            }
        } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_DIFFERENT)) {
            title = "三不同号";//猜中3个号(各不相同)
            int redSize = numList.size();
            Collections.sort(numList);
            for (int i = 0; i < redSize; i++) {
                if (i == 0) {
                    numStr = new StringBuilder(String.valueOf(numList.get(i)));
                } else {
                    numStr.append(", ").append(numList.get(i));
                }
            }
        } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_SAME_ALL)) {
            title = "三同号通选";//111, 222, 333, 444, 555, 666 任意一个开出
            numStr = new StringBuilder("111, 222, 333, 444, 555, 666");
        } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_SAME_ONE)) {
            title = "三同号单选";//猜中111, 222, 333, 444, 555, 666 中指定的一个
            int redSize = numList.size();
            Collections.sort(numList);
            for (int i = 0; i < redSize; i++) {
                if (i == 0) {
                    numStr = new StringBuilder(numList.get(i));
                } else {
                    numStr.append(numList.get(i));
                }
            }
        } else if (playMode.equals(FAST_THREE_ORDER_TYPE_3_TO_ALL)) {
            title = "三连号通选";//123,234,345,456任意一个开出
            numStr = new StringBuilder("123, 234, 345, 456");
        } else if (mLotteryName.equals(LOTTERY_TYPE_11_5)
                || mLotteryName.equals(LOTTERY_TYPE_11_5_OLD)
                || mLotteryName.equals(LOTTERY_TYPE_11_5_LUCKY)
                || mLotteryName.equals(LOTTERY_TYPE_11_5_YUE)
                || mLotteryName.equals(LOTTERY_TYPE_11_5_YILE)) {
            int orderType = 0;//bean.getOrderType() 11选5 类型(0.复试, 1.单式)
            if (orderType == DOUBLE_COLOR_ORDER_TYPE_FUSHI) {
                title += " " + StaticResourceUtils.getStringResourceById(R.string.lottery_fushi);
                //复式投注:所选号码个数超过单式投注的号码个数
            } else if (orderType == DOUBLE_COLOR_ORDER_TYPE_DANSHI) {
                title += " " + StaticResourceUtils.getStringResourceById(R.string.lottery_danshi);
                 /*购买“11选5”时，从01（射击）、02（射箭）、03（游泳）、04（跳水）、05（田径）、06（乒乓球）、
                 07（篮球）、08（足球）、09（举重）、10（赛艇）、11（帆船）共11个号码（比赛项目）中任选1个
                 或多个（最多8个）号码（比赛项目）所组成的一注彩票的投注为单式投注*/
            } else {
                /*胆拖投注是指先选取少于单式投注号码个数的号码作为胆码（即每注彩票均包含的号码），
                再选取除胆码以外的号码作为拖码，胆码与拖码个数之和须多于单式投注号码个数，
                由胆码与拖码按该单式投注方式组成多注彩票的投注。*/
            }

            switch (playMode) {//11选5
                case ORDER_11_5_ANY_2:
                    title = mContext.getString(R.string.PLAY_11_5_ANY_2); //选2个号，猜中开奖的任意2个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_ANY_3:
                    setAnyLotteryNum(numStr);
                    title = mContext.getString(R.string.PLAY_11_5_ANY_3); //选3个号，猜中开奖的任意3个号
                    break;

                case ORDER_11_5_ANY_4:
                    title = mContext.getString(R.string.PLAY_11_5_ANY_4); //选4个号，猜中开奖的任意4个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_ANY_5:
                    title = mContext.getString(R.string.PLAY_11_5_ANY_5);//选5个号，猜中开奖的全部5个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_ANY_6:
                    title = mContext.getString(R.string.PLAY_11_5_ANY_6);//选6个号，猜中开奖的全部5个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_ANY_7:
                    title = mContext.getString(R.string.PLAY_11_5_ANY_7);//选7个号,猜中开奖的全部5个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_ANY_8:
                    title = mContext.getString(R.string.PLAY_11_5_ANY_8);//选8个号,猜中开奖的全部5个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_FRONT_1_DIRECT:
                    title = mContext.getString(R.string.PLAY_11_5_FRONT_1_DIRECT);//选1个号猜中第1个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_FRONT_2_DIRECT:
                    title = mContext.getString(R.string.PLAY_11_5_FRONT_2_DIRECT);//选2个号猜中定位前2个号
                    setHeadLotteryNum(numStr);
                    break;

                case ORDER_11_5_FRONT_3_DIRECT:
                    title = mContext.getString(R.string.PLAY_11_5_FRONT_3_DIRECT);//选3个号猜中定位前3个号
                    setHeadLotteryNum(numStr);
                    break;
                case ORDER_11_5_FRONT_2_GROUP:
                    title = mContext.getString(R.string.PLAY_11_5_FRONT_2_GROUP); //选2个号猜中不定位前2个号
                    setAnyLotteryNum(numStr);
                    break;

                case ORDER_11_5_FRONT_3_GROUP:
                    title = mContext.getString(R.string.PLAY_11_5_FRONT_3_GROUP);//选3个号猜中不定位前3个号
                    setAnyLotteryNum(numStr);
                    break;
            }
        } else if (mLotteryName.equals(LOTTERY_TYPE_ALWAYS_COLOR) || mLotteryName.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            switch (playMode) {//时时彩(重庆时时彩和新时时彩)
                case ALWAYS_COLOR_ORDER_TYPE_2_GROUP:
                    title = "二星组选";//选2个号,与开奖号连续后两位相同
                    setGroupLotteryNum(numStr);
                    break;


                case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_3:
                    title = "三星组三";//选3个号码,投注号与开奖号后三位相同且为组三,顺序不限(组三是指开奖号后三位任意二位号码相同,如188)
                    setGroupLotteryNum(numStr);
                    break;

                case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_6:
                    title = "三星组六";//选3个号码,投注号与开奖号后三位相同且为组六,顺序不限(组六是指开奖号后三位号码各不相同,如135)
                    setGroupLotteryNum(numStr);
                    break;

                case ALWAYS_COLOR_ORDER_TYPE_1_DIRECT:
                    title = "一星直选";//选3个号码,与开奖号码个位相符
                    setDirectLotteryNum(numStr);
                    break;

                case ALWAYS_COLOR_ORDER_TYPE_2_DIRECT:
                    title = "二星直选";//选3个号码,与开奖号码连续后两位按位相符
                    setDirectLotteryNum(numStr);
                    break;

                case ALWAYS_COLOR_ORDER_TYPE_3_DIRECT:
                    title = "三星直选";//选3个号码,与开奖号码连续后三位按位相符
                    setDirectLotteryNum(numStr);
                    break;

                case ALWAYS_COLOR_ORDER_TYPE_5_DIRECT:
                    title = "五星直选";//选5个号码,与开奖号码完全相同且顺序一致
                    setHeadLotteryNum(numStr);
                    break;

                case ALWAYS_COLOR_ORDER_TYPE_5_ALL:
                    title = "五星通选";//选5个号码,与开奖号码按位相同或前/后三位按位相同或前/后两位按位相同
                    setHeadLotteryNum(numStr);
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_BIG_SMALL_SINGLE_DOUBLE:
                    title = "大小单双";//与开奖号码后两位数字属性按位相符
                    setBetNumSize(numStr);
                    break;
            }
        }

        //        if (!mLotteryName.equals(LOTTERY_TYPE_FAST_3)
        //                && !mLotteryName.equals(LOTTERY_TYPE_FAST_3_JS)
        //                && !mLotteryName.equals(LOTTERY_TYPE_FAST_3_HB)
        //                && !mLotteryName.equals(LOTTERY_TYPE_FAST_3_NEW)
        //                && !mLotteryName.equals(LOTTERY_TYPE_FAST_3_EASY)
        //                && !mLotteryName.equals(LOTTERY_TYPE_ALWAYS_COLOR)
        //                && !mLotteryName.equals(LOTTERY_TYPE_ARRANGE_5)) {
        //            int blueLength = blueSize * 4;
        //            int length = numStr.length();
        //            int startIdx = length - blueLength;
        //            SpannableString ss = new SpannableString(numStr.toString());
        //            ss.setSpan(new ForegroundColorSpan(StaticResourceUtils.getColorResourceById(R.color.color_blue_ball)),
        //                    startIdx, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //            ss.setSpan(new ForegroundColorSpan(StaticResourceUtils.getColorResourceById(R.color.color_red_ball)),
        //                    0, startIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //            holder.numberTv.setText(ss);
        //        } else {
        holder.numberTv.setText(numStr.toString());
        //        }

        holder.titleTv.setText(title);

    }

    //下注号的大小单双展示
    private void setBetNumSize(StringBuilder numStr) {
        //大小单双：按照位投放数据，1:大；2:小；3:单，4:双
        String betResult = "";
        if (null != numList && numList.size() > 0) {
            for (String num : numList) {
                switch (num) {
                    case "1":
                        betResult = "大";
                        break;
                    case "2":
                        betResult = "小";
                        break;
                    case "3":
                        betResult = "单";
                        break;
                    case "4":
                        betResult = "双";
                        break;
                }
                numStr.append(betResult);
                numStr.append(" ");
            }
        }
        if (numStr.length() > 0) {
            numStr.delete(numStr.length() - 1, numStr.length());
        }
    }

    //任选玩法下注号展示
    private void setAnyLotteryNum(StringBuilder numStr) {
        if (null != numList && numList.size() > 0) {
            for (String num : numList) {
                num = Integer.valueOf(num) < 10 ? "0" + num : num;
                numStr.append(num);
                numStr.append(" ");
            }
            if (numStr.length() > 0) {
                numStr.delete(numStr.length() - 1, numStr.length());
            }
        }
    }

    //时时彩组选玩法下注号展示
    private void setGroupLotteryNum(StringBuilder numStr) {
        if (null != numList && numList.size() > 0) {
            for (String num : numList) {
                numStr.append(num);
                numStr.append(" ");
            }
            if (numStr.length() > 0) {
                numStr.delete(numStr.length() - 1, numStr.length());
            }
        }
    }

    //直选玩法下注号展示
    private void setHeadLotteryNum(StringBuilder numStr) {

        if (null != numList && numList.size() > 0) {
            int size = numList.size();
            for (int i = 0; i < size; i++) {
                String num = numList.get(i);
                num = Integer.valueOf(num) < 10 ? "0" + num : num;
                numStr.append(num);
                if (i == size - 1) {
                    numStr.append(" ");
                } else {
                    numStr.append(" | ");
                }
            }
        }
    }

    //时时彩直选二,三,五玩法下注号展示
    private void setDirectLotteryNum(StringBuilder numStr) {
        if (null != numList && numList.size() > 0) {
            int size = numList.size();
            for (int i = 0; i < 5; i++) {
                if (i < size) {
                    String num = numList.get(i);
                    numStr.append(num);
                    numStr.append(" ");
                } else {
                    numStr.insert(0, "_ ");
                }
            }
            if (numStr.length() > 0) {
                numStr.delete(numStr.length() - 1, numStr.length());
            }
            LogF.d(TAG, "直选号码数为-->" + numStr.toString());
        }
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
