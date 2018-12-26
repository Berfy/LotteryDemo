package cn.berfy.sdk.mvpbase.model;

import java.io.Serializable;

/**
 * 消息详情
 * @author NorthStar
 * @date  2018/9/26 15:25
 */
public class DetailMessage implements Serializable{
    private String order_id;//订单ID
    private String chase;//追期期数(跳转界面判断依据  >1 追期详情页 else 订单页)
    private String lottery_name;//彩种名称

    public DetailMessage(String order_id, String chase, String lottery_name) {
        this.order_id = order_id;
        this.chase = chase;
        this.lottery_name = lottery_name;
    }

    public String getOrderId() {
        return order_id == null ? "" : order_id;
    }

    public String getChase() {
        return chase == null ? "" : chase;
    }

    public String getLotteryName() {
        return lottery_name == null ? "" : lottery_name;
    }
}
