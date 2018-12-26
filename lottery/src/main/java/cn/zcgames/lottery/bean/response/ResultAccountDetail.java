package cn.zcgames.lottery.bean.response;

import cn.zcgames.lottery.personal.model.AccountDetail;

/**
 * 账户明细数据实体类
 *
 * @author NorthStar
 * @date 2018/9/7 16:01
 */
public class ResultAccountDetail extends ResponseBaseBean {
    AccountDetail payload;

    public AccountDetail getData() {
        return payload;
    }

    public void setData(AccountDetail data) {
        this.payload = data;
    }
}
