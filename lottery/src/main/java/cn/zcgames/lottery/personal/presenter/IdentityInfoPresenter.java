package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.personal.view.iview.IIdentityInfoActivity;
import cn.zcgames.lottery.utils.AppValidationMgr;
import cn.zcgames.lottery.utils.StaticResourceUtils;

/**
 * 绑定用户身份信息的presenter
 *
 * @author NorthStar
 * @date 2018/8/20 17:17
 */
public class IdentityInfoPresenter {

    private static final String TAG = "IdentityInfoPresenter";

    private Activity mContext;
    private IAccountModel mModel;
    private IIdentityInfoActivity mActivity;

    public IdentityInfoPresenter(Activity activity, IIdentityInfoActivity iActivity) {
        mContext = activity;
        mActivity = iActivity;
        mModel = new AccountModel();
    }

    /**
     * 设置用户身份信息
     *
     * @param name
     * @param id
     */
    public void setIdentityInfo(String name, String id) {
        if (!AppValidationMgr.isRealName(name)) {
            mActivity.setIdentityInfoResult(false, "请填写真实姓名");
            return;
        }

        if (!AppValidationMgr.isIDCard(id)) {
            mActivity.setIdentityInfoResult(false, "请填写真实身份证号码");
            return;
        }

        mActivity.showWaitingDialog(StaticResourceUtils.getStringResourceById(R.string.tips_setting));
        mModel.setIdentityInfo(name, id, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mActivity.hiddenWaitingDialog();
                mActivity.setIdentityInfoResult(true, msgStr);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    UIHelper.gotoActivity(mContext, LoginActivity.class, false);
                }
                mActivity.hiddenWaitingDialog();
                mActivity.setIdentityInfoResult(false, errorMsg);
            }
        });
    }
}
