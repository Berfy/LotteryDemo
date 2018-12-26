package cn.zcgames.lottery.bean.response;

import java.util.List;

/**
 * Created by admin on 2017/5/10.
 */

public class DoubleColorDetailBean extends ResponseBaseBean {

    DoubleColorDetail payload;

    public DoubleColorDetail getData() {
        return payload;
    }

    public void setData(DoubleColorDetail data) {
        this.payload = data;
    }

    public class DoubleColorDetail {
        String sequence;
        String total_selling;
        String prize_pool;
        String datetime;
        List<Prize> prizes;
        List<Prize> info;
        String sale_volume;

        public List<Prize> getInfo() {
            return info;
        }

        public void setInfo(List<Prize> info) {
            this.info = info;
        }

        public String getSale_volume() {
            return sale_volume;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public String getTotal_selling() {
            return total_selling;
        }

        public String getPrize_pool() {
            return prize_pool;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public List<Prize> getPrizes() {
            return prizes;
        }

        public void setPrizes(List<Prize> prizes) {
            this.prizes = prizes;
        }
    }

    public static class Prize {
        String level;
        String winning_bet;
        String winning_prize;

        String name;
        String num;
        String price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getWinning_bet() {
            return winning_bet;
        }

        public void setWinning_bet(String winning_bet) {
            this.winning_bet = winning_bet;
        }

        public String getWinning_prize() {
            return winning_prize;
        }

        public void setWinning_prize(String winning_prize) {
            this.winning_prize = winning_prize;
        }
    }
}
