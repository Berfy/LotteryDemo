package cn.zcgames.lottery.home.bean;

import java.io.Serializable;
import java.util.List;

public class FastThreeMissBean implements Serializable{


    /**
     * missNumber : {"erbutong":[14,9,9,8,8,1,3,5,4,7,13,7,5,4,8],"ertong":[15,3,1,2,0,10],"shuzi":[8,1,1,2,0,4],"sum":[407,37,15,32,27,1,13,21,5,2,7,6,0,44,59,144],"xingtai":[0,4,30,0,1,1]}
     * period : 20181013011
     * winnerNumber : [5,5,5]
     */

    private MissNumberBean missNumber;
    private String period;
    private List<Integer> winnerNumber;

    public MissNumberBean getMissNumber() {
        return missNumber;
    }

    public void setMissNumber(MissNumberBean missNumber) {
        this.missNumber = missNumber;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<Integer> getWinnerNumber() {
        return winnerNumber;
    }

    public void setWinnerNumber(List<Integer> winnerNumber) {
        this.winnerNumber = winnerNumber;
    }

    public static class MissNumberBean {
        private List<Integer> erbutong;
        private List<Integer> ertong;
        private List<Integer> shuzi;
        private List<Integer> sum;
        private List<Integer> xingtai;

        public List<Integer> getErbutong() {
            return erbutong;
        }

        public void setErbutong(List<Integer> erbutong) {
            this.erbutong = erbutong;
        }

        public List<Integer> getErtong() {
            return ertong;
        }

        public void setErtong(List<Integer> ertong) {
            this.ertong = ertong;
        }

        public List<Integer> getShuzi() {
            return shuzi;
        }

        public void setShuzi(List<Integer> shuzi) {
            this.shuzi = shuzi;
        }

        public List<Integer> getSum() {
            return sum;
        }

        public void setSum(List<Integer> sum) {
            this.sum = sum;
        }

        public List<Integer> getXingtai() {
            return xingtai;
        }

        public void setXingtai(List<Integer> xingtai) {
            this.xingtai = xingtai;
        }
    }
}
