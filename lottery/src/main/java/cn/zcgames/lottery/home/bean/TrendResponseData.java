package cn.zcgames.lottery.home.bean;

import java.io.Serializable;
import java.util.List;

import cn.zcgames.lottery.utils.okhttp.utils.L;

/**
 * author: Berfy
 * date: 2018/10/15
 * 走势图接口
 */
public class TrendResponseData implements Serializable {

    private List<TrendData> data;

    public List<TrendData> getData() {
        return data;
    }

    public void setData(List<TrendData> data) {
        this.data = data;
    }

    public static class TrendData implements Serializable {
        private String period;
        private List<String> winnerNumber;
        private MissNumber missNumber;
        private OverNumber overNumber;

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public List<String> getWinnerNumber() {
            return winnerNumber;
        }

        public void setWinnerNumber(List<String> winnerNumber) {
            this.winnerNumber = winnerNumber;
        }

        public MissNumber getMissNumber() {
            return missNumber;
        }

        public void setMissNumber(MissNumber missNumber) {
            this.missNumber = missNumber;
        }

        public OverNumber getOverNumber() {
            return overNumber;
        }

        public void setOverNumber(OverNumber overNumber) {
            this.overNumber = overNumber;
        }
    }

    //遗留号码
    public static class MissNumber implements Serializable {
        //快三
        private List<String> erbutong;//二不同
        private List<String> ertong;//二同
        private List<String> shuzi;//基本走势
        private List<String> sum;//和值
        private List<String> xingtai;//形态  数组中  三同号 1   三不同  2    三连号3       二同号 4        二同号单选5           二不同6   （3和5不需要展示了）

        //11选5
        private List<String> qian1_danshi;//前一
        private List<String> qian1_danshi_xingtai;//前一形态
        private List<String> qian2_zhixuan;//前二直选
        private List<String> qian2_zuxuan;//前二组选
        private List<String> qian3_zhixuan;//前三直选
        private List<String> qian3_zuxuan;//前三组选
        private List<String> renxuan_general;//任选

        //时时彩
        private List<String> daxiaodanshuang;//大小单双
        private List<String> erxing_zuxuan;//二星组选
        private List<String> erxing_zuxuan_span;//二星组选 跨度
        private List<String> sanxing_zuliu;//三星组六
        private List<String> sanxing_zusan;//三星组三
        private List<String> sanxing_zuxuan_span;// 三星组选(三\六)跨度
        private List<String> wuxing_tongxuan;//五星直(通)选(万位-个位依次排列)   一星、二星直选的个位走势 十位走势也是这个
        private List<String> yixing_zhixuan_swing;//一星直选 个位振幅   振幅数字需要跟上一起  相减取绝对值

        public List<String> getErbutong() {
            return erbutong;
        }

        public void setErbutong(List<String> erbutong) {
            this.erbutong = erbutong;
        }

        public List<String> getErtong() {
            return ertong;
        }

        public void setErtong(List<String> ertong) {
            this.ertong = ertong;
        }

        public List<String> getShuzi() {
            return shuzi;
        }

        public void setShuzi(List<String> shuzi) {
            this.shuzi = shuzi;
        }

        public List<String> getSum() {
            return sum;
        }

        public void setSum(List<String> sum) {
            this.sum = sum;
        }

        public List<String> getXingtai() {
            return xingtai;
        }

        public void setXingtai(List<String> xingtai) {
            this.xingtai = xingtai;
        }

        public List<String> getQian1_danshi() {
            return qian1_danshi;
        }

        public void setQian1_danshi(List<String> qian1_danshi) {
            this.qian1_danshi = qian1_danshi;
        }

        public List<String> getQian1_danshi_xingtai() {
            return qian1_danshi_xingtai;
        }

        public void setQian1_danshi_xingtai(List<String> qian1_danshi_xingtai) {
            this.qian1_danshi_xingtai = qian1_danshi_xingtai;
        }

        public List<String> getQian2_zhixuan() {
            return qian2_zhixuan;
        }

        public void setQian2_zhixuan(List<String> qian2_zhixuan) {
            this.qian2_zhixuan = qian2_zhixuan;
        }

        public List<String> getQian2_zuxuan() {
            return qian2_zuxuan;
        }

        public void setQian2_zuxuan(List<String> qian2_zuxuan) {
            this.qian2_zuxuan = qian2_zuxuan;
        }

        public List<String> getQian3_zhixuan() {
            return qian3_zhixuan;
        }

        public void setQian3_zhixuan(List<String> qian3_zhixuan) {
            this.qian3_zhixuan = qian3_zhixuan;
        }

        public List<String> getQian3_zuxuan() {
            return qian3_zuxuan;
        }

        public void setQian3_zuxuan(List<String> qian3_zuxuan) {
            this.qian3_zuxuan = qian3_zuxuan;
        }

        public List<String> getRenxuan_general() {
            return renxuan_general;
        }

        public void setRenxuan_general(List<String> renxuan_general) {
            this.renxuan_general = renxuan_general;
        }

        public List<String> getDaxiaodanshuang() {
            return daxiaodanshuang;
        }

        public void setDaxiaodanshuang(List<String> daxiaodanshuang) {
            this.daxiaodanshuang = daxiaodanshuang;
        }

        public List<String> getErxing_zuxuan() {
            return erxing_zuxuan;
        }

        public void setErxing_zuxuan(List<String> erxing_zuxuan) {
            this.erxing_zuxuan = erxing_zuxuan;
        }

        public List<String> getErxing_zuxuan_span() {
            return erxing_zuxuan_span;
        }

        public void setErxing_zuxuan_span(List<String> erxing_zuxuan_span) {
            this.erxing_zuxuan_span = erxing_zuxuan_span;
        }

        public List<String> getSanxing_zuliu() {
            return sanxing_zuliu;
        }

        public void setSanxing_zuliu(List<String> sanxing_zuliu) {
            this.sanxing_zuliu = sanxing_zuliu;
        }

        public List<String> getSanxing_zusan() {
            return sanxing_zusan;
        }

        public void setSanxing_zusan(List<String> sanxing_zusan) {
            this.sanxing_zusan = sanxing_zusan;
        }

        public List<String> getSanxing_zuxuan_span() {
            return sanxing_zuxuan_span;
        }

        public void setSanxing_zuxuan_span(List<String> sanxing_zuxuan_span) {
            this.sanxing_zuxuan_span = sanxing_zuxuan_span;
        }

        public List<String> getWuxing_tongxuan() {
            return wuxing_tongxuan;
        }

        public void setWuxing_tongxuan(List<String> wuxing_tongxuan) {
            this.wuxing_tongxuan = wuxing_tongxuan;
        }

        public List<String> getYixing_zhixuan_swing() {
            return yixing_zhixuan_swing;
        }

        public void setYixing_zhixuan_swing(List<String> yixing_zhixuan_swing) {
            this.yixing_zhixuan_swing = yixing_zhixuan_swing;
        }
    }

    //重号(11选5)
    public static class OverNumber implements Serializable {
        private List<String> qian1_danshi;
        private List<String> qian2_zuxuan;
        private List<String> qian3_zuxuan;
        private List<String> renxuan_general;

        public List<String> getQian1_danshi() {
            return qian1_danshi;
        }

        public List<String> getQian2_zuxuan() {
            return qian2_zuxuan;
        }

        public List<String> getQian3_zuxuan() {
            return qian3_zuxuan;
        }

        public List<String> getRenxuan_general() {
            return renxuan_general;
        }
    }
}
