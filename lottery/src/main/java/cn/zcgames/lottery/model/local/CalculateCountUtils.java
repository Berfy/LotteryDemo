package cn.zcgames.lottery.model.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.home.bean.ChooseNumberBean;
import cn.zcgames.lottery.home.bean.ChooseNumberInfoBean;

import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_GROUP;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_3;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_6;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_ALL;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.BALL_MIN_SELECTED_NUMBER_RED;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_2_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_3;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_3_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_4;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_4_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_5;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_5_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_6;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_6_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_7;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_7_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_8;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_ANY_8_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_GROUP;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_GROUP;
import static cn.zcgames.lottery.app.AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN;

/**
 * Created by admin on 2017/8/30.
 */

public class CalculateCountUtils {

    /**
     * 计算11选5的投注数量
     *
     * @param oneList
     * @param towList
     * @param threeList
     * @param playType
     * @return
     */
    public static long calculateEleven5Count(List<LotteryBall> oneList,
                                             List<LotteryBall> towList,
                                             List<LotteryBall> threeList,
                                             int playType) {
        int oneSize = oneList == null ? 0 : oneList.size();
        int twoSize = towList == null ? 0 : towList.size();
        int threeSize = threeList == null ? 0 : threeList.size();

        LogF.d("calculateEleven5Count", "11选5 计算count" + oneSize + "  TWO=" + twoSize + "  three" + threeSize);
        long count = 0;
        switch (playType) {
            case PLAY_11_5_ANY_2://任选二
            case PLAY_11_5_FRONT_2_GROUP://前二组选
                if (oneSize >= 2) {
                    count = combination(oneSize, 2);
                }
                break;
            case PLAY_11_5_ANY_3://任选三
            case PLAY_11_5_FRONT_3_GROUP://前三组选
                if (oneSize >= 3) {
                    count = combination(oneSize, 3);
                }
                break;
            case PLAY_11_5_ANY_4://任选四
                if (oneSize >= 4) {
                    count = combination(oneSize, 4);
                }
                break;
            case PLAY_11_5_ANY_5:
                if (oneSize >= 5) {
                    count = combination(oneSize, 5);
                }
                break;
            case PLAY_11_5_ANY_6:
                if (oneSize >= 6) {
                    count = combination(oneSize, 6);
                }
                break;
            case PLAY_11_5_ANY_7:
                if (oneSize >= 7) {
                    count = combination(oneSize, 7);
                }
                break;
            case PLAY_11_5_ANY_8:
                if (oneSize >= 8) {
                    count = combination(oneSize, 8);
                }
                break;
            case PLAY_11_5_FRONT_1_DIRECT://前一
                if (oneSize >= 1) {
                    count = oneSize;
                }
                break;
            case PLAY_11_5_FRONT_2_DIRECT://前二
                if (oneSize >= 1) {
                    count = get11_5DirectCount(oneList, towList, null, PLAY_11_5_FRONT_2_DIRECT);
                }
                break;
            case PLAY_11_5_FRONT_3_DIRECT://前三
                if (oneSize >= 1) {
                    count = get11_5DirectCount(oneList, towList, threeList, PLAY_11_5_FRONT_3_DIRECT);
                }
                break;
            //以下是胆拖
            case PLAY_11_5_FRONT_2_GROUP_DAN:
            case PLAY_11_5_ANY_2_DAN:
                if (oneSize > 0 && twoSize > 0 && oneSize + twoSize >= 2) {
                    count = combination(twoSize, 1);
                }
                break;
            case PLAY_11_5_FRONT_3_GROUP_DAN:
            case PLAY_11_5_ANY_3_DAN:
                if (oneSize > 0 && twoSize > 0 && oneSize + twoSize >= 3) {
                    count = combination(twoSize, 3 - oneSize);
                }
                break;
            case PLAY_11_5_ANY_4_DAN:
                if (oneSize > 0 && twoSize > 0 && oneSize + twoSize >= 4) {
                    count = combination(twoSize, 4 - oneSize);
                }
                break;
            case PLAY_11_5_ANY_5_DAN:
                if (oneSize > 0 && twoSize > 0 && oneSize + twoSize >= 5) {
                    count = combination(twoSize, 5 - oneSize);
                }
                break;
            case PLAY_11_5_ANY_6_DAN:
                if (oneSize > 0 && twoSize > 0 && oneSize + twoSize >= 6) {
                    count = combination(twoSize, 6 - oneSize);
                }
                break;
            case PLAY_11_5_ANY_7_DAN:
                if (oneSize > 0 && twoSize > 0 && oneSize + twoSize >= 7) {
                    count = combination(twoSize, 7 - oneSize);
                }
                break;
            case PLAY_11_5_ANY_8_DAN:
                if (oneSize > 0 && twoSize > 0 && oneSize + twoSize >= 8) {
                    count = combination(twoSize, 8 - oneSize);
                }
                break;
        }
        LogF.d("calculateEleven5Count", "11选5 计算count=" + count);
        return count;
    }

