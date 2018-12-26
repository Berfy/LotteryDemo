package cn.zcgames.lottery.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by admin on 2017/4/12.
 */
@Entity
public class LotteryOrder {

    @Id(autoincrement = true)
    private Long id;
    private String redBall;//红球
    private String blueBall;//蓝球
    private String danBall;//胆球
    private String tuoBall;//拖球
    private String fiveBall;//
    private String sixBall;//
    private String sevenBall;//
    private Integer playMode;
    private Long totalCount;
    private String totalMoney;
    private Integer orderType;
    private String lotteryType;

    @Keep
    public LotteryOrder(Long id, String redBall, String blueBall, String danBall,
                        String tuoBall, String fiveBall, String sixBall, String sevenBall,
                        Integer playMode, Long totalCount, String totalMoney, Integer orderType,
                        String lotteryType) {
        this.id = id;
        this.redBall = redBall;
        this.blueBall = blueBall;
        this.danBall = danBall;
        this.tuoBall = tuoBall;
        this.fiveBall = fiveBall;
        this.sixBall = sixBall;
        this.sevenBall = sevenBall;
        this.playMode = playMode;
        this.totalCount = totalCount;
        this.totalMoney = totalMoney;
        this.orderType = orderType;
        this.lotteryType = lotteryType;
    }

    @Generated(hash = 276421452)
    public LotteryOrder() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRedBall() {
        return this.redBall;
    }

    public void setRedBall(String redBall) {
        this.redBall = redBall;
    }

    public String getBlueBall() {
        return this.blueBall;
    }

    public void setBlueBall(String blueBall) {
        this.blueBall = blueBall;
    }

    public String getDanBall() {
        return this.danBall;
    }

    public void setDanBall(String danBall) {
        this.danBall = danBall;
    }

    public String getTuoBall() {
        return this.tuoBall;
    }

    public void setTuoBall(String tuoBall) {
        this.tuoBall = tuoBall;
    }

    public Integer getPlayMode() {
        return this.playMode;
    }

    public void setPlayMode(Integer playMode) {
        this.playMode = playMode;
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalMoney() {
        return this.totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getOrderType() {
        return this.orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getLotteryType() {
        return this.lotteryType;
    }

    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType;
    }

    public String getFiveBall() {
        return this.fiveBall;
    }

    public void setFiveBall(String fiveBall) {
        this.fiveBall = fiveBall;
    }

    public String getSixBall() {
        return this.sixBall;
    }

    public void setSixBall(String sixBall) {
        this.sixBall = sixBall;
    }

    public String getSevenBall() {
        return this.sevenBall;
    }

    public void setSevenBall(String sevenBall) {
        this.sevenBall = sevenBall;
    }
}
