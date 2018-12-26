package cn.zcgames.lottery.personal.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.qiaoxg.dialoglibrary.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.response.ResponseNormalBean;
import cn.zcgames.lottery.event.LogoutEvent;
import cn.zcgames.lottery.personal.model.UserBean;
import cn.zcgames.lottery.personal.view.iview.IAccountModel;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;
import cn.zcgames.lottery.model.remote.impl.AccountModel;
import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.utils.okhttp.OkHttpUtils;
import cn.zcgames.lottery.utils.okhttp.callback.GenericsCallback;
import cn.zcgames.lottery.utils.okhttp.callback.JsonGenericsSerializer;
import okhttp3.Call;

/**
 * 我的模块的Presenter
 *
 * @author NorthStar
 * @date 2018/8/20 15:59
 */
public class MinePresenter {

    private Activity mContext;
    private IBaseView mBaseView;
    private IAccountModel mMineFragmentModel;
    public static final String TAG = "MinePresenter";

    public MinePresenter(Activity activity, IBaseView baseView) {
        mContext = activity;
        mBaseView = baseView;
        mMineFragmentModel = new AccountModel();
    }

    //请求用户数据
    public void requestMineData() {
        //        mBaseView.showTipDialog(StaticResourceUtils.getStringResourceById(R.string.tips_loading));
        mMineFragmentModel.requestMinePageData(new NormalCallback() {
            @Override
            public void responseOk(Object obj) {
                //                mBaseView.hideTipDialog();
                mBaseView.requestResult(true, obj);
                updateUser((UserBean) obj);
            }

            @Override
            public void responseFail(boolean isNeedLogin, String errorMsg) {
                if (isNeedLogin) {
                    MyApplication.updateCurrLoginUser(null);//清除本地user
                }
                mBaseView.requestResult(false, errorMsg);
                setFailMineData(errorMsg);
            }
        });
    }

    //从服务器上获取用户信息成功之后更新本地缓存信息
    private void updateUser(UserBean bean) {
        UserBean user = MyApplication.getCurrLoginUser();
        if (user != null) {
            user.setAvatar(bean.getAvatar());
            user.setNickname(bean.getNickname());
            user.setMoney(bean.getMoney());
            user.setIncome(bean.getIncome());
            user.setIsPayPwd(bean.getIsPayPwd());
            user.setIsIdentSet(bean.getIsIdentSet());
            user.setIs_mobile_set(bean.getIs_mobile_set());
            MyApplication.updateCurrLoginUser(user);
            LogF.d(TAG,"userbean==>"+user.toString());
        }
    }

    //从服务器上获取用户信息失败之后尝试读取本地缓存信息
    private void setFailMineData(String errorStr) {
        //        mBaseView.hideTipDialog();
        UserBean user = MyApplication.getCurrLoginUser();
        if (user != null) {
            mBaseView.requestResult(true, user);
        } else {
            mBaseView.requestResult(false, errorStr);
        }
    }


    //设置退出登录事件
    public void setLogout() {
        AlertDialog alertDialog = new AlertDialog(mContext)
                .builder()
                .setCancelable(false)
                .setMsg(R.string.Your_Sure_Back)
                .setNegativeButton("", v -> {
                })
                .setPositiveButton("", v -> {
                    MyApplication.getInstance().unSubscribeAllTopic();
                    MinePresenter.this.logoutCurrentUser();
                });
        alertDialog.show();
    }


    private void logoutCurrentUser() {
        String tokenStr = MyApplication.getTokenId();
        ActivityManager.getInstance().popAllActivityExceptOne(MainActivity.class);
        if (TextUtils.isEmpty(tokenStr)) {
            UIHelper.showToast("token is null");
            return;
        }
        final LogoutEvent logoutEvent = new LogoutEvent(true);
        OkHttpUtils.postString()
                .url(HttpHelper.ACCOUNT_LOGOUT)
                .content("")
                .build()
                .execute(new GenericsCallback<ResponseNormalBean>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogF.d(TAG, "logoutError==>" + e.getMessage());
                        EventBus.getDefault().post(logoutEvent);
                    }

                    @Override
                    public void onResponse(ResponseNormalBean response, int id) {
                        if (response == null) return;
                        if (response.isOk()) {
                            EventBus.getDefault().post(logoutEvent);
                            LogF.d(TAG, "logoutSuccess==>" + response.getMsg());
                        } else {
                            EventBus.getDefault().post(logoutEvent);
                            LogF.d(TAG, "logoutFail==>" + response.getMsg());
                        }
                    }
                });
    }
}