    /**
     * 双色球普通投注方式，总共所投注数计算公式
     *
     * @param redBallNum  红球的个数
     * @param blueBallNum 蓝色球的个数
     * @return
     */
    public static long doubleColorOrdinary(int redBallNum, int blueBallNum) {
        long c = jieCheng(redBallNum);
        long d = jieCheng(redBallNum - BALL_MIN_SELECTED_NUMBER_RED);
        long e = jieCheng(BALL_MIN_SELECTED_NUMBER_RED);
        return (c / (d * e)) * blueBallNum;
    }

    /**
     * 双色球胆拖投注方式，注数计算公式
     *
     * @param redDanBall 胆码区红球的个数
     * @param redTuoBall 拖码区红球的个数
     * @param blueBall   蓝色球的个数
     * @return
     */
    public static long doubleColorDantuo(int redDanBall, int redTuoBall, int blueBall) {
        long a = combination(redTuoBall, BALL_MIN_SELECTED_NUMBER_RED - redDanBall);
        long b = combination(blueBall, 1);
        return a * b;
    }

    /**
     * 快三：三不同
     *
     * @param size 选中号码的个数
     * @return
     */
    public static int threeDifferentCount(int size) {
        int a = size * (size - 1) * (size - 2);
        return a / 6;
    }

    /**
     * 快三：二不同
     *
     * @param size 选中号码的个数
     * @return
     */
    public static int towDifferentCount(int size) {
        int a = size * (size - 1);
        return a / 2;
    }

    /**
     * 求一个整数的阶乘：N！ = N*(N-1)*(N-2)...1
     *
     * @param a
     * @return
     */
    public static long jieCheng(int a) {
        long dividend = 1;
        for (int i = 1; i <= a; i++) {
            dividend = dividend * i;
        }
        return dividend;
    }

    /**
     * 组合的计算公式
     *
     * @param n
     * @param m
     * @return
     */
    public static long combination(int n, int m) {
        int minNum = n - m + 1;
        long divisor = 1;
        long dividend = 1;
        for (int i = minNum; i <= n; i++) {
            divisor = divisor * i;
        }
        for (int i = m; i > 0; i--) {
            dividend = dividend * i;
        }
        return divisor / dividend;
    }

    /**
     * 计算七乐彩注数
     *
     * @param size
     * @return
     */
    public static int calculateSevenCount(int size) {
        long a = CalculateCountUtils.jieCheng(size);
        long b = CalculateCountUtils.jieCheng(7) * CalculateCountUtils.jieCheng(size - 7);
        return (int) (a / b);
    }

    /**
     * 计算福彩3D直选的注数
     *
     * @param size  百位
     * @param size1 十位
     * @param size2 个位
     */
    public static int getThreeDDirectCount(int size, int size1, int size2) {
        return size * size1 * size2;
    }

    /**
     * 计算组选3的注数
     *
     * @param size 号码的个数
     * @return
     */
    public static int getGroup3Count(int size) {
        return (size - 1) * size;
    }

