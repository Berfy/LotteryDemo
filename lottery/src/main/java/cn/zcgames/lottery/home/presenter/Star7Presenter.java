package cn.zcgames.lottery.home.presenter;

import android.app.Activity;

import java.util.List;

import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.model.local.LotteryOrderDbUtils;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.IStar7Activity;

import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_7_STAR;
import static cn.zcgames.lottery.app.HttpHelper.STAR7_LATEST_WINNING;

/**
 * Created by admin on 2017/6/28.
 */

public class Star7Presenter {

    private static final String TAG = "Arrange5Presenter";

    private Activity mContext;
    private IStar7Activity iActivity;
    private ILotteryModel iLotteryModel;


    public Star7Presenter(Activity activity, IStar7Activity iView) {
        mContext = activity;
        this.iActivity = iView;
        iLotteryModel = new LotteryModel();
    }


    /**
     * 获取当前期号
     */
    public void requestCurrentSequence() {
        iLotteryModel.requestCurrentSequence(LOTTERY_TYPE_7_STAR, new NormalCallback() {
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

    /**
     * 查询前十条开奖记录
     */
    public void requestTopTenHistory() {
        iLotteryModel.requestResultHistory(AppConstants.LOTTERY_TYPE_7_STAR, STAR7_LATEST_WINNING, 10, 0, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                iActivity.requestResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iActivity.requestResult(false, errorStr);
            }
        });
    }

    /**
     * 创建本地投注单
     *
     * @param red
     * @param blue
     * @param dan
     * @param tuo
     * @param five
     * @param six
     * @param seven
     * @param mCount
     */
    public void createLocalOrder(List<LotteryBall> red,
                                 List<LotteryBall> blue,
                                 List<LotteryBall> dan,
                                 List<LotteryBall> tuo,
                                 List<LotteryBall> five,
                                 List<LotteryBall> six,
                                 List<LotteryBall> seven,
                                 int mCount) {
        boolean isOk = false;
        String msgStr = "";

        if (mCount <= 0) {
            msgStr = "请选择投注号码";
            iActivity.onCreateOrder(isOk, msgStr);
            return;
        }
        isOk = LotteryOrderDbUtils.addLotteryOrder(red, blue, dan, tuo, five,
                six, seven, mCount, -1, LOTTERY_TYPE_7_STAR);

        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iActivity.onCreateOrder(isOk, msgStr);
    }
}
