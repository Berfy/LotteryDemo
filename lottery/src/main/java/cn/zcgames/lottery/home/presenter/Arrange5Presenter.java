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
import cn.zcgames.lottery.home.view.iview.IArrange5Activity;

import static cn.zcgames.lottery.app.AppConstants.LOTTERY_TYPE_ARRANGE_5;
import static cn.zcgames.lottery.app.HttpHelper.ARRANGE5_LATEST_WINNING;

/**
 * Created by admin on 2017/6/28.
 */

public class Arrange5Presenter {

    private static final String TAG = "Arrange5Presenter";

    private Activity mContext;
    private IArrange5Activity iActivity;
    private ILotteryModel iLotteryModel;


    public Arrange5Presenter(Activity activity, IArrange5Activity iView) {
        mContext = activity;
        this.iActivity = iView;
        iLotteryModel = new LotteryModel();
    }


    /**
     * 获取当前期号
     */
    public void requestCurrentSequence() {
        iLotteryModel.requestCurrentSequence(LOTTERY_TYPE_ARRANGE_5, new NormalCallback() {
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
        iLotteryModel.requestResultHistory(AppConstants.LOTTERY_TYPE_ARRANGE_5, ARRANGE5_LATEST_WINNING, 10, 0, new NormalCallback() {
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
     * @param redBall  万
     * @param blueBall 千
     * @param danBall  百
     * @param tuoBall  十
     * @param fiveBall 个
     * @param mCount   订单数量
     */
    public void createLocalOrder(List<LotteryBall> redBall,
                                 List<LotteryBall> blueBall,
                                 List<LotteryBall> danBall,
                                 List<LotteryBall> tuoBall,
                                 List<LotteryBall> fiveBall,
                                 int mCount) {
        boolean isOk = false;
        String msgStr = "";

        if (mCount <= 0) {
            msgStr = "请选择投注号码";
            iActivity.onCreateOrder(isOk, msgStr);
            return;
        }
        isOk = LotteryOrderDbUtils.addLotteryOrder(LOTTERY_TYPE_ARRANGE_5, -1, redBall, blueBall, danBall, tuoBall,
                fiveBall, mCount);
        if (!isOk) {
            msgStr = "保存本地失败";
        }

        iActivity.onCreateOrder(isOk, msgStr);
    }
}
