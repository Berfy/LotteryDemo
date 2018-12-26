package cn.berfy.sdk.mvpbase.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 消息详情
 * @author NorthStar
 * @date  2018/9/26 15:25
 */
public class MessageInfo {
    /**
     * page : 1
     * page_size : 20
     * total : 4
     * ts : 1538132273699
     * list : [{"title":"中奖通知","body":"恭喜您在eleven_five中, 中奖540元","order_id":"265620349461204992","lottery_name":"eleven_five","chase":1,"created":1538132219398},{"title":"中奖通知","body":"恭喜您在luck_eleven_five中, 中奖18元","order_id":"265617173035094016","lottery_name":"luck_eleven_five","chase":1,"created":1538131837759},{"title":"中奖通知","body":"恭喜您在fast_three中, 中奖9元","order_id":"265614114976436224","lottery_name":"fast_three","chase":1,"created":1538130772949},{"title":"中奖通知","body":"恭喜您在fast_three中, 中奖9元","order_id":"265612277779009536","lottery_name":"fast_three","chase":1,"created":1538130165699}]
     */

    private int page;
    private int page_size;
    private int total;
    private long ts;
    private List<MessageBean> list;

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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public List<MessageBean> getList() {
        return list;
    }

    public void setList(List<MessageBean> list) {
        this.list = list;
    }
}
