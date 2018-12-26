package cn.zcgames.lottery.home.view.iview;

import java.util.List;

import cn.zcgames.lottery.base.IBaseView;
import cn.zcgames.lottery.bean.LotteryOrder;

/**
 * Created by admin on 2017/4/28.
 */

public interface ILotteryOrderActivity {

    /**
     * 创建订单结果
     *
     * @param isOk   是否成功
     * @param msgStr 提示信息
     */
    void createOrderResult(boolean isOk, boolean isNeedLogin, Object msgStr);

    /**
     * 创建本地投注单结果
     *
     * @param isOk   是否成功
     * @param msgStr 提示信息
     */
    void createLocalOrderResult(String lotteryType, int playType, boolean isOk, String msgStr);

    /**
     * 机选结果
     *
     * @param order
     */
    void machineAddResult(LotteryOrder order, boolean isOk);

    /**
     * 删除结果
     *
     * @param isOk
     * @param errorStr
     */
    void deleteResult(boolean isOk, Object errorStr);

    /**
     * @param msgStr
     */
    void showWaitingDialog(String msgStr);

    /**
     *
     */
    void hiddenWaitingDialog();

    /**
     * 初始化订单数据
     */
    void initLotteryOrderListResult(List<LotteryOrder> mOrders);

    /**
     * 获取期次
     */
    void onRequestSequence(boolean b, Object msgStr);
}
