package cn.zcgames.lottery.home.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/11.
 */

public class DoubleColorOrderBet {

    List<String> red;
    List<String> blue;
    List<String> dan;
    List<String> tuo;
    List<String> five;
    List<String> six;
    List<String> seven;
    String mode;

    public List<String> getSix() {
        return six;
    }

    public void setSix(List<String> six) {
        this.six = six;
    }

    public List<String> getSeven() {
        return seven;
    }

    public void setSeven(List<String> seven) {
        this.seven = seven;
    }

    public List<String> getFive() {
        return five;
    }

    public void setFive(List<String> five) {
        this.five = five;
    }

    public List<String> getRed() {
        return red;
    }

    public void setRed(List<String> red) {
        this.red = red;
    }

    public List<String> getBlue() {
        return blue;
    }

    public void setBlue(List<String> blue) {
        this.blue = blue;
    }

    public List<String> getDan() {
        return dan;
    }

    public void setDan(List<String> dan) {
        this.dan = dan;
    }

    public List<String> getTuo() {
        return tuo;
    }

    public void setTuo(List<String> tuo) {
        this.tuo = tuo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
