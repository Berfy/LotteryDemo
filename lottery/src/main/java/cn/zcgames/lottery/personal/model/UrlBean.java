package cn.zcgames.lottery.personal.model;

import cn.zcgames.lottery.bean.response.ResponseBaseBean;

/**
 * 接收图片上传结果数据类
 *
 * @author NorthStar
 * @date 2018/9/1 15:54
 */
public class UrlBean extends ResponseBaseBean{

    /**
     * payload : {"url":"http://127.0.0.1:8090/account/upload/37b16293-adb7-11e8-aebf-62d470358023.png"}
     */

    private UrlData payload;

    public UrlData getPayload() {
        return payload;
    }

    public static class UrlData {
        private String url;

        public String getUrl() {
            return url == null ? "" : url;
        }
    }
}
