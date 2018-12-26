package cn.zcgames.lottery.model.remote.callback;

/**
 * Created by admin on 2017/5/10.
 */

public interface NormalCallback<T> {

    void responseOk(T msg);

    void responseFail(boolean isNeedLogin, String errorMsg);
}
