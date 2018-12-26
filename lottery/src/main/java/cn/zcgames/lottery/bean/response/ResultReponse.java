package cn.zcgames.lottery.bean.response;

import java.util.List;

/**
 * 开奖结果列表接口返回Bean
 * @author Berfy
 */
public class ResultReponse extends ResponseBaseBean {

    ResultBeanList payload;

    public ResultBeanList getPayload() {
        return payload;
    }

    public void setPayload(ResultBeanList payload) {
        this.payload = payload;
    }
}
