package cn.zcgames.lottery.personal.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.base.UIHelper;

/**
 * 充值说明界面
 *
 * @author NorthStar
 * @date 2018/10/8 11:51
 */
public class RechargeDirectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_direction);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.recharge_instructions);
        View backIV = findViewById(R.id.title_back);
        UIHelper.showWidget(backIV, true);
        backIV.setOnClickListener(v -> goBack(RechargeDirectionActivity.this));
    }
}
