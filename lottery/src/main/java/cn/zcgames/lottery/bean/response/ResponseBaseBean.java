package cn.zcgames.lottery.bean.response;

import android.text.TextUtils;

import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;

/**
 * Created by admin on 2017/5/18.
 */

public class ResponseBaseBean {

    private String error;
    private String msg;
    private int code;
    private String status;
    private String type;
    private String remain;
    private String income;

    public boolean isOk() {
        return HttpHelper.RESPONSE_CODE_OK == code || "true".equals(status);
    }

    //是否被冻结
    public boolean isForbidden() {
        if (code == HttpHelper.RESPONSE_CODE_FORBIDEN_USER) {
            ToastUtil.getInstances().showShort(TextUtils.isEmpty(msg)
                    ? MyApplication.getAppContext().getString(R.string.tips_request_code207) : msg);
        } else if (code == HttpHelper.RESPONSE_CODE_FORBIDEN_CHANNEL) {
            ToastUtil.getInstances().showShort(TextUtils.isEmpty(msg)
                    ? MyApplication.getAppContext().getString(R.string.tips_request_code208) : msg);
        }
        return code == HttpHelper.RESPONSE_CODE_FORBIDEN_USER || code == HttpHelper.RESPONSE_CODE_FORBIDEN_CHANNEL;
    }

    //是否被抢登
    public boolean isStolen() {
        return code == HttpHelper.RESPONSE_CODE_TOKEN_INVALID
                || code == HttpHelper.RESPONSE_CODE_STOLEN
                || HttpHelper.RESPONSE_STATUS_UN_LOGIN.equals(status);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return TextUtils.isEmpty(code == 0 ? "" : (code + "")) ? status : code + "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "Response is error : " + error
                + " , msg : " + msg
                + " , status : " + code
                + " , type : " + type
                + " , remain : " + remain
                + " , income : " + income;
    }
}
