package cn.zcgames.lottery.bean.response;

/**
 * Created by admin on 2017/5/18.
 */

public class ResponseStringBean extends ResponseBaseBean{

    private String payload;

    public String getData() {
        return payload;
    }

    public void setData(String data) {
        this.payload = data;
    }
}
