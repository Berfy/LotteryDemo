package cn.zcgames.lottery.home.bean;

/**
 * author: Berfy
 * date: 2018/10/17
 * 走势图刷新Event
 */
public class TrendRefreshEvent {

    private boolean needRefresh;

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public TrendRefreshEvent(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }
}
