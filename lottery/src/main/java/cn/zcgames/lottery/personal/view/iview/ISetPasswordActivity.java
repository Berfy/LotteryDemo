package cn.zcgames.lottery.personal.view.iview;

/**
 * 用于设置密码的iView
 *
 * @author NorthStar
 * @date 2018/8/20 17:27
 */
public interface ISetPasswordActivity{
    //type区分是去设置还是修改密码(登录/支付)
    void setPasswordSuccess(int type, String msg); //设置成功

    void setPasswordFail(String errorMsg);// //设置失败

    void setPasswordDoing(String msg);//正在设置

    //获取验证码
    void requestVerifyResult(boolean isOk, Object msg);
}
