package cn.zcgames.lottery.home.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.zcgames.lottery.bean.BasicTrendBean;

/**
 * 各类型走势图相关数据
 *
 * @author NorthStar
 * @date 2018/10/18 16:29
 */
public class TrendTypeData implements Serializable {

    private List<OpenAwardBean> awardDatas;//开奖走势图数据(所有彩种通用)
    private List<BasicTrendBean> basicDatas;//基本走势(所有彩种通用)
    private List<OpenAwardBean> formDatas;//11选5 (任选、前二组选、前三组选)形态
    private HashMap<String, List<TrendData>> trendDatas;//内容数据
    private HashMap<String, List<String>> trendLeftTitles;//左侧增加标题列
    private HashMap<String, List<String>> trendTitles;//内容标题

    public List<OpenAwardBean> getAwardDatas() {
        if (awardDatas == null) {
            return new ArrayList<>();
        }
        return awardDatas;
    }

    public void setAwardDatas(List<OpenAwardBean> awardDatas) {
        this.awardDatas = awardDatas;
    }

    public List<BasicTrendBean> getBasicDatas() {
        if (basicDatas == null) {
            return new ArrayList<>();
        }
        return basicDatas;
    }

    public void setBasicDatas(List<BasicTrendBean> basicDatas) {
        this.basicDatas = basicDatas;
    }

    public List<OpenAwardBean> getFormDatas() {
        if (formDatas == null) {
            return new ArrayList<>();
        }
        return formDatas;
    }

    public void setFormDatas(List<OpenAwardBean> formDatas) {
        this.formDatas = formDatas;
    }

    public HashMap<String, List<TrendData>> getTrendDatas() {
        if(null == trendDatas){
            trendDatas = new HashMap<>();
        }
        return trendDatas;
    }

    public void setTrendDatas(HashMap<String, List<TrendData>> trendDatas) {
        this.trendDatas = trendDatas;
    }

    public HashMap<String, List<String>> getTrendLeftTitles() {
        if(null == trendLeftTitles){
            trendLeftTitles = new HashMap<>();
        }
        return trendLeftTitles;
    }

    public void setTrendLeftTitles(HashMap<String, List<String>> trendLeftTitles) {
        this.trendLeftTitles = trendLeftTitles;
    }

    public HashMap<String, List<String>> getTrendTitles() {
        if(null == trendTitles){
            trendTitles = new HashMap<>();
        }
        return trendTitles;
    }

    public void setTrendTitles(HashMap<String, List<String>> trendTitles) {
        this.trendTitles = trendTitles;
    }
}
