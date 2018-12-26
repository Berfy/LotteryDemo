package cn.zcgames.lottery.home.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BasePresenter;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.AlwaysColorOrder;
import cn.zcgames.lottery.home.bean.Arrange5Order;
import cn.zcgames.lottery.home.bean.DoubleColorOrder;
import cn.zcgames.lottery.home.bean.ElevenFiveOrder;
import cn.zcgames.lottery.home.bean.FastThreeOrder;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.home.bean.SevenHappyOrder;
import cn.zcgames.lottery.home.bean.SevenStarOrder;
import cn.zcgames.lottery.home.bean.ThreeDOrder;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.ILotteryOrderActivity;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.model.local.LotteryUtils;
import cn.zcgames.lottery.utils.StaticResourceUtils;

import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_1_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_2_GROUP;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_3;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_3_GROUP_6;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_ALL;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_5_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.ARRANGE_3_PLAY_GROUP_SIX;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_DANSHI;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_FUSHI;
import static cn.zcgames.lottery.app.AppConstants.FAST_DEFAULT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_MORE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_2_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_DIFFERENT;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ALL;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_SAME_ONE;
import static cn.zcgames.lottery.app.AppConstants.FAST_THREE_3_TO_ALL;
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
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_DIRECT;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_GROUP_SIX;

/**
 * Created by admin on 2017/4/28.
 * 投注单presenter
 */
public class LotteryOrderPresenter implements BasePresenter {

    private static final String TAG = "LotteryOrderPresenter";

    private Activity mContext;
    private ILotteryOrderActivity iActivity;
    private ILotteryModel iModel;

    public LotteryOrderPresenter(Activity activity, ILotteryOrderActivity iActivity) {
        mContext = activity;
        this.iActivity = iActivity;
        iModel = new LotteryModel();
    }

