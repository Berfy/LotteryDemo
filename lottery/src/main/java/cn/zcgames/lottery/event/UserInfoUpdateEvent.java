package cn.zcgames.lottery.event;

import cn.zcgames.lottery.personal.model.UserBean;

/**
 * Created by admin on 2017/4/17.
 */

public class UserInfoUpdateEvent {

    private UserBean userBean;

    public UserInfoUpdateEvent(UserBean user) {
        this.userBean = user;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
