package cn.zcgames.lottery.personal.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.berfy.sdk.mvpbase.base.ActivityManager;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.berfy.sdk.mvpbase.view.XWebView;
import cn.berfy.sdk.mvpbase.view.dialog.CommonDialog;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.MyApplication;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.bean.response.ResultRecharge;
import cn.zcgames.lottery.app.MainActivity;
import cn.zcgames.lottery.utils.GsonUtils;
import cn.zcgames.lottery.utils.IsInstallWeChatOrAliPay;
import cn.zcgames.lottery.utils.okhttp.OkHttpUtils;
import cn.zcgames.lottery.utils.okhttp.callback.GenericsCallback;
import cn.zcgames.lottery.utils.okhttp.callback.JsonGenericsSerializer;
import okhttp3.Call;

import static cn.zcgames.lottery.app.HttpHelper.PARAMS_OEDER_ID;
import static cn.zcgames.lottery.app.HttpHelper.WALLET_PAY_RESULT;

/**
 * 充值订单页
 *
 * @author NorthStar
 * @date 2018/8/30 15:24
 */
public class RechargeOrderActivity extends BaseActivity {

    @BindView(R.id.pay_webView)
    XWebView mWebView;
    @BindView(R.id.wap_title_tv)
    TextView mTitleTv;
    @BindView(R.id.wap_title_back)
    ImageButton backBtn;
    @BindView(R.id.webView_progressBar)
    ProgressBar progressBar;

    private String mTitle;
    private String mUrl;
    private String orderId;
    private int failedCount = 0;
    private CommonDialog remindDialog;//充值成功提示
    private boolean isDestroy = false;
    private int payType; //100 支付宝,200 微信

    public static final String INTENT_EXTRA_TITLE = "web_title";//标题
    public static final String INTENT_EXTRA_PAY_TYPE = "pay_type";//标题
    public static final String INTENT_EXTRA_ORDER_ID = "orderId";//购买的充值订单id
    public static final String INTENT_EXTRA_URL = "url";//链接地址
    public static final String TAG = "RechargeOrderActivity";

    public static void launcher(Activity context, String url, String title, String orderId, int payType) {
        if (TextUtils.isEmpty(url)) return;
        Intent intent = new Intent(context, RechargeOrderActivity.class);
        intent.putExtra(INTENT_EXTRA_PAY_TYPE, payType);
        intent.putExtra(INTENT_EXTRA_URL, url);
        intent.putExtra(INTENT_EXTRA_TITLE, title);
        intent.putExtra(INTENT_EXTRA_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        UIHelper.darkStatusBar(this, true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_layout);
        initIntentData();
        initView();
        loadWebView();
    }

    private void initIntentData() {
        setButterKnife(this);
        payType = getIntent().getIntExtra(INTENT_EXTRA_PAY_TYPE, 0);
        LogF.d(TAG, "payType==>" + payType);
        mTitle = getIntent().getStringExtra(INTENT_EXTRA_TITLE);
        mUrl = getIntent().getStringExtra(INTENT_EXTRA_URL);
        LogF.d(TAG, "mUrl==>" + mUrl);
        orderId = getIntent().getStringExtra(INTENT_EXTRA_ORDER_ID);
    }

    private void initView() {
        if (!TextUtils.isEmpty(mTitle)) mTitleTv.setText(mTitle);
        remindDialog = new CommonDialog(this);
        backBtn.setOnClickListener(v -> close());
    }


    /**
     * 加载webView
     */
    private void loadWebView() {
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e(TAG, "onPageStarted====" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = mWebView.getTitle();
                mTitleTv.setText(title);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "onReceivedError");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.e(TAG, "onReceivedSslError");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading url=" + url);
                return Build.VERSION.SDK_INT < 26 && load(view, url);
            }

            //是否在webview内加载页面
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                        request.getUrl().toString() : request.toString();
                LogF.d(TAG, "shouldOverrideUrlLoading--Url" + url);
                return load(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e(TAG, "onProgressChanged newProgress=" + newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }

            // FILE UPLOAD >=21
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(title)) {//传进来title了就不修改了
                    mTitle = title;
                    if (!TextUtils.isEmpty(mTitle))
                        ((TextView) findViewById(R.id.wap_title_tv)).setText(mTitle);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                return true;
            }
        });
    }

    private boolean load(WebView view, String url) {
        if (url.startsWith("http:") || url.startsWith("https:")) {
            if (payType == 100 && !IsInstallWeChatOrAliPay.checkAliPayInstalled(mContext)) {
                ToastUtil.getInstances().showLong("请安装支付宝手机客户端");
            }
            view.loadUrl(url);
            return false;
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                if (payType == 100) {
                    ToastUtil.getInstances().showLong("请安装支付宝手机客户端");
                }
                return false;
            }
        }
    }

    /**
     * 查询充值结果
     */
    public void queryPayResult() {
        if (TextUtils.isEmpty(orderId)) return;

        String tokenStr = MyApplication.getTokenId();
        if (TextUtils.isEmpty(tokenStr)) {
            UIHelper.showToast("token is null");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(PARAMS_OEDER_ID, orderId);
        String jsonParam = GsonUtils.getGsonInstance().toJson(params);
        //"status": 1.已有订单(开启轮循)  2  充值成功  其他:无订单不论循
        OkHttpUtils.postString()
                .url(WALLET_PAY_RESULT)
                .content(jsonParam)
                .build()
                .execute(new GenericsCallback<ResultRecharge>(new JsonGenericsSerializer()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogF.d(TAG, "充值第" + failedCount + "次查询失败：" + "msg: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(ResultRecharge response, int id) {
                        if (response.isOk()) {
                            if (response.getData().getStatus() == 1) {
                                loopResult(response);
                            } else if (response.getData().getStatus() == 2) {
                                failedCount = 0;
                                runOnUiThread(() -> {
                                    if (!isFinishing()) {
                                        remindDialog.showTipDialog("提示", "充值成功",
                                                "确定", (Dialog, which) -> {
                                                    remindDialog.dismiss();
                                                    ActivityManager.getInstance().popAllActivityExceptOne(MainActivity.class);
                                                });
                                    }
                                });
                            } else {
                                failedCount = 0;
                            }
                            LogF.d(TAG, "queryPayResult==>" + response.getMsg());
                        } else {
                            loopResult(response);
                        }
                    }
                });
    }

    private void loopResult(ResultRecharge response) {
        new Handler().postDelayed(() -> {
            if (!AppUtils.isBackground(RechargeOrderActivity.this)) {
                if (isDestroy) return;//本界面销毁轮循停止
                if (failedCount < 60) {//如果连续3分钟查询都失败就不查了
                    failedCount++;
                    queryPayResult();
                    LogF.d(TAG, "充值第" + failedCount + "次查询失败：" + "msg: " + response.getMsg());
                }
            }
        }, 3000);
    }

    private void close() {
        if (mWebView.close()) finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWebView) {
            mWebView.onResume();
            queryPayResult();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWebView) mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (null != mWebView) mWebView.destroy();
        isDestroy = true;
        AppUtils.releaseAllWebViewCallback();
        super.onDestroy();
    }
}
