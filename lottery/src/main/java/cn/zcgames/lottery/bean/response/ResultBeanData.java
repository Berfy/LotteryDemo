package cn.zcgames.lottery.bean.response;

import java.util.List;

/**
 * @author Berfy
 * 开奖记过列表返回值
 * */
public class ResultBeanData {

    List<ResultPageDataBean> payload;

    public List<ResultPageDataBean> getPayload() {
        return payload;
    }

    public void setPayload(List<ResultPageDataBean> payload) {
        this.payload = payload;
    }
}
