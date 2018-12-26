package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.personal.model.UserBean;

/**
 * 网络请求的用户数据
 * @author NorthStar
 * @date  2018/9/1 14:53
 */
public class ResponseUserData extends ResponseBaseBean{
    private UserBean payload;

    public UserBean getUser() {
        return payload;
    }
}
