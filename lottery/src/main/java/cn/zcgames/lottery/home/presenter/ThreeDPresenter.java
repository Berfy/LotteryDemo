package cn.zcgames.lottery.home.presenter;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtil;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.LotteryOrder;
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.IThreeDActivity;
import cn.zcgames.lottery.model.local.LotteryUtils;

import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_DANSHI;
import static cn.zcgames.lottery.app.AppConstants.DOUBLE_COLOR_ORDER_TYPE_FUSHI;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_3_D;
import static cn.zcgames.lottery.app.AppConstants.THREE_D_PLAY_GROUP_SIX;

/**
 * Created by admin on 2017/5/24.
 */

public class ThreeDPresenter {

    private static final String TAG = "ThreeDPresenter";

    private Activity mContext;
    private IThreeDActivity iThreeDActivity;
    private ILotteryModel iModel;

    public ThreeDPresenter(Activity activity, IThreeDActivity iThreeDActivity) {
        mContext = activity;
        this.iThreeDActivity = iThreeDActivity;
        iModel = new LotteryModel();
    }

    /**
     * 查询当前期号
     */
    public void requestSequence() {
        iModel.requestCurrentSequence(LOTTERY_TYPE_3_D, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iThreeDActivity.onRequestSequence(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iThreeDActivity.onRequestSequence(false, errorStr);
            }
        });
    }

    /**
     * 计算直选的注数
     *
     * @param mHundredBalls
     * @param mTenBalls
     * @param mOneBalls
     */
    public void getDirectTotalCount(List<LotteryBall> mHundredBalls, List<LotteryBall> mTenBalls, List<LotteryBall> mOneBalls) {
        if (mHundredBalls == null || mHundredBalls.size() <= 0) {
            iThreeDActivity.onTotalCountResult(0);
            return;
        }
        if (mTenBalls == null || mTenBalls.size() <= 0) {
            iThreeDActivity.onTotalCountResult(0);
            return;
        }
        if (mOneBalls == null || mOneBalls.size() <= 0) {
            iThreeDActivity.onTotalCountResult(0);
            return;
        }
        int count = CalculateCountUtils.getThreeDDirectCount(mHundredBalls.size(), mTenBalls.size(), mOneBalls.size());
        iThreeDActivity.onTotalCountResult(count);
    }

    /**
     * 计算组选3的注数
     *
     * @param mSelectedBalls
     */
    public void getGroup3Count(List<LotteryBall> mSelectedBalls) {
        if (mSelectedBalls == null || mSelectedBalls.size() < 2) {
            iThreeDActivity.onTotalCountResult(0);
            return;
        }
        int count = CalculateCountUtils.getGroup3Count(mSelectedBalls.size());
        iThreeDActivity.onTotalCountResult(count);
    }

    /**
     * 计算组选6的注数
     *
     * @param mSelectedBalls
     */
    public void getGroup6Count(List<LotteryBall> mSelectedBalls) {
        if (mSelectedBalls == null || mSelectedBalls.size() < 3) {
            iThreeDActivity.onTotalCountResult(0);
            return;
        }
        int count = CalculateCountUtils.getGroup6Count(mSelectedBalls.size());
        iThreeDActivity.onTotalCountResult(count);
    }

    /**
     * 创建3d直选订单
     *
     * @param playStyle
     * @param red
     * @param mTenBalls
     * @param mOneBalls
     * @param totalCount
     */
    public void createDirectOrder(int playStyle,
                                  List<LotteryBall> red,
                                  List<LotteryBall> mTenBalls,
                                  List<LotteryBall> mOneBalls,
                                  long totalCount) {
        boolean isOk = false;
        String msgStr = "";

        if (red == null || red.size() <= 0) {
            msgStr = "至少选择一个百位号码";
            iThreeDActivity.onCreateDirectOrder(isOk, msgStr);
            return;
        }
        if (mTenBalls == null || mTenBalls.size() <= 0) {
            msgStr = "至少选择一个十位号码";
            iThreeDActivity.onCreateDirectOrder(isOk, msgStr);
            return;
        }
        if (mOneBalls == null || mOneBalls.size() <= 0) {
            msgStr = "至少选择一个个位号码";
            iThreeDActivity.onCreateDirectOrder(isOk, msgStr);
            return;
        }
        LotteryOrder order = new LotteryOrder();

        order.setPlayMode(playStyle);

        String hundredStr = LotteryUtils.changeBallToBallNumber(red);
        order.setRedBall(hundredStr);

        order.setTotalCount(totalCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(totalCount * (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_3_D + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        if (totalCount == 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        }

        String tenStr = LotteryUtils.changeBallToBallNumber(mTenBalls);
        order.setDanBall(tenStr);

        String oneStr = LotteryUtils.changeBallToBallNumber(mOneBalls);
        order.setTuoBall(oneStr);

        order.setLotteryType(LOTTERY_TYPE_3_D);

        isOk = LotteryOrderDbUtils.addLotteryOrder(order);
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iThreeDActivity.onCreateDirectOrder(isOk, msgStr);
    }

    /**
     * 创建3D组选订单
     *
     * @param playStyle
     * @param mSelectedBalls
     * @param totalCount
     */
    public void createGroupOrder(int playStyle, List<LotteryBall> mSelectedBalls, long totalCount) {
        boolean isOk = false;
        String msgStr = "";
        int size;
        if (playStyle == THREE_D_PLAY_GROUP_SIX) {
            size = 3;
        } else {
            size = 2;
        }

        if (mSelectedBalls == null || mSelectedBalls.size() < size) {
            msgStr = "至少选择" + size + "个号码";
            Log.e(TAG, "createGroupOrder: size is " + size);
            iThreeDActivity.onCreateDirectOrder(isOk, msgStr);
            return;
        }
        LotteryOrder order = new LotteryOrder();

        order.setPlayMode(playStyle);

        String hundredStr = LotteryUtils.changeBallToBallNumber(mSelectedBalls);
        order.setRedBall(hundredStr);

        order.setTotalCount(totalCount);
        order.setTotalMoney(StringUtils.getNumberNoZero(totalCount * (float) SharedPreferenceUtil.get(mContext, AppConstants.LOTTERY_TYPE_3_D + "_price", AppConstants.LOTTERY_DEFAULT_PRICE)));
        if (totalCount == 1) {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_DANSHI);
        } else {
            order.setOrderType(DOUBLE_COLOR_ORDER_TYPE_FUSHI);
        }

        order.setLotteryType(LOTTERY_TYPE_3_D);

        isOk = LotteryOrderDbUtils.addLotteryOrder(order);
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iThreeDActivity.onCreateDirectOrder(isOk, msgStr);
    }
}
