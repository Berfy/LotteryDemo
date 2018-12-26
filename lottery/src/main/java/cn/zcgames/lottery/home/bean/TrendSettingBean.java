package cn.zcgames.lottery.home.bean;

/**
 * author: Berfy
 * date: 2018/10/17
 * 走势图设置
 */
public class TrendSettingBean {

    private int periodPos = 3;//期次位置
    private boolean isListAsc = true;//顺序显示
    private boolean isShowYilou = true;//显示遗漏
    private boolean isShowStatistic = true;//显示统计
    private boolean isShowLine = true;//显示折线

    public TrendSettingBean() {
        this.periodPos = 3;
        this.isListAsc = true;
        this.isShowYilou = true;
        this.isShowStatistic = true;
        this.isShowLine = true;
    }

    public TrendSettingBean(int periodPos, boolean isListAsc, boolean isShowYilou, boolean isShowStatistic, boolean isShowLine) {
        this.periodPos = periodPos;
        this.isListAsc = isListAsc;
        this.isShowYilou = isShowYilou;
        this.isShowStatistic = isShowStatistic;
        this.isShowLine = isShowLine;
    }

    public int getPeriodPos() {
        return periodPos;
    }

    public void setPeriodPos(int periodPos) {
        this.periodPos = periodPos;
    }

    public boolean isShowYilou() {
        return isShowYilou;
    }

    public void setShowYilou(boolean showYilou) {
        isShowYilou = showYilou;
    }

    public boolean isShowStatistic() {
        return isShowStatistic;
    }

    public void setShowStatistic(boolean showStatistic) {
        isShowStatistic = showStatistic;
    }

    public boolean isShowLine() {
        return isShowLine;
    }

    public void setShowLine(boolean showLine) {
        isShowLine = showLine;
    }

    public boolean isListAsc() {
        return isListAsc;
    }

    public void setListAsc(boolean listAsc) {
        isListAsc = listAsc;
    }

    public boolean equals(TrendSettingBean obj) {
        if (null != obj
                && obj.getPeriodPos() == periodPos
                && obj.isListAsc() == isListAsc
                && obj.isShowLine() == isShowLine
                && obj.isShowYilou() == isShowYilou
                && obj.isShowStatistic() == isShowStatistic) {
            return true;
        }
        return false;
    }
}
