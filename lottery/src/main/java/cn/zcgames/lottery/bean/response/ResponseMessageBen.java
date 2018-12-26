package cn.zcgames.lottery.bean.response;

import cn.berfy.sdk.mvpbase.model.MessageInfo;

/**
 * 站内信息接口返回Bean
 * @author NorthStar
 * @date  2018/9/28 18:46
 */
public class ResponseMessageBen extends ResponseBaseBean {
    private MessageInfo payload;
    public MessageInfo getData() {
        return payload;
    }
}
