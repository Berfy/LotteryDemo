package cn.zcgames.lottery.bean;

import java.util.List;

/**
 * 投注及追期详情返回数据
 *
 * @author NorthStar
 * @date 2018/9/10 14:28
 */
public class LotteryOrderDetail {

    /**
     * order_id : 259024222771220480
     * period_start : 180910034  追期开始期数
     * period_end : 180910035    追期结束期数
     * cost : 480000             花费  分为单位
     * chase : 2                 共追几期
     * chased : 0                已追期数
     * times : 6                 倍数
     * total_rewards : 0         总奖金
     * stakes_count              注数
     * stake_ts : 1536559075146   下单时间
     * period : [{"period":"180910034","stakes":[{"numbers":[1,2,3],"Sum":0,"gameplay":"triple_different","status":3}]}]
     * status : 1               1未付款  2 付款; 2正在追期 3.追期结束
     * image_url  彩种logo
     */

    private String order_id;
    private String period_start;
    private String period_end;
    private String image_url;
    private String cost;
    private String chase;
    private String chased;
    private String times;//
    private long stake_ts;//下单时间
    private String raw_stakes_str;//订单下注参数
    private String stakes_count;//单期注数
    private List<PeriodBean> periods;// 追期数据列表，不追期的投注只有一期的
    private List<StakesBean> stakes;//下注数据列表
    private String total_rewards;//总奖金
    private int status;// 状态，1未付款  2 付款; 2正在追期 3.追期结束


    public String getOrder_id() {
        return order_id;
    }

    public String getPeriod_start() {
        return period_start == null ? "" : period_start;
    }

    public String getPeriod_end() {
        return period_end == null ? "" : period_end;
    }

    public String getCost() {
        return cost == null ? "" : cost;
    }

    public String getChase() {
        return chase == null ? "" : chase;
    }

    public String getChased() {
        return chased == null ? "" : chased;
    }

    public String getTimes() {
        return times == null ? "" : times;
    }

    public long getCreated() {
        return stake_ts;
    }

    public List<PeriodBean> getPeriod() {
        return periods;
    }

    public List<StakesBean> getStakes() {
        return stakes;
    }

    public String getStakesCount() {
        return stakes_count == null ? "" : stakes_count;
    }

    public String getImageUrl() {
        return image_url == null ? "" : image_url;
    }

    public String getRaw_stakes_str() {
        return raw_stakes_str == null ? "" : raw_stakes_str;
    }

    public String getTotal_rewards() {
        return total_rewards == null ? "" : total_rewards;
    }


    public int getStatus() {
        return status;
    }
}
