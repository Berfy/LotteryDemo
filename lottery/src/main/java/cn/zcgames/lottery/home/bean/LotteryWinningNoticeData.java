package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * @author Berfy
 * 首页公告data
 */
public class LotteryWinningNoticeData {

    private List<String> winners_info;
    private int count;

    public List<String> getWinners_info() {
        return winners_info;
    }

    public void setWinners_info(List<String> winners_info) {
        this.winners_info = winners_info;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
