package cn.zcgames.lottery.bean.response;

import java.util.List;

import cn.zcgames.lottery.bean.BankCardBean;

/**
 * Created by admin on 2017/6/12.
 */

public class ResultBankCard extends ResponseBaseBean {

    private List<BankCardBean> data;

    public List<BankCardBean> getData() {
        return data;
    }

    public void setData(List<BankCardBean> data) {
        this.data = data;
    }
}
