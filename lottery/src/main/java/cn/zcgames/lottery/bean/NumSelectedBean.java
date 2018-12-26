package cn.zcgames.lottery.bean;

import java.io.Serializable;

public class NumSelectedBean implements Serializable{
    private String num;
    private boolean selected;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
