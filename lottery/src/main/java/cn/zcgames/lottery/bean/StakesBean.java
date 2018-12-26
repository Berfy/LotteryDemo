package cn.zcgames.lottery.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 下注数据列表
 * @author NorthStar
 * @date  2018/12/6 14:36
 */
public class StakesBean {
    /**
     * numbers : [1,2,3]            :开奖号码
     * Sum : 0                      :和值
     * gameplay : triple_different  :赌注类型
     */

    private int sum;
    private String gameplay;
    private List<String> numbers;
    private int status;// 状态，1: lose; 2: win; 3: undraw

    public int getSum() {
        return sum;
    }

    public String getGamePlay() {
        return gameplay == null ? "" : gameplay;
    }


    public List<String> getNumbers() {
        if (numbers == null) {
            return new ArrayList<>();
        }
        return numbers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
