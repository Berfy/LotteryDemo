package cn.zcgames.lottery.bean.response;

/**
 * Created by admin on 2017/6/15.
 */

public class ResultSequenceBean extends ResponseBaseBean {

    private SequenceBean payload;
    private SequenceBean data;

    public SequenceBean getData() {
        return null == payload ? data : payload;
    }

    public class SequenceBean {
        String deadline;
        String lottery_time;
        String sequence;
        long until_end;

        public long getUntil_end() {
            return until_end;
        }

        public void setUntil_end(long until_end) {
            this.until_end = until_end;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getLottery_time() {
            return lottery_time;
        }

        public void setLottery_time(String lottery_time) {
            this.lottery_time = lottery_time;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }
    }
}
