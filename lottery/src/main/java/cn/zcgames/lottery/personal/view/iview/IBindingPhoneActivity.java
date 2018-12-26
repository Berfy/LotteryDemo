package cn.zcgames.lottery.personal.view.iview;

/**
 * 用于绑定手机号的iView
 *
 * @author NorthStar
 * @date 2017/5/9 16:50
 */
public interface IBindingPhoneActivity {

    /**
     * 显示等待框
     *
     * @param msg
     */
    void showWaitingMsg(String msg);

    //获取验证码
    void requestVerifyResult(boolean isOk, Object msg);


    /**
     * 验证注册的验证码,并绑定手机号或邮箱
     *
     * @param isOk
     * @param msg
     */
    void bindResult(boolean isOk, Object msg);

}
