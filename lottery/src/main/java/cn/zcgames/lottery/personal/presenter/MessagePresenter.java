package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;

/**
 * 站内消息的presenter
 *
 * @author NorthStar
 * @date 2017/5/18 17:02
 */
public class MessagePresenter {

    private static final String TAG = "OrderRecordPresenter";

    private Activity mContext;
    private IBaseView iView;
    private IAccountModel iModel;

    public MessagePresenter(Activity activity, IBaseView iView) {
        mContext = activity;
        this.iView = iView;
        iModel = new AccountModel();
    }

    /**
     * 获取站内信息
     *
     * @param startPageIdx 从1开始
     * @param ts           第一次传0,之后不传
     */
    public void requestMessage(int startPageIdx, long ts) {
        iModel.requestMessage(startPageIdx, ts, new NormalCallback() {
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