    /**
     * 计算组选6的注数
     *
     * @param size 号码的个数
     * @return
     */
    public static int getGroup6Count(int size) {
        return (size - 1) * (size - 2) * size / 6;
    }

    /**
     * 计算排列五，时时彩五星通选、五星直选
     *
     * @param red
     * @param blue
     * @param dan
     * @param tuo
     * @param other
     * @return
     */
    public static int getArrange5Count(List<String> red, List<String> blue,
                                       List<String> dan, List<String> tuo, List<String> other) {
        return red.size() * blue.size() * dan.size() * tuo.size() * other.size();
    }

    /**
     * 计算七星彩
     *
     * @param red
     * @param blue
     * @param dan
     * @param tuo
     * @param other
     * @param six
     * @param seven
     * @return
     */
    public static int get7StarCount(List<String> red, List<String> blue,
                                    List<String> dan, List<String> tuo,
                                    List<String> other, List<String> six,
                                    List<String> seven) {
        return red.size() * blue.size() * dan.size() * tuo.size() * other.size() * six.size() * seven.size();
    }

    //11选5前X直选 算法
    public static long get11_5DirectCount(List<LotteryBall> oneList, List<LotteryBall> twoList, List<LotteryBall> threeList, int playType) {
        int oneSize = null == oneList ? 0 : oneList.size();
        int twoSize = null == twoList ? 0 : twoList.size();
        int threeSize = null == threeList ? 0 : threeList.size();
        long count;
        switch (playType) {
            case PLAY_11_5_FRONT_2_DIRECT:
                count = oneSize * twoSize;
                if (count > 0) {
                    for (int i = 0; i < oneSize; i++) {
                        String iNum = oneList.get(i).getNumber();
                        for (int j = 0; j < twoSize; j++) {
                            String jNum = twoList.get(j).getNumber();
                            if (iNum.equals(jNum)) {
                                count--;
                            }
                        }
                    }
                }
                return count;
            case PLAY_11_5_FRONT_3_DIRECT:
                count = oneSize * twoSize * threeSize;
                if (count > 0) {
                    for (int i = 0; i < oneSize; i++) {
                        String iNum = oneList.get(i).getNumber();
                        for (int j = 0; j < twoSize; j++) {
                            String jNum = twoList.get(j).getNumber();
                            for (int k = 0; k < threeSize; k++) {
                                String kNum = threeList.get(k).getNumber();
                                if (iNum.equals(kNum) || jNum.equals(iNum) || kNum.equals(jNum)) {
                                    count--;
                                } else {
                                    LogF.d("11选5组合注数 ", "11选5组合 " + iNum + "," + jNum + "," + kNum);
                                }
                            }
                        }
                    }
                }
                return count;
        }
        return 0;
    }

    public static long getACCount(int playType, List<LotteryBall> mMillionBalls, List<LotteryBall> mThousandBall,
                                  List<LotteryBall> mHundredBall, List<LotteryBall> mTenBall, List<LotteryBall> mOneBall) {
        return getACCount(playType, null == mMillionBalls ? 0 : mMillionBalls.size()
                , null == mThousandBall ? 0 : mThousandBall.size()
                , null == mHundredBall ? 0 : mHundredBall.size()
                , null == mTenBall ? 0 : mTenBall.size()
                , null == mOneBall ? 0 : mOneBall.size());
    }

