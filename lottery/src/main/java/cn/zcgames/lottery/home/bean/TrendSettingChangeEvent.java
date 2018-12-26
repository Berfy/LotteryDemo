package cn.zcgames.lottery.home.bean;

/**
 * author: Berfy
 * date: 2018/10/17
 * 走势图设置更改Event
 */
public class TrendSettingChangeEvent {

    private boolean needRefresh;
    private TrendSettingBean trendSettingBean;

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public TrendSettingBean getTrendSettingBean() {
        return trendSettingBean;
    }

    public void setTrendSettingBean(TrendSettingBean trendSettingBean) {
        this.trendSettingBean = trendSettingBean;
    }
}
