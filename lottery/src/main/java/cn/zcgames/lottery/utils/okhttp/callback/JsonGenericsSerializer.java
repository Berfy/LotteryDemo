package cn.zcgames.lottery.utils.okhttp.callback;

import cn.zcgames.lottery.utils.GsonUtils;

/**
 * Created by admin on 2016/6/23.
 */
public class JsonGenericsSerializer implements IGenericsSerializer {

    @Override
    public <T> T transform(String response, Class<T> classOfT) {
        return GsonUtils.getGsonInstance().fromJson(response, classOfT);
    }
}
