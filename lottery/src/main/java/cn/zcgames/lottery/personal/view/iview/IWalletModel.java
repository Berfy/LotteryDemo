package cn.zcgames.lottery.personal.view.iview;

import cn.zcgames.lottery.model.remote.callback.CommonCallback;
import cn.zcgames.lottery.model.remote.callback.NormalCallback;

/**
 * 关于充值,提现,支付等交易业务的iView
 *
 * @author NorthStar
 * @date 2018/8/29 12:10
 */
public interface IWalletModel {

    /**
     * 充值
     *
     * @param amount   充值金额  以分为单位
     * @param payType  充值方式  100 支付宝   200 微信
     * @param goods_name  选择的充值金额:10元
     * @param callback 回调
     */
    void recharge(String goods_name, long amount, int payType, CommonCallback callback);

    /**
     * 支付
     *
     * @param orderId  订单ID
     * @param amount   金额
     * @param channel  支付方式
     * @param callback
     */
    void pay(String orderId, long amount, String channel, NormalCallback callback);

    /**
     * 查询余额
     *
     * @param callback
     */
    void requestRemain(NormalCallback callback);

    /**
     * 查询账户明细
     *
     * @param startPageIndex
     * @param pageSize
     * @param type     1:充值  2:购买  3:提现  4:中奖   0:全部
     * @param callback
     */
    void requestAccountDetail(int startPageIndex, int pageSize, int type, NormalCallback callback);

    /**
     * 提现
     *
     * @param amount   提现金额  以分为单位
     * @param payType  提现方式  100 支付宝  200 微信
     * @param callback 回调
     */
    void withdraw(long amount, int payType,String payPassword, NormalCallback callback);

    /**
     * 提现记录
     *
     * @param startPageIdx   起始页
     * @param ts  第一次不传 其他时候传返回值
     * @param callback 回调
     */
    void requestWithdrawRecord(long startPageIdx, long ts, NormalCallback callback);
}
