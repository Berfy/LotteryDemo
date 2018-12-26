package cn.zcgames.lottery.personal.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;

public class ServicePageActivity extends BaseActivity implements View.OnClickListener {

    private TextView mContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_page);
        initView();
        showData();
    }

    private void showData() {
        InputStream is = null;
        try {
            is = getAssets().open("mine/service.txt");
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            String result = new String(buffer, "gbk");
            mContentTv.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText(R.string.mine_service);

        View backView = findViewById(R.id.title_back);
        UIHelper.showWidget(backView, true);
        backView.setOnClickListener(this);

        mContentTv = (TextView) findViewById(R.id.service_tv_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(ServicePageActivity.this);
                break;
        }
    }
}
