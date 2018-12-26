package cn.zcgames.lottery.model.local;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.bean.LotteryOrderDao;

import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_DANSHI;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_FUSHI;

/**
 * Created by admin on 2017/5/24.
 */

public class LotteryOrderDbUtils {

    /**
     * 插入操作
     *
     * @param order
     * @return
     */
    public static boolean addLotteryOrder(LotteryOrder order) {
        long id = DaoManager.getInstance().getDaoSession().insert(order);
        if (id >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除
     *
     * @param order
     * @return
     */
    public static boolean deleteLotteryOrder(LotteryOrder order) {
        DaoManager.getInstance().getDaoSession().delete(order);
        return true;
    }

    public static boolean deleteLotteryOrder(List<LotteryOrder> orders) {
        DaoManager.getInstance().getDaoSession().getLotteryOrderDao().deleteInTx(orders);
        return true;
    }

    /**
     * 删除全部
     *
     * @return
     */
    public static boolean deleteAllLotteryOrder() {
        DaoManager.getInstance().getDaoSession().deleteAll(LotteryOrder.class);
        return true;
    }

    /**
     * 查询单个彩种投注列表
     */
    public static List<LotteryOrder> listAllLotteryOrder(String lotteryType) {
        return listAllLotteryOrder(lotteryType, -1);
    }

    /**
     * 查询单个彩种和玩法的投注列表
     */
    public static List<LotteryOrder> listAllLotteryOrder(String lotteryType, int mPlayMode) {
        //查询构建器
        QueryBuilder<LotteryOrder> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(LotteryOrder.class);
        if (mPlayMode == -1) {
            return queryBuilder.where(LotteryOrderDao.Properties.LotteryType.eq(lotteryType))
                    .orderDesc(LotteryOrderDao.Properties.Id).list();
        } else {
            return queryBuilder.where(LotteryOrderDao.Properties.LotteryType.eq(lotteryType))
                    .where(LotteryOrderDao.Properties.PlayMode.eq(mPlayMode))
                    .orderDesc(LotteryOrderDao.Properties.Id)
                    .list();
        }
    }

    public static boolean addLotteryOrder(List<LotteryBall> balls,
                                          long totalCount, int playStyle, String lotteryType) {
        return addLotteryOrder(lotteryType, playStyle, balls, null, null, null, null, totalCount);
    }

    public static boolean addLotteryOrder(List<LotteryBall> red, List<LotteryBall> blue,
                                          long totalCount, int playStyle, String lotteryType) {
        return addLotteryOrder(lotteryType, playStyle, red, blue, null, null, null, totalCount);
    }

    public static boolean addLotteryOrder(List<LotteryBall> dan, List<LotteryBall> tuo, List<LotteryBall> blue,
                                          long totalCount, int playStyle, String lotteryType) {
        return addLotteryOrder(lotteryType, playStyle, null, blue, dan, tuo, null, totalCount);
    }

    public static boolean addLotteryOrder(String lotteryType, int playStyle,
                                          List<LotteryBall> redBall, List<LotteryBall> blueBall,
                                          List<LotteryBall> danBall, List<LotteryBall> tuoBall,
                                          List<LotteryBall> fiveBall, long mCount) {
        return addLotteryOrder(redBall, blueBall, danBall, tuoBall,
                fiveBall, null, null, mCount, playStyle, lotteryType);
    }

    /**
     * @param redBall
     * @param blueBall
     * @param danBall
     * @param tuoBall
     * @param fiveBall
     * @param sixBall
     * @param sevenBall
     * @param mCount      注数
     * @param playStyle   玩法
     * @param lotteryType 彩票类型
     * @return
     */
    public static boolean addLotteryOrder(List<LotteryBall> redBall, List<LotteryBall> blueBall,
                                          List<LotteryBall> danBall, List<LotteryBall> tuoBall,
                                          List<LotteryBall> fiveBall, List<LotteryBall> sixBall,
                                          List<LotteryBall> sevenBall, long mCount, int playStyle,
                                          String lotteryType) {
        LotteryOrder order = new LotteryOrder();
        order.setPlayMode(playStyle);

        if (redBall != null && redBall.size() > 0) {
            String wanStr = LotteryUtils.changeBallToBallNumber(redBall);
            order.setRedBall(wanStr);
        }

        if (blueBall != null && blueBall.size() > 0) {
            String qianStr = LotteryUtils.changeBallToBallNumber(blueBall);
            order.setBlueBall(qianStr);
        }

        if (danBall != null && danBall.size() > 0) {
            String baiStr = LotteryUtils.changeBallToBallNumber(danBall);
            order.setDanBall(baiStr);
        }

        if (tuoBall != null && tuoBall.size() > 0) {
            String shiStr = LotteryUtils.changeBallToBallNumber(tuoBall);
            order.setTuoBall(shiStr);
        }

        if (fiveBall != null && fiveBall.size() > 0) {
            String geStr = LotteryUtils.changeBallToBallNumber(fiveBall);
            order.setFiveBall(geStr);
        }

        if (sixBall != null && sixBall.size() > 0) {
            String geStr = LotteryUtils.changeBallToBallNumber(sixBall);
            order.setSixBall(geStr);
        }

        if (sevenBall != null && sevenBall.size() > 0) {
            String geStr = LotteryUtils.changeBallToBallNumber(sevenBall);
            order.setSevenBall(geStr);
        }

        order.setTotalCount(mCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(mCount * (float) SharedPreferenceUtil.get(MyApplication.getAppContext(), lotteryType + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        if (mCount == 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        }
        order.setLotteryType(lotteryType);
        return LotteryOrderDbUtils.addLotteryOrder(order);
    }

}