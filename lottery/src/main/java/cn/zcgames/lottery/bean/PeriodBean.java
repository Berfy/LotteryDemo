package cn.zcgames.lottery.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 追期数据列表，不追期的投注只有一期的
 * @author NorthStar
 * @date  2018/12/6 14:37
 */
public class PeriodBean {

    /**
     * period : 180910034
     * stakes : [{"numbers":[1,2,3],"Sum":0,"gameplay":"triple_different","status":3}]
     */
    private String period;//期次号
    private String rewards;//奖金
    private long draw_ts;//开奖时间戳
    private List<String> draw_numbers;//开奖号码
    private List<String> win_stakes_index;//这一期中奖投注的下标也就是Stakes数组的下标
    private int status;// 状态，1: lose; 2: win; 3: undraw

    public String getPeriod() {
        return period;
    }

    public long getDrawTs() {
        return draw_ts;
    }

    public List<String> getWinNumbers() {
        if (draw_numbers == null) {
            return new ArrayList<>();
        }
        return draw_numbers;
    }

    public List<String> getWinStakesIndex() {
        if (win_stakes_index == null) {
            return new ArrayList<>();
        }
        return win_stakes_index;
    }

    public String getRewards() {
        return rewards == null ? "" : rewards;
    }

    public int getStatus() {
        return status;
    }
}
