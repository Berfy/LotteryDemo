package cn.zcgames.lottery.home.view.activity.fastthree;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;

/**
 * 快3玩法说明
 * Berfy修改
 * 2018.8.23
 */
public class FastThreeDescriptionActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_fast_three_description);
        initView();
    }

    private void initView() {
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText(R.string.mine_pay_description);

        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        UIHelper.showWidget(backView, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(FastThreeDescriptionActivity.this);
                break;
        }
    }
}
