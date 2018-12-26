package cn.zcgames.lottery.bean.response;

import java.util.ArrayList;
import java.util.List;

/**
 * 投注记录列表
 *
 * @author NorthStar
 * @date 2018/9/11 10:14
 */
public class OrderRecordInfo {

    /**
     * page : 1
     * page_size : 20
     * total : 20
     * ts : 1536630992192
     * status : 1
     * list : [{"lottery_name":"fast_three","period":"180910034","gameplay":"triple_different","red":[4,5,6],
     * "blue":[],"Sum":0,"red_color":"#cc0033","blue_coloe":"#6633ff","style":"dice","status":1}]
     */

    private int page;
    private int page_size;
    private int total;
    private long ts;
    private int status;
    private List<OrderList> list;

    public int getPage() {
        return page;
    }

    public int getPage_size() {
        return page_size;
    }

    public int getTotal() {
        return total;
    }

    public long getTs() {
        return ts;
    }

    public int getStatus() {
        return status;
    }

    public List<OrderList> getOrderList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public static class OrderList {
        /**
         * lottery_name : fast_three
         * period : 180910034
         * gameplay : triple_different
         * red : [4,5,6]
         * blue : []
         * Sum : 0
         * red_color : #cc0033
         * blue_coloe : #6633ff
         * style : dice
         * win_lose : 1
         */

        private String lottery_name;
        private String period;
        private String cost;
        private long creatd;
        private String gameplay;
        private int Sum;
        private String red_color;
        private String blue_coloe;
        private String style;
        private int status;//2: 未开奖； 3: 已开奖
        private List<String> red;
        private List<String> blue;
        private String order_id;

        public String getName() {
            return lottery_name == null ? "" : lottery_name;
        }

        public String getPeriod() {
            return period == null ? "" : period;
        }

        public String getGameplay() {
            return gameplay == null ? "" : gameplay;
        }

        public int getSum() {
            return Sum;
        }

        public String getRed_color() {
            return red_color == null ? "" : red_color;
        }

        public String getBlue_coloe() {
            return blue_coloe == null ? "" : blue_coloe;
        }

        public String getStyle() {
            return style == null ? "" : style;
        }

        public int getStatus() {
            return status;
        }

        public String getAmount() {
            return cost == null ? "" : cost;
        }

        public long getCreatd() {
            return creatd;
        }

        public List<String> getRed() {
            if (red == null) {
                return new ArrayList<>();
            }
            return red;
        }

        public List<String> getBlue() {
            if (blue == null) {
                return new ArrayList<>();
            }
            return blue;
        }

        public String getOrderId() {
            return order_id;
        }
    }
}
