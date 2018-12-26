package cn.zcgames.lottery.personal.view.iview;

/**
 * Created by admin on 2017/5/9.
 */

public interface IIdentityInfoActivity {

    /**
     * 设置身份信息结果
     *
     * @param isOk
     * @param errorStr
     */
    void setIdentityInfoResult(boolean isOk, Object errorStr);

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
