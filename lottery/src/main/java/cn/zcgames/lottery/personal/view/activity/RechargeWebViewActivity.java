package cn.zcgames.lottery.personal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_URL;

public class RechargeWebViewActivity extends BaseActivity {
    private static final String TAG = "RechargeWebViewActivity";

    @BindView(R.id.title_back)
    ImageButton titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.recharge_WebView)
    WebView rechargeWebView;

    private String mWebUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_web_view);
        ButterKnife.bind(this);
        initIntentData();
        initView();
        initWebView();
    }

    private void initWebView(){
//        rechargeWebView.loadUrl(mWebUrl);
        rechargeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                Log.e(TAG, "shouldOverrideUrlLoading: url is " + request );
                rechargeWebView.loadUrl(request);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showWaitingDialog(RechargeWebViewActivity.this, R.string.tips_loading, false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideWaitingDialog();
//                String title = mWebView.getTitle();
//                mTitleTv.setText(title);
            }
        });
    }

    private void initIntentData() {
        mWebUrl = getIntent().getStringExtra(PARAM_URL);
        Log.e(TAG, "initIntentData: mWebUrl is " +mWebUrl );
    }


    private void initView() {
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack(RechargeWebViewActivity.this);
            }
        });
    }

    /**
     * 进入此activity的入口
     *
     * @param fromActivity
     * @param url          支付链接
     */
    public static void intoThisActivity(Activity fromActivity, String url) {
        Intent i = new Intent(fromActivity, RechargeWebViewActivity.class);
        i.putExtra(PARAM_URL, url);
        fromActivity.startActivity(i);
    }
}
