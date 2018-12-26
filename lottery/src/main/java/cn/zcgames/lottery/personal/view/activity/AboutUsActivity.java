package cn.zcgames.lottery.personal.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;
import cn.zcgames.lottery.utils.SystemUtils;

/**
 * 关于我们
 *
 * @author NorthStar
 * @date 2018/8/20 17:37
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AboutUsActivity";
    @BindView(R.id.tv_version)
    TextView aboutUsTvVersion;
    @BindView(R.id.tv_url)
    TextView urlTV;
    @BindView(R.id.tv_company)
    TextView companyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText(R.string.mine_about_us);

        View backView = findViewById(R.id.title_back);
        backView.setOnClickListener(this);
        UIHelper.showWidget(backView, true);

        aboutUsTvVersion.setText("V" + SystemUtils.getAppVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack(AboutUsActivity.this);
                break;
        }
    }

}
