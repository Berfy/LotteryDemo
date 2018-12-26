package cn.zcgames.lottery.utils.okhttp.interceptor;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import java.io.IOException;
import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import cn.zcgames.lottery.app.MyApplication;
import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 从本地取出保存的Cookie
 *
 * @author Berfy
 */
public class AddCookiesInterceptor implements Interceptor {

    public AddCookiesInterceptor() {
    }

    @SuppressLint("CheckResult")
    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        if (!TextUtils.isEmpty(MyApplication.getTokenId())) {
            LogF.d(Constant.HTTP_TAG, "设置header  " + MyApplication.getTokenId());
            Observable.just(MyApplication.getTokenId())
                    .subscribe(cookie -> {
                        builder.addHeader(HttpHelper.HEADER_PARAMS_AUTHORIZATION, "BEARER " + cookie);
//                        builder.addHeader(HttpHelper.HEADER_PARAMS_CHANNEL_ID, AppConstants.getChannelId());
                        LogF.d(Constant.HTTP_TAG, "从SharedPreference中获取的Cookie---" + cookie);
                    });
        } else {
            LogF.d(Constant.HTTP_TAG, "无法设置header =null");
        }
        return chain.proceed(builder.build());
    }
}
