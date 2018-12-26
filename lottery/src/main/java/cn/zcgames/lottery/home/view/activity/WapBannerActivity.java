package cn.zcgames.lottery.home.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.view.XWebView;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.app.ActivityConstants;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.home.view.activity.fastthree.FastThreeActivity;

/**
 * author: Berfy
 * date: 2018/10/13
 * 广告详情web
 */
public class WapBannerActivity extends BaseActivity {

    private final String TAG = "广告";
    private Unbinder mUnbinder;
    @BindView(R.id.webView)
    XWebView mWebView;
    @BindView(R.id.title_back)
    ImageButton mIBtnBack;
    @BindView(R.id.title_tv)
    TextView mTvTitle;

    private String mTitle;
    private String mUrl;
    private long mOpenTime = 0;

    /**
     * 进入此activity的入口
     *
     * @param title
     * @param url
     */
    public static void intoThisActivity(Context fromActivity, String title, String url) {
        Intent i = new Intent(fromActivity, WapBannerActivity.class);
        i.putExtra(ActivityConstants.PARAM_URL, url);
        i.putExtra(ActivityConstants.PARAM_ACTIVITY_TITLE, title);
        fromActivity.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHelper.hideSystemTitleBar(this);
        setContentView(R.layout.activity_web_banner_activity);
        mUnbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mOpenTime = System.currentTimeMillis();
        mTitle = getIntent().getStringExtra(ActivityConstants.PARAM_ACTIVITY_TITLE);
        mUrl = getIntent().getStringExtra(ActivityConstants.PARAM_URL);
        //处理返回事件
        mIBtnBack.setVisibility(View.VISIBLE);
        mIBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close(false);
            }
        });
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading url=" + url);
                view.loadUrl(url);
                return false;
            }

            //是否在webview内加载页面
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.e(TAG, "shouldOverrideUrlLoading url=" + request.getUrl().toString());
                    view.loadUrl(request.getUrl().toString());
                } else {
                    Log.e(TAG, "shouldOverrideUrlLoading url=" + request.toString());
                    view.loadUrl(request.toString());
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted url=" + url);
//                showWaitingDialog(WapBannerActivity.this, R.string.tips_loading, false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                hideWaitingDialog();
//                String title = mWebView.getTitle();
//                mTitleTv.setText(title);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "onReceivedError");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e(TAG, "标题=" + title);
                if (null != mTvTitle && !TextUtils.isEmpty(title)) {
                    mTvTitle.setText(title);
                }
                super.onReceivedTitle(view, title);
            }
        });
        if (!TextUtils.isEmpty(mUrl))
            mWebView.loadUrl(mUrl);
    }

    /**
     * 是否一步步返回
     *
     * @param isCanNextBack true 按照历史页面退出 false直接退出
     */
    private void close(Boolean isCanNextBack) {
        LogF.d(TAG, "退出  isCanNextBack=" + isCanNextBack + "  mWebView.canGoBack()=" + mWebView.canGoBack());
        if (!isCanNextBack || !mWebView.canGoBack()) {
            if (System.currentTimeMillis() - mOpenTime > 500) {
                finish();
            }
        } else {
            mWebView.goBack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
        if (null != mWebView) {
            mWebView.destroy();
        }
    }
}
