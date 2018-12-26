package cn.zcgames.lottery.message.presenter;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.response.ResultBallBean;
import cn.zcgames.lottery.bean.response.ResultBeanData;
import cn.zcgames.lottery.bean.response.ResultPageDataBean;
import cn.zcgames.lottery.message.view.iview.ILotteryModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.LotteryModel;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;

/**
 * 首页-开奖的presenter
 *
 * @author NorthStar
 * @date 2018/8/20 18:21
 */
public class ResultFragmentPresenter {

    private static final String TAG = "ResultFragmentPresenter";

    private Activity mContext;
    private IBaseView mBaseView;
    private ILotteryModel iModel;

    public ResultFragmentPresenter(Activity activity, IBaseView baseView) {
        mContext = activity;
        mBaseView = baseView;
        iModel = new LotteryModel();
    }

    //获取开奖信息
    public void requestWinningInfo() {
//        mBaseView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        iModel.requestWinningInfo(new NormalCallback<ResultBeanData>() {
            @Override
            public void responseOk(ResultBeanData result) {
                LogF.d(TAG, "开奖结果" + GsonUtil.getInstance().toJson(result));
                mBaseView.requestResult(true, result);
//                mBaseView.hideTipDialog();

                //假数据
//                ResultBeanData resultBeanData = new ResultBeanData();
//                List<ResultPageDataBean> list = new ArrayList<>();
//                ResultPageDataBean resultPageDataBean = new ResultPageDataBean();
//                resultPageDataBean.setLott_inner_name(AppConstants.LOTTERY_TYPE_ALWAYS_COLOR);
//                resultPageDataBean.setName("重庆时时彩");
//                resultPageDataBean.setType("ball");
//                resultPageDataBean.setDatetime(System.currentTimeMillis());
//                resultPageDataBean.setPeriod("123");
//                List<ResultBallBean> resultBallBeans = new ArrayList<>();
//                ResultBallBean resultBallBean = new ResultBallBean();
//                resultBallBean.setValue(new String[]{"1","2","3","4","5"});
//                resultBallBean.setColor("dd3048");
//                resultBallBeans.add(resultBallBean);
//                resultPageDataBean.setBall(resultBallBeans);
//                list.add(resultPageDataBean);
//                ResultPageDataBean resultPageDataBean1 = new ResultPageDataBean();
//                resultPageDataBean1.setLott_inner_name(AppConstants.LOTTERY_TYPE_11_5);
//                resultPageDataBean1.setName("11选5");
//                resultPageDataBean1.setType("ball");
//                resultPageDataBean1.setDatetime(System.currentTimeMillis());
//                resultPageDataBean1.setPeriod("123");
//                List<ResultBallBean> resultBallBeans1 = new ArrayList<>();
//                ResultBallBean resultBallBean1 = new ResultBallBean();
//                resultBallBean1.setValue(new String[]{"1","2","3","4","5"});
//                resultBallBean1.setColor("dd3048");
//                resultBallBeans1.add(resultBallBean1);
//                resultPageDataBean1.setBall(resultBallBeans1);
//                list.add(resultPageDataBean1);
//                resultBeanData.setPayload(list);
//                mBaseView.requestResult(true, resultBeanData);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }

                mBaseView.requestResult(false, errorStr);
//                mBaseView.hideTipDialog();
            }
        });
    }

}
