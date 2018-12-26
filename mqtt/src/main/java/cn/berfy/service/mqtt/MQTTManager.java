package cn.berfy.service.mqtt;

import android.content.Context;
import android.text.TextUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.model.NotifyMessage;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;

/**
 * author: Berfy
 * data: 2018/9/15
 */
public class MQTTManager {

    private final String TAG = "MQTT连接管理";
    private static MQTTManager mMQTTManager;
    private Context mContext;
    private MqttAndroidClient mClient;
    private MqttConnectOptions mMQTTConnectOptions;
    private boolean mIsConnected;//是否连接成功
    private String mHost;//主机ip
    private String mClientId;//客户端标识码,可以自定义，必须保证唯一性，否则连接服务器的时候会导致服务器断开，
    private String mUserName;//登录名
    private String mPassWord;//登录密码
    private List<String> mTopic = new ArrayList<>();//话题集合
    private MessageArrivedListener mMessageListener;

    public static void init(Context context) {
        if (null == mMQTTManager) {
            mMQTTManager = new MQTTManager(context);
        }
    }

    private MQTTManager(Context context) {
        mContext = context;
    }


    public static MQTTManager getInstance() {
        if (null == mMQTTManager) {
            throw new NullPointerException("没有在Applicaion中初始化(init)MQTTManager");
        }
        return mMQTTManager;
    }

    public MQTTManager setHost(String serverIp) {
        mHost = serverIp;
        return mMQTTManager;
    }

    public MQTTManager setClientId(String clientId) {
        mClientId = clientId;
        return mMQTTManager;
    }

    public MQTTManager setUserName(String userName) {
        mUserName = userName;
        return mMQTTManager;
    }

    public MQTTManager setPassWord(String password) {
        mPassWord = password;
        return mMQTTManager;
    }

    public MQTTManager setTopic(List<String> topic) {
        mTopic = topic;
        return mMQTTManager;
    }

    public void onMessageArrivedListener(MessageArrivedListener listener) {
        mMessageListener = listener;
    }

    public void connect(boolean isNeedNewConnection) {
        mIsConnected = false;
        if (TextUtils.isEmpty(mHost)) {
            LogF.d(TAG, "主机名为空");
            return;
        }
        if (TextUtils.isEmpty(mClientId)) {
            LogF.d(TAG, "clientId为空");
            return;
        }
        if (TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassWord)) {
            LogF.d(TAG, "用户名密码为空");
        }
        if (isNeedNewConnection) {
            LogF.d(TAG, "新连接");
            destroy(); //断开之前的连接
            mClient = new MqttAndroidClient(mContext, mHost, mClientId);
            mClient.setCallback(mCallback);
            mMQTTConnectOptions = new MqttConnectOptions();
            mMQTTConnectOptions.setAutomaticReconnect(true);//自动重连
            mMQTTConnectOptions.setCleanSession(false);
            if (!TextUtils.isEmpty(mUserName))
                mMQTTConnectOptions.setUserName(mUserName);
            if (!TextUtils.isEmpty(mPassWord))
                mMQTTConnectOptions.setPassword(mPassWord.toCharArray());
        } else {
            LogF.d(TAG, "连接已存在");
            if (null == mClient || null == mMQTTConnectOptions) {//实例被销毁，递归
                connect(true);
            }
        }
        if (null != mClient && mClient.isConnected()) {
            LogF.d(TAG, "已连接，不用再次连接");
            mIsConnected = true;
            return;
        }
        LogF.d(TAG, "连接参数  host=" + mHost + " clienId=" + mClientId +
                " username=" + mUserName + " password=" + mPassWord + " topic=" + mTopic);
        //直接连接
        try {
            mClient.connect(mMQTTConnectOptions, null, mListener);
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    private MqttCallbackExtended mCallback = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            mIsConnected = true;
            if (reconnect) {
                if (mTopic != null && mTopic.size() > 0) {
                    LogF.d(TAG, "Callback_mTopicSize==>" + mTopic.size());
                    for (int i = 0; i < mTopic.size(); i++) {
                        LogF.d(TAG, "Callback_mTopic==>" + mTopic.get(i));
                        subscribeToTopic(mTopic.get(i));
                    }
                }
                LogF.d(TAG, "重连连接完毕");
            } else {
                LogF.d(TAG, "连接完毕");
            }
        }

