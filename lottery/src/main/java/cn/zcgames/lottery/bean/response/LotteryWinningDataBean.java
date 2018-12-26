package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.bean.LotteryResultHistory;

/**
 * Created by admin on 2017/5/10.
 * "status": "true",
 * "error": "",
 * "msg": "",
 * "data":
 */

public class LotteryWinningDataBean extends ResponseBaseBean {

    private String lottery_name;//彩种
    private int page;
    private int page_size;
    private int total;
    private String ts;
    private int start;
    private int end;
    List<LotteryResultHistory> list;

    public String getLottery_name() {
        return lottery_name;
    }

    public void setLottery_name(String lottery_name) {
        this.lottery_name = lottery_name;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<LotteryResultHistory> getList() {
        return list;
    }

    public void setList(List<LotteryResultHistory> list) {
        this.list = list;
    }
}
