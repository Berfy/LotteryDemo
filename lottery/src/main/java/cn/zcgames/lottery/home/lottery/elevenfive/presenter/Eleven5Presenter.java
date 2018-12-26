package cn.zcgames.lottery.home.lottery.elevenfive.presenter;

import android.app.Activity;

import java.util.List;

import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.bean.LotteryBall;
import cn.zcgames.lottery.model.local.CalculateCountUtils;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.home.view.iview.IEleven5Activity;

/**
 * Created by admin on 2017/6/28.
 */
public class Eleven5Presenter {

    private static final String TAG = "11选5Presenter";

    private Activity mContext;
    private IEleven5Activity mIActivity;
    private ILotteryModel mILotteryModel;


    public Eleven5Presenter(Activity activity, IEleven5Activity iView) {
        mContext = activity;
        mIActivity = iView;
        mILotteryModel = new LotteryModel();
    }

    /**
     * 获取当前期号
     */
    public void requestCurrentSequence(String lotteryType) {
        mILotteryModel.requestCurrentSequence(lotteryType, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mIActivity.onRequestSequence(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mIActivity.onRequestSequence(false, errorStr);
            }
        });
    }

    /**
     * 根据玩法计算总投注
     *
     * @param oneList
     * @param towList
     * @param threeList
     * @param mPlayType
     */
    public void getCountByPlayStyle(List<LotteryBall> oneList,
                                    List<LotteryBall> towList,
                                    List<LotteryBall> threeList, int mPlayType) {
        mIActivity.onTotalCount(CalculateCountUtils.calculateEleven5Count(oneList, towList, threeList, mPlayType));
    }
}