    public static long getACCount(int mPlayType, int millionSize, int thousandSize, int hundredSize, int tenSize, int oneSize) {
        int count = 0;
        if (mPlayType == ALWAYS_COLOR_5_DIRECT) {
            count = millionSize * thousandSize * tenSize * hundredSize * oneSize;
        } else if (mPlayType == ALWAYS_COLOR_5_ALL) {
            count = millionSize * thousandSize * tenSize * hundredSize * oneSize;
        } else if (mPlayType == ALWAYS_COLOR_3_DIRECT) {
            count = tenSize * hundredSize * oneSize;
        } else if (mPlayType == ALWAYS_COLOR_2_DIRECT) {
            count = tenSize * oneSize;
        } else if (mPlayType == ALWAYS_COLOR_2_GROUP) {
            count = oneSize * (oneSize - 1) / 2;
        } else if (mPlayType == ALWAYS_COLOR_1_DIRECT) {
            count = oneSize;
        } else if (mPlayType == ALWAYS_COLOR_3_GROUP_3) {
            count = oneSize * (oneSize - 1);
        } else if (mPlayType == ALWAYS_COLOR_3_GROUP_6) {
            count = (int) CalculateCountUtils.combination(oneSize, 3);
        }
        return count;
    }

    /**
     * 获取当前选号的奖金 盈利
     * 数组大小3   位置0：奖金 位置1：盈利 2注数
     *
     * @param lotteryType 彩种
     * @param playType    玩法
     */
    public static ChooseNumberInfoBean getLotteryChoosePriceInfo(String lotteryType, int playType) {
        ChooseNumberInfoBean chooseNumberInfoBean = new ChooseNumberInfoBean();
        switch (lotteryType) {
            case AppConstants.LOTTERY_TYPE_FAST_3:
            case AppConstants.LOTTERY_TYPE_FAST_3_JS:
            case AppConstants.LOTTERY_TYPE_FAST_3_HB:
            case AppConstants.LOTTERY_TYPE_FAST_3_NEW:
            case AppConstants.LOTTERY_TYPE_FAST_3_EASY:
                float onePrice = (float) SharedPreferenceUtil.get(MyApplication.getAppContext(),
                        lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE);
                ChooseNumberBean geNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 1);
                List<String> prices = new ArrayList<>();
                List<String> winPrices = new ArrayList<>();
                if (null != geNum) {
                    switch (playType) {
                        case AppConstants.FAST_THREE_SUM:
                            int count = geNum.getLotteryBalls().size();//注数
                            Collections.sort(geNum.getLotteryBalls());
                            chooseNumberInfoBean.setCount(count);
                            chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                            if (count > 0) {
                                int small = 0;
                                int big = 0;
                                for (LotteryBall lotteryBall : geNum.getLotteryBalls()) {
                                    int price = LotteryUtils.mFastThreeSumPrices[Integer.valueOf(lotteryBall.getNumber()) - 3];
                                    if (small == 0 || price < small) {
                                        small = price;
                                    }
                                    if (price > big) {
                                        big = price;
                                    }
                                }
                                if (small != 0) {
                                    prices.add(small + "");
                                    winPrices.add(StringUtils.getNumberNoZero(small - count * onePrice));
                                }
                                if (big != 0 && small != big) {
                                    prices.add(big + "");
                                    winPrices.add(StringUtils.getNumberNoZero(big - count * onePrice));
                                }
                                chooseNumberInfoBean.setPrice(prices);
                                chooseNumberInfoBean.setWinPrice(winPrices);
                            }
                            break;
                        case AppConstants.FAST_THREE_3_SAME_ONE://三同号单选
                            count = geNum.getLotteryBalls().size();//注数
                            chooseNumberInfoBean.setCount(count);
                            chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                            if (count > 0) {
                                prices = new ArrayList<>();
                                winPrices = new ArrayList<>();
                                prices.add("240");
                                winPrices.add(StringUtils.getNumberNoZero(240 - count * onePrice));
                                chooseNumberInfoBean.setPrice(prices);
                                chooseNumberInfoBean.setWinPrice(winPrices);
                            }
                            break;
                        case AppConstants.FAST_THREE_3_SAME_ALL:
                            count = 1;
                            chooseNumberInfoBean.setCount(count);
                            prices = new ArrayList<>();
                            winPrices = new ArrayList<>();
                            prices.add("40");
                            winPrices.add(StringUtils.getNumberNoZero(40 - count * onePrice));
                            chooseNumberInfoBean.setPrice(prices);
                            chooseNumberInfoBean.setWinPrice(winPrices);
                            break;
                        case AppConstants.FAST_THREE_3_DIFFERENT://三不同
                            count = threeDifferentCount(geNum.getLotteryBalls().size());//注数
                            chooseNumberInfoBean.setCount(count);
                            chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                            if (count > 0) {
                                prices = new ArrayList<>();
                                winPrices = new ArrayList<>();
                                prices.add("40");
                                winPrices.add(StringUtils.getNumberNoZero(40 - count * onePrice));
                                chooseNumberInfoBean.setPrice(prices);
                                chooseNumberInfoBean.setWinPrice(winPrices);
                            }
                            break;
                        case AppConstants.FAST_THREE_3_TO_ALL:
                            count = 1;
                            chooseNumberInfoBean.setCount(count);
                            prices = new ArrayList<>();
                            winPrices = new ArrayList<>();
                            prices.add("10");
                            winPrices.add(StringUtils.getNumberNoZero(10 - count * onePrice));
                            chooseNumberInfoBean.setPrice(prices);
                            chooseNumberInfoBean.setWinPrice(winPrices);
                            break;
                        case AppConstants.FAST_THREE_2_SAME_ONE://二同号单选
                            ChooseNumberBean diffNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 2);
                            count = geNum.getLotteryBalls().size() * diffNum.getLotteryBalls().size();
                            chooseNumberInfoBean.setCount(count);
                            chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                            chooseNumberInfoBean.setShi(diffNum.getLotteryBalls());
                            if (count > 0) {
                                prices = new ArrayList<>();
                                winPrices = new ArrayList<>();
                                prices.add("15");
                                prices.add("95");
                                winPrices.add(StringUtils.getNumberNoZero(15 - count * onePrice));
                                winPrices.add(StringUtils.getNumberNoZero(95 - count * onePrice));
                                chooseNumberInfoBean.setPrice(prices);
                                chooseNumberInfoBean.setWinPrice(winPrices);
                            }
                            break;
                        case AppConstants.FAST_THREE_2_SAME_MORE://二同号复选
                            count = geNum.getLotteryBalls().size();//注数
                            chooseNumberInfoBean.setCount(count);
                            chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                            if (count > 0) {
                                prices = new ArrayList<>();
                                winPrices = new ArrayList<>();
                                prices.add("15");
                                winPrices.add(StringUtils.getNumberNoZero(15 - count * onePrice));
                                chooseNumberInfoBean.setPrice(prices);
                                chooseNumberInfoBean.setWinPrice(winPrices);
                            }
                            break;
                        case AppConstants.FAST_THREE_2_DIFFERENT://二不同号
                            count = towDifferentCount(geNum.getLotteryBalls().size());//注数
                            int trueCount = count < 3 ? count : 3;
                            chooseNumberInfoBean.setCount(count);
                            chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                            if (count > 0) {
                                prices = new ArrayList<>();
                                winPrices = new ArrayList<>();
                                if (count > 1) {
                                    prices.add("8");
                                    prices.add(String.valueOf(8 * trueCount));
                                    winPrices.add(StringUtils.getNumberNoZero(8 - count * onePrice));
                                    winPrices.add(StringUtils.getNumberNoZero(8 * trueCount - count * onePrice));
                                } else {
                                    prices.add("8");
                                    winPrices.add(StringUtils.getNumberNoZero(8 - count * onePrice));
                                }
                                chooseNumberInfoBean.setPrice(prices);
                                chooseNumberInfoBean.setWinPrice(winPrices);
                            }
                            break;
                    }
                }
                break;
            case AppConstants.LOTTERY_TYPE_11_5:
            case AppConstants.LOTTERY_TYPE_11_5_LUCKY:
            case AppConstants.LOTTERY_TYPE_11_5_OLD:
            case AppConstants.LOTTERY_TYPE_11_5_YILE:
            case AppConstants.LOTTERY_TYPE_11_5_YUE:
                onePrice = (Float) SharedPreferenceUtil.get(MyApplication.getAppContext(),
                        lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE);
                ChooseNumberBean wanNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 5);
                ChooseNumberBean qianNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 4);
                ChooseNumberBean baiNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 3);
                ChooseNumberBean tuoNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 2);
                ChooseNumberBean danNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 1);
                long count = calculateEleven5Count(wanNum.getLotteryBalls(), qianNum.getLotteryBalls(), baiNum.getLotteryBalls(), playType);
                chooseNumberInfoBean.setCount(count);
                switch (playType) {
                    case AppConstants.PLAY_11_5_ANY_2:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        if (count > 0) {
                            prices = new ArrayList<>();
                            winPrices = new ArrayList<>();
                            prices.add("6");
                            winPrices.add("4");
                            if (count > 1) {
                                prices.add(String.valueOf(6 * count));
                                winPrices.add(StringUtils.getNumberNoZero(6 * count - onePrice * count));
                            }
                            chooseNumberInfoBean.setPrice(prices);
                            chooseNumberInfoBean.setWinPrice(winPrices);
                        }
                        break;
                    case AppConstants.PLAY_11_5_ANY_3:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_4:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_5:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_6:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_7:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_8:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_FRONT_1_DIRECT:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_FRONT_2_DIRECT:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        chooseNumberInfoBean.setQian(qianNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_FRONT_3_DIRECT:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        chooseNumberInfoBean.setQian(qianNum.getLotteryBalls());
                        chooseNumberInfoBean.setBai(baiNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_FRONT_2_GROUP:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_FRONT_3_GROUP:
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_2_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_3_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_4_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_5_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_6_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_7_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_ANY_8_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_FRONT_2_GROUP_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                    case AppConstants.PLAY_11_5_FRONT_3_GROUP_DAN:
                        chooseNumberInfoBean.setGe(danNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(tuoNum.getLotteryBalls());
                        break;
                }
                break;
            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR:
            case AppConstants.LOTTERY_TYPE_ALWAYS_COLOR_NEW:
                onePrice = (Float) SharedPreferenceUtil.get(MyApplication.getAppContext(), lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE);
                wanNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 5);
                qianNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 4);
                baiNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 3);
                ChooseNumberBean shiNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 2);
                geNum = LotteryUtils.getChooseNumberCache(lotteryType, playType, 1);
                count = CalculateCountUtils.getACCount(playType, wanNum.getLotteryBalls(), qianNum.getLotteryBalls(),
                        baiNum.getLotteryBalls(), shiNum.getLotteryBalls(), geNum.getLotteryBalls());
                chooseNumberInfoBean.setCount(count);
                switch (playType) {
                    case AppConstants.ALWAYS_COLOR_1_DIRECT://一星直选
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_2_DIRECT://二星直选
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(shiNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_2_GROUP://二星组选
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_3_DIRECT://三星直选
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(shiNum.getLotteryBalls());
                        chooseNumberInfoBean.setBai(baiNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_3_GROUP_3://三星组三
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_3_GROUP_6://三星组六
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_5_DIRECT://五星直选
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(shiNum.getLotteryBalls());
                        chooseNumberInfoBean.setBai(baiNum.getLotteryBalls());
                        chooseNumberInfoBean.setQian(qianNum.getLotteryBalls());
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_5_ALL://五星通选
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(shiNum.getLotteryBalls());
                        chooseNumberInfoBean.setBai(baiNum.getLotteryBalls());
                        chooseNumberInfoBean.setQian(qianNum.getLotteryBalls());
                        chooseNumberInfoBean.setWan(wanNum.getLotteryBalls());
                        break;
                    case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE://大小单双
                        count = 1;
                        chooseNumberInfoBean.setCount(count);
                        chooseNumberInfoBean.setGe(geNum.getLotteryBalls());
                        chooseNumberInfoBean.setShi(shiNum.getLotteryBalls());
                        break;
                }
                break;
        }
        LogF.d("下注计算结果", GsonUtil.getInstance().toJson(chooseNumberInfoBean));
        return chooseNumberInfoBean;
    }
}
