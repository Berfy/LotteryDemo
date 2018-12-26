package cn.zcgames.lottery.personal.view.iview;


/**
 * 用于验证手机号的iView
 *
 * @author NorthStar
 * @date 2018/8/20 16:51
 */
public interface IVerifyCodeActivity {

    //获取验证码
    void requestVerifyResult(boolean isOk, Object msg);

    //验证新注册用户的验证码
    void verifyCodeResult(boolean isOk, Object msg);

    void showWaitingMsg(String msg);

    //验证修改密码的验证码
//    void pwdVerifyCodeResult(boolean isOk, Object msg);

}
