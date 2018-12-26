package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.BuyHistoryBean;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 订单记录presenter
 *
 * @author NorthStar
 * @date 2017/5/18 17:02
 */
public class OrderRecordPresenter {

    private static final String TAG = "OrderRecordPresenter";

    private Activity mContext;
    private IBaseView iView;
    private IAccountModel iModel;

    public OrderRecordPresenter(Activity activity, IBaseView iView) {
        mContext = activity;
        this.iView = iView;
        iModel = new AccountModel();
    }

    /**
     * 获取购彩记录
     *
     * @param status 1 全部 2已开奖 3待开奖
     */
    public void requestOrderRecord(int startPageIdx, int pageMaxNum, String status) {
        /*iView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));*/
        iModel.requestOrderRecord(startPageIdx, pageMaxNum, 1, new NormalCallback() {
            @Override
            public void responseOk(Object result) {
                iView.requestResult(true, result);
                /*iView.hideTipDialog();*/
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iView.requestResult(false, errorStr);
                /*iView.hideTipDialog();*/
            }
        });
    }

    /**
     * 获取购彩记录
     *
     * @param status 1 全部 2已开奖 3待开奖
     */
    public void requestOrderRecord(int startPageIdx, long ts, int status) {
        iModel.requestOrderRecord(startPageIdx, ts, status, new NormalCallback() {
            @Override
            public void responseOk(Object result) {
                iView.requestResult(true, result);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorStr) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                iView.requestResult(false, errorStr);
            }
        });
    }
}
