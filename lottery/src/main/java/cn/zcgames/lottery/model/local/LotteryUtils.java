package cn.zcgames.lottery.model.local;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.view.CustomShapTextView;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.bean.AlwaysColorOrder;
import cn.zcgames.lottery.home.bean.Arrange5Order;
import cn.zcgames.lottery.bean.BillDetail7Star;
import cn.zcgames.lottery.bean.BillDetailArrange5;
import cn.zcgames.lottery.bean.BillDetailFastThree;
import cn.zcgames.lottery.bean.BillDetailSevenHappy;
import cn.zcgames.lottery.bean.BillDetailThreeD;
import cn.zcgames.lottery.bean.BuyHistoryBean;
import cn.zcgames.lottery.home.bean.ElevenFiveOrder;
import cn.zcgames.lottery.home.bean.FastThreeOrder;
import cn.zcgames.lottery.bean.LotteryOrderDetail;
import cn.zcgames.lottery.bean.LotteryResultHistory;
import cn.zcgames.lottery.home.bean.DoubleColorOrder;
import cn.zcgames.lottery.home.bean.DoubleColorOrderBet;
import cn.zcgames.lottery.home.bean.FastThreeOrderOld;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.bean.SevenHappyOrder;
import cn.zcgames.lottery.home.bean.SevenStarOrder;
import cn.zcgames.lottery.bean.StakesBean;
import cn.zcgames.lottery.home.bean.ThreeDOrder;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.ChooseNumberBean;
import cn.zcgames.lottery.home.bean.TrendSettingBean;
import cn.zcgames.lottery.utils.DateUtils;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.*;

/**
 * Created by admin on 2017/4/6.
 * Berfy修改 2018.10.9
 * 彩票算法工具类
 */
public class LotteryUtils {

