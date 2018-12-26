package cn.zcgames.lottery.utils.okhttp.interceptor;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import cn.berfy.sdk.mvpbase.util.LogF;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by zhy on 16/3/1.
 */
public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LoggerInterceptor(String tag) {
        this(tag, false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            LogF.e(tag, "○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○HTTP请求--开始○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○");
            LogF.e(tag, "○ method : " + request.method());
            LogF.e(tag, "○ url : " + url);
            if (headers != null && headers.size() > 0) {
                LogF.e(tag, "○ headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    LogF.e(tag, "○ requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        LogF.e(tag, "○ requestBody's content : " + bodyToString(request));
                    } else {
                        LogF.e(tag, "○ requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            LogF.e(tag, "○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○HTTP请求--结束○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○○");
        } catch (Exception e) {
            //            e.printStackTrace();
        }
    }

    private Response logForResponse(Response response) {
        try {
            //===>response log
            LogF.e(tag, "●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●HTTP返回--开始●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            LogF.e(tag, "● url : " + clone.request().url());
            LogF.e(tag, "● code : " + clone.code());
            LogF.e(tag, "● protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                LogF.e(tag, "● message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        LogF.e(tag, "● responseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            LogF.e(tag, "● responseBody's content : " + resp);

                            body = ResponseBody.create(mediaType, resp);
                            LogF.e(tag, "●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●HTTP返回--结束●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●====");
                            return response.newBuilder().body(body).build();
                        } else {
                            LogF.e(tag, "● responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }
        } catch (Exception e) {
            //            e.printStackTrace();
        }
        LogF.e(tag, "●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●HTTP返回--结束●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●●====");
        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml"))
                return true;

        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
