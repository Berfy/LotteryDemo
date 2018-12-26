package cn.zcgames.lottery.home.listener;

import cn.zcgames.lottery.bean.LotteryOrder;

/**
 * 双色球投注订单列表的长按事件的监听器
 * Created by admin on 2017/4/12.
 */

public interface OnLotteryOrderItemLongClickListener {
    void onLongClickListener(LotteryOrder order);
}
