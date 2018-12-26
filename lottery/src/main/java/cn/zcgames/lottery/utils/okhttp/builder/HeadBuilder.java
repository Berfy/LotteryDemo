package cn.zcgames.lottery.utils.okhttp.builder;

import cn.zcgames.lottery.utils.okhttp.OkHttpUtils;
import cn.zcgames.lottery.utils.okhttp.request.OtherRequest;
import cn.zcgames.lottery.utils.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null,
                OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
