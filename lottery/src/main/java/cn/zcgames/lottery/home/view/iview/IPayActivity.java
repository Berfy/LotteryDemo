package cn.zcgames.lottery.home.view.iview;

import cn.zcgames.lottery.base.IBaseView;

/**
 * 支付View接口
 * Berfy修改 2018.8.27
 */
public interface IPayActivity<T> extends IBaseView {

    void onBuyResult(boolean isOk, Object obj);

    void requestResultOk(T object);

    void requestResultFailed(boolean isNeedLogin, String msg);

}
