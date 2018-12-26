package cn.zcgames.lottery.personal.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.berfy.sdk.mvpbase.util.CommonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;
import cn.berfy.sdk.mvpbase.util.ToastUtil;
import cn.zcgames.lottery.R;
import cn.zcgames.lottery.base.UIHelper;
import cn.zcgames.lottery.base.BaseActivity;

/**
 * 意见反馈界面
 *
 * @author NorthStar
 * @date 2018/8/20 17:37
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.et_feedback)
    EditText mEdtIdea;

    @BindView(R.id.tv_detail_font_count)
    TextView countTV;

    @BindView(R.id.tv_commit_feedback)
    TextView submit;

    @BindView(R.id.rg_feedback)
    RadioGroup rg;

    private String title = "";
    private boolean hasTitle=false;
    private boolean hasContent=false;
    private String content="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIHelper.hideSystemTitleBar(this);
        super.onCreate(savedInstanceState);
        initView();
        setListener();
    }

    private void initView() {
        setContentView(R.layout.activity_feedback);
        setButterKnife(this);
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.mine_feedback);
        UIHelper.showWidget(findViewById(R.id.title_back), true);
    }

    @OnClick({R.id.title_back, R.id.tv_commit_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                goBack(FeedbackActivity.this);
                break;
            case R.id.tv_commit_feedback:
                ToastUtil.getInstances().showLong("提交成功");
                goBack(FeedbackActivity.this);
                break;
        }
    }

    private void setListener() {
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton checkedRb = (RadioButton) group.getChildAt(i);
                if (checkedRb.getId() == checkedId) {
                    checkedRb.setBackground(CommonUtil.getDrawable(FeedbackActivity.this, R.drawable.shape_bg_feed));
                    title = checkedRb.getText().toString();
                } else {
                    checkedRb.setBackground(null);
                }
            }
            hasTitle = !TextUtils.isEmpty(title);
            submit.setEnabled(hasTitle && hasContent);
        });

        mEdtIdea.addTextChangedListener(new TextWatcher() {
            private String temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(temp)) {
                    editStart = mEdtIdea.getSelectionStart();
                    editEnd = mEdtIdea.getSelectionEnd();
                    if (temp.length() > 300) {
                        ToastUtil.getInstances().showLong("输入字数已超过限制!");
                        s.delete(editStart - 1, editEnd);
                        int tempSelection = editStart;
                        mEdtIdea.setText(s);
                        mEdtIdea.setSelection(tempSelection);
                        countTV.setText("300/300");
                    } else {
                        countTV.setText(String.format(Locale.CHINA, "%d/300", mEdtIdea.length()));
                    }
                }

                content = mEdtIdea.getText().toString();
                hasContent = !TextUtils.isEmpty(content);
                hasTitle = !TextUtils.isEmpty(title);
                LogF.d("feedback", "content==>" + content + ", title==>" + title);
                submit.setEnabled(hasTitle && hasContent);
            }
        });
    }

}
