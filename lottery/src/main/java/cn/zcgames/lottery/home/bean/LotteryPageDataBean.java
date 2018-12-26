package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */

public class LotteryPageDataBean {

    private List<ADInfo> banners;

    private List<String> tips;

    private List<LotteryType> lotteries;

    public List<ADInfo> getBanners() {
        return banners;
    }

    public void setBanners(List<ADInfo> banners) {
        this.banners = banners;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }

    public List<LotteryType> getLotteries() {
        return lotteries;
    }

    public void setLotteries(List<LotteryType> lotteries) {
        this.lotteries = lotteries;
    }
}
