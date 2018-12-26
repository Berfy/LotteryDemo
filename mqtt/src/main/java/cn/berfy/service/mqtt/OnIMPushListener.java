package cn.berfy.service.mqtt;


import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Berfy on 2018/1/16.
 * 腾讯云推送监听
 */

public interface OnIMPushListener {
    void onNewPushMessage(MqttMessage message);
}
