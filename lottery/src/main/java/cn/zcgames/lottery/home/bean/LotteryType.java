package cn.zcgames.lottery.home.bean;

/**
 * 全彩种与用于展示通知设置的彩种共用Entity
 *
 * @author NorthStar
 * @date 2018/9/28 16:44
 */
public class LotteryType {

//    private String name;
//    private String tag;
//    private String url;
//    private String draw_datetime;
//    private boolean isChecked = false;//各彩种是否打开中奖通知

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

//    public String getTag() {
//        return tag;
//    }
//
//    public void setTag(String tag) {
//        this.tag = tag;
//    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setChecked(boolean checked) {
//        isChecked = checked;
//    }
//
//    public String getDraw_datetime() {
//        return draw_datetime;
//    }
//
//    public void setDraw_datetime(String draw_datetime) {
//        this.draw_datetime = draw_datetime;
//    }

    private String show;  //中文名
    private String name;  //彩票类型
    private String staking_countdown;   //倒计时
    private String icon_url;        //图片地址
    private String lottery_state;  //1黑白 暂停销售  2彩色

    private boolean isChecked = false;//各彩种是否打开中奖通知

    public String getShow() {
        return show;
    }


    public String getName() {
        return name;
    }

    public String getDraw_datetime() {
        return staking_countdown;
    }

    public void setStaking_countdown(String staking_countdown) {
        this.staking_countdown = staking_countdown;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public String getLottery_state() {
        return lottery_state;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
