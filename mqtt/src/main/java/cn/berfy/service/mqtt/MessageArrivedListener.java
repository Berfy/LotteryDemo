package cn.berfy.service.mqtt;



import cn.berfy.sdk.mvpbase.model.NotifyMessage;

/**
 * 接受推送消息的回调
 *
 * @author NorthStar
 * @date 2018/9/29 10:57
 */
public interface MessageArrivedListener {
    /**
     * @param topic    订阅话题
     * @param notifyMessage  消息内容
     */
    void itemListener(String topic, NotifyMessage notifyMessage);
}
