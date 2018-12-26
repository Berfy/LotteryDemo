package cn.berfy.sdk.mvpbase.model;


import java.io.Serializable;

public class MessageBean implements Serializable{
    /**
     * title : 中奖通知
     * body : 恭喜您在eleven_five中, 中奖540元
     * order_id : 265620349461204992
     * lottery_name : eleven_five
     * chase : 1
     * created : 1538132219398
     */

    private String title;
    private String body;
    private String order_id;
    private String lottery_name;
    private String chase;
    private long created;

    public String getTitle() {
        return title;
    }

    public void setTitle(String titleX) {
        this.title = titleX;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrderId() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getLotteryName() {
        return lottery_name;
    }

    public void setLotteryName(String lottery_name) {
        this.lottery_name = lottery_name;
    }

    public String getChase() {
        return chase;
    }

    public void setChase(String chase) {
        this.chase = chase;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}