package cn.zcgames.lottery.bean;

/**
 * Created by admin on 2017/7/14.
 */

public class ChaseBillBean {
    String name;
    int chaseall;
    int chased;
    int cost;
    String status;
    String url;
    String billid;
    String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChaseall() {
        return chaseall;
    }

    public void setChaseall(int chaseall) {
        this.chaseall = chaseall;
    }

    public int getChased() {
        return chased;
    }

    public void setChased(int chased) {
        this.chased = chased;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }
}
