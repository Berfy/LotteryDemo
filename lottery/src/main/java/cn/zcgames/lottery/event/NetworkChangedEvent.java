package cn.zcgames.lottery.event;

/**
 * Created by admin on 2017/4/24.
 */

public class NetworkChangedEvent {

    int networkType;

    public NetworkChangedEvent(int type) {
        this.networkType = type;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }
}
