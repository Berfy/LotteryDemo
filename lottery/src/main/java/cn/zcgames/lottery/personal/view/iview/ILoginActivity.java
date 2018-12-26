package cn.zcgames.lottery.personal.view.iview;

import cn.zcgames.lottery.personal.model.UserBean;

/**
 * 用于登录的iView
 */
public interface ILoginActivity{

    //区分登录类型
    int LOGIN_OK_TYPE_THIRDPLAT = 1;
    int LOGIN_OK_TYPE_OUR_SERVER = 0;

    void loginSuccess(UserBean user, int type);

    void loginFail(String errorMsg);

    void loginLoading(String msgStr);

    void hideDialog();

}
