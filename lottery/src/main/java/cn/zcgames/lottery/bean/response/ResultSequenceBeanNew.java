package cn.zcgames.lottery.bean.response;

/**
 * Created by admin on 2017/6/15.
 */

public class ResultSequenceBeanNew extends ResponseBaseBean {

    private SequenceBean payload;

    public SequenceBean getData() {
        return payload;
    }

    public class SequenceBean {

        private String lottery_name;
        private String staking_period;//期次 又特么改了
        private long staking_countdown;//倒计时
        private int staking_status;//彩种状态
        private String last_period;
        private String draw_time;
        private String style;

        public String getLottery_name() {
            return lottery_name;
        }

        public void setLottery_name(String lottery_name) {
            this.lottery_name = lottery_name;
        }

        public String getCur_period() {
            return staking_period;
        }

        public void setCur_period(String cur_period) {
            this.staking_period = cur_period;
        }

        public long getDraw_time_left() {
            return staking_countdown / 1000;
        }

        public void setDraw_time_left(long draw_time_left) {
            this.staking_countdown = draw_time_left;
        }

        public String getLast_period() {
            return last_period;
        }

        public void setLast_period(String last_period) {
            this.last_period = last_period;
        }

        public String getDraw_time() {
            return draw_time;
        }

        public void setDraw_time(String draw_time) {
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