        @Override
        public void connectionLost(Throwable exception) {
            mIsConnected = false;
            if (null == exception) {
                LogF.d(TAG, "连接丢失");
                return;
            }
            LogF.d(TAG, "连接丢失  message=" + exception.getMessage()
                    + "   cause=" + exception.getCause()
                    + "   lLocalizedMessage=" + exception.getLocalizedMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {//接收到后台推送推送来的消息
            LogF.d(TAG, "最终收到推送");
            if (null == message) {
                LogF.d(TAG, "推送解析失败null == pushMessage");
                return;
            }
            String json = new String(message.getPayload());

            if (TextUtils.isEmpty(json)) {
                LogF.d(TAG, "推送解析失败null == msg");
                return;
            }
            LogF.d(TAG, "最终收到推送消息 Incoming message: 来自话题:" + topic + ", msgStr==>" + json);
            NotifyMessage notifyMessage = GsonUtil.getInstance().toClass(json, NotifyMessage.class);
            if (null == notifyMessage) {
                LogF.d(TAG, "推送解析失败null == customNotifyMessage");
                return;
            }
            LogF.d(TAG, "收到推送customNotifyMessage" + GsonUtil.getInstance().toJson(notifyMessage));
            //处理推送的消息
            mMessageListener.itemListener(topic, notifyMessage);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            try {
                LogF.d(TAG, "发送完毕 " + new String(token.getMessage().getPayload()));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    };

    private IMqttActionListener mListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            mIsConnected = true;
            LogF.d(TAG, "连接成功");
            DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
            disconnectedBufferOptions.setBufferEnabled(true);
            disconnectedBufferOptions.setBufferSize(100);
            disconnectedBufferOptions.setPersistBuffer(false);
            disconnectedBufferOptions.setDeleteOldestMessages(false);
            mClient.setBufferOpts(disconnectedBufferOptions);
            if (mTopic != null && mTopic.size() > 0) {
                LogF.d(TAG, "ActionListener_mTopicSize==>" + mTopic.size());
                for (int i = 0; i < mTopic.size(); i++) {
                    LogF.d(TAG, "ActionListener_mTopic==>" + mTopic.get(i));
                    subscribeToTopic(mTopic.get(i));
                }

            }
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            mIsConnected = false;
            if (null == exception) {
                LogF.d(TAG, "连接失败");
                return;
            }
            LogF.d(TAG, "连接失败 Failed to connect to: " + asyncActionToken
                    + "   message=" + exception.getMessage()
                    + "   cause=" + exception.getCause()
                    + "   lLocalizedMessage=" + exception.getLocalizedMessage());
        }
    };

    //取消订阅
    public void unsubscribeToTopic(List<String> topics) {
        for (int i = 0; i < topics.size(); i++) {
            unSubscribeTopic(topics.get(i));
            if (i == topics.size() - 1) {
                destroy();
            }
        }
    }

    public void unSubscribeTopic(final String topic) {
        try {
            mClient.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogF.d(TAG, "注销订阅成功 topic==>" + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    String message = "";
                    if (exception != null) {
                        message = exception.getMessage();
                    }
                    LogF.d(TAG, "注销订阅失败 " + asyncActionToken + "   " + message);
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void subscribeToTopic(final String topic) {
        try {
            mClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogF.d(TAG, "订阅话题成功 topic:" + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    String message = "";
                    if (exception != null) {
                        message = exception.getMessage();
                    }
                    LogF.d(TAG, "订阅话题失败 " + asyncActionToken + "   " + message);
                }
            });


        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        LogF.d(TAG, "发送消息: topic:" + mTopic + "  消息:" + message);
        if (null == mClient) {
            LogF.d(TAG, "发送消息: mClient==null");
            return;
        }
        if (!mClient.isConnected()) {
            LogF.d(TAG, "发送消息: 未连接重新连接");
            connect(false);
            return;
        }
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            mqttMessage.setQos(2);
            mClient.publish("", mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
            LogF.d(TAG, "发送消息失败");
            System.err.println("Error Publishing: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return null != mClient && mClient.isConnected();
    }

    public void destroy() {
        if (null == mClient) return;
        try {
            mClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试数据
    public void setTestPush(NotifyMessage notifyMessage) {
        //处理推送的消息
        mMessageListener.itemListener("666", notifyMessage);
    }
}
