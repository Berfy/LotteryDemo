package cn.zcgames.lottery.model.local;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.home.bean.TrendResponseData;
import cn.zcgames.lottery.home.bean.TrendTypeData;

/**
 * 彩票走势整合后数据
 *
 * @author NorthStar
 * @date 2018/10/19 11:00
 */
public class LotteryTrendData implements Serializable {

    private String lotteryType;
    private int playType;
    private int numPos;//页面标记  区分个位-万位（1-5） 跨度0 振幅-1 基本走势-2 形态-3
    private TrendTypeData typeData;
    private String sourceType;
    List<TrendResponseData.TrendData> data;

    public LotteryTrendData(String lotteryType, int playType, TrendTypeData typeData, String sourceType) {
        this.lotteryType = lotteryType;
        this.playType = playType;
        this.typeData = typeData;
        this.sourceType = sourceType;
    }

    public LotteryTrendData(String lotteryType, int playType, TrendTypeData typeData) {
        this.lotteryType = lotteryType;
        this.playType = playType;
        this.typeData = typeData;
    }

    public LotteryTrendData(String lotteryType, int playType, TrendTypeData typeData, int numPos) {
        this.lotteryType = lotteryType;
        this.playType = playType;
        this.typeData = typeData;
        this.numPos = numPos;
    }

    public LotteryTrendData(String lotteryType, int playType, List<TrendResponseData.TrendData> data) {
        this.lotteryType = lotteryType;
        this.playType = playType;
        this.data = data;
    }

    public String getLotteryType() {
        return lotteryType == null ? "" : lotteryType;
    }

    public void setLotteryType(String mLotteryType) {
        this.lotteryType = mLotteryType;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public TrendTypeData getTypeData() {
        return typeData;
    }

    public void setTypeData(TrendTypeData typeData) {
        this.typeData = typeData;
    }

    public String getSourceType() {
        return sourceType == null ? "" : sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public List<TrendResponseData.TrendData> getData() {
        return data;
    }

    public void setData(List<TrendResponseData.TrendData> data) {
        this.data = data;
    }

    public int getNumPos() {
        return numPos;
    }

    public void setNumPos(int numPos) {
        this.numPos = numPos;
    }
}