    private static final String TAG = "LotteryUtils";
    public static String[] mRedNumbers = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31", "32", "33"};
    public static String[] mBlueNumbers = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16"};
    //快三
    public static String[] mFastThreeSumNumbers = new String[]{"3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};
    public static String[] mFastThreeNumbers = new String[]{"1", "2", "3", "4", "5",
            "6"};
    public static String[] mFastThreeTwoNumbers = new String[]{"11", "22", "33", "44", "55",
            "66"};
    public static Integer[] mFastThreeSumPrices = new Integer[]{240, 80, 40, 25, 16, 12, 10, 9, 9, 10, 12, 16, 25, 40, 80, 240};

    public static Integer[] mFastThreeErBuTongs = new Integer[]{12, 13, 14, 15, 16, 23, 24, 25, 26, 34, 35, 36, 45, 46, 56};
    //11选5
    public static String[] m115Numbers = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11"};

    //11选5前一形态
    public static String[] m115DirectFrom = new String[]{"奇", "偶", "质", "合", "0路", "1路", "2路"};

    //时时彩 3D
    public static String[] mThreeDNumbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9"};
    //时时彩形态
    public static String[] mACFormNumbers = new String[]{"大", "小", "单", "双", "大", "小", "单", "双"};
    public static String[] mBigSmallSingleDouble = new String[]{"大", "小", "单", "双"};
    public static String[] mFastThreeThree3SameSingleNumbers = new String[]{"111", "222", "333", "444", "555", "666"};
    public static String[] mSevenNumbers = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30"};

    /**
     * 根据球的type创建ballView
     *
     * @param context
     * @param numberStr
     * @return
     */
    public static View createDCBallView(Context context, String numberStr, String colorStr, int size) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_double_color_ball, null);
        CustomShapTextView tv = view.findViewById(R.id.ball_numer);
        ViewGroup.LayoutParams params = tv.getLayoutParams();
        int sizeDip = UIHelper.dip2px(MyApplication.getAppContext(), size);
        params.height = sizeDip;
        params.width = sizeDip;
        tv.setText(numberStr);
        if (colorStr.indexOf("#") == -1) {
            colorStr = "#" + colorStr;
        }
        tv.setBackgroundColor(Color.parseColor(colorStr));
        tv.setFillColor(true);
        //        if (type == BALL_TYPE_RED) {
        //            tv.setBackgroundResource(R.drawable.shape_bg_redball_selected);
        //        } else {
        //            tv.setBackgroundResource(R.drawable.shape_bg_blueball_selected);
        //        }
        return view;
    }

    /**
     * 根据球的number创建ballView
     *
     * @param context
     * @param number
     * @return
     */
    public static View createFast3BallView(Context context, int number, int size) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_fast_three_ball, null);
        ImageView iv = (ImageView) view.findViewById(R.id.ball_numer);
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        int sizeDip = UIHelper.dip2px(MyApplication.getAppContext(), size);
        params.height = sizeDip;
        params.width = sizeDip;
        int imageResourceId = R.drawable.label_six;
        if (number == 5) {
            imageResourceId = R.drawable.label_five;
        } else if (number == 6) {
            imageResourceId = R.drawable.label_six;
        } else if (number == 3) {
            imageResourceId = R.drawable.label_three;
        } else if (number == 1) {
            imageResourceId = R.drawable.label_one;
        } else if (number == 2) {
            imageResourceId = R.drawable.label_tow;
        } else if (number == 4) {
            imageResourceId = R.drawable.label_four;
        }
        iv.setImageResource(imageResourceId);
        return view;
    }

    public static List<LotteryBall> createFast3BallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        for (int i = 0; i < mFastThreeNumbers.length; i++) {
            LotteryBall ball = new LotteryBall();
            ball.setNumber(LotteryUtils.mFastThreeNumbers[i]);
            ball.setSelected(false);
            ball.setType(AppConstants.BALL_TYPE_RED);
            lotteryBalls.add(ball);
        }
        return lotteryBalls;
    }

    public static List<LotteryBall> createFast3ThreeToAllBallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        LotteryBall ball = new LotteryBall();
        ball.setNumber("123 234 345 456");
        ball.setSelected(false);
        ball.setType(AppConstants.BALL_TYPE_RED);
        lotteryBalls.add(ball);
        return lotteryBalls;
    }

    public static List<LotteryBall> createFast3ThreeSameAllBallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        LotteryBall ball = new LotteryBall();
        ball.setNumber("111 222 333 444 555 666");
        ball.setSelected(false);
        ball.setType(AppConstants.BALL_TYPE_RED);
        lotteryBalls.add(ball);
        return lotteryBalls;
    }

    public static List<LotteryBall> createFast3SumBallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        for (int i = 0; i < mFastThreeSumNumbers.length; i++) {
            LotteryBall ball = new LotteryBall();
            ball.setNumber(LotteryUtils.mFastThreeSumNumbers[i]);
            ball.setSelected(false);
            ball.setType(AppConstants.BALL_TYPE_RED);
            lotteryBalls.add(ball);
        }
        return lotteryBalls;
    }

    public static List<LotteryBall> createFast3ThreeSameBallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        for (int i = 0; i < mFastThreeThree3SameSingleNumbers.length; i++) {
            LotteryBall ball = new LotteryBall();
            ball.setNumber(LotteryUtils.mFastThreeThree3SameSingleNumbers[i]);
            ball.setSelected(false);
            ball.setType(AppConstants.BALL_TYPE_RED);
            lotteryBalls.add(ball);
        }
        return lotteryBalls;
    }

    public static List<LotteryBall> createFast3TwoSameBallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        for (int i = 0; i < mFastThreeTwoNumbers.length; i++) {
            LotteryBall ball = new LotteryBall();
            ball.setNumber(LotteryUtils.mFastThreeTwoNumbers[i]);
            ball.setSelected(false);
            ball.setType(AppConstants.BALL_TYPE_RED);
            lotteryBalls.add(ball);
        }
        return lotteryBalls;
    }

    public static List<LotteryBall> create115BallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        for (int i = 0; i < LotteryUtils.m115Numbers.length; i++) {
            LotteryBall ball = new LotteryBall();
            ball.setNumber(LotteryUtils.m115Numbers[i]);
            ball.setSelected(false);
            ball.setType(AppConstants.BALL_TYPE_RED);
            lotteryBalls.add(ball);
        }
        return lotteryBalls;
    }

    //时时彩选号数据
    public static List<LotteryBall> createACBallList() {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        for (int i = 0; i < LotteryUtils.mThreeDNumbers.length; i++) {
            LotteryBall ball = new LotteryBall();
            ball.setNumber(LotteryUtils.mThreeDNumbers[i]);
            ball.setSelected(false);
            ball.setType(AppConstants.BALL_TYPE_RED);
            lotteryBalls.add(ball);
        }
        return lotteryBalls;
    }

    /**
     * 根据球的String创建TextView
     *
     * @param context
     * @param number
     * @return
     */
    public static View createTextView(Context context, String number) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_textview, null);
        TextView tv = (TextView) view.findViewById(R.id.ball_number);
        tv.setText(number);
        return view;
    }

    /**
     * 显示双色球view
     *
     * @param context               上下文
     * @param mLotteryResultHistory LotteryResultHistory
     * @param mBallView             展现双色球的view
     */
    public static void showDoubleColorView(Context context, LotteryResultHistory mLotteryResultHistory, LinearLayout mBallView, TextView tv_sum, String lotteryType) {
        List<LotteryResultHistory.DoubleColorNumber> balls = mLotteryResultHistory.getBall();
        if (balls == null) {
            return;
        }
        for (int i = 0; i < balls.size(); i++) {
            LotteryResultHistory.DoubleColorNumber ball = balls.get(i);
            if (lotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)
                    || lotteryType.equals(LOTTERY_TYPE_FAST_3)) {
                tv_sum.setVisibility(View.VISIBLE);
                tv_sum.setText(context.getString(R.string.lottery_type_fast_three_sum_title) + ball.getSum());
                for (String number : ball.getValue()) {
                    mBallView.addView(LotteryUtils.createFast3BallView(context, Integer.valueOf(number), 40));
                }
            } else {
                tv_sum.setVisibility(View.GONE);
                for (String number : ball.getValue()) {
                    mBallView.addView(LotteryUtils.createDCBallView(context, number, ball.getColor(), 35));
                }
                try {
                    if (lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                            || lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
                        tv_sum.setVisibility(View.VISIBLE);
                        tv_sum.setText(LotteryUtils.formatBigSmallSingleDouble(Integer.valueOf(ball.getValue()[3]))
                                + " | " + LotteryUtils.formatBigSmallSingleDouble(Integer.valueOf(ball.getValue()[4])));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 随机选取指定个数的数字
     *
     * @param count
     * @param type
     * @return
     */
    public static List<String> randomSelectNumber(int count, int type) {
        List<String> strings = new ArrayList<>();
        int length;
        if (type == BALL_TYPE_RED) {
            length = mRedNumbers.length;
        } else {
            length = mBlueNumbers.length;
        }

        Random rand = new Random();
        while (strings.size() < count) {
            int num = rand.nextInt(length);
            String str;
            if (type == BALL_TYPE_RED) {
                str = mRedNumbers[num];
            } else {
                str = mBlueNumbers[num];
            }
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    /**
     * 随机选取指定个数的数字
     *
     * @param count
     * @return
     */
    public static List<String> randomSelectSevenNumber(int count) {
        List<String> strings = new ArrayList<>();
        int length = mSevenNumbers.length;

        Random rand = new Random();
        while (strings.size() < count) {
            int num = rand.nextInt(length);
            String str = mSevenNumbers[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    /**
     * 随机选取11选5指定个数的数字
     *
     * @param count
     * @return
     */
    public static List<String> random11_5Number(int count) {
        List<String> strings = new ArrayList<>();
        int length = m115Numbers.length;
        Random rand = new Random();
        while (strings.size() < count) {
            int num = rand.nextInt(length);
            String str = m115Numbers[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    /**
     * 随机选取指定个数的数字
     *
     * @param count
     * @return
     */
    public static List<String> randomSelectNumber(int count) {
        List<String> strings = new ArrayList<>();
        int length = mThreeDNumbers.length;
        Random rand = new Random();
        while (strings.size() < count) {
            int num = rand.nextInt(length);
            String str = mThreeDNumbers[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    /**
     * 随机选取指定个数的数字
     *
     * @param count
     * @return
     */
    public static List<String> randomSelectFastThreeSumNumber(int count) {
        List<String> strings = new ArrayList<>();
        int length = mFastThreeSumNumbers.length;
        Random rand = new Random();
        while (strings.size() < count) {
            int num = rand.nextInt(length);
            String str = mFastThreeSumNumbers[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    /**
     * 随机选取指定个数的数字
     *
     * @param count
     * @return
     */
    public static List<String> randomSelectFastThree3SameSingleNumber(int count) {
        List<String> strings = new ArrayList<>();
        int length = mFastThreeThree3SameSingleNumbers.length;
        Random rand = new Random();
        while (strings.size() < count) {
            int num = rand.nextInt(length);
            String str = mFastThreeThree3SameSingleNumbers[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    /**
     * 将List<String>格式抓换成Ball数组
     *
     * @param balls
     * @return
     */
    public static List<LotteryBall> changeBallNumberToBallList(List<String> balls) {
        List<LotteryBall> lotteryBalls = new ArrayList<>();
        for (int i = 0; i < balls.size(); i++) {
            LotteryBall lotteryBall = new LotteryBall();
            lotteryBall.setNumber(balls.get(i));
            lotteryBalls.add(lotteryBall);
        }
        return lotteryBalls;
    }

    /**
     * 将DoubleColorBall列表中的number提出来形成数组
     *
     * @param balls
     * @return
     */
    public static String changeBallToBallNumber(List<LotteryBall> balls) {
        List<String> ballNumber = new ArrayList<>();
        for (int i = 0; i < balls.size(); i++) {
            ballNumber.add(balls.get(i).getNumber());
        }
        return GsonUtils.getGsonInstance().toJson(ballNumber);
    }

    /**
     * 根据Order将红蓝球的号码形成字符串
     *
     * @param order
     * @return
     */
    public static SpannableString getSpannableNormalString(LotteryOrder order) {
        List<String> redBallList = GsonUtils.formatStringToStringList(order.getRedBall());
        List<String> blueBallList = GsonUtils.formatStringToStringList(order.getBlueBall());
        int redSize = redBallList == null ? 0 : redBallList.size();
        int blueSize = blueBallList == null ? 0 : blueBallList.size();
        String ballString = "";
        if (redSize > 0) {
            Collections.sort(redBallList);
            for (int i = 0; i < redSize; i++) {
                String bean = redBallList.get(i);
                if (i == 0) {
                    ballString = bean;
                } else {
                    ballString = ballString + "  " + bean;
                }
            }
        }

        if (blueSize > 0) {
            Collections.sort(blueBallList);
            for (int i = 0; i < blueSize; i++) {
                ballString = ballString + "  " + blueBallList.get(i);
            }
        }

        SpannableString ss = new SpannableString(ballString);
        ss.setSpan(new ForegroundColorSpan(StaticResourceUtils.getColorResourceById(R.color.color_blue_ball)),
                ballString.length() - 4 * blueSize,
                ballString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString getSpannableNormalString(String[] redBallList, String[] blueBallList) {
        int redSize = redBallList == null ? 0 : redBallList.length;
        int blueSize = blueBallList == null ? 0 : blueBallList.length;
        String ballString = "";
        if (redSize > 0) {
            for (int i = 0; i < redSize; i++) {
                String bean = redBallList[i];
                if (i == 0) {
                    ballString = bean;
                } else {
                    ballString = ballString + "  " + bean;
                }
            }
        }

        if (blueSize > 0) {
            for (int i = 0; i < blueSize; i++) {
                ballString = ballString + "  " + blueBallList[i];
            }
        }

        SpannableString ss = new SpannableString(ballString);
        ss.setSpan(new ForegroundColorSpan(StaticResourceUtils.getColorResourceById(R.color.color_blue_ball)),
                ballString.length() - 4 * blueSize,
                ballString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 根据Order将红蓝球的号码形成字符串
     *
     * @param order
     * @return
     */
    public static SpannableString getSpannableDantuoString(LotteryOrder order) {
        List<String> danBallList = GsonUtils.formatStringToStringList(order.getDanBall());
        List<String> tuoBallList = GsonUtils.formatStringToStringList(order.getTuoBall());
        List<String> blueBallList = GsonUtils.formatStringToStringList(order.getBlueBall());
        int danSize = danBallList == null ? 0 : danBallList.size();
        int tuoSize = tuoBallList == null ? 0 : tuoBallList.size();
        int blueSize = blueBallList == null ? 0 : blueBallList.size();
        //        Collections.sort(danBallList);
        //        Collections.sort(blueBallList);
        //        Collections.sort(tuoBallList);
        String ballString = "";
        if (danSize == 1) {
            String bean = danBallList.get(0);
            ballString = ballString + " (" + bean + ") ";
        } else {
            for (int i = 0; i < danSize; i++) {
                String bean = danBallList.get(i);
                if (i == 0) {
                    ballString = ballString + " (" + bean;
                } else if (i == danSize - 1) {
                    ballString = ballString + "  " + bean + ") ";
                } else {
                    ballString = ballString + "  " + bean;
                }

            }
        }

        for (int i = 0; i < tuoSize; i++) {
            String bean = tuoBallList.get(i);
            ballString = ballString + "  " + bean;
        }
        for (int i = 0; i < blueSize; i++) {
            ballString = ballString + "  " + blueBallList.get(i);
        }
        SpannableString ss = new SpannableString(ballString);
        ss.setSpan(new ForegroundColorSpan(StaticResourceUtils.getColorResourceById(R.color.color_blue_ball)),
                ballString.length() - 4 * blueSize,
                ballString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * @param chase
     * @param multiple
     * @param mOrders
     * @return
     */
    public static DoubleColorOrder createDoubleColorOrder(int chase, int multiple, List<LotteryOrder> mOrders) {
        DoubleColorOrder order = new DoubleColorOrder();
        order.setChase(chase);
        order.setMultiple(multiple);
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        for (int i = 0; i < mOrders.size(); i++) {
            LotteryOrder bean = mOrders.get(i);
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setBlue(GsonUtils.formatStringToStringList(bean.getBlueBall()));
            bet.setDan(GsonUtils.formatStringToStringList(bean.getDanBall()));
            bet.setRed(GsonUtils.formatStringToStringList(bean.getRedBall()));
            bet.setTuo(GsonUtils.formatStringToStringList(bean.getTuoBall()));
            if (bean.getPlayMode() == DOUBLE_COLOR_PLAY_STYLE_NORMAL) {
                bet.setMode("plain");
            } else {
                bet.setMode("dantuo");
            }
            bets.add(bet);
        }
        order.setBets(bets);
        return order;
    }

    public static ThreeDOrder createThreeDOrder(int chase, int multiple, List<LotteryOrder> mOrders, int playMode, String sequence) {
        ThreeDOrder order = new ThreeDOrder();
        order.setChase(chase);
        order.setMultiple(multiple);
        order.setStart_sequence(sequence);
        if (playMode == THREE_D_PLAY_DIRECT || playMode == ARRANGE_3_PLAY_DIRECT) {
            List<ThreeDOrder.ThreeDDirect> directs = new ArrayList<>();
            for (LotteryOrder bean : mOrders) {
                ThreeDOrder.ThreeDDirect direct = new ThreeDOrder.ThreeDDirect();
                direct.setHundred(GsonUtils.formatStringToStringList(bean.getRedBall()));
                direct.setOne(GsonUtils.formatStringToStringList(bean.getTuoBall()));
                direct.setTen(GsonUtils.formatStringToStringList(bean.getDanBall()));
                directs.add(direct);
            }
            List<ThreeDOrder.ThreeDGroup> group6 = new ArrayList<>();
            List<ThreeDOrder.ThreeDGroup> group3 = new ArrayList<>();
            order.setDirect(directs);
            order.setGroup6(group6);
            order.setGroup3(group3);
        } else if (playMode == THREE_D_PLAY_GROUP_SIX || playMode == ARRANGE_3_PLAY_GROUP_SIX) {
            List<ThreeDOrder.ThreeDGroup> group6 = new ArrayList<>();
            for (LotteryOrder bean : mOrders) {
                ThreeDOrder.ThreeDGroup group = new ThreeDOrder.ThreeDGroup();
                group.setDigit(GsonUtils.formatStringToStringList(bean.getRedBall()));
                group6.add(group);
            }
            List<ThreeDOrder.ThreeDDirect> directs = new ArrayList<>();
            List<ThreeDOrder.ThreeDGroup> group3 = new ArrayList<>();
            order.setGroup6(group6);
            order.setDirect(directs);
            order.setGroup3(group3);
        } else if (playMode == THREE_D_PLAY_GROUP_THREE || playMode == ARRANGE_3_PLAY_GROUP_THREE) {
            List<ThreeDOrder.ThreeDGroup> group3 = new ArrayList<>();
            for (LotteryOrder bean : mOrders) {
                ThreeDOrder.ThreeDGroup group = new ThreeDOrder.ThreeDGroup();
                group.setDigit(GsonUtils.formatStringToStringList(bean.getRedBall()));
                group3.add(group);
            }
            List<ThreeDOrder.ThreeDDirect> directs = new ArrayList<>();
            List<ThreeDOrder.ThreeDGroup> group6 = new ArrayList<>();
            order.setGroup3(group3);
            order.setDirect(directs);
            order.setGroup6(group6);
        }
        return order;
    }

    //再来一单逻辑
    public static void againCreateOrder(String lotteryType, LotteryOrderDetail lotteryOrderDetail, List<StakesBean> stakesBeans) {
        if (null == stakesBeans) {
            return;
        }
        //订单详情反回的数据都是单注拆开的
        //所以循环每一注存储到数据库中
        //遇到三星组三不处理  上方单独处理
        for (int i = stakesBeans.size() - 1; i >= 0; i--) {
            StakesBean stakesBean = stakesBeans.get(i);
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setMode(stakesBean.getGamePlay());
            switch (lotteryType) {
                case LOTTERY_TYPE_FAST_3:
                case LOTTERY_TYPE_FAST_3_JS:
                case LOTTERY_TYPE_FAST_3_HB:
                case LOTTERY_TYPE_FAST_3_EASY:
                case LOTTERY_TYPE_FAST_3_NEW:
                    switch (stakesBean.getGamePlay()) {
                        case FAST_THREE_ORDER_TYPE_SUM://和值
                            List<String> num = new ArrayList<>();
                            num.add(stakesBean.getSum() + "");
                            bet.setRed(num);
                            break;
                        case FAST_THREE_ORDER_TYPE_2_DIFFERENT://二不同号
                            bet.setRed(stakesBean.getNumbers());
                            break;
                        case FAST_THREE_ORDER_TYPE_2_SAME_MORE://二同号复选
                            List<String> twoMoreList = new ArrayList<>();
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int j = 0; j < stakesBean.getNumbers().size(); j++) {
                                String number = stakesBean.getNumbers().get(j);
                                stringBuilder.append(number);
                            }
                            twoMoreList.add(stringBuilder.toString());
                            bet.setRed(twoMoreList);
                            break;
                        case FAST_THREE_ORDER_TYPE_2_SAME_ONE://二同号单选
                            List<String> sameList = new ArrayList<>();
                            List<String> diffList = new ArrayList<>();
                            int size = stakesBean.getNumbers().size();
                            if (size == 3) {
                                boolean isFirstDiff = !stakesBean.getNumbers().get(0)
                                        .equals(stakesBean.getNumbers().get(1));//不同号是否在第一位，否则在最后
                                if (isFirstDiff) {
                                    diffList.add(stakesBean.getNumbers().get(0));
                                    sameList.add(stakesBean.getNumbers().get(1) + stakesBean.getNumbers().get(2));
                                } else {
                                    sameList.add(stakesBean.getNumbers().get(0) + stakesBean.getNumbers().get(1));
                                    diffList.add(stakesBean.getNumbers().get(2));
                                }
                                bet.setRed(sameList);
                                bet.setBlue(diffList);
                            }
                            break;
                        case FAST_THREE_ORDER_TYPE_3_DIFFERENT://三不同号
                            bet.setRed(stakesBean.getNumbers());
                            break;
                        case FAST_THREE_ORDER_TYPE_3_SAME_ALL://三同号通选
                            List<String> same3All = new ArrayList<>();
                            same3All.add("111");
                            same3All.add("222");
                            same3All.add("333");
                            same3All.add("444");
                            same3All.add("555");
                            same3All.add("666");
                            bet.setRed(same3All);
                            break;
                        case FAST_THREE_ORDER_TYPE_3_SAME_ONE://三同号单选
                            List<String> threeSingleList = new ArrayList<>();
                            if (stakesBean.getNumbers().size() > 0) {
                                threeSingleList.add(stakesBean.getNumbers().get(0));
                            }
                            bet.setRed(threeSingleList);
                            break;
                        case FAST_THREE_ORDER_TYPE_3_TO_ALL://三连号通选
                            List<String> same3AllList = new ArrayList<>();
                            same3AllList.add("123");
                            same3AllList.add("234");
                            same3AllList.add("345");
                            same3AllList.add("456");
                            bet.setRed(same3AllList);
                            break;
                    }
                    break;
                case LOTTERY_TYPE_11_5:
                case LOTTERY_TYPE_11_5_OLD:
                case LOTTERY_TYPE_11_5_LUCKY:
                case LOTTERY_TYPE_11_5_YUE:
                case LOTTERY_TYPE_11_5_YILE:
                    switch (stakesBean.getGamePlay()) {
                        case ORDER_11_5_ANY_2://任选
                        case ORDER_11_5_ANY_3:
                        case ORDER_11_5_ANY_4:
                        case ORDER_11_5_ANY_5:
                        case ORDER_11_5_ANY_6:
                        case ORDER_11_5_ANY_7:
                        case ORDER_11_5_ANY_8:
                        case ORDER_11_5_FRONT_1_DIRECT://直选
                        case ORDER_11_5_FRONT_2_GROUP://组选
                        case ORDER_11_5_FRONT_3_GROUP:
                            bet.setRed(stakesBean.getNumbers());
                            break;
                        case ORDER_11_5_FRONT_2_DIRECT:
                            List<String> wanList = new ArrayList<>();
                            List<String> qianList = new ArrayList<>();
                            if (null != stakesBean.getNumbers() && stakesBean.getNumbers().size() == 2) {
                                wanList.add(stakesBean.getNumbers().get(0));//万位
                                qianList.add(stakesBean.getNumbers().get(1));//千位
                                bet.setRed(wanList);
                                bet.setBlue(qianList);
                            }
                            break;
                        case ORDER_11_5_FRONT_3_DIRECT:
                            wanList = new ArrayList<>();
                            qianList = new ArrayList<>();
                            List<String> baiList = new ArrayList<>();
                            if (null != stakesBean.getNumbers() && stakesBean.getNumbers().size() == 3) {
                                wanList.add(stakesBean.getNumbers().get(0));//万位
                                qianList.add(stakesBean.getNumbers().get(1));//千位
                                baiList.add(stakesBean.getNumbers().get(2));//百位
                                bet.setRed(wanList);
                                bet.setBlue(qianList);
                                bet.setDan(baiList);
                            }
                            break;
                    }
                    break;
                case LOTTERY_TYPE_ALWAYS_COLOR:
                case LOTTERY_TYPE_ALWAYS_COLOR_NEW://适配时时彩
                    switch (stakesBean.getGamePlay()) {
                        case ALWAYS_COLOR_ORDER_TYPE_1_DIRECT: //一星直选
                            bet.setFive(stakesBean.getNumbers());
                            break;
                        case ALWAYS_COLOR_ORDER_TYPE_2_DIRECT://二星直选
                            List<String> shiList = new ArrayList<>();
                            List<String> geList = new ArrayList<>();
                            if (stakesBean.getNumbers().size() == 2) {
                                shiList.add(stakesBean.getNumbers().get(0));
                                geList.add(stakesBean.getNumbers().get(1));
                                bet.setTuo(shiList);
                                bet.setFive(geList);
                            }
                            break;
                        case ALWAYS_COLOR_ORDER_TYPE_2_GROUP://二星组选
                            bet.setFive(stakesBean.getNumbers());
                            break;
                        case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_3://三星组三 不处理
                            bet.setFive(stakesBean.getNumbers());
                            continue;
                        case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_6://三星组六
                            bet.setFive(stakesBean.getNumbers());
                            break;
                        case ALWAYS_COLOR_ORDER_TYPE_3_DIRECT://三星直选
                            List<String> baiList = new ArrayList<>();
                            shiList = new ArrayList<>();
                            geList = new ArrayList<>();
                            if (stakesBean.getNumbers().size() == 3) {
                                baiList.add(stakesBean.getNumbers().get(0));
                                shiList.add(stakesBean.getNumbers().get(1));
                                geList.add(stakesBean.getNumbers().get(2));
                                bet.setDan(baiList);
                                bet.setTuo(shiList);
                                bet.setFive(geList);
                            }
                            break;
                        case ALWAYS_COLOR_ORDER_TYPE_5_DIRECT://五星直选
                        case ALWAYS_COLOR_ORDER_TYPE_5_ALL://五星通选
                            List<String> wanList = new ArrayList<>();
                            List<String> qianList = new ArrayList<>();
                            baiList = new ArrayList<>();
                            shiList = new ArrayList<>();
                            geList = new ArrayList<>();
                            if (stakesBean.getNumbers().size() == 5) {
                                wanList.add(stakesBean.getNumbers().get(0));
                                qianList.add(stakesBean.getNumbers().get(1));
                                baiList.add(stakesBean.getNumbers().get(2));
                                shiList.add(stakesBean.getNumbers().get(3));
                                geList.add(stakesBean.getNumbers().get(4));
                                bet.setRed(wanList);
                                bet.setBlue(qianList);
                                bet.setDan(baiList);
                                bet.setTuo(shiList);
                                bet.setFive(geList);
                            }
                            break;
                        case ALWAYS_COLOR_ORDER_TYPE_BIG_SMALL_SINGLE_DOUBLE://大小单双
                            shiList = new ArrayList<>();
                            geList = new ArrayList<>();
                            if (stakesBean.getNumbers().size() == 2) {
                                shiList.add(stakesBean.getNumbers().get(0));
                                geList.add(stakesBean.getNumbers().get(1));
                                bet.setTuo(LotteryUtils.getDXDSStringList(shiList));
                                bet.setFive(LotteryUtils.getDXDSStringList(geList));
                            }
                            break;

                    }
                    break;
            }

            LotteryOrder order = fixLotteryCountAndGetOrder(lotteryType, bet);
            LotteryOrderDbUtils.addLotteryOrder(order);
        }
        //时时彩 组三单独处理插入投注单
        if (lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                || lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            if (null != lotteryOrderDetail) {
                Type type = new TypeToken<List<AlwaysColorOrder.AlwaysColorBet>>() {
                }.getType();
                List<AlwaysColorOrder.AlwaysColorBet> alwaysColorBets
                        = GsonUtil.getInstance().toClass(lotteryOrderDetail.getRaw_stakes_str(), type);
                orderToDBAC(lotteryType, alwaysColorBets);
            }
        }
    }

    /**
     * 计算每单的注数和金额
     *
     * @param bet
     * @return
     */
    public static LotteryOrder fixLotteryCountAndGetOrder(String lotteryType, DoubleColorOrderBet bet) {
        //TODO 新增彩种需要适配 Y
        LotteryOrder order = new LotteryOrder();
        List<String> red = bet.getRed();
        List<String> blue = bet.getBlue();
        List<String> dan = bet.getDan();
        List<String> tuo = bet.getTuo();
        List<String> other = bet.getFive();
        List<String> six = bet.getSix();
        List<String> seven = bet.getSeven();

        String mode = bet.getMode();
        long totalCount = 0;
        int modeId = -1;
        if (lotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
            if (mode.equals(DC_PLAY_STYLE_NORMAL)) {
                modeId = DOUBLE_COLOR_PLAY_STYLE_NORMAL;
                totalCount = CalculateCountUtils.doubleColorOrdinary(red.size(), blue.size());
            } else if (mode.equals(DC_PLAY_STYLE_DANTUO)) {
                modeId = DOUBLE_COLOR_PLAY_STYLE_DANTUO;
                totalCount = CalculateCountUtils.doubleColorDantuo(dan.size(), tuo.size(), blue.size());
            }
        } else if (lotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
            modeId = DOUBLE_COLOR_PLAY_STYLE_NORMAL;
            totalCount = CalculateCountUtils.calculateSevenCount(red.size());
        } else if (lotteryType.equals(LOTTERY_TYPE_3_D)) {
            if (mode.equals(TD_PLAY_GROUP_THREE)) {
                totalCount = CalculateCountUtils.getGroup3Count(red.size());
                modeId = THREE_D_PLAY_GROUP_THREE;
            } else if (mode.equals(TD_PLAY_GROUP_SIX)) {
                totalCount = CalculateCountUtils.getGroup6Count(red.size());
                modeId = THREE_D_PLAY_GROUP_SIX;
            } else if (mode.equals(TD_PLAY_DIRECT)) {
                totalCount = CalculateCountUtils.getThreeDDirectCount(red.size(), dan.size(), tuo.size());
                modeId = THREE_D_PLAY_DIRECT;
            }
        } else if (lotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            if (mode.equals(A3_PLAY_GROUP_THREE)) {
                totalCount = CalculateCountUtils.getGroup3Count(red.size());
                modeId = ARRANGE_3_PLAY_GROUP_THREE;
            } else if (mode.equals(A3_PLAY_GROUP_SIX)) {
                totalCount = CalculateCountUtils.getGroup6Count(red.size());
                modeId = ARRANGE_3_PLAY_GROUP_SIX;
            } else if (mode.equals(A3_PLAY_DIRECT)) {
                totalCount = CalculateCountUtils.getThreeDDirectCount(red.size(), dan.size(), tuo.size());
                modeId = ARRANGE_3_PLAY_DIRECT;
            }
        } else if (lotteryType.equals(LOTTERY_TYPE_FAST_3)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            if (mode.equals(FAST_THREE_ORDER_TYPE_2_DIFFERENT)) {
                totalCount = CalculateCountUtils.towDifferentCount(red.size());
                modeId = FAST_THREE_2_DIFFERENT;
            } else if (mode.equals(FAST_THREE_ORDER_TYPE_SUM)) {
                totalCount = red.size();
                modeId = FAST_THREE_SUM;
            } else if (mode.equals(FAST_THREE_ORDER_TYPE_2_SAME_MORE)) {
                totalCount = red.size();
                modeId = FAST_THREE_2_SAME_MORE;
            } else if (mode.equals(FAST_THREE_ORDER_TYPE_2_SAME_ONE)) {
                totalCount = red.size() * blue.size();
                modeId = FAST_THREE_2_SAME_ONE;
            } else if (mode.equals(FAST_THREE_ORDER_TYPE_3_DIFFERENT)) {
                totalCount = CalculateCountUtils.threeDifferentCount(red.size());
                modeId = FAST_THREE_3_DIFFERENT;
            } else if (mode.equals(FAST_THREE_ORDER_TYPE_3_SAME_ALL)) {
                totalCount = 1;
                modeId = FAST_THREE_3_SAME_ALL;
            } else if (mode.equals(FAST_THREE_ORDER_TYPE_3_SAME_ONE)) {
                totalCount = red.size();
                modeId = FAST_THREE_3_SAME_ONE;
            } else if (mode.equals(FAST_THREE_ORDER_TYPE_3_TO_ALL)) {
                totalCount = 1;
                modeId = FAST_THREE_3_TO_ALL;
            }
        } else if (lotteryType.equals(LOTTERY_TYPE_11_5)
                || lotteryType.equals(LOTTERY_TYPE_11_5_LUCKY)
                || lotteryType.equals(LOTTERY_TYPE_11_5_OLD)
                || lotteryType.equals(LOTTERY_TYPE_11_5_YUE)
                || lotteryType.equals(LOTTERY_TYPE_11_5_YILE)) {
            if (mode.equals(ORDER_11_5_ANY_2)) {
                totalCount = CalculateCountUtils.combination(red.size(), 2);
                modeId = PLAY_11_5_ANY_2;
            } else if (mode.equals(ORDER_11_5_FRONT_2_GROUP)) {
                totalCount = CalculateCountUtils.combination(red.size(), 3);
                modeId = PLAY_11_5_FRONT_2_GROUP;
            } else if (mode.equals(ORDER_11_5_ANY_3)) {
                totalCount = CalculateCountUtils.combination(red.size(), 3);
                modeId = PLAY_11_5_ANY_3;
            } else if (mode.equals(ORDER_11_5_FRONT_3_GROUP)) {
                totalCount = CalculateCountUtils.combination(red.size(), 3);
                modeId = PLAY_11_5_FRONT_3_GROUP;
            } else if (mode.equals(ORDER_11_5_ANY_4)) {
                totalCount = CalculateCountUtils.combination(red.size(), 4);
                modeId = PLAY_11_5_ANY_4;
            } else if (mode.equals(ORDER_11_5_ANY_5)) {
                totalCount = CalculateCountUtils.combination(red.size(), 5);
                modeId = PLAY_11_5_ANY_5;
            } else if (mode.equals(ORDER_11_5_ANY_6)) {
                totalCount = CalculateCountUtils.combination(red.size(), 6);
                modeId = PLAY_11_5_ANY_6;
            } else if (mode.equals(ORDER_11_5_ANY_7)) {
                totalCount = CalculateCountUtils.combination(red.size(), 7);
                modeId = PLAY_11_5_ANY_7;
            } else if (mode.equals(ORDER_11_5_ANY_8)) {
                totalCount = CalculateCountUtils.combination(red.size(), 8);
                modeId = PLAY_11_5_ANY_8;
            } else if (mode.equals(ORDER_11_5_FRONT_1_DIRECT)) {
                totalCount = red.size();
                modeId = PLAY_11_5_FRONT_1_DIRECT;
            } else if (mode.equals(ORDER_11_5_FRONT_2_DIRECT)) {
                totalCount = CalculateCountUtils.get11_5DirectCount(changeBallNumberToBallList(red),
                        changeBallNumberToBallList(blue), null, PLAY_11_5_FRONT_2_DIRECT);
                modeId = PLAY_11_5_FRONT_2_DIRECT;
            } else if (mode.equals(ORDER_11_5_FRONT_3_DIRECT)) {
                totalCount = CalculateCountUtils.get11_5DirectCount(changeBallNumberToBallList(red),
                        changeBallNumberToBallList(blue), changeBallNumberToBallList(dan), PLAY_11_5_FRONT_3_DIRECT);
                modeId = PLAY_11_5_FRONT_3_DIRECT;
            }
        } else if (lotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
            totalCount = CalculateCountUtils.getArrange5Count(red, blue, dan, tuo, other);
        } else if (lotteryType.equals(LOTTERY_TYPE_7_STAR)) {
            totalCount = CalculateCountUtils.get7StarCount(red, blue, dan, tuo, other, six, seven);
        } else if (lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR) || lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            switch (mode) {
                case ALWAYS_COLOR_ORDER_TYPE_5_ALL:
                    modeId = ALWAYS_COLOR_5_ALL;
                    totalCount = CalculateCountUtils.getArrange5Count(red, blue, dan, tuo, other);
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_BIG_SMALL_SINGLE_DOUBLE:
                    modeId = ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
                    totalCount = 1;
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_1_DIRECT:
                    totalCount = other.size();
                    modeId = ALWAYS_COLOR_1_DIRECT;
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_2_GROUP:
                    modeId = ALWAYS_COLOR_2_GROUP;
                    int oneSize = other.size();
                    totalCount = oneSize * (oneSize - 1) / 2;
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_3_DIRECT:
                    modeId = ALWAYS_COLOR_3_DIRECT;
                    totalCount = dan.size() * tuo.size() * other.size();
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_3:
                    modeId = ALWAYS_COLOR_3_GROUP_3;
                    totalCount = other.size() * (other.size() - 1);
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_6:
                    modeId = ALWAYS_COLOR_3_GROUP_6;
                    totalCount = CalculateCountUtils.combination(other.size(), 3);
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_5_DIRECT:
                    totalCount = CalculateCountUtils.getArrange5Count(red, blue, dan, tuo, other);
                    modeId = ALWAYS_COLOR_5_DIRECT;
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_2_DIRECT:
                    modeId = ALWAYS_COLOR_2_DIRECT;
                    totalCount = tuo.size() * other.size();
                    break;
            }
        }

        order.setPlayMode(modeId);
        order.setTotalCount(totalCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(totalCount * (float) SharedPreferenceUtil.get(MyApplication.getAppContext(), lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        order.setRedBall(GsonUtils.getGsonInstance().toJson(red));
        order.setBlueBall(GsonUtils.getGsonInstance().toJson(blue));
        order.setDanBall(GsonUtils.getGsonInstance().toJson(dan));
        order.setTuoBall(GsonUtils.getGsonInstance().toJson(tuo));
        order.setFiveBall(GsonUtils.getGsonInstance().toJson(other));
        order.setSixBall(GsonUtils.getGsonInstance().toJson(six));
        order.setSevenBall(GsonUtils.getGsonInstance().toJson(seven));
        if (totalCount > 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        }
        order.setLotteryType(lotteryType);
        return order;
    }

    public static String getSpannable3dDirectString(LotteryOrder order, int playMode) {

        String ballString = "";
        List<String> redList = GsonUtils.formatStringToStringList(order.getRedBall());
        int redSize = 0;
        if (redList != null) {
            redSize = redList.size();
            Collections.sort(redList);
        }
        if (playMode == THREE_D_PLAY_DIRECT || playMode == ARRANGE_3_PLAY_DIRECT) {
            List<String> tuoList = GsonUtils.formatStringToStringList(order.getTuoBall());
            List<String> danList = GsonUtils.formatStringToStringList(order.getDanBall());

            for (int i = 0; i < redSize; i++) {
                String bean = redList.get(i);
                if (i == redSize - 1) {
                    ballString = ballString + bean + ", ";
                } else {
                    ballString = ballString + bean;
                }
            }

            if (null != danList) {
                Collections.sort(danList);
                int danSize = danList.size();
                for (int i = 0; i < danSize; i++) {
                    String bean = danList.get(i);
                    if (i == danSize - 1) {
                        ballString = ballString + bean + ", ";
                    } else {
                        ballString = ballString + bean;
                    }
                }
            }

            if (null != tuoList) {
                Collections.sort(tuoList);
                int tuoSize = tuoList.size();
                for (int i = 0; i < tuoSize; i++) {
                    String bean = tuoList.get(i);
                    ballString = ballString + bean;
                }
            }
        } else if (playMode == FAST_THREE_2_SAME_ONE) {
            List<String> blueList = GsonUtils.formatStringToStringList(order.getBlueBall());
            Collections.sort(blueList);
            int shiSize = blueList.size();

            for (int i = 0; i < redSize; i++) {
                String bean = redList.get(i);
                if (bean.length() == 1) {
                    bean = bean + bean;
                }
                if (i == 0) {
                    ballString = bean;
                } else {
                    ballString = ballString + ", " + bean;
                }
            }
            if (shiSize == 1) {
                ballString = ballString + " (" + blueList.get(0) + ")";
            } else {
                for (int i = 0; i < shiSize; i++) {
                    String bean = blueList.get(i);
                    if (i == 0) {
                        ballString = ballString + " (" + bean;
                    } else if (i == shiSize - 1) {
                        ballString = ballString + ", " + bean + ")";
                    } else {
                        ballString = ballString + ", " + bean;
                    }
                }
            }
        } else if (playMode == FAST_THREE_3_SAME_ONE) {
            for (int i = 0; i < redSize; i++) {
                String bean = redList.get(i);
                if (bean.length() == 1) {
                    bean = bean + bean + bean;
                }
                if (i == 0) {
                    ballString = bean;
                } else {
                    ballString = ballString + ", " + bean;
                }
            }
        } else if (playMode == FAST_THREE_2_SAME_MORE) {
            for (int i = 0; i < redSize; i++) {
                String bean = redList.get(i);
                if (bean.length() == 1) {
                    bean = bean + bean;
                }
                if (i == 0) {
                    ballString = bean;
                } else {
                    ballString = ballString + ", " + bean;
                }
            }
        } else {
            for (int i = 0; i < redSize; i++) {
                String bean = redList.get(i);
                if (i == 0) {
                    ballString = bean;
                } else {
                    ballString = ballString + ", " + bean;
                }
            }
        }

        return ballString;
    }

    public static BuyHistoryBean createBuyHistoryBean(String mOrderString, String lotteryType) {
        BuyHistoryBean bean = new BuyHistoryBean();
        bean.setCreated_at(DateUtils.formatDate2UTC(System.currentTimeMillis()));
        if (lotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
            DoubleColorOrder order = GsonUtils.formatStringToDCOrder(mOrderString);
            bean.setChase(order.getChase());
            bean.setBets(order.getBets());
            bean.setMultiple(order.getMultiple());
        } else {
            ThreeDOrder order = GsonUtils.formatStringToThreeDOrder(mOrderString);
            bean.setChase(order.getChase());
            bean.setSequence(order.getStart_sequence());
            bean.setMultiple(order.getMultiple());

            List<DoubleColorOrderBet> bets = new ArrayList<>();
            List<ThreeDOrder.ThreeDGroup> group3s = order.getGroup3();
            List<ThreeDOrder.ThreeDGroup> group6s = order.getGroup6();
            List<ThreeDOrder.ThreeDDirect> directs = order.getDirect();
            for (ThreeDOrder.ThreeDGroup tg : group3s) {
                DoubleColorOrderBet bet = new DoubleColorOrderBet();
                bet.setRed(tg.getDigit());
                bet.setMode(TD_PLAY_GROUP_THREE);
                bets.add(bet);
            }

            for (ThreeDOrder.ThreeDGroup tg : group6s) {
                DoubleColorOrderBet bet = new DoubleColorOrderBet();
                bet.setRed(tg.getDigit());
                bet.setMode(TD_PLAY_GROUP_SIX);
                bets.add(bet);
            }

            for (ThreeDOrder.ThreeDDirect tg : directs) {
                DoubleColorOrderBet bet = new DoubleColorOrderBet();
                bet.setRed(tg.getHundred());
                bet.setDan(tg.getTen());
                bet.setTuo(tg.getOne());
                bet.setMode(TD_PLAY_DIRECT);
                bets.add(bet);
            }
            bean.setBets(bets);
        }
        return bean;
    }

    public static List<DoubleColorOrderBet> switchA3BillToDCBet(BillDetailThreeD.ThreeDBill bill) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        if (bill == null) {
            return bets;
        }
        List<BillDetailThreeD.ThreeDGroup> group3s = bill.getGroup3();
        List<BillDetailThreeD.ThreeDGroup> group6s = bill.getGroup6();
        List<ThreeDOrder.ThreeDDirect> directs = bill.getDirect();
        for (BillDetailThreeD.ThreeDGroup tg : group3s) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getDigit());
            bet.setMode(A3_PLAY_GROUP_THREE);
            bets.add(bet);
        }

        for (BillDetailThreeD.ThreeDGroup tg : group6s) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getDigit());
            bet.setMode(A3_PLAY_GROUP_SIX);
            bets.add(bet);
        }

        for (ThreeDOrder.ThreeDDirect tg : directs) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getHundred());
            bet.setDan(tg.getTen());
            bet.setTuo(tg.getOne());
            bet.setMode(A3_PLAY_DIRECT);
            bets.add(bet);
        }
        return bets;
    }

    public static List<DoubleColorOrderBet> switchThreeDBillToDCBet(BillDetailThreeD.ThreeDBill bill) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        if (bill == null) {
            return bets;
        }
        List<BillDetailThreeD.ThreeDGroup> group3s = bill.getGroup3();
        List<BillDetailThreeD.ThreeDGroup> group6s = bill.getGroup6();
        List<ThreeDOrder.ThreeDDirect> directs = bill.getDirect();
        for (BillDetailThreeD.ThreeDGroup tg : group3s) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getDigit());
            bet.setMode(TD_PLAY_GROUP_THREE);
            bets.add(bet);
        }

        for (BillDetailThreeD.ThreeDGroup tg : group6s) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getDigit());
            bet.setMode(TD_PLAY_GROUP_SIX);
            bets.add(bet);
        }

        for (ThreeDOrder.ThreeDDirect tg : directs) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getHundred());
            bet.setDan(tg.getTen());
            bet.setTuo(tg.getOne());
            bet.setMode(TD_PLAY_DIRECT);
            bets.add(bet);
        }
        return bets;
    }

    /**
     * 将快三投注号码转成string
     *
     * @param mSelectButtons
     * @return
     */
    public static String changeFT2BallNumber(List<LotteryBall> mSelectButtons) {
        List<String> ballNumber = new ArrayList<>();
        for (int i = 0; i < mSelectButtons.size(); i++) {
            ballNumber.add(mSelectButtons.get(i).getNumber() + "");
        }
        return GsonUtils.getGsonInstance().toJson(ballNumber);
    }

    public static String[] mFastThree2SameSingleDifferentNumbers = new String[]{"1", "2", "3", "4", "5", "6"};

    public static List<String> randomSelectFastThree2SameSingleDifferentNumber(int i, String ignoreNumber) {
        List<String> strings = new ArrayList<>();
        if (!TextUtils.isEmpty(ignoreNumber) && ignoreNumber.length() > 1) {
            i = i + 1;
            strings.add(ignoreNumber.substring(0, 1));
        }
        int length = mFastThree2SameSingleDifferentNumbers.length;
        Random rand = new Random();
        while (strings.size() < i) {
            int num = rand.nextInt(length);
            String str = mFastThree2SameSingleDifferentNumbers[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        if (!TextUtils.isEmpty(ignoreNumber) && ignoreNumber.length() > 1) {
            strings.remove(ignoreNumber.substring(0, 1));
        }
        return strings;
    }

    public static String[] mFastThree2SameSingleSameNumbers = new String[]{"11", "22", "33", "44", "55", "66"};

    public static List<String> randomSelectFastThree2SameSingleSameNumber(int i) {
        List<String> strings = new ArrayList<>();
        int length = mFastThree2SameSingleSameNumbers.length;
        Random rand = new Random();
        while (strings.size() < i) {
            int num = rand.nextInt(length);
            String str = mFastThree2SameSingleSameNumbers[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    //==================================================创建订单start=======================================================
    public static FastThreeOrder createFastThreeOrderNew(int chase, int multiple, List<LotteryOrder> mOrders, String mSequence) {
        FastThreeOrder order = new FastThreeOrder();
        order.setChase(chase);
        order.setTimes(multiple);
        order.setPeriod(mSequence);
        List<FastThreeOrder.FastThreeBet> bets = new ArrayList<>();
        for (LotteryOrder localOrder : mOrders) {
            int playMode = localOrder.getPlayMode();
            switch (playMode) {
                case FAST_THREE_SUM://和值
                    FastThreeOrder.FastThreeBet sumBet = new FastThreeOrder.FastThreeBet();
                    sumBet.setGameplay(FAST_THREE_ORDER_TYPE_SUM);
                    sumBet.setNumbers(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case FAST_THREE_3_DIFFERENT://三不同号
                    FastThreeOrder.FastThreeBet differentBet = new FastThreeOrder.FastThreeBet();
                    differentBet.setGameplay(FAST_THREE_ORDER_TYPE_3_DIFFERENT);
                    differentBet.setNumbers(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(differentBet);
                    break;
                case FAST_THREE_3_SAME_ALL://三同号通选
                    FastThreeOrder.FastThreeBet threeAllBet = new FastThreeOrder.FastThreeBet();
                    threeAllBet.setGameplay(FAST_THREE_ORDER_TYPE_3_SAME_ALL);
                    //                    threeAllBet.setNumbers(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(threeAllBet);
                    break;
                case FAST_THREE_3_SAME_ONE://三同号单选
                    FastThreeOrder.FastThreeBet threeOneBet = new FastThreeOrder.FastThreeBet();
                    threeOneBet.setGameplay(FAST_THREE_ORDER_TYPE_3_SAME_ONE);
                    List<String> threeOneNumbers = GsonUtils.formatStringToStringList(localOrder.getRedBall());
                    List<String> newThreeOneNumbers = new ArrayList<>();
                    for (String number : threeOneNumbers) {
                        newThreeOneNumbers.add(number.substring(0, 1));
                    }
                    threeOneBet.setTriple(newThreeOneNumbers);
                    bets.add(threeOneBet);
                    break;
                case FAST_THREE_2_DIFFERENT://二不同号
                    FastThreeOrder.FastThreeBet towDifferentBet = new FastThreeOrder.FastThreeBet();
                    towDifferentBet.setGameplay(FAST_THREE_ORDER_TYPE_2_DIFFERENT);
                    towDifferentBet.setNumbers(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(towDifferentBet);
                    break;
                case FAST_THREE_2_SAME_MORE://二同号复选
                    FastThreeOrder.FastThreeBet towMoreBet = new FastThreeOrder.FastThreeBet();
                    towMoreBet.setGameplay(FAST_THREE_ORDER_TYPE_2_SAME_MORE);
                    List<String> towMoreNumbers = GsonUtils.formatStringToStringList(localOrder.getRedBall());
                    List<String> newTowMoreNumbers = new ArrayList<>();
                    for (String number : towMoreNumbers) {
                        newTowMoreNumbers.add(number.substring(0, 1));
                    }
                    towMoreBet.setDouble_number(newTowMoreNumbers);
                    bets.add(towMoreBet);
                    break;
                case FAST_THREE_2_SAME_ONE://二同号单选
                    FastThreeOrder.FastThreeBet towOneBet = new FastThreeOrder.FastThreeBet();
                    towOneBet.setGameplay(FAST_THREE_ORDER_TYPE_2_SAME_ONE);
                    List<String> towOneSameNumbers = GsonUtils.formatStringToStringList(localOrder.getRedBall());
                    List<String> newTowOneSameNumbers = new ArrayList<>();
                    for (String number : towOneSameNumbers) {
                        newTowOneSameNumbers.add(number.substring(0, 1));
                    }
                    towOneBet.setDouble_number(newTowOneSameNumbers);
                    towOneBet.setNumbers(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(towOneBet);
                    break;
                case FAST_THREE_3_TO_ALL://三连号通选
                    FastThreeOrder.FastThreeBet threeToAllBet = new FastThreeOrder.FastThreeBet();
                    threeToAllBet.setGameplay(FAST_THREE_ORDER_TYPE_3_TO_ALL);
                    bets.add(threeToAllBet);
                    break;
            }
        }
        order.setStakes(bets);
        return order;
    }

    public static ElevenFiveOrder create115Order(int chase, int multiple, List<LotteryOrder> mOrders, String mSequence) {
        ElevenFiveOrder order = new ElevenFiveOrder();
        order.setChase(chase);
        order.setTimes(multiple);
        order.setPeriod(mSequence);
        List<ElevenFiveOrder.ElvenFiveBet> bets = new ArrayList<>();
        for (LotteryOrder localOrder : mOrders) {
            int playMode = localOrder.getPlayMode();
            ElevenFiveOrder.ElvenFiveBet sumBet = new ElevenFiveOrder.ElvenFiveBet();
            switch (playMode) {
                case PLAY_11_5_ANY_2://任选二
                    sumBet.setGameplay(ORDER_11_5_ANY_2);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_3://任选三
                    sumBet.setGameplay(ORDER_11_5_ANY_3);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_4://任选四
                    sumBet.setGameplay(ORDER_11_5_ANY_4);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_5://任选五
                    sumBet.setGameplay(ORDER_11_5_ANY_5);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_6://任选六
                    sumBet.setGameplay(ORDER_11_5_ANY_6);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_7://任选七
                    sumBet.setGameplay(ORDER_11_5_ANY_7);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_8://任选八
                    sumBet.setGameplay(ORDER_11_5_ANY_8);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_FRONT_2_GROUP://前二组选
                    sumBet.setGameplay(ORDER_11_5_FRONT_2_GROUP);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_FRONT_3_GROUP://前三组选
                    sumBet.setGameplay(ORDER_11_5_FRONT_3_GROUP);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_FRONT_1_DIRECT://前一直选
                    sumBet.setGameplay(ORDER_11_5_FRONT_1_DIRECT);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_FRONT_2_DIRECT://前二直选
                    sumBet.setGameplay(ORDER_11_5_FRONT_2_DIRECT);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_FRONT_3_DIRECT://前三直选
                    sumBet.setGameplay(ORDER_11_5_FRONT_3_DIRECT);
                    sumBet.setIs_dan_tuo(1);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    sumBet.setNumbers_c(GsonUtils.formatStringToStringList(localOrder.getDanBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_2_DAN://任选二胆拖
                    sumBet.setGameplay(ORDER_11_5_ANY_2);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_3_DAN://任选三胆拖
                    sumBet.setGameplay(ORDER_11_5_ANY_3);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_4_DAN://任选四胆拖
                    sumBet.setGameplay(ORDER_11_5_ANY_4);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_5_DAN://任选五胆拖
                    sumBet.setGameplay(ORDER_11_5_ANY_5);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_6_DAN://任选六胆拖
                    sumBet.setGameplay(ORDER_11_5_ANY_6);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_7_DAN://任选七胆拖
                    sumBet.setGameplay(ORDER_11_5_ANY_7);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_ANY_8_DAN://任选八胆拖
                    sumBet.setGameplay(ORDER_11_5_ANY_8);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_FRONT_2_GROUP_DAN://前二组选胆拖
                    sumBet.setGameplay(ORDER_11_5_FRONT_2_GROUP);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;
                case PLAY_11_5_FRONT_3_GROUP_DAN://前三组选胆拖
                    sumBet.setGameplay(ORDER_11_5_FRONT_3_GROUP);
                    sumBet.setIs_dan_tuo(2);
                    sumBet.setNumbers_a(GsonUtils.formatStringToStringList(localOrder.getRedBall()));
                    sumBet.setNumbers_b(GsonUtils.formatStringToStringList(localOrder.getBlueBall()));
                    bets.add(sumBet);
                    break;

            }
        }
        order.setStakes(bets);
        return order;
    }

    public static AlwaysColorOrder createACOrder(int chase, int multiple, List<LotteryOrder> mOrders, String sequence) {
        AlwaysColorOrder order = new AlwaysColorOrder();
        order.setChase(chase);
        order.setTimes(multiple);
        order.setPeriod(sequence);
        List<AlwaysColorOrder.AlwaysColorBet> bets = new ArrayList();
        for (int i = 0; i < mOrders.size(); i++) {
            LotteryOrder lotteryOrder = mOrders.get(i);
            int playMode = lotteryOrder.getPlayMode();
            AlwaysColorOrder.AlwaysColorBet bet = new AlwaysColorOrder.AlwaysColorBet();
            switch (playMode) {
                case ALWAYS_COLOR_5_ALL:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_5_ALL);
                    List<String> wanBalls = GsonUtils.formatStringToStringList(lotteryOrder.getRedBall());
                    bet.setNumbers_a(wanBalls);
                    List<String> qianBalls = GsonUtils.formatStringToStringList(lotteryOrder.getBlueBall());
                    bet.setNumbers_b(qianBalls);
                    List<String> baiBalls = GsonUtils.formatStringToStringList(lotteryOrder.getDanBall());
                    bet.setNumbers_c(baiBalls);
                    List<String> shiBalls = GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall());
                    bet.setNumbers_d(shiBalls);
                    List<String> geBalls = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBalls);
                    break;
                case ALWAYS_COLOR_5_DIRECT:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_5_DIRECT);
                    List<String> wanBalls1 = GsonUtils.formatStringToStringList(lotteryOrder.getRedBall());
                    bet.setNumbers_a(wanBalls1);
                    List<String> qianBalls1 = GsonUtils.formatStringToStringList(lotteryOrder.getBlueBall());
                    bet.setNumbers_b(qianBalls1);
                    List<String> baiBalls1 = GsonUtils.formatStringToStringList(lotteryOrder.getDanBall());
                    bet.setNumbers_c(baiBalls1);
                    List<String> shiBalls1 = GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall());
                    bet.setNumbers_d(shiBalls1);
                    List<String> geBalls1 = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBalls1);
                    break;
                case ALWAYS_COLOR_1_DIRECT:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_1_DIRECT);
                    List<String> geBalls3 = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBalls3);
                    break;
                case ALWAYS_COLOR_2_GROUP:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_2_GROUP);
                    List<String> geBalls4 = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBalls4);
                    break;
                case ALWAYS_COLOR_3_DIRECT:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_3_DIRECT);
                    List<String> baiBalls5 = GsonUtils.formatStringToStringList(lotteryOrder.getDanBall());
                    bet.setNumbers_c(baiBalls5);
                    List<String> shiBalls5 = GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall());
                    bet.setNumbers_d(shiBalls5);
                    List<String> geBalls5 = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBalls5);
                    break;
                case ALWAYS_COLOR_3_GROUP_3:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_3_GROUP_3);
                    List<String> geBallG3 = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBallG3);
                    break;
                case ALWAYS_COLOR_3_GROUP_6:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_3_GROUP_6);
                    List<String> geBallG6 = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBallG6);
                    break;
                case ALWAYS_COLOR_2_DIRECT:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_2_DIRECT);
                    List<String> shiBalls6 = GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall());
                    bet.setNumbers_d(shiBalls6);
                    List<String> geBalls6 = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(geBalls6);
                    break;
                case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                    bet.setGameplay(ALWAYS_COLOR_ORDER_TYPE_BIG_SMALL_SINGLE_DOUBLE);
                    List<String> dxdsShi = GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall());
                    bet.setNumbers_d(getDXDSIntList(dxdsShi));
                    List<String> dxdsGe = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
                    bet.setNumbers_e(getDXDSIntList(dxdsGe));
                    break;
            }
            bets.add(bet);
        }
        order.setStakes(bets);
        return order;
    }

    //时时彩订单格式转换成数据库存储格式
    public static void orderToDBAC(String lotteryType, List<AlwaysColorOrder.AlwaysColorBet> stacks) {
        if (null == stacks) {
            return;
        }
        for (int i = 0; i < stacks.size(); i++) {
            LotteryOrder lotteryOrder = new LotteryOrder();
            AlwaysColorOrder.AlwaysColorBet bet = stacks.get(i);
            String gamePlay = bet.getGameplay();
            switch (gamePlay) {
                case ALWAYS_COLOR_ORDER_TYPE_5_ALL:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_5_ALL);
                    lotteryOrder.setRedBall(GsonUtil.getInstance().toJson(bet.getNumbers_a()));
                    lotteryOrder.setBlueBall(GsonUtil.getInstance().toJson(bet.getNumbers_b()));
                    lotteryOrder.setDanBall(GsonUtil.getInstance().toJson(bet.getNumbers_c()));
                    lotteryOrder.setTuoBall(GsonUtil.getInstance().toJson(bet.getNumbers_d()));
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
                case ALWAYS_COLOR_ORDER_TYPE_5_DIRECT:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_5_DIRECT);
                    lotteryOrder.setRedBall(GsonUtil.getInstance().toJson(bet.getNumbers_a()));
                    lotteryOrder.setBlueBall(GsonUtil.getInstance().toJson(bet.getNumbers_b()));
                    lotteryOrder.setDanBall(GsonUtil.getInstance().toJson(bet.getNumbers_c()));
                    lotteryOrder.setTuoBall(GsonUtil.getInstance().toJson(bet.getNumbers_d()));
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
                case ALWAYS_COLOR_ORDER_TYPE_1_DIRECT:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_1_DIRECT);
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
                case ALWAYS_COLOR_ORDER_TYPE_2_GROUP:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_2_GROUP);
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
                case ALWAYS_COLOR_ORDER_TYPE_3_DIRECT:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_3_DIRECT);
                    lotteryOrder.setDanBall(GsonUtil.getInstance().toJson(bet.getNumbers_c()));
                    lotteryOrder.setTuoBall(GsonUtil.getInstance().toJson(bet.getNumbers_d()));
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
                case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_3://只处理组三
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_3_GROUP_3);
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    break;
                case ALWAYS_COLOR_ORDER_TYPE_3_GROUP_6:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_3_GROUP_6);
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
                case ALWAYS_COLOR_ORDER_TYPE_2_DIRECT:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_2_DIRECT);
                    lotteryOrder.setTuoBall(GsonUtil.getInstance().toJson(bet.getNumbers_d()));
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
                case ALWAYS_COLOR_ORDER_TYPE_BIG_SMALL_SINGLE_DOUBLE:
                    lotteryOrder.setPlayMode(ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE);
                    lotteryOrder.setTuoBall(GsonUtil.getInstance().toJson(bet.getNumbers_d()));
                    lotteryOrder.setFiveBall(GsonUtil.getInstance().toJson(bet.getNumbers_e()));
                    continue;
            }
            long count = CalculateCountUtils.getACCount(lotteryOrder.getPlayMode(), null == bet.getNumbers_a() ? 0 : bet.getNumbers_a().size()
                    , null == bet.getNumbers_b() ? 0 : bet.getNumbers_b().size()
                    , null == bet.getNumbers_c() ? 0 : bet.getNumbers_c().size()
                    , null == bet.getNumbers_d() ? 0 : bet.getNumbers_d().size()
                    , null == bet.getNumbers_e() ? 0 : bet.getNumbers_e().size());
            lotteryOrder.setTotalCount(count);
            lotteryOrder.setTotalMoney(StringUtils.getNumberNoZero(count * (float) SharedPreferenceUtil.get(MyApplication.getAppContext(), lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
            if (count > 1) {
                lotteryOrder.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
            } else {
                lotteryOrder.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
            }
            lotteryOrder.setLotteryType(lotteryType);
            LotteryOrderDbUtils.addLotteryOrder(lotteryOrder);
        }
    }
    //==================================================创建订单end=======================================================

    //大小单双数组文字转数字
    public static List<String> getDXDSIntList(List<String> bssd) {
        if (null != bssd) {
            List<String> newNums = new ArrayList<>();
            for (String num : bssd) {
                switch (num) {
                    case "大":
                        newNums.add("1");
                        break;
                    case "小":
                        newNums.add("2");
                        break;
                    case "单":
                        newNums.add("3");
                        break;
                    case "双":
                        newNums.add("4");
                        break;
                }
            }
            return newNums;
        }
        return new ArrayList<>();
    }

    //大小单双数组数字转文字
    public static List<String> getDXDSStringList(List<String> bssd) {
        if (null != bssd) {
            List<String> newNums = new ArrayList<>();
            for (String num : bssd) {
                switch (num) {
                    case "1":
                        newNums.add("大");
                        break;
                    case "2":
                        newNums.add("小");
                        break;
                    case "3":
                        newNums.add("单");
                        break;
                    case "4":
                        newNums.add("双");
                        break;
                }
            }
            return newNums;
        }
        return new ArrayList<>();
    }

    public static List<DoubleColorOrderBet> switchFastThreeBillToDCBet(BillDetailFastThree bill) {
        if (bill == null) {
            return new ArrayList<DoubleColorOrderBet>();
        }


        List<FastThreeOrderOld.FastThreeBet> fastThreeBet3 = bill.getBets();
        List<DoubleColorOrderBet> bets = switchFastThreeBetToDCBet(fastThreeBet3);
        return bets;
    }

    public static List<DoubleColorOrderBet> switchFastThreeBetToDCBet(List<FastThreeOrderOld.FastThreeBet> fastThreeBet3) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        for (FastThreeOrderOld.FastThreeBet tg : fastThreeBet3) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            String modelStr = tg.getMode();
            if (modelStr.equals(FAST_THREE_ORDER_TYPE_2_DIFFERENT)) {
                bet.setRed(tg.getNumber());
            } else if (modelStr.equals(FAST_THREE_ORDER_TYPE_SUM)) {
                bet.setRed(tg.getNumber());
            } else if (modelStr.equals(FAST_THREE_ORDER_TYPE_2_SAME_MORE)) {
                bet.setRed(tg.getDouble_number());
            } else if (modelStr.equals(FAST_THREE_ORDER_TYPE_2_SAME_ONE)) {
                bet.setRed(tg.getDouble_number());
                bet.setBlue(tg.getNumber());
            } else if (modelStr.equals(FAST_THREE_ORDER_TYPE_3_DIFFERENT)) {
                bet.setRed(tg.getNumber());
            } else if (modelStr.equals(FAST_THREE_ORDER_TYPE_3_SAME_ALL)) {
                List<String> numbers = new ArrayList<>();
                numbers.add("111");
                numbers.add("222");
                numbers.add("333");
                numbers.add("444");
                numbers.add("555");
                numbers.add("666");
                bet.setRed(numbers);
            } else if (modelStr.equals(FAST_THREE_ORDER_TYPE_3_SAME_ONE)) {
                bet.setRed(tg.getTriple());
            } else if (modelStr.equals(FAST_THREE_ORDER_TYPE_3_TO_ALL)) {
                List<String> numbers = new ArrayList<>();
                numbers.add("123");
                numbers.add("234");
                numbers.add("345");
                numbers.add("456");
                bet.setRed(numbers);
            }
            bet.setMode(modelStr);
            bets.add(bet);
        }
        return bets;
    }

    public static String getSpannableACString(LotteryOrder order, Integer playMode) {
        List<String> wanBallList = GsonUtils.formatStringToStringList(order.getRedBall());
        List<String> qianBallList = GsonUtils.formatStringToStringList(order.getBlueBall());
        List<String> baiBallList = GsonUtils.formatStringToStringList(order.getDanBall());
        List<String> shiBallList = GsonUtils.formatStringToStringList(order.getTuoBall());
        List<String> geBallList = GsonUtils.formatStringToStringList(order.getFiveBall());
        if (playMode == ALWAYS_COLOR_3_GROUP_6 || playMode == ALWAYS_COLOR_3_GROUP_3 || playMode == ALWAYS_COLOR_2_GROUP) {
            int geSize = geBallList == null ? 0 : geBallList.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < geSize; i++) {
                sb.append(geBallList.get(i));
                sb.append(" ");
            }
            return sb.toString();
        } else if (playMode == ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE) {
            if (null == shiBallList || null == geBallList) {
                return "";
            }
            StringBuilder sbBig = new StringBuilder();
            int geSize = geBallList == null ? 0 : geBallList.size();
            sbBig.append(shiBallList.get(0));
            sbBig.append(" ");
            sbBig.append(geBallList.get(0));
            return sbBig.toString();
        }
        return getStringByStringList(wanBallList, qianBallList,
                baiBallList, shiBallList, geBallList, null, null);
    }

    public static String getSpannable7StarString(LotteryOrder order) {
        List<String> wanBallList = GsonUtils.formatStringToStringList(order.getRedBall());
        List<String> qianBallList = GsonUtils.formatStringToStringList(order.getBlueBall());
        List<String> baiBallList = GsonUtils.formatStringToStringList(order.getDanBall());
        List<String> shiBallList = GsonUtils.formatStringToStringList(order.getTuoBall());
        List<String> geBallList = GsonUtils.formatStringToStringList(order.getFiveBall());
        List<String> sixBallList = GsonUtils.formatStringToStringList(order.getSixBall());
        List<String> sevenBallList = GsonUtils.formatStringToStringList(order.getSevenBall());

        return getStringByStringList(wanBallList, qianBallList,
                baiBallList, shiBallList, geBallList, sixBallList, sevenBallList);
    }

    public static String getSpannable11_5String(LotteryOrder order, Integer playMode) {
        List<String> wanBallList = GsonUtils.formatStringToStringList(order.getRedBall());
        List<String> qianBallList = GsonUtils.formatStringToStringList(order.getBlueBall());
        List<String> baiBallList = GsonUtils.formatStringToStringList(order.getDanBall());
        StringBuilder number = new StringBuilder();
        switch (playMode) {
            case PLAY_11_5_ANY_2:
            case PLAY_11_5_ANY_3:
            case PLAY_11_5_ANY_4:
            case PLAY_11_5_ANY_5:
            case PLAY_11_5_ANY_6:
            case PLAY_11_5_ANY_7:
            case PLAY_11_5_ANY_8:
            case PLAY_11_5_FRONT_1_DIRECT:
            case PLAY_11_5_FRONT_2_GROUP:
            case PLAY_11_5_FRONT_3_GROUP:
                if (null != wanBallList) {
                    for (String num : wanBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                    if (number.length() > 0) {
                        number.delete(number.length() - 1, number.length());
                    }
                }
                break;
            case PLAY_11_5_FRONT_2_DIRECT:
                if (null != wanBallList) {
                    for (String num : wanBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                    number.append("| ");
                }
                if (null != qianBallList) {
                    for (String num : qianBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                }
                break;
            case PLAY_11_5_FRONT_3_DIRECT:
                if (null != wanBallList) {
                    for (String num : wanBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                    number.append("| ");
                }
                if (null != qianBallList) {
                    for (String num : qianBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                    number.append("| ");
                }
                if (null != baiBallList) {
                    for (String num : baiBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                }
                break;
            case PLAY_11_5_ANY_2_DAN:
            case PLAY_11_5_ANY_3_DAN:
            case PLAY_11_5_ANY_4_DAN:
            case PLAY_11_5_ANY_5_DAN:
            case PLAY_11_5_ANY_6_DAN:
            case PLAY_11_5_ANY_7_DAN:
            case PLAY_11_5_ANY_8_DAN:
            case PLAY_11_5_FRONT_2_GROUP_DAN:
            case PLAY_11_5_FRONT_3_GROUP_DAN:
                if (null != wanBallList) {
                    number.append("(");
                    for (String num : wanBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                    number.delete(number.length() - 1, number.length());
                    number.append(")");
                    number.append(" ");
                }
                if (null != qianBallList) {
                    for (String num : qianBallList) {
                        number.append(num);
                        number.append(" ");
                    }
                }
                break;
        }
        return number.toString();
    }

    public static String getStringByStringList(List<String> wanBallList,
                                               List<String> qianBallList,
                                               List<String> baiBallList,
                                               List<String> shiBallList,
                                               List<String> geBallList,
                                               List<String> sixBallList,
                                               List<String> sevenBallList) {
        int wanSize = wanBallList == null ? 0 : wanBallList.size();
        int qianSize = qianBallList == null ? 0 : qianBallList.size();
        int baiSize = baiBallList == null ? 0 : baiBallList.size();
        int shiSize = shiBallList == null ? 0 : shiBallList.size();
        int geSize = geBallList == null ? 0 : geBallList.size();
        int sixSize = sixBallList == null ? 0 : sixBallList.size();
        int sevenSize = sevenBallList == null ? 0 : sevenBallList.size();

        String ballString = "";
        if (wanSize == 0) {
            ballString = ballString + "_ ";
        } else if (wanSize == 1) {
            ballString = ballString + wanBallList.get(0) + "  ";
        } else {
            Collections.sort(wanBallList);
            for (int i = 0; i < wanSize; i++) {
                String bean = wanBallList.get(i);
                if (i == 0) {
                    ballString = bean;
                } else if (i == wanSize - 1) {
                    ballString = ballString + bean + "  ";
                } else {
                    ballString = ballString + bean;
                }
            }
        }
        if (qianSize == 0) {
            ballString = ballString + "_ ";
        } else if (qianSize == 1) {
            ballString = ballString + qianBallList.get(0) + "  ";
        } else {
            Collections.sort(qianBallList);
            for (int i = 0; i < qianSize; i++) {
                String bean = qianBallList.get(i);
                if (i == 0) {
                    ballString = ballString + bean;
                } else if (i == qianSize - 1) {
                    ballString = ballString + bean + "  ";
                } else {
                    ballString = ballString + bean;
                }
            }
        }

        if (baiSize == 0) {
            ballString = ballString + "_ ";
        } else if (baiSize == 1) {
            ballString = ballString + baiBallList.get(0) + "  ";
        } else {
            Collections.sort(baiBallList);
            for (int i = 0; i < baiSize; i++) {
                String bean = baiBallList.get(i);
                if (i == 0) {
                    ballString = ballString + bean;
                } else if (i == baiSize - 1) {
                    ballString = ballString + bean + "  ";
                } else {
                    ballString = ballString + bean;
                }
            }
        }

        if (shiSize == 0) {
            ballString = ballString + "_ ";
        } else if (shiSize == 1) {
            ballString = ballString + shiBallList.get(0) + "  ";
        } else {
            Collections.sort(shiBallList);
            for (int i = 0; i < shiSize; i++) {
                String bean = shiBallList.get(i);
                if (i == 0) {
                    ballString = ballString + bean;
                } else if (i == shiSize - 1) {
                    ballString = ballString + bean + "  ";
                } else {
                    ballString = ballString + bean;
                }
            }
        }

        if (geSize == 0) {
            ballString = ballString + "—";
        } else if (geSize == 1) {
            ballString = ballString + geBallList.get(0) + "  ";
        } else {
            Collections.sort(geBallList);
            for (int i = 0; i < geSize; i++) {
                String bean = geBallList.get(i);
                if (i == 0) {
                    ballString = ballString + bean;
                } else if (i == geSize - 1) {
                    ballString = ballString + bean + "  ";
                } else {
                    ballString = ballString + bean;
                }
            }
        }

        if (sixSize == 0) {
            ballString = ballString + "";
        } else if (sixSize == 1) {
            ballString = ballString + sixBallList.get(0) + "  ";
        } else {
            Collections.sort(sixBallList);
            for (int i = 0; i < sixSize; i++) {
                String bean = sixBallList.get(i);
                if (i == 0) {
                    ballString = ballString + bean;
                } else if (i == sixSize - 1) {
                    ballString = ballString + bean + "  ";
                } else {
                    ballString = ballString + bean;
                }
            }
        }


        if (sevenSize == 0) {
            ballString = ballString + "";
        } else if (sevenSize == 1) {
            ballString = ballString + sevenBallList.get(0);
        } else {
            Collections.sort(sevenBallList);
            for (int i = 0; i < sevenSize; i++) {
                String bean = sevenBallList.get(i);
                if (i == 0) {
                    ballString = ballString + bean;
                } else if (i == sevenSize - 1) {
                    ballString = ballString + bean;
                } else {
                    ballString = ballString + bean;
                }
            }
        }
        return ballString;
    }

    public static List<String> randomBigSmallSingleDouble(int i) {
        List<String> strings = new ArrayList<>();
        int length = mBigSmallSingleDouble.length;
        Random rand = new Random();
        while (strings.size() < i) {
            int num = rand.nextInt(length);
            String str = mBigSmallSingleDouble[num];
            if (!strings.contains(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    public static Arrange5Order createArrange5Order(int chase, int multiple, List<LotteryOrder> mOrders, String sequence) {
        Arrange5Order order = new Arrange5Order();
        order.setChase(chase);
        order.setMultiple(multiple);
        order.setStart_sequence(sequence);
        List<Arrange5Order.Arrange5Bet> bets = new ArrayList();
        for (int i = 0; i < mOrders.size(); i++) {
            LotteryOrder lotteryOrder = mOrders.get(i);
            Arrange5Order.Arrange5Bet bet = new Arrange5Order.Arrange5Bet();
            List<String> wanBalls = GsonUtils.formatStringToStringList(lotteryOrder.getRedBall());
            bet.setTenthousand(wanBalls);
            List<String> qianBalls = GsonUtils.formatStringToStringList(lotteryOrder.getBlueBall());
            bet.setThousand(qianBalls);
            List<String> baiBalls = GsonUtils.formatStringToStringList(lotteryOrder.getDanBall());
            bet.setHundred(baiBalls);
            List<String> shiBalls = GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall());
            bet.setTen(shiBalls);
            List<String> geBalls = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
            bet.setOne(geBalls);
            bets.add(bet);
        }
        order.setDirect(bets);
        return order;
    }

    public static SevenStarOrder create7StarOrder(int chase, int multiple, List<LotteryOrder> mOrders, String sequence) {
        SevenStarOrder order = new SevenStarOrder();
        order.setChase(chase);
        order.setMultiple(multiple);
        order.setStart_sequence(sequence);
        List<SevenStarOrder.SevenStarBet> bets = new ArrayList();
        for (int i = 0; i < mOrders.size(); i++) {
            LotteryOrder lotteryOrder = mOrders.get(i);
            SevenStarOrder.SevenStarBet bet = new SevenStarOrder.SevenStarBet();
            List<String> one = GsonUtils.formatStringToStringList(lotteryOrder.getRedBall());
            bet.setOne(one);
            List<String> two = GsonUtils.formatStringToStringList(lotteryOrder.getBlueBall());
            bet.setTwo(two);
            List<String> three = GsonUtils.formatStringToStringList(lotteryOrder.getDanBall());
            bet.setThree(three);
            List<String> four = GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall());
            bet.setFour(four);
            List<String> five = GsonUtils.formatStringToStringList(lotteryOrder.getFiveBall());
            bet.setFive(five);
            List<String> six = GsonUtils.formatStringToStringList(lotteryOrder.getSixBall());
            bet.setSix(six);
            List<String> seven = GsonUtils.formatStringToStringList(lotteryOrder.getSevenBall());
            bet.setSeven(seven);
            bets.add(bet);
        }
        order.setDirect(bets);
        return order;
    }

    public static SevenHappyOrder createSevenHappy(int chase, int multiple, List<LotteryOrder> mOrders, String mSequence) {
        SevenHappyOrder order = new SevenHappyOrder();
        order.setSequence(mSequence);
        order.setChase(chase);
        order.setMultiple(multiple);
        List<SevenHappyOrder.SevenHappyOrderPlain> plain = new ArrayList<>();
        List<SevenHappyOrder.SevenHappyOrderDan> dantuo = new ArrayList();
        for (LotteryOrder lotteryOrder : mOrders) {
            SevenHappyOrder.SevenHappyOrderPlain p = new SevenHappyOrder.SevenHappyOrderPlain();
            p.setNumber(GsonUtils.formatStringToStringList(lotteryOrder.getRedBall()));
            plain.add(p);
            SevenHappyOrder.SevenHappyOrderDan dan = new SevenHappyOrder.SevenHappyOrderDan();
            dan.setDan(GsonUtils.formatStringToStringList(lotteryOrder.getDanBall()));
            dan.setTuo(GsonUtils.formatStringToStringList(lotteryOrder.getTuoBall()));
            //            dantuo.add(dan);
        }
        order.setPlain(plain);
        order.setDantuo(dantuo);

        return order;
    }

    public static List<DoubleColorOrderBet> switchSevenHappyBillToDCBet(BillDetailSevenHappy.SevenHappyBillDetail bill) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        if (bill == null) {
            return bets;
        }
        List<SevenHappyOrder.SevenHappyOrderPlain> plain = bill.getPlain();
        bets = switchSH2DCBet(plain);
        return bets;
    }

    public static List<DoubleColorOrderBet> switchSH2DCBet(List<SevenHappyOrder.SevenHappyOrderPlain> plain) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        for (SevenHappyOrder.SevenHappyOrderPlain tg : plain) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            List<String> numbers = tg.getNumber();
            bet.setRed(numbers);
            bet.setMode(SEVEN_HAPPY_ORDER_NORMAL);
            bets.add(bet);
        }
        return bets;
    }

    public static List<DoubleColorOrderBet> switchArrange5BillToDCBet(BillDetailArrange5.Arrange5Direct arrange5) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        if (arrange5 == null) {
            return bets;
        }
        List<Arrange5Order.Arrange5Bet> arrange5Bet = arrange5.getDirect();
        bets = switchA52DCBet(arrange5Bet);
        return bets;
    }

    public static List<DoubleColorOrderBet> switchA52DCBet(List<Arrange5Order.Arrange5Bet> fastThreeBet3) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        for (Arrange5Order.Arrange5Bet tg : fastThreeBet3) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getTenthousand());
            bet.setBlue(tg.getThousand());
            bet.setDan(tg.getHundred());
            bet.setTuo(tg.getTen());
            bet.setFive(tg.getOne());
            bet.setMode(A5_PLAY_DIRECT);
            bets.add(bet);
        }
        return bets;
    }

    public static List<DoubleColorOrderBet> switch7StarBillToDCBet(BillDetail7Star.SevenStarDirect arrange5) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        if (arrange5 == null) {
            return bets;
        }
        List<SevenStarOrder.SevenStarBet> arrange5Bet = arrange5.getDirect();
        bets = switch7Star2DCBet(arrange5Bet);
        return bets;
    }


    public static List<DoubleColorOrderBet> switch7Star2DCBet(List<SevenStarOrder.SevenStarBet> fastThreeBet3) {
        List<DoubleColorOrderBet> bets = new ArrayList<>();
        for (SevenStarOrder.SevenStarBet tg : fastThreeBet3) {
            DoubleColorOrderBet bet = new DoubleColorOrderBet();
            bet.setRed(tg.getOne());
            bet.setBlue(tg.getTwo());
            bet.setDan(tg.getThree());
            bet.setTuo(tg.getFour());
            bet.setFive(tg.getFive());
            bet.setSix(tg.getSix());
            bet.setSeven(tg.getSeven());
            bet.setMode(SEVEN_STAR_ORDER_DIRECT);
            bets.add(bet);
        }
        return bets;
    }

    public static LotteryOrder machine(String mLotteryType, int mPlayMode) {
        Log.e(TAG, "machineAdd: mPlayMode is " + mPlayMode + " and mLotteryType is " + mLotteryType);
        LotteryOrder bean = new LotteryOrder();
        bean.setTotalCount((long) 1);
        bean.setTotalMoney("2");
        bean.setPlayMode(mPlayMode);
        bean.setLotteryType(mLotteryType);
        bean.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        //TODO 新增彩种需要适配的地方 Y
        if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            switch (mPlayMode) {
                case FAST_DEFAULT://默认下注
                case FAST_THREE_SUM://和值
                    bean.setPlayMode(FAST_THREE_SUM);
                    List<String> sumBalls = LotteryUtils.randomSelectFastThreeSumNumber(1);
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(sumBalls));
                    break;
                case FAST_THREE_3_DIFFERENT://三不同号
                    List<String> threeDifferent = LotteryUtils.randomSelectFastThree2SameSingleDifferentNumber(3, null);
                    String threeDifferentString = GsonUtils.getGsonInstance().toJson(threeDifferent);
                    bean.setRedBall(threeDifferentString);
                    break;
                case FAST_THREE_3_SAME_ALL://三同号通选
                    List<String> threeSameAll = new ArrayList<>();
                    threeSameAll.add("111");
                    threeSameAll.add("222");
                    threeSameAll.add("333");
                    threeSameAll.add("444");
                    threeSameAll.add("555");
                    threeSameAll.add("666");
                    String threeSameAllString = GsonUtils.getGsonInstance().toJson(threeSameAll);
                    bean.setRedBall(threeSameAllString);
                    break;
                case FAST_THREE_3_SAME_ONE://三同号单选
                    List<String> threeSameSingle = LotteryUtils.randomSelectFastThree3SameSingleNumber(1);
                    String threeSameSingleString = GsonUtils.getGsonInstance().toJson(threeSameSingle);
                    bean.setRedBall(threeSameSingleString);
                    break;
                case FAST_THREE_2_DIFFERENT://二不同号
                    List<String> towDifferent = LotteryUtils.randomSelectFastThree2SameSingleDifferentNumber(2, null);
                    String towDifferentString = GsonUtils.getGsonInstance().toJson(towDifferent);
                    bean.setRedBall(towDifferentString);
                    break;
                case FAST_THREE_2_SAME_MORE://二同号复选
                    List<String> towSameMore = LotteryUtils.randomSelectFastThree2SameSingleSameNumber(1);
                    String towSameMoreString = GsonUtils.getGsonInstance().toJson(towSameMore);
                    bean.setRedBall(towSameMoreString);
                    break;
                case FAST_THREE_2_SAME_ONE://二同号单选
                    List<String> towSameSingle = LotteryUtils.randomSelectFastThree2SameSingleSameNumber(1);
                    String towSameSingleString = GsonUtils.getGsonInstance().toJson(towSameSingle);
                    bean.setRedBall(towSameSingleString);

                    List<String> towDifferentSingle = LotteryUtils.randomSelectFastThree2SameSingleDifferentNumber(1, towSameSingle.get(0));
                    String towDifferentSingleString = GsonUtils.getGsonInstance().toJson(towDifferentSingle);
                    bean.setBlueBall(towDifferentSingleString);
                    break;
                case FAST_THREE_3_TO_ALL://三连号通选
                    List<String> threeToAll = new ArrayList<>();
                    threeToAll.add("123");
                    threeToAll.add("234");
                    threeToAll.add("345");
                    threeToAll.add("456");
                    String threeToAllString = GsonUtils.getGsonInstance().toJson(threeToAll);
                    bean.setRedBall(threeToAllString);
                    break;
                default://和值
                    List<String> defaultBalls = LotteryUtils.randomSelectFastThreeSumNumber(1);
                    String defaultBallString = GsonUtils.getGsonInstance().toJson(defaultBalls);
                    bean.setRedBall(defaultBallString);
                    break;
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_11_5)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_LUCKY)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_OLD)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_YUE)
                || mLotteryType.equals(LOTTERY_TYPE_11_5_YILE)) {
            switch (mPlayMode) {
                case PLAY_11_5_ANY_2:
                case PLAY_11_5_ANY_2_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(2)));
                    bean.setPlayMode(PLAY_11_5_ANY_2);
                    break;
                case PLAY_11_5_ANY_3:
                case PLAY_11_5_ANY_3_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(3)));
                    bean.setPlayMode(PLAY_11_5_ANY_3);
                    break;
                case PLAY_11_5_ANY_4:
                case PLAY_11_5_ANY_4_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(4)));
                    bean.setPlayMode(PLAY_11_5_ANY_4);
                    break;
                case PLAY_11_5_ANY_5:
                case PLAY_11_5_ANY_5_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(5)));
                    bean.setPlayMode(PLAY_11_5_ANY_5);
                    break;
                case PLAY_11_5_ANY_6:
                case PLAY_11_5_ANY_6_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(6)));
                    bean.setPlayMode(PLAY_11_5_ANY_6);
                    break;
                case PLAY_11_5_ANY_7:
                case PLAY_11_5_ANY_7_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(7)));
                    bean.setPlayMode(PLAY_11_5_ANY_7);
                    break;
                case PLAY_11_5_ANY_8:
                case PLAY_11_5_ANY_8_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(8)));
                    bean.setPlayMode(PLAY_11_5_ANY_8);
                    break;
                case PLAY_11_5_FRONT_2_GROUP:
                case PLAY_11_5_FRONT_2_GROUP_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(2)));
                    bean.setPlayMode(PLAY_11_5_FRONT_2_GROUP);
                    break;
                case PLAY_11_5_FRONT_3_GROUP:
                case PLAY_11_5_FRONT_3_GROUP_DAN:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(3)));
                    bean.setPlayMode(PLAY_11_5_FRONT_3_GROUP);
                    break;
                case PLAY_11_5_FRONT_1_DIRECT:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(1)));
                    bean.setPlayMode(PLAY_11_5_FRONT_1_DIRECT);
                    break;
                case PLAY_11_5_FRONT_2_DIRECT:
                    bean.setRedBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(1)));
                    bean.setBlueBall(GsonUtils.getGsonInstance().toJson(LotteryUtils.random11_5Number(1)));
                    bean.setPlayMode(PLAY_11_5_FRONT_2_DIRECT);
                    break;
                case PLAY_11_5_FRONT_3_DIRECT:
                    Random rand = new Random();
                    List<String> tempNum = StringUtils.transferArrayToList(LotteryUtils.m115Numbers);
                    int wanInt = rand.nextInt(11);
                    List<String> wanList = new ArrayList<>();
                    wanList.add(tempNum.get(wanInt));
                    bean.setRedBall(GsonUtil.getInstance().toJson(wanList));
                    tempNum.remove(wanInt);
                    int qianInt = rand.nextInt(10);
                    List<String> qianList = new ArrayList<>();
                    qianList.add(tempNum.get(qianInt));
                    bean.setBlueBall(GsonUtil.getInstance().toJson(qianList));
                    tempNum.remove(qianInt);
                    int baiInt = rand.nextInt(9);
                    List<String> baiList = new ArrayList<>();
                    baiList.add(tempNum.get(baiInt));
                    bean.setDanBall(GsonUtil.getInstance().toJson(baiList));
                    tempNum.remove(baiInt);
                    bean.setPlayMode(PLAY_11_5_FRONT_3_DIRECT);
                    break;
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                || mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            switch (mPlayMode) {
                case ALWAYS_COLOR_1_DIRECT:
                    List<String> geBalls4 = LotteryUtils.randomSelectNumber(1);
                    String geString4 = GsonUtils.getGsonInstance().toJson(geBalls4);
                    bean.setFiveBall(geString4);
                    break;
                case ALWAYS_COLOR_2_DIRECT:
                    List<String> shiBalls2 = LotteryUtils.randomSelectNumber(1);
                    List<String> geBalls2 = LotteryUtils.randomSelectNumber(1);
                    String shiString2 = GsonUtils.getGsonInstance().toJson(shiBalls2);
                    bean.setTuoBall(shiString2);
                    String geString2 = GsonUtils.getGsonInstance().toJson(geBalls2);
                    bean.setFiveBall(geString2);
                    break;
                case ALWAYS_COLOR_2_GROUP:
                    List<String> geBalls3 = LotteryUtils.randomSelectNumber(2);
                    String geString3 = GsonUtils.getGsonInstance().toJson(geBalls3);
                    bean.setFiveBall(geString3);
                    break;
                case ALWAYS_COLOR_3_DIRECT:
                    List<String> baiBalls1 = LotteryUtils.randomSelectNumber(1);
                    List<String> shiBalls1 = LotteryUtils.randomSelectNumber(1);
                    List<String> geBalls1 = LotteryUtils.randomSelectNumber(1);
                    String baiString1 = GsonUtils.getGsonInstance().toJson(baiBalls1);
                    bean.setDanBall(baiString1);
                    String shiString1 = GsonUtils.getGsonInstance().toJson(shiBalls1);
                    bean.setTuoBall(shiString1);
                    String geString1 = GsonUtils.getGsonInstance().toJson(geBalls1);
                    bean.setFiveBall(geString1);
                    break;
                case ALWAYS_COLOR_3_GROUP_3:
                    List<String> geBallG3 = LotteryUtils.randomSelectNumber(2);
                    String geStringG3 = GsonUtils.getGsonInstance().toJson(geBallG3);
                    bean.setFiveBall(geStringG3);
                    break;
                case ALWAYS_COLOR_3_GROUP_6:
                    List<String> geBallG6 = LotteryUtils.randomSelectNumber(3);
                    String geStringG6 = GsonUtils.getGsonInstance().toJson(geBallG6);
                    bean.setFiveBall(geStringG6);
                    break;
                case ALWAYS_COLOR_5_ALL:
                case ALWAYS_COLOR_5_DIRECT:
                    machineCreateAlwaysColorOrder(bean);
                    break;
                case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                    List<String> shiBalls5 = LotteryUtils.randomBigSmallSingleDouble(1);
                    List<String> geBalls5 = LotteryUtils.randomBigSmallSingleDouble(1);
                    String shiString5 = GsonUtils.getGsonInstance().toJson(shiBalls5);
                    bean.setTuoBall(shiString5);
                    String geString5 = GsonUtils.getGsonInstance().toJson(geBalls5);
                    bean.setFiveBall(geString5);
                    break;
                default:
                    mPlayMode = ALWAYS_COLOR_5_ALL;
                    machineCreateAlwaysColorOrder(bean);
                    break;
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
            List<String> redBalls = LotteryUtils.randomSelectNumber(BALL_MIN_SELECTED_NUMBER_RED, BALL_TYPE_RED);
            List<String> blueBalls = LotteryUtils.randomSelectNumber(BALL_MIN_SELECTED_NUMBER_BLUE, BALL_TYPE_BLUE);
            String redballString = GsonUtils.getGsonInstance().toJson(redBalls);
            bean.setRedBall(redballString);
            String blueballString = GsonUtils.getGsonInstance().toJson(blueBalls);
            bean.setBlueBall(blueballString);
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
            List<String> redBalls = LotteryUtils.randomSelectSevenNumber(7);
            String redballString = GsonUtils.getGsonInstance().toJson(redBalls);
            bean.setRedBall(redballString);
        } else if (mLotteryType.equals(LOTTERY_TYPE_3_D)) {
            if (mPlayMode == THREE_D_PLAY_DIRECT) {//直选
                List<String> baiBalls = LotteryUtils.randomSelectNumber(1);
                List<String> shiBalls = LotteryUtils.randomSelectNumber(1);
                List<String> geBalls = LotteryUtils.randomSelectNumber(1);
                String baiString = GsonUtils.getGsonInstance().toJson(baiBalls);
                bean.setRedBall(baiString);
                String shiString = GsonUtils.getGsonInstance().toJson(shiBalls);
                bean.setDanBall(shiString);
                String geString = GsonUtils.getGsonInstance().toJson(geBalls);
                bean.setTuoBall(geString);

            } else if (mPlayMode == THREE_D_PLAY_GROUP_SIX) {//组选6
                List<String> baiBalls = LotteryUtils.randomSelectNumber(3);
                String baiString = GsonUtils.getGsonInstance().toJson(baiBalls);
                bean.setRedBall(baiString);
            } else {//组选3
                List<String> baiBalls = LotteryUtils.randomSelectNumber(2);
                String baiString = GsonUtils.getGsonInstance().toJson(baiBalls);
                bean.setRedBall(baiString);
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            if (mPlayMode == ARRANGE_3_PLAY_DIRECT) {//直选
                List<String> baiBalls = LotteryUtils.randomSelectNumber(1);
                List<String> shiBalls = LotteryUtils.randomSelectNumber(1);
                List<String> geBalls = LotteryUtils.randomSelectNumber(1);
                String baiString = GsonUtils.getGsonInstance().toJson(baiBalls);
                bean.setRedBall(baiString);
                String shiString = GsonUtils.getGsonInstance().toJson(shiBalls);
                bean.setDanBall(shiString);
                String geString = GsonUtils.getGsonInstance().toJson(geBalls);
                bean.setTuoBall(geString);

            } else if (mPlayMode == ARRANGE_3_PLAY_GROUP_SIX) {//组选6
                List<String> baiBalls = LotteryUtils.randomSelectNumber(3);
                String baiString = GsonUtils.getGsonInstance().toJson(baiBalls);
                bean.setRedBall(baiString);
            } else {//组选3
                List<String> baiBalls = LotteryUtils.randomSelectNumber(2);
                String baiString = GsonUtils.getGsonInstance().toJson(baiBalls);
                bean.setRedBall(baiString);
            }
        } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
            machineCreateAlwaysColorOrder(bean);
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {
            machineCreate7StarOrder(bean);
        }
        Log.e(TAG, "machineAdd: 结果=" + GsonUtil.getInstance().toJson(bean));
        return bean;
    }

    public static void machineCreateAlwaysColorOrder(LotteryOrder bean) {
        List<String> wanBalls = LotteryUtils.randomSelectNumber(1);
        List<String> qianBalls = LotteryUtils.randomSelectNumber(1);
        List<String> baiBalls = LotteryUtils.randomSelectNumber(1);
        List<String> shiBalls = LotteryUtils.randomSelectNumber(1);
        List<String> geBalls = LotteryUtils.randomSelectNumber(1);
        String wanString = GsonUtils.getGsonInstance().toJson(wanBalls);
        bean.setRedBall(wanString);
        String qianString = GsonUtils.getGsonInstance().toJson(qianBalls);
        bean.setBlueBall(qianString);
        String baiString = GsonUtils.getGsonInstance().toJson(baiBalls);
        bean.setDanBall(baiString);
        String shiString = GsonUtils.getGsonInstance().toJson(shiBalls);
        bean.setTuoBall(shiString);
        String geString = GsonUtils.getGsonInstance().toJson(geBalls);
        bean.setFiveBall(geString);
    }

    public static void machineCreate7StarOrder(LotteryOrder bean) {
        List<String> sixBalls = LotteryUtils.randomSelectNumber(1);
        String sixString = GsonUtils.getGsonInstance().toJson(sixBalls);
        bean.setSixBall(sixString);
        List<String> sevenBalls = LotteryUtils.randomSelectNumber(1);
        String sevenString = GsonUtils.getGsonInstance().toJson(sevenBalls);
        bean.setSevenBall(sevenString);
        machineCreateAlwaysColorOrder(bean);
    }

    public static String formatBigSmallSingleDouble(int num) {
        StringBuilder sb = new StringBuilder();
        if (num > 4) {
            sb.append("大");
        } else {
            sb.append("小");
        }
        if (num % 2 == 0) {
            sb.append("双");
        } else {
            sb.append("单");
        }
        return sb.toString();
    }

    /**
     * //缓存选号
     *
     * @param lotteryType 彩种
     * @param playType    玩法
     * @param numPos      个位-万位1-5(普通投注走个位，其他投注方式依次个位到万位（五星直选）  比如胆拖就是胆码(同号)个位 拖码（不同号）十位)     *
     */
    public static void saveChooseNumberCache(String lotteryType, int playType, int numPos, List<LotteryBall> lotteryBalls) {
        LogF.d("选号缓存", "存储 lotteryType=" + lotteryType + " playType="
                + playType + " numPos=" + numPos + " ball=" + GsonUtil.getInstance().toJson(lotteryBalls));
        if (numPos <= 0) {
            numPos = 1;//默认是个位
        }
        if (null == lotteryBalls || lotteryBalls.size() == 0) {
            clearChooseNumberCache(lotteryType, playType, numPos);
            LogF.d("选号缓存", "ball==null");
            return;
        }
        ChooseNumberBean chooseNumberBean = new ChooseNumberBean();
        chooseNumberBean.setLotteryType(lotteryType);
        chooseNumberBean.setPlayType(playType);
        chooseNumberBean.setNumPos(numPos);
        chooseNumberBean.setLotteryBalls(lotteryBalls);
        SharedPreferenceUtil.put(MyApplication.getAppContext(),
                "chooseNum_" + lotteryType + "_" + playType + "_" + numPos, GsonUtil.getInstance().toJson(chooseNumberBean));
        LogF.d("选号缓存", "存储成功");
    }

    /**
     * 获取选号
     *
     * @param lotteryType 彩种
     * @param playType    玩法
     * @param numPos      个位-万位1-5(普通投注走个位，其他投注方式依次个位到万位（五星直选）  比如胆拖就是胆码(同号)个位 拖码（不同号）十位)     *
     */
    public static ChooseNumberBean getChooseNumberCache(String lotteryType, int playType, int numPos) {
        LogF.d("选号缓存", "获取 lotteryType=" + lotteryType + " playType="
                + playType + " numPos=" + numPos);
        if (numPos <= 0) {
            numPos = 1;//默认是个位
        }
        String json = (String) SharedPreferenceUtil.get(MyApplication.getAppContext(),
                "chooseNum_" + lotteryType + "_" + playType + "_" + numPos, "");
        ChooseNumberBean chooseNumberBean = GsonUtil.getInstance().toClass(json, ChooseNumberBean.class);
        LogF.d("选号缓存", "获取成功 " + json);
        if (null == chooseNumberBean) {
            chooseNumberBean = new ChooseNumberBean();
        }
        return chooseNumberBean;

    }

    /**
     * 清楚当前选号
     *
     * @param lotteryType 彩种
     * @param playType    玩法
     * @param numPos      个位-万位1-5(普通投注走个位，其他投注方式依次个位到万位（五星直选）  比如胆拖就是胆码(同号)个位 拖码（不同号）十位)     *
     */
    public static void clearChooseNumberCache(String lotteryType, int playType, int numPos) {
        LogF.d("选号缓存", "清空 lotteryType=" + lotteryType + " playType="
                + playType + " numPos=" + numPos);
        SharedPreferenceUtil.put(MyApplication.getAppContext(),
                "chooseNum_" + lotteryType + "_" + playType + "_" + numPos, "");
    }

    /**
     * 缓存走势图设置
     *
     * @param lotteryType      彩种
     * @param trendSettingBean 设置
     */
    public static void saveTrendSettingCache(String lotteryType, TrendSettingBean trendSettingBean) {
        LogF.d("缓存走势图设置", "存储 lotteryType=" + lotteryType + " 设置=" + GsonUtil.getInstance().toJson(trendSettingBean));
        if (null == trendSettingBean) {
            clearTrendSettingCache(lotteryType);
            return;
        }
        SharedPreferenceUtil.put(MyApplication.getAppContext(),
                "trend_setting_" + lotteryType, GsonUtil.getInstance().toJson(trendSettingBean));
        LogF.d("缓存走势图设置", "存储成功");
    }

    /**
     * 走势图设置
     *
     * @param lotteryType 彩种
     */
    public static TrendSettingBean getTrendSettingCache(String lotteryType) {
        String json = (String) SharedPreferenceUtil.get(MyApplication.getAppContext(),
                "trend_setting_" + lotteryType, "");
        LogF.d("获取走势图设置  ", lotteryType + "  " + json);
        TrendSettingBean trendSettingBean = GsonUtil.getInstance().toClass(json, TrendSettingBean.class);
        if (null == trendSettingBean) {
            trendSettingBean = new TrendSettingBean();
        }
        return trendSettingBean;
    }

    /**
     * 清楚当前走势图设置
     *
     * @param lotteryType 彩种
     */
    public static void clearTrendSettingCache(String lotteryType) {
        LogF.d("清空当前走势图设置", "清空 lotteryType=" + lotteryType);
        SharedPreferenceUtil.put(MyApplication.getAppContext(),
                "trend_setting_" + lotteryType, "");
    }

    /**
     * 走势图比对号码
     *
     * @param number 要判断的号码
     */
    public static boolean isTrendWinNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        try {
            return Integer.valueOf(number) <= 0;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * 走势图比对号码
     *
     * @param number 要判断的号码
     */
    public static boolean isTrendSameWinNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        try {
            return Integer.valueOf(number) < 0;
        } catch (NumberFormatException e) {
        }
        return false;
    }

}
