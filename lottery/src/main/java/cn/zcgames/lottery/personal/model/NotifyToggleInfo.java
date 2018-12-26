package cn.zcgames.lottery.personal.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.zcgames.lottery.home.bean.LotteryType;

/**
 * 用于存储中奖及开奖的通知开关设置
 * @author NorthStar
 * @date  2018/9/28 11:17
 */
public class NotifyToggleInfo implements Serializable {

    private boolean isOpenWin;//是否打开中奖通知

    private List<LotteryType> toggles=new ArrayList<>();//各彩种开奖通知开关状态

    public List<LotteryType> getToggles() {
        if (toggles == null) {
            return new ArrayList<>();
        }
        return toggles;
    }

    public void setToggles(List<LotteryType> toggles) {
        this.toggles = toggles;
    }

    public boolean isOpenWin() {
        return isOpenWin;
    }

    public void setOpenWin(boolean openWin) {
        isOpenWin = openWin;
    }
}
