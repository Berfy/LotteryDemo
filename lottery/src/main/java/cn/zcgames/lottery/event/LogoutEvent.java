package cn.zcgames.lottery.event;

/**
 * Created by admin on 2017/4/25.
 */

public class LogoutEvent {
    boolean isLogout;

    public LogoutEvent(boolean logout) {
        this.isLogout = logout;
    }

    public boolean isLogout() {
        return isLogout;
    }
}
