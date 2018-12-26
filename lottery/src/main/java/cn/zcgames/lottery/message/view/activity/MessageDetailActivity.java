package cn.zcgames.lottery.message.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.berfy.sdk.mvpbase.model.MessageBean;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.TimeUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;

public class MessageDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String MSG_KEY = "msg_bean";
    public static final String TAG = "MessageDetailActivity";
    private MessageBean msg;
    @BindView(R.id.tv_title)
    TextView titleTV;
    @BindView(R.id.tv_date)
    TextView dateTV;
    @BindView(R.id.tv_content)
    TextView contentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        setButterKnife(this);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_tv)).setText("消息详情");
        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        UIHelper.showWidget(backView, true);
    }

    private void initData() {
        msg = (MessageBean) getIntent().getSerializableExtra(MSG_KEY);
        if (msg == null) return;
        if (!TextUtils.isEmpty(msg.getTitle())) titleTV.setText(msg.getTitle());
        if (!TextUtils.isEmpty(msg.getBody())) contentTV.setText(msg.getBody());
        long created = msg.getCreated();
        if (created != 0) {
            String time = TimeUtil.timestampConvertDate(" yyyy-MM-dd HH:mm:ss", created);
            dateTV.setText(time);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(MessageDetailActivity.this);
                break;
        }
    }


    /**
     * 其他activity进入此activity的入口
     *
     * @param fromActivity 发送请求的activity
     * @param msgBean      消息内容体
     */
    public static void inToThisActivity(Activity fromActivity, MessageBean msgBean) {

        if (msgBean == null) {
            LogF.d(TAG, "msg no find");
            return;
        }
        Intent i = new Intent(fromActivity, MessageDetailActivity.class);
        i.putExtra(MSG_KEY, msgBean);
        fromActivity.startActivity(i);
    }
}
