package cn.zcgames.lottery.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.event.NetworkChangedEvent;

import static cn.zcgames.lottery.app.AppConstants.NETWORK_TYPE_MOBILE;
import static cn.zcgames.lottery.app.AppConstants.NETWORK_TYPE_NONE;
import static cn.zcgames.lottery.app.AppConstants.NETWORK_TYPE_WIFI;

/**
 * Created by admin on 2017/4/24.
 */

public class NetworkBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            MyApplication.setNetworkIsOk(true);
            EventBus.getDefault().post(new NetworkChangedEvent(NETWORK_TYPE_MOBILE));
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            // 手机没有任何的网络
            MyApplication.setNetworkIsOk(false);
            EventBus.getDefault().post(new NetworkChangedEvent(NETWORK_TYPE_NONE));
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            MyApplication.setNetworkIsOk(true);
            EventBus.getDefault().post(new NetworkChangedEvent(NETWORK_TYPE_WIFI));
        }

    }
}
