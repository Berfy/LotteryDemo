package cn.zcgames.lottery.home.presenter;

//import com.lottery.entity.LotteryOrder;

import android.app.Activity;

import java.util.List;

import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.bean.response.ResultSequenceBean;
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.IDoubleColorActivity;

import static cn.zcgames.lottery.app.AppConstants.BALL_MIN_SELECTED_NUMBER_RED;
import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_2_COLOR;

/**
 * Created by admin on 2017/5/11.
 */

public class DoubleColorPresenter {

    private Activity mContext;
    private IDoubleColorActivity iActivity;
    private ILotteryModel iModel;

    public DoubleColorPresenter(Activity activity, IDoubleColorActivity iActivity) {
        mContext = activity;
        this.iActivity = iActivity;
        iModel = new LotteryModel();
    }

    /**
     * 计算胆拖投注的注数
     *
     * @param mSelectedDantuoBlueBall
     * @param mSelectedRedDanBall
     * @param mSelectedRedTuoBall
     */
    public void getDantuoTotalCoount(List<LotteryBall> mSelectedDantuoBlueBall, List<LotteryBall> mSelectedRedDanBall, List<LotteryBall> mSelectedRedTuoBall) {
        if (mSelectedDantuoBlueBall == null || mSelectedRedDanBall == null || mSelectedRedTuoBall == null) {
            iActivity.getDantuoTotalCountResul(0);
            return;
        }
        int redDanSize = mSelectedRedDanBall.size();
        int redTuoSize = mSelectedRedTuoBall.size();
        int blueBallSize = mSelectedDantuoBlueBall.size();
        long mDantuoTotalNumber;
        if (blueBallSize <= 0 || redDanSize <= 0 || redTuoSize <= 0) {
            iActivity.getDantuoTotalCountResul(0);
            return;
        }
//        if (redBallSize == BALL_MIN_SELECTED_NUMBER_RED) {
//            mTotalNumber = blueBallSize;
//        } else {
        mDantuoTotalNumber = CalculateCountUtils.doubleColorDantuo(redDanSize, redTuoSize, blueBallSize);
//        }

        iActivity.getDantuoTotalCountResul(mDantuoTotalNumber);
    }

    /**
     * 计算普通投注的注数
     *
     * @param mSelectedBlueBall
     * @param mSelectedRedBall
     */
    public void getNormalTotalCount(List<LotteryBall> mSelectedBlueBall, List<LotteryBall> mSelectedRedBall) {
        if (mSelectedBlueBall == null || mSelectedRedBall == null) {
            iActivity.getNormalTotalResult(0);
            return;
        }
        int redBallSize = mSelectedRedBall.size();
        int blueBallSize = mSelectedBlueBall.size();
        long mNormalTotalNumber;
        if (blueBallSize < 0 || redBallSize < BALL_MIN_SELECTED_NUMBER_RED) {
            iActivity.getNormalTotalResult(0);
            return;
        }
        if (redBallSize == BALL_MIN_SELECTED_NUMBER_RED) {
            mNormalTotalNumber = blueBallSize;
        } else {
            mNormalTotalNumber = CalculateCountUtils.doubleColorOrdinary(redBallSize, blueBallSize);
        }
        iActivity.getNormalTotalResult(mNormalTotalNumber);
    }

    /**
     * 创建普通投注订单
     *
     * @param blue
     * @param red
     * @param playStyle
     * @param count
     */
    public void createNormalOrder(List<LotteryBall> blue,
                                  List<LotteryBall> red,
                                  int playStyle,
                                  long count) {
        if (red == null || red.size() < AppConstants.BALL_MIN_SELECTED_NUMBER_RED) {
            iActivity.createNormalOrderResult(false, "红球个数不能少于6个");
            return;
        }
        if (blue == null || blue.size() < AppConstants.BALL_MIN_SELECTED_NUMBER_BLUE) {
            iActivity.createNormalOrderResult(false, "至少选择一个蓝球");
            return;
        }
        boolean isOk = LotteryOrderDbUtils.addLotteryOrder(red, blue, count, playStyle, LOTTERY_TYPE_2_COLOR);
        String msgStr = "";
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iActivity.createNormalOrderResult(isOk, msgStr);
    }

    /**
     * 创建胆拖投注订单
     *
     * @param blue
     * @param dan
     * @param tuo
     * @param playStyle
     * @param count
     */
    public void createDantuoOrder(List<LotteryBall> blue,
                                  List<LotteryBall> dan,
                                  List<LotteryBall> tuo,
                                  int playStyle,
                                  long count) {

        if (dan.size() < 1) {
            iActivity.createDantuoOrderResult(false, "胆区红球个数不能少于1个");
            return;
        }
        if (dan.size() > AppConstants.BALL_MAX_SELECTED_NUMBER_RED_DAN) {
            iActivity.createDantuoOrderResult(false, "胆区红球个数不能大于5个");
            return;
        }
        if (tuo.size() < 2) {
            iActivity.createDantuoOrderResult(false, "拖区红球个数不能少于2个");
            return;
        }
        if (blue.size() < AppConstants.BALL_MIN_SELECTED_NUMBER_BLUE) {
            iActivity.createDantuoOrderResult(false, "至少选择一个蓝球");
            return;
        }
        if (dan.size() + tuo.size() < 7) {
            iActivity.createDantuoOrderResult(false, "至少选择7个红球");
            return;
        }
        boolean isOk = LotteryOrderDbUtils.addLotteryOrder(dan, tuo, blue, count, playStyle, LOTTERY_TYPE_2_COLOR);

        String msgStr = "";
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iActivity.createDantuoOrderResult(isOk, msgStr);
    }

    /**
     * 获取当前双色球期号
     */
    public void requestCurrentSequence(String lotteryType) {
        iModel.requestCurrentSequence(lotteryType, new NormalCallback<ResultSequenceBean.SequenceBean>() {
            @Override
            public void responseOk(ResultSequenceBean.SequenceBean msgStr) {
                iActivity.requestSequenceResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.requestSequenceResult(false, errorStr);
            }
        });
    }
}
