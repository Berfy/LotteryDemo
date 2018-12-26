package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * @author Berfy
 * 新的历史开奖列表
 * 2018.9.10
 */
public class ResultHistoryListData {

    private int page;
    private int page_size;
    private int total;
    private long ts;
    private List<HistoryList> list;

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

    public List<HistoryList> getList() {
        return list;
    }

    public void setList(List<HistoryList> list) {
        this.list = list;
    }

    public static class HistoryList {
        private String period;//期次
        private List<String> numbers;//中奖号码
        private String sum;//和值
        private String big_small;//大小
        private String odd_even;//单双
        private long draw_time;//开奖时间
        private String style;//dice 快三 ball 其他球

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public List<String> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<String> numbers) {
            this.numbers = numbers;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getBig_small() {
            return big_small;
        }

        public void setBig_small(String big_small) {
            this.big_small = big_small;
        }

        public String getOdd_even() {
            return odd_even;
        }

        public void setOdd_even(String odd_even) {
            this.odd_even = odd_even;
        }

        public long getDraw_time() {
            return draw_time;
        }

        public void setDraw_time(long draw_time) {
            this.draw_time = draw_time;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }
    }
}
