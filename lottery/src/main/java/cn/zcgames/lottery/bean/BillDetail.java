package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.bean.response.ResponseBaseBean;

/**
 * Created by admin on 2017/6/5.
 * 投注订单详情结果Bean的父类
 * Berfy修改 2018.8.31
 */
public class BillDetail extends ResponseBaseBean {
    String bill_status;
    String lottery_status;
    String reward_money;
    String bill_id;
    WinningBean winning;
    String multiple;
    String chase;
    String cost;
    String created_at;
    String lottery_time;
    String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBill_status() {
        return bill_status;
    }

    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }

    public String getLottery_status() {
        return lottery_status;
    }

    public void setLottery_status(String lottery_status) {
        this.lottery_status = lottery_status;
    }

    public String getReward_money() {
        return reward_money;
    }

    public void setReward_money(String reward_money) {
        this.reward_money = reward_money;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public WinningBean getWinning() {
        return winning;
    }

    public void setWinning(WinningBean winning) {
        this.winning = winning;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getChase() {
        return chase;
    }

    public void setChase(String chase) {
        this.chase = chase;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLottery_time() {
        return lottery_time;
    }

    public void setLottery_time(String lottery_time) {
        this.lottery_time = lottery_time;
    }

    public class WinningBean {
        List<String> red;
        List<String> blue;

        public List<String> getRed() {
            return red;
        }

        public void setRed(List<String> red) {
            this.red = red;
        }

        public List<String> getBlue() {
            return blue;
        }

        public void setBlue(List<String> blue) {
            this.blue = blue;
        }
    }

}
