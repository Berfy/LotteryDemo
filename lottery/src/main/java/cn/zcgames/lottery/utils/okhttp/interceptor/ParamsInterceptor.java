package cn.zcgames.lottery.utils.okhttp.interceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cn.berfy.sdk.mvpbase.util.Hmac;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.app.AppConstants;
import cn.zcgames.lottery.app.HttpHelper;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Berfy on 2017/12/19.
 * 签名
 */
public class ParamsInterceptor implements Interceptor {

    private String TAG = "ParamsInterceptor";

    public ParamsInterceptor(String tag) {
        TAG = tag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if ("GET".equals(request.method())) {
            request = addGetParams(request);
        } else if ("POST".equals(request.method())) {
            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;
            if (!hasRequestBody) {
                LogF.d(TAG, "------" + request.method());
            } else if (bodyEncoded(request.headers())) {
                LogF.d(TAG, "------" + request.method() + " (encoded body omitted)");
            } else {
                request = addPostParams(request);
            }
        }
        return chain.proceed(request);
    }

    private static Request addGetParams(Request request) throws UnsupportedEncodingException {
        //添加时间戳
//        HttpUrl httpUrl = request.url()
//                .newBuilder()
//                .addQueryParameter(HttpHelper.HEADER_PARAMS_CHANNEL_ID, AppConstants.getChannelId())
//                .build();
        request = request.newBuilder()
//                .url(httpUrl)
                .addHeader(HttpHelper.HEADER_PARAMS_CHANNEL_ID, AppConstants.getChannelId())
                .build();
        return request;
    }

    private Request addPostParams(Request request) throws IOException {
        Buffer buffer = new Buffer();
        LogF.d(TAG, "拦截post" + request.body().contentType().toString());
        if (null == request.body().contentType()) {
            return request;
        }
        //只拦截form body是json的请求
        if (request.body().contentType().toString().contains("text/plain")) {
            request.body().writeTo(buffer);
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(new String(buffer.readByteArray(), "UTF-8"));
//                jsonObject.put(HttpHelper.HEADER_PARAMS_CHANNEL_ID, AppConstants.getChannelId());
//            } catch (JSONException e) {
//                try {
//                    jsonObject = new JSONObject();
//                    jsonObject.put(HttpHelper.HEADER_PARAMS_CHANNEL_ID, AppConstants.getChannelId());
//                } catch (JSONException e1) {
//                }
//            }
            //添加sign_ts
//            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            //md5签名
            String sign = Hmac.md5(buffer.readByteArray());
            request = request.newBuilder()
//                    .post(requestBody)
                    .addHeader(HttpHelper.HEADER_PARAMS_CHANNEL_ID, AppConstants.getChannelId())
                    .build();
        }
        return request;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
