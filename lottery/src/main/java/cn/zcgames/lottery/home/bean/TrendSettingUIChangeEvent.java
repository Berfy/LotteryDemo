package cn.zcgames.lottery.home.bean;

/**
 * author: Berfy
 * date: 2018/10/17
 * 走势图设置UI配置Event
 */
public class TrendSettingUIChangeEvent {

    private boolean needShowListAsc;//需要显示顺序
    private boolean needShowShowYilou;//需要显示遗漏
    private boolean needShowShowStatistic;//需要显示统计
    private boolean needShowShowLine;//需要显示折线

    public boolean isNeedShowListAsc() {
        return needShowListAsc;
    }

    public void setNeedShowListAsc(boolean needShowListAsc) {
        this.needShowListAsc = needShowListAsc;
    }

    public boolean isNeedShowShowYilou() {
        return needShowShowYilou;
    }

    public void setNeedShowShowYilou(boolean needShowShowYilou) {
        this.needShowShowYilou = needShowShowYilou;
    }

    public boolean isNeedShowShowStatistic() {
        return needShowShowStatistic;
    }

    public void setNeedShowShowStatistic(boolean needShowShowStatistic) {
        this.needShowShowStatistic = needShowShowStatistic;
    }

    public boolean isNeedShowShowLine() {
        return needShowShowLine;
    }

    public void setNeedShowShowLine(boolean needShowShowLine) {
        this.needShowShowLine = needShowShowLine;
    }
}
