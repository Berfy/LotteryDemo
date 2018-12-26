package cn.zcgames.lottery.base;

/**
 * Created by admin on 2017/5/18.
 */

public interface IBaseView {
    void requestResult(boolean isOk, Object object);

    void showTipDialog(String msgStr);

    void hideTipDialog();
}
