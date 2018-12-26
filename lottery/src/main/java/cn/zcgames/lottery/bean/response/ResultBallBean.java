package cn.zcgames.lottery.bean.response;

import java.util.List;

/**
 * 开奖结果列表接口返回  DrawNumbers Bean
 *
 * @author Berfy
 */
public class ResultBallBean {
    String color;
    String[] numbers;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String[] getValue() {
        return numbers;
    }

    public void setValue(String[] value) {
        this.numbers = value;
    }
}
