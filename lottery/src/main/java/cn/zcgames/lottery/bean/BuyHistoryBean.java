package cn.zcgames.lottery.bean;

import java.util.List;

import cn.zcgames.lottery.home.bean.DoubleColorOrderBet;

/**
 * "name": "双色球",
 * "created_at": "2017-06-06T14:16:30.313+08:00",
 * "type": "ball",
 * "id": "593648be297f9475ff89fc08",
 * "sequence": "2017065",
 * "cost": 2,
 * "status": "未开奖"
 */
public class BuyHistoryBean {

    String name;
    String sequence;
    String type;
    long cost;
    String created_at;
    String id;//order id
    String status;

    List<DoubleColorOrderBet> bets;
    int chase;
    int multiple;

    public int getChase() {
        return chase;
    }

    public void setChase(int chase) {
        this.chase = chase;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public List<DoubleColorOrderBet> getBets() {
        return bets;
    }

    public void setBets(List<DoubleColorOrderBet> bets) {
        this.bets = bets;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
