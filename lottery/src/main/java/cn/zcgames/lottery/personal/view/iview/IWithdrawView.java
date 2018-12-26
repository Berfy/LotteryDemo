package cn.zcgames.lottery.personal.view.iview;


import cn.zcgames.lottery.base.IBaseView;

/**
 * 提现的iView
 *
 * @author NorthStar
 * @date 2018/9/20 14:00
 */
public interface IWithdrawView extends IBaseView {
    //获取用户收款码
    void getWithdrawWay(boolean isOk, Object object);
}
