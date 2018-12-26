package cn.zcgames.lottery.utils.okhttp.callback;

import android.util.Log;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import cn.berfy.sdk.mvpbase.config.Constant;
import cn.zcgames.lottery.app.AppConstants;
import okhttp3.Response;

/**
 * Created by JimGong on 2016/6/23.
 */

public abstract class GenericsCallback<T> extends Callback<T> {

    private static final String TAG = "GenericsCallback";

    IGenericsSerializer mGenericsSerializer;

    public GenericsCallback(IGenericsSerializer serializer) {
        mGenericsSerializer = serializer;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();

        if (Constant.DEBUG) {
            Log.e(TAG, "parseNetworkResponse: response is " + string);
        }

        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (entityClass == String.class) {
            return (T) string;
        }
        T bean = mGenericsSerializer.transform(string, entityClass);
        return bean;
    }

}
