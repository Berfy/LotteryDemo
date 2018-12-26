package cn.zcgames.lottery.home.bean;

import android.support.annotation.NonNull;

/**
 * Created by admin on 2017/4/1.
 */

public class LotteryBall implements Comparable<LotteryBall> {

    private String number;
    private String tip;
    private int type;//0:red;1:blue
    private boolean selected;

    public int getNumberInt() {
        try {
            return Integer.valueOf(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNumber(int number) {
        this.number = number + "";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public int compareTo(@NonNull LotteryBall o) {
        try {
            return Integer.valueOf(number).compareTo(Integer.valueOf(o.getNumber()));
        } catch (Exception e) {
            return 0;
        }
    }
}
