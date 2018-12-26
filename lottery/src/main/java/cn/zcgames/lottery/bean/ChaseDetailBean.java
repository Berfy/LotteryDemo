package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.personal.model.ChaseDetailStatus;

/**
 * Created by admin on 2017/7/14.
 */

public class ChaseDetailBean {
    String cost;
    String start;
    String end;
    String chase;
    String chased;
    String reward_money;
    String created_at;
    String multiple;
    String bets_num;
    String type;
    List<ChaseDetailStatus> list;

    public String getBets_num() {
        return bets_num;
    }

    public void setBets_num(String bets_num) {
        this.bets_num = bets_num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getChase() {
        return chase;
    }

    public void setChase(String chase) {
        this.chase = chase;
    }

    public String getChased() {
        return chased;
    }

    public void setChased(String chased) {
        this.chased = chased;
    }

    public String getReward_money() {
        return reward_money;
    }

    public void setReward_money(String reward_money) {
        this.reward_money = reward_money;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public List<ChaseDetailStatus> getList() {
        return list;
    }

    public void setList(List<ChaseDetailStatus> list) {
        this.list = list;
    }

}
