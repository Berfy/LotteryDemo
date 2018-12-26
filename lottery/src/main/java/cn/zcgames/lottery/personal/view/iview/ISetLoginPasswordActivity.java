package cn.zcgames.lottery.personal.view.iview;

/**
 * 设置账户密码的iView
 *
 * @author NorthStar
 * @date 2017/5/9 17:10
 */
public interface ISetLoginPasswordActivity {

    /**
     * 检查登录密码是否认证结果
     *
     * @param isOk
     */
    void checkPasswordResult(boolean isOk);

    /**
     * 修改登录密码结果
     *
     * @param isOk
     */
    void updateLoginPasswordResult(boolean isOk, Object rStr);

    /**
     * 设置登录密码结果
     *
     * @param isOk
     * @param Str
     */
    void setLoginPasswordResult(boolean isOk, Object Str);

    /**
     * 显示等待框
     *
     * @param msgStr
     */
    void showWaitingDialog(String msgStr);

    /**
     * 隐藏等待框
     */
    void hiddenWaitingDialog();

}
