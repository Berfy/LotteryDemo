package cn.zcgames.lottery.bean.response;

import java.util.List;

/**
 * 开奖结果列表接口返回  payload Bean
 *
 * @author Berfy
 */
public class ResultPageDataBean {

    private String name;
    private String lott_inner_name;
    private long draw_datetime;
    private String period;
    private String style;
    private String more;
    private List<ResultBallBean> draw_numbers;

    public String getName() {
        return name;
    }

    public String getType() {
        return style;
    }

    public void setType(String type) {
        this.style = type;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDatetime() {
        return draw_datetime;
    }

    public void setDatetime(long datetime) {
        this.draw_datetime = datetime;
    }

    public String getLott_inner_name() {
        return lott_inner_name;
    }

    public void setLott_inner_name(String lott_inner_name) {
        this.lott_inner_name = lott_inner_name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<ResultBallBean> getBall() {
        return draw_numbers;
    }

    public void setBall(List<ResultBallBean> ball) {
        this.draw_numbers = ball;
    }
}
