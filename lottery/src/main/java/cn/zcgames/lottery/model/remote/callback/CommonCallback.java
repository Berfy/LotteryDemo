package cn.zcgames.lottery.model.remote.callback;

/**
 * @author Berfy
 * 新的接口回调
 */
public interface CommonCallback<T> {

    void responseOk(T msg);

    void responseFail(int code, String errorMsg);
}