    /**
     * 创建本地订单
     *
     * @param playType
     * @param mSelectButtons
     * @param mCount
     */
    public void createFast3SingleLocalOrder(String lotteryType, int playType, List<LotteryBall> mSelectButtons, long mCount) {
        boolean isOk = false;
        String msgStr = "";
        LogF.d(TAG, "快三下注生成投注单 lotteryType=" + lotteryType + " playType=" + playType
                + "  " + GsonUtil.getInstance().toJson(mSelectButtons));
        if (mCount <= 0) {
            msgStr = "请选择投注号码";
            iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
            return;
        }

        LotteryOrder order = new LotteryOrder();
        order.setPlayMode(playType);
        if (null == mSelectButtons) {
            mSelectButtons = new ArrayList<>();
        }
        if (playType == AppConstants.FAST_THREE_3_SAME_ALL) {
            mSelectButtons.clear();
            LotteryBall bean1 = new LotteryBall();
            bean1.setNumber(111);
            mSelectButtons.add(bean1);

            LotteryBall bean2 = new LotteryBall();
            bean2.setNumber(222);
            mSelectButtons.add(bean2);

            LotteryBall bean3 = new LotteryBall();
            bean3.setNumber(333);
            mSelectButtons.add(bean3);

            LotteryBall bean4 = new LotteryBall();
            bean4.setNumber(444);
            mSelectButtons.add(bean4);

            LotteryBall bean5 = new LotteryBall();
            bean5.setNumber(555);
            mSelectButtons.add(bean5);

            LotteryBall bean6 = new LotteryBall();
            bean6.setNumber(666);
            mSelectButtons.add(bean6);
        } else if (playType == AppConstants.FAST_THREE_3_TO_ALL) {
            mSelectButtons.clear();
            LotteryBall bean1 = new LotteryBall();
            bean1.setNumber(123);
            mSelectButtons.add(bean1);

            LotteryBall bean2 = new LotteryBall();
            bean2.setNumber(234);
            mSelectButtons.add(bean2);

            LotteryBall bean3 = new LotteryBall();
            bean3.setNumber(345);
            mSelectButtons.add(bean3);

            LotteryBall bean4 = new LotteryBall();
            bean4.setNumber(456);
            mSelectButtons.add(bean4);
        }
        String numberStr = LotteryUtils.changeFT2BallNumber(mSelectButtons);
        order.setRedBall(numberStr);
        order.setTotalCount(mCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(mContext, lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        if (mCount == 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        }
        order.setLotteryType(lotteryType);
        isOk = LotteryOrderDbUtils.addLotteryOrder(order);
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
    }

    public void createFast3LocalOrder(String lotteryType, int playType, List<LotteryBall> mSameSelect,
                                      List<LotteryBall> mDifferentSelect, long mTotalCount) {
        boolean isOk = false;
        String msgStr = "";

        LogF.d(TAG, "快三二同号单选下注生成投注单 lotteryType=" + lotteryType + " playType=" + playType
                + "  same=" + GsonUtil.getInstance().toJson(mSameSelect) + "  diff=" + GsonUtil.getInstance().toJson(mDifferentSelect));
        if (mTotalCount <= 0) {
            msgStr = "请选择投注号码";
            iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
            return;
        }

        LotteryOrder order = new LotteryOrder();
        order.setPlayMode(playType);
        String sameStr = LotteryUtils.changeFT2BallNumber(mSameSelect);
        order.setRedBall(sameStr);

        String differentStr = LotteryUtils.changeFT2BallNumber(mDifferentSelect);
        order.setBlueBall(differentStr);

        order.setTotalCount((long) mTotalCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(mTotalCount * (float) SharedPreferenceUtil.get(mContext, lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        if (mTotalCount == 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        }
        order.setLotteryType(lotteryType);
        isOk = LotteryOrderDbUtils.addLotteryOrder(order);
        if (!isOk) {
            msgStr = "保存本地失败";
        }
        iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
    }

    /**
     * 创建本地投注单
     *
     * @param oneList   万
     * @param towList   千
     * @param threeList 百
     * @param mCount    订单数量
     */
    public void create115LocalOrder(List<LotteryBall> oneList,
                                    List<LotteryBall> towList,
                                    List<LotteryBall> threeList,
                                    long mCount, String lotteryType, int playType) {
        int oneSize = oneList == null ? 0 : oneList.size();
        int twoSize = towList == null ? 0 : towList.size();
        int threeSize = threeList == null ? 0 : threeList.size();
        LogF.d("calculateEleven5Count", "11选5 下单 ONE=" + oneSize + "  TWO=" + twoSize + "  three" + threeSize);
        boolean isOk = false;
        String msgStr = "";
        if (mCount <= 0) {
            msgStr = "请选择投注号码";
        }
        switch (playType) {
            case PLAY_11_5_ANY_2:
            case PLAY_11_5_FRONT_2_GROUP:
                if (oneSize < 2) {
                    msgStr = "至少选择2个号码";
                }
                break;
            case PLAY_11_5_ANY_3:
            case PLAY_11_5_FRONT_3_GROUP:
                if (oneSize < 3) {
                    msgStr = "至少选择3个号码";
                }
                break;
            case PLAY_11_5_ANY_4:
                if (oneSize < 4) {
                    msgStr = "至少选择4个号码";
                }
                break;
            case PLAY_11_5_ANY_5:
                if (oneSize < 5) {
                    msgStr = "至少选择5个号码";
                }
                break;
            case PLAY_11_5_ANY_6:
                if (oneSize < 6) {
                    msgStr = "至少选择6个号码";
                }
                break;
            case PLAY_11_5_ANY_7:
                if (oneSize < 7) {
                    msgStr = "至少选择7个号码";
                }
                break;
            case PLAY_11_5_ANY_8:
                if (oneSize < 8) {
                    msgStr = "至少选择8个号码";
                }
                break;
            case PLAY_11_5_FRONT_1_DIRECT:
                if (oneSize < 1) {
                    msgStr = "至少选择1个号码";
                }
                break;
            case PLAY_11_5_FRONT_2_DIRECT:
                if (oneSize < 1) {
                    msgStr = "至少选择1个万位号码";
                } else if (twoSize < 1) {
                    msgStr = "至少选择1个千位号码";
                } else if (mCount < 1) {
                    msgStr = "每位至少选择1个不同号码";
                }
                break;
            case PLAY_11_5_FRONT_3_DIRECT:
                if (oneSize < 1) {
                    msgStr = "至少选择1个万位号码";
                } else if (twoSize < 1) {
                    msgStr = "至少选择1个千位号码";
                } else if (threeSize < 1) {
                    msgStr = "至少选择1个百位号码";
                } else if (mCount < 1) {
                    msgStr = "每位至少选择1个不同号码";
                }
                break;
            //以下是胆拖
            case PLAY_11_5_FRONT_2_GROUP_DAN:
            case PLAY_11_5_ANY_2_DAN:
                if (oneSize < 1) {
                    msgStr = "胆码不能小于1个";
                } else if (twoSize < 1) {
                    msgStr = "拖码不能小于1个";
                } else if (oneSize + twoSize < 2) {
                    msgStr = "胆拖码不能小于2个";
                }
                break;
            case PLAY_11_5_FRONT_3_GROUP_DAN:
            case PLAY_11_5_ANY_3_DAN:
                if (oneSize < 1) {
                    msgStr = "胆码不能小于1个";
                } else if (twoSize < 1) {
                    msgStr = "拖码不能小于1个";
                } else if (oneSize + twoSize < 3) {
                    msgStr = "胆拖码不能小于3个";
                }
                break;
            case PLAY_11_5_ANY_4_DAN:
                if (oneSize < 1) {
                    msgStr = "胆码不能小于1个";
                } else if (twoSize < 1) {
                    msgStr = "拖码不能小于1个";
                } else if (oneSize + twoSize < 4) {
                    msgStr = "胆拖码不能小于4个";
                }
                break;
            case PLAY_11_5_ANY_5_DAN:
                if (oneSize < 1) {
                    msgStr = "胆码不能小于1个";
                } else if (twoSize < 1) {
                    msgStr = "拖码不能小于1个";
                } else if (oneSize + twoSize < 5) {
                    msgStr = "胆拖码不能小于5个";
                }
                break;
            case PLAY_11_5_ANY_6_DAN:
                if (oneSize < 1) {
                    msgStr = "胆码不能小于1个";
                } else if (twoSize < 1) {
                    msgStr = "拖码不能小于1个";
                } else if (oneSize + twoSize < 6) {
                    msgStr = "胆拖码不能小于6个";
                }
                break;
            case PLAY_11_5_ANY_7_DAN:
                if (oneSize < 1) {
                    msgStr = "胆码不能小于1个";
                } else if (twoSize < 1) {
                    msgStr = "拖码不能小于1个";
                } else if (oneSize + twoSize < 7) {
                    msgStr = "胆拖码不能小于7个";
                }
                break;
            case PLAY_11_5_ANY_8_DAN:
                if (oneSize < 1) {
                    msgStr = "胆码不能小于1个";
                } else if (twoSize < 1) {
                    msgStr = "拖码不能小于1个";
                } else if (oneSize + twoSize < 8) {
                    msgStr = "胆拖码不能小于8个";
                }
                break;
        }

        if (!TextUtils.isEmpty(msgStr)) {
            iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
            return;
        }

        LotteryOrder order = new LotteryOrder();
        order.setPlayMode(playType);

        if (oneList != null && oneList.size() > 0) {
            String wanStr = LotteryUtils.changeBallToBallNumber(oneList);
            order.setRedBall(wanStr);
        }

        if (towList != null && towList.size() > 0) {
            String qianStr = LotteryUtils.changeBallToBallNumber(towList);
            order.setBlueBall(qianStr);
        }

        if (threeList != null && threeList.size() > 0) {
            String baiStr = LotteryUtils.changeBallToBallNumber(threeList);
            order.setDanBall(baiStr);
        }

        order.setTotalCount(mCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(mContext, lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        if (mCount == 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        }
        order.setLotteryType(lotteryType);
        isOk = LotteryOrderDbUtils.addLotteryOrder(order);
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
    }

    /**
     * 创建本地投注单
     *
     * @param mPlayType 玩法
     * @param red       万
     * @param blue      千
     * @param dan       百
     * @param tuo       十
     * @param five      个
     * @param mCount    订单数量
     */
    public void createAlwayColorLocalOrder(String lotteryType, int playType, List<LotteryBall> red,
                                           List<LotteryBall> blue,
                                           List<LotteryBall> dan,
                                           List<LotteryBall> tuo,
                                           List<LotteryBall> five,
                                           long mCount) {
        int wanSize = red == null ? 0 : red.size();
        int qianSize = blue == null ? 0 : blue.size();
        int baiSize = dan == null ? 0 : dan.size();
        int shiSize = tuo == null ? 0 : tuo.size();
        int geSize = five == null ? 0 : five.size();
        boolean isOk = false;
        String msgStr = "";

        if (mCount <= 0) {
            msgStr = "请选择投注号码";
        }

        switch (playType) {
            case AppConstants.ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                if (shiSize <= 0) {
                    msgStr = "请在十位之少选择一种属性";
                } else if (geSize <= 0) {
                    msgStr = "请在个位之少选择一种属性";
                }
                break;
            case AppConstants.ALWAYS_COLOR_1_DIRECT:
                if (geSize <= 0) {
                    msgStr = "至少选择1个号码";
                }
                break;
            case AppConstants.ALWAYS_COLOR_2_DIRECT:
                if (shiSize <= 0) {
                    msgStr = "请在十位选择1个号码";
                } else if (geSize <= 0) {
                    msgStr = "请在个位选择1个号码";
                }
                break;
            case AppConstants.ALWAYS_COLOR_2_GROUP:
                if (mCount <= 0) {
                    msgStr = "至少选择2个号码";
                }
                break;
            case AppConstants.ALWAYS_COLOR_3_GROUP_3:
                if (mCount <= 0) {
                    msgStr = "至少选择2个号码";
                }
                break;
            case AppConstants.ALWAYS_COLOR_3_GROUP_6:
                if (mCount <= 0) {
                    msgStr = "至少选择3个号码";
                }
                break;
            case AppConstants.ALWAYS_COLOR_5_DIRECT:
            case AppConstants.ALWAYS_COLOR_5_ALL:
                if (wanSize <= 0) {
                    msgStr = "请在万位选择1个号码";
                } else if (qianSize <= 0) {
                    msgStr = "请在千位选择1个号码";
                } else if (baiSize <= 0) {
                    msgStr = "请在百位选择1个号码";
                } else if (shiSize <= 0) {
                    msgStr = "请在十位选择1个号码";
                } else if (geSize <= 0) {
                    msgStr = "请在个位选择1个号码";
                }
                break;
        }

        if (!TextUtils.isEmpty(msgStr)) {
            iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
            return;
        }

        isOk = LotteryOrderDbUtils.addLotteryOrder(lotteryType, playType, red, blue, dan, tuo, five, mCount);
        if (!isOk) {
            msgStr = "保存本地失败";
        }
        iActivity.createLocalOrderResult(lotteryType, playType, isOk, msgStr);
    }

    /**
     * 机选双色球
     *
     * @param isCache      是否需要缓存到投注单
     * @param mLotteryType
     * @param mPlayMode
     */
    public void machineAdd(boolean isCache, String mLotteryType, int mPlayMode) {
        Log.e(TAG, "machineAdd: mPlayMode is " + mPlayMode + " and mLotteryType is " + mLotteryType);
        LotteryOrder bean = new LotteryOrder();
        bean.setTotalCount((long) 1);
        bean.setTotalMoney("2");
        bean.setPlayMode(mPlayMode);
        bean.setLotteryType(mLotteryType);
        bean.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        //TODO 新增彩种需要适配的地方 Y
        if (mLotteryType.equals(LOTTERY_TYPE_FAST_3)//快3
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || mLotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            switch (mPlayMode) {
                case FAST_DEFAULT://默认下注
                case FAST_THREE_SUM://和值
                    bean.setPlayMode(FAST_THREE_SUM);
                    List<String> sumBalls = LotteryUtils.randomSelectFastThreeSumNumber(1);
                    String baiString = GsonUtils.getGsonInstance().toJson(sumBalls);
                    bean.setRedBall(baiString);
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
        } else if (mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)//11选5
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                || mLotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
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
        } else if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)//时时彩
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
                case ALWAYS_COLOR_3_GROUP_3://三星组三最低是2注
                    List<String> geBallG3 = LotteryUtils.randomSelectNumber(2);
                    String geStringG3 = GsonUtils.getGsonInstance().toJson(geBallG3);
                    bean.setFiveBall(geStringG3);
                    bean.setTotalCount((long) 2);
                    bean.setTotalMoney("4");
                    break;
                case ALWAYS_COLOR_3_GROUP_6:
                    List<String> geBallG6 = LotteryUtils.randomSelectNumber(3);
                    String geStringG6 = GsonUtils.getGsonInstance().toJson(geBallG6);
                    bean.setFiveBall(geStringG6);
                    break;
                case ALWAYS_COLOR_BIG_SMALL_SINGLE_DOUBLE:
                    List<String> shiBalls5 = LotteryUtils.randomBigSmallSingleDouble(1);
                    List<String> geBalls5 = LotteryUtils.randomBigSmallSingleDouble(1);
                    String shiString5 = GsonUtils.getGsonInstance().toJson(shiBalls5);
                    bean.setTuoBall(shiString5);
                    String geString5 = GsonUtils.getGsonInstance().toJson(geBalls5);
                    bean.setFiveBall(geString5);
                    break;
                case ALWAYS_COLOR_5_ALL:
                case ALWAYS_COLOR_5_DIRECT:
                    LotteryUtils.machineCreateAlwaysColorOrder(bean);
                    break;
                default:
                    mPlayMode = ALWAYS_COLOR_5_ALL;
                    LotteryUtils.machineCreateAlwaysColorOrder(bean);
                    break;
            }
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
        } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {//排列三
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
        } else if (mLotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {//排列五
            LotteryUtils.machineCreateAlwaysColorOrder(bean);
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_STAR)) {//七星彩
            LotteryUtils.machineCreate7StarOrder(bean);
        } else if (mLotteryType.equals(LOTTERY_TYPE_2_COLOR)) {//双色球
            List<String> redBalls = LotteryUtils.randomSelectNumber(AppConstants.BALL_MIN_SELECTED_NUMBER_RED, AppConstants.BALL_TYPE_RED);
            List<String> blueBalls = LotteryUtils.randomSelectNumber(AppConstants.BALL_MIN_SELECTED_NUMBER_BLUE, AppConstants.BALL_TYPE_BLUE);
            String redballString = GsonUtils.getGsonInstance().toJson(redBalls);
            bean.setRedBall(redballString);
            String blueballString = GsonUtils.getGsonInstance().toJson(blueBalls);
            bean.setBlueBall(blueballString);
        } else if (mLotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {//七乐彩
            List<String> redBalls = LotteryUtils.randomSelectSevenNumber(7);
            String redballString = GsonUtils.getGsonInstance().toJson(redBalls);
            bean.setRedBall(redballString);
        }
        boolean isOk = LotteryOrderDbUtils.addLotteryOrder(bean);
        iActivity.machineAddResult(bean, isOk);
    }

    /**
     * 删除订单
     *
     * @param order
     */
    public void deleteDoubleColorOrder(LotteryOrder order) {
        boolean isOk = LotteryOrderDbUtils.deleteLotteryOrder(order);
        if (isOk) {
            iActivity.deleteResult(true, "删除成功");
        } else {
            iActivity.deleteResult(false, StaticResourceUtils.getStringResourceById(R.string.tips_delete_fail));
        }
    }

    /**
     * 投注
     *
     * @param mOrders
     * @param chase
     * @param multiple
     * @param playMode
     * @param lotteryType
     * @param mSequence
     */
    public void createOrder(List<LotteryOrder> mOrders, int chase, int multiple, int playMode, String lotteryType, String mSequence) {
        if (mOrders == null || mOrders.size() <= 0) {
            iActivity.createOrderResult(false, false, "请选择投注号码");
            iActivity.hiddenWaitingDialog();
            return;
        }
        String jsonStr = "";
        //TODO 新增彩种需要适配的地方 Y
        if (lotteryType.equals(LOTTERY_TYPE_2_COLOR)) {
            DoubleColorOrder order = LotteryUtils.createDoubleColorOrder(chase, multiple, mOrders);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(LOTTERY_TYPE_3_D)) {
            ThreeDOrder order = LotteryUtils.createThreeDOrder(chase, multiple, mOrders, playMode, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(LOTTERY_TYPE_ARRANGE_3)) {
            ThreeDOrder order = LotteryUtils.createThreeDOrder(chase, multiple, mOrders, playMode, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(LOTTERY_TYPE_FAST_3)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_JS)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_HB)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_NEW)
                || lotteryType.equals(LOTTERY_TYPE_FAST_3_EASY)) {
            FastThreeOrder order = LotteryUtils.createFastThreeOrderNew(chase, multiple, mOrders, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(AppConstants.LOTTERY_TYPE_11_5)
                || lotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_LUCKY)
                || lotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_OLD)
                || lotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YUE)
                || lotteryType.equals(AppConstants.LOTTERY_TYPE_11_5_YILE)) {
            ElevenFiveOrder order = LotteryUtils.create115Order(chase, multiple, mOrders, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)
                || lotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR_NEW)) {
            AlwaysColorOrder order = LotteryUtils.createACOrder(chase, multiple, mOrders, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(LOTTERY_TYPE_ARRANGE_5)) {
            Arrange5Order order = LotteryUtils.createArrange5Order(chase, multiple, mOrders, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(LOTTERY_TYPE_7_STAR)) {
            SevenStarOrder order = LotteryUtils.create7StarOrder(chase, multiple, mOrders, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        } else if (lotteryType.equals(LOTTERY_TYPE_7_HAPPY)) {
            SevenHappyOrder order = LotteryUtils.createSevenHappy(chase, multiple, mOrders, mSequence);
            jsonStr = GsonUtils.getGsonInstance().toJson(order);
        }

        Log.e(TAG, "createOrder: jsonStr is " + jsonStr);

        if (!TextUtils.isEmpty(jsonStr)) {
            iActivity.createOrderResult(true, false, jsonStr);
        }
    }

    /**
     * 初始化订单列表
     */
    public void initDoubleColorData(String mLotteryType, int mPlayMode) {
        List<LotteryOrder> mOrders = LotteryOrderDbUtils.listAllLotteryOrder(mLotteryType, mPlayMode);
        iActivity.initLotteryOrderListResult(mOrders);
    }

    public void requestSequence(String mLotteryType) {
        iModel.requestCurrentSequence(mLotteryType, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iActivity.onRequestSequence(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.onRequestSequence(false, errorStr);
            }
        });
    }

    @Override
    public void onDestroy() {
        iModel = null;
    }
}
