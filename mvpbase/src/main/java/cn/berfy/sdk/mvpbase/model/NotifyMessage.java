package cn.berfy.sdk.mvpbase.model;

import java.io.Serializable;

/**
 * 后台推送来的消息
 *
 * @author NorthStar
 * @date 2018/9/26 15:06
 */
public class NotifyMessage implements Serializable {

    private String from_id;//为空  系统消息
    private int type; //1001系统消息   2001 开奖通知  3001 中奖通知
    private String msg_id;//消息id
    private String ts;//时间戳
    private String alert;//通知内容
    private DetailMessage body;//消息详情

    public NotifyMessage(String msg_id) {
        this.msg_id = msg_id;
        this.type =  NotifyType.TYPE_3;
        this.alert = "恭喜您在快3第181011069期中压中[和值],中奖12元.";
        this.body = new DetailMessage("270358061732859904","1","fast_three");
    }

    public String getFrom_id() {
        return from_id == null ? "" : from_id;
    }

    public int getType() {
        return type;
    }

    public String getMsgId() {
        return msg_id == null ? "" : msg_id;
    }

    public String getTs() {
        return ts == null ? "" : ts;
    }

    public String getAlert() {
        return alert == null ? "" : alert;
    }

    public DetailMessage getBody() {
        return body;
    }

    public NotifyMessage getTest(){
        type = 3001;
        alert = "123";
        return this;
    }
}
