package cn.zcgames.lottery.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.personal.model.UserBean;

/**
 * 引导页
 * Berfy修改
 * 2018.8.23
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "SplashActivity";
    private static final int MSG_UPDATE_TIME = 0;
    private static final int MSG_GOTO_MAINACTIVITY = 1;

    private UserBean mCurrUser;

    private LinearLayout mSkipLl;
    private TextView mTimeTv;

    private int mTotalCount = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mHandler == null) {
                return;
            }
            switch (msg.what) {
                case MSG_GOTO_MAINACTIVITY:
                    SplashActivity.this.finish();
                    if (mCurrUser != null) {
                        // 做自动登录

                        //                    } else {
                        //                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    break;
                case MSG_UPDATE_TIME:
                    mTimeTv.setText(mTotalCount + "S跳过");
                    LogF.d(TAG, "倒计时" + mTotalCount);
                    if (mTotalCount > 0) {
                        mTotalCount--;
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000);
                    } else {
                        mHandler.sendEmptyMessage(MSG_GOTO_MAINACTIVITY);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogF.d("App启动时间", (System.currentTimeMillis() - Constant.APP_START_TIME) + "ms");
        UIHelper.hideSystemTitleBar(this);
        setContentView(R.layout.activity_splash);
        mCurrUser = MyApplication.getCurrLoginUser();
        initView();
        mHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    private void initView() {
        mSkipLl = findViewById(R.id.splash_ll_skip);
        mSkipLl.setOnClickListener(this);
        mTimeTv = findViewById(R.id.splash_tv_skip);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.splash_ll_skip) {
            if (mHandler != null && mHandler.hasMessages(MSG_GOTO_MAINACTIVITY)) {
                mHandler.removeMessages(MSG_GOTO_MAINACTIVITY);
            }
            if (mHandler != null) {
                mHandler.sendEmptyMessage(MSG_GOTO_MAINACTIVITY);
            }
        }
    }
}
