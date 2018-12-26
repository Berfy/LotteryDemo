package cn.zcgames.lottery.home.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;

import static cn.zcgames.lottery.app.ActivityConstants.PARAM_LOTTERY_TYPE;
import static cn.zcgames.lottery.app.ActivityConstants.PARAM_URL;

/**
 * 玩法说明（HTML版本）暂时不用
 * @author Berfy
 */
public class PlayIntroductionActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;
    private TextView mTitleTv;

    private String mLotteryType;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_introduction);
        initIntentData();
        initView();
        showData();
    }

    private void initIntentData() {
        mLotteryType = getIntent().getStringExtra(PARAM_LOTTERY_TYPE);
        mUrl = getIntent().getStringExtra(PARAM_URL);
    }

    private void showData() {
        mTitleTv.setText("玩法说明");
//        String filePath = "";
//        if (mLotteryType.equals(LOTTERY_TYPE_ALWAYS_COLOR)) {
//           filePath = "file:///android_asset/lottery/play_style_alwayscolor.html";
//        }
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showWaitingDialog(PlayIntroductionActivity.this, R.string.tips_loading, false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideWaitingDialog();
                String title = mWebView.getTitle();
                mTitleTv.setText(title);
            }
        });

    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.introduction_webView);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        View backView = findViewById(R.id.title_back);
        UIHelper.showWidget(backView, true);
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_back) {
            goBack(PlayIntroductionActivity.this);
        }
    }

    /**
     * 进入这个页面的入口
     *
     * @param lotteryType
     * @param fromActivity
     */
    public static void intoThisActivity(String url, String lotteryType, Activity fromActivity) {
        Intent i = new Intent(fromActivity, PlayIntroductionActivity.class);
        i.putExtra(PARAM_LOTTERY_TYPE, lotteryType);
        i.putExtra(PARAM_URL, url);
        fromActivity.startActivity(i);
    }
}
