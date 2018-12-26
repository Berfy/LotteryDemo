package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.AccountDetailBean;
import cn.zcgames.lottery.personal.model.AccountDetail;
import cn.zcgames.lottery.personal.view.activity.LoginActivity;
import cn.zcgames.lottery.personal.view.iview.IWalletModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.WalletModel;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.utils.StaticResourceUtils;
import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_AWARD;
import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_BUY;
import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_RECHARGE;
import static cn.zcgames.lottery.app.AppConstants.ACCOUNT_DETAIL_TYPE_WITHDRAW;

/**
 * 账户明细的presenter
 *
 * @author NorthStar
 * @date 2017/6/12 17:20
 */
public class AccountDetailPresenter {

    private Activity mContext;
    private IBaseView mBaseView;
    private IWalletModel iWalletModel;

    public AccountDetailPresenter(Activity context, IBaseView view) {
        mContext = context;
        mBaseView = view;
        this.iWalletModel = new WalletModel();
    }

    /**
     * 请求账户明细
     *
     * @param position
     */
    public void requestAccountDetail(final int position, int pageIndex, int pageSize) {
//        mBaseView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));

        int type = 0;//全部
        switch (position) {
            case 1:
                type = 2;//购买
                break;
            case 2:
                type = 1;//充值

                break;
            case 3:
                type = 4;//中奖

                break;
            case 4:
                type = 3;//提现
                break;
        }

        iWalletModel.requestAccountDetail(pageIndex, pageSize, type, new NormalCallback() {
            @Override
            public void responseOk(Object msgStr) {
                mBaseView.requestResult(true, msgStr);
//                mBaseView.hideTipDialog();
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

    private AccountDetail getAccountDetailBeanByType(String typeStr, AccountDetail bean) {
        if (TextUtils.isEmpty(typeStr)) {
            return bean;
        }
        List<AccountDetailBean> beans = new ArrayList<>();
        List<AccountDetailBean> old = bean.getDetails();
        if (old != null && old.size() > 0) {
            for (AccountDetailBean detail : old) {
                if (detail.getType().equals(typeStr)) {
                    beans.add(detail);
                }
            }
        }

        bean.setDetails(beans);
        return bean;
    }
}
